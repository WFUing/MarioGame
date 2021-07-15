package characters.myJPanel;

import characters.background.*;
import characters.bullet.maBullet;
import characters.bullet.soBullet;
import characters.character;
import characters.enemy.boss;
import characters.enemy.insect;
import characters.enemy.soldier;
import characters.player.mario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class marioJPanel extends JPanel implements KeyListener {


    private boolean isBoss = false;
    private boolean isWin = false;
    private boolean isFailed = false;
    private final Failed failed = new Failed();
    private final Win win = new Win();

    private final mapLon PMap = new mapLon();
    private final mapCha PMap1 = new mapCha();
    public AMap NMap = PMap;

    private final mario PMario = mario.GetMario();
    private final soldier soldier1 = new soldier();
    private final boss boss1 = new boss();
    // 方向常量
    public final static int DIR_RIGHT = 1;
    public final static int DIR_LEFT = 2;

    // 所有可以用的按键集合
    private final int[] VK = {KeyEvent.VK_D, KeyEvent.VK_A, KeyEvent.VK_K, KeyEvent.VK_J};//有效的按键
    // 已经按下的按键
    private final Set<Integer> pKey = new HashSet<>();
    // mario的子弹
    private final List<maBullet> maBullets = new ArrayList<>();
    // 士兵的子弹
    private final List<soBullet> soBullets = new ArrayList<>();
    // 虫子
    private final insect insect1 = new insect();

    // mario走路的线程
    MarioWalk marioWalk = new MarioWalk();
    // 子弹的线程
    MaBulletThread maBulletThread = new MaBulletThread();
    SoBulletThread soBulletThread = new SoBulletThread();
    // mario运动的线程
    MaActionThread maActionThread = new MaActionThread();
    // mario自由落体线程
    MarioFall marioFall = new MarioFall();
    Thread marioFallThread = new Thread(marioFall);
    // 虫子线程
    InsectRun insectRun = new InsectRun();
    Thread insectRunThread = new Thread(insectRun);
    //士兵线程
    SoAction soAction = new SoAction();
    Thread soActionThread = new Thread(soAction);
    // boss 线程
    BoAction boAction = new BoAction();
    Thread boActionThread = new Thread(boAction);
    // 线程池
    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    // 循环执行的线程池
    ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    ScheduledExecutorService service1 = Executors.newSingleThreadScheduledExecutor();

    public marioJPanel() {
        service.scheduleAtFixedRate(maBulletThread, 300, 300, TimeUnit.MILLISECONDS);
        service1.scheduleAtFixedRate(soBulletThread, 300, 300, TimeUnit.MILLISECONDS);
        service.scheduleAtFixedRate(maActionThread, 60, 60, TimeUnit.MILLISECONDS);
        marioFallThread.start();
        insectRunThread.start();
        soActionThread.start();
        this.setFocusable(true);
        this.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int flag;
        for (flag = 0; flag < VK.length; flag++) {
            if (e.getKeyCode() == VK[flag]) {
                break;
            }
        }
        if (flag == VK.length) {
            return;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_D -> pKey.add(KeyEvent.VK_D);
            case KeyEvent.VK_A -> pKey.add(KeyEvent.VK_A);
            case KeyEvent.VK_J -> {//攻击
                maBullet maBullet1 = new maBullet();
                maBullet1.y = PMario.y + 20;
                if (PMario.direct == DIR_LEFT) {
                    maBullet1.Mx = PMario.Mx - 20;
                    maBullet1.x = PMario.x - 20;
                    maBullet1.direct = DIR_LEFT;
                } else if (PMario.direct == DIR_RIGHT) {
                    maBullet1.Mx = PMario.Mx + 20;
                    maBullet1.x = PMario.x + 20;
                    maBullet1.direct = DIR_RIGHT;
                }
                maBullet1.changeImg();
                maBullets.add(maBullet1);
            }
            case KeyEvent.VK_K -> pKey.add(KeyEvent.VK_K);
        }
    }

    // mario按下按键后动作判断 并且更新地面的值
    public class MaActionThread implements Runnable {                                 //发射子弹
        @Override
        public void run() {
            if (PMario.Mx >= 3200) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                NMap = PMap1;
                PMap.x = 0;
                soBullets.clear();
                maBullets.clear();
                PMario.xueLiang = 50;
                PMario.Mx = PMario.x = 50;
                PMario.y = 100;
                isBoss = true;
                boActionThread.start();
            }
            for (Integer key : pKey) {
                switch (key) {
                    case KeyEvent.VK_A -> {//左
                        PMario.direct = DIR_LEFT;
                        cachedThreadPool.execute(marioWalk);
                    }
                    case KeyEvent.VK_D -> {//右
                        PMario.direct = DIR_RIGHT;
                        cachedThreadPool.execute(marioWalk);
                    }
                    case KeyEvent.VK_K -> {
                        if (!PMario.isMaJump) {
                            PMario.isMaJump = true;
                            PMario.y -= 10;
                            maJumpSpeed = 50;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int flag;
        for (flag = 0; flag < VK.length; flag++) {
            if (e.getKeyCode() == VK[flag]) {
                break;
            }
        }
        if (flag == VK.length) {
            return;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> {
                pKey.remove(KeyEvent.VK_A);
                if (pKey.size() == 0)
                    PMario.maStand();
            }
            case KeyEvent.VK_D -> {
                pKey.remove(KeyEvent.VK_D);
                if (pKey.size() == 0)
                    PMario.maStand();
            }
            case KeyEvent.VK_K -> {
                pKey.remove(KeyEvent.VK_K);
                if (pKey.size() == 0)
                    PMario.maStand();
            }
        }
    }


    public int random(int lower, int upper) {
        return (int) (Math.floor(Math.random() * (upper - lower)) + lower);
    }


    // mario按下按键后动作判断 并且更新地面的值
    public class BoAction implements Runnable {                                 //发射子弹
        @Override
        public void run() {
            while (boss1.isLive) {
                boss1.danDao = random(3, 15);
                soBullet soBullet1 = new soBullet();
                soBullet1.y = boss1.danDao * 50;
                soBullet1.Mx = boss1.Mx - 20;
                soBullet1.x = boss1.x - 20;
                soBullet1.direct = DIR_LEFT;
                soBullets.add(soBullet1);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    int[] SState = {1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 3};

    // mario按下按键后动作判断 并且更新地面的值
    public class SoAction implements Runnable {                                 //发射子弹
        @Override
        public void run() {
            while (!isBoss && soldier1.isLive) {
                // 随机生成soldier的方向
                soldier1.direct = random(1, 3);
                // soldier的状态 1:走路 2:趴下 3:射击
                for (int state : SState) {
                    switch (state) {
                        case 1 -> {//1:走路
                            if (soldier1.isDown) {
                                soldier1.y -= 50;
                                soldier1.isDown = false;
                            }
                            if (soldier1.direct == DIR_LEFT) {
                                if (soldier1.Mx <= 1800) {
                                    soldier1.direct = DIR_RIGHT;
                                } else {
                                    soldier1.Mx -= 10;
                                    soldier1.x = soldier1.Mx + PMap.x;
                                    soldier1.changeImage();
                                }
                            } else if (soldier1.direct == DIR_RIGHT) {
                                if (soldier1.Mx >= 2800) {
                                    soldier1.direct = DIR_LEFT;
                                } else {
                                    soldier1.Mx += 10;
                                    soldier1.x = soldier1.Mx + PMap.x;
                                    soldier1.changeImage();
                                }
                            }
                        }
                        case 2 -> {//2:趴下
                            if (!soldier1.isDown)
                                soldier1.y += 50;
                            soldier1.isDown = true;
                            soldier1.SoDownImg();
                        }
                        case 3 -> {//3:射击
                            if (soldier1.isDown) {
                                soldier1.y -= 50;
                                soldier1.isDown = false;
                            }
                            soBullet soBullet1 = new soBullet();
                            soBullet1.y = soldier1.y + 50;
                            if (soldier1.direct == DIR_LEFT) {
                                soBullet1.Mx = soldier1.Mx - 20;
                                soBullet1.x = soldier1.x - 20;
                                soBullet1.direct = DIR_LEFT;
                            } else if (soldier1.direct == DIR_RIGHT) {
                                soBullet1.Mx = soldier1.Mx + 20;
                                soBullet1.x = soldier1.x + 20;
                                soBullet1.direct = DIR_RIGHT;
                            }
                            soBullets.add(soBullet1);
                        }
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    // insect运动的线程
    private class InsectRun implements Runnable {

        @Override
        public void run() {
            while (!isBoss && insect1.isLive) {
                if (isAHitB(insect1, PMario)) {
                    isFailed = true;
                }
                if (insect1.direct == DIR_LEFT) {
                    if (isHitMapRight(insect1, NMap)) {
                        insect1.direct = DIR_RIGHT;
                    } else {
                        insect1.Mx -= 10;
                        insect1.x = insect1.Mx + PMap.x;
                        insect1.changeImage();
                    }
                } else if (insect1.direct == DIR_RIGHT) {
                    if (isHitMapLeft(insect1, NMap)) {
                        insect1.direct = DIR_LEFT;
                    } else {
                        insect1.Mx += 10;
                        insect1.x = insect1.Mx + PMap.x;
                        insect1.changeImage();
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //改变跳跃时，地面的位置
    public void changeGroundMY(AMap aMap) {
        int x = PMario.Mx / 50;
        int ex = PMario.Mx % 50;
        int y = (PMario.y + 50) / 50;
        // System.out.println("ey:" + ey + " ex:" + ex + " x:" + x + " y:" + y);
        if (y >= 15) {
            isFailed = true;
        } else {
            boolean isHasFloor = false;
            if (ex == 0) {
                for (int i = y; i < aMap.map.length; i++) {
                    if (aMap.map[i][x] != 0) {
                        PMario.groundMY = i * 50 - 50;
                        isHasFloor = true;
                        break;
                    }
                }
            } else {
                for (int i = y; i < aMap.map.length; i++) {
                    if (aMap.map[i][x] != 0 || aMap.map[i][x - 1] != 0) {
                        PMario.groundMY = i * 50 - 50;
                        isHasFloor = true;
                        break;
                    }
                }
            }
            if (!isHasFloor) {
                PMario.groundMY = 1000;
            }
        }
    }

    // mario自由落体的线程
    double maJumpSpeed = 0;
    double g = 10;
    double t = 1.2;
    double s;

    private class MarioFall implements Runnable {
        @Override
        public void run() {
            while (!isFailed) {
                changeGroundMY(NMap);
                // System.out.println(PMario.groundMY);
                double v0 = maJumpSpeed;
                s = v0 * t - g * t * t / 2;
                maJumpSpeed = v0 - (g * t);
                PMario.y -= (int) s;
                PMario.changeImage();
                if (PMario.y < 0) {
                    PMario.y = 0;
                }
                if (PMario.y >= PMario.groundMY) {
                    PMario.isMaJump = false;
                    PMario.y = PMario.groundMY;
                    PMario.maStand();
                }
                try {
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // mario走路的线程
    private class MarioWalk implements Runnable {
        @Override
        public void run() {
            if (PMario.direct == DIR_LEFT && !isHitMapRight(PMario, NMap)) {
                if (PMario.Mx <= 570 || PMario.Mx > 2670) {
                    PMario.x -= PMario.speed;
                } else {
                    PMap.x += PMario.speed;
                }
                PMario.Mx -= PMario.speed;
                if (PMario.Mx < 0) {
                    PMario.Mx = PMario.x = 0;
                }
            } else if (PMario.direct == DIR_RIGHT && !isHitMapLeft(PMario, NMap)) {
                if (PMario.Mx < 570 || PMario.Mx >= 2670) {
                    PMario.x += PMario.speed;
                } else {
                    PMap.x -= PMario.speed;
                }
                PMario.Mx += PMario.speed;
                if (PMario.Mx > 3250) {
                    PMario.Mx = 3250;
                    PMario.x = 1150;
                }
            }
            PMario.changeImage();
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 子弹的线程
    public class MaBulletThread implements Runnable {                                 //发射子弹
        @Override
        public void run() {
            for (int i = 0; i < maBullets.size(); i++) {
                if (isHitMap(maBullets.get(i), NMap)) {
                    maBullets.get(i).isLive = false;
                } else if (insect1.isLive && isAHitB(maBullets.get(i), insect1)) {
                    insect1.isLive = false;
                    maBullets.get(i).isLive = false;
                } else if (!isBoss && soldier1.isLive && isAHitB(maBullets.get(i), soldier1)) {
                    maBullets.get(i).isLive = false;
                    soldier1.xueLiang -= 25;
                    if (soldier1.xueLiang <= 0)
                        soldier1.isLive = false;
                } else if (isBoss && boss1.isLive && isAHitB(maBullets.get(i), boss1)) {
                    maBullets.get(i).isLive = false;
                    boss1.xueLiang -= 500;
                    if (boss1.xueLiang <= 0) {
                        boss1.isLive = false;
                        isBoss = false;
                        isWin = true;
                    }
                }
                if (maBullets.get(i).direct == DIR_LEFT) {
                    maBullets.get(i).x -= 50;
                    maBullets.get(i).Mx -= 50;
                } else if (maBullets.get(i).direct == DIR_RIGHT) {
                    maBullets.get(i).x += 50;
                    maBullets.get(i).Mx += 50;
                }
                if (!maBullets.get(i).isLive) {
                    maBullets.remove(maBullets.get(i));
                }
            }
        }
    }

    // 子弹的线程
    public class SoBulletThread implements Runnable {                                 //发射子弹
        @Override
        public void run() {
            for (int i = 0; i < soBullets.size(); i++) {
                if (isHitMap(soBullets.get(i), NMap)) {
                    soBullets.get(i).isLive = false;
                } else if (isAHitB(soBullets.get(i), PMario)) {
                    PMario.xueLiang -= 10;
                    if (PMario.xueLiang <= 0) {
                        isFailed = true;
                    }
                    soBullets.get(i).isLive = false;
                }
                if (soBullets.get(i).direct == DIR_LEFT) {
                    soBullets.get(i).x -= 50;
                    soBullets.get(i).Mx -= 50;
                } else if (soBullets.get(i).direct == DIR_RIGHT) {
                    soBullets.get(i).x += 50;
                    soBullets.get(i).Mx += 50;
                }
                if (!soBullets.get(i).isLive) {
                    soBullets.remove(soBullets.get(i));
                }
            }
        }
    }

    // 元素A有没有撞到B
    private boolean isAHitB(character A, character B) {
        Rectangle rA = new Rectangle(A.Mx, A.y, A.w, A.h);
        Rectangle rB = new Rectangle(B.Mx, B.y, B.w, B.h);
        return rA.intersects(rB);
    }

    // 元素有没有撞到地图中的内容
    private boolean isHitMap(character cha, AMap aMap) {
        Rectangle rA = new Rectangle(cha.Mx, cha.y, cha.w, cha.h);
        Rectangle rB;
        for (int i = 0; i < aMap.map.length; i++) {
            for (int j = 0; j < aMap.map[i].length; j++) {
                if (aMap.map[i][j] != 0) {
                    rB = new Rectangle(j * 50, i * 50, 50, 50);
                    if (rA.intersects(rB)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // 判断有没有撞到墙的左边
    public boolean isHitMapLeft(character cha, AMap aMap) {
        int x = (cha.Mx + 50) / 50;
        int y = (cha.y + 50) / 50;
        int ey = (cha.y + 50) % 50;
        if (ey == 0) {
            return aMap.map[y - 1][x] != 0;
        }
        return aMap.map[y][x] != 0;
    }

    // 判断有没有撞到墙的右边
    public boolean isHitMapRight(character cha, AMap aMap) {
        int x = cha.Mx / 50;
        int y = (cha.y + 50) / 50;
        int ey = (cha.y + 50) % 50;
        if (ey == 0) {
            return aMap.map[y - 1][x] != 0;
        }
        return aMap.map[y][x] != 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isFailed) {
            failed.paintObject(g);
        } else if (isWin) {
            win.paintObject(g);
        } else if (isBoss) {
            PMap1.paintObject(g);
            for (soBullet bullet1 : soBullets) {
                bullet1.paintObject(g);
            }
            for (maBullet bullet : maBullets) {
                bullet.paintObject(g);
            }
            PMario.paintObject(g);
            if (boss1.isLive)
                boss1.paintObject(g);
        } else {
            PMap.paintObject(g);
            PMario.paintObject(g);
            if (soldier1.isLive) {
                soldier1.paintObject(g);
            }
            for (maBullet bullet : maBullets) {
                bullet.paintObject(g);
            }
            for (soBullet bullet1 : soBullets) {
                bullet1.paintObject(g);
            }
            if (insect1.isLive) {
                insect1.paintObject(g);
            }
        }
        repaint();
    }
}
