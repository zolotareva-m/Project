package Model;
import Math.Matrix4f;
import Math.Vector3f;
import Math.aTransform;
import java.util.ArrayList;
import java.util.List;

public class Model3D {
    private List<Vector3f> vertices;
    private List<Vector3f> originalVertices;
    private aTransform aTransform;
    private String name;
    private boolean applyaTransformations = true;

    public Model3D(String name) {
        this.name = name;
        this.vertices = new ArrayList<>();
        this.originalVertices = new ArrayList<>();
        this.aTransform = new aTransform();
    }

    public void addVertex(Vector3f vertex) {
        vertices.add(vertex);
        originalVertices.add(new Vector3f(vertex.x, vertex.y, vertex.z));
    }

    public List<Vector3f> getaTransformedVertices() {
        if (!applyaTransformations) {
            return vertices;
        }

        List<Vector3f> aTransformed = new ArrayList<>();
        Matrix4f TransformMatrix = aTransform.getTransformationMatrix();

        for (Vector3f vertex : vertices) {
            aTransformed.add(TransformMatrix.transform(vertex));
        }

        return aTransformed;
    }

    public List<Vector3f> getOriginalVertices() {
        return originalVertices;
    }

    public void resetaTransformations() {
        aTransform = new aTransform();
        vertices = new ArrayList<>();
        for (Vector3f v : originalVertices) {
            vertices.add(new Vector3f(v.x, v.y, v.z));
        }
    }

    public aTransform getaTransform() { return aTransform; }
    public void setApplyaTransformations(boolean apply) {
        this.applyaTransformations = apply;
    }
    public String getName() { return name; }
}