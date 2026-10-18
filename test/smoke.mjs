// Browser smoke test for dist/: boots the game in headless Chromium and plays a little.
//
//   npm install && npx playwright install chromium     (once)
//   ./build.sh && npm test
//
// Fails on page errors, a blank screen, silent audio, or a key sequence that does not reach the stage.
// Screenshots are written to test/output/.
import { createServer } from 'node:http';
import { mkdir, readFile } from 'node:fs/promises';
import { extname, join, normalize } from 'node:path';
import { fileURLToPath } from 'node:url';
import { chromium } from 'playwright';

const root = fileURLToPath(new URL('..', import.meta.url));
const dist = join(root, 'dist');
const output = join(root, 'test', 'output');
const types = { '.html': 'text/html', '.js': 'text/javascript', '.png': 'image/png', '.jar': 'application/java-archive' };

const server = createServer(async (req, res) => {
    const path = normalize(decodeURIComponent(new URL(req.url, 'http://x').pathname)).replace(/^(\.\.[/\\])+/, '');
    const file = join(dist, path === '/' ? 'index.html' : path);
    try {
        const body = await readFile(file);
        res.writeHead(200, { 'content-type': types[extname(file)] || 'application/octet-stream' });
        res.end(body);
    } catch {
        res.writeHead(404).end();
    }
});
await new Promise((resolve) => server.listen(0, '127.0.0.1', resolve));
const url = `http://127.0.0.1:${server.address().port}/`;

// Taps whatever is routed to the speakers so the test can check that the synthesizer makes sound.
const audioTap = () => {
    const connect = AudioNode.prototype.connect;
    AudioNode.prototype.connect = function (dest, ...rest) {
        if (dest instanceof AudioDestinationNode && !window.__analyser) {
            window.__analyser = this.context.createAnalyser();
            connect.call(this, window.__analyser);
        }
        return connect.call(this, dest, ...rest);
    };
};

const failures = [];
const check = (ok, message) => {
    console.log(`${ok ? 'ok  ' : 'FAIL'} ${message}`);
    if (!ok) failures.push(message);
};

const browser = await chromium.launch({ args: ['--autoplay-policy=no-user-gesture-required'] });
try {
    const page = await browser.newPage({ viewport: { width: 900, height: 700 } });
    const errors = [];
    page.on('pageerror', (e) => errors.push(e.message));
    await page.addInitScript(audioTap);
    await mkdir(output, { recursive: true });

    const key = async (k, hold = 120) => {
        await page.keyboard.down(k);
        await page.waitForTimeout(hold);
        await page.keyboard.up(k);
    };
    const shot = (name) => page.locator('#screen').screenshot({ path: join(output, `${name}.png`) });
    const screenVariety = () => page.evaluate(() => {
        const c = document.getElementById('screen');
        const d = c.getContext('2d').getImageData(0, 0, c.width, c.height).data;
        const colors = new Set();
        for (let i = 0; i < d.length; i += 4 * 97) colors.add((d[i] << 16) | (d[i + 1] << 8) | d[i + 2]);
        return colors.size;
    });

    await page.goto(url);
    await page.waitForSelector('#overlay-action:not([hidden])', { timeout: 15000 });
    await page.click('#overlay-action');
    await page.waitForTimeout(10000);                       // Gameloft logo, loading, title
    await shot('1-title');
    check((await screenVariety()) > 20, 'title screen is drawn');
    const level = await page.evaluate(() => {
        const a = window.__analyser;
        if (!a) return 0;
        const buf = new Float32Array(a.fftSize);
        a.getFloatTimeDomainData(buf);
        return Math.sqrt(buf.reduce((s, v) => s + v * v, 0) / buf.length);
    });
    check(level > 0.01, `title music is audible (rms ${level.toFixed(3)})`);

    await key('Enter');                                      // title -> main menu
    await page.waitForTimeout(800);
    await key('Enter');                                      // New game / Continue
    await page.waitForTimeout(6000);
    await shot('2-stage');
    for (const k of ['ArrowRight', 'ArrowDown', 'ArrowLeft', 'ArrowUp']) {
        await key(k, 700);
    }
    await shot('3-moved');
    check((await screenVariety()) > 20, 'stage is drawn');

    await key('q');                                          // pause menu
    await page.waitForTimeout(700);
    await shot('4-pause');

    check(errors.length === 0, `no page errors${errors.length ? ': ' + errors.join(' | ') : ''}`);
} finally {
    await browser.close();
    server.close();
}

console.log(failures.length ? `\n${failures.length} check(s) failed` : '\nall checks passed');
process.exit(failures.length ? 1 : 0);
