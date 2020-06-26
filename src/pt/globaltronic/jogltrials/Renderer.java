package pt.globaltronic.jogltrials;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.math.Matrix4;
import pt.globaltronic.jogltrials.entity.Entity;
import pt.globaltronic.jogltrials.models.RawModel;
import pt.globaltronic.jogltrials.models.TexturedModel;
import pt.globaltronic.jogltrials.toolbox.Maths;

import java.util.List;
import java.util.Map;

public class Renderer {

    GL3 gl;
    private static final float FOV = 70f;
    private static final float NEAR_PLANE = 0.5f;
    private static final float FAR_PLANE = 1000.0f;
    private  float[] projectionMatrix;

    private MyShaderProgram shader;

    public Renderer(GL3 gl, MyShaderProgram shader){
        this.shader = shader;
        this.gl = gl;
        //cull face will improve performance, cutting away triangles pointing away from the camera.
        gl.glEnable(gl.GL_CULL_FACE);
        gl.glCullFace(gl.GL_BACK);
        createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void prepare(){
        gl.glEnable(gl.GL_DEPTH_TEST);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(1, 0, 0, 0);
    }

    public void render(Map<TexturedModel, List<Entity>> entities){
        for(TexturedModel model:entities.keySet()){
            prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);
            for(Entity entity:batch){
                prepareInstance(entity);
                gl.glDrawElements(GL.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }

    }

    public void prepareTexturedModel (TexturedModel model){
        RawModel rawModel = model.getRawModel();
        gl.glBindVertexArray(rawModel.getVaoID());
        gl.glEnableVertexAttribArray(0);
        gl.glEnableVertexAttribArray(1);
        gl.glEnableVertexAttribArray(2);
        gl.glActiveTexture(gl.GL_TEXTURE0);
        gl.glBindTexture(gl.GL_TEXTURE_2D, model.getModelTexture().getTextureID());
    }

    private void unbindTexturedModel(){
        gl.glDisableVertexAttribArray(0);
        gl.glDisableVertexAttribArray(1);
        gl.glDisableVertexAttribArray(2);
        gl.glBindVertexArray(0);
    }

    private void prepareInstance (Entity entity){
        Matrix4 transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
    }

    /*
    public void render(Entity entity, MyShaderProgram shader){
        TexturedModel texturedModel = entity.getModel();
        RawModel model = texturedModel.getRawModel();
        gl.glBindVertexArray(model.getVaoID());
        gl.glEnableVertexAttribArray(0);
        gl.glEnableVertexAttribArray(1);

        //rot in degrees
        Matrix4 transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);

        gl.glActiveTexture(gl.GL_TEXTURE0);
        gl.glBindTexture(gl.GL_TEXTURE_2D, texturedModel.getModelTexture().getTextureID());
        gl.glDrawElements(GL.GL_TRIANGLES, model.getVertexCount(), GL.GL_UNSIGNED_INT, 0);
        gl.glDisableVertexAttribArray(0);
        gl.glDisableVertexAttribArray(1);
        gl.glBindVertexArray(0);
    }

     */

    private void createProjectionMatrix(){
        float aspectRatio = (float) 1028 / 768;
        float y_scale = (float) ((1f / (Math.tan(Math.toRadians(FOV / 2f)))));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;


        float[] projectionMatrixData = {
                x_scale, 0, 0, 0,
                0, y_scale, 0, 0,
                0, 0, -((FAR_PLANE + NEAR_PLANE) / frustum_length), -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length),
                0, 0, -1, 0
        };

        projectionMatrix = projectionMatrixData;

    }

    public float[] getProjectionMatrix() {
        return projectionMatrix;
    }
}
