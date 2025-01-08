import greenfoot.*;

/**
 * TallWallNut 是一个防御型植物，具有较高的生命值并能阻挡僵尸。
 * 它会使用14张图片进行逐帧动画展示。
 */
public class TallWallNut extends Plant {
    private GreenfootImage[] framesLevel1;  // 存储坚果墙的动画帧（level 1）
    private GreenfootImage[] framesLevel2;  // 存储坚果墙的动画帧（level 2）
    private GreenfootImage[] framesLevel3;  // 存储坚果墙的动画帧（level 3）
    private int currentFrame = 0;           // 当前帧索引
    private int frameCount = 0;             // 计数器，用来追踪 act() 被调用的次数
    private int animationSpeed = 5;         // 动画速度（每 5 次 act 更新一次帧）

    public TallWallNut() {
        super(400, 50, 0);  // 设定默认生命值为 600，初始位置为 (50, 50)
        framesLevel1 = new GreenfootImage[16];  // 为 level 1 分配空间
        framesLevel2 = new GreenfootImage[11];  // 为 level 2 分配空间
        framesLevel3 = new GreenfootImage[15];  // 为 level 3 分配空间
        
        // 加载 level 1 的图片并缩小
        for (int i = 0; i < framesLevel1.length; i++) {
            framesLevel1[i] = new GreenfootImage("images/TallWallNut/WallNut_level1_" + (i + 1) + ".png");
            framesLevel1[i].scale(framesLevel1[i].getWidth() * 7/ 8, framesLevel1[i].getHeight() * 7/ 8); // 缩小到原大小的70%
        }
        
        // 加载 level 2 的图片并缩小
        for (int i = 0; i < framesLevel2.length; i++) {
            framesLevel2[i] = new GreenfootImage("images/TallWallNut/WallNut_level2_" + (i + 1) + ".png");
            framesLevel2[i].scale(framesLevel2[i].getWidth() * 7/ 8, framesLevel2[i].getHeight() * 7/ 8); // 缩小到原大小的70%
        }
        
        // 加载 level 3 的图片并缩小
        for (int i = 0; i < framesLevel3.length; i++) {
            framesLevel3[i] = new GreenfootImage("images/TallWallNut/WallNut_level3_" + (i + 1) + ".png");
            framesLevel3[i].scale(framesLevel3[i].getWidth() * 7/ 8, framesLevel3[i].getHeight() * 7/ 8); // 缩小到原大小的70%
        }
        
        // 默认使用 level 1 的图片
        setImage(framesLevel1[0]);
    }

    public void act() {
        super.handleBeingEaten();    // 检查是否被僵尸吃，并更新植物状态

         // 如果植物死亡，从世界中移除
        if (health <= 0) {
            if (getWorld() != null) {
                getWorld().removeObject(this);  // 植物死亡后移除它
            }
            return;  // 停止执行后续的逻辑
        }
        animateTallWallNut();  // 控制坚果墙的动画帧更新
    }

    // 控制坚果墙的动画：每隔几次 act 更新一次帧
    private void animateTallWallNut() {
        frameCount++;  // 每次 act() 被调用时增加计数
        if (frameCount % animationSpeed == 0) {
            // 根据当前的生命值选择不同的动画帧
            if (health > 300) {
                currentFrame = (currentFrame + 1) % framesLevel1.length;  // level 1 动画
                setImage(framesLevel1[currentFrame]);
            } else if (health > 200) {
                currentFrame = (currentFrame + 1) % framesLevel2.length;  // level 2 动画
                setImage(framesLevel2[currentFrame]);
            } else {
                currentFrame = (currentFrame + 1) % framesLevel3.length;  // level 3 动画
                setImage(framesLevel3[currentFrame]);
            }
        }
    }
    
    // 其他可以根据需要添加的方法，比如控制坚果墙的行为等。
}