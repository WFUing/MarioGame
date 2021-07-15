/*
 * 目的：学会使用intersects方法进行游戏碰撞测试
 * 方法：两个移动的正方形间的碰撞测试
 */
package JFrameTest;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.Graphics;

public class intersectsTest extends JFrame {
    MyPanel mp;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new intersectsTest();
    }

    public intersectsTest() {
        mp = new MyPanel();
        this.add(mp);
        this.setTitle("碰撞测试");
        this.setSize(400, 400);
        this.setLocation(100, 100);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}

class MyPanel extends JPanel implements Runnable {
    RectangleA A;
    RectangleA B;

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 400, 400);
        g.setColor(Color.cyan);
        this.drawRactangle(A.x, A.y, g);
        this.drawRactangle(B.x, B.y, g);
        A.setBump(this.isHitA());
        B.setBump(this.isHitB());
        this.repaint();
    }

    public MyPanel() {
        A = new RectangleA(30, 30);
        A.setSpeed(15);
        Thread tA = new Thread(A);
        tA.start();
        B = new RectangleA(190, 190);
        B.setSpeed(10);
        Thread tB = new Thread(B);
        tB.start();
    }

    public void drawRactangle(int x, int y, Graphics g) {
        g.setColor(Color.cyan);
        g.fill3DRect(x, y, 100, 100, true);
    }

    public boolean isHitA() {
        Rectangle rA = new Rectangle(A.getX(), A.getY(), 100, 100);
        Rectangle rB = new Rectangle(B.getX(), B.getY(), 100, 100);
        return !rA.intersects(rB);
    }

    //碰撞函数
    public boolean isHitB() {
        Rectangle rA = new Rectangle(A.getX(), A.getY(), 105, 105);
        Rectangle rB = new Rectangle(B.getX(), B.getY(), 105, 105);
        //System.out.println("sdas");
        return !rB.intersects(rA);
    }

    @Override
    public void run() {
        // TODO Auto-generated method stu;
        try {
            Thread.sleep(50);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //重绘
//        this.repaint();
    }
}

class RectangleX {
    boolean bump = true;

    public void setBump(boolean bump) {
        this.bump = bump;
    }

    int x, y;
    int direct = 1;
    int speed;

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    RectangleX(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class RectangleA extends RectangleX implements Runnable {
    RectangleA(int x, int y) {
        super(x, y);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (true) {
            //System.out.println("saa");
            this.direct = (int) (Math.random() * 4);
            switch (this.direct) {
                case 0:
                    for (int i = 0; i < 15; i++) {
                        if (this.y > 0 && this.bump) {
                            y -= speed;
                        } else if (this.y > 0 && !this.bump) {
                            y += speed;
                            this.setBump(true);
                            break;
                        }
                        try {
                            Thread.sleep(50);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 1:
                    for (int i = 0; i < 15; i++) {
                        if (this.x < 275 && this.bump) {
                            x += speed;
                        } else if (this.x < 275) {
                            x -= speed;
                            i = 15;
                            this.setBump(true);
                            break;
                        }
                        try {
                            Thread.sleep(50);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 2:
                    for (int i = 0; i < 15; i++) {
                        if (this.y < 253 && this.bump) {
                            y += speed;
                        } else if (this.y < 253) {
                            y -= speed;
                            i = 15;
                            this.setBump(true);
                            break;
                        }
                        try {
                            Thread.sleep(50);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 3:
                    for (int i = 0; i < 15; i++) {
                        if (this.x > 0 && this.bump) {
                            x -= speed;
                        } else if (this.x > 0) {
                            x += speed;
                            i = 15;
                            this.setBump(true);
                            break;
                        }
                        try {
                            Thread.sleep(50);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
            this.direct = (int) (Math.random() * 4);
        }
    }
}
