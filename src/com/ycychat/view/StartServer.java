package com.ycychat.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.swing.*;

import com.ycychat.control.YcychatServer;
import com.ycychat.model.User;

/**
 * 服务器界面，提供启动/停止服务器的功能。
 * 打开界面时会先尝试关闭可能已在运行的服务器，避免端口被占用。
 */
public class StartServer extends JFrame implements ActionListener {
    JButton jb1, jb2;
    YcychatServer server;
    Thread serverThread;

    public StartServer() {
        // 打开界面时先关闭一遍服务器，避免多次开启导致端口占用
        shutdownRunningServer();

        jb1 = new JButton("启动服务器");
        jb1.addActionListener(this);
        jb1.setFont(new Font("宋体", Font.BOLD, 25));

        jb2 = new JButton("停止服务器");
        jb2.addActionListener(this);
        jb2.setFont(new Font("宋体", Font.BOLD, 25));

        this.setLayout(new GridLayout(1, 2));
        this.add(jb1);
        this.add(jb2);
        this.setSize(400, 100);
        this.setLocationRelativeTo(null);
        this.setIconImage(new ImageIcon("images/duck2.jpg").getImage());
        this.setTitle("YYchat 服务器");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new StartServer();
    }

    /**
     * 尝试关闭已经在运行的服务器（可能是上一次启动后残留的进程）。
     * 若端口上没有服务器监听，则直接忽略。
     */
    private void shutdownRunningServer() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("127.0.0.1", 3456), 500);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            User user = new User();
            user.setUserName(YcychatServer.SHUTDOWN_COMMAND);
            oos.writeObject(user);
            oos.flush();
            System.out.println("已发送关闭指令，正在关闭残留的服务器。");
        } catch (IOException e) {
            // 端口无服务器监听（连接被拒绝/超时），无需处理
        }
    }

    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == jb1) {
            // 启动服务器：在独立线程中运行，避免阻塞界面
            if (serverThread != null && serverThread.isAlive()) {
                System.out.println("服务器已经在运行中。");
                return;
            }
            server = new YcychatServer();
            serverThread = new Thread(server);
            serverThread.start();
            System.out.println("服务器已启动。");
        } else if (arg0.getSource() == jb2) {
            // 停止服务器
            if (server != null) {
                server.stop();
                server = null;
                serverThread = null;
                System.out.println("服务器已停止。");
            }
        }
    }
}