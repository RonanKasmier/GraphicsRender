package primitives;

import tests.JFrameTest;
import util.Function3D;
import util.ImageMod;
import util.Math3D;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Mesh {
    public boolean collidable = false;
    public Trigon[] faces;

    //public BufferedImage image;

    public Mesh(Trigon[] faces){
        this.faces = faces;
    }

    public Mesh(Function3D f, float[] extremeTs, float[] extremeUs, float titerator, float uiterator){
        ArrayList<Trigon> trigonlist = new ArrayList<Trigon>();
        float[][][] points = new float[(int)Math.round(Math.abs(extremeTs[1] - extremeTs[0])/titerator)]
                [(int)Math.round(Math.abs(extremeUs[1] - extremeUs[0])/uiterator)]
                [3];
        /*for(float t = extremeTs[0]; t < extremeTs[1]; t+=titerator){
            for(float u = extremeUs[0]; u < extremeUs[1]; u+=uiterator){*/
        for(int i = 0; i < points.length; i++){
            for(int j = 0; j < points[0].length; j++){
                //**careful with Math.round, may have unexpected consequences**
                //was put here to account for small titerator and uiterator values
                //System.out.println(Math3D.lerp(t, extremeTs[0], extremeTs[1], 0, points.length) + " " + Math3D.lerp(u, extremeUs[0], extremeUs[1], 0, points[0].length));
                /*points[(int)Math.round(Math3D.lerp(t, extremeTs[0], extremeTs[1], 0, points.length))]
                        [(int)Math.round(Math3D.lerp(u, extremeUs[0], extremeUs[1], 0, points[0].length))]
                        = f.planeFunctiontoPlane(t, u);*/
                points[i][j] = f.planeFunctiontoPlane(Math3D.lerp(i, 0, points.length, extremeTs[0], extremeTs[1]),
                        Math3D.lerp(j, 0, points[0].length, extremeUs[0], extremeUs[1]));
            }
        }
        faces = new Trigon[(points.length - 0) * (points[0].length - 0) * 2];
        for(int i = 0; i < points.length - 1; i++){
            for(int j = 0; j < points[0].length - 1; j++){
                trigonlist.add(new Trigon(
                        points[i + (((i%2) + j)%4)/2][j + (((i%2) + j+1)%4)/2],
                        points[i + (((i%2) + j + 1)%4)/2][j + (((i%2) + j+2)%4)/2],
                        points[i + (((i%2) + j + 2)%4)/2][j + (((i%2) + j+3)%4)/2]
                ));
                trigonlist.add(new Trigon(
                        points[i + (((i%2) + j + 2)%4)/2][j + (((i%2) + j+3)%4)/2],
                        points[i + (((i%2) + j + 3)%4)/2][j + (((i%2) + j)%4)/2],
                        points[i + (((i%2) + j)%4)/2][j + (((i%2) + j + 1)%4)/2]
                ));
            }
        }
        trigonlist.toArray(faces);
    }

    public Mesh(Function3D f, BufferedImage image, float[] extremeTs, float[] extremeUs, float titerator, float uiterator){
        ArrayList<Trigon> trigonlist = new ArrayList<Trigon>();
        float[][][] points = new float[(int)Math.round(Math.abs(extremeTs[1] - extremeTs[0])/titerator)]
                [(int)Math.round(Math.abs(extremeUs[1] - extremeUs[0])/uiterator)]
                [3];
        /*for(float t = extremeTs[0]; t < extremeTs[1]; t+=titerator){
            for(float u = extremeUs[0]; u < extremeUs[1]; u+=uiterator){*/
        for(int i = 0; i < points.length; i++){
            for(int j = 0; j < points[0].length; j++){
                //**careful with Math.round, may have unexpected consequences**
                //was put here to account for small titerator and uiterator values
                //System.out.println(Math3D.lerp(t, extremeTs[0], extremeTs[1], 0, points.length) + " " + Math3D.lerp(u, extremeUs[0], extremeUs[1], 0, points[0].length));
                /*points[(int)Math.round(Math3D.lerp(t, extremeTs[0], extremeTs[1], 0, points.length))]
                        [(int)Math.round(Math3D.lerp(u, extremeUs[0], extremeUs[1], 0, points[0].length))]
                        = f.planeFunctiontoPlane(t, u);*/
                points[i][j] = f.planeFunctiontoPlane(Math3D.lerp(i, 0, points.length, extremeTs[0], extremeTs[1]),
                        Math3D.lerp(j, 0, points[0].length, extremeUs[0], extremeUs[1]));
            }
        }
        faces = new Trigon[(points.length - 0) * (points[0].length - 0) * 2];
        int[] angle = {0, 90, 180, 270};
        for(int i = 0; i < points.length - 1; i++){
            for(int j = 0; j < points[0].length - 1; j++){
                int twidth = image.getWidth()/(points[0].length-1);
                int theight = image.getHeight()/(points.length - 1);
                BufferedImage tile = image.getSubimage(j*twidth, i*theight, twidth, theight);
                Trigon t1 = new Trigon(
                        points[i + (((i%2) + j)%4)/2][j + (((i%2) + j+1)%4)/2],
                        points[i + (((i%2) + j + 1)%4)/2][j + (((i%2) + j+2)%4)/2],
                        points[i + (((i%2) + j + 2)%4)/2][j + (((i%2) + j+3)%4)/2]
                );
                t1.setImage(ImageMod.rotateImage(tile, angle[((i%2) + j)%4]));
                trigonlist.add(t1);
                Trigon t2 = new Trigon(
                        points[i + (((i%2) + j + 2)%4)/2][j + (((i%2) + j+3)%4)/2],
                        points[i + (((i%2) + j + 3)%4)/2][j + (((i%2) + j)%4)/2],
                        points[i + (((i%2) + j)%4)/2][j + (((i%2) + j + 1)%4)/2]
                );
                t2.setImage(ImageMod.rotateImage(tile, angle[((i%2) + j + 2)%4]));
                trigonlist.add(t2);
            }
        }
        trigonlist.toArray(faces);
    }
}
