package Tests;

import Math.Vector3f;
import Math.Matrix4f;
import Math.aTransform;

public class MathTests {

    public static void runAllTests() {
        testVectorOperations();
        testMatrixOperations();
        testTransformations();
        System.out.println("Все тесты пройдены");
    }

    private static void testVectorOperations() {
        System.out.println("Проверка векторных операций");

        Vector3f v1 = new Vector3f(1, 2, 3);
        Vector3f v2 = new Vector3f(4, 5, 6);

        Vector3f sum = v1.add(v2);
        assert sum.x == 5 && sum.y == 7 && sum.z == 9 : "Не удалось добавить вектор";

        Vector3f diff = v1.sub(v2);
        assert diff.x == -3 && diff.y == -3 && diff.z == -3 : "Вычитание вектора не удалось";

        float dot = v1.dot(v2);
        assert dot == 32 : "Скалярное произведение не удалось";

        Vector3f cross = v1.cross(v2);
        assert cross.x == -3 && cross.y == 6 && cross.z == -3 : "Ошибка векторного произведения";

        Vector3f normalized = new Vector3f(3, 0, 0).normalize();
        assert Math.abs(normalized.x - 1) < 0.001 : "Нормализация не выполнена";

    }

    private static void testMatrixOperations() {
        System.out.println("Проверка матричных операций");

        Matrix4f identity = new Matrix4f();
        assert identity.get(0, 0) == 1 : "Ошибка идентификации матрицы";

        Matrix4f m1 = new Matrix4f();
        m1.set(0, 0, 2);
        m1.set(1, 1, 2);

        Matrix4f result = m1.mul(identity);
        assert result.get(0, 0) == 2 : "Ошибка умножения матриц";

        Vector3f v = new Vector3f(1, 2, 3);
        Vector3f transformed = identity.transform(v);
        assert transformed.x == 1 && transformed.y == 2 && transformed.z == 3 :
                "Ошибка преобразования";
    }

    private static void testTransformations() {
        System.out.println("Проверка преобразований");

        aTransform transform = new aTransform();

        transform.setTranslation(5, 10, 15);
        Vector3f point = new Vector3f(1, 2, 3);
        Vector3f translated = transform.getTransformationMatrix().transform(point);
        assert Math.abs(translated.x - 6) < 0.001 : "Ошибка транспонирования";

        transform.setScale(2, 2, 2);
        transform.setTranslation(0, 0, 0);
        Vector3f scaled = transform.getTransformationMatrix().transform(point);
        assert Math.abs(scaled.x - 2) < 0.001 : "Ошибка масштабирования";

    }

    public static void main(String[] args) {
        runAllTests();
    }
}
