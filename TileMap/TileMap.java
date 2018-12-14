/*

 */
package TileMap;

import java.awt.*;
import java.awt.Graphics2D;
import java.awt.image.*;

import java.io.*;
import javax.imageio.ImageIO;

import Main.GamePanel;

/**
 *
 * @author Family
 */
public class TileMap {
    
    //position
    private double x;
    private double y;
    
    //bounds
    private int xmin;
    private int ymin;
    private int xmax;
    private int ymax;
    
    private double tween; //allows smooth scrolling rather than choppy
    
    //map
    private int[][] map;
    private int tileSize;
    private int numRows;
    private int numCols;
    private int width;
    private int height;
    
    //tileset
    private BufferedImage tileset;
    private int numTilesAcross;
    private Tile[][] tiles;
    
    //drawing
    private int rowOffset; //specifies which row to start drawing
    private int colOffset; //specifies which row to start drawing
    private int numRowsToDraw;//number of rows
    private int numColsToDraw;//number of columns
    
    //Constructor
    public TileMap(int tileSize){
        this.tileSize = tileSize;
        numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
        numColsToDraw = GamePanel.WIDTH / tileSize + 2;
        tween = 1;
    }
    
    //Loads the tiles
    public void loadTiles(String s){
        try {
            tileset = ImageIO.read(getClass().getResourceAsStream(s));
            numTilesAcross = tileset.getWidth() / tileSize;
            tiles = new Tile[2][numTilesAcross];
            
            BufferedImage subimage;
            for (int col = 0; col < numTilesAcross; col++) {
                subimage = tileset.getSubimage(col * tileSize, 0, tileSize, tileSize);
                tiles[0][col] = new Tile(subimage, Tile.NORMAL);
                subimage = tileset.getSubimage(col * tileSize, tileSize, tileSize, tileSize);
                tiles[1][col] = new Tile(subimage, Tile.BLOCKED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadMap(String s){
        //first line is number of columns
        //number of rows
        //then map itself
        
        try {
            InputStream in = getClass().getResourceAsStream(s);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            
            numCols = Integer.parseInt(br.readLine()); //reads the number of columns
            numRows = Integer.parseInt(br.readLine()); //reads the number of rows
            map = new int[numRows][numCols];
            width = numCols * tileSize;
            height = numRows * tileSize;
            
            //Sets bounds
            xmin = GamePanel.WIDTH - width;
            xmax = 0;
            ymin = GamePanel.HEIGHT - height;
            ymax = 0;
            
            //Reads-in the map
            String delims = ","; //gets values between commas
            for (int row = 0; row < numRows; row ++){
                String line = br.readLine();
                String[] tokens = line.split(delims); //removes white space and splits the line into its seperate values
                for (int col = 0; col < numCols; col++) {
                    map[row][col] = Integer.parseInt(tokens[col]);
                    if (map[row][col] < 0){
                        map[row][col] = 0;
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int getTileSize(){
        return tileSize;
    }
    
    public double getx(){
        return x;
    }
    
    public double gety(){
        return y;
    }
    
    public int getWidth(){
        return width;
    }
    
    public int getHeight(){
        return height;
    }
    
    public int getType(int row, int col){
        int rc = map[row][col];
        int r = rc / numTilesAcross; //gets row number
        int c = rc % numTilesAcross; //gets tiles number
        return tiles[r][c].getType();
    }
    
    public int getNumRows(){
        return numRows;
    }
    
    public int getNumCols(){
        return numCols;
    }
    
    public void setTween(double d){
        tween = d;
    }
    
    public void setPosition(double x, double y){
        //uses the tween to make the scrolling smoother
        this.x += (x - this.x) * tween;
        this.y += (y - this.y) * tween;
        
        fixBounds();
        
        colOffset = (int)-this.x / tileSize;
        rowOffset = (int)-this.y / tileSize;
    }
    
    //Ensures that the bounds aren't being passed
    private void fixBounds(){
        if (x < xmin){
            x = xmin;
        }
        if (y < ymin){
            y = ymin;
        }
        if (x > xmax){
            x = xmax;
        }
        if (y > ymax){
            y = ymax;
        }
    }
    
    public void draw(Graphics2D g){
        for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {
            if(row >= numRows) break; //stops loop if it exceeds the number of rows
            for (int col = colOffset; col < colOffset + numColsToDraw; col++) {
                if (col >= numCols) break; //stops loop if it exceeds the number of columns
                
                if(map[row][col] == 0) continue; //continue if it is empty
                
                int rc = map[row][col];
                int r = rc / numTilesAcross;
                int c = rc % numTilesAcross;
                g.drawImage(tiles[r][c].getImage(), (int)x + col * tileSize, (int)y + row * tileSize, null);
            }
        }//end loop
    }
    
    
    
}
