package JFrameTest;

import javax.swing.*;
import java.awt.*;

public class 上下左右边框的宽度 {
    public static void main(String args[]) {

        //创建JFrame
        JFrame frame = new JFrame("测试窗口");
        frame.setTitle("上下左右边框的宽度");
        frame.setSize(1237,764);
        frame.setVisible(false);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //显示窗体
        frame.setVisible(true);

        //获取上下左右边框的宽度，frame必须先可见（Visible）
        Insets inset = frame.getInsets();
        System.out.println("上下左右边框的宽度 = " + inset);

    }

}
