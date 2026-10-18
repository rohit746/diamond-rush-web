# Diamond Rush in the browser

Gameloft's *Diamond Rush* (2006, the Nokia J2ME build) running unmodified in a web browser, plus the tools
used to take it apart: a symbol map that names the obfuscated code, recompilable Java source, and a
differential tester that proves the recompiled game behaves exactly like the original.

- **Runtime.** A small MIDP 2.0 implementation (`runtime/`) maps the phone APIs onto HTML5 canvas, Web
  Audio and localStorage.
- **Compilation.** The game's own bytecode is compiled to JavaScript with [TeaVM](https://teavm.org).
- **Sound.** The game's MIDI music and effects play through a small General MIDI synthesizer written for
  this project.

> **You need your own copy of the game.** The JAR, its graphics, sound and levels are Gameloft's and are
> not in this repository. `game/src/` contains decompiled Gameloft code and is not covered by this
> project's license; see [game/src/NOTICE.md](game/src/NOTICE.md). This project is not affiliated with or
> endorsed by Gameloft.

## Quick start

Requirements: JDK 17 or later, `curl` and `unzip`. Network access is needed on the first build, which
fetches Maven, TeaVM and ASM from Maven Central into `tools/`.

1. Copy your `Diamondrush.jar` (Nokia build, MIDlet-Version 1.1.6) into the repository root.
2. Build and serve:

   ```sh
   ./build.sh        # game code from the original JAR
   ./serve.sh        # http://localhost:8000
   ```

`dist/` is a static site of about 830 KB. Host it anywhere that serves HTTP; it won't work from a
`file://` URL because the page fetches the JAR at runtime.

Build options:

- `SOURCE=1 ./build.sh` builds the game from the recompilable source in `game/src/`.
- `DEOBF=1 ./build.sh` builds it from the renamed bytecode.
- `DEBUG=1` gives unminified output.

### Controls

| Key | Action |
| --- | --- |
| Arrows / WASD | Move |
| Enter / Space | OK (fire, use the weapon) |
| `Q` / `P` | Left soft key (menu, pause, select) |
| `Esc` | Right soft key (back) |
| `0`–`9`, `*`, `#` | Phone keypad |
| `M` | Mute |
| `F` | Fullscreen |

- **Touch devices** get an on-screen keypad, which the Keypad button also shows on desktop.
- **Language:** add `?lang=fr`, `es-US`, `pt-BR` or `nl-NL` to the URL. Otherwise the browser's
  language picks one, falling back to English.
- **Cheat menu:** type `#` `4` `7` `7` `2` during play to open Gameloft's hidden developer menu.

## Repository layout

| Path | What it is |
| --- | --- |
| `runtime/src/javax/microedition/` | The MIDP 2.0 / MMAPI / RMS subset the game uses |
| `runtime/src/dr/web/` | Browser bridge (`Host`), JAR resources, PNG decoder, MIDI `Player` |
| `runtime/src/Launcher.java` | Entry point: loads the JAR's resources and starts the MIDlet |
| `web/` | `index.html`, `host.js` (canvas, input, storage) and `synth.js` (MIDI synthesizer) |
| `build.sh`, `serve.sh` | Build `dist/` and serve it locally |
| `tools/env.sh` | Shared setup: checks the JAR and fetches the toolchain |
| `tools/src/Rewrite.java` | Build-time bytecode pass that makes the game TeaVM-friendly |
| `mappings/diamondrush.map` | Hand-written symbol map: obfuscated name to readable name, with notes |
| `mappings/SYMBOLS.md` | Generated table of every class, field and method and its name |
| `deobfuscate.sh`, `tools/src/Deobfuscate.java` | Apply the map and decompile into `decompiled/` |
| `tools/src/Xref.java` | Cross-reference report (callers, callees, field reads/writes, strings) |
| `game/src/` | The decompilation, fixed so it compiles and behaves like the original (Gameloft's code) |
| `tools/difftest/` | Differential tester: the original JAR against another build, frame by frame |
| `test/smoke.mjs` | Browser smoke test |

`build/`, `dist/`, `decompiled/` and the downloaded tools are generated and ignored by git.

## How it works

**What the game needs.** Its constant pools reference only standard MIDP 2.0 APIs, nothing
Nokia-specific: `Canvas`, `Graphics`, `Image`, `Font`, `Display`, MMAPI `Player` for MIDI, `RecordStore`
and `MIDlet`. The screen is 320×240 landscape. The game checks that `microedition.platform` matches the
manifest's `Nokia-Platform: *` and calls `System.exit` otherwise, so the runtime reports a Nokia E71.

**Bytecode rewrite.** ProGuard gave the same name to several fields that differ only by type: class `i`
has fields called `a` of type `String`, `byte`, `int`, `Image` and more. The JVM tells fields apart by name
and type, but TeaVM uses the name alone. `tools/src/Rewrite.java` therefore renames every field to
`name$descriptor`, and redirects `Class.getResourceAsStream`, `System.getProperty` and `System.exit` into
the runtime.

**Threads.** TeaVM runs Java threads as cooperative green threads on the browser's single thread.
- **Event thread.** Browser callbacks only queue key and audio events. A MIDP event thread (`Display`)
  drains them once per animation frame and is the only thread that calls back into the game, so `paint`
  always runs between game ticks, as on the phone.
- **Pausing.** The game's `hideNotify` path busy-waits for `showNotify`, which would hang a cooperative
  scheduler. The runtime never sends it and mutes audio while the tab is hidden instead.

**Graphics.** Every `Image` is an offscreen canvas.
- **Clipping.** Blits and fills are clipped arithmetically, including all eight `drawRegion` transforms,
  so canvas clip state is only needed for text and paths.
- **PNG decoding.** `createImage(byte[])` must return a decoded image immediately, and browser image
  decoding is asynchronous, so the runtime has its own synchronous PNG decoder.

**Sound.** The 21 clips in `snd.f` are standard MIDI files. `web/synth.js` gives each instrument family
an oscillator recipe, has a synthesized General MIDI drum kit, and honours volume, expression, pan and
pitch bend. It does not reproduce a Nokia's wavetable sound, but it follows the music and effects closely.

**Saves.** Record stores persist in localStorage under `diamond-rush.rms.*`.

## Deobfuscated source

```sh
./deobfuscate.sh      # rename the classes from the map, then decompile them into decompiled/
```

ProGuard reduced every class, method and field to one or two letters and reused them freely: one class
has fields named `a` of ten different types, and some methods differ only by return type. Renaming the
decompiled text is therefore unreliable. Instead, names from `mappings/diamondrush.map` are applied to
the bytecode, and the result is decompiled with [Vineflower](https://github.com/Vineflower/vineflower).

```
class  i                      GameCanvas
field  i   aZ  I              stageDiamonds
method i   Z   ()V            loseLife
```

| Class | Was | Named |
| --- | --- | --- |
| `GameCanvas` (game loop, states, stages, map, menus, saving) | `i` | 426 of 703 |
| `Sprite` (Gameloft ASprite format) | `f` | all 28 |
| `SpriteInstance` (animated sprite on screen) | `b` | all 15 |
| `Cutscene` (scripted tutorial and story scenes from `demo.f`) | `c` | all 39 |
| `SoundManager` (MIDI clips, priorities, playback thread) | `j` | all 23 |
| `FontRenderer` (system-font text) | `h` | all 20 |
| `Text` (string table and platform check) | `d` | all 8 |
| `Constants`, `TreasureRain`, `Rand`, `PlatformRequestThread`, `GloftDIRU` | `a`, `g`, `e`, `k` | all |

In total, 268 of 287 renamable methods and 305 of 582 fields have real names. Every other member gets a
unique placeholder built from its type and original name, such as `int_bg` or `ab_void`, so nothing is
ambiguous. The remaining gaps are mostly narrow counters for single mechanics inside `GameCanvas`, such
as enemy chases and the hook drag.

The tool rejects map lines that don't match a member, names mapped twice, name collisions, and attempts
to rename platform callbacks such as `paint` and `run`.

## Recompiled source

`game/src/` is the decompilation edited until it compiles. `SOURCE=1 ./build.sh` builds the browser game
from it, so changes to the game's Java code show up in the browser. `./deobfuscate.sh` regenerates the
unedited reference copy in `decompiled/`.

The raw decompiler output had 188 compile errors. Fixing them changed about 120 of 21,000 lines:

- **Missing declarations:** variables Vineflower used without declaring them.
- **Reused variable slots:** the bytecode reuses a slot for values of different types, such as a flag
  that later holds a coordinate, or a `byte` parameter that later holds an `int`. Each use now has its own
  variable of the right type.
- **Narrowing casts:** added only where the value is provably in range, or where the cast does exactly
  what the original `bastore`/`sastore` instruction did.
- **Leftover artifacts:** `this = this`, chained interface constants, and missing `throws` clauses.

Compiling cleanly did not mean the game behaved the same. The decompiler typed some `int` locals as
`byte`, which broke two things:

- **A loop that never ends.** `for (byte x = 0; x < 320; x += 24)` wraps at 127 and hung the seal screen.
- **Corrupted enemies.** `var1 <<= var2` narrowed a shifted value to `byte` and corrupted enemy state.

The two checks under [Tests](#tests) catch this class of bug.

## Tests

```sh
npm install && npx playwright install chromium     # once
./build.sh && npm test                             # browser smoke test
tools/difftest/difftest.sh build/game-src-classes 30000 1 2 3 4 5 6 7 8   # after SOURCE=1 ./build.sh
```

- **`npm test`** serves `dist/` and drives headless Chromium through the title screen, the menu and the
  first stage. It fails on page errors, a blank screen or silent audio, and saves screenshots to
  `test/output/`.
- **`tools/difftest/difftest.sh <classes-or-jar> [ticks] [seeds...]`** runs the original JAR and
  another build side by side on a headless JVM.
  - *Determinism:* time, sleep and threads are virtualised (`Prep.java` rewrites those calls), and both
    builds get the same seeded key presses.
  - *Comparison:* a fake MIDP layer hashes every drawing and platform call, and each tick logs the game
    state, world, stage and hash. The two logs must match line for line.
  - *Coverage:* the scripted runs open the cheat menu, unlock everything, skip through stages and travel
    to Bavaria and Siberia. Yes/no dialogs are always answered "No", so random input can't quit.
  - *Results:* eight seeds of 30,000 ticks (about 3.3 hours of game time) are identical for the
    recompiled source. Reintroducing the enemy bug makes the run diverge at tick 2965, in Siberia stage
    13.
- **`tools/difftest/Narrowing.java`** lists every method whose recompiled bytecode narrows values
  (`i2b`/`i2s`/`i2c`) more often than the original. Each remaining case is a constant or a provably
  in-range value.

**Not verified by any test:** the boss fights and the ending, which the scripted runs don't reach, and
how the synthesizer sounds to a human ear.

## Things found along the way

- **Developer cheat menu.** Type `#` `4` `7` `7` `2` during play. It can add diamonds and red diamonds,
  set weapons, unlock all stages or the secret stages, and enable stage skipping (7 or 9 during play).
- **One state machine.** `mappings/diamondrush.map` documents 28 of the game's states, from boot and the
  Gameloft logo to the world map, the seal, the shop, the credits and the ending.
- **Save format.** The `DiamondRush` record holds lives, totals, armor level and weapons, then one record
  per world and per stage, including which hidden treasures were taken.
- **Progression.** Armor unlocks at 150, 400, 1000 and 3000 total diamonds. Bavaria needs 10 red
  diamonds and Siberia needs 25.
- **Checkpoints.** Stepping on a magic circle snapshots the whole stage: tiles, objects, enemies and
  counters. Losing a life restores that snapshot.

## Reverse-engineering notes

- **REA.** [REA](https://github.com/morluto/rea) (`rea-agents` 6.4.0) supplied the archive inventory and
  integrity-checked extraction. The JAR's SHA-256 is
  `1a27a721a4440e8bdf63aa456732d71cb4e0cbd00054899dd233b0f69fd96b00`.
- **REA's gaps.** It cannot parse JVM class files: their `CAFEBABE` magic is shared with Mach-O universal
  binaries, so they are rejected as a bad FAT header. Its browser-scenario tool also failed on this host.
- **Other tools.** Class-level analysis therefore used `javap`, the decompilers and `tools/src/Xref.java`,
  and browser testing used Playwright.

## License

The code written for this project is [MIT](LICENSE). That covers `runtime/`, `web/`, `tools/`, `test/`,
`mappings/` and the scripts. `game/src/` is Gameloft's code and is excluded. Diamond Rush and Gameloft are
trademarks of their respective owners.
