package com.ycychat.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FriendChat extends JFrame implements ActionListener {
    JTextArea jta;
    JScrollPane jsp;
    JTextField jtf;
    JButton jb;

    public FriendChat(String oneToAnother) {
        jta = new JTextArea();
        jta.setForeground(Color.red);
        jsp = new JScrollPane(jta);
        this.add(jsp, "Center");

        jtf = new JTextField(25);
        jb = new JButton("发送");
        jb.addActionListener(this);
        jb.setForeground(Color.blue);
        JPanel jp = new JPanel();
        jp.add(jtf);
        jp.add(jb);
        this.add(jp, "South");

        this.setSize(350, 250);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setTitle(oneToAnother + "的聊天界面");
        this.setIconImage(new ImageIcon("images/duck2.jpg").getImage());
        this.setVisible(true);
    }

    public static void main(String[] args) {
        FriendChat fc = new FriendChat("");
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jb) {
            jta.append(jtf.getText() + "\r\n");
        }
    }
}