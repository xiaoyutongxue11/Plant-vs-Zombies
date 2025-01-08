import greenfoot.*;

public class Shovel extends Actor {
    private boolean isSelected = false;  // 标记铲子是否被选中

    public Shovel() {
        GreenfootImage image = new GreenfootImage("Shovel.png"); 
        image.scale(image.getWidth() / 2, image.getHeight() / 2); 
        setImage(image);
    }

    public void act() {
        MouseInfo mouse = Greenfoot.getMouseInfo();  // 获取鼠标信息

        if (mouse != null && Greenfoot.mouseClicked(this)) {
            isSelected = !isSelected;  // 切换铲子选中状态
            updateShovelImage();
        }

        // 如果铲子被选中，点击草坪进行铲除
        if (isSelected && mouse != null && Greenfoot.mouseClicked(null)) {
            removePlantAt(mouse.getX(), mouse.getY());
        }
    }

    // 铲除当前位置的植物
    private void removePlantAt(int x, int y) {
        // 获取当前位置的所有植物对象
        java.util.List<Plant> plantsAtLocation = getWorld().getObjectsAt(x, y, Plant.class);
        
        // 确保该位置有植物
        if (!plantsAtLocation.isEmpty()) {
            Plant plant = plantsAtLocation.get(0);  // 获取第一个植物
            getWorld().removeObject(plant);  // 移除植物
            isSelected = !isSelected;
            updateShovelImage();
        }
    }
    
     private void updateShovelImage() {
        if (isSelected) {
            GreenfootImage image = new GreenfootImage("Shovel_selected.png");
            image.scale(image.getWidth() / 2, image.getHeight() / 2); 
            setImage(image); 
        } else {
            GreenfootImage image = new GreenfootImage("Shovel.png"); 
            image.scale(image.getWidth() / 2, image.getHeight() / 2); 
            setImage(image); 
        }
    }
}
