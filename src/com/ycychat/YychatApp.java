package com.ycychat;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.ycychat.view.StartServer;

/**
 * 程序入口 / 界面联调启动器。
 * <p>
 * 客户端的连接对象是静态的，因此每个客户端必须运行在独立进程中。
 * 本窗口负责打开服务器界面，并不断新开独立的客户端进程，方便做多用户联调。
 */
public class YychatApp extends JFrame implements ActionListener {

    private final JButton serverButton;
    private final JButton clientButton;

    public YychatApp() {
        serverButton = new JButton("启动服务器界面");
        serverButton.setFont(new Font("宋体", Font.BOLD, 20));
        serverButton.addActionListener(this);

        clientButton = new JButton("新开客户端（独立进程）");
        clientButton.setFont(new Font("宋体", Font.BOLD, 20));
        clientButton.addActionListener(this);

        JLabel tip = new JLabel("提示：可多次点击“新开客户端”，模拟多个用户登录联调。", JLabel.CENTER);

        this.setLayout(new GridLayout(3, 1));
        this.add(serverButton);
        this.add(clientButton);
        this.add(tip);
        this.setTitle("YYChat 联调入口");
        this.setSize(420, 180);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new YychatApp();
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == serverButton) {
            new StartServer();
        } else if (e.getSource() == clientButton) {
            startClientProcess();
        }
    }

    /**
     * 启动一个独立的客户端进程，避免多个客户端共用同一份静态连接对象。
     */
    private void startClientProcess() {
        String javaPath = System.getProperty("java.home") + File.separator + "bin"
                + File.separator + "java";
        String classPath = System.getProperty("java.class.path");
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    javaPath, "-Dfile.encoding=UTF-8", "-cp", classPath, "com.ycychat.view.ClientLogin");
            pb.directory(new File(System.getProperty("user.dir")));
            pb.start();
            System.out.println("已启动新的客户端进程。");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "启动客户端进程失败：" + ex.getMessage());
        }
    }
}