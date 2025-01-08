import greenfoot.*;

public class Bullet extends Actor {
    private int damage = 25;  // 子弹造成的伤害

    public Bullet() {
        setImage("fly1.png");  // 设置子弹图像
    }

    public void act() {
        if (getWorld() == null) {
            return;  // 如果 Bullet 没有被添加到世界中，直接退出
        }
        
        move(5);  // 子弹向右移动（可以根据需要调整速度）

        // 检查是否超出屏幕
        if (getX() > getWorld().getWidth()) {
            getWorld().removeObject(this);  // 如果子弹移出屏幕，移除子弹
            return;  // 移除后不再执行后续代码
        }

        // 检查是否与僵尸发生碰撞
        checkCollisionWithZombie();
    }

    // 播放音效
    private void playShootSound() {
        GreenfootSound shootSound = new GreenfootSound("shoot.mp3");
        shootSound.play();
    }
    
    // 检查子弹是否与僵尸碰撞
    private void checkCollisionWithZombie() {
        if (getWorld() != null) {  // 确保 Bullet 还在世界中
            Zombie zombie = (Zombie) getOneIntersectingObject(Zombie.class);
            if (zombie != null) {
                zombie.takeDamage(damage);  // 给僵尸造成伤害
                playShootSound();  // 播放音效
                getWorld().removeObject(this);  // 移除子弹
            }
        }
    }
}
