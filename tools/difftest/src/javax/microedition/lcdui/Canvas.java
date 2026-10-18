package javax.microedition.lcdui;

public abstract class Canvas extends Displayable {
    public static final int UP = 1;
    public static final int DOWN = 6;
    public static final int LEFT = 2;
    public static final int RIGHT = 5;
    public static final int FIRE = 8;
    public static final int KEY_NUM0 = 48;
    public static final int KEY_STAR = 42;
    public static final int KEY_POUND = 35;

    protected Canvas() {
    }

    protected abstract void paint(Graphics g);

    public final void repaint() {
        if (this == Display.current) {
            Display.repaintPending = true;
        }
    }

    public final void repaint(int x, int y, int w, int h) {
        repaint();
    }

    public final void serviceRepaints() {
    }

    public void setFullScreenMode(boolean mode) {
    }

    public boolean hasPointerEvents() {
        return false;
    }

    public int getGameAction(int keyCode) {
        return 0;
    }

    protected void keyPressed(int keyCode) {
    }

    protected void keyReleased(int keyCode) {
    }

    protected void keyRepeated(int keyCode) {
    }

    protected void showNotify() {
    }

    protected void hideNotify() {
    }
}
