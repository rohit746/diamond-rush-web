#!/usr/bin/env bash
# Shared setup, sourced by build.sh, deobfuscate.sh and tools/difftest/difftest.sh (which set ROOT first).
#   - checks that your Diamondrush.jar is present, and warns if it is not the build this project targets
#     (ALLOW_NO_JAR=1 continues without it, for CI: only SOURCE=1 ./build.sh works then)
#   - fetches a private Maven into tools/ and resolves TeaVM + ASM once (tools/deps/pom.xml)
#   - exports JAR, HAVE_JAR (1 or 0) and TOOLCP

JAR="$ROOT/Diamondrush.jar"
EXPECTED_JAR_SHA256=1a27a721a4440e8bdf63aa456732d71cb4e0cbd00054899dd233b0f69fd96b00

HAVE_JAR=1
if [ ! -f "$JAR" ]; then
    if [ "${ALLOW_NO_JAR:-0}" = "1" ]; then
        echo "note: no Diamondrush.jar; continuing without it (ALLOW_NO_JAR=1)" >&2
        HAVE_JAR=0
    else
        echo "Diamondrush.jar not found at $JAR" >&2
        echo "Copy your own Diamond Rush JAR (Nokia build, MIDlet-Version 1.1.6) there; it is not distributed." >&2
        exit 1
    fi
fi
if [ "$HAVE_JAR" = "1" ]; then
    if command -v shasum > /dev/null; then
        actual_sha=$(shasum -a 256 "$JAR" | cut -d' ' -f1)
    else
        actual_sha=$(sha256sum "$JAR" | cut -d' ' -f1)
    fi
    if [ "$actual_sha" != "$EXPECTED_JAR_SHA256" ]; then
        echo "warning: Diamondrush.jar has SHA-256 $actual_sha," >&2
        echo "         expected $EXPECTED_JAR_SHA256 (Nokia 1.1.6). Other builds may not match the symbol map or game/src." >&2
    fi
fi

MAVEN_VERSION=3.9.9
MVN="$ROOT/tools/apache-maven-$MAVEN_VERSION/bin/mvn"
if [ ! -x "$MVN" ]; then
    echo "==> Fetching Maven $MAVEN_VERSION into tools/"
    curl -sfL "https://repo1.maven.org/maven2/org/apache/maven/apache-maven/$MAVEN_VERSION/apache-maven-$MAVEN_VERSION-bin.tar.gz" \
        | tar xz -C "$ROOT/tools"
fi
CLASSPATH_FILE="$ROOT/tools/deps/classpath.txt"
if [ ! -f "$CLASSPATH_FILE" ] || [ "$ROOT/tools/deps/pom.xml" -nt "$CLASSPATH_FILE" ]; then
    echo "==> Resolving TeaVM and ASM"
    (cd "$ROOT/tools/deps" && "$MVN" -q dependency:build-classpath -Dmdep.outputFile=classpath.txt)
fi
TOOLCP="$(cat "$CLASSPATH_FILE")"
