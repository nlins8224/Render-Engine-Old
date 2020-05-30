package EngineDisplay;

import Camera.Camera;
import Entities.Entity;
import Light.Light;
import Models.ModelTexture;
import terrain.TerrainTexture;
import Models.TexturedModel;
import org.joml.Vector3f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import renderEngine.Loader;
import Models.RawModel;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrain.Terrain;
import terrain.TerrainTexturePack;

import java.io.IOException;
import java.nio.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * This is Display class. It uses GLFW to handle window and main game loop.
 */

public class EngineDisplay {

    // The window handle
    private long window;

    private static final int WIDTH = 800, HEIGHT = 600;

    public static int getWidth() {
        return WIDTH;
    }

    public static int getHeigth() {
        return HEIGHT;
    }

        public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        try {
            loop();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to load file");


        }

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    /**
     *
     * @throws IOException if filepath is not found
     */
    private void loop()  throws IOException {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        Loader loader = new Loader();

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("res/grassy.png"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("res/dirt.png"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("res/grass.png"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("res/water.png"));
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture,
                gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("res/blendMap.png"));
        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");

        RawModel treeRawModel = OBJLoader.loadObjModel("tree", loader);
        RawModel fernRawModel = OBJLoader.loadObjModel("fern", loader);


        TexturedModel fern = new TexturedModel(fernRawModel, new ModelTexture(loader.loadTexture("res/fernAtlas.png")));

        ModelTexture fernTextureAtlas = fern.getTexture();
        fernTextureAtlas.setNumberOfAtlasRows(2);


        TexturedModel treeTexturedModel = new TexturedModel(treeRawModel, new ModelTexture(loader.loadTexture("res/tree.png")));
        ModelTexture treeModelTex = treeTexturedModel.getTexture();
        treeModelTex.setShineDamper(3);
        treeModelTex.setReflectivity(0.2f);
        treeModelTex.setHasTransparency(true);

        List<Entity> fernEntities = new ArrayList<Entity>();
        List<Entity> treeEntities = new ArrayList<Entity>();
        Random random = new Random(213414);
        for (int  i = 0; i < 1000; i++){
            if (i % 100 == 0){
                float x = random.nextFloat() * 500 + 40;
                float z = random.nextFloat() * 500 - 600;
                float y = terrain.getHeightOfTerrain(x, z);

                float x2 = random.nextFloat() * 600 + 40;
                float z2 = random.nextFloat() * 400 - 600;
                float y2 = terrain.getHeightOfTerrain(x2, z2);
                treeEntities.add(new Entity (treeTexturedModel, new Vector3f(x, y, z), 0, 0, 0, 20));
                fernEntities.add (new Entity (fern, new Vector3f(x2, y2, z2),0,0,0,1, random.nextInt(4)));

            }
        }

        Light sun = new Light(new Vector3f(0,30000, -1000), new Vector3f(1,1,1));
        List<Light> lights = new ArrayList<Light>();
        lights.add(sun);
        lights.add(new Light (new Vector3f (-200, 10, -200), new Vector3f(10, 0, 0)));
        lights.add(new Light (new Vector3f (200, 10, 200), new Vector3f(0, 0, 10)));

        Camera camera = new Camera(window);

        MasterRenderer renderer = new MasterRenderer();
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
            camera.move();

            renderer.processTerrain(terrain);

            for (Entity entity : treeEntities)
                renderer.processEntity(entity);
            for (Entity entity : fernEntities)
                renderer.processEntity(entity);
            renderer.render(lights, camera);




            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();


        }
        renderer.cleanUp();
        loader.cleanUp();

    }


    public static void main(String[] args) {
        new EngineDisplay().run();
    }

}