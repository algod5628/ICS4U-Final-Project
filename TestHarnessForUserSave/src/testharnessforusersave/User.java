/**
 * Sasha Seufert
 * Dec 19th, 2018
 * This is a class that represents a user that plays a game. This class stores the progress of the user, as well as basic information, such as the name, id, etc.
 */
package testharnessforusersave;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    
    //Attributes
    private static int  GREATEST_ID = 0,
                        HIGHEST_LEVEL = 5;
    private static ArrayList<User> allUsers = new ArrayList();
    private String name;
    private int ID,
                currentLevel;
    private ArrayList<Integer> scoreForLevel = new ArrayList();
    
    //Constructors
    /**
     * Primary constructor for a User.
     * @param name Name of the user
     * @param currentLevel Current level of the user (unlocked)
     */
    public User(String name, int currentLevel) {
        //Assign new id
        assignNewID();
        
        //Set attributes
        this.name = name;
        this.currentLevel = currentLevel;
        
        //Add this user to array of all users for this runtime
        allUsers.add(this);
        
        //Update
        UserManager.update();
    }
    
    //Methods
    
    /**
     * Sets the highest level of this game that a user can reach.
     * @param level The level to set
     * @return the response object
     */
    public static Response setHighestLevel(int level) {
        //Check conditions
        if(level < 1) {
            return new ErrorResponse("Cannot set highest level lower than one.");
        }
        
        //Set the level
        HIGHEST_LEVEL = level;
        
        //Return 
        return new SuccessResponse(HIGHEST_LEVEL);
    }
    
    /**
     * Returns all of the users stored during runtime.
     * @return the users as an array.
     */
    public static User[] getAll() {
        //Convert all users to array of Users
        User users[] = new User[allUsers.size()];
        for(int i = 0; i < allUsers.size(); i++)
            users[i] = allUsers.get(i);
        
        //Return
        return users;
    }
    
    /**
     * Returns the highest possible level that the user can reach.
     * @return a response object.
     */
    public static SuccessResponse getHighestLevel() {
        return new SuccessResponse(HIGHEST_LEVEL);
    }
    
    /**
     * Automatically assigns a new unique identifier to the user.
     */
    private Response assignNewID() {
        //Assign an ID that has not be used yet 
        this.ID = GREATEST_ID + 1;
        
        //Increment the greatest id
        GREATEST_ID++;
        
        //Return a response
        return new SuccessResponse(this.ID);
    }
    
    /**
     * Returns the name of the current user
     * @return The name 
     */
    public SuccessResponse getName() {
        return new SuccessResponse(this.name);
    }
    
    public SuccessResponse setName(String name) {
        //Declare variables
        boolean nameWasBlank = false;

        //Check if the name is blank
        if(name.length() == 0) {
            nameWasBlank = true;
            this.name = "[Unnamed User]";
        }
        else this.name = name;
         
        //Update
        UserManager.update();
        
        //Formulate response
        if(nameWasBlank) {
            return new SuccessResponse(true, "You did not choose a name! You have been assigned a default name.");
        }
        else {
            return new SuccessResponse(true);
        }
    }
    
    /**
     * Returns the ID of the current user
     * @return The ID as an int 
     */
    public SuccessResponse getID() {
        return new SuccessResponse(this.ID);
    }
    
    
    /**
     * Returns the score of a level for the current user
     * @param levelIndex Level index
     * @return The score as an int 
     */
    public Response getScoreForLevel(int levelIndex) {
        //Check if that score exists
        if(levelIndex >= scoreForLevel.size()) {
            return new ErrorResponse("Score for level index " + levelIndex + " has not been set for the current user.");
        }
        
        //Return
        return new SuccessResponse((int)this.scoreForLevel.get(levelIndex));
    }
    
    /**
     * Sets the score of the user at a given level.
     * @param score The new score to set
     * @param levelIndex Level to set the score of
     * @return A response that contains the new score
     */
    public Response setScoreForLevel(int score, int levelIndex) {
        //Check if conditions are met
        if(score < 0) {
            return new ErrorResponse("The score cannot be set lower than zero.");
        }
        if(levelIndex < 0) {
            return new ErrorResponse("There is no level with an index lower than zero.");
        }
        
        //Set the score
        this.scoreForLevel.set(levelIndex, score);
        
        //Update
        UserManager.update();
        
        //Return
        return new SuccessResponse(score);
    }
    
    /**
     * Returns the current level of the current user
     * @return The current level as an int 
     */
    public SuccessResponse getCurrentLevel() {
        return new SuccessResponse(this.currentLevel);
    }
    
    /**
     * Sets the current level of the user
     * @param level The level to set
     * @return a response object with corresponding data
     */
    public Response setCurrentLevel(int level) {
        //Check conditions
        if(level < 1 || level > HIGHEST_LEVEL) {
            return new ErrorResponse("Invalid level of " + level + " set.");
        }
        
        //Set the level
        this.currentLevel = level;
        
        //Update
        UserManager.update();
        
        //Return
        return new SuccessResponse(currentLevel);
    }
    
    /**
     * Level up the user by one level
     * @return a response object
     */
    public Response levelUp() {
        //Set the level
        return setCurrentLevel((int)getCurrentLevel().getResponse() + 1);
    }
}
