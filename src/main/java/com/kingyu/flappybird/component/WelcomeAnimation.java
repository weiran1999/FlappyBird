package com.kingyu.flappybird.component;

import com.kingyu.flappybird.util.GameUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.kingyu.flappybird.common.Constant.FRAME_HEIGHT;
import static com.kingyu.flappybird.common.Constant.FRAME_WIDTH;
import static com.kingyu.flappybird.common.Constant.NOTICE_IMG_PATH;
import static com.kingyu.flappybird.common.Constant.TITLE_IMG_PATH;

/**
 * 游戏启动界面
 */
public class WelcomeAnimation {

	private final BufferedImage titleImg;
	private final BufferedImage noticeImg;

	private int flashCount = 0; // 图像闪烁参数

	public WelcomeAnimation() {
		titleImg = GameUtil.loadBufferedImage(TITLE_IMG_PATH);
		noticeImg = GameUtil.loadBufferedImage(NOTICE_IMG_PATH);
	}

	public void draw(Graphics graphics) {
		int x = (FRAME_WIDTH - titleImg.getWidth()) / 2;
		int y = FRAME_HEIGHT / 3;
		graphics.drawImage(titleImg, x, y, null);

		// 使notice的图像闪烁
		final int CYCLE = 30; // 闪烁周期
		if (flashCount++ > CYCLE)
			GameUtil.drawImage(noticeImg, (FRAME_WIDTH - noticeImg.getWidth()) / 2, FRAME_HEIGHT / 5 * 3, graphics);
		if (flashCount == CYCLE * 2)
			flashCount = 0;
	}

}
