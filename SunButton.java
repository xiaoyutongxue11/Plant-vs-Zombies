import greenfoot.*;
import java.awt.Color;
import java.util.List;

public class SunButton extends Actor {
    private int sunCount;
    private GreenfootImage buttonImage;
    private GreenfootImage backgroundImage;

    public SunButton() {
        sunCount = 100;
        backgroundImage = new GreenfootImage("suncount.png");
        buttonImage = new GreenfootImage(backgroundImage);
        setImage(buttonImage);
        updateButtonImage(); 
    }

    public void act() {
    }

    public void incrementSun() {
        sunCount += 25;
        updateButtonImage();
        updatePlantButtons(); 
    }

    public boolean decrementSun(int amount) {
        if (sunCount >= amount) {
            sunCount -= amount;
            updateButtonImage();
            updatePlantButtons(); 
            return true;
        }
        return false;
    }

    public int getSunCount() {
        return sunCount;
    }
    
    private void updateButtonImage() {
        buttonImage.clear();
        buttonImage.drawImage(backgroundImage, 0, 0);
        buttonImage.setColor(Color.BLACK);
        buttonImage.drawString(" " + sunCount, 70, 25);
    }

    private void updatePlantButtons() {
        List<PlantButton> plantButtons = getWorld().getObjects(PlantButton.class);
        for (PlantButton button : plantButtons) {
            boolean canPlant = sunCount >= button.getPlantCost();
            button.updateButtonImage(canPlant);  
        }
    }
}
