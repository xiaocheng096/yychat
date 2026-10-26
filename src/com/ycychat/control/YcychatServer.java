package com.ycychat.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.ycychat.model.Message;
import com.ycychat.model.MessageType;
import com.ycychat.model.User;

/**
 * 服务器端，监听 3456 端口并处理客户端的登录校验请求。
 * 实现了 {@link Runnable}，应在独立线程中运行，避免阻塞界面线程；
 * 通过 {@link #stop()} 可关闭服务器。
 */
public class YcychatServer implements Runnable {

    /** 关闭服务器的指令标识，客户端用该用户名连接时服务器会停止 */
    public static final String SHUTDOWN_COMMAND = "SHUTDOWN_SERVER";

    private ServerSocket ss;
    private Socket s;
    private volatile boolean running = false;

    @Override
    public void run() {
        try {
            ss = new ServerSocket(3456);
            running = true;
            System.out.println("服务器启动成功，正在监听3456端口...");
            while (running) {
                s = ss.accept();
                System.out.println("连接成功:" + s);

                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                User user = (User) ois.readObject();

                // 收到关闭指令则停止服务器
                if (SHUTDOWN_COMMAND.equals(user.getUserName())) {
                    System.out.println("收到关闭指令，服务器即将停止。");
                    break;
                }

                String userName = user.getUserName();
                String password = user.getPassword();
                System.out.println("服务器端收到的客户端登录信息 userName:" + userName + " password:" + password);

                Message mess = new Message();
                if (password.equals("123456")) {
                    System.out.println("密码验证通过！");
                    mess.setMessageType(MessageType.LOGIN_VALIDATE_SUCCESS);
                } else {
                    System.out.println("密码验证失败！");
                    mess.setMessageType(MessageType.LOGIN_VALIDATE_FAILURE);
                }

                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                oos.writeObject(mess);
            }
        } catch (IOException e) {
            // 正常停止时，accept() 会抛出 SocketException，此处不视为异常
            if (running) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            running = false;
            closeServerSocket();
            System.out.println("服务器已停止。");
        }
    }

    /**
     * 停止服务器，关闭监听的 ServerSocket。
     */
    public void stop() {
        running = false;
        closeServerSocket();
    }

    private void closeServerSocket() {
        try {
            if (ss != null && !ss.isClosed()) {
                ss.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(new YcychatServer()).start();
    }
}