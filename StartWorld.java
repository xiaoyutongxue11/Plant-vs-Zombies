import greenfoot.*;

/**
 * StartWorld - The main start screen for the Plants vs. Zombies game.
 * Displays the start screen with a background image and a start button.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class StartWorld extends World {

    private GreenfootSound backgroundMusic;  // Declare the music variable

    /**
     * Constructor for objects of class StartWorld.
     */
    public StartWorld() {
        // Create a new world with 1000x600 cells with a cell size of 1x1 pixels.
        super(1200, 700, 1); 

        // Set the background image to StartScreenBg.png and scale it to fit the world size.
        GreenfootImage background = new GreenfootImage("StartScreenBg.png");
        background.scale(getWidth(), getHeight());
        setBackground(background);

        // Add the StartButton at the center of the screen
        StartButton startButton = new StartButton(this);  // Pass the current world (StartWorld) to the button
        addObject(startButton, getWidth() / 2 + 200, getHeight() / 2 - 100); // Position the button

        // Initialize and play the background music
        backgroundMusic = new GreenfootSound("Loonboon.mp3");
        backgroundMusic.playLoop();  // Play the music in a loop
    }

    /**
     * Method to stop the background music in StartWorld.
     */
    public void stopMusic() {
        backgroundMusic.stop();
    }
}
