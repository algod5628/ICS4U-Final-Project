/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Enemies;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.*;

import TileMap.TileMap;
import java.util.ArrayList;
import Entity.*;
import Entity.Animation;
import Handlers.Content;
import java.util.Set;
/**
 *
 * @author Family
 */
public class Slime extends Enemy{
    
    BufferedImage[] sprites;
    
    private static long stopTime;
    private static boolean stopped;
    
    public Slime(TileMap tm){
        super(tm);
        
        moveSpeed = 0.1;
        maxSpeed = 1;
        fallSpeed = 0.2; //shouldn't fall - just move side to side
        maxFallSpeed = 10.0;
        
        width = 32;
        height = 25;
        cwidth = 25;
        cheight = 20;
        
        maxHealth = 20;
        health = maxHealth;
        damage = 5;
        
        stopped = false;
        
        //load sprites
        
        /* REMOVE
        try {
            
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/Slugger.gif"));
            
           sprites = new BufferedImage[3];
            for (int i = 0; i < sprites.length; i++) {
                sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        
        //Sets-up the sprite array for movement
       sprites = Content.Slime[0];
        
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setFrame(0);
        animation.setDelay(100);
        
        right = true; //always starts moving to the right when created
        facingRight = false; //image is facing left; not right
        
    }
    
    private void getNextPosition(){
        
         //falling
         if (falling){ //only falls, and can't jump
                dy += fallSpeed;
            }
         
        //periodically stops (like taking steps)
        if (stopped){
            if (left){
                dx = -0.1;
            }else{ //right
                dx = 0.1;
            }
            
             long elapsed = (System.nanoTime() - stopTime) / 1000000;
            if (elapsed > 400){
                stopped = false;
                stopTime = System.nanoTime();
            }
        } else {
        
        //only moves side-to-side between walls
         if (left) {
            dx -= moveSpeed; //speeds up
            if (dx < -maxSpeed) { //limits the max speed of the slime
                dx = -maxSpeed;
            }
        } else if (right) {
            dx += moveSpeed; //speeds up
            if (dx > maxSpeed) { //limits the max speed of the slime
                dx = maxSpeed;
            }
        }   
       }
    }
    
    public void update(){
        //update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        
        
        //check flinching
        if(flinching){
            long elapsed = (System.nanoTime() - flinchTimer / 1000000);
            if(elapsed > 1000){
                flinching = false;
            }
        }
        
        if(!stopped){
         long elapsed = (System.nanoTime() - stopTime) / 1000000;
            if (elapsed > 400){
                stopped = true;
                stopTime = System.nanoTime();
            }
        }
        
        //if it hits a wall, go the other direction
        if (right && dx == 0){ //automatically sets movement to 0 when an entity hits a wall
            right = false;
            left = true;
            facingRight = true;
        } else if (left && dx == 0){
            right = true;
            left = false;
            facingRight = false;
        }
     
        //update animation
        animation.update();
        
    }
    
    public void draw(Graphics2D g){
        
        setMapPosition();
        
        if (!notOnScreen()){ //if it isn't on the screen, don't draw it
            //draws it the proper way it should be facing
           if (flinching) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed / 10 % 2 == 0) {
                return; //doesn't draw the slime (flashing effect when hit) - every 100 milliseconds
            }
            super.draw(g);
        } else {
               super.draw(g);
           }
           
        }
        
        
        
    }
    
}
