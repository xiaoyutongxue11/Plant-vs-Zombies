import greenfoot.*;

/**
 * Peashooter 是一个攻击型植物，可以发射子弹并定期播放动画。
 */
public class Peashooter extends Plant {
    private GreenfootImage[] frames;  // 存储豌豆射手的动画帧
    private int currentFrame = 0;     // 当前帧索引
    private int frameCount = 0;       // 计数器，用来追踪 act() 被调用的次数
    private int animationSpeed = 5;   // 动画速度（每 5 次 act 更新一次帧）
    private int shootingCooldown = 0; // 控制射击频率的冷却计数器
    private GreenfootSound shootSound; // 发射音效

    public Peashooter() {
        super(200, 100, 50);  // 假设豌豆射手有200生命值，花费100个太阳，攻击力为50
        frames = new GreenfootImage[10];  
        for (int i = 0; i < frames.length; i++) {
            frames[i] = new GreenfootImage("images/Peashooter/Peashooter" + (i + 1) + ".png");
        }
        setImage(frames[0]);  // 设置初始图像为第一帧
        // 加载发射音效
        shootSound = new GreenfootSound("peahit.mp3");
    }

    public void act() {
        super.handleBeingEaten();  // 检查是否被僵尸吃，并更新植物状态

        // 如果植物死亡，从世界中移除
        if (health <= 0) {
            if (getWorld() != null) {
                getWorld().removeObject(this);  // 植物死亡后移除它
            }
            return;  // 停止执行后续的射击和动画
        }

        frameCount++;  // 每次 act() 被调用时增加计数
        animatePeashooter();  // 调用动画方法
        shootBullet();  // 检查是否可以射击
    }

    // 控制豌豆射手的动画：每隔几次 act 更新一次帧
    private void animatePeashooter() {
        if (frameCount % animationSpeed == 0) {
            currentFrame = (currentFrame + 1) % frames.length;  // 循环播放动画帧
            setImage(frames[currentFrame]);  // 更新图像为当前帧
        }
    }

    // 控制射击：每隔一定的时间发射一次子弹
    private void shootBullet() {
        // 如果冷却时间为 0，则可以发射子弹
        if (shootingCooldown == 0) {
            Bullet bullet = new Bullet();  // 创建新的子弹
            getWorld().addObject(bullet, getX() + 50, getY()-10);  // 将子弹添加到场景中，位置在豌豆射手右侧
            // 播放发射音效
            shootSound.play();
            shootingCooldown = 50;  // 设置冷却时间，每隔 50 次 act 允许发射一次
        } else {
            shootingCooldown--;  // 每次 act 被调用时冷却时间减少
        }
    }
}
