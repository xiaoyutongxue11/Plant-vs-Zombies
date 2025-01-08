import greenfoot.*;

/**
 * Zombie 是所有僵尸类的父类，提供通用的死亡动画逻辑及吃植物的行为。
 */
public abstract class Zombie extends Actor {
    protected GreenfootImage[] frames;       // 存储移动动画的帧
    protected GreenfootImage[] deathFrames;  // 死亡动画帧
    protected GreenfootImage[] cherryBombDeathFrames;  // 特殊死亡动画帧（CherryBomb攻击死亡）
    protected GreenfootImage[] eatingFrames; // 吃植物的动画帧
    protected int currentFrame = 0;          // 当前帧索引
    protected int frameCount = 0;            // 动画帧计数器
    protected int health;                    // 僵尸的血量
    protected int damage = 5;                // 僵尸的伤害值
    protected int speed;                     // 僵尸的速度

    protected boolean isDead = false;        // 判断僵尸是否死亡
    protected boolean isEating = false;      // 判断僵尸是否正在吃植物
    protected boolean isDying = false;       // 判断是否在死亡过程中
    protected boolean isCherryBombDeath = false; // 判断是否被CherryBomb攻击而死亡

    protected int eatingSpeed = 10;           // 吃植物动画的播放速度
    protected int deathAnimationSpeed = 10;  // 死亡动画的播放速度

    public enum State {
        MOVING, EATING, DYING, DEAD
    }

    private State currentState = State.MOVING; // 当前僵尸状态

    public Zombie() {
        setSpeed(1);      // 默认速度为1
        setHealth(150);   // 默认血量为150
        loadDeathFrames();  // 加载普通死亡动画
        loadCherryBombDeathFrames(); // 加载CherryBomb特殊死亡动画
        loadEatingFrames(); // 加载吃植物动画
    }

    public void act() {
        switch (currentState) {
            case MOVING:
                moveZombie();        // 僵尸移动
                animateZombie();     // 播放移动动画
                break;

            case EATING:
                animateEating();     // 播放吃植物的动画
                break;

            case DYING:
                animateDeath();      // 播放死亡动画
                break;

            case DEAD:
                return;  // 如果已经死亡，停止进一步的动作
        }

        // 检查植物是否在接近范围内，并进行相应的行为
        if (getWorld() != null) {
            Plant plant = (Plant) getOneIntersectingObject(Plant.class);
            if (plant != null && isCenterColliding(plant)) {
                if (!isEating) {
                    startEating(plant);  // 开始吃植物
                    plant.addAttackingZombie(this);  // 将僵尸添加到植物的攻击列表
                }
            }
        }

        // 检查是否与推车碰撞
        if (getWorld() != null && getOneIntersectingObject(LawnMower.class) != null) {
            LawnMower lawnMower = (LawnMower) getOneIntersectingObject(LawnMower.class);
            lawnMower.checkCollisionWithZombie();  // 让推车带走僵尸
        }
    }
// 加载吃植物的动画帧（子类必须实现这个方法）
    protected abstract void loadEatingFrames();
    // 开始吃植物
    protected void startEating(Plant plant) {
        isEating = true;
        currentState = State.EATING;
    }
 // 僵尸的伤害
    public int getDamage() {
        return damage;
    }
    // 停止吃植物
    protected void stopEating() {
        isEating = false;
        currentState = State.MOVING;
    }

    // 播放僵尸的死亡动画
    protected void animateDeath() {
        if (!isDying) {
            return;  // 如果没有在死亡动画状态，跳过
        }

        // 如果是CherryBomb攻击死亡，播放特殊死亡动画
        if (isCherryBombDeath) {
            playCherryBombDeathAnimation();
        } else {
            playNormalDeathAnimation();
        }
    }

    // 播放普通死亡动画
    private void playNormalDeathAnimation() {
        frameCount++;
        if (frameCount % deathAnimationSpeed == 0) {
            currentFrame = (currentFrame + 1) % deathFrames.length;
            setImage(deathFrames[currentFrame]);
        }

        // 如果死亡动画播放完毕，从世界中移除该僵尸
        if (currentFrame == deathFrames.length - 1) {
            getWorld().removeObject(this);  // 移除僵尸
            currentState = State.DEAD;
        }
    }

