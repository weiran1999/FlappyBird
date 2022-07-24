package com.kingyu.flappybird.component;

import com.kingyu.flappybird.util.GameUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.kingyu.flappybird.common.Constant.BG_COLOR;
import static com.kingyu.flappybird.common.Constant.BG_IMG_PATH;
import static com.kingyu.flappybird.common.Constant.FRAME_HEIGHT;
import static com.kingyu.flappybird.common.Constant.FRAME_WIDTH;
import static com.kingyu.flappybird.common.Constant.GAME_SPEED;

/**
 * 游戏背景类，实现游戏背景的绘制
 */
public class GameBackground {

	private static final BufferedImage BackgroundImg; // 背景图片
	private final int speed; // 背景层的速度
	private int layerX; // 背景层的坐标
	// 背景层高度
	public static final int GROUND_HEIGHT;

	static {
		BackgroundImg = GameUtil.loadBufferedImage(BG_IMG_PATH);
		assert BackgroundImg != null;
		GROUND_HEIGHT = BackgroundImg.getHeight() / 2;
	}

	// 在构造器中初始化
	public GameBackground() {
		this.speed = GAME_SPEED;
		this.layerX = 0;
	}

	// 绘制方法
	public void draw(Graphics graphics, Bird bird) {
		// 绘制背景色
		graphics.setColor(BG_COLOR);
		graphics.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);

		// 获得背景图片的尺寸
		assert BackgroundImg != null;
		int imgWidth = BackgroundImg.getWidth();
		int imgHeight = BackgroundImg.getHeight();

		int count = FRAME_WIDTH / imgWidth + 2; // 根据窗口宽度得到图片的绘制次数
		for (int i = 0; i < count; i++) {
			graphics.drawImage(BackgroundImg, imgWidth * i - layerX, FRAME_HEIGHT - imgHeight, null);
		}
		
		if (bird.isDead()) {
			// 小鸟死亡则不再绘制
			return;
		}
		movement();
	}

	// 背景层的运动逻辑
	private void movement() {
		layerX += speed;
		assert BackgroundImg != null;
		if (layerX > BackgroundImg.getWidth())
			layerX = 0;
	}
}
