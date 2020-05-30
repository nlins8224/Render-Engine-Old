package Models;

/**
 * This class binds texture ID to a ModelTexture object and sets it's properties
 */

public class ModelTexture {

    private int textureID;
    private boolean hasTransparency = false;
    private boolean useFakeLighting = false;

    private float shineDamper = 1;
    private float reflectivity = 0;

    private int numberOfAtlasRows = 1;

    /**
     *
     * @param texture textureID
     */
    public ModelTexture(int texture){
        this.textureID = texture;
    }

    public int getTextureID() {
        return textureID;
    }

    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public boolean isHasTransparency(){
        return hasTransparency;
    }

    public void setHasTransparency(boolean hasTransparency){
        this.hasTransparency = hasTransparency;

    }

    public int getNumberOfAtlasRows() {
        return numberOfAtlasRows;
    }

    public void setNumberOfAtlasRows(int numberOfAtlasRows) {
        this.numberOfAtlasRows = numberOfAtlasRows;
    }

    public boolean isUseFakeLighting() {
        return useFakeLighting;
    }

    public void setUseFakeLighting(boolean useFakeLighting) {
        this.useFakeLighting = useFakeLighting;
    }

}

