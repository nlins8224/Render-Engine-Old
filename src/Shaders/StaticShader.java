package Shaders;

import Camera.Camera;
import Light.Light;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import toolbox.Matrices;

import java.util.List;

/**
 * This is shader class used to process entities
 */

public class StaticShader extends ShaderProgram {

    private static final int MAX_LIGHTS = 4; // Change in shader also

    private static final String VERTEX_FILE = "src/Shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "src/Shaders/fragmentShader.txt";

    private int location_transformationMatrix;
    private  int location_projectionMatrix;
    private int location_viewMatrix;
    private int[] location_lightPosition;
    private int[] location_lightColor;
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_useFakeLighting;
    private int location_skyColor;
    private int location_numberOfRows;
    private int location_offset;

    /**
     *
     */
    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations(){
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_useFakeLighting = super.getUniformLocation("useFakeLighting");
        location_skyColor = super.getUniformLocation("skyColor");
        location_numberOfRows = super.getUniformLocation("numberOfRows");
        location_offset = super.getUniformLocation("offset");

        location_lightPosition = new int[MAX_LIGHTS];
        location_lightColor = new int[MAX_LIGHTS];

        for (int i = 0; i < MAX_LIGHTS; i++ ){
            location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            location_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
        }



    }

    /**
     *
     * @param numberOfRows specifies number of rows of a texture atlas
     */
    public void loadNumberOfRows(int numberOfRows){
        super.loadFloat(location_numberOfRows, numberOfRows);
    }

    /**
     * loads 2D vector to the location of an offset
     * @param x x position of an offset
     * @param y y position of an offset
     */
    public void loadOffset(float x, float y){
        super.load2DVector(location_offset, new Vector2f(x, y));
    }

    /**
     * Loads color of the sky
     * @param r red color parameter
     * @param g blue color parameter
     * @param b green color parameter
     */
    public void loadSkyColor (float r, float g, float b){
        super.load3DVector(location_skyColor, new Vector3f(r,g,b));
    }

    /**
     *
     * @param damper damper variable
     * @param reflectivity reflectivity variable
     */
    public void loadShineVariables(float damper, float reflectivity){
        super.loadFloat(location_shineDamper, damper);
        super.loadFloat(location_reflectivity, reflectivity);
    }

    /**
     *
     * @param useFake Enables or disables fake lighting
     */
    public void loadFakeLightingVariable(boolean useFake){
        super.loadBoolean(location_useFakeLighting, useFake);
    }

    /**
     * Loads transformation matrix
     * @param matrix is a transformation matrix
     */
    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    /**
     * Loads light sources
     * @param lights is the list of all light sources
     */
    public void loadLights(List<Light> lights){
       for (int i = 0; i < MAX_LIGHTS; i++){
           if(i < lights.size()){
               super.load3DVector(location_lightPosition[i], lights.get(i).getPosition());
               super.load3DVector(location_lightColor[i], lights.get(i).getColor());
           }else{ // Load empty info
               super.load3DVector(location_lightPosition[i], new Vector3f(0, 0, 0));
               super.load3DVector(location_lightColor[i], new Vector3f(0, 0, 0));
           }
       }

    }

    /**
     * Loads view matrix
     * @param camera is main camera object
     */
    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Matrices.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    /**
     * Loads projection matrix
     * @param projection is projection matrix
     */
    public void loadProjectionMatrix(Matrix4f projection){
        super.loadMatrix(location_projectionMatrix, projection);
    }

}
