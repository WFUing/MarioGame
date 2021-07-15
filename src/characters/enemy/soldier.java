package characters.enemy;

import characters.character;
import utils.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static characters.myJPanel.marioJPanel.DIR_LEFT;
import static characters.myJPanel.marioJPanel.DIR_RIGHT;


public class soldier extends character {

    // 血量
    public int xueLiang;
    // soldier是否存活
    public boolean isLive;
    // soldier的地面位置
    public int groundSY;
    // 是否soldier向上
    public boolean isUp;
    // 是否soldier向下
    public boolean isDown;
    // 当前soldier的图片
    private BufferedImage img;
    // 向左/右  走/跳的图片
    List<BufferedImage> sL = new ArrayList<>();
    List<BufferedImage> sR = new ArrayList<>();
    List<BufferedImage> sJL = new ArrayList<>();
    List<BufferedImage> sJR = new ArrayList<>();

    public soldier() {
        for (int i = 0; i < 12; i++) {
            sR.add(ImageUtil.getImage("/img/soldier/soldier/PR/player" + i + ".png"));
            sL.add(ImageUtil.getImage("/img/soldier/soldier/PL/player" + i + ".png"));
        }
        for (int i = 0; i < 8; i++) {
            sJR.add(ImageUtil.getImage("/img/soldier/soldier/PR/jump" + i + ".png"));
            sJL.add(ImageUtil.getImage("/img/soldier/soldier/PL/jump" + i + ".png"));
        }
        sL.add(ImageUtil.getImage("/img/soldier/soldier/PL/up.png"));//12
        sL.add(ImageUtil.getImage("/img/soldier/soldier/PL/down.png"));//13
        sR.add(ImageUtil.getImage("/img/soldier/soldier/PR/up.png"));//12
        sR.add(ImageUtil.getImage("/img/soldier/soldier/PR/down.png"));//13
        img = sR.get(0);
        isUp = false;
        isDown = false;
        Mx = 2000;
        y = 600;
        direct = DIR_RIGHT;
        w = img.getWidth();
        h = img.getHeight();
        isLive = true;
        xueLiang = 50;
    }

    // 士兵跑动的动画
    int index = 1;

    public void changeImage() {
        index++;
        if (index == 12) {
            index = 0;
        }
        if (direct == DIR_LEFT) {
            img = sL.get(index);
        } else if (direct == DIR_RIGHT) {
            img = sR.get(index);
        }
        w = img.getWidth();
        h = img.getHeight();
    }

    // 士兵跳跃的动画
    int jIndex = 0;

    public void SoJumImg() {
        jIndex++;
        if (jIndex == sJL.size()) {
            jIndex = 0;
        }
        if (direct == DIR_LEFT) {
            img = sJL.get(jIndex);
        } else if (direct == DIR_RIGHT) {
            img = sJR.get(jIndex);
        }
        w = img.getWidth();
        h = img.getHeight();
    }

    // 士兵改为站立图片
    public void SoStand() {
        if (direct == DIR_LEFT) {
            img = sL.get(0);
        } else if (direct == DIR_RIGHT) {
            img = sR.get(0);
        }
        w = img.getWidth();
        h = img.getHeight();
    }

    // 士兵改为向上图片
    public void SoUpImg() {
        if (direct == DIR_LEFT) {
            img = sL.get(12);
        } else if (direct == DIR_RIGHT) {
            img = sR.get(12);
        }
        w = img.getWidth();
        h = img.getHeight();
    }

    // 士兵改为向下图片
    public void SoDownImg() {
        if (direct == DIR_LEFT) {
            img = sL.get(13);
        } else if (direct == DIR_RIGHT) {
            img = sR.get(13);
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
