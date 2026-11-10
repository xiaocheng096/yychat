package com.ycychat.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;

import com.ycychat.model.Message;
import com.ycychat.model.MessageType;

/**
 * 实验16：服务器端接收线程。
 * 每个登录成功的客户端对应一个本线程，负责持续接收该客户端发来的消息，
 * 并根据消息类型进行聊天转发、返回在线好友列表、通知好友上线等处理。
 */
public class ServerReceiverThread extends Thread {
    private final Socket s;
    private final ObjectInputStream ois;

    public ServerReceiverThread(Socket s, ObjectInputStream ois) {
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
                    // 实验16 第4步：从 hmSocket 中找到接收方的 Socket，转发聊天信息
                    System.out.println(mess.getSender() + " 对 " + mess.getReceiver() + " 说:" + mess.getContent());
                    String receiver = mess.getReceiver();
                    Socket rs = YcychatServer.hmSocket.get(receiver);
                    System.out.println("接收方 " + receiver + " 的 Socket对象: " + rs);
                    if (rs != null) {
                        sendMessage(rs, mess);
                    } else {
                        System.out.println(receiver + "不在线上");
                    }
                } else if (MessageType.REQUEST_ONLINE_FRIEND.equals(messageType)) {
                    // 实验18：把当前所有在线好友的名字返回给请求方
                    StringBuilder onlineFriends = new StringBuilder();
                    Iterator<String> it = YcychatServer.hmSocket.keySet().iterator();
                    while (it.hasNext()) {
                        onlineFriends.append(" ").append(it.next());
                    }
                    mess.setReceiver(mess.getSender());
                    mess.setSender("Server");
                    mess.setMessageType(MessageType.RESPONSE_ONLINE_FRIEND);
                    mess.setContent(onlineFriends.toString());
                    sendMessage(s, mess);
                } else if (MessageType.NEW_ONLINE_TO_ALL_FRIEND.equals(messageType)) {
                    // 实验18 第7步：通知所有在线好友，有新好友上线
                    mess.setMessageType(MessageType.NEW_ONLINE_FRIEND);
                    for (String receiver : YcychatServer.hmSocket.keySet()) {
                        Socket rs = YcychatServer.hmSocket.get(receiver);
                        mess.setReceiver(receiver);
                        sendMessage(rs, mess);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                // 客户端断开或连接异常，结束本线程并清理在线记录
                System.out.println("客户端连接已断开：" + s);
                YcychatServer.hmSocket.values().remove(s);
                YcychatServer.hmOos.remove(s);
                break;
            }
        }
    }

    /**
     * 向指定 Socket 发送消息。
     */
    public void sendMessage(Socket socket, Message mess) {
        YcychatServer.sendMessage(socket, mess);
    }
}