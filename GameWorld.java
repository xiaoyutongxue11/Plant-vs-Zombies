import greenfoot.*;
import java.util.List;
import java.awt.Color;

public class GameWorld extends World {
    private int scrollX = 0;  // 背景滚动的X坐标
    private int scrollSpeed = 5;  // 背景滚动速度
    private boolean scrollingRight = true;  // 控制滚动方向
    private int maxScroll;  // 背景最大滚动距离
    private boolean previewDone = false;  // 是否完成了背景滚动的预览
    private boolean sunButtonAdded = false;  // 是否已经添加了太阳按钮

    private GreenfootImage background;  // 背景图片

    private PlantButton[] plantButtons;  // 存储植物选择按钮
    private String selectedPlant;  // 当前选择的植物类型

    private static final int GRID_WIDTH = 87;  // 每个草坪小区域的宽度
    private static final int GRID_HEIGHT = 120;  // 每个草坪小区域的高度

    // 草坪区域的坐标和尺寸
    private int lawnX = 280;  // 草坪左上角的 X 坐标
    private int lawnY = 90;   // 草坪左上角的 Y 坐标
    private int lawnWidth = 825;  // 草坪区域的宽度
    private int lawnHeight = 565;  // 草坪区域的高度

    private SunButton sunButton;  // 太阳按钮

    private int zombieSpawnRate = 300;  // 每1000帧生成一个僵尸
    private int timeSinceLastZombie = 0; // 记录上一个生成僵尸的时间
    private static final int NUM_ROWS = 5;  // 草坪的行数
    private PlantButton selectedPlantButton = null;  // 当前选中的植物按钮
    
    private int timeSinceLastSun = 0;  // 计时器，记录上次生成太阳的时间
    private int sunGenerationInterval = 180;  // 太阳生成的时间间隔（每500帧生成一个太阳）

    private Shovel shovel;
    private GreenfootSound backgroundMusic;
    private GreenfootSound finalWaveSound;
    
    private StartImage startImage;  // 游戏开始的提示文字
    private GrowImage growImage;
    private PlantImage plantImage;
    private int delayTime = 60;  // 每张文字图片显示1s
    private int currentTime = 0;
    
    
    public GameWorld() {
        super(1200, 700, 1, false);
        
        startImage = new StartImage();
        growImage = new GrowImage();
        plantImage = new PlantImage();
        addObject(startImage, getWidth() / 2, getHeight() / 2);
        
        // 初始化背景音乐
        backgroundMusic = new GreenfootSound("Faster.mp3");
        finalWaveSound = new GreenfootSound("finalwave.mp3");
        backgroundMusic.playLoop();  // 循环播放

        background = new GreenfootImage("lawn.png");
        background.scale(getWidth() + 340, getHeight());
        setBackground(background);

        maxScroll = background.getWidth() - getWidth();  // 计算背景的最大滚动距离

        plantButtons = new PlantButton[4];  // 假设有1个植物按钮
        selectedPlant = null;  // 默认没有选择植物
        addLawnMowers();  // 调用方法来添加推车

        // 立即检查植物按钮的状态
        checkPlantButtonAvailability();  // 更新植物按钮状态
        
    }

    
    private void checkPlantButtonAvailability() {
        List<PlantButton> plantButtons = getObjects(PlantButton.class);
        for (PlantButton button : plantButtons) {
            boolean canPlant = sunButton.getSunCount() >= button.getPlantCost();
            button.updateButtonImage(canPlant);  // 更新按钮图像
        }
    }

    
    private void addLawnMowers() {
        for (int i = 0; i < NUM_ROWS; i++) {
            LawnMower lawnMower = new LawnMower();  // 创建推车对象
            int yPosition = lawnY + i * GRID_HEIGHT + GRID_HEIGHT / 2;  // 计算纵坐标
            addObject(lawnMower, lawnX-50, yPosition);  // 将推车对象添加到世界
        }
     
    }
    
    private void slidePreview() {
        if (scrollingRight) {
            scrollX += scrollSpeed;
            if (scrollX >= maxScroll) {
                scrollingRight = false;  // 到达最大滚动位置
            }
        } else {
            scrollX -= scrollSpeed;
            if (scrollX <= 0) {
                previewDone = true;  // 背景滚动完成
                scrollX = 0;
            }
        }
        scrollBackground();  // 刷新背景
    }
    
