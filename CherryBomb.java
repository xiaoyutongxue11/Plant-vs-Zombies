import greenfoot.*;

/**
 * CherryBomb 是一个一次性攻击型植物，能够爆炸并对周围的僵尸造成伤害。
 * 它会使用 7 张图片进行动画展示，随后播放爆炸动画。
 */
public class CherryBomb extends Plant {
    private GreenfootImage[] frames;   // 存储动画帧
    private GreenfootImage[] explosionFrames; // 存储爆炸动画帧
    private int currentFrame = 0;      // 当前帧索引
    private int frameCount = 0;        // 计数器，用于追踪 act() 调用次数
    private int animationSpeed = 5;    // 动画速度（每 5 次 act 更新一次帧）
    private boolean exploded = false;  // 是否已爆炸
    private boolean exploding = false; // 是否正在播放爆炸动画

    public CherryBomb() {
        super(1, 0, 0);  // CherryBomb 没有生命值，爆炸后立即消失
        frames = new GreenfootImage[7];
        explosionFrames = new GreenfootImage[12];

        // 加载图片并缩放
        for (int i = 0; i < frames.length; i++) {
            frames[i] = new GreenfootImage("images/CherryBomb/CherryBomb" + (i + 1) + ".png");
            frames[i].scale(frames[i].getWidth() * 4 / 8, frames[i].getHeight() * 4 / 8);  // 缩小到原大小的 70%
        }

        for (int i = 0; i < explosionFrames.length; i++) {
            explosionFrames[i] = new GreenfootImage("images/CherryBomb/bomb/CherryBomb" + (i + 1) + ".png");
            explosionFrames[i].scale(explosionFrames[i].getWidth() * 4 / 8, explosionFrames[i].getHeight() * 4 / 8);  // 缩小到原大小的 70%
        }

        // 默认显示第一张图片
        setImage(frames[0]);
    }

    public void act() {
        if (!exploded && !exploding) {
            animateCherryBomb();
        } else if (exploding) {
            playExplosionAnimation();
        }
    }

    // 控制 CherryBomb 的动画过程
    private void animateCherryBomb() {
        frameCount++;
        if (frameCount % animationSpeed == 0) {
            if (currentFrame < frames.length - 1) {
                currentFrame++;
                setImage(frames[currentFrame]);
            } else {
                explode();
            }
        }
    }

    // 执行爆炸效果，清除附近的僵尸
    private void explode() {
        if (getWorld() != null && !exploded) {
           for (Object obj : getObjectsInRange(200, Zombie.class)) {
                if (obj instanceof Zombie) {
                    ((Zombie) obj).takeDamage(9999);  // 爆炸造成巨大伤害
                    // 设置被 CherryBomb 攻击的僵尸触发特殊死亡动画
                   ((Zombie) obj).setCherryBombDeath(true);
                }
            }
            Greenfoot.playSound("CherryBomb.mp3");  // 播放爆炸音效
            exploded = true;
            exploding = true;
            currentFrame = 0;  // 重置当前帧索引以播放爆炸动画
        }
    }

    // 播放爆炸动画
    private void playExplosionAnimation() {
        frameCount++;
        if (frameCount % animationSpeed == 0) {
            if (currentFrame < explosionFrames.length) {
                setImage(explosionFrames[currentFrame]);
                currentFrame++;
            } else {
                getWorld().removeObject(this);  // 动画结束后移除 CherryBomb
            }
        }
    }
}
