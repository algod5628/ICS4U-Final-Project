/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameState;

import Audio.AudioPlayer;
import java.awt.Graphics2D;

/**
 *
 * @author Family
 */
public class GameStateManager {

    private GameState[] gameStates;
    private int currentState;

    public static final int NUMSTATES = 5;
    
    public static final int MENUSTATE = 0;
    public static final int LEVELSELECTSTATE = 1;
    public static final int CREDITSSTATE = 2;
    public static final int CONTROLSSTATE = 3;
    
    public static final int LEVEL1STATE = 4;

    public GameStateManager() {
        gameStates = new GameState[NUMSTATES];
        AudioPlayer.init();
        currentState = MENUSTATE;
        loadState(currentState);
    }

    //Loads states as needed, rather than all at the start-up
    private void loadState(int state) {
        if (state == MENUSTATE) {
            gameStates[state] = new MenuState(this);
        }
        if (state == LEVELSELECTSTATE) {
            gameStates[state] = new LevelSelectState(this);
        }
        if (state == LEVEL1STATE) {
            gameStates[state] = new Level1State(this);
        }
        if (state == CREDITSSTATE) {
            gameStates[state] = new CreditsState(this);
        }
        if (state == CONTROLSSTATE) {
            gameStates[state] = new ControlsState(this);
        }
    }

    //unloads to improve runtime and memory
    private void unloadState(int state) {
        gameStates[state] = null; //sets it to null
    }

    public void setState(int state) {
        unloadState(currentState);
        currentState = state;
        loadState(currentState);
        //gameStates[currentState].init();
    }

    public void update() {
        if (gameStates[currentState] != null){ //if it is not null, update
            gameStates[currentState].update();
        }
        
    }

    public void draw(Graphics2D g) {
        if (gameStates[currentState] != null){ //if it is not null, draw
            gameStates[currentState].draw(g);
        }
    }
/* REMOVE
    public void keyPressed(int k) {
        gameStates[currentState].keyPressed(k);
    }

    public void keyReleased(int k) {
        gameStates[currentState].keyReleased(k);
    }*/
}
