package Model;

import Math.Vector3f;
import java.util.List;

public class NormalUtils {

    public static void recalculateNormals(Model model) {
        model.normals.clear();
        for (int i = 0; i < model.vertices.size(); i++) {
            model.normals.add(new Vector3f(0, 0, 0));
        }

        for (Polygon polygon : model.polygons) {
            List<Integer> vertexIndices = polygon.getVertexIndices();

            Vector3f v0 = model.vertices.get(vertexIndices.get(0));
            Vector3f v1 = model.vertices.get(vertexIndices.get(1));
            Vector3f v2 = model.vertices.get(vertexIndices.get(2));

            Vector3f side1 = new Vector3f(v1.x - v0.x, v1.y - v0.y, v1.z - v0.z);
            Vector3f side2 = new Vector3f(v2.x - v0.x, v2.y - v0.y, v2.z - v0.z);

            Vector3f normal = Vector3f.crossProduct(side1, side2);

            for (Integer index : vertexIndices) {
                Vector3f current = model.normals.get(index);
                model.normals.set(index, new Vector3f(current.x + normal.x, current.y + normal.y, current.z + normal.z));
            }
        }

        for (Vector3f n : model.normals) {
            float len = (float) Math.sqrt(n.x * n.x + n.y * n.y + n.z * n.z);
            if (len > 1e-6) {
                n.x /= len; n.y /= len; n.z /= len;
            }
        }
    }
}