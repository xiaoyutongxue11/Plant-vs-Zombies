import greenfoot.*;

/**
 * NewspaperZombie 是报纸僵尸，继承自 Zombie 类。
 * 它有一个死亡动画，生命值较高，吃植物的动画也不同于其他僵尸。
 */
public class NewspaperZombie extends Zombie {

    public NewspaperZombie() {
        super();  // 调用父类构造函数
        setSpeed(2);  // 设置速度为2，报纸僵尸较快
        loadMoveFrames();  // 加载移动动画帧
    }

    // 加载移动动画帧
    private void loadMoveFrames() {
        frames = new GreenfootImage[10];  // 假设有10帧移动动画
        for (int i = 0; i < frames.length; i++) {
            frames[i] = new GreenfootImage("images/Zombie/NewspaperZombie/move/move" + (i + 1) + ".png");
            frames[i].scale(frames[i].getWidth() * 6/ 8, frames[i].getHeight() * 6/ 8);  // 调整大小
        }
        setImage(frames[0]);  // 设置初始图像
    }

    // 加载吃植物的动画帧
    @Override
    protected void loadEatingFrames() {
        eatingFrames = new GreenfootImage[8];  // 假设有8帧吃植物动画
        for (int i = 0; i < eatingFrames.length; i++) {
            eatingFrames[i] = new GreenfootImage("images/Zombie/NewspaperZombie/attack/attack" + (i + 1) + ".png");
            eatingFrames[i].scale(eatingFrames[i].getWidth() * 6 / 8, eatingFrames[i].getHeight() * 6 / 8);  // 调整大小
        }
    }

    @Override
    public void act() {
        super.act();  // 调用父类的 act 方法，控制移动、死亡、吃植物等行为
    }
}
