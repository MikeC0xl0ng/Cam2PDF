package com.cam2pdf;

import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;

public class HoughTransform {
    final int WHITE = Color.argb(255, 255, 255,255);
    final int BLACK = Color.argb(255, 0, 0,0);
    final int RED = Color.argb(255, 255, 0,0);

    int[][] space;
    int[][] processed_img;
    int max_val;
    public Bitmap img;
    public int[] k;
    public double filter;
    public int theta_max;



    public HoughTransform(Bitmap img_, double k1, double filter, int theta_max){
        try{
            img = Bitmap.createBitmap(img_.getWidth(),img_.getHeight(),Bitmap.Config.ARGB_8888);
            for (int y=0; y<img.getHeight(); y++){
                for (int x=0; x<img.getWidth(); x++){
                    img.setPixel(x, y, img_.getPixel(x, y));
                }
            }
        }catch (Exception ex){
            return;
        }
        int diagonal = (int)Math.sqrt(Math.pow(img.getWidth(), 2) + Math.pow(img.getHeight(), 2)) + 1;
        space = new int[diagonal][theta_max];
        processed_img = new int[img.getHeight()][img.getWidth()];
        this.theta_max = theta_max;
        this.filter = filter;
        this.k = new int[]{(int)(k1 * space[0].length), (int)(k1 * space.length)};
        Bitmap edge_img = new EdgeDetector(img).detectEdges();
        for (int i=0; i<edge_img.getHeight(); i++){
            for (int j=0; j<edge_img.getWidth(); j++){
                processed_img[i][j] = edge_img.getPixel(j, i);
            }
        }
        detectLines();
        findMaxes(space);
        drawDetectedLines(processed_img);
    }

    public HoughTransform(Bitmap img, int theta_max){
        this.img=img;
        int diagonal = (int)Math.sqrt(Math.pow(img.getWidth(), 2) + Math.pow(img.getHeight(), 2)) + 1;
        space = new int[diagonal][theta_max];
        processed_img = new int[img.getHeight()][img.getWidth()];
        this.theta_max = theta_max;
        this.filter = 0.6;
        Bitmap edge_img = new EdgeDetector(img).detectEdges();
        for (int i=0; i<edge_img.getHeight(); i++){
            for (int j=0; j<edge_img.getWidth(); j++){
                processed_img[i][j] = edge_img.getPixel(j, i);
            }
        }
        detectLines();
        findMaxes(space);
        drawDetectedLines(processed_img);
    }

    private void detectLines(){
        for (int y=0; y<processed_img.length; y++){
            for (int x=0; x<processed_img[0].length; x++){
                if (processed_img[y][x] != BLACK){
                    vote(x, y);
                }
            }
        }
        max_val = maxVal(space);
    }

    private void vote(int x, int y){
        double r;
        double theta;
        for (theta=0; theta<space[0].length; theta++){
            try{
                r = x * cos(theta) + y * sin(theta);
                space[(int)r][(int)theta]++;
            }catch(Exception ex){

            }
        }
    }

    private double sin(double n){
        return Math.sin(n);
    }

    private double cos(double n){
        return Math.cos(n);
    }

    private void findMaxes(int[][] m){
        if(k == null){
            k = calculateRegion(space);
        }
        for (int r=0; r<m.length; r++){
            for (int theta=0; theta<theta_max; theta++){
                if (isMax(m, theta, r)){
                    drawLine(processed_img, new int[]{theta, r});
                }
            }
        }
    }

    private int[] calculateRegion(int[][] m){
        int region_x;
        int region_y;
        int[] m_x = new int[m.length];
        int[] m_y = new int[m[0].length];
        for (int i=0; i<m.length; i++){
            for (int j=0; j<m[0].length; j++){
                if (m[i][j] > max_val*filter){
                    m_x[i]++;
                }
            }
        }
        region_x = sum(m_x);
        for (int i=0; i<m[0].length; i++){
            for (int j=0; j<m.length; j++){
                if (m[j][i] > max_val*filter){
                    m_y[i]++;
                }
            }
        }
        region_y = sum(m_y);
        return new int[]{region_x, region_y};
    }

    private int sum(int[] v){
        int sum = 0;
        for (int i=0; i<v.length; i++){
            sum += v[i];
        }
        sum *= 4;
        return (int)sum;
    }

    private boolean isMax(int[][] m, int theta, int r){
        if (m[r][theta] <= max_val*filter){
            return false;
        }
        for (int i=r-k[1]; i<r+k[1]; i++){
            for (int j=theta-k[0]; j<theta+k[0]; j++){
                try{
                    if (!(i == r && j == theta)){
                        if (m[i][j] >= m[r][theta]){
                            return false;
                        }
                    }
                }catch (Exception ex){

                }
            }
        }
        return true;
    }

    private int maxVal(int[][] m){
        int result = 0;
        int[] param = new int[2];
        for (int i=0; i<m.length; i++){
            for (int j=0; j<m[0].length; j++){
                if (m[i][j] > result){
                    result = m[i][j];
                    param[0] = j;
                    param[1] = i;
                }
            }
        }
        return result;
    }

    private void drawLine(int[][] matrix, int[] param){
        double theta = param[0];
        double r = param[1];
        if (theta % 180 == 0){
            theta += 0.001;
        }
        for (int x=0; x<matrix[0].length; x++){
            try{
                int y = (int)((-x*cos(theta) + r)/sin(theta));
                matrix[y][x] = RED;
            }catch (Exception ex){

            }
        }
        for (int y=0; y<matrix.length; y++){
            try{
                int x = (int)((-y*sin(theta) + r)/cos(theta));
                matrix[y][x] = RED;
            }catch (Exception ex){

            }
        }
    }

    private void drawDetectedLines(int[][] m){
        for (int y=0; y<m.length; y++){
            for (int x=0; x<m[0].length; x++){
                if (m[y][x] == RED){
                    try{
                        img.setPixel(x, y, RED);
                    }catch (Exception ex){

                    }
                }
            }
        }
    }

    /*public void drawImage(){
        JFrame f = new JFrame("Hough Transform (theta max: " + space[0].length + ")");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.getContentPane().setLayout(new FlowLayout());
        f.add(new JLabel(new ImageIcon(img)));
        f.pack();
        f.setVisible(true);
    }*/
}
