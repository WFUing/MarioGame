package characters.player;

import characters.character;
import utils.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static characters.myJPanel.marioJPanel.DIR_LEFT;
import static characters.myJPanel.marioJPanel.DIR_RIGHT;

public class mario extends character {
    // 单例模式
    private static mario _mario;

    public static mario GetMario() {
        if (_mario == null) {
            _mario = new mario();
        }
        return _mario;
    }

    // 当前mario的图片
     BufferedImage img;
    // 向左/右的图片
    List<BufferedImage> imgL = new ArrayList<>();
    List<BufferedImage> imgR = new ArrayList<>();

    // mario 的血量
    public int xueLiang;

    // mario脚下最近的地面
    public int groundMY;
    // mario是否跳跃
    public Boolean isMaJump;

    private mario() {
        for (int i = 1; i <= 5; i++) {
            imgR.add(ImageUtil.getImage("/img/mario/" + i + ".png"));
            imgL.add(ImageUtil.getImage("/img/mario/" + (i + 5) + ".png"));
        }
        img = imgR.get(0);
        h = img.getHeight();
        w = img.getWidth();
        Mx = x = 150;
        y = 100;
        speed = 10;
        xueLiang = 50;
    }

    int index = 0;
    public void changeImage() {
        index++;
        if (index == imgL.size()) {
            index = 0;
        }
        if (direct == DIR_LEFT) {
            img = imgL.get(index);
        } else if (direct == DIR_RIGHT) {
            img = imgR.get(index);
        }
    }

    // 改为站立图片
    public void maStand(){
        if (direct == DIR_LEFT) {
            img = imgL.get(0);
        } else if (direct == DIR_RIGHT) {
            img = imgR.get(0);
        }
        w = img.getWidth();
        h = img.getHeight();
    }

    public void paintObject(Graphics g) {
        g.drawImage(img, x, y, null);
        g.setColor(Color.RED);
        g.drawRect(x + 5, y - 50, 50, 20);
        g.fillRect(x + 5, y - 50, xueLiang, 20);
    }
}
