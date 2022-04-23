package primitives;

import util.Function3D;

import java.awt.image.BufferedImage;

public class Scene3D {
    //Have parent class "renderObject" with method render
    //Function3D would inherit from renderObject and have its own method to render
    //each object has their own update behavior
    public Camera activeCamera;
    protected Function3D[] functions;

    public float[] initialize(){

        return new float[0];
    }

    public float[] update(){

        return new float[0];
    }

    public float[] render(BufferedImage canvas){

        return new float[0];
    }

}
