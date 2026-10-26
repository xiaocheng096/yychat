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
 */
public class YcychatClientConnection {
    Socket s;

    public YcychatClientConnection() {
        try {
            s = new Socket("127.0.0.1", 3456);
            System.out.println("客户端连接成功：" + s);
        } catch (IOException e) {
            e.printStackTrace();
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
        try {
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(user);

            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            Message mess = (Message) ois.readObject();

            if (mess.getMessageType().equals(MessageType.LOGIN_VALIDATE_SUCCESS)) {
                loginSuccess = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return loginSuccess;
    }
}