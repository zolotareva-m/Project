package Camera;

import java.awt.event.*;

public class Controller implements KeyListener, MouseListener, MouseMotionListener {
    private Camera camera;
    private boolean[] keysPressed;
    private int lastMouseX, lastMouseY;
    private boolean mouseDragging = false;

    public Controller(Camera camera) {
        this.camera = camera;
        this.keysPressed = new boolean[256];
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keysPressed[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keysPressed[e.getKeyCode()] = false;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON2) {
            mouseDragging = true;
            lastMouseX = e.getX();
            lastMouseY = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON2) {
            mouseDragging = false;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mouseDragging) {
            int deltaX = e.getX() - lastMouseX;
            int deltaY = e.getY() - lastMouseY;

            camera.rotate(deltaX, deltaY);

            lastMouseX = e.getX();
            lastMouseY = e.getY();
        }
    }

    public void update() {

        if (keysPressed[KeyEvent.VK_W]) camera.moveForward(1);
        if (keysPressed[KeyEvent.VK_S]) camera.moveForward(-1);
        if (keysPressed[KeyEvent.VK_A]) camera.moveRight(-1);
        if (keysPressed[KeyEvent.VK_D]) camera.moveRight(1);
        if (keysPressed[KeyEvent.VK_SPACE]) camera.moveUp(1);
        if (keysPressed[KeyEvent.VK_SHIFT]) camera.moveUp(-1);

        if (keysPressed[KeyEvent.VK_PLUS] || keysPressed[KeyEvent.VK_EQUALS]) {
            camera.setMoveSpeed(camera.getMoveSpeed() * 1.1f);
        }
        if (keysPressed[KeyEvent.VK_MINUS]) {
            camera.setMoveSpeed(camera.getMoveSpeed() * 0.9f);
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}
