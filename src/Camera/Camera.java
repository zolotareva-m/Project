package Camera;

import Math.Vector3f;
import Math.Matrix4f;

public class Camera {
    private Vector3f position;
    private Vector3f rotation; // pitch, yaw, roll
    private Vector3f target;
    private Vector3f up;

    private float moveSpeed = 0.1f;
    private float rotationSpeed = 0.01f;

    public Camera() {
        position = new Vector3f(0, 0, 5);
        rotation = new Vector3f(0, 0, 0);
        target = new Vector3f(0, 0, 0);
        up = new Vector3f(0, 1, 0);
    }

    public Matrix4f getViewMatrix() {
        Vector3f zAxis = position.sub(target).normalize();
        Vector3f xAxis = up.cross(zAxis).normalize();
        Vector3f yAxis = zAxis.cross(xAxis);

        float[][] viewMatrix = {
                {xAxis.x, xAxis.y, xAxis.z, -xAxis.dot(position)},
                {yAxis.x, yAxis.y, yAxis.z, -yAxis.dot(position)},
                {zAxis.x, zAxis.y, zAxis.z, -zAxis.dot(position)},
                {0, 0, 0, 1}
        };

        return new Matrix4f(viewMatrix);
    }

    public void moveForward(float amount) {
        Vector3f forward = getForwardVector();
        position = position.add(forward.mul(amount * moveSpeed));
    }

    public void moveRight(float amount) {
        Vector3f right = getRightVector();
        position = position.add(right.mul(amount * moveSpeed));
    }

    public void moveUp(float amount) {
        position = position.add(up.mul(amount * moveSpeed));
    }

    public void rotate(float deltaX, float deltaY) {
        rotation.y += deltaX * rotationSpeed; // yaw
        rotation.x += deltaY * rotationSpeed; // pitch
        rotation.x = Math.max(-(float)Math.PI/2, Math.min((float)Math.PI/2, rotation.x));

        updateTarget();
    }

    private void updateTarget() {
        float cosYaw = (float) Math.cos(rotation.y);
        float sinYaw = (float) Math.sin(rotation.y);
        float cosPitch = (float) Math.cos(rotation.x);
        float sinPitch = (float) Math.sin(rotation.x);

        Vector3f forward = new Vector3f(
                cosYaw * cosPitch,
                sinPitch,
                sinYaw * cosPitch
        );

        target = position.add(forward);
    }

    private Vector3f getForwardVector() {
        return target.sub(position).normalize();
    }

    private Vector3f getRightVector() {
        Vector3f forward = getForwardVector();
        return forward.cross(up).normalize();
    }

    public Vector3f getPosition() { return position; }
    public void setPosition(Vector3f position) { this.position = position; }
    public Vector3f getTarget() { return target; }
    public float getMoveSpeed() { return moveSpeed; }
    public void setMoveSpeed(float speed) { this.moveSpeed = speed; }
}