#!/usr/bin/env bash
# Builds the browser version of Diamond Rush into dist/.
#
#   1. Resolve the toolchain (TeaVM, ASM) with a local Maven copy in tools/.
#   2. Rewrite the MIDlet bytecode for TeaVM (tools/src/Rewrite.java).
#   3. Compile the MIDP-on-HTML5 runtime (runtime/src) against the rewritten game classes.
#   4. Translate runtime + game bytecode to JavaScript with TeaVM.
#   5. Copy the page, the JS host and the original JAR (its resources are read at runtime).
#
# Requires JDK 17+ and network access on the first run (Maven Central).
# Set DEBUG=1 for unminified output. By default the game code comes from the original JAR; SOURCE=1 builds
# it from the recompilable source in game/src, DEOBF=1 from the renamed bytecode (see deobfuscate.sh).
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
BUILD="$ROOT/build"
DIST="$ROOT/dist"
source "$ROOT/tools/env.sh"

rm -rf "$BUILD" "$DIST"
mkdir -p "$BUILD/game-classes" "$BUILD/runtime-classes" "$DIST"

GAME_JAR="$JAR"
if [ "${SOURCE:-0}" = "1" ]; then
    # Build from the recompilable source in game/src. It needs the MIDP API to compile and the runtime's
    # Launcher needs the game, so both are compiled together; only the game classes are kept.
    echo "==> Compiling game source (game/src)"
    javac --release 11 -nowarn -encoding UTF-8 -cp "$TOOLCP" -d "$BUILD/game-src-all" \
        $(find "$ROOT/runtime/src" "$ROOT/game/src" -name '*.java')
    mkdir -p "$BUILD/game-src-classes"
    for f in "$ROOT"/game/src/*.java; do
        cp "$BUILD/game-src-all/$(basename "$f" .java)"*.class "$BUILD/game-src-classes/"
    done
    (cd "$BUILD/game-src-classes" && jar cf "$BUILD/game-src.jar" *.class)
    GAME_JAR="$BUILD/game-src.jar"
elif [ "${DEOBF:-0}" = "1" ]; then
    # Build from the renamed classes instead; the game must behave identically.
    echo "==> Deobfuscating game classes"
    "$ROOT/deobfuscate.sh" > /dev/null
    GAME_JAR="$BUILD/Diamondrush-deobf.jar"
fi

echo "==> Rewriting game bytecode"
java -cp "$TOOLCP" "$ROOT/tools/src/Rewrite.java" "$GAME_JAR" "$BUILD/game-classes"

echo "==> Compiling MIDP runtime"
javac --release 11 -nowarn -encoding UTF-8 \
    -cp "$TOOLCP:$BUILD/game-classes" \
    -d "$BUILD/runtime-classes" \
    $(find "$ROOT/runtime/src" -name '*.java')

echo "==> Translating to JavaScript (TeaVM)"
TEAVM_FLAGS=(-O 2 -m)
if [ "${DEBUG:-0}" = "1" ]; then
    TEAVM_FLAGS=(-O 1 -g)
fi
# TeaVM resolves program classes (including its own Java class library) only from -p entries.
PROGRAM_CP=(-p "$BUILD/runtime-classes" -p "$BUILD/game-classes")
IFS=':' read -r -a TOOL_JARS <<< "$TOOLCP"
for jar in "${TOOL_JARS[@]}"; do
    case "$(basename "$jar")" in
        teavm-classlib-*|teavm-jso-*|teavm-platform-*|teavm-interop-*|teavm-core-*|teavm-metaprogramming-*|jzlib-*|joda-time-*)
            PROGRAM_CP+=(-p "$jar") ;;
    esac
done
java -Xss16m -cp "$TOOLCP" org.teavm.cli.TeaVMRunner \
    -t js --js-module-type none "${TEAVM_FLAGS[@]}" \
    "${PROGRAM_CP[@]}" \
    -d "$DIST" -f classes.js \
    Launcher

echo "==> Copying web assets"
cp "$ROOT/web/"* "$DIST/"
cp "$JAR" "$DIST/Diamondrush.jar"
unzip -p "$JAR" icon.png > "$DIST/icon.png"

echo "==> Done: $DIST (serve it over HTTP, e.g. ./serve.sh)"
