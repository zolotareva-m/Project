package render_engine;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import java.util.Arrays;
import Math.Vector3f;
import Math.Vector2f;

public class Rasterizer {
    private int width, height;
    private float[] zBuffer;

    private boolean useTexture = false;
    private boolean useLighting = false;
    private boolean drawMesh = false;

    private Texture currentTexture;

    public Rasterizer(int width, int height) {
        this.width = width;
        this.height = height;
        this.zBuffer = new float[width * height];
    }

    public void clear() {
        Arrays.fill(zBuffer, Float.NEGATIVE_INFINITY);
    }

    public void updateSize(int width, int height) {
        if (this.width != width || this.height != height) {
            this.width = width;
            this.height = height;
            this.zBuffer = new float[width * height];
        }
    }

    public void setUseTexture(boolean useTexture) { this.useTexture = useTexture; }
    public void setUseLighting(boolean useLighting) { this.useLighting = useLighting; }
    public void setDrawMesh(boolean drawMesh) { this.drawMesh = drawMesh; }
    public void setTexture(Texture texture) { this.currentTexture = texture; }

    public void drawTriangle(
            Vector3f v1, Vector3f v2, Vector3f v3,  // Экранные координаты
            Vector3f n1, Vector3f n2, Vector3f n3,  // Нормали
            Vector2f t1, Vector2f t2, Vector2f t3,  // Текстурные коорд.
            PixelWriter pixelWriter) {

        // Ограничивающий прямоугольник
        int minX = (int) Math.max(0, Math.min(v1.x, Math.min(v2.x, v3.x)));
        int maxX = (int) Math.min(width - 1, Math.max(v1.x, Math.max(v2.x, v3.x)));
        int minY = (int) Math.max(0, Math.min(v1.y, Math.min(v2.y, v3.y)));
        int maxY = (int) Math.min(height - 1, Math.max(v1.y, Math.max(v2.y, v3.y)));

        Vector3f lightDir = new Vector3f(0, 0, 1); // Свет светит "от камеры"

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {

                // Барицентрические координаты
                Vector3f bary = calculateBarycentric(v1, v2, v3, x, y);

                // Если пиксель мимо треугольника -> пропускаем
                if (bary.x < 0 || bary.y < 0 || bary.z < 0) continue;

                // Z-Buffer (глубина)
                float z = v1.z * bary.x + v2.z * bary.y + v3.z * bary.z;

                int idx = y * width + x;
                if (idx >= 0 && idx < zBuffer.length && z > zBuffer[idx]) {
                    zBuffer[idx] = z;

                    Color color = Color.WHITE;

                    // 1. Текстурирование
                    if (useTexture && currentTexture != null && t1 != null) {
                        // Интерполяция UV координат
                        float u = t1.X() * bary.x + t2.X() * bary.y + t3.X() * bary.z;
                        float v = t1.Y() * bary.x + t2.Y() * bary.y + t3.Y() * bary.z;
                        color = currentTexture.getPixel(u, v);
                    }

                    // 2. Освещение
                    if (useLighting && n1 != null) {
                        // Интерполяция нормали
                        float nx = n1.x * bary.x + n2.x * bary.y + n3.x * bary.z;
                        float ny = n1.y * bary.x + n2.y * bary.y + n3.y * bary.z;
                        float nz = n1.z * bary.x + n2.z * bary.y + n3.z * bary.z;

                        Vector3f normal = new Vector3f(nx, ny, nz);
                        normal.normalize(); // Важно нормализовать после интерполяции!

                        // Интенсивность = dot(N, L)
                        float intensity = Math.max(0.2f, Vector3f.dot(normal, lightDir));

                        color = Color.color(
                                Math.min(1, color.getRed() * intensity),
                                Math.min(1, color.getGreen() * intensity),
                                Math.min(1, color.getBlue() * intensity)
                        );
                    }

                    pixelWriter.setColor(x, y, color);
                }
            }
        }
    }

    // Математика для поиска точки внутри треугольника
    private Vector3f calculateBarycentric(Vector3f a, Vector3f b, Vector3f c, int x, int y) {
        float detT = (b.y - c.y) * (a.x - c.x) + (c.x - b.x) * (a.y - c.y);
        if (Math.abs(detT) < 1e-5) return new Vector3f(-1, -1, -1); // Вырожденный треугольник

        float lambda1 = ((b.y - c.y) * (x - c.x) + (c.x - b.x) * (y - c.y)) / detT;
        float lambda2 = ((c.y - a.y) * (x - c.x) + (a.x - c.x) * (y - c.y)) / detT;
        float lambda3 = 1.0f - lambda1 - lambda2;

        return new Vector3f(lambda1, lambda2, lambda3);
    }
}