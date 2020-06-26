package pt.globaltronic.jogltrials.models;

import pt.globaltronic.jogltrials.textures.ModelTexture;

public class TexturedModel {

    private RawModel rawModel;
    private ModelTexture modelTexture;

    public TexturedModel(RawModel model, ModelTexture modelTexture){
        this.rawModel = model;
        this.modelTexture = modelTexture;
    }

    public ModelTexture getModelTexture() {
        return modelTexture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }
}
