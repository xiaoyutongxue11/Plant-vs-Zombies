import greenfoot.*;

/**
 * BucketheadZombie 是带有桶头的僵尸，继承自 Zombie 类。
 * 它有一个死亡动画，生命值较高，吃植物的动画也不同于普通僵尸。
 */
public class BucketheadZombie extends Zombie {
    
    public BucketheadZombie() {
        super();  // 调用父类构造函数
        setHealth(175);  // 生命值增加为175，战斗力更强
        loadMoveFrames();  // 加载移动动画帧
        setSpeed(1);        // 设置速度
    }

    // 加载移动动画帧
    private void loadMoveFrames() {
        frames = new GreenfootImage[15];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = new GreenfootImage("images/Zombie/BucketheadZombie/move/move" + (i + 1) + ".png");
            frames[i].scale(frames[i].getWidth() * 6/ 8, frames[i].getHeight() * 6/ 8);  // 调整大小
        }
        setImage(frames[0]);  // 设置初始图像
    }

    // 加载吃植物的动画帧
    @Override
    protected void loadEatingFrames() {
        eatingFrames = new GreenfootImage[11];  // 假设有11帧吃植物动画
        for (int i = 0; i < eatingFrames.length; i++) {
            eatingFrames[i] = new GreenfootImage("images/Zombie/BucketheadZombie/attack/attack" + (i + 1) + ".png");
            eatingFrames[i].scale(eatingFrames[i].getWidth() * 6 / 8, eatingFrames[i].getHeight() * 6 / 8);  // 调整大小
        }
    }

    @Override
    public void act() {
        super.act();  // 调用父类的 act 方法，控制移动、死亡、吃植物等行为
    }
}
