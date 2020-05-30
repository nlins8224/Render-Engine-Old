package Models;

/**
 * This class creates a container for vaoID and amount of vertices
 */
public class RawModel {

    private int vaoID;
    private int vertexCount;

    /**
     *
     * @param vaoID ID of Vertex Array Object
     * @param vertexCount Amount of vertices
     */
    public RawModel(int vaoID, int vertexCount)
    {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }


    public int getVertexCount() {
        return vertexCount;
    }


    public int getVaoID() {
        return vaoID;
    }


}
