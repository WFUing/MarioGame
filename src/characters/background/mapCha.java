package characters.background;

import utils.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class mapCha extends AMap{

    //大背景图
    BufferedImage background;

    //读入图片
    Reader reader;

    //存储的元素
    String[] strMap;

    //背景元素
    BufferedImage[] titleList = new BufferedImage[9];

    public mapCha() {
        background = ImageUtil.getImage("/img/map/background.png");
        titleList[0] = ImageUtil.getImage("/img/map/tile_A.png");
        titleList[1] = ImageUtil.getImage("/img/map/tile_B.png");
        titleList[2] = ImageUtil.getImage("/img/map/tile_C.png");
        titleList[3] = ImageUtil.getImage("/img/map/tile_D.png");
        titleList[4] = ImageUtil.getImage("/img/map/tile_E.png");
        titleList[5] = ImageUtil.getImage("/img/map/tile_F.png");
        titleList[6] = ImageUtil.getImage("/img/map/tile_G.png");
        titleList[7] = ImageUtil.getImage("/img/map/tile_H.png");
        titleList[8] = ImageUtil.getImage("/img/map/tile_I.png");
        // 读入map1中的字符串，并初始化为int数组
        try {
            String path = mapCha.class.getResource("/img/maps/map1.txt").toURI().getPath();
//            String path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader()
//                    .getResource("img/maps/map1.txt")).getPath();
            reader = new FileReader(path);
            BufferedReader buff = new BufferedReader(reader);
            int index = 0;
            strMap = new String[16];
            map = new int[16][24];
            while (buff.ready()) {
                String line = buff.readLine();
                if (line != null) {
                    for (int i = 0; i < line.length(); i++) {
                        map[index][i] = line.charAt(i) - 'A' + 1;
                        if (map[index][i] < 0) {
                            map[index][i] = 0;
                        }
                    }
                    strMap[index++] = line;
                }
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        ScheduledExecutorService service1 = Executors.newSingleThreadScheduledExecutor();
        service1.scheduleAtFixedRate(IRun, 300, 300, TimeUnit.MILLISECONDS);
    }

    Runnable IRun = () -> {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 24; j++) {
                if (map[i][j] == 9) {
                    int left = ((j - 1) % 24 < 0) ? (j - 1) % 24 + 24 : (j - 1) % 24;
                    if (i < 5) {
                        map[i][left] = map[i][j];
                        map[i][j] = map[i][(j + 1) % 24];
                    } else {
                        map[i][((j - 2) % 24 < 0) ? (j - 2) % 24 + 24 : (j - 2) % 24] = map[i][j];
                        map[i][j] = map[i][(j + 2) % 24];
                        map[i][left] = map[i][(j + 1) % 24];
                        map[i][(j + 1) % 24] = map[i][(j + 3) % 24];
                    }
                }
            }
        }
    };

    // 画背景
    public void paintObject(Graphics g) {
        g.drawImage(background, 0, 0, null);
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 24; j++) {
                if (map[i][j] != 0) {
                    g.drawImage(titleList[map[i][j] - 1], j * 50, i * 50, null);
                }
            }
        }
    }
}
