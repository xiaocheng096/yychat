package com.ycychat.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.ycychat.model.Message;
import com.ycychat.model.MessageType;
import com.ycychat.model.User;

/**
 * 客户端连接类，负责向服务器发送登录信息并接收校验结果。
 * 登录成功后启动接收线程，持续接收服务器转发的消息。
 */
public class YcychatClientConnection {

    /** 与服务器建立的 Socket 连接，聊天窗口发送消息时也使用该连接 */
    public static Socket s;
    /** 复用的对象输出流 */
    public static ObjectOutputStream oos;
    /** 复用的对象输入流 */
    public static ObjectInputStream ois;

    public YcychatClientConnection() {
        try {
            s = new Socket("127.0.0.1", 3456);
            System.out.println("客户端连接成功：" + s);
            oos = new ObjectOutputStream(s.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(s.getInputStream());
        } catch (IOException e) {
            System.out.println("连接服务器失败，请先启动服务器！");
            s = null;
            oos = null;
            ois = null;
        }
    }

    /**
     * 校验用户登录信息。
     *
     * @param user 待校验的用户
     * @return 校验通过返回 true，否则返回 false
     */
    public boolean loginValidate(User user) {
        boolean loginSuccess = false;
        if (s == null || oos == null || ois == null) {
            return false;
        }
        try {
            oos.writeObject(user);
            oos.flush();

            Message mess = (Message) ois.readObject();
            if (MessageType.LOGIN_VALIDATE_SUCCESS.equals(mess.getMessageType())) {
                loginSuccess = true;
                // 实验16 第6步：登录成功后创建客户端接收线程
                new ClientReceiverThread(s, ois).start();
            } else {
                s.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return loginSuccess;
    }

    /**
     * 向服务器发送消息。
     */
    public static void sendMessage(Message mess) {
        if (s == null || oos == null) {
            System.out.println("尚未连接服务器，消息发送失败。");
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
}