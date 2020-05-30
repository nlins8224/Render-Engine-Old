package terrain;

/**
 * This class is used to specify terrain texture
 */
public class TerrainTexture {

    private int textureID;

    /**
     *
     * @param textureID specifies terrain texture ID
     */
    public TerrainTexture(int textureID){
        this.textureID = textureID;
    }

    public int getTextureID() {
        return textureID;
    }

    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }
}
