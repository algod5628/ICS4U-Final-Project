/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import Handlers.Content;
/**
 *
 * @author Family
 */
public class Explosion {
    
    private int x;
    private int y;
    private int xmap;
    private int ymap;
    
    private int width;
    private int height;
    
    private Animation animation;
    private BufferedImage[] sprites;
    
    private boolean remove;
    
    public Explosion(int x, int y){
        this.x = x;
        this.y = y;
        
        width = 30;
        height = 30;
            
            sprites = new BufferedImage[6];
            for (int i = 0; i < sprites.length; i++) {
                sprites[i] = Content.Explosion[0][i];
            }
            
        
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(70);
    }
    
    public void update(){
        animation.update();
        if(animation.hasPlayedOnce()){
            remove = true;
        }
    }
    
    public boolean shouldRemove(){
        return remove;
    }
    
    public void setMapPosition(int x, int y){
        xmap = x;
        ymap = y;
    }
    
    public void draw(Graphics2D g){
        g.drawImage(animation.getImage(),
                x + xmap - width / 2,
                y + ymap - height / 2,
                null);
    }
    
}
