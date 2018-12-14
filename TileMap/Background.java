/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import Main.GamePanel;

/**
 *
 * @author Family
 */
public class Background {
    
    private BufferedImage image;
    
    private double x;
    private double y;
    private double dx;
    private double dy;
    
    private double moveScale; //speed
    
    //Constructor
    public Background(String s, double ms){
        try {
            image = ImageIO.read(getClass().getResourceAsStream(s)); //reads in image
            moveScale = ms;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setPosition(double x, double y){
        //Ensures smooth-scrolling - don't want it to scroll off the screen
        this.x = (x * moveScale) % GamePanel.WIDTH;
        this.y = (y * moveScale) % GamePanel.WIDTH;
    }
    
    public void setVector(double dx, double dy){
        this.dx = dx;
        this.dy = dy;
    }
    
    public void update(){
        x += dx;
        y += dy;
    }
    
    public void draw(Graphics2D g){
        g.drawImage(image, (int)x, (int)y, null);
        //Makes sure the background is always filling the screen
        //Draws another image when the limit is reached
        if(x < 0) { //background shifting to left
            g.drawImage(image, (int)x + GamePanel.WIDTH, (int)y, null);
        } else if (x > 0){ //used if the background is shifting to the right
            g.drawImage(image, (int)x - GamePanel.WIDTH, (int)y, null);
        }
        if(x < 0 - GamePanel.WIDTH || x > GamePanel.WIDTH) { //makes animation endless to the left
            x = 0;
        }
    }
    
}
