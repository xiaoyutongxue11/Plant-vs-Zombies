import greenfoot.*;

/**
 * StartButton - Handles the appearance and actions of the start button.
 * Changes its image on hover and starts the game on click.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class StartButton extends Actor {

    private GreenfootImage buttonDark = new GreenfootImage("StartButtonDark.png"); // Default image
    private GreenfootImage buttonLight = new GreenfootImage("StartButtonLight.png"); // Hover image
    private StartWorld startWorld;  // Reference to StartWorld

    public StartButton(StartWorld world) {
        setImage(buttonDark); // Set default button image
        startWorld = world;  // Store the reference to StartWorld
    }

    public void act() {
        // Check if the mouse is hovering over the button
        if (Greenfoot.mouseMoved(this)) {
            setImage(buttonLight);
        } else if (Greenfoot.mouseMoved(null) && !Greenfoot.mouseMoved(this)) {
            setImage(buttonDark);
        }

        // Check if the button is clicked
        if (Greenfoot.mouseClicked(this)) {
            // Stop the background music of StartWorld
            startWorld.stopMusic();

            // Transition to the GameWorld
            Greenfoot.setWorld(new GameWorld());
        }
    }
}
