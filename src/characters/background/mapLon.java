package characters.background;

import utils.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class mapLon extends AMap{

    //大背景图
    BufferedImage background;

    //读入图片
    Reader reader;

    //存储的元素
    String[] strMap;

    //背景元素
    BufferedImage[] titleList = new BufferedImage[9];

    //背景走过的路程
    public int x;

    public mapLon() {
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
            String path = mapCha.class.getResource("/img/maps/map.txt").toURI().getPath();
//            String path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader()
//                    .getResource("img/maps/map.txt")).getPath();
            reader = new FileReader(path);
            BufferedReader buff = new BufferedReader(reader);
            int index = 0;
            strMap = new String[15];
            map = new int[15][66];
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
/*            for (int[] in : map) {
                for (int inn : in) {
                    System.out.print(inn);
                }
                System.out.println();
            }*/
            System.out.println();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        ScheduledExecutorService service1 = Executors.newSingleThreadScheduledExecutor();
        service1.scheduleAtFixedRate(IRun, 300, 300, TimeUnit.MILLISECONDS);
    }

    Runnable IRun = () -> {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 66; j++) {
                if (map[i][j] == 9) {
                    int left = ((j - 1) % 66 < 0) ? (j - 1) % 66 + 66 : (j - 1) % 66;
                    if (i < 5) {
                        map[i][left] = map[i][j];
                        map[i][j] = map[i][(j + 1) % 66];
                    } else {
                        map[i][((j - 2) % 66 < 0) ? (j - 2) % 66 + 66 : (j - 2) % 66] = map[i][j];
                        map[i][j] = map[i][(j + 2) % 66];
                        map[i][left] = map[i][(j + 1) % 66];
                        map[i][(j + 1) % 66] = map[i][(j + 3) % 66];
                    }
                }
            }
        }
    };

    // 画背景
    public void paintObject(Graphics g) {
        g.drawImage(background, 0, 0, null);
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 66; j++) {
                if (map[i][j] != 0) {
                    g.drawImage(titleList[map[i][j] - 1],x+ j * 50, i * 50, null);
                }
            }
        }
    }
}
