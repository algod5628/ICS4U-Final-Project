/*Austin Van Braeckel
12/14/2018
This class represents level one, having the handling for player controls,
creating entities, loading the background, map, and tileset.  Loads all elements
necessary for level one specifically.
 */
package GameState;

import java.awt.*;
import TileMap.*;
import Entity.*;
import java.util.ArrayList;

import Handlers.Keys;
import Main.GamePanel;
import Entity.Enemies.*;
import Audio.AudioPlayer;

/**
 *
 * @author Family
 */
public class Level1State extends GameState {

    private TileMap tileMap;
    private TileMap collisions;
    private Background bg;

    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion> explosions;
    
    private boolean flashstepEnabled = true;
    private long flashstepTimer;
    private boolean checkForAttacks = true;
    private long attackTimer;

    private HUD hud; 
    

    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    public void init() {
        //initialize tile map
        tileMap = new TileMap(32, true);
        tileMap.loadMap("/Maps/UndergroundTest_Display.csv");
        tileMap.loadTiles("/Tilesets/underground.png");
        tileMap.setPosition(0, 0);
        tileMap.setTween(1);
        collisions = new TileMap(32, false);
        collisions.loadMap("/Maps/UndergroundTest_Collision.csv");
        collisions.loadTiles("/Tilesets/32x32Collision.png");
        collisions.setPosition(0, 0);
        collisions.setTween(1);

        bg = new Background("/Backgrounds/grassbg1.gif", 0.5);
        
        AudioPlayer.load("/Music/Surreal-Chase_Looping.mp3", "bgMusic");
        AudioPlayer.decreaseVolume("bgMusic",-40.0f);
        AudioPlayer.loopFor("bgMusic", 10); //loops 10 times
        AudioPlayer.play("bgMusic");

        player = new Player(collisions);
        player.setPosition(100, 150);

       hud = new HUD(player);
 
        populateEnemies(); //adds enemies to the level

       explosions = new ArrayList<Explosion>(); 
    }

    private void populateEnemies() {
        enemies = new ArrayList<Enemy>();
        Slime s;
        Point[] points = new Point[]{
            new Point(200,100),
            new Point(250,100),
            new Point(275,100),
            new Point(100,150),
            new Point(50,50),
            new Point(800,50)
        };
//loops through the points and adds enemy at specified position
        for (int i = 0; i < points.length; i++) { 
            s = new Slime(collisions);
            s.setPosition(points[i].x, points[i].y);
            enemies.add(s);
        }

    }

    public void update() {

        handleInput();
        
        //update player
        player.update();
        tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
        collisions.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());


        //Makes sure the player's attack only hits the enemy once
        if (checkForAttacks){
        //attack enemies
       player.checkAttack(enemies);
       checkForAttacks = false;
       attackTimer = System.nanoTime();
        } else {
            long elapsed = (System.nanoTime() - attackTimer) / 1000000;
            if(elapsed > 100){
                checkForAttacks = true;
            }
        }
        
        //set background
        bg.setPosition(tileMap.getx(), tileMap.gety());

        //update all enemies
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.update();
            if (e.isDead()) { //Checks if the enemy is dead
                //removes from array list and reduces index by one to ensure no enemies are skipped in the update process
                enemies.remove(i);
                i--; //compensate removal
               explosions.add(new Explosion(e.getx(), e.gety())); //create explosion at the enemy's position
            }
        }

        //update explosions
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).update();
            if (explosions.get(i).shouldRemove()) {
                explosions.remove(i); //removes the explosion from the array list
                i--; //reduces index by one to compensate removal
            }
        }
    }

    public void draw(Graphics2D g) {
        //draw background
        bg.draw(g);
        //draw tilemap
        tileMap.draw(g);
        //draw player
        player.draw(g);
        //draw enemies
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }
        //draw explosions
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).setMapPosition((int)tileMap.getx(), (int)tileMap.gety());
            explosions.get(i).draw(g);
        }

        //draw HUD
        hud.draw(g);
    }
    
    
    
    public void handleInput() {
        player.setLeft(Keys.keyState[Keys.LEFT]);
        player.setRight(Keys.keyState[Keys.RIGHT]);
        //player.setUp(Keys.keyState[Keys.UP]);
        player.setDown(Keys.keyState[Keys.DOWN]);
        player.setJumping(Keys.keyState[Keys.SPACE] || Keys.keyState[Keys.UP]);
        player.setGliding(Keys.keyState[Keys.CONTROL]);
       if (Keys.isPressed(Keys.SHIFT)){
           if (flashstepEnabled){
               player.setFlashstepping();
               flashstepTimer = System.nanoTime();
               flashstepEnabled = false; //disables it
           } else {
               long elapsed = (System.nanoTime() - flashstepTimer) / 1000000;
               if (elapsed > 1000){ //1 second cool-down
                   flashstepEnabled = true; //enables it again
               }
           }
       }
       if (Keys.isPressed(Keys.ENTER)){
           if (player.isDead()){
               //respawn
               player.respawn();
           }
       }
        /*if(Keys.isPressed(Keys.A)){
            player.setMeleeAttacking();
        }*/
        
        //ADD MEDIUM MELEE player.setLeft(Keys.isPressed(Keys.S));
        //ADD HEAVY MELEE player.setLeft(Keys.isPressed(Keys.D));
        //ADD QUICK SHOT player.setLeft(Keys.isPressed(Keys.Q));
        if(!player.getIsAttacking()){ //determines the attack that the user will use
            if(Keys.isPressed(Keys.W)) player.setRangedAttacking();
            if(Keys.isPressed(Keys.A)) player.setMeleeAttacking();
        } 
            
        //ADD PAUSE/SOMETHING player.Pause(Keys.isPressed(Keys.ESCAPE));
    }
/*
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_LEFT) {
            player.setLeft(true);
        }
        if (k == KeyEvent.VK_RIGHT) {
            player.setRight(true);
        }
        if (k == KeyEvent.VK_UP) {
            player.setUp(true);
        }
        if (k == KeyEvent.VK_DOWN) {
            player.setDown(true);
        }
        if (k == KeyEvent.VK_SPACE) {
            player.setJumping(true);
        }
        if (k == KeyEvent.VK_CONTROL) {
            player.setGliding(true);
        }
        if (k == KeyEvent.VK_X) {
            player.setMeleeAttacking();
        }
        if (k == KeyEvent.VK_C) {
            player.setRangedAttacking();
        }
    } REMOVE*/
/* REMOVE
    public void keyReleased(int k) {
        if (k == KeyEvent.VK_LEFT) {
            player.setLeft(false);
        }
        if (k == KeyEvent.VK_RIGHT) {
            player.setRight(false);
        }
        if (k == KeyEvent.VK_UP) {
            player.setUp(false);
        }
        if (k == KeyEvent.VK_DOWN) {
            player.setDown(false);
        }
        if (k == KeyEvent.VK_SPACE) {
            player.setJumping(false);
        }
        if (k == KeyEvent.VK_CONTROL) {
            player.setGliding(false);
        }
    }*/

}
