package com.ycychat.model;

/**
 * 消息类型常量接口。
 */
public interface MessageType {
    /** 登录校验成功 */
    String LOGIN_VALIDATE_SUCCESS = "1";
    /** 登录校验失败 */
    String LOGIN_VALIDATE_FAILURE = "2";
    /** 普通聊天消息 */
    String COMMON_CHAT_MESSAGE = "3";
    /** 客户端请求获取在线好友的名字 */
    String REQUEST_ONLINE_FRIEND = "4";
    /** 服务器返回在线好友的名字 */
    String RESPONSE_ONLINE_FRIEND = "5";
    /** 客户端告诉服务器：自己上线了，请通知所有在线好友（实验18） */
    String NEW_ONLINE_TO_ALL_FRIEND = "6";
    /** 服务器告诉客户端：有新好友上线（实验18） */
    String NEW_ONLINE_FRIEND = "7";
}