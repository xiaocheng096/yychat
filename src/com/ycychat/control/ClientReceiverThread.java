package com.ycychat.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.ycychat.model.Message;
import com.ycychat.model.MessageType;
import com.ycychat.view.ClientLogin;
import com.ycychat.view.FriendChat;
import com.ycychat.view.FriendList;

/**
 * 实验16：客户端接收线程。
 * 登录成功后启动，持续接收服务器转发过来的消息，
 * 在好友聊天界面显示消息、激活在线好友图标等。
 */
public class ClientReceiverThread extends Thread {
    private final Socket s;
    private final ObjectInputStream ois;

    public ClientReceiverThread(Socket s, ObjectInputStream ois) {
        this.s = s;
        this.ois = ois;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message mess = (Message) ois.readObject();
                String messageType = mess.getMessageType();

                if (MessageType.COMMON_CHAT_MESSAGE.equals(messageType)) {
                    // 实验16 第11步：从 HashMap 中找到聊天界面并显示消息
                    String receiver = mess.getReceiver();
                    String sender = mess.getSender();
                    FriendChat fc = FriendList.hmFriendChat.get(receiver + "to" + sender);
                    if (fc != null) {
                        fc.append(mess);
                    } else {
                        System.out.println("请打开" + receiver + "to" + sender + "的聊天界面");
                    }
                } else if (MessageType.RESPONSE_ONLINE_FRIEND.equals(messageType)) {
                    // 实验18：激活服务器返回的全部在线好友图标
                    FriendList fl = ClientLogin.hmFriendList.get(mess.getReceiver());
                    if (fl != null) {
                        fl.activeOnlineFriendIcon(mess);
                    }
                } else if (MessageType.NEW_ONLINE_FRIEND.equals(messageType)) {
                    // 实验18 第8步：激活新上线好友的图标
                    FriendList fl = ClientLogin.hmFriendList.get(mess.getReceiver());
                    if (fl != null) {
                        fl.activeNewOnlineFriendIcon(mess.getSender());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("与服务器的连接已断开。");
                break;
            }
        }
    }
}