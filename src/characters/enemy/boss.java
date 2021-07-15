package characters.enemy;

import characters.character;
import utils.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class boss extends character {
    public int xueLiang;
    public boolean isLive;
    BufferedImage img;
    // 子弹的轨道
    public int danDao;

    public boss() {
        img = ImageUtil.getImage("/img/soldier/boss.png");
        speed = 20;
        Mx = x = 950;
        y = 50;
        isLive = true;
        xueLiang = 1000;
        assert img != null;
        w = img.getWidth();
        h = img.getHeight();
        ScheduledExecutorService service1 = Executors.newSingleThreadScheduledExecutor();
        service1.scheduleAtFixedRate(BossRun, 300, 300, TimeUnit.MILLISECONDS);
    }

    public int random(int lower, int upper) {
        return (int) (Math.floor(Math.random() * (upper - lower)) + lower);
    }

    Runnable BossRun = new Runnable() {
        @Override
        public void run() {
            while (isLive) {
                direct = random(1, 5);
                switch (direct) {
                    case 1 -> {
                        x -= 10;
                        Mx -= 10;
                        if (x <= 900) {
                            Mx = x = 900;
                        }
                    }
                    case 2 -> {
                        x += 10;
                        Mx += 10;
                        if (x >= 1000) {
                            Mx = x = 1000;
                        }
                    }
                    case 3 -> {
                        y -= 10;
                        if (y <= 50) {
                            y = 50;
                        }
                    }
                    case 4 -> {
                        y += 10;
                        if (y >= 100) {
                            y = 100;
                        }
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void paintObject(Graphics g) {
        g.drawImage(img, x, y, null);
        g.setColor(Color.RED);
        g.drawRect(100, 20, 1000, 20);
        g.fillRect(100, 20, xueLiang, 20);
    }
}
