package util;

public abstract class Function3D {

    public float[] a = {1, 0, 0};
    public float[] b = {0, 1, 0};
    public float[] c = {0, 0, 1};


    public abstract double[] function2D(double s); //return in form <t, u>

    public abstract double[] planeFunction(double t, double u); //return in form <f, g, h>

    public float[] function2DtoPlane(double s){
        double[] v = function2D(s);
        double[] P = planeFunction(v[0], v[1]);
        return Math3D.addVectors(Math3D.addVectors(Math3D.scalarMult(a, (float)P[0]), Math3D.scalarMult(b, (float)P[1]), 1), Math3D.scalarMult(c, (float)P[2]), 1);
    }
    /*public void setAxis(double[] a, double[] b, double[] c){

    }*/
}
