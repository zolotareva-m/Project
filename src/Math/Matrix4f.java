package Math;
public class Matrix4f {
    private float[][] m;

    public Matrix4f() {
        m = new float[4][4];
        setIdentity();
    }

    public Matrix4f(float[][] matrix) {
        m = matrix;
    }

    public void setIdentity() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                m[i][j] = (i == j) ? 1.0f : 0.0f;
            }
        }
    }

    public Matrix4f mul(Matrix4f other) {
        Matrix4f result = new Matrix4f();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result.m[i][j] = 0;
                for (int k = 0; k < 4; k++) {
                    result.m[i][j] += this.m[i][k] * other.m[k][j];
                }
            }
        }
        return result;
    }

    public Vector3f transform(Vector3f v) {
        float x = m[0][0] * v.x + m[0][1] * v.y + m[0][2] * v.z + m[0][3];
        float y = m[1][0] * v.x + m[1][1] * v.y + m[1][2] * v.z + m[1][3];
        float z = m[2][0] * v.x + m[2][1] * v.y + m[2][2] * v.z + m[2][3];
        float w = m[3][0] * v.x + m[3][1] * v.y + m[3][2] * v.z + m[3][3];

        if (w != 0 && w != 1) {
            x /= w;
            y /= w;
            z /= w;
        }

        return new Vector3f(x, y, z);
    }

    public Matrix4f transpose() {
        Matrix4f result = new Matrix4f();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result.m[i][j] = m[j][i];
            }
        }
        return result;
    }

    public float get(int row, int col) {
        return m[row][col];
    }

    public void set(int row, int col, float value) {
        m[row][col] = value;
    }
}
