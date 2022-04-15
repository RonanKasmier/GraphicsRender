package util;
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

    public static void drawQuickLine(BufferedImage canvas, int[] p1, int[] p2, int color){
        int dx = p2[0] - p1[0];
        int dy = p2[1] - p1[1];
        if(Math.abs(dy) > Math.abs(dx)){
            float m = (float) dx/dy;
            if (dy < 0) {
                for (int i = 0; i > dy; i -= 2) {
                    if (p1[0] + (int)(m * i) < canvas.getWidth() && p1[0] + (int)(m * i) >= 0 && p1[1] + i < canvas.getHeight() && p1[1] + i >= 0) {
                        canvas.setRGB(p1[0] + (int)(m * i), p1[1] + i, color);
                    }
                }
            } else {
                for (int i = 0; i < dy; i += 2) {
                    if (p1[0] + (int)(m * i) < canvas.getWidth() && p1[0] + (int)(m * i) > 0 && p1[1] + i < canvas.getHeight() && p1[1] + i >= 0) {
                        canvas.setRGB(p1[0] + (int) (m * i), p1[1] + i, color);
                    }
                }
            }
        }else {
            float m = (float) dy / dx;
            if (dx < 0) {
                for (int i = 0; i > dx; i -= 2) {
                    if (p1[0] + i < canvas.getWidth() && p1[0] + i >= 0 && p1[1] + (int) (m * i) < canvas.getHeight() && p1[1] + (int) (m * i) >= 0) {
                        canvas.setRGB(p1[0] + i, p1[1] + (int) (m * i), color);
                    }
                }
            } else {
                for (int i = 0; i < dx; i += 2) {
                    if (p1[0] + i < canvas.getWidth() && p1[0] + i > 0 && p1[1] + (int) (m * i) < canvas.getHeight() && p1[1] + (int) (m * i) >= 0) {
                        canvas.setRGB(p1[0] + i, p1[1] + (int) (m * i), color);
                    }
                }
            }
        }
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

        int canvW = canvas.getWidth();
        int canvH = canvas.getHeight();
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
            float[] AY = {t*AB[0], t*AB[1]};
            float[] AZ = {t*CD[0] + AC[0], t*CD[1] + AC[1]};
            float[] YZ = {AZ[0]-AY[0], AZ[1]-AY[1]};
            int height = (int)Math.hypot(YZ[0], YZ[1]);
            for(int j = 0; j < height; j++){
                float s = (float)j/height;
                int imgY = (int)(s*imgH);
                float[] AX = {s*YZ[0] + AY[0], s*YZ[1] + AY[1]};
                int[] X = {(int)AX[0] + A[0], (int)AX[1] + A[1]};
                if(!(X[0] < 0 || X[0] >= canvW || X[1] < 0 || X[1] >= canvH)) {
                    canvas.setRGB(X[0], X[1], image.getRGB(imgX, imgY));
                }
            }
        }
    }
}
