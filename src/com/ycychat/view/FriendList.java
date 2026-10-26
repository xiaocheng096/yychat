package com.ycychat.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FriendList extends JFrame implements ActionListener, MouseListener {
    JPanel friendPanel;
    JButton myFriendButton1;
    JButton myStrangerButton1;
    JButton blackListButton1;
    JScrollPane friendListScrollPane1;
    JPanel friendListPanel1;
    final int MYFRIENDCOUNT = 50;
    JLabel friendLabel1[] = new JLabel[MYFRIENDCOUNT];

    JPanel strangerPanel;
    JButton myFriendButton2;
    JButton myStrangerButton2;
    JButton blackListButton2;
    JScrollPane strangerListScrollPane1;
    JPanel strangerListPanel1;
    final int STRANGERCOUNT = 20;
    JLabel strangerLabel1[] = new JLabel[STRANGERCOUNT];

    CardLayout cl;
    String name;

    public FriendList(String name) {
        this.name = name;

        friendPanel = new JPanel(new BorderLayout());
        myFriendButton1 = new JButton("我的好友");
        friendPanel.add(myFriendButton1, "North");

        friendListPanel1 = new JPanel(new GridLayout(MYFRIENDCOUNT, 1));
        for (int i = 0; i < friendLabel1.length; i++) {
            String imageStr = "Images/" + (int) (Math.random() * 6) + ".jpg";
            ImageIcon imageIcon = new ImageIcon(imageStr);
            friendLabel1[i] = new JLabel(i + "号好友", imageIcon, JLabel.LEFT);
            friendLabel1[i].addMouseListener(this);
            friendListPanel1.add(friendLabel1[i]);
        }
        friendListScrollPane1 = new JScrollPane(friendListPanel1);
        friendPanel.add(friendListScrollPane1, "Center");

        myStrangerButton1 = new JButton("陌生人");
        myStrangerButton1.addActionListener(this);
        blackListButton1 = new JButton("黑名单");
        JPanel stranger_BlackPanel = new JPanel(new GridLayout(2, 1));
        stranger_BlackPanel.add(myStrangerButton1);
        stranger_BlackPanel.add(blackListButton1);
        friendPanel.add(stranger_BlackPanel, "South");

        strangerPanel = new JPanel(new BorderLayout());
        myFriendButton2 = new JButton("我的好友");
        myFriendButton2.addActionListener(this);
        myStrangerButton2 = new JButton("陌生人");
        JPanel friend_strangerPanel = new JPanel(new GridLayout(2, 1));
        friend_strangerPanel.add(myFriendButton2);
        friend_strangerPanel.add(myStrangerButton2);
        strangerPanel.add(friend_strangerPanel, "North");

        strangerListPanel1 = new JPanel(new GridLayout(STRANGERCOUNT, 1));
        for (int i = 0; i < strangerLabel1.length; i++) {
            strangerLabel1[i] = new JLabel("" + i + "号陌生人", new ImageIcon("Images/touxiang_1.jpg"), JLabel.LEFT);
            strangerLabel1[i].addMouseListener(this);
            strangerListPanel1.add(strangerLabel1[i]);
        }
        strangerListScrollPane1 = new JScrollPane(strangerListPanel1);
        strangerPanel.add(strangerListScrollPane1, "Center");

        blackListButton2 = new JButton("黑名单");
        strangerPanel.add(blackListButton2, "South");

        cl = new CardLayout();
        this.setLayout(cl);
        this.add(friendPanel, "card1");
        this.add(strangerPanel, "card2");

        this.setIconImage(new ImageIcon("images/duck2.jpg").getImage());
        this.setTitle(name + "的好友列表");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setBounds(800, 600, 350, 250);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        FriendList fl = new FriendList("");
    }

    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == myStrangerButton1) {
            cl.show(this.getContentPane(), "card2");
        }
        if (arg0.getSource() == myFriendButton2) {
            cl.show(this.getContentPane(), "card1");
        }
        if (arg0.getSource() == myStrangerButton2) {
            cl.show(this.getContentPane(), "card2");
        }
    }

    public void mouseClicked(MouseEvent arg0) {
        if (arg0.getClickCount() == 2) {
            JLabel jl = (JLabel) arg0.getSource();
            String toName = jl.getText();
            new FriendChat(name + " 对 " + toName);
        }
    }

    public void mouseEntered(MouseEvent arg0) {
        JLabel jl = (JLabel) arg0.getSource();
        jl.setForeground(Color.red);
    }

    public void mouseExited(MouseEvent arg0) {
        JLabel jl = (JLabel) arg0.getSource();
        jl.setForeground(Color.blue);
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }
}