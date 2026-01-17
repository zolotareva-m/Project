package Math;

public class aTransform {
    private Vector3f translation;
    private Vector3f rotation; // углы Эйлера в радианах
    private Vector3f scale;

    public aTransform() {
        translation = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);
        scale = new Vector3f(1, 1, 1);
    }

    //масштаб, поворот, перенос
    public Matrix4f getTransformationMatrix() {
        Matrix4f scaleMatrix = getScaleMatrix();
        Matrix4f rotationMatrix = getRotationMatrix();
        Matrix4f translationMatrix = getTranslationMatrix();

        return translationMatrix.mul(rotationMatrix).mul(scaleMatrix);
    }

    private Matrix4f getScaleMatrix() {
        Matrix4f matrix = new Matrix4f();
        matrix.set(0, 0, scale.x);
        matrix.set(1, 1, scale.y);
        matrix.set(2, 2, scale.z);
        return matrix;
    }

    private Matrix4f getRotationMatrix() {
        float cosX = (float) Math.cos(rotation.x);
        float sinX = (float) Math.sin(rotation.x);
        float cosY = (float) Math.cos(rotation.y);
        float sinY = (float) Math.sin(rotation.y);
        float cosZ = (float) Math.cos(rotation.z);
        float sinZ = (float) Math.sin(rotation.z);

        Matrix4f rx = new Matrix4f();
        rx.set(1, 1, cosX); rx.set(1, 2, -sinX);
        rx.set(2, 1, sinX); rx.set(2, 2, cosX);

        Matrix4f ry = new Matrix4f();
        ry.set(0, 0, cosY); ry.set(0, 2, sinY);
        ry.set(2, 0, -sinY); ry.set(2, 2, cosY);

        Matrix4f rz = new Matrix4f();
        rz.set(0, 0, cosZ); rz.set(0, 1, -sinZ);
        rz.set(1, 0, sinZ); rz.set(1, 1, cosZ);

        return rx.mul(ry).mul(rz);
    }

    private Matrix4f getTranslationMatrix() {
        Matrix4f matrix = new Matrix4f();
        matrix.set(0, 3, translation.x);
        matrix.set(1, 3, translation.y);
        matrix.set(2, 3, translation.z);
        return matrix;
    }

    public Vector3f getTranslation() { return translation; }
    public void setTranslation(Vector3f translation) { this.translation = translation; }
    public void setTranslation(float x, float y, float z) {
        this.translation = new Vector3f(x, y, z);
    }

    public Vector3f getRotation() { return rotation; }
    public void setRotation(Vector3f rotation) { this.rotation = rotation; }
    public void setRotation(float x, float y, float z) {
        this.rotation = new Vector3f(x, y, z);
    }

    public Vector3f getScale() { return scale; }
    public void setScale(Vector3f scale) { this.scale = scale; }
    public void setScale(float x, float y, float z) {
        this.scale = new Vector3f(x, y, z);
    }
}
