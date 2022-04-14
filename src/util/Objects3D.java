package util;

public class Objects3D {

    public static float[][] createCube(float x, float y, float z, float w){
        return new float[][]{
            {x, y, z},
            {x, y + w, z},
            {x + w, y + w, z},
            {x + w, y, z},
            {x + w, y, z + w},
            {x + w, y + w, z + w},
            {x, y + w, z + w},
            {x, y, z + w}
        };
    }
    
    public static float[][] square3D(float x, float y, float z, float w){
        return new float[][]{
                {x, y, z},
                {x + w, y, z},
                {x, y, z + w},
                {x + w, y, z + w}
        };
    }

}