    public void startMessage(){
        if (startImage != null && currentTime < 60) {
            currentTime++;
        } else if (startImage != null) {
            // 让文字延迟一会再消失
            removeObject(startImage);
            startImage = null;
            addObject(growImage, getWidth() / 2, getHeight() / 2);
        }

        if (growImage != null && currentTime < 120) {
            currentTime++; 
        } else if (growImage != null) {
            removeObject(growImage);
            growImage = null;  // 清除文字
            addObject(plantImage, getWidth() / 2, getHeight() / 2);
        }

        if (plantImage != null && currentTime < 180) {
            currentTime++; 
        } else if (plantImage != null) {
            removeObject(plantImage);
            plantImage = null;
        }
    }
    
    public void act() {
        startMessage();
        
        if (!previewDone) {
            slidePreview();  // 滚动背景
        }

        // 一旦预览完成，显示按钮
        if (previewDone && plantButtons[0] == null) {
            addPlantButtons();  // 只有滚动完之后才添加按钮
            shovel = new Shovel();  // 初始化铲子
            addObject(shovel, 430, 46);  // 添加铲子到世界中，可以根据需要调整位置}
        }

        checkButtonClicks();  // 检查按钮点击事件
        placePlant();  // 放置植物

        // 只在背景停止滚动后添加太阳按钮
        if (previewDone && !sunButtonAdded) {
            sunButton = new SunButton();  // Initialize only once
            addObject(sunButton, 320, 46);  // Add to the world
            sunButtonAdded = true;
        }

        // 控制僵尸的生成
        timeSinceLastZombie++;
        if (timeSinceLastZombie >= zombieSpawnRate) {
            generateZombie();  // 生成僵尸
            timeSinceLastZombie = 0;  // 重置计时器
        }
        
         timeSinceLastSun++;  // 增加计时器
        if (timeSinceLastSun >= sunGenerationInterval) {
            generateSun();  // 生成太阳
            timeSinceLastSun = 0;  // 重置计时器
        }

      // 更新场景滚动
        slidePreview();
        
        // 只有在背景滚动时才更新推车位置
        if (scrollingRight || scrollX > 0) {
            updateLawnMowersPosition();
        }
        // 更新场景背景
        scrollBackground();
        checkGameOver();
    }

    // 生成僵尸
    private void generateZombie() {
        // 随机选择一个行数，生成僵尸的纵坐标（5行）
        int row = Greenfoot.getRandomNumber(5);  // 假设草坪有5行
        int zombieX = lawnX + lawnWidth;  // 僵尸从草坪右边生成
        int zombieY = lawnY + row * GRID_HEIGHT + GRID_HEIGHT / 2 ;  // 根据行计算纵坐标

        // 随机生成僵尸类型
        Zombie zombie;
        int zombieType = Greenfoot.getRandomNumber(3);  // 3种类型的僵尸
        switch (zombieType) {
            case 0:
                zombie = new NormalZombie();  // 创建普通僵尸
                break;
            case 1:
                zombie = new BucketheadZombie();  // 创建桶头僵尸
                break;
            case 2:
                zombie = new NewspaperZombie();  // 创建报纸僵尸
                break;
            default:
                zombie = new NormalZombie();  // 默认创建普通僵尸
        }

        // 将生成的僵尸添加到世界中
        addObject(zombie, zombieX, zombieY);
    }

  
    private void scrollBackground() {
        GreenfootImage tempBackground = new GreenfootImage(background);
        tempBackground.drawImage(background, -scrollX, 0);
        setBackground(tempBackground);
    }
    
 private void updateLawnMowersPosition() {
    List<LawnMower> lawnMowers = getObjects(LawnMower.class);  // 获取所有推车对象
    // 如果没有推车对象，则直接返回
    if (lawnMowers.isEmpty()) {
        return;
    }

    // 更新每个推车的位置
    for (int i = 0; i < lawnMowers.size(); i++) {
        LawnMower lawnMower = lawnMowers.get(i);  // 获取每个推车对象
        int xPosition = lawnX-50-scrollX;  // 计算推车的横坐标
        int yPosition = lawnY + i * GRID_HEIGHT + GRID_HEIGHT / 2;  // 推车的纵坐标
        lawnMower.setLocation(xPosition, yPosition);  // 更新推车位置
      
    }
}

private void checkButtonClicks() {
        for (int i = 0; i < plantButtons.length; i++) {
            PlantButton button = plantButtons[i];
            if (Greenfoot.mouseClicked(button)) {
                selectedPlant = button.getPlantType();  // 更新当前选择的植物
                button.addYellowBorder();
            }
        }
    }

    // 获取当前选择的植物按钮
    public PlantButton getSelectedButton() {
        for (PlantButton button : plantButtons) {
            if (button.getPlantType().equals(selectedPlant)) {
                return button;  // 返回当前选中的植物按钮
            }
        }
        return null;  // 如果没有选中任何按钮，返回 null
    }


