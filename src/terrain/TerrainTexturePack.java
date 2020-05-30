package terrain;

/**
 * This class is used to specify terrain texture pack
 */
public class TerrainTexturePack {

    private TerrainTexture backgroundTexuture;
    private TerrainTexture rTexuture;
    private TerrainTexture gTexuture;
    private TerrainTexture bTexuture;

    /**
     * Specifies terrain textures
     * @param backgroundTexuture terrain background texture
     * @param rTexuture terrain red texture
     * @param gTexuture terrain green texture
     * @param bTexuture terrain blue texture
     */
    public TerrainTexturePack(TerrainTexture backgroundTexuture, TerrainTexture rTexuture,
                              TerrainTexture gTexuture, TerrainTexture bTexuture)
    {
        this.backgroundTexuture = backgroundTexuture;
        this.rTexuture = rTexuture;
        this.gTexuture = gTexuture;
        this.bTexuture = bTexuture;

    }

    public TerrainTexture getBackgroundTexuture() {
        return backgroundTexuture;
    }

    public TerrainTexture getrTexuture() {
        return rTexuture;
    }

    public TerrainTexture getgTexuture() {
        return gTexuture;
    }

    public TerrainTexture getbTexuture() {
        return bTexuture;
    }



}
