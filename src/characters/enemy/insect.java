package characters.enemy;

import characters.character;
import utils.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import static characters.myJPanel.marioJPanel.DIR_RIGHT;

public class insect extends character {
    List<BufferedImage> InImg = new LinkedList<>();
    BufferedImage img;
    public boolean isLive;

    public insect() {
        InImg.add(ImageUtil.getImage("/img/insect/insect1.png"));
        InImg.add(ImageUtil.getImage("/img/insect/insect2.png"));
        InImg.add(ImageUtil.getImage("/img/insect/insect3.png"));
        img = InImg.get(1);
        direct = DIR_RIGHT;
        isLive = true;
        Mx = 1000;
        y = 650;
        w = img.getWidth();
        h = img.getHeight();
    }

    int index = 0;

    public void changeImage() {
        index++;
        if (index == InImg.size() - 1) {
            index = 0;
        }
        img = InImg.get(index);
    }

    public void changeDeadImage() {
        img = InImg.get(2);
    }

    public void paintObject(Graphics g) {
        g.drawImage(img, x, y, null);
    }
}
