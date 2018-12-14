/*
 */
package Entity;

import TileMap.*;
import Audio.AudioPlayer;

import java.util.HashMap;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


/**
 *
 * @author Family
 */
public class Player extends MapObject {
    
    //player attributes
    private int health;
    private int maxHealth;
    private int ammo;
    private int maxAmmo;
    private boolean dead;
    private boolean flinching;
    private long flinchTime;

    //ranged attack
    private boolean rangedAttacking;
    private int ammoCost;
    private int ammoDamage;
   // private ArrayList<FireBall> fireBalls;

    //melee/close-ranged attack
    private boolean meleeAttacking;
    private int meleeDamage;
    private int meleeRange;

    //gliding
    private boolean gliding;

    //animations
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {
        4, //idle
        6, //running 
        8, //jumping
        2, //falling
        5, //gliding
        9, //ranged
        6 //melee
    };

    // animation actions
    private static final int IDLE = 0;
    private static final int RUNNING = 1;
    private static final int JUMPING = 2;
    private static final int FALLING = 3;
    private static final int GLIDING = 4;
    private static final int RANGED = 5;
    private static final int MELEE = 6;

    
    //Constructor
    public Player(TileMap tm) {
        super(tm);

        width = 50;
        height = 37;
        cwidth = 25;
        cheight = 30;

        moveSpeed = 0.4;
        maxSpeed = 2.3;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;

        facingRight = true;

        maxHealth = 5;
        health = maxHealth;
        maxAmmo = 2500;
        ammo = maxAmmo;

        ammoCost = 200;
        ammoDamage = 5;
      //  fireBalls = new ArrayList<FireBall>();

        meleeDamage = 8;
        meleeRange = 40;

        //load sprites
        loadSprites();
        /*try {
            BufferedImage mainSpritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/adventurer-Sheet.png"));
            BufferedImage bowSpritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/adventurer-bow-Sheet.png"));
            BufferedImage attack = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/adventurerAttack.png"));
            sprites = new ArrayList<BufferedImage[]>();

            // 7 animation actions
            for (int i = 0; i < 7; i++) {
                BufferedImage[] bi = new BufferedImage[numFrames[i]];
                for (int j = 0; j < numFrames[i]; j++) {

                    if (i != 6) { //scratching animation has a larger width (60 pixel width compared to 30 pixels)
                        bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);
                    } else {
                        bi[j] = spritesheet.getSubimage(j * width * 2, i * height, width * 2, height);
                    }
                }

                sprites.add(bi);

            } // end loop for loading in a sprite animation set
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(400);
        
        //set-up sound effects
        AudioPlayer.load("/Sound FX/NFF-jump.wav", "jump");
        AudioPlayer.decreaseVolume("jump", -50.0f);
        /*
        sfx = new HashMap<String, AudioPlayer>();
        sfx.put("jump", new AudioPlayer("/Sound FX/NFF-jump.wav"));
        sfx.get("jump").decreaseVolume(-50.0f); */
        
    }

