import greenfoot.*;
import java.util.List;
import java.util.ArrayList;
/**
 * Plant 是一个抽象植物类，包含了所有植物共享的功能。
 */
public abstract class Plant extends Actor {
    protected int health;  // 植物的健康值
    protected int sunCost; // 植物的太阳花费
    protected int damage;  // 植物的攻击力
    protected int attackCooldown;  // 攻击冷却时间
    protected int attackTimer;  // 用来计时冷却时间

    protected int eatingDamage = 5;          // 每次被吃时减少的伤害
    protected int eatingTimer = 0;           // 计时器，用于控制植物被吃的时间

    // 存储正在攻击植物的所有僵尸
    protected List<Zombie> attackingZombies = new ArrayList<Zombie>(); // 记录所有攻击该植物的僵尸
    
    public Plant(int health, int sunCost, int damage) {
        this.health = health;
        this.sunCost = sunCost;
        this.damage = damage;
        this.attackCooldown = 50;  // 默认冷却时间设置为50
        this.attackTimer = 0;
    }

    // 累积所有正在攻击的僵尸的伤害
    protected void handleBeingEaten() {
        if (!attackingZombies.isEmpty()) {
            int totalDamage = 0;
            for (Zombie zombie : attackingZombies) {
                totalDamage += zombie.getDamage();  // 累积每个僵尸的攻击伤害
            }

            // 每隔一定时间减少生命值
            eatingTimer++;
            if (eatingTimer % 10 == 0) {
                takeDamage(totalDamage);  // 每隔10帧减少总伤害值
            }

            // 如果植物的生命值小于等于0，就认为植物被吃掉并从世界中移除
            if (health <= 0) {
                if (getWorld() != null) {
                    getWorld().removeObject(this);  // 植物死亡后移除它
                }
            }
        }
    }

    // 检查是否植物被僵尸吃掉
    public boolean isBeingEaten() {
        return !attackingZombies.isEmpty();
    }
    
    // 新增方法：添加攻击的僵尸
    public void addAttackingZombie(Zombie zombie) {
        if (!attackingZombies.contains(zombie)) {
            attackingZombies.add(zombie);
        }
    }

    // 新增方法：移除攻击的僵尸
    public void removeAttackingZombie(Zombie zombie) {
        attackingZombies.remove(zombie);
    }

    // 每次 act() 方法调用时，会调用这个方法来进行攻击（需要在子类中实现具体行为）
    public abstract void act();

    // 处理植物受伤
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            if (getWorld() != null) {
                getWorld().removeObject(this);  // 植物死亡后移除它
            }
        }
    }

    // 获取植物的太阳花费
    public int getSunCost() {
        return sunCost;
    }

    // 获取植物的攻击力
    public int getDamage() {
        return damage;
    }
}



