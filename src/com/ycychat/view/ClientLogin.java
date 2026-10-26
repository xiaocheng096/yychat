package com.ycychat.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.ycychat.control.YcychatClientConnection;
import com.ycychat.model.User;

public class ClientLogin extends JFrame implements ActionListener {
    JLabel jl2;
    JButton jb1, jb2, jb3;
    JPanel jp1;

    JLabel jl11, jl12, jl13, jl14;
    JTextField jtf;
    JPasswordField jpf;
    JButton jb4;
    JCheckBox jcb1, jcb2;
    JPanel jp, jp2, jp3;
    JTabbedPane jtp;

    public ClientLogin() {
        jl2 = new JLabel(new ImageIcon("images/head.png"));
        this.add(jl2, "North");

        jl11 = new JLabel("YY号码：");
        jl12 = new JLabel("YY密码：");
        jl13 = new JLabel("忘记密码：");
        jl13.setForeground(Color.blue);
        jl14 = new JLabel("申请密码保护");
        jb4 = new JButton(new ImageIcon("images/clear.png"));
        jtf = new JTextField();
        jpf = new JPasswordField();
        jcb1 = new JCheckBox("隐身登录");
        jcb2 = new JCheckBox("记住密码");
        jp1 = new JPanel(new GridLayout(3, 3));
        jp1.add(jl11);
        jp1.add(jtf);
        jp1.add(jb4);
        jp1.add(jl12);
        jp1.add(jpf);
        jp1.add(jl13);
        jp1.add(jcb1);
        jp1.add(jcb2);
        jp1.add(jl14);
        jtp = new JTabbedPane();
        jp2 = new JPanel();
        jp3 = new JPanel();
        jtp.add(jp1, "YY号码");
        jtp.add(jp2, "手机号码");
        jtp.add(jp3, "电子邮箱");
        this.add(jtp, "Center");

        jb1 = new JButton(new ImageIcon("images/login.png"));
        jb2 = new JButton(new ImageIcon("images/register.png"));
        jb3 = new JButton(new ImageIcon("images/cancel.png"));
        jb1.addActionListener(this);
        jp = new JPanel();
        jp.add(jb1);
        jp.add(jb2);
        jp.add(jb3);
        this.add(jp, "South");

        Image im = new ImageIcon("images/duck2.jpg").getImage();
        this.setIconImage(im);

        this.setSize(350, 250);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("YY 聊天");
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ClientLogin();
            }
        });
    }

    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == jb1) {
            String name = jtf.getText();
            String password = new String(jpf.getPassword());

            User user = new User();
            user.setUserName(name);
            user.setPassword(password);

            if (new YcychatClientConnection().loginValidate(user)) {
                new FriendList(name);
                System.out.println("客户端登录成功！");
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "密码错误，请重新登录！");
            }
        }
    }
}