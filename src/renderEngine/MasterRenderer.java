package renderEngine;

import EngineDisplay.EngineDisplay;
import Camera.Camera;
import Entities.Entity;
import Light.Light;
import Shaders.StaticShader;
import Shaders.TerrainShader;
import Models.TexturedModel;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import terrain.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class handles rendering
 */
public class MasterRenderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 100000;

    private static final float RED = 0.5f;
    private static final float GREEN = 0.5f;
    private static final float BLUE = 0.8f;

    private Matrix4f projectionMatrix;
    private EntityRenderer renderer;

    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();

    private StaticShader shader = new StaticShader();
    private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List <Entity>>();
    private List<Terrain> terrains = new ArrayList<Terrain>();


    /**
     *
     */
    public MasterRenderer(){
        enableCulling();
        createProjectionMatrix();
        renderer = new EntityRenderer(shader, projectionMatrix);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);

    }

    /**
     *
     * @param lights List of light sources in the world
     * @param camera Camera
     */
    public void render(List<Light> lights, Camera camera){
        prepare();
        shader.start();
        shader.loadSkyColor(RED, GREEN, BLUE);
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);
        renderer.render(entities);
        shader.stop();
        terrainShader.start();
        terrainShader.loadSkyColor(RED, GREEN, BLUE);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        terrains.clear();
        entities.clear();
    }

    /**
     *
     * @param terrain
     */
    public void processTerrain(Terrain terrain){
        terrains.add(terrain);


    }

    /**
     * Enables culling
     */
    public static void enableCulling(){
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    /**
     * Disables culling
     */

    public static void disableCulling(){
        GL11.glDisable(GL11.GL_CULL_FACE);

    }

    /**
     *
     * @param entity Entity object
     */
    public void processEntity(Entity entity){
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if(batch!=null){
            batch.add(entity);

        }else{
            List<Entity> newBatch = new ArrayList<Entity>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);

        }
    }

    public void cleanUp(){
        shader.cleanUp();
        terrainShader.cleanUp();
    }

    private void prepare(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(RED, GREEN, BLUE,1);
    }

    private void createProjectionMatrix() {
        projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(FOV),
                (float) EngineDisplay.getWidth()/EngineDisplay.getHeigth(), NEAR_PLANE, FAR_PLANE);

    }
}
