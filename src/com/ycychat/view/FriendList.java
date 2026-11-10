package com.ycychat.view;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.*;

import com.ycychat.model.Message;

public class FriendList extends JFrame implements ActionListener, MouseListener {

    /**
     * 实验16 第8步：保存“当前用户to好友名”和对应的聊天界面。
     * 使用 ConcurrentHashMap，避免界面线程与接收线程同时读写出错。
     */
    public static ConcurrentHashMap<String, FriendChat> hmFriendChat = new ConcurrentHashMap<>();

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
            String imageStr = "images/" + (int) (Math.random() * 6) + ".jpg";
            ImageIcon imageIcon = new ImageIcon(imageStr);
            // 实验16 第13步：好友名字改为数字，便于与登录用户名对应
            friendLabel1[i] = new JLabel(i + "", imageIcon, JLabel.LEFT);
            // 实验18：默认处于离线状态（图标变灰），上线后再激活
            friendLabel1[i].setEnabled(false);
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
            strangerLabel1[i] = new JLabel("" + i + "号陌生人", new ImageIcon("images/touxiang_1.jpg"), JLabel.LEFT);
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
            // 实验16 第10步：创建并保存 FriendChat 对象
            FriendChat fc = new FriendChat(name, toName);
            hmFriendChat.put(name + "to" + toName, fc);
        }
    }

    /**
     * 实验18：根据服务器返回的在线好友字符串，激活全部在线好友图标。
     */
    public void activeOnlineFriendIcon(Message mess) {
        String content = mess.getContent();
        if (content == null) {
            return;
        }
        String[] onlineFriends = content.trim().split("\\s+");
        for (String onlineFriend : onlineFriends) {
            if (!onlineFriend.isEmpty()) {
                activeNewOnlineFriendIcon(onlineFriend);
            }
        }
    }

    /**
     * 实验18 第9步：激活新上线好友的图标。
     */
    public void activeNewOnlineFriendIcon(String newOnlineFriend) {
        try {
            int index = Integer.parseInt(newOnlineFriend.trim());
            if (index >= 0 && index < friendLabel1.length) {
                friendLabel1[index].setEnabled(true);
            }
        } catch (NumberFormatException e) {
            System.out.println("好友名不是数字，无法激活图标：" + newOnlineFriend);
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