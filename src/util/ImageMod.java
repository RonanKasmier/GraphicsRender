package util;
import tests.JFrameTest;

import java.awt.image.BufferedImage;

public class ImageMod {
    public static void fillRect(BufferedImage canvas, final int x, final int y, final int width, final int height, final int color){
        for(int i = x; i < width + x; i++) {
            if (i < canvas.getWidth() && i >= 0) {
                for (int j = y; j < height + y; j++) {
                    if (j < canvas.getHeight() && j >= 0) {
                        canvas.setRGB(i, j, color);

                    }
                }
            }
        }
    }

    public static void drawQuickLine(BufferedImage canvas, int[] p1, int[] p2, int color, final int iterator){
        //iterator must greater than or equal to 1
        int dx = p2[0] - p1[0];
        int dy = p2[1] - p1[1];
        if(Math.abs(dy) > Math.abs(dx)){
            float m = (float) dx/dy;
            if (dy < 0) {
                for (int i = 0; i > dy; i -= iterator) {
                    if (p1[0] + (int)(m * i) < canvas.getWidth() && p1[0] + (int)(m * i) >= 0 && p1[1] + i < canvas.getHeight() && p1[1] + i >= 0) {
                        canvas.setRGB(p1[0] + (int)(m * i), p1[1] + i, color);
                    }
                }
            } else {
                for (int i = 0; i < dy; i += iterator) {
                    if (p1[0] + (int)(m * i) < canvas.getWidth() && p1[0] + (int)(m * i) > 0 && p1[1] + i < canvas.getHeight() && p1[1] + i >= 0) {
                        canvas.setRGB(p1[0] + (int) (m * i), p1[1] + i, color);
                    }
                }
            }
        }else {
            float m = (float) dy / dx;
            if (dx < 0) {
                for (int i = 0; i > dx; i -= iterator) {
                    if (p1[0] + i < canvas.getWidth() && p1[0] + i >= 0 && p1[1] + (int) (m * i) < canvas.getHeight() && p1[1] + (int) (m * i) >= 0) {
                        canvas.setRGB(p1[0] + i, p1[1] + (int) (m * i), color);
                    }
                }
            } else {
                for (int i = 0; i < dx; i += iterator) {
                    if (p1[0] + i < canvas.getWidth() && p1[0] + i > 0 && p1[1] + (int) (m * i) < canvas.getHeight() && p1[1] + (int) (m * i) >= 0) {
                        canvas.setRGB(p1[0] + i, p1[1] + (int) (m * i), color);
                    }
                }
            }
        }
    }

    public static void drawQuickLine(BufferedImage canvas, int[] p1, int[] p2, int color){
        drawQuickLine(canvas, p1, p2, color, 2);
    }

