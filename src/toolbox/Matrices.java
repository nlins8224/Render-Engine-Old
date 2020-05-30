package toolbox;

import Camera.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * This class contains Tranformation and View matricess
 */
public class Matrices {


    /**
     *
     * @param translation xyz translation vector
     * @param rx rotation over x axis
     * @param ry rotation over y axis
     * @param rz rotation over z axis
     * @param scale scale
     * @return transformation matrix
     */
    public static Matrix4f createTransformationMatrix(Vector3f translation,
                                                      float rx, float ry, float rz, float scale){

        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.translate(translation);
        matrix.rotate((float) Math.toRadians(rx), 1.0f, 0.0f, 0.0f);
        matrix.rotate((float) Math.toRadians(ry), 0.0f, 1.0f, 0.0f);
        matrix.rotate((float) Math.toRadians(rz), 0.0f, 0.0f, 1.0f);
        matrix.scale(scale);
        return matrix;
    }

    /**
     * view from the eye of camera
     * @param camera specifies camera used to create viewMatrix
     * @return view matrix
     */
    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.identity();
        viewMatrix.rotate( (float) Math.toRadians(camera.getPitch()), 1.0f, 0.0f, 0.0f);
        viewMatrix.rotate((float) Math.toRadians(camera.getYaw()), 0.0f, 1.0f, 0.0f);
        viewMatrix.rotate((float) Math.toRadians(camera.getRoll()), 0.0f, 0.0f, 1.0f);


        Vector3f cameraPos = camera.getCameraPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        viewMatrix.translate(negativeCameraPos, viewMatrix);
        return viewMatrix;

    }



}
