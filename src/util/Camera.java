package util;

import java.awt.image.BufferedImage;

public class Camera {

    public float[] position = {0.0f, 0.0f, 0.0f};
    float PN_Length = 0.1f;
    public float[] PN = {PN_Length, 0.0f, 0.0f}; //facing
    public float[] NH = {0.0f, 1.0f, 0.0f}; //remain magnitude of 1
    public float[] NV = {0.0f, 0.0f, 1.0f}; //remain magnitude of 1

    final float[] i = {1, 0 ,0};
    final float[] j = {0, 1 ,0};
    final float[] k = {0, 0 ,1};

    public Camera(){
        position = new float[]{0, 0, 0};
    }

    public float[] project(float[] OB){
        if(Math3D.dotProduct(Math3D.addVectors(OB, Math3D.addVectors(position, PN, 1.0f), -1.0f), PN) < 0){//also a place to put a render distance conditional
            return null; // when system is to be more completely implemented, this check will be done far before this function is called
        }
        //float[] AB = Math3D.addVectors(OB, position, -1.0f);
        //float[] NI = Math3D.addVectors(Math3D.scalarMult(AB, Math3D.magsquared(PN)/Math3D.dotProduct(PN, AB)), PN, -1.0f);
        //return new float[]{Math3D.dotProduct(NI, NH)/Math3D.magnitude(NH), Math3D.dotProduct(NI, NV)/Math3D.magnitude(NV), 0.0f};
        float[] PB = Math3D.addVectors(OB, position, -1.0f);
        float[] I = Math3D.addVectors(Math3D.scalarMult(PB, Math3D.magsquared(PN)/ Math3D.dotProduct(PN, PB)), position, 1.0f);
        float[] NI = Math3D.addVectors(I, Math3D.addVectors(position, PN, 1f), -1f);
        return new float[]{Math3D.dotProduct(NI, NH)/*/Math3D.magnitude(NH)*/, Math3D.dotProduct(NI, NV)/*/Math3D.magnitude(NV)*/, 0.0f};
       /* float t = (Math3D.dotProduct(Math3D.addVectors(position, PN, 1f), PN) - Math3D.dotProduct(position, PN))/Math3D.dotProduct(Math3D.addVectors(OB, position, -1f), PN);
        float[] OI = Math3D.addVectors(Math3D.scalarMult(Math3D.addVectors(OB, position, -1f), t), position, 1f);
        float[] Ni = Math3D.addVectors(OI, Math3D.addVectors(position, PN, 1f), -1f);
        return new float[]{Math3D.dotProduct(Ni, NH)/Math3D.magnitude(NH), Math3D.dotProduct(Ni, NV)/Math3D.magnitude(NV), 0.0f};*/

    }

    public int[] projectToCanvas(BufferedImage canvas, float[] projection){
        return new int[]{(int)(projection[0]*canvas.getWidth() / PN_Length) + canvas.getWidth()/2, (int)(projection[1]*canvas.getHeight()/PN_Length) + canvas.getHeight()/2};
    }

    public void roll(float theta){
        NH = Math3D.rotateAbout(NH, PN, theta);
        NV = Math3D.rotateAbout(NV, PN, theta);
    }
    public void pitch(float theta){
        PN = Math3D.rotateAbout(PN, NH, theta);
        NV = Math3D.rotateAbout(NV, NH, theta);
    }
    public void yaw(float theta){
        PN = Math3D.rotateAbout(PN, k, theta);
        NH = Math3D.rotateAbout(NH, k, theta);
        NV = Math3D.rotateAbout(NV, k, theta);
        
    }

   /* public void setFOV(float depth){ //this does not change the field of view
        PN = Math3D.scalarMult(PN, depth/PN_Length);
        PN_Length = depth;
    }
    */
    public float[] getLooking(){
        return Math3D.scalarMult(PN, 1f/PN_Length);
    }
    
    public void look(float[] point){
        
    }
}