    public static void drawImage(BufferedImage image, BufferedImage canvas, int x, int y, int w, int h){
        int canvasW = canvas.getWidth();
        int canvasH = canvas.getHeight();
        float xratio = (float)image.getWidth()/w;
        float yratio = (float)image.getHeight()/h;

        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                int canvasX = i + x;
                int canvasY = j + y;
                if(canvasY < canvasH && canvasY >= 0 && canvasX < canvasW && canvasX >= 0) {
                    canvas.setRGB(canvasX, canvasY, image.getRGB((int)(i*xratio), (int)(j*yratio)));
                }
            }
        }
    }

    public static void drawImage(BufferedImage image, BufferedImage canvas, int[] A, int[] B, int[] C, int[] D){
        int[] AB = {B[0]-A[0], B[1]-A[1]};
        int[] CD = {D[0]-C[0], D[1]-C[1]};
        int[] AC = {C[0]-A[0], C[1]-A[1]};

        /*int canvW = 400;*/ int canvW = canvas.getWidth();
        /*int canvH = 400;*/ int canvH = canvas.getHeight();
        int imgW = image.getWidth();
        int imgH = image.getHeight();

        int width = 0;
        int width1 = (int)Math.hypot(AB[0], AB[1]);
        int width2 = (int)Math.hypot(CD[0], CD[1]);
        if(width1 > width2){
            width = width1;
        }else{
            width = width2;
        }

        for(int i = 0; i < width; i++){
            float t = (float)i/width;
            int imgX = (int)(t*imgW);
            float[] AY = {Math.round(t*AB[0]), Math.round(t*AB[1])};
            float[] AZ = {t*CD[0] + AC[0], t*CD[1] + AC[1]};
            float[] YZ = {AZ[0]-AY[0], AZ[1]-AY[1]};
            int height = (int)Math.hypot(YZ[0], YZ[1]);
            for(int j = 0; j < height; j++){
                float s = (float)j/height;
                int imgY = (int)(s*imgH);
                float[] AX = {Math.round(s*YZ[0]) + AY[0], Math.round(s*YZ[1]) + AY[1]};
                int[] X = {(int)AX[0] + A[0], (int)AX[1] + A[1]};
                if(!(X[0] < 0 || X[0] >= canvW || X[1] < 0 || X[1] >= canvH)) {
                    /*canvas[X[0]*400 + X[1]] = image.getRGB(imgX, imgY);*/ canvas.setRGB(X[0], X[1], image.getRGB(imgX, imgY));
                }
            }
        }
    }
    
    public static void drawTriangle(BufferedImage canvas, int[] A, int[] B, int[] C, int color){
        drawQuickLine(canvas, A, B, color);
        drawQuickLine(canvas, A, C, color);
        drawQuickLine(canvas, B, C, color);
    }

    /**
     *
     * @param canvas
     * @param A
     * @param B
     * @param C
     * @param color
     * @param iterator: must be greater than or equal to 1
     */
    public static void drawTriangle(BufferedImage canvas, int[] A, int[] B, int[] C, int color, final int iterator){
        //iterator must be greater than or equal to one
        drawQuickLine(canvas, A, B, color, iterator);
        drawQuickLine(canvas, A, C, color, iterator);
        drawQuickLine(canvas, B, C, color, iterator);
    }
    public static void fillTriangle(BufferedImage canvas, int[] A, int[] B, int[] C, int color) {
        int last = 0;
        final int cWidth = canvas.getWidth();
        final int cHeight = canvas.getHeight();
        final float[] AB = {B[0] - A[0], B[1] - A[1]};
        final float[] AC = {C[0] - A[0], C[1] - A[1]};
        final float dab = (float) Math.hypot(AB[0], AB[1]);
        final float dac = (float) Math.hypot(AC[0], AC[1]);
        //final int iterator = (int)Math.max((dab/10), 1);
        //final int jterator = (int)Math.max((dac/10), 1);
        for (int i = 0; i < (int) dab; i++) {
            float m = i / dab;
            int[] coordi = {(int) (Math.round(m * AB[0]) + A[0]), (int) Math.round(m * AB[1]) + A[1]};
            for (int j = 0; j < (int) (dac * (1 - m)); j++) {
                float o = j / dac;
                int[] coord = {(int) Math.round(o * AC[0]) + coordi[0], (int) Math.round(o * AC[1]) + coordi[1]};
                if (coord[0] >= 0 && coord[0] < cWidth && coord[1] >= 0 && coord[1] < cHeight) {
                    canvas.setRGB(coord[0], coord[1], color);
                }

                /*for(int k = coord[0]; k < coord[0] + iterator; k++){
                    for(int h = coord[1]; h < coord[1] + jterator; h++){
                        if(k >= 0 && k < cWidth && h >= 0 && h < cHeight){
                            canvas.setRGB(k, h, color);
                        }
                    }
                }*/

            }
        }
    }
    public static void fillTriangle(BufferedImage canvas, int[] A, int[] B, int[] C, int color, float transparency){
        if(transparency >= 0.996){return;}
        float transparency_1 = 1 - transparency;
        int last = 0;
        final int cWidth = canvas.getWidth();
        final int cHeight = canvas.getHeight();
        final float[] AB = {B[0] - A[0], B[1] - A[1]};
        final float[] AC = {C[0] - A[0], C[1] - A[1]};
        final float dab = (float)Math.hypot(AB[0], AB[1]);
        final float dac = (float)Math.hypot(AC[0], AC[1]);
        //final int iterator = (int)Math.max((dab/10), 1);
        //final int jterator = (int)Math.max((dac/10), 1);
        for(int i = 0; i < (int)dab; i++){
            float m = i/dab;
            int[] coordi = {(int)(Math.round(m*AB[0])+A[0]), (int)Math.round(m*AB[1])+A[1]};
            for(int j = 0; j < (int)(dac*(1 - m)); j++){
                float o = j/dac;
                int[] coord = {(int)Math.round(o*AC[0])+coordi[0], (int)Math.round(o*AC[1])+coordi[1]};
                if(coord[0] >= 0 && coord[0] < cWidth && coord[1] >= 0 && coord[1] < cHeight){
                    //transparency by weighted average
                    //color = canvas.getRGB(coord[0], coord[1]) ^ color;
                    int c = canvas.getRGB(coord[0], coord[1]);
                    int r = (c >> 16) & 0xff;
                    int g = (c >> 8) & 0xff;
                    int b = (c) & 0xff;
                    int rcolor = (color >> 16) & 0xff;
                    int gcolor = (color >> 8) & 0xff;
                    int bcolor  = (color) & 0xff;
                   /* rcolor = (rcolor + r)/2;
                    gcolor = (gcolor + g)/2;
                    bcolor = (bcolor + b)/2;
                    rcolor = (r - transparency) + (rcolor - (255 - transparency));
                    gcolor = (g - transparency) + (gcolor - (255 - transparency));
                    bcolor = (b - transparency) + (bcolor - (255 - transparency));*/
                    rcolor = (int)(r*transparency + rcolor*transparency_1);
                    gcolor = (int)(g*transparency + gcolor*transparency_1);
                    bcolor = (int)(b*transparency + bcolor*transparency_1);
                    canvas.setRGB(coord[0], coord[1], (rcolor) << 16 | gcolor << 8 | bcolor);
                }

                /*for(int k = coord[0]; k < coord[0] + iterator; k++){
                    for(int h = coord[1]; h < coord[1] + jterator; h++){
                        if(k >= 0 && k < cWidth && h >= 0 && h < cHeight){
                            canvas.setRGB(k, h, color);
                        }
                    }
                }*/


            }
        }
    }
}
