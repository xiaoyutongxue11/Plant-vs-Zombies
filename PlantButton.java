import greenfoot.*;
import java.awt.Color;

public class PlantButton extends Actor {
    private String plantType;
    private GreenfootImage lightImage;
    private GreenfootImage darkImage;
    private static final int BUTTON_WIDTH = 80;
    private static final int BUTTON_HEIGHT = 60;
    private int plantCost;

    public PlantButton(String plantType) {
        this.plantType = plantType;

        // Set plant cost based on the plant type
        if (plantType.equals("SunFlower")) {
            plantCost = 50;
        } else if (plantType.equals("Peashooter")) {
            plantCost = 100;
        }else if (plantType.equals("CherryBomb")) {
            plantCost = 100;
        }  else {
            plantCost = 50;
        }

        loadImages();

        // Determine the initial state of the button
        boolean canPlant = canAffordPlant(); // Check if player can afford this plant
        updateButtonImage(canPlant);
    }

    // Check if the player has enough resources to afford the plant
    private boolean canAffordPlant() {
        int playerResources = getPlayerResources(); // Replace with actual method to get resources
        return playerResources >= plantCost;
    }

    // Mock method to get player's resources (replace with actual implementation)
    private int getPlayerResources() {
        // This should return the current resources of the player from your game logic
        // Example: return Player.getResources();
        return 100; // Example: player starts with 100 resources
    }

    // Load the light and dark images for the button
    private void loadImages() {
        lightImage = new GreenfootImage(plantType + "Light.png");
        darkImage = new GreenfootImage(plantType + "Dark.png");

        lightImage.scale(BUTTON_WIDTH, BUTTON_HEIGHT);
        darkImage.scale(BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    public String getPlantType() {
        return plantType;
    }

    public int getPlantCost() {
        return plantCost;
    }

    public void updateButtonImage(boolean canPlant) {
        if (canPlant) {
            setImage(lightImage);
        } else {
            setImage(darkImage);
        }
    }

    public void addYellowBorder() {
        GreenfootImage currentImage = getImage();
        GreenfootImage imageWithBorder = new GreenfootImage(currentImage);
        imageWithBorder.setColor(Color.YELLOW);
        imageWithBorder.drawRect(0, 0, BUTTON_WIDTH - 1, BUTTON_HEIGHT - 1);
        setImage(imageWithBorder);
    }

    public void removeYellowBorder() {
        setImage(lightImage);
    }
} 