/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameState;

/**
 *
 * @author algod5628
 */
import Handlers.Keys;
import java.awt.*;
import TileMap.Background;
import java.awt.event.KeyEvent;

public class LevelSelectState extends GameState {
    
    private Background bg;
    private int currentChoice;
    private String[] levels = {
        "1",
        "2",
        "3",
        "4",
        "5"
    };
    private Color titleColor;
    private Font titleFont;
    private Font font;

    private boolean selection = false;
    private long selectionTime;
    
    /**
     * 
     * @param gsm Game State manager class
     */
    public LevelSelectState(GameStateManager gsm) {
        this.gsm = gsm;
        try {
            bg = new Background("/Backgrounds/menubg.gif", 1);
            bg.setVector(-0.3, 0);

            titleColor = new Color(128, 0, 0);
            titleFont = new Font("Century Gothic", Font.BOLD, 27);
            font = new Font("Arial", Font.BOLD, 14);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void init(){
        
    }
    
    public void update(){
        bg.update();
        handleInput();
    }
    
    public void draw(Graphics2D g){
        bg.draw(g);
        
        //draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("SELECT YOUR LEVEL", 30, 70);
        
        
        //draw levels
        g.setFont(font);
        for (int i = 0; i < levels.length; i++) {
            if(i == currentChoice){
                g.setColor(Color.WHITE);
            }else {
                g.setColor(Color.BLACK);
            }
            
            g.fillRect(i*30+95,135,20,20);
            if(i == currentChoice){
                g.setColor(Color.BLACK);
            }else {
                g.setColor(Color.WHITE);
            }
            g.drawString(levels[i], i*30 + 100, 150);
            
        }
        g.setColor(Color.BLUE);
        g.drawString("BACK",0,10);
        
    }
    private void select(){
        if (currentChoice == 0){
            //Level 1
            gsm.setState(GameStateManager.LEVEL1STATE);
        }
        if (currentChoice == 1){
            //Level 2
            System.out.println("2");
        }
        
        if (currentChoice == 2){
            //Level 3
            System.out.println("3");
        }
        if (currentChoice == 3){
            //Level 4
            System.out.println("4");
        }
        if (currentChoice == 4){
            //Level 5
            System.out.println("5");
        }
    }
    
    /**
     * Detects user input (pressing backspace to go back to main menu)
     */
    public void handleInput(){
        if (selection) {
            long elapsed = (System.nanoTime() - selectionTime) / 1000000;
            if (elapsed > 150) {
                selection = false;
            }
        }
        
        if (Keys.isPressed(Keys.BACKSPACE)) {
            //Go back to main menu
            gsm.setState(GameStateManager.MENUSTATE);
        }
        
        if (Keys.isPressed(Keys.ENTER)){
            //play selected level
            select();
        }
        
        if (Keys.isPressed(Keys.LEFT)) {
                if(!selection){
                    currentChoice--;
                    if (currentChoice < 0){
                        currentChoice = levels.length - 1;
                    }
                    selection = true;
                    selectionTime = System.nanoTime();
                }
                
        }

        if (Keys.isPressed(Keys.RIGHT)) {
                if(!selection){
                    currentChoice++;
                    if (currentChoice >= levels.length){
                currentChoice = 0;
            }
                    selection = true;
                    selectionTime = System.nanoTime();
                }
            }
    }
    
    /*
    public void keyPressed(int k){
        if (k == KeyEvent.VK_ENTER){//select
            select();
        }
        if (k == KeyEvent.VK_RIGHT){//right
            currentChoice++;
            if (currentChoice >= 5){
                currentChoice = levels.length - 1;
            }
        }
        if (k == KeyEvent.VK_LEFT){//left
            currentChoice--;
            if (currentChoice < 0){
                currentChoice = 0;
            }
        }
        
        if(k == KeyEvent.VK_BACK_SPACE){
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }
    
    public void keyReleased(int k){
        
    }
*/


}


