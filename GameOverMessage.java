import greenfoot.*;  

public class GameOverMessage extends Actor {
    
    public GameOverMessage() {
        // Set the image for the game over message
        setImage(new GreenfootImage("game_over.png"));
    }

    public void act() {
        // Check if the image is clicked
        if (Greenfoot.mouseClicked(this)) {
            // Transition back to the StartWorld
            Greenfoot.setWorld(new StartWorld());
        }
    }
}
