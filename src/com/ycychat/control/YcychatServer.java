package com.ycychat.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

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

    /** 实验16 第1步：保存在线用户的用户名和对应的 Server 端 Socket 对象 */
    public static ConcurrentHashMap<String, Socket> hmSocket = new ConcurrentHashMap<>();

    /** 保存每个 Socket 对应的输出流，复用同一个流，避免重复创建导致数据错乱 */
    public static ConcurrentHashMap<Socket, ObjectOutputStream> hmOos = new ConcurrentHashMap<>();

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
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                User user = (User) ois.readObject();

                // 收到关闭指令则停止服务器
                if (SHUTDOWN_COMMAND.equals(user.getUserName())) {
                    System.out.println("收到关闭指令，服务器即将停止。");
                    break;
                }

                String userName = user.getUserName();
                String password = user.getPassword();
                System.out.println(userName + " 连接成功: " + s);
                System.out.println("服务器端收到的客户端登录信息 userName:" + userName + " password:" + password);

                // 实验20：调用 DBUtil 完成数据库中的用户登录验证
                boolean loginSuccess = DBUtil.loginValidate(userName, password);
                Message mess = new Message();
                if (loginSuccess) {
                    System.out.println("密码验证通过！");
                    mess.setMessageType(MessageType.LOGIN_VALIDATE_SUCCESS);
                    oos.writeObject(mess);
                    oos.flush();

                    // 实验16 第3步：保存用户名和 Socket 对象，并为每个客户端创建接收线程
                    hmSocket.put(userName, s);
                    hmOos.put(s, oos);
                    new ServerReceiverThread(s, ois).start();
                    System.out.println("启动线程成功！");
                } else {
                    System.out.println("密码验证失败！");
                    mess.setMessageType(MessageType.LOGIN_VALIDATE_FAILURE);
                    oos.writeObject(mess);
                    oos.flush();
                    s.close();
                }
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
            closeAllClients();
            System.out.println("服务器已停止。");
        }
    }

    /**
     * 关闭所有已连接的客户端，使各个 ServerReceiverThread 能正常结束。
     */
    private static void closeAllClients() {
        for (Socket client : hmSocket.values()) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        hmSocket.clear();
        hmOos.clear();
    }

    /**
     * 向指定 Socket 发送消息，同一输出流的写入加锁，避免多线程并发写导致数据错乱。
     */
    public static void sendMessage(Socket socket, Message mess) {
        if (socket == null) {
            return;
        }
        ObjectOutputStream oos = hmOos.get(socket);
        if (oos == null) {
            return;
        }
        try {
            synchronized (oos) {
                oos.writeObject(mess);
                oos.flush();
            }
        } catch (IOException e) {
            System.out.println("发送消息失败：" + e.getMessage());
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