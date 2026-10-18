#!/usr/bin/env bash
# What CI runs (.github/workflows/ci.yml). It works without Diamondrush.jar:
#   1. syntax checks of the shell scripts and the JavaScript
#   2. builds dist/ from game/src: runtime + game source compiled together, then TeaVM (no game resources)
#   3. compiles the tools and the difftest harness
# The tests that need the original game (tools/difftest/difftest.sh, npm test) run locally only.
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

echo "==> Shell syntax"
for f in *.sh tools/*.sh tools/difftest/*.sh; do
    bash -n "$f"
done

echo "==> JavaScript syntax"
for f in web/*.js test/*.mjs; do
    node --check "$f"
done

export ALLOW_NO_JAR=1
SOURCE=1 ./build.sh
test -s dist/classes.js

source "$ROOT/tools/env.sh"
echo "==> Compiling tools"
OUT="$ROOT/build/ci-tools"
for f in tools/src/*.java tools/difftest/*.java; do
    javac -nowarn -encoding UTF-8 -cp "$TOOLCP" -d "$OUT/$(basename "$f" .java)" "$f"
done
RT="$ROOT/runtime/src"
javac -nowarn --release 11 -encoding UTF-8 -d "$OUT/harness" \
    $(find "$ROOT/tools/difftest/src" -name '*.java') \
    "$RT/dr/web/Png.java" \
    "$RT"/javax/microedition/rms/*.java \
    "$RT/javax/microedition/midlet/MIDletStateChangeException.java" \
    "$RT"/javax/microedition/media/{Player,PlayerListener,Control,Controllable,MediaException}.java \
    "$RT/javax/microedition/media/control/VolumeControl.java"

echo "==> CI checks passed"
