package com.kingyu.flappybird.component;

import com.kingyu.flappybird.util.GameUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.kingyu.flappybird.common.Constant.FRAME_HEIGHT;
import static com.kingyu.flappybird.common.Constant.FRAME_WIDTH;
import static com.kingyu.flappybird.common.Constant.GAME_SPEED;
import static com.kingyu.flappybird.common.Constant.GROUND_HEIGHT;
import static com.kingyu.flappybird.common.Constant.PIPE_IMG_PATH;
import static com.kingyu.flappybird.common.Constant.TOP_PIPE_LENGTHENING;

/**
 * 水管类，实现水管的绘制与运动逻辑
 */
public class Pipe {

    static BufferedImage[] images; // 水管的图片，static保证图片只加载一次

    // 静态代码块，类加载的时候，初始化图片
    static {
        final int PIPE_IMAGE_COUNT = 3;
        images = new BufferedImage[PIPE_IMAGE_COUNT];
        for (int i = 0; i < PIPE_IMAGE_COUNT; i++) {
            images[i] = GameUtil.loadBufferedImage(PIPE_IMG_PATH[i]);
        }
    }

    // 所有水管的宽高
    public static final int PIPE_WIDTH = images[0].getWidth();
    public static final int PIPE_HEIGHT = images[0].getHeight();
    public static final int PIPE_HEAD_WIDTH = images[1].getWidth();
    public static final int PIPE_HEAD_HEIGHT = images[1].getHeight();

    int pipe_x, pipe_y; // 水管的坐标，相对于元素层
    int pipe_width, pipe_height; // 水管的宽，高

    boolean pipe_visible; // 水管可见状态，true为可见，false表示可归还至对象池
    // 水管的类型
    int pipe_type;
    public static final int TYPE_TOP_NORMAL = 0;
    public static final int TYPE_TOP_HARD = 1;
    public static final int TYPE_BOTTOM_NORMAL = 2;
    public static final int TYPE_BOTTOM_HARD = 3;
    public static final int TYPE_HOVER_NORMAL = 4;
    public static final int TYPE_HOVER_HARD = 5;

    // 水管的速度
    int pipe_speed;

    Rectangle pipeRect; // 水管的碰撞矩形

    // 构造器
    public Pipe() {
        this.pipe_speed = GAME_SPEED;
        this.pipe_width = PIPE_WIDTH;

        pipeRect = new Rectangle();
        pipeRect.width = PIPE_WIDTH;
    }

    /**
     * 设置水管参数
     *
     * @param x:x坐标
     * @param y：y坐标
     * @param height：水管高度
     * @param type：水管类型
     * @param visible：水管可见性
     */
    public void setAttribute(int x, int y, int height, int type, boolean visible) {
        this.pipe_x = x;
        this.pipe_y = y;
        this.pipe_height = height;
        this.pipe_type = type;
        this.pipe_visible = visible;
        setRectangle(this.pipe_x, this.pipe_y, this.pipe_height);
    }

    /**
     * 设置碰撞矩形参数
     */
    public void setRectangle(int x, int y, int height) {
        pipeRect.x = x;
        pipeRect.y = y;
        pipeRect.height = height;
    }

    // 判断水管是否位于窗口
    public boolean isPipe_visible() {
        return pipe_visible;
    }

    // 绘制方法
    public void draw(Graphics graphics, Bird bird) {
        switch (pipe_type) {
            case TYPE_TOP_NORMAL:
                drawTopNormal(graphics);
                break;
            case TYPE_BOTTOM_NORMAL:
                drawBottomNormal(graphics);
                break;
            case TYPE_HOVER_NORMAL:
                drawHoverNormal(graphics);
                break;
        }
//      //绘制碰撞矩形
//      graphics.setColor(Color.black);
//      graphics.drawRect((int) pipeRect.getPipe_x(), (int) pipeRect.getY(), (int) pipeRect.getWidth(), (int) pipeRect.getHeight());

        //鸟死后水管停止移动
        if (bird.isDead()) {
            return;
        }
        movement();
    }

    // 绘制从上往下的普通水管
    private void drawTopNormal(Graphics graphics) {
        // 拼接的个数
        int count = (pipe_height - PIPE_HEAD_HEIGHT) / PIPE_HEIGHT + 1; // 取整+1
        // 绘制水管的主体
        for (int i = 0; i < count; i++) {
            graphics.drawImage(images[0], pipe_x, pipe_y + i * PIPE_HEIGHT, null);
        }
        // 绘制水管的顶部
        graphics.drawImage(images[1], pipe_x - ((PIPE_HEAD_WIDTH - pipe_width) / 2),
                pipe_height - TOP_PIPE_LENGTHENING - PIPE_HEAD_HEIGHT, null); // 水管头部与水管主体的宽度不同，x坐标需要处理
    }

    // 绘制从下往上的普通水管
    private void drawBottomNormal(Graphics graphics) {
        // 拼接的个数
        int count = (pipe_height - PIPE_HEAD_HEIGHT - GROUND_HEIGHT) / PIPE_HEIGHT + 1;
        // 绘制水管的主体
        for (int i = 0; i < count; i++) {
            graphics.drawImage(images[0], pipe_x, FRAME_HEIGHT - PIPE_HEIGHT - GROUND_HEIGHT - i * PIPE_HEIGHT,
                    null);
        }
        // 绘制水管的顶部
        graphics.drawImage(images[2], pipe_x - ((PIPE_HEAD_WIDTH - pipe_width) / 2), FRAME_HEIGHT - pipe_height, null);
    }

    // 绘制悬浮的普通水管
    private void drawHoverNormal(Graphics graphics) {
        // 拼接的个数
        int count = (pipe_height - 2 * PIPE_HEAD_HEIGHT) / PIPE_HEIGHT + 1;
        // 绘制水管的上顶部
        graphics.drawImage(images[2], pipe_x - ((PIPE_HEAD_WIDTH - pipe_width) / 2), pipe_y, null);
        // 绘制水管的主体
        for (int i = 0; i < count; i++) {
            graphics.drawImage(images[0], pipe_x, pipe_y + i * PIPE_HEIGHT + PIPE_HEAD_HEIGHT, null);
        }
        // 绘制水管的下底部
        int y = this.pipe_y + pipe_height - PIPE_HEAD_HEIGHT;
        graphics.drawImage(images[1], pipe_x - ((PIPE_HEAD_WIDTH - pipe_width) / 2), y, null);
    }

    /**
     * 普通水管的运动逻辑
     */
    private void movement() {
        pipe_x -= pipe_speed;
        pipeRect.x -= pipe_speed;
        if (pipe_x < -1 * PIPE_HEAD_WIDTH) {// 水管完全离开了窗口
            pipe_visible = false;
        }
    }

    /**
     * 判断当前水管是否完全出现在窗口中
     *
     * @return 若完全出现则返回true，否则返回false
     */
    public boolean isInFrame() {
        return pipe_x + pipe_width < FRAME_WIDTH;
    }

    // 获取水管的x坐标
    public int getPipe_x() {
        return pipe_x;
    }

    // 获取水管的碰撞矩形
    public Rectangle getPipeRect() {
        return pipeRect;
    }

}
