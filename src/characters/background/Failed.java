package characters.background;


import java.awt.*;

public class Failed {
    public void paintObject(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, 1200, 750);
        Font font = new Font("微软雅黑", Font.BOLD, 100);
        g.setColor(Color.RED);
        g.setFont(font);
        g.drawString("you lose！！", 350, 200);
        font = new Font("微软雅黑", Font.BOLD, 30);
        g.setColor(Color.RED);
        g.setFont(font);
        g.drawString("制作人：蒋一杰", 500, 500);
    }
}
