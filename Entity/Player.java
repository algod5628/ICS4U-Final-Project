/*
 */
package Entity;

import TileMap.*;
import Audio.AudioPlayer;
import Main.GamePanel;

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
    private double health;
    private double maxHealth;
    private int ammo;
    private int maxAmmo;
    private boolean dead;
    private boolean hasStrongShot;
    private long shotDelay;
    private boolean flinching;
    private long flinchTime;

    //ranged attack
    private boolean sRangedAttacking;
    private int ammoCost;
    private int rangedDamage;
    private ArrayList<Arrow> arrows;

    //melee/close-ranged attack
    private boolean meleeAttacking;
    private int meleeDamageL;
    private int meleeRangeL;
    private int meleeDamageM; //IMPLEMENT
    private int meleeRangeM;
    private int meleeDamageH;
    private int meleeRangeH;

    //other actions
    private boolean gliding;
    private boolean flashstepping;
    private boolean isDying = false;

    //animations
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {
        4, //idle
        4,//crouching
        6, //running 
        8, //jumping
        2, //falling
        2, //gliding
        9,//hanging (climbing from ledge)
        4,//attackIdle
        7,//mMelee
        6,//hMelee
        6, //lMelee
        10,//dying
        20,//flashstep
        9, //ranged
        6//rangedQ
    };

    // animation actions
    private static final int IDLE = 0;
    private static final int CROUCHING = 1; //NOT USING
    private static final int RUNNING = 2;
    private static final int JUMPING = 3;
    private static final int FALLING = 4;
    private static final int GLIDING = 5;
    private static final int HANGING = 6; //NOT USING
    private static final int ATTACKIDLE = 7; //NOT USING
    private static final int MMELEE = 8;
    private static final int HMELEE = 9;
    private static final int LMELEE = 10;
    private static final int DYING = 11;
    private static final int FLASHSTEP = 12;
    private static final int RANGEDSTRONG = 13;
    private static final int RANGEDQ = 14;

    //Constructor
    public Player(TileMap tm) {
        super(tm);

        width = 50;
        height = 37;
        cwidth = 23;
        cheight = 30;

        moveSpeed = 0.4;
        maxSpeed = 2.3;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;

        facingRight = true;
        hasStrongShot = false;

        maxHealth = 30.0;
        health = maxHealth;
        maxAmmo = 5000;
        ammo = maxAmmo;

        ammoCost = 1000;
        rangedDamage = 8;
        arrows = new ArrayList<Arrow>();

        meleeDamageL = 2;
        meleeRangeL = 40;

        //load sprites
        loadSprites();
       
        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(400);

        //set-up sound effects
        //jump
        AudioPlayer.load("/Sound FX/Bounce.wav", "jump");
        AudioPlayer.decreaseVolume("jump", -20.0f);
        //quick shot
        //strong shot
        AudioPlayer.load("/Sound FX/FlameArrow.wav", "strong");
        AudioPlayer.decreaseVolume("strong", -20.0f);
        /*
        sfx = new HashMap<String, AudioPlayer>();
        sfx.put("jump", new AudioPlayer("/Sound FX/NFF-jump.wav"));
        sfx.get("jump").decreaseVolume(-50.0f); */

    }

    public void loadSprites() {
        //load sprites
        try {

            BufferedImage mainSpritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/AdventurerAVBFinal.png"));
            BufferedImage bowSpritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/BowFinal.png"));

            sprites = new ArrayList<BufferedImage[]>();

            //MAIN
            //Fill array list with BufferedImage arrays of images, for animation
            //13 rows
            for (int i = 0; i < 13; i++) {
                //20 columns - but only for the needed number of frames for each animation
                //create a 2D array to hold all animation images
                BufferedImage[] playerSprites = new BufferedImage[numFrames[i]];
                for (int j = 0; j < numFrames[i]; j++) {
                    playerSprites[j] = mainSpritesheet.getSubimage(j * width, i * height, width, height);
                }
                sprites.add(playerSprites); //add to array list of player animation
            }
            //BOW //Same as above, but with the seperate bow spritesheet
            for (int i = 0; i < 2; i++) {
                BufferedImage[] bowSprites = new BufferedImage[numFrames[i + RANGEDSTRONG]];
                for (int j = 0; j < numFrames[i + RANGEDSTRONG]; j++) {
                    bowSprites[j] = bowSpritesheet.getSubimage(j * width, i * height, width, height);
                }
                sprites.add(bowSprites);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getHealth() {
        return health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public int getAmmo() {
        return ammo;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public void setRangedAttacking() {
        sRangedAttacking = true;
    }
    public boolean getRangedAttacking() {
        return sRangedAttacking;
    }

    public void setMeleeAttacking() {
        meleeAttacking = true;
    }
    public boolean getMeleeAttacking() {
        return meleeAttacking;
    }
    
    public boolean getIsAttacking(){
        return meleeAttacking ^ sRangedAttacking;  
    }

    public void setGliding(boolean b) {
        gliding = b;
    }

    public void setFlashstepping() {
        flashstepping = true;
    }
    
    public boolean isDead(){
        return dead;
    }
    
    public void respawn(){
        health = maxHealth;
        ammo = maxAmmo;
        currentAction = IDLE;
        dead = false;
        x = 100;
        y = 100;
        //fix jumping while dead
    }

    public void checkAttack(ArrayList<Enemy> enemies) {

        //loop through enemies
        for (int i = 0; i < enemies.size(); i++) { //linear search through enemies to check if they are attacked

            Enemy e = enemies.get(i);

            //check melee attack
            if (meleeAttacking) {
                if (facingRight) {
                    if (e.getx() > x //Makes sure enemy is in front of the player when facing right
                            && e.getx() < x + meleeRangeL
                            && e.gety() > y - height / 2
                            && e.gety() < y + height / 2) {
                        e.hit(meleeDamageL);
                    }
                } else {
                    if (e.getx() < x //Makes sure enemy is in front of the player when facing left
                            && e.getx() > x - meleeRangeL
                            && e.gety() > y - height / 2
                            && e.gety() < y + height / 2) {
                        e.hit(meleeDamageL);
                    }
                }
            }//end meleeAttacking if

            //Projectiles
            for (int j = 0; j < arrows.size(); j++) { //loop through arrows
                if (arrows.get(j).intersects(e)) { //if it hits the enemy
                    e.hit(rangedDamage); //causes damage to the enemy
                    arrows.get(j).setHit(); //specifies that arrow has hit something
                    break; //end whole loop if enemy has been hit
                }
            }

            //check enemy collision
            if (intersects(e)) {
                hit(e.getDamage());
            }

        }//End enemy search loop

    }

    public void hit(int damage) {
        if (flinching || flashstepping || dead) { //can't be damaged while flinching or flashstepping
            return;
        }

        health -= damage;
        if (health < 0) {
            health = 0;
        }
        if (health == 0) {
            dead = true;
        }
        if (!dead){
        flinching = true;
        flinchTime = System.nanoTime();
        }
    }

    private void getNextPosition() {

        if (flashstepping) {
            maxSpeed = 6.0;
            moveSpeed = 0.8;
            fallSpeed = 0.05;
            maxFallSpeed = 2.0;
            jumpStart = 0;
        } else if(gliding) {
            maxSpeed = 3.0;
        } else {
            maxSpeed = 2.3;
            moveSpeed = 0.4;
            fallSpeed = 0.15;
            maxFallSpeed = 4.0;
            jumpStart = -4.8;
        }

        //movement
        if (left && !dead) {
            dx -= moveSpeed; //speeds up
            if (dx < -maxSpeed) { //limits the max speed of the player
                dx = -maxSpeed;
            }

        } else if (right && !dead) {
            dx += moveSpeed; //speeds up
            if (dx > maxSpeed) { //limits the max speed of the player
                dx = maxSpeed;
            }

        } else { //stopped/not moving
            if (flashstepping) {
                if (facingRight) {
                    dx = 4.0;
                } else {
                    dx = -4.0;
                }
            } else {
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
        }

        //cannot attack while moving (but still in the air)
        if ((currentAction == MMELEE || 
                currentAction == HMELEE || 
                currentAction == RANGEDSTRONG ||
                currentAction == DYING) && !(jumping || falling)) {
            dx = 0;
        }

        //jumping
        if (jumping && !falling) {
            if (!flashstepping && !dead) {
                AudioPlayer.play("jump");
                dy = jumpStart;
            falling = true;
            }
            
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
        
        //check action has stopped
        if (currentAction == LMELEE) {
            if (animation.hasPlayedOnce()) {
                meleeAttacking = false;
            }
        } else if (currentAction == RANGEDSTRONG) {
            if (animation.hasPlayedOnce()) {
                sRangedAttacking = false;
            }
        } else if (currentAction == FLASHSTEP) {
            if (animation.hasPlayedOnce()) {
                flashstepping = false;
            }
        } else if (currentAction == DYING){
            if (animation.hasPlayedOnce()){
                isDying = false;
                dead = true;
                health = maxHealth;
            }
        }

        //shoot an arrow
        ammo += 2; //constantly regenerates the ammo
       if (!dead) health += 0.01; //constantly regenerates health
        if (ammo > maxAmmo) { //limits it to the specified max ammo
            ammo = maxAmmo;
        }
        if (health > maxHealth){ //limits it to maximum health
            health = maxHealth;
        }
        if (sRangedAttacking && currentAction != RANGEDSTRONG && !hasStrongShot) {
            //Creates an arrow entity, and specifies direction based on where player is facing
            //Only if sufficient ammo is present
            if (ammo > ammoCost) {
                ammo -= ammoCost; //subtracts the ammo used to  attack
                hasStrongShot = true;
                shotDelay = System.nanoTime();
            }
        }
        if (hasStrongShot) {
            long elapsed = (System.nanoTime() - shotDelay) / 1000000;
            if (elapsed > 93 * numFrames[RANGEDSTRONG]) {
                Arrow ar = new Arrow(tileMap, facingRight);
                ar.setPosition(x, y + 3); //sets it to the same position as player
                arrows.add(ar);
                hasStrongShot = false;
                //plays sound effect
                AudioPlayer.play("strong");
            }
        }

        //update arrows
        for (int i = 0; i < arrows.size(); i++) {
            arrows.get(i).update();
            if (arrows.get(i).shouldRemove()) { //removes from game if it hits
                //remove from array list and reduce the index to make sure all items in the array list are updated
                arrows.remove(i);
                i--;
            }
        }

        //check if done flinching
        if (flinching) {
            long elapsed = (System.nanoTime() - flinchTime) / 1000000;
            if (elapsed > 1000) { //stops flinching after one second
                flinching = false;
            }
        }

         //check if player's health has reached 0
        if (dead && health == 0){
                isDying = true;
                meleeAttacking = false;
                sRangedAttacking = false;
                flashstepping = false;
                dx = 0;
                dy = 0;
        }
        
        if (!isDying && dead && health > 0){ //they have finished dying
            animation.setFrame(9);
            return;
        }
        
        // set animation
        if (meleeAttacking) {
            sRangedAttacking = false;
            flashstepping = false;
            dx *= 0.8; //makes them move 20% slower
            if (currentAction != LMELEE) {
                currentAction = LMELEE;
                animation.setFrames(sprites.get(LMELEE));
                animation.setDelay(50);
                //REMOVE width = 60;
            }

        } else if (sRangedAttacking) {

            if (currentAction != RANGEDSTRONG) {
                currentAction = RANGEDSTRONG;
                animation.setFrames(sprites.get(RANGEDSTRONG));
                animation.setDelay(100);
                //width = 30;
            }

        } else if (flashstepping) {
            if (currentAction != FLASHSTEP) {
                currentAction = FLASHSTEP;
                animation.setFrames(sprites.get(FLASHSTEP));
                animation.setDelay(15);
            }

        } else if (isDying) {
            if (currentAction != DYING){
                currentAction = DYING;
                animation.setFrames(sprites.get(DYING));
                animation.setDelay(375);
            }
        }else if (dy > 0) {

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
        if (currentAction != LMELEE &&
                currentAction != RANGEDSTRONG &&
                currentAction != DYING) { //makes sure the player isn't turning while attacking
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

        //draw arrows
        for (int i = 0; i < arrows.size(); i++) {
            arrows.get(i).draw(g);
        }

        //draw player
        if (flinching) {
            long elapsed = (System.nanoTime() - flinchTime) / 1000000;
            if (elapsed / 10 % 2 == 0) {
                return; //doesn't draw the player (flashing effect when hit) - every 100 milliseconds
            }
        }

        if (dead && !isDying && health == maxHealth){
            System.out.println("DEAD");
            Font font = new Font("Century Gothic", Font.BOLD, 20);
            g.setFont(font);
            g.setColor(Color.DARK_GRAY);
            g.drawString("Press ENTER to Respawn", (int)(GamePanel.WIDTH / 2) - 115, (int)(GamePanel.HEIGHT / 2) + 10);
            g.drawString("Press ENTER to Respawn", (int)(GamePanel.WIDTH / 2) - 115, (int)(GamePanel.HEIGHT / 2) + 9);
            g.setColor(Color.WHITE);
            g.setFont(font);
            g.drawString("Press ENTER to Respawn", (int)(GamePanel.WIDTH / 2) - 113, (int)(GamePanel.HEIGHT / 2) + 10);
        }
        
        /*if (facingRight) {
            g.drawImage(animation.getImage(), (int) (x + xmap - width / 2), (int) (y + ymap - height / 2), null);
        } else { //facing left - flips the image because it is only facing right (-width)
            g.drawImage(animation.getImage(), (int) (x + xmap - width / 2 + width), (int) (y + ymap - height / 2), -width, height, null);
        }*/
        super.draw(g);
    }

}
