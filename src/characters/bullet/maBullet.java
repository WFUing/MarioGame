package characters.bullet;

import characters.character;
import utils.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static characters.myJPanel.marioJPanel.DIR_LEFT;
import static characters.myJPanel.marioJPanel.DIR_RIGHT;

public class maBullet extends character {
    public boolean isLive;
    private BufferedImage Img;
    private final BufferedImage rBuImg;
    private final BufferedImage lBuImg;
    ImageIcon boom;
    public JLabel boomJL;

    public maBullet() {
        rBuImg = ImageUtil.getImage("/img/bullet/bullet3.png");
        lBuImg = ImageUtil.getImage("/img/bullet/bullet2.png");
        boom = new ImageIcon("img/bullet/boom.gif");
        boomJL = new JLabel(boom);
        Img = rBuImg;
        isLive = true;
        direct = DIR_RIGHT;
        assert Img != null;
        w = Img.getWidth();
        h = Img.getHeight();
    }

    public void changeImg() {
        if (direct == DIR_LEFT) {
            Img = lBuImg;
        } else if (direct == DIR_RIGHT) {
            Img = rBuImg;
        }
    }

    public void paintObject(Graphics g) {
        g.drawImage(Img, x, y, null);
    }
}
