package com.huangjian.service;

import com.alibaba.fastjson.JSON;
import com.huangjian.qqcommon.Message;
import com.huangjian.qqcommon.MessageType;
import com.huangjian.qqcommon.NoticeThread;
import com.huangjian.qqcommon.User;
import com.huangjian.utilities.ByteUtilies;
import com.huangjian.utilities.ManageServiceSocketCollection;
import com.huangjian.utilities.RetainSocketThread;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"all"})
public class QQserver {
    private ServerSocket serverSocket;
    private static Map<String, User> userMap = new HashMap<>();

    public static void main(String[] args) {
        new QQserver().qqServer();


    }

    static {
        userMap.put("100", new User("100", "123"));
        userMap.put("200", new User("200", "123"));
        userMap.put("300", new User("300", "123"));
        userMap.put("400", new User("400", "123"));
        userMap.put("500", new User("500", "123"));
    }

    public void qqServer() {
        try {
            serverSocket = new ServerSocket(8083);
            System.out.println("服务器启动");
            //服务器线程用来发公告
            NoticeThread noticeThread = new NoticeThread();
            noticeThread.start();
            while (true) {
                Socket socket = serverSocket.accept();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] bytes = ByteUtilies.readBytes(socket);
                String messageString = new String(bytes);
                User user = JSON.parseObject(messageString, User.class);
                Message message = new Message();

                if (userMap.get(user.getUserId()) != null) {
                    RetainSocketThread retainSocketThread = new RetainSocketThread(socket);
                    retainSocketThread.setName(user.getUserId() + "-server-thread");
                    retainSocketThread.start();
                    ManageServiceSocketCollection.putThreadSocket(user.getUserId(), retainSocketThread);
                    System.out.println("Server created new thread: " + retainSocketThread + " with hashCode: " + System.identityHashCode(retainSocketThread));
                    message.setMsgType(MessageType.LOGIN_SUCCEED);
                    String jsonString = JSON.toJSONString(message);
                    ByteUtilies.writeBytes(socket, jsonString.getBytes());
                    System.out.println("用户：" + user.getUserId() + "，登录成功");
                } else {
                    System.out.println("用户：" + user.getUserId() + "，登录失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
