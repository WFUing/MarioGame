package characters;

import characters.myJPanel.marioJPanel;
import utils.musicThread;

import javax.swing.*;
import java.util.Objects;

public class startJFrame extends JFrame {
    public startJFrame() {
        marioJPanel marioJPanel1 = new marioJPanel();
        this.setTitle("start");
        this.setSize(1214, 787);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(marioJPanel1);
        this.setVisible(true);
        String path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader()
                .getResource("music/bgm.wav")).getPath();
        Runnable r = new musicThread(path);
        Thread t = new Thread(r);
        t.start();
    }
}
