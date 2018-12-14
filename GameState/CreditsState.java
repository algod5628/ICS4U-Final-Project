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

public class CreditsState extends GameState {

    private Background bg;
    private String[] credits = {
        "AUSTIN VAN BRAECKEL",
        "ALEC GODFREY",
        "SASHA SEUFERT",};

    private Color titleColor;
    private Font titleFont;
    private Font font;

    public CreditsState(GameStateManager gsm) {
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
        g.drawString("CREDITS", 50, 70);

        //draw 
        g.setFont(font);
        g.setColor(Color.BLUE);
        for (int i = 0; i < credits.length; i++) {
            g.drawString(credits[i], 50, 138 + i * 22);
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
    
}
