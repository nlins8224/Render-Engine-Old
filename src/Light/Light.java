package Light;

import org.joml.Vector3f;

public class Light {
    private Vector3f position;
    private Vector3f color;
    private Vector3f attenuation = new Vector3f(1, 0, 0);


    /**
     *
     * @param position xyz position of light in the world
     * @param color rgb color of the light
     */
    public Light(Vector3f position, Vector3f color) {
        this.position = position;
        this.color = color;
    }

    /**
     *
     * @param position xyz position of light in the world
     * @param color rgb color of the light
     * @param attenuation attenuation of light
     */
    public Light(Vector3f position, Vector3f color, Vector3f attenuation) {
        this.position = position;
        this.color = color;
        this.attenuation = attenuation;
    }

    public Vector3f getAttenuation(){
        return attenuation;
    }


    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }
}