/*Austin Van Braeckel
12/14/2018
Represents an entity that can be placed on the map and have functionality and/or
be displayed
 */
package Entity;

import TileMap.TileMap;
import Main.GamePanel;

import java.awt.*;
import java.awt.Graphics2D;

/**
 *
 * @author Family
 */
public class Enemy extends MapObject{
    
    //protected so all subclasses can access
    protected int health;
    protected int maxHealth;
    protected boolean dead;
    protected int damage;
    
    protected boolean flinching;
    protected long flinchTimer;
    
    //Constructor
    public Enemy(TileMap tm){
        super(tm);
    }
    
    /**
     * Determines whether the enemy is dead or not
     * @return true if dead, and false if not
     */
    public boolean isDead(){
        return dead;
    }
    
    /**
     * Retrieves the damage that the enemy gives to the player
     * @return damage as an integer
     */
    public int getDamage(){
        return damage;
    }
    
    /**
     * Hits the enemy with the specified amount of damage
     * @param damage damage to the enemy as an integer
     */
    public void hit(int damage){
        if(dead || flinching){ //can't be hit if dead or flinching
            return;
        }
        health -= damage;
        
        //IMPLEMENT use <= ??
        if(health < 0){ //ensures their health is 0 or greater
            health = 0;
        }
        if(health == 0){ //dead if health reaches 0
            dead = true;
        }
        flinching = true;
        flinchTimer = System.nanoTime(); //initiates the timer for the flinch
    }
    
    /**
     * Updates the enemy characteristics, animations, attributes
     */
    public void update(){}
        
    /**
     * Draws the enemy with its current characteristics, animations, attributes
     */
    public void draw(Graphics2D g){
        super.draw(g);
        
           Font font = new Font("Century Gothic", Font.PLAIN, 10);;
           g.setFont(font);
           g.setColor(Color.WHITE);
           g.drawString("HEALTH: " + health, (int)(x + xmap - 20), (int)(y + ymap - 10));
    }
}
