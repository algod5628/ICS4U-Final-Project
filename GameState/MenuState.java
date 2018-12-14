/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameState;

import java.awt.*;
import TileMap.Background;
import Handlers.Keys;

/**
 *
 * @author Family
 */
public class MenuState extends GameState {

    private Background bg;

    private int currentChoice; //keeps track of the selected option
    private String[] options = { //List of options that are given to the user
        "LEVEL SELECT",
        "CONTROLS",
        "CREDITS",
        "QUIT"
    };

    //Sets-up the font style and colour of the text
    private Color titleColor;
    private Font titleFont;
    private Font font;
    //Allow the user to use key input to select the options
    //(since it runs at 60fps, there needs to be a delay or else it will update too fast)
    private boolean selection = false;
    private long selectionTime;

    public MenuState(GameStateManager gsm) {
        //sets the game state manager object to the protected variable instantiated in the GamePanel class
        this.gsm = gsm; 

        try { //try to read-in image file
            bg = new Background("/Backgrounds/menubg.gif", 1);
            bg.setVector(-0.3, 0); //sets it to move to the left

            //set-up font style of the text
            titleColor = new Color(128, 0, 0);
            titleFont = new Font("Century Gothic", Font.BOLD, 28);
            font = new Font("Arial", Font.BOLD, 14);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes necessary variables and objects for the class
     */
    public void init() {} //included in the GameState class, but isn't defined here

    /**
     * Updates necessary variables and objects for the class
     */
    public void update() {
        bg.update(); //updates background
        handleInput(); //updates the user input detection
    }

    /**
     * Draws everything that is needs to be displayed
     * @param g Graphics2D object that is instantiated in the GamePanel class
     */
    public void draw(Graphics2D g) {
        bg.draw(g); //draws background

        //draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("UNDECIDED NAME", 30, 70);

        //Draw menu options
        g.setFont(font);
        for (int i = 0; i < options.length; i++) {
            if (i == currentChoice) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(Color.BLUE);
            }
            g.drawString(options[i], 30, 138 + i * 22);
        }
    }

    /**
     * Selects the user's current option choice
     */
    private void select() {
        if (currentChoice == 0) {
            //Start level select
            gsm.setState(GameStateManager.LEVELSELECTSTATE);
        }
        if (currentChoice == 1) {
            //Controls
            gsm.setState(GameStateManager.CONTROLSSTATE);
        }

        if (currentChoice == 2) {
            //Credits
            gsm.setState(GameStateManager.CREDITSSTATE);
        }
        if (currentChoice == 3) {
            //Quit
            System.exit(0);
        }
    }

    /**
     * Detects user input and does the necessary actions
     */
    public void handleInput() {
        if (selection) {
            long elapsed = (System.nanoTime() - selectionTime) / 1000000;
            if (elapsed > 150) {
                selection = false;
            }
        }
        
        if (Keys.isPressed(Keys.ENTER)) {
            select();
        }
        if (Keys.isPressed(Keys.UP)) {
           // if (currentChoice > 0) {
                if(!selection){
                    currentChoice--;
                    if (currentChoice < 0){
                        currentChoice = options.length - 1;
                    }
                    selection = true;
                    selectionTime = System.nanoTime();
                }
                
          //  }
        }

        if (Keys.isPressed(Keys.DOWN)) {
           // if (currentChoice < options.length - 1) {
                if(!selection){
                    currentChoice++;
                    if (currentChoice >= options.length){
                currentChoice = 0;
            }
                    selection = true;
                    selectionTime = System.nanoTime();
                }
            }
        //}

    }
    /* REMOVE
    public void keyPressed(int k){
        if (k == KeyEvent.VK_ENTER){//select
            select();
        }
        if (k == KeyEvent.VK_UP){//up
            currentChoice--;
            if (currentChoice < 0){
                currentChoice = options.length - 1;
            }
        }
        if (k == KeyEvent.VK_DOWN){//down
            currentChoice++;
            if (currentChoice >= options.length){
                currentChoice = 0;
            }
        }
    }
    public void keyReleased(int k){
        
    }
     */

}
