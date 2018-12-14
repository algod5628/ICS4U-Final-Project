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
import java.awt.*;
import TileMap.Background;
import Handlers.Keys;

public class ControlsState extends GameState {

    private Background bg;
    private String[] controls = {
        "LEFT:                      ◄",
        "RIGHT:                     ►",
        "JUMP:                     SPACEBAR",
        "LIGHT MELEE:        A",
        "MEDIUM MELEE:    S",
        "HEAVY MELEE:         D",
        "QUICKSHOT:              Q",
        "STRONGSHOT:           W",
        "FLASH STEP:             SHIFT",
        "GLIDE:                       CTRL"
    };
    private Color titleColor;
    private Font titleFont;
    private Font font;

    public ControlsState(GameStateManager gsm) {
        this.gsm = gsm;
        try {
            bg = new Background("/Backgrounds/menubg.gif", 1);
            bg.setVector(-0.3, 0);

            titleColor = new Color(128, 0, 0);
            titleFont = new Font("Century Gothic", Font.BOLD, 27);
            font = new Font("Arial", Font.BOLD, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {

    }

    public void update() {
        bg.update();
        handleInput();
    }

    public void draw(Graphics2D g) {
        bg.draw(g);

        //draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("CONTROLS", 90, 70);

        //draw 
        g.setFont(font);
        g.setColor(Color.BLUE);
        
        
        for (int i = 0; i < controls.length; i++) {
            if (i < 5) {
                g.drawString(controls[i], 10, 138 + i * 22);
            } else {
                g.drawString(controls[i], 180, 28 + i * 22);
            }
        }
        g.drawString("BACK", 0, 10);
    }
    
    /**
     * Detects user input (pressing backspace to go back to main menu)
     */
    public void handleInput(){
        if (Keys.isPressed(Keys.BACKSPACE)) {
            //Go back to main menu
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }
/* REMOVE
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_BACK_SPACE) {
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }

    public void keyReleased(int k) {

    }*/

}
