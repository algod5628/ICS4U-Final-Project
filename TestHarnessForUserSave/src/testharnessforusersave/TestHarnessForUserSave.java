
package testharnessforusersave;

public class TestHarnessForUserSave {

    public static void main(String[] args) {
        //Create user
        User me = new User("Sashaa1", 0);
        me.setName("hello");
        User You = new User("Peter", 0);
        User someoneElse = new User("Johnny", 0);
        
        //Get user
        User savedMe[] = (User[])((SuccessResponse)UserManager.getUsers()).getResponse();
        System.out.println(savedMe[0].getName().getResponse());
    }
    
}

/**
 * Todo:...
 * - getUserByID() returns only second user after 2
 * - getUserByID() throws error with id below 1
 */

