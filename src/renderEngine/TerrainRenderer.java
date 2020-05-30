package renderEngine;

import Shaders.TerrainShader;
import Models.RawModel;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import terrain.Terrain;
import terrain.TerrainTexturePack;
import toolbox.Matrices;

import java.util.List;

/**
 * This class renders terrain
 */
public class TerrainRenderer {

    private TerrainShader shader;

    /**
     *
     * @param shader specifies shader used to process terrain
     * @param projectionMatrix specifies projection matrix used to process terrain
     */
    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix){
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.connectTextureUnits();
        shader.stop();
    }

    /**
     *
     * @param terrains specifies all the terrains rendered
     */
    public void render(List<Terrain> terrains){
        for (Terrain terrain:terrains){
            prepareTerrain(terrain);
            loadModelMatrix(terrain);
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(),
                    GL11.GL_UNSIGNED_INT, 0);

            unbindTerrainModel();

        }
    }


    private void prepareTerrain(Terrain terrain){
        RawModel rawModel = terrain.getModel();

        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        bindTextures(terrain);
        shader.loadShineVariables(1, 0); //temporary 1 and 0 will add in the future



    }

    private void bindTextures(Terrain terrain){
        TerrainTexturePack texturePack = terrain.getTexturePack();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexuture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexuture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexuture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexuture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,  terrain.getBlendMap().getTextureID());
    }

    private void unbindTerrainModel(){
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);

    }

    private void loadModelMatrix(Terrain terrain){
        Matrix4f transformationMatrix = Matrices.createTransformationMatrix(
                new Vector3f(terrain.getX(), 0, terrain.getZ()), 0,
                0,0, 1);
        shader.loadTransformationMatrix(transformationMatrix);
    }


}