    public void loadSprites() {
        //load sprites
        try {
            BufferedImage mainSpritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/adventurer-Sheet.png"));
            BufferedImage bowSpritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/adventurer-bow-Sheet.png"));
            //BufferedImage idle = mainSpritesheet.getSubimage(0, 0, 4 * width, height);
            BufferedImage attack = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/adventurerAttack.png"));

            
            sprites = new ArrayList<BufferedImage[]>();

            //create 2D array to hold all images
            BufferedImage[][] playerSprites = new BufferedImage[11][7]; //11 x 7

            //MAIN
            //Fill 2D array with images
            //11 rows
            for (int i = 0; i < 11; i++) {
                //7 columns
                for (int j = 0; j < 7; j++) {
                    if (i < 11 && j < 2) {
                        playerSprites[i][j] = mainSpritesheet.getSubimage(j * width, i * height, width, height);
                    } else {
                        break;
                    }
                }
            }

            //create 2D array to hold all images
            BufferedImage[] groundBowSprites = new BufferedImage[numFrames[RANGED]];
            BufferedImage[] airBowSprites =  new BufferedImage[6];

            
            //BOW
            spriteArrayOneLine(bowSpritesheet.getSubimage(0, 0, 9 * width, height), groundBowSprites, numFrames[RANGED]);
               /*for (int x = 0; x < 9; x++) {
                    groundBowSprites[x] = bowSpritesheet.getSubimage(x * width, 0, width, height);
                }*/
            

            /*
            //4 rows
            for (int i = 0; i < 4; i++) {
                //4 columns
                for (int j = 0; j < 4; j++) {
                    if (i == 3 && j < 2){
                        bowSprites[i][j] = bowSpritesheet.getSubimage(j * width, i * height, width, height);
                    } else {
                        break;
                    }
                }
            }*/
             
            //Separate the different actions into 1D arrays and add to the spritesheet ArrayList
            //-----------------------MAIN --------------------------------------------
           
            BufferedImage[] array;
             BufferedImage image;
             
            //IDLE
            array = new BufferedImage[numFrames[IDLE]];
            image = mainSpritesheet.getSubimage(0, 0, 4 * width, height);
            spriteArrayOneLine(image, array, numFrames[IDLE]);
            sprites.add(array);
            /*BufferedImage[] array = new BufferedImage[numFrames[IDLE]];
            makeSpriteArray(playerSprites, array, 0, 0);
            sprites.add(array); */
            //RUN 
            array = new BufferedImage[numFrames[RUNNING]];
            image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/adventurerRun.png"));
            spriteArrayOneLine(image, array, numFrames[RUNNING]);
            sprites.add(array);
            //JUMP
            array = new BufferedImage[numFrames[JUMPING]];
            image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/adventurerJump.png"));
            spriteArrayOneLine(image, array, numFrames[JUMPING]);
            sprites.add(array);
            //FALL
            array = new BufferedImage[numFrames[FALLING]];
            image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/adventurerSlideAndFall.png"));
            image = image.getSubimage(0, height, numFrames[FALLING] * width, height);
            spriteArrayOneLine(image, array, numFrames[FALLING]);
            sprites.add(array);
            //GLIDE
            array = new BufferedImage[numFrames[GLIDING]];
            image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/adventurerSlideAndFall.png"));
            image = image.getSubimage(0, 0, numFrames[GLIDING] * width, height);
            spriteArrayOneLine(image, array, numFrames[GLIDING]);
            sprites.add(array);
            //RANGED
         /*   array = new BufferedImage[numFrames[RANGED]];
            makeSpriteArray(bowSprites, array, 0, 0, 4); 
            sprites.add(array); */
         sprites.add(groundBowSprites);
            //MELEE
            array = new BufferedImage[numFrames[MELEE]];
            spriteArrayOneLine(attack, array, numFrames[MELEE]);
            sprites.add(array);
            
            /*
            array = new BufferedImage[numFrames[MELEE]];
            makeSpriteArray(playerSprites, array, 7, 4);
            sprites.add(array);
            
            /*
            // 7 animation actions
            for (int i = 0; i < 7; i++) {
                BufferedImage[] bi = new BufferedImage[numFrames[i]];
                for (int j = 0; j < numFrames[i]; j++) {

                    if (i != 6) { //scratching animation has a larger width (60 pixel width compared to 30 pixels)
                        bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);
                    } else {
                        bi[j] = spritesheet.getSubimage(j * width * 2, i * height, width * 2, height);
                    }
                }

                sprites.add(bi);

            } // end loop for loading in a sprite animation set
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
     public void spriteArrayOneLine(BufferedImage sourceImage, BufferedImage[] array, int numFrames){
        for (int x = 0; x < numFrames; x++) {
                    array[x] = sourceImage.getSubimage(x * width, 0, width, height);
                }
    }
    
    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getAmmo() {
        return ammo;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public void setRangedAttacking() {
        rangedAttacking = true;
    }

    public void setMeleeAttacking() {
        meleeAttacking = true;
    }

    public void setGliding(boolean b) {
        gliding = b;
    }

    /*
    public void checkAttack(ArrayList<Enemy> enemies) {

        //loop through enemies
        for (int i = 0; i < enemies.size(); i++) { //linear search through enemies to check if they are attacked

            Enemy e = enemies.get(i);

            //check melee attack
            if (meleeAttacking) {
                if (facingRight) {
                    if (e.getx() > x
                            && //Makes sure enemy is in front of the player when facing right
                            e.getx() < x + meleeRange
                            && e.gety() > y - height / 2
                            && e.gety() < y + height / 2) {
                        e.hit(meleeDamage);
                    }
                } else {
                    if (e.getx() < x
                            && //Makes sure enemy is in front of the player when facing left
                            e.getx() > x - meleeRange
                            && e.gety() > y - height / 2
                            && e.gety() < y + height / 2) {
                        e.hit(meleeDamage);
                    }
                }
            }//end meleeAttacking if

            //Projectiles
            for (int j = 0; j < fireBalls.size(); j++) { //loop through fireballs
                if (fireBalls.get(j).intersects(e)) { //if it hits the enemy
                    e.hit(ammoDamage); //causes damage to the enemy
                    fireBalls.get(j).setHit(); //specifies that fireball has hit something
                    break; //end whole loop if enemy has been hit
                }
            }
            
            //check enemy collision
            if(intersects(e)){
                hit(e.getDamage());
            }
            
        }//End enemy search loop

        
    }*/

    public void hit(int damage){
        if(flinching){
            return;
        }
        
        health -= damage;
        if(health < 0){
            health = 0;
        }
        if(health == 0){
            dead = true;
        }
        flinching = true;
        flinchTime = System.nanoTime();
    }
    
    private void getNextPosition() {

        //movement
        if (left) {
            dx -= moveSpeed; //speeds up
            if (dx < -maxSpeed) { //limits the max speed of the player
                dx = -maxSpeed;
            }
        } else if (right) {
            dx += moveSpeed; //speeds up
            if (dx > maxSpeed) { //limits the max speed of the player
                dx = maxSpeed;
            }
        } else { //stopped/not moving
            if (dx > 0) {
                dx -= stopSpeed; //slows down
                if (dx < 0) { //complete stop
                    dx = 0;
                }
            } else if (dx < 0) {
                dx += stopSpeed; //slows down
                if (dx > 0) { //complete stop
                    dx = 0;
                }
            }
        }

        //cannot attack while moving (but still in the air)
        if ((currentAction == MELEE || currentAction == RANGED) && !(jumping || falling)) {
            dx = 0;
        }

        //jumping
        if (jumping && !falling) {
            AudioPlayer.play("jump");
            dy = jumpStart;
            falling = true;
        }

        //falling
        if (falling) {
            if (dy > 0 && gliding) {
                dy += fallSpeed * 0.1; //gliding causes to player to fall 90% slower
            } else { //not gliding
                dy += fallSpeed;
            }

            if (dy > 0) {
                jumping = false;
            }

            if (dy < 0 && !jumping) {
                dy += stopJumpSpeed; //Allows a shorter jump when the jump key is released
            }

            if (dy > maxFallSpeed) {
                dy = maxFallSpeed; //limits fall speed
            }

        }

    }

    public void update() {

        //update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        //check attack has stopped
        if (currentAction == MELEE) {
            if (animation.hasPlayedOnce()) {
                meleeAttacking = false;
            }
        } else if (currentAction == RANGED) {
            if (animation.hasPlayedOnce()) {
                rangedAttacking = false;
            }
        }

        //fireball attack
        ammo += 1; //constantly regenerates the ammo
        if (ammo > maxAmmo) { //limits it to the specified max ammo
            ammo = maxAmmo;
        }
        if (rangedAttacking && currentAction != RANGED) {
            //Creates a fireball entity, and specifies direction based on where player is facing
            //Only if sufficient ammo is present
            if (ammo > ammoCost) {
                ammo -= ammoCost; //subtracts the ammo used to  attack
                //FireBall fb = new FireBall(tileMap, facingRight);
                //fb.setPosition(x, y); //sets it to the same position as player
               // fireBalls.add(fb);
            }
        }

        //update fireballs
        /*
        for (int i = 0; i < fireBalls.size(); i++) {
            fireBalls.get(i).update();
            if (fireBalls.get(i).shouldRemove()) { //removes from game if it hits
                //remove from array list and reduce the index to make sure all items in the array list are updated
                fireBalls.remove(i);
                i--;
            }
        }*/
        
        //check if done flinching
        if (flinching){
            long elapsed = (System.nanoTime() - flinchTime) / 1000000;
            if (elapsed > 1000){ //stops flinching after one second
                flinching = false;
            }
        }

        // set animation
        if (meleeAttacking) {

            if (currentAction != MELEE) {
                currentAction = MELEE;
                animation.setFrames(sprites.get(MELEE));
                animation.setDelay(50);
                //REMOVE width = 60;
            }

        } else if (rangedAttacking) {

            if (currentAction != RANGED) {
                currentAction = RANGED;
                animation.setFrames(sprites.get(RANGED));
                animation.setDelay(100);
                //width = 30;
            }

        } else if (dy > 0) {

            if (gliding) {
                if (currentAction != GLIDING) {
                    currentAction = GLIDING;
                    animation.setFrames(sprites.get(GLIDING));
                    animation.setDelay(100);
                   // width = 30;
                }
            } else if (currentAction != FALLING) {
                currentAction = FALLING;
                animation.setFrames(sprites.get(FALLING));
                animation.setDelay(50);
                //width = 30;
            }

        } else if (dy < 0) {

            if (currentAction != JUMPING) {
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(50);
                //width = 30;
            }

        } else if (left || right) {

            if (currentAction != RUNNING) {
                currentAction = RUNNING;
                animation.setFrames(sprites.get(RUNNING));
                animation.setDelay(120);
                //width = 30;
            }

        } else {

            if (currentAction != IDLE) {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(500);
                //width = 30;
            }

        } //End if

        //update the animation
        animation.update();

        //set direction
        if (currentAction != MELEE && currentAction != RANGED) { //makes sure the player isn't moving while attacking
            if (right) {
                facingRight = true;
            }
            if (left) {
                facingRight = false;
            }
        }

        //Print position to console (for level design purposes)
        //System.out.println("X: " + x + ", Y: " + y + ", Xmap: " + xmap + ", Ymap: " + ymap);
    }

    public void draw(Graphics2D g) {

        setMapPosition();

        //draw fireballs
        /*
        for (int i = 0; i < fireBalls.size(); i++) {
            fireBalls.get(i).draw(g);
        }*/

        //draw player
        if (flinching) {
            long elapsed = (System.nanoTime() - flinchTime) / 1000000;
            if (elapsed / 100 % 2 == 0) {
                return; //doesn't draw the player (flashing effect when hit) - every 100 milliseconds
            }
        }

        if (facingRight) {
            g.drawImage(animation.getImage(), (int) (x + xmap - width / 2), (int) (y + ymap - height / 2), null);
        } else { //facing left - flips the image because it is only facing right (-width)
            g.drawImage(animation.getImage(), (int) (x + xmap - width / 2 + width), (int) (y + ymap - height / 2), -width, height, null);
        }
       //REMOVE super.draw(g);
    }

}
