package util;
import java.lang.Math;

public static class Math3D {
    public static float dotProduct(float[] a, float[] b){
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }
    public static float[] crossProduct(float[] a, float[] b){
        return new float[]{a[1]*b[2] - a[2]*b[1], a[2]*b[0] - a[0]*b[2], a[0]*b[1] - a[1]*b[0]};
    }

    public static float[] addVectors(float[] A, float[] B, float sign){
        return new float[]{A[0] + sign*B[0], A[1] + sign*B[1], A[2] + sign*B[2]};
    }

    public static float[] scalarMult(float[] A, float scalar){
        return new float[]{A[0]*scalar, A[1]*scalar, A[2]*scalar};
    }

    public static float[] reflect(float[] AP, float AB[]){
        return addVectors(scalarMult(AB, 2), AP, -1);
    }

    public static float magnitude(float[] A) {
        return (float)Math.sqrt(A[0]*A[0] + A[1]*A[1] + A[2]*A[2]);
    }

    public static float magsquared(float[] A){
        return A[0]*A[0] + A[1]*A[1] + A[2]*A[2];
    }
    
    public static distanceBetweenSquared(float[] A, float[] B){
        float d1 = B[0] - A[0];
        float d2 = B[1] - A[1];
        float d3 = B[2] - A[2];
        return d1*d1 + d2*d2 + d3*d3;
    }

    public static float[] rotateAbout(float[] OB, float[] OA, float theta){
        float[] OC = scalarMult(OA, dotProduct(OB, OA) * 1.0f/magsquared(OA));
        float[] CB = addVectors(OB, OC, -1.0f);
        float[] CK = crossProduct(CB, OA);
        if(magsquared(CK) < 0.000001){
            return new float[]{OB[0], OB[1], OB[2]};
        }
        float[] CR = addVectors(scalarMult(CB, (float)Math.cos(theta)), scalarMult(CK, magnitude(CB)*(float)Math.sin(theta)/magnitude(CK)), 1.0f);
        return addVectors(CR, OC, 1.0f);
    }

    public static float percent(float n, float min, float max){
        return (n - min)/(max - min);
    }

    public static float percent_inverse(float percent, float min, float max){
        return min + percent*(max - min);
    }

    public static float lerp(float n, float min1, float max1, float min2, float max2){
        return min2 + (max2 - min2)*(n - min1)/(max1 - min1);
    }
}