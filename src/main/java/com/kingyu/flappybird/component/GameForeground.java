package com.kingyu.flappybird.component;

import com.kingyu.flappybird.util.GameUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static com.kingyu.flappybird.common.Constant.CLOUDS_IMG_PATH;
import static com.kingyu.flappybird.common.Constant.CLOUD_BORN_PERCENT;
import static com.kingyu.flappybird.common.Constant.CLOUD_IMAGE_COUNT;
import static com.kingyu.flappybird.common.Constant.FRAME_HEIGHT;
import static com.kingyu.flappybird.common.Constant.FRAME_WIDTH;
import static com.kingyu.flappybird.common.Constant.MAX_CLOUD_COUNT;
import static com.kingyu.flappybird.common.Constant.TOP_BAR_HEIGHT;

/**
 * 前景层，目前管理云朵的生成逻辑并绘制容器中的云朵
 */
public class GameForeground {

    private final List<Cloud> clouds; // 云朵的容器
    private final BufferedImage[] cloudImages; // 图片资源
    private long time; // 控制云的逻辑运算周期
    public static final int CLOUD_INTERVAL = 100; // 云朵刷新的逻辑运算的周期

    public GameForeground() {
        clouds = new ArrayList<>(); //云朵的容器
        // 读入图片资源
        cloudImages = new BufferedImage[CLOUD_IMAGE_COUNT];
        for (int i = 0; i < CLOUD_IMAGE_COUNT; i++) {
            cloudImages[i] = GameUtil.loadBufferedImage(CLOUDS_IMG_PATH[i]);
        }
        time = System.currentTimeMillis(); // 获取当前时间，用于控制云的逻辑运算周期
    }

    // 绘制方法
    public void draw(Graphics graphics, Bird bird) {
        cloudBornLogic();
        for (Cloud cloud : clouds) {
            cloud.draw(graphics, bird);
        }
    }

    // 云朵的控制
    private void cloudBornLogic() {
        // 100ms运算一次
        if (System.currentTimeMillis() - time > CLOUD_INTERVAL) {
            time = System.currentTimeMillis(); // 重置time
            // 如果屏幕的云朵的数量小于允许的最大数量，根据给定的概率随机添加云朵
            if (clouds.size() < MAX_CLOUD_COUNT) {
                try {
                    if (GameUtil.isInProbability(CLOUD_BORN_PERCENT, 100)) { // 根据给定的概率添加云朵
                        int index = GameUtil.getRandomNumber(0, CLOUD_IMAGE_COUNT); // 随机选取云朵图片
                        // y坐标随机在上1/3屏选取
                        int y = GameUtil.getRandomNumber(TOP_BAR_HEIGHT, FRAME_HEIGHT / 3);
                        // 向容器中添加云朵
                        Cloud cloud = new Cloud(cloudImages[index], FRAME_WIDTH, y);
                        clouds.add(cloud);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } // 添加云朵

            // 若云朵飞出屏幕则从容器中移除
            for (int i = 0; i < clouds.size(); i++) {
                // 遍历容器中的云朵
                Cloud tempCloud = clouds.get(i);
                if (tempCloud.isOutFrame()) {
                    clouds.remove(i);
                    i--;
                }
            }
        }
    }
}
