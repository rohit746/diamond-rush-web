#!/usr/bin/env bash
# Renames the game's obfuscated symbols (mappings/diamondrush.map) and decompiles the result (Vineflower) into decompiled/.
#   build/Diamondrush-deobf.jar   renamed classes (DEOBF=1 ./build.sh runs the web build from these)
#   decompiled/*.java             readable decompilation
#   mappings/SYMBOLS.md           obfuscated -> readable name table
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
source "$ROOT/tools/env.sh"
VINEFLOWER_VERSION=1.12.0
VINEFLOWER="$ROOT/tools/vineflower-$VINEFLOWER_VERSION.jar"
if [ ! -f "$VINEFLOWER" ]; then
    curl -sfL -o "$VINEFLOWER" \
        "https://repo1.maven.org/maven2/org/vineflower/vineflower/$VINEFLOWER_VERSION/vineflower-$VINEFLOWER_VERSION.jar"
fi

mkdir -p "$ROOT/build"
java -cp "$TOOLCP" "$ROOT/tools/src/Deobfuscate.java" \
    "$JAR" "$ROOT/mappings/diamondrush.map" "$ROOT/build/Diamondrush-deobf.jar" "$ROOT/mappings/SYMBOLS.md"

rm -rf "$ROOT/decompiled"
mkdir -p "$ROOT/decompiled"
# -jvn: name locals after their types (JAD style); -dgs: decompile generic signatures.
java -jar "$VINEFLOWER" -jvn=1 -dgs=1 -log=WARN "$ROOT/build/Diamondrush-deobf.jar" "$ROOT/decompiled"
echo "Decompiled sources: $ROOT/decompiled"
