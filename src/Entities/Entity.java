package Entities;

import Models.TexturedModel;
import org.joml.Vector3f;

/**
 * Entity is an object with it's position, rotation, scale and texture
 */
public class Entity {

    private TexturedModel model;
    private Vector3f position;
    private int textureAtlasIndex = 0;
    private float rotX, rotY, rotZ;
    private float scale;

    /**
     *
     * @param model texturedModel
     * @param position xyz position of an entity in the world
     * @param rotX rotation over x axis
     * @param rotY rotation over y axis
     * @param rotZ rotation over z axis
     * @param scale scale of an entity object
     */
    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    /**
     *
     * @param model texturedModel
     * @param position xyz position of an entity in the world
     * @param rotX rotation over x axis
     * @param rotY rotation over y axis
     * @param rotZ rotation over z axis
     * @param scale object scale
     * @param textureAtlasIndex index of a texture
     */
    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, int textureAtlasIndex) {
        this.textureAtlasIndex = textureAtlasIndex;
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    /**
     *
     * @return X texture offset
     */
    public float getTextureXOffset(){
        int column = textureAtlasIndex % model.getTexture().getNumberOfAtlasRows();
        return (float)column / model.getTexture().getNumberOfAtlasRows();
    }

    /**
     *
     * @return y texture offset
     */
    public float getTextureYOffset(){
        int row = textureAtlasIndex / model.getTexture().getNumberOfAtlasRows();
        return (float) row / model.getTexture().getNumberOfAtlasRows();
    }

    /**
     *
     * @param dx x position
     * @param dy y position
     * @param dz z position
     */
    public void increasePosition(float dx, float dy, float dz){
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    /**
     *
     * @param dx rotation over x axis
     * @param dy rotation over y axis
     * @param dz rotatiion over z axis
     */
    public void increaseRoatation(float dx, float dy, float dz){
        this.rotX+=dx;
        this.rotY+=dy;
        this.rotZ+=dz;
    }

    public TexturedModel getModel() {
        return model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getRotX() {
        return rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public float getScale() {
        return scale;

    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
