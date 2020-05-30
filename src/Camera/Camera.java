package Camera;

import org.joml.Vector3f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.*;

/**
 * This class allows user to move around the world
 */
public class Camera {

    private Vector3f cameraPosition = new Vector3f(0, 0, 0);
    private static long window;
    private float pitch = 0;
    private float yaw = 0;
    private float roll = 0;

    private double scrollValue = 0;
    private int clickValue = -1;
    private float lastValueY = 0;
    private float lastValueX = 0;
    private double mouseDx = 0;
    private double mouseDy = 0;
    private double angleAroundCamera = 0;
    private double distanceFromCamera = 0;

    private boolean inWire = false;
    private boolean beingPressed = false;

    /**
     *
     * @param window engine window
     */
    public Camera(long window){
        this.window = window;
        GLFW.glfwSetScrollCallback(window, GLFWScrollCallback.create(this::glfwScrollCallback));
        GLFW.glfwSetMouseButtonCallback(window, GLFWMouseButtonCallback.create(this::glfwMouseButtonCallback));
        GLFW.glfwSetCursorPosCallback(window, GLFWCursorPosCallback.create(this::glfwCursorPos));

        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);

    }

    /**
     *
     */
    public void move(){
        checkInputs();
        calculateZoom();
        calculatePitch();
        calculateAngle();
    }

    private void checkInputs(){

        glfwSetKeyCallback(window, GLFWKeyCallback.create((window, key, scancode, action, mods) -> {

            if ( key == GLFW_KEY_W ) {
                this.cameraPosition.z -= 6.5f;
            }

            else if ( key == GLFW_KEY_S ) {
                this.cameraPosition.z += 6.5f;
            }


            if ( key == GLFW_KEY_A ) {
                this.cameraPosition.x -= 6.5f;
            }

            else if ( key == GLFW_KEY_D ) {
                this.cameraPosition.x += 6.5f;
            }
            if ( key == GLFW_KEY_E ) {
                this.cameraPosition.y -= 6.5f;
            }

            else if ( key == GLFW_KEY_Q ) {
                this.cameraPosition.y += 6.5f;
            }

        }));

    }

    public Vector3f getCameraPosition() {
        return cameraPosition;
    }

    /**
     *
     * @param window engine window
     * @param dx x coordinate
     * @param dy y coordinate
     */
    public void glfwScrollCallback(long window, double dx, double dy){
        scrollValue = dy;
    }

    /**
     *
     * @param window engine window
     * @param button mouse button
     * @param action
     * @param mods
     */
    public void glfwMouseButtonCallback(long window, int button, int action, int mods){
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {
            clickValue = 0;
        } else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && action == GLFW.GLFW_PRESS) {
            clickValue = 1;
        } else if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE && action == GLFW.GLFW_PRESS) {
            clickValue = 2;
        } else {
            clickValue = -1;
        }
    }

    /**
     *
     * @param window engine window
     * @param dx x coordinate
     * @param dy y coordinate
     */
    public void glfwCursorPos(long window, double dx, double dy){
        mouseDx = dy;
        mouseDy = dy;
    }

    private void calculateZoom(){
        float zoomLevel = (float)(scrollValue);
        distanceFromCamera -= zoomLevel;
        scrollValue = 0;
    }

    private void calculateAngle() {

        if(clickValue == 0) {
            float angleChange = (float) mouseDx; // *0.1f
            yaw += angleChange * 0.01f;
        }
        lastValueX = (float) mouseDx;
    }



    private void calculatePitch(){
        if(clickValue == 1) {
            float pitchChange = (float)mouseDy - lastValueY; // *0.1f
            pitch -= pitchChange;
        }

        lastValueY = (float) mouseDy;
    }

    private void calculateCameraPosition(float horizontalDistance, float verticalDistance){
        float theta = yaw;
        float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));

        cameraPosition.x = cameraPosition.x - offsetX;
        cameraPosition.z = cameraPosition.z - offsetZ;
        cameraPosition.y = cameraPosition.y + verticalDistance;

    }
    private float calculateHorizontalDistance() {
        return (float) Math.cos(Math.toRadians(pitch));
    }

    private float calculateVerticalDistance() {
        return (float)  Math.sin(Math.toRadians(pitch));
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
