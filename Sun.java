import greenfoot.*;

public class Sun extends Actor {
    private int value = 25; // 每个太阳的值为25
    private int targetX, targetY; // 目标位置的X和Y坐标
    private boolean isMoving = false; // 是否正在移动
    private static final int MAX_DISTANCE = 300; // 最大移动距离

    public Sun(int startX, int startY) {
        GreenfootImage image = new GreenfootImage("Sun.png"); 
        image.scale(50, 50);
        setImage(image);
        setLocation(startX, startY); 
    }

    public void act() {
        if (getWorld() != null) {
            getWorld().setPaintOrder(Sun.class);  // 确保阳光显示在最上层
        }
        if (isMoving) {
            // 使太阳逐步移动到目标位置
            int currentX = getX();
            int currentY = getY();
            // 控制移动速度，通过计算步长让太阳平滑移动
            int stepX = (targetX - currentX) / 50; 
            int stepY = (targetY - currentY) / 50; 
            setLocation(currentX + stepX, currentY + stepY);
            // 如果接近目标位置，停止移动
            if (Math.abs(targetX - currentX) < 5 && Math.abs(targetY - currentY) < 5) {
                isMoving = false; // 停止移动
            }
        }

        // 如果点击了太阳，增加25个太阳，并删除太阳对象
        if (Greenfoot.mouseClicked(this)) {
            GameWorld world = (GameWorld) getWorld(); 
            SunButton sunButton = world.getSunButton(); 
            sunButton.incrementSun();
            getWorld().removeObject(this);
        }
    }

    public void startMoving() {
        int currentX = getX();
        int currentY = getY();
        int minX = Math.max(200, currentX - MAX_DISTANCE);
        int maxX = Math.min(getWorld().getWidth() - 1, currentX + MAX_DISTANCE);
        int minY = Math.max(0, currentY - MAX_DISTANCE);
        int maxY = Math.min(getWorld().getHeight() - 1, currentY + MAX_DISTANCE);

        targetX = Greenfoot.getRandomNumber(maxX - minX + 1) + minX;
        targetY = Greenfoot.getRandomNumber(maxY - minY + 1) + minY;

        // 添加最小移动限制，确保目标位置与当前坐标的距离至少为 250
        while (Math.abs(targetX - currentX) < 150 || Math.abs(targetY - currentY) < 150) {
            targetX = Greenfoot.getRandomNumber(maxX - minX + 1) + minX;
            targetY = Greenfoot.getRandomNumber(maxY - minY + 1) + minY;
        }

        isMoving = true; // 开始移动
    }

    public int getValue() {
        return value;
    }
} 