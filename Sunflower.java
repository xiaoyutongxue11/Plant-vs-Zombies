import greenfoot.*;

/**
 * Sunflower 是一个不进行攻击的植物，每隔一段时间生成太阳。
 */
public class Sunflower extends Plant {
    private GreenfootImage[] frames;  // 存储太阳花的动画帧
    private int currentFrame = 0;     // 当前帧索引
    private int frameCount = 0;       // 计数器：记录执行了多少次 act()
    private int sunProductionRate = 500;  // 太阳生成的时间间隔（1000帧 = 10秒）
    private int timeSinceLastSun = 0;  // 记录上一次生成太阳的时间

    public Sunflower() {
        super(100, 50, 0);  // 假设太阳花有100生命值，花费50个太阳，不造成伤害
        frames = new GreenfootImage[18];  
        for (int i = 0; i < frames.length; i++) {
            frames[i] = new GreenfootImage("images/sunflower/SunFlower" + (i + 1) + ".png");
            if (frames[i] == null) {
                System.out.println("Error loading image for frame " + (i + 1));
            }
        }
        setImage(frames[0]);  // 初始显示第一帧
    }

    public void act() {
        super.handleBeingEaten();  // 检查是否被僵尸吃，并更新植物状态

        // 如果植物死亡，从世界中移除
        if (health <= 0) {
            if (getWorld() != null) {
                getWorld().removeObject(this);  // 植物死亡后移除它
            }
            return;  // 停止执行后续的逻辑
        }

        frameCount++;  // 每次调用 act() 时增加计数器
        animatePlant();  // 执行动画

        // 每隔 sunProductionRate 帧生成一个太阳
        timeSinceLastSun++;
        if (timeSinceLastSun >= sunProductionRate) {
            generateSun();  // 生成一个太阳
            timeSinceLastSun = 0;  // 重置计时器
        }
    }

    // 控制太阳花的动画：每隔一段时间更新帧
    private void animatePlant() {
        if (frameCount % 2 == 0) {
            currentFrame = (currentFrame + 1) % frames.length;  // 循环显示动画帧
            setImage(frames[currentFrame]);  // 设置当前帧的图像
        }
    }

    // 生成太阳，并将太阳添加到世界中
    private void generateSun() {
        if (getWorld() != null) {  // 确保世界不为空
            Sun sun = new Sun(getX(), getY());
            getWorld().addObject(sun, getX(), getY());  // 将太阳添加到世界中
            sun.startMoving();  // 启动太阳的移动
        } else {
            System.out.println("World is null, cannot generate sun");
        }
    }
}
