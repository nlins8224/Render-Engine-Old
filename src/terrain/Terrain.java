package terrain;

import Models.RawModel;
import org.joml.Vector2f;
import org.joml.Vector3f;
import renderEngine.Loader;
import toolbox.Maths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This class is used to generate terrain with it's attributes; textures, height map, blend map, position and size
 */
public class Terrain {

    private static final float SIZE = 800;
    private static final float MAX_HEIGHT = 140;
    private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;


    private float x;
    private float z;
    private RawModel model;
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;

    private float[][] terrainHeights;

    /**
     *
     * @param gridX position X of a terrain
     * @param gridZ position Z of a terrain
     * @param loader loader used to load terrain
     * @param texturePack terrain texture pack
     * @param blendMap terrain blend map
     * @param heightMap terrain height map
     */
    public Terrain(int gridX, int gridZ, Loader loader,
                   TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap){
        this.texturePack = texturePack;
        this.blendMap = blendMap;
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.model = generateTerrain(loader, heightMap);

    }

    /**
     * gets height of terrain by using bary centric method
     * @param worldX position X on a terrain
     * @param worldZ position y on a terrain
     * @return terrain height
     */
    public float getHeightOfTerrain(float worldX, float worldZ){
        float terrainX = setupCoordinateX(worldX);
        float terrainZ = setupCoordinateZ(worldZ);
        float gridSquareSize = SIZE / ((float)terrainHeights.length - 1);
        int gridCoordinateX = findGridSquareCoordinateX(terrainX, gridSquareSize);
        int gridCoordinateZ = findGridSquareCoordinateZ(terrainZ, gridSquareSize);
        float xCoord = findXOnGridSquare(worldX, gridSquareSize);
        float zCoord = findZOnGridSquare(worldZ, gridSquareSize);
        float heightOfTerrain;
        if (xCoord <= (1-zCoord)) {
            heightOfTerrain = Maths
                    .barryCentric(new Vector3f(0, terrainHeights[gridCoordinateX][gridCoordinateZ], 0), new Vector3f(1,
                            terrainHeights[gridCoordinateX + 1][gridCoordinateZ], 0), new Vector3f(0,
                            terrainHeights[gridCoordinateX][gridCoordinateZ + 1], 1), new Vector2f(xCoord, zCoord));
        } else {
            heightOfTerrain = Maths
                    .barryCentric(new Vector3f(1, terrainHeights[gridCoordinateX + 1][gridCoordinateZ], 0), new Vector3f(1,
                            terrainHeights[gridCoordinateX + 1][gridCoordinateZ + 1], 1), new Vector3f(0,
                            terrainHeights[gridCoordinateX][gridCoordinateZ + 1], 1), new Vector2f(xCoord, zCoord));
        }
        return heightOfTerrain;


    }

    private float setupCoordinateX(float worldX){
        return worldX - this.x;
    }

    private float setupCoordinateZ(float worldZ){
        return worldZ - this.z;
    }

    private int findGridSquareCoordinateX(float worldX, float gridSquareSize){

        int gridX = (int) Math.floor(worldX / gridSquareSize);
        if (gridX >= terrainHeights.length - 1 ||  gridX < 0 ){
            return 0;
        }
        return gridX;

    }

    private int findGridSquareCoordinateZ(float worldZ, float gridSquareSize){
        int gridZ = (int) Math.floor(worldZ / gridSquareSize);
        if ( gridZ >= terrainHeights.length - 1 || gridZ < 0 ){
            return 0;
        }
        return gridZ;
    }

    private float findXOnGridSquare(float worldX, float gridSquareSize){
        return (worldX % gridSquareSize) / gridSquareSize;
    }

    private float findZOnGridSquare(float worldZ, float gridSquareSize){
        return (worldZ % gridSquareSize) / gridSquareSize;
    }



    private RawModel generateTerrain(Loader loader, String heightMap){

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("res/" + heightMap + ".png"));
        } catch (IOException e){
            e.printStackTrace();
        }



        int VERTEX_COUNT = image.getHeight();
        terrainHeights = new float[VERTEX_COUNT][VERTEX_COUNT];
        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
                float terrainHeight = getHeight(j, i, image);
                terrainHeights[j][i] = terrainHeight;
                vertices[vertexPointer*3+1] = getHeight(j, i, image);
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
                Vector3f normal = calculateNormal(j, i, image);
                normals[vertexPointer*3] = normal.x;
                normals[vertexPointer*3+1] = normal.y;
                normals[vertexPointer*3+2] = normal.z;
                textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    private Vector3f calculateNormal(int x, int z, BufferedImage image){
        float heightL = getHeight(x - 1, z, image);
        float heightR = getHeight(x + 1, z, image);
        float heightD = getHeight (x, z - 1, image);
        float heightU = getHeight(x, z + 1, image);
        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalize();
        return normal;

    }

    private float getHeight (int x, int z, BufferedImage image){
        if( x < 0 || x >= image.getHeight() || z < 0 || z >= image.getHeight()){
            return 0;
        }
        float height = image.getRGB(x, z);
        height += MAX_PIXEL_COLOR/2F;
        height /= MAX_PIXEL_COLOR;
        height *= MAX_HEIGHT;
        return height;

    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public RawModel getModel() {
        return model;
    }

    public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }


}
