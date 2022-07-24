package com.kingyu.flappybird.component;

import com.kingyu.flappybird.util.GameUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.kingyu.flappybird.common.Constant.AGAIN_IMG_PATH;
import static com.kingyu.flappybird.common.Constant.FRAME_HEIGHT;
import static com.kingyu.flappybird.common.Constant.FRAME_WIDTH;
import static com.kingyu.flappybird.common.Constant.OVER_IMG_PATH;
import static com.kingyu.flappybird.common.Constant.SCORE_FONT;
import static com.kingyu.flappybird.common.Constant.SCORE_IMG_PATH;

/**
 * 游戏结束界面
 */
public class GameOverAnimation {

    private final BufferedImage scoreImg; // 计分牌
    private final BufferedImage overImg; // 结束标志
    private final BufferedImage againImg; // 继续标志

    public GameOverAnimation(){
        overImg = GameUtil.loadBufferedImage(OVER_IMG_PATH);
        scoreImg = GameUtil.loadBufferedImage(SCORE_IMG_PATH);
        againImg = GameUtil.loadBufferedImage(AGAIN_IMG_PATH);
    }

    private static final int SCORE_LOCATE = 5; // 计分牌位置补偿参数
    private int flash = 0; // 图片闪烁参数

    public void draw(Graphics graphics, Bird bird) {
        int x = (FRAME_WIDTH - overImg.getWidth()) / 2;
        int y = FRAME_HEIGHT / 4;
        graphics.drawImage(overImg, x, y, null);

        // 绘制计分牌
        x = (FRAME_WIDTH - scoreImg.getWidth()) / 2;
        y = FRAME_HEIGHT / 3;
        graphics.drawImage(scoreImg, x, y, null);

        // 绘制本局的分数
        graphics.setColor(Color.white);
        graphics.setFont(SCORE_FONT);
        x = ((FRAME_WIDTH - scoreImg.getWidth() / 2) / 2) + SCORE_LOCATE;// 位置补偿
        y += scoreImg.getHeight() / 2;
        String str = Long.toString(bird.getCurrentScore());
        x -= GameUtil.getStringWidth(SCORE_FONT, str) / 2;
        y += GameUtil.getStringHeight(SCORE_FONT, str);
        graphics.drawString(str, x, y);

        // 绘制最高分数
        if (bird.getBestScore() > 0) {
            str = Long.toString(bird.getBestScore());
            x = ((FRAME_WIDTH + scoreImg.getWidth() / 2) / 2) - SCORE_LOCATE;// 位置补偿
            x -= GameUtil.getStringWidth(SCORE_FONT, str) / 2;
            graphics.drawString(str, x, y);
        }

        // 绘制继续游戏，图像闪烁
        final int COUNT = 30; // 闪烁周期
        if (flash++ > COUNT)
            GameUtil.drawImage(againImg,(FRAME_WIDTH - againImg.getWidth()) / 2, FRAME_HEIGHT / 5 * 3, graphics);
        if (flash == COUNT * 2) // 重置闪烁参数
            flash = 0;
    }
}