    // 放置植物
    private void placePlant() {
        if (Greenfoot.mouseClicked(this) && selectedPlant != null) {
            // 获取鼠标点击的位置
            int x = Greenfoot.getMouseInfo().getX();
            int y = Greenfoot.getMouseInfo().getY();
    
            // 判断点击是否在草坪区域内（例如，点击的x, y应该落在草坪的网格区域内）
            if (isInLawnArea(x, y)) {
                // 计算草坪小区域的中心位置
                int centerX = (x - lawnX) / GRID_WIDTH * GRID_WIDTH + GRID_WIDTH / 2 + lawnX;
                int centerY = (y - lawnY) / GRID_HEIGHT * GRID_HEIGHT + GRID_HEIGHT / 2 + lawnY - 6;

                // 获取SunButton实例，检查太阳值是否足够
                SunButton sunButton = getSunButton();  // 获取 SunButton 实例
                PlantButton selectedButton = getSelectedButton();
                int plantCost = selectedButton.getPlantCost();
                
                if (sunButton.getSunCount() >= plantCost) {
                    if (selectedPlant.equals("SunFlower") && sunButton.getSunCount() >= 50) {
                    addObject(new Sunflower(), centerX, centerY);  // 放置太阳花
                    sunButton.decrementSun(50);  // 扣除50个太阳
                    } else if (selectedPlant.equals("Peashooter") && sunButton.getSunCount() >= 100) {
                        addObject(new Peashooter(), centerX, centerY);  // 放置豌豆射手
                        sunButton.decrementSun(100);  // 扣除100个太阳
                    }else if (selectedPlant.equals("TallWallNut") && sunButton.getSunCount() >= 50) {
                        addObject(new TallWallNut(), centerX, centerY);
                        sunButton.decrementSun(50);
                }else if (selectedPlant.equals("CherryBomb") && sunButton.getSunCount() >= 100) {
                        addObject(new CherryBomb(), centerX, centerY); 
                        sunButton.decrementSun(100); 
                }
                }
               selectedPlant = null; 
            }
        }
    }


    // 判断点击位置是否在草坪区域内
    private boolean isInLawnArea(int x, int y) {
        // 检查鼠标点击位置是否在草坪区域内
        return x >= lawnX && x <= lawnX + lawnWidth && y >= lawnY && y <= lawnY + lawnHeight;
    }

    public void addPlantButtons() {
        plantButtons[0] = new PlantButton("SunFlower");
        plantButtons[1] = new PlantButton("Peashooter");
        plantButtons[2] = new PlantButton("TallWallNut");
        plantButtons[3] = new PlantButton("CherryBomb");
        
        int offsetX = 106; 
        int buttonVerticalSpacing = 90; 
    
        for (int i = 0; i < plantButtons.length; i++) {
            addObject(plantButtons[i], offsetX, 100 + i * buttonVerticalSpacing); 
        }
    }


    // 获取 SunButton 实例
    public SunButton getSunButton() {
        return sunButton;  // 返回 SunButton 实例
    }
    
    private void generateSun() {
        int randomX = Greenfoot.getRandomNumber(361) + 100;
        Sun sun = new Sun(randomX, -10);  // 假设初始位置为(100, 100)，你可以根据需要调整
        addObject(sun, randomX, -10);  // 将太阳对象添加到世界中
        sun.startMoving(); 
    }
    
    // 游戏结束的条件，可以根据需要调整
    private void checkGameOver() {
        List<Zombie> zombies = getObjects(Zombie.class);  // 获取所有僵尸对象
        for (Zombie zombie : zombies) {
            if (zombie.getX() <= lawnX-80) {  // 如果僵尸到达草坪的左边界
                endGame();  // 结束游戏
                return;  // 游戏结束后不再检查
            }
        }
    }

    // 结束游戏的方法
      private void endGame() {     
          Greenfoot.stop();  // 停止游戏   
          playFinalWaveSound();
          showGameOverMessage();  // 显示游戏结束的提示 
        }
        
        // 播放游戏结束音效
    private void playFinalWaveSound() {
        if (backgroundMusic.isPlaying()) {
            backgroundMusic.stop(); // 停止背景音乐
        }
        finalWaveSound.play(); // 播放游戏结束音效
    }

    // 显示游戏结束的消息（显示图片）
    private void showGameOverMessage() {
        GameOverMessage message = new GameOverMessage();  // 加载游戏结束图片
        int x = getWidth() / 2;  // 屏幕中心的横坐标
        int y = getHeight() / 2;  // 屏幕中心的纵坐标
        addObject(message, x, y);  // 将图片对象添加到世界中
    }


}