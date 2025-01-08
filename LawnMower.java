import greenfoot.*;  // 导入Greenfoot类

public class LawnMower extends Actor {
    private boolean isMoving = false;  // 是否正在移动
    private int speed = 3;  // 推车的移动速度

    private GreenfootSound moveSound;  // 用于播放移动音效

    public LawnMower() {
       GreenfootImage image = new GreenfootImage("images/LawnMower.png");  // 加载推车图片
        image.scale(image.getWidth() * 7 / 8, image.getHeight() * 7 / 8);   // 将图像缩小为原来的八分之七
        setImage(image);  // 设置缩放后的图片
        moveSound = new GreenfootSound("sounds/Cart.mp3");  // 初始化音效
    }

    // 每帧更新推车状态
    public void act() {
        if (isMoving) {
            moveCar();
        }
    }

    // 启动推车的移动
    public void startMoving() {
        if (!isMoving) {
            isMoving = true;
            moveSound.play();  // 播放音效
        }
    }

    // 控制推车的移动
    private void moveCar() {
        setLocation(getX() + speed, getY());  // 向右移动
        if (getX() > getWorld().getWidth()) {  // 如果推车移出场景，停止移动
            getWorld().removeObject(this);  // 从世界中移除推车
        }
    }

    // 检查与僵尸的碰撞
    public void checkCollisionWithZombie() {
        Actor zombie = getOneIntersectingObject(Zombie.class);  // 检查是否有僵尸与推车碰撞
        if (zombie != null) {
            startMoving();  // 启动推车的移动
            getWorld().removeObject(zombie);  // 移除被推走的僵尸
        }
    }
}
