package IO;

import Model.Model3D;
import Math.Vector3f;
import java.io.*;
import java.util.List;

public class Export {

    public static void saveModel(Model3D model, String filename, boolean saveTransformed) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            List<Vector3f> vertices = saveTransformed ?
                    model.getaTransformedVertices() :
                    model.getOriginalVertices();

            writer.println("Экспортировано");
            writer.println("Вершин " + vertices.size());

            for (Vector3f vertex : vertices) {
                writer.printf("v %.6f %.6f %.6f\n",
                        vertex.x, vertex.y, vertex.z);
            }

            System.out.println("Модель сохранена в файл " + filename);

        } catch (IOException e) {
            System.err.println("Ошибка сохранения " + e.getMessage());
        }
    }
}