package characters.bullet;

import characters.character;
import utils.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

import static characters.myJPanel.marioJPanel.DIR_RIGHT;

public class soBullet extends character {
    public boolean isLive;
    private BufferedImage Img;
    public soBullet(){
        Img = ImageUtil.getImage("/img/bullet/bullet.png");
        isLive = true;
        direct = DIR_RIGHT;
        assert Img != null;

        w = Img.getWidth();
        h = Img.getHeight();
    }

    public void paintObject(Graphics g) {
        g.drawImage(Img, x, y, null);
    }
}
