package com.huangjian.utilities;

import com.alibaba.fastjson2.JSON;
import com.huangjian.qqcommon.Message;
import com.huangjian.qqcommon.MessageType;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;

public class RetainSocketThread extends Thread {
    private Socket socket;
    //严重错误，造成 socket无法改变
    //DataOutputStream dos;

    public RetainSocketThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                byte[] bytes = ByteUtilies.readBytes(socket);
                byte[] decode = ByteUtilies.decode(new String(bytes));
                Message messageFromClient = JSON.parseObject(new String(decode), Message.class);
                //在线用户数量
                if (messageFromClient!=null&&MessageType.ONLINE_USER_COUNT_COMMAND.equals(messageFromClient.getMsgType())) {
                    Message msg = new Message();
                    msg.setMsgType(MessageType.ONLINE_USER_COUNT_SUCCEED);
                    msg.setSendTime(LocalDate.now());
                    msg.setSender("服务器");
                    msg.setReceiver(messageFromClient.getSender());
                    msg.setContent(ManageServiceSocketCollection.getThreadCount());
                    String jsonString = JSON.toJSONString(msg);
                    RetainSocketThread socketThread = ManageServiceSocketCollection.getThreadSocket(messageFromClient.getSender());
                    ByteUtilies.writeBytes(socketThread.getSocket(),jsonString.getBytes());
                } else if (messageFromClient!=null&&MessageType.USER_QUIT_COMMAND.equals(messageFromClient.getMsgType())) {//退出指令
                    RetainSocketThread threadSocket = ManageServiceSocketCollection.getThreadSocket(messageFromClient.getSender());
                    if (threadSocket != null) {
                        ManageServiceSocketCollection.removeSocketThread(messageFromClient.getSender());
                        //服务器退出while循环，走完run方法
                        System.out.println("用户:" + messageFromClient.getSender() + ",退出系统");
                        break;
                    }
                } else if (messageFromClient!=null&&MessageType.PRIVATE_CHAT_COMMAND.equals(messageFromClient.getMsgType())) {
                    RetainSocketThread threadSocket = ManageServiceSocketCollection.getThreadSocket(messageFromClient.getReceiver());
                    Message msg = new Message();
                    msg.setSendTime(LocalDate.now());
                    msg.setContent(messageFromClient.getContent());
                    msg.setSender(messageFromClient.getSender());
                    msg.setReceiver(messageFromClient.getReceiver());
                    msg.setMsgType(MessageType.PRIVATE_CHAT_SUCCEED);
//                    if (dos == null) {
//                        Socket socket = threadSocket.getSocket();
//                        dos = new DataOutputStream(socket.getOutputStream());
//                    }
                    System.out.println("server---"+Thread.currentThread().getName());
                    String jsonString = JSON.toJSONString(msg);
                    ByteUtilies.writeBytes(threadSocket.getSocket(),jsonString.getBytes());
                }else if(messageFromClient!=null&&MessageType.SEND_FILE_COMMAND.equals(messageFromClient.getMsgType())){
                    RetainSocketThread threadSocket = ManageServiceSocketCollection.getThreadSocket(messageFromClient.getReceiver());
                    Message msg = new Message();
                    msg.setSendTime(LocalDate.now());
                    msg.setContent(messageFromClient.getContent());
                    msg.setSender(messageFromClient.getSender());
                    msg.setReceiver(messageFromClient.getReceiver());
                    msg.setMsgType(MessageType.SEND_FILE_SUCCEED);
                    String jsonString = JSON.toJSONString(msg);
                    String encode = ByteUtilies.encode(jsonString.getBytes());
                    ByteUtilies.writeBytes(threadSocket.getSocket(),encode.getBytes());
                }
            }
        } catch (Exception e) {
            System.out.println("连接中断: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }

}
