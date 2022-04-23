package primitives;

import tests.JFrameTest;
import util.Function3D;
import util.Math3D;

import java.util.ArrayList;

public class Mesh {
    public boolean collidable = false;
    public Trigon[] faces;



    public Mesh(Trigon[] faces){
        this.faces = faces;
    }

    public Mesh(Function3D f, float[] extremeTs, float[] extremeUs, float titerator, float uiterator){
        ArrayList<Trigon> trigonlist = new ArrayList<Trigon>();
        float[][][] points = new float[(int)(Math.abs(extremeTs[1] - extremeTs[0])/titerator)]
                [(int)(Math.abs(extremeUs[1] - extremeUs[0])/uiterator)]
                [3];
        for(float t = extremeTs[0]; t < extremeTs[1]; t+=titerator){
            for(float u = extremeUs[0]; u < extremeUs[1]; u+=uiterator){
                //**careful with Math.round, may have unexpected consequences**
                //was put here to account for small titerator and uiterator values
                points[(int)Math.round(Math3D.lerp(t, extremeTs[0], extremeTs[1], 0, points.length))] 
                        [(int)Math.round(Math3D.lerp(u, extremeUs[0], extremeUs[1], 0, points[0].length))]
                        = f.planeFunctiontoPlane(t, u);
            }
        }
        faces = new Trigon[(points.length - 1) * (points[0].length - 1) * 2];
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
}
