#!/usr/bin/env bash
# Differential test: does another build of the game behave exactly like the original Diamondrush.jar?
#
#   tools/difftest/difftest.sh <classes-dir-or-jar> [ticks] [seed ...]
#
# Both builds run headlessly on a virtual clock with the same seeded key presses. Every drawing and platform
# call is hashed per tick; the logs must match line for line. Defaults: 20000 ticks (about 17 minutes of
# play), seeds 1 2 3. Besides numbers, a seed can name a scenario: anaconda, knight, yeti or ending
# (see src/difftest/Scenario.java).
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/../.." && pwd)"
if [ $# -lt 1 ]; then
    echo "usage: $0 <classes-dir-or-jar> [ticks] [seed ...]" >&2
    exit 2
fi
source "$ROOT/tools/env.sh"
CANDIDATE="$1"
TICKS="${2:-20000}"
shift $(( $# > 1 ? 2 : 1 ))
SEEDS=("$@")
if [ ${#SEEDS[@]} -eq 0 ]; then
    SEEDS=(1 2 3)
fi

WORK="$ROOT/build/difftest"
rm -rf "$WORK"
mkdir -p "$WORK/harness" "$WORK/original" "$WORK/candidate" "$WORK/logs"

RT="$ROOT/runtime/src"
javac -nowarn --release 11 -encoding UTF-8 -d "$WORK/harness" \
    $(find "$ROOT/tools/difftest/src" -name '*.java') \
    "$RT/dr/web/Png.java" \
    "$RT"/javax/microedition/rms/*.java \
    "$RT/javax/microedition/midlet/MIDletStateChangeException.java" \
    "$RT"/javax/microedition/media/{Player,PlayerListener,Control,Controllable,MediaException}.java \
    "$RT/javax/microedition/media/control/VolumeControl.java"

java -cp "$TOOLCP" "$ROOT/tools/difftest/Prep.java" "$JAR" "$WORK/original" > /dev/null
java -cp "$TOOLCP" "$ROOT/tools/difftest/Prep.java" "$CANDIDATE" "$WORK/candidate" > /dev/null

run() { # build seed
    java -Xss8m -Ddifftest.jar="$JAR" \
        -cp "$WORK/harness:$WORK/$1:$JAR" \
        difftest.Harness "$TICKS" "$2" "$WORK/logs/$1-$2.log" > "$WORK/logs/$1-$2.out" 2> "$WORK/logs/$1-$2.err" || true
}

status=0
for seed in "${SEEDS[@]}"; do
    run original "$seed" &
    run candidate "$seed" &
    wait
    a="$WORK/logs/original-$seed.log"
    b="$WORK/logs/candidate-$seed.log"
    lines=$(wc -l < "$a" | tr -d ' ')
    if cmp -s "$a" "$b"; then
        echo "seed $seed: identical ($lines lines, $(tail -1 "$a"))"
    else
        status=1
        first=$(diff "$a" "$b" | head -1)
        echo "seed $seed: DIFFERENT at $first"
        diff "$a" "$b" | head -6 | sed 's/^/    /'
    fi
done
# Coverage of the original's runs: game states seen, (world, stage) pairs actually played (state 1) and
# ticks spent in each boss stage.
cat "$WORK"/logs/original-*.log | awk '$1 ~ /^[0-9]+$/ {
        states[$2]++
        if ($2 == 1) stages[$3 "-" $4]++
    }
    END {
        printf "states visited:"; for (s = 0; s < 64; s++) if (s in states) printf " %d", s; print ""
        printf "stages played (world-stage):"
        for (w = 0; w < 3; w++) for (s = 0; s < 32; s++) if ((w "-" s) in stages) printf " %d-%d", w, s
        print ""
        printf "boss stage ticks: Anaconda (0-8) %d, Teutonic Knight (1-9) %d, Yeti (2-10) %d\n", \
            stages["0-8"], stages["1-9"], stages["2-10"]
    }'
exit $status
