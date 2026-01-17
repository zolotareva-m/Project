package objwriter;

import math.Vector2f;
import math.Vector3f;
import model.Model;
import model.Polygon;

import java.io.FileWriter;
import java.io.IOException;

public class ObjWriter {

    private static final String OBJ_VERTEX_TOKEN = "v";
    private static final String OBJ_TEXTURE_TOKEN = "vt";
    private static final String OBJ_NORMAL_TOKEN = "vn";
    private static final String OBJ_FACE_TOKEN = "f";

    public static void write(Model model, String outputPath) {

        try (FileWriter writer = new FileWriter(outputPath)) {
            int counter = 0;

            // Запись вершин
            for (Vector3f vertex : model.vertices) {
                writer.write(OBJ_VERTEX_TOKEN + " " + vertex.X() + " " + vertex.Y() + " " + vertex.Z() + "\n");
                counter++;
            }

            if (counter > 0) {
                writer.write("# " + counter + " vertices\n\n");
                counter = 0;
            }

            // Запись текстур вершин
            for (Vector2f vertexTexture : model.textureVertices) {
                writer.write(OBJ_TEXTURE_TOKEN + " " + vertexTexture.X() + " " + vertexTexture.Y() + " 0.0000" + "\n");
                counter++;
            }

            if (counter > 0) {
                writer.write("# " + counter + " texture coords\n\n");
                counter = 0;
            }

            // Запись нормалей вершин
            for (Vector3f vertexNormal : model.normals) {
                writer.write(OBJ_NORMAL_TOKEN + " " + vertexNormal.X() + " " + vertexNormal.Y() + " " + vertexNormal.Z() + "\n");
                counter++;
            }

            if (counter > 0) {
                writer.write("# " + counter + " normals\n\n");
                counter = 0;
            }

            int trianglesCounter = 0;

            // Запись полигонов
            for (Polygon polygon : model.polygons) {

                writer.write(OBJ_FACE_TOKEN);

                if (polygon.getVertexIndices().size() == 3){
                    trianglesCounter++;
                }

                int vertexCount = polygon.getVertexIndices().size();
                boolean hasTextures = polygon.getTextureVertexIndices().size() == vertexCount;
                boolean hasNormales = polygon.getNormalIndices().size() == vertexCount;

                for (int i = 0; i < vertexCount; i++) {
                    // Пишем ID вертекса
                    writer.write( " " + polygon.getVertexIndices().get(i));

                    // Пишем ID текстуры
                    if (hasTextures){
                        writer.write("/" + polygon.getTextureVertexIndices().get(i));
                    }

                    // Пишем ID нормали
                    if (hasNormales){
                        if (!hasTextures) {
                            writer.write("/");
                        }
                        writer.write("/" + polygon.getNormalIndices().get(i));
                    }
                }

                writer.write("\n");

                counter++;
            }

            if (counter > 0) {
                writer.write("# " + counter + " polygons - " + trianglesCounter + " triangles\n\n");
            }

            System.out.println("Модель успешно сохранена в " + outputPath);

        } catch (IOException e) {
            System.out.println("Возникла ошибка при сохранении модели: " + e.getMessage());;
        }
    }
}