package Shaders;

import Camera.Camera;
import Light.Light;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import toolbox.Matrices;

import java.util.List;

/**
 * This is shader class used to process Terrain
 */
public class TerrainShader extends ShaderProgram {

        private static final int MAX_LIGHTS = 4; // Change in shader also

        private static final String VERTEX_FILE = "src/Shaders/terrainVertexShader.txt";
        private static final String FRAGMENT_FILE = "src/Shaders/terrainFragmentShader.txt";

        private int location_transformationMatrix;
        private int location_projectionMatrix;
        private int location_viewMatrix;
        private int location_lightPosition[];
        private int location_lightColor[];
        private int location_shineDamper;
        private int location_reflectivity;
        private int location_skyColor;
        private int location_backgroundTexture;
        private int location_rTexture;
        private int location_gTexture;
        private int location_bTexture;
        private int location_blendMap;

        public TerrainShader() {
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
            location_skyColor = super.getUniformLocation("skyColor");

            location_backgroundTexture = super.getUniformLocation("backgroundTexture");
            location_rTexture = super.getUniformLocation("rTexture");
            location_gTexture = super.getUniformLocation("gTexture");
            location_bTexture = super.getUniformLocation("bTexture");
            location_blendMap = super.getUniformLocation("blendMap");

            location_lightPosition = new int[MAX_LIGHTS];
            location_lightColor = new int[MAX_LIGHTS];

        }

    /**
     *
     */
        public void connectTextureUnits(){
            super.loadInt(location_backgroundTexture, 0);
            super.loadInt(location_rTexture, 1);
            super.loadInt(location_gTexture, 2);
            super.loadInt(location_bTexture, 3);
            super.loadInt(location_blendMap, 4);
        }

    /**
     *
     * @param damper specifies damper
     * @param reflectivity specifies reflectivity
     */
        public void loadShineVariables(float damper, float reflectivity){
            super.loadFloat(location_shineDamper, damper);
            super.loadFloat(location_reflectivity, reflectivity);
        }

    /**
     *
     * @param r red variable
     * @param g green variable
     * @param b blue variable
     */
        public void loadSkyColor (float r, float g, float b){
             super.load3DVector(location_skyColor, new Vector3f(r,g,b));
         }

    /**
     *
     * @param matrix specifies transformation matrix
     */
        public void loadTransformationMatrix(Matrix4f matrix){
            super.loadMatrix(location_transformationMatrix, matrix);
        }

    /**
     *
     * @param lights specifies list of lights
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
     *
     * @param camera specifies camera
     */
        public void loadViewMatrix(Camera camera){
            Matrix4f viewMatrix = Matrices.createViewMatrix(camera);
            super.loadMatrix(location_viewMatrix, viewMatrix);
        }

    /**
     *
     * @param projection specifies projection matrix
     */
        public void loadProjectionMatrix(Matrix4f projection){
            super.loadMatrix(location_projectionMatrix, projection);
        }

    }