    // 播放 CherryBomb 特殊死亡动画
    private void playCherryBombDeathAnimation() {
        frameCount++;
        if (frameCount % deathAnimationSpeed == 0) {
            currentFrame = (currentFrame + 1) % cherryBombDeathFrames.length;
            setImage(cherryBombDeathFrames[currentFrame]);
        }

        // 如果特殊死亡动画播放完毕，从世界中移除该僵尸
        if (currentFrame == cherryBombDeathFrames.length - 1) {
            getWorld().removeObject(this);  // 移除僵尸
            currentState = State.DEAD;
        }
    }

    // 播放移动动画
    protected void animateZombie() {
        frameCount++;
        if (frameCount % 6 == 0) {  // 每6次调用 act() 更新一次帧
            currentFrame = (currentFrame + 1) % frames.length;
            setImage(frames[currentFrame]);
        }
    }

    // 播放吃植物的动画
    private void animateEating() {
        frameCount++;
        if (frameCount % eatingSpeed == 0) {
            currentFrame = (currentFrame + 1) % eatingFrames.length;
            setImage(eatingFrames[currentFrame]);
        }

        // 检查植物是否已消失，停止吃动画
        if (getWorld() != null && getOneIntersectingObject(Plant.class) == null) {
            stopEating();  // 停止吃植物的动画
        }
    }

    // 僵尸移动
    protected void moveZombie() {
        setLocation(getX() - speed, getY());  // 向左移动
        if (getX() < 0) {
            getWorld().removeObject(this);  // 如果僵尸移动到屏幕外，删除该对象
        }
    }

    // 僵尸受伤方法
    public void takeDamage(int damage) {
        if (!isDead) {
            health -= damage;  // 减少生命值
            if (health <= 0) {
                die();  // 如果生命值为零，死亡
            }
        }
    }

    // 僵尸死亡处理
    protected void die() {
        isDying = true;  // 开始死亡动画
        if (isCherryBombDeath) {
            // 如果是CherryBomb攻击导致死亡，播放特殊死亡动画
            currentState = State.DYING;
        } else {
            // 否则播放普通死亡动画
            currentState = State.DYING;
        }
    }

    // 加载死亡动画帧
    private void loadDeathFrames() {
        deathFrames = new GreenfootImage[10];  // 假设有10帧普通死亡动画
        for (int i = 0; i < deathFrames.length; i++) {
            deathFrames[i] = new GreenfootImage("images/Zombie/NormalZombie/die/dieh" + (i + 1) + ".png");
            deathFrames[i].scale(deathFrames[i].getWidth() * 6 / 8, deathFrames[i].getHeight() * 6 / 8);
        }
    }

    // 加载 CherryBomb 特殊死亡动画帧
    private void loadCherryBombDeathFrames() {
        cherryBombDeathFrames = new GreenfootImage[20];  // 假设有20帧CherryBomb特殊死亡动画
        for (int i = 0; i < cherryBombDeathFrames.length; i++) {
            cherryBombDeathFrames[i] = new GreenfootImage("images/Zombie/NormalZombie/die/died" + (i + 1) + ".png");
            cherryBombDeathFrames[i].scale(cherryBombDeathFrames[i].getWidth() * 6 / 8, cherryBombDeathFrames[i].getHeight() * 6 / 8);
        }
    }

    // 设置是否是 CherryBomb 攻击死亡
    public void setCherryBombDeath(boolean isCherryBombDeath) {
        this.isCherryBombDeath = isCherryBombDeath;
    }

    // 设置血量
    public void setHealth(int health) {
        this.health = health;
    }

    // 获取血量
    public int getHealth() {
        return health;
    }

    // 设置速度
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    // 获取速度
    public int getSpeed() {
        return speed;
    }

    // 判断僵尸是否与植物发生碰撞
    public boolean isCenterColliding(Plant plant) {
        int zombieX = getX();
        int plantX = plant.getX();
        return (zombieX - plantX) > 0 && (zombieX - plantX) < 70;
    }
} 