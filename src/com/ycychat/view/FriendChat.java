package com.ycychat.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.ycychat.control.YcychatClientConnection;
import com.ycychat.model.Message;
import com.ycychat.model.MessageType;

public class FriendChat extends JFrame implements ActionListener {
    JTextArea jta;
    JScrollPane jsp;
    JTextField jtf;
    JButton jb;
    String sender;
    String receiver;

    public FriendChat(String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;

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
        this.setTitle(sender + " to " + receiver + " 的聊天界面");
        this.setIconImage(new ImageIcon("images/duck2.jpg").getImage());
        this.setVisible(true);
    }

    public static void main(String[] args) {
        FriendChat fc = new FriendChat("", "");
    }

    /**
     * 实验16 第12步：接收到好友消息后，追加显示到聊天界面。
     */
    public void append(Message mess) {
        jta.append(mess.getSender() + "对你说: " + mess.getContent() + "\r\n");
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jb) {
            jta.append(jtf.getText() + "\r\n");

            Message mess = new Message();
            mess.setSender(sender);
            mess.setReceiver(receiver);
            mess.setContent(jtf.getText());
            // 实验16：设置聊天消息类型后发送给服务器
            mess.setMessageType(MessageType.COMMON_CHAT_MESSAGE);
            YcychatClientConnection.sendMessage(mess);
        }
    }
}