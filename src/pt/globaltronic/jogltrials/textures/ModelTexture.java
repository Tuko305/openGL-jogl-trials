package pt.globaltronic.jogltrials.textures;

import com.jogamp.opengl.util.texture.Texture;

public class ModelTexture {

    private int textureID;

    public ModelTexture(int id){
        this.textureID = id;
    }

    public int getTextureID() {
        return textureID;
    }
}
