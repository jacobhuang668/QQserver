package com.huangjian.qqcommon;

import com.alibaba.fastjson.JSON;
import com.huangjian.utilities.ByteUtilies;
import com.huangjian.utilities.ManageServiceSocketCollection;
import com.huangjian.utilities.RetainSocketThread;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class NoticeThread extends Thread {
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        while (true) {
            System.out.println("请输入通告内容：");
            String line = scanner.nextLine();
            List<RetainSocketThread> allSocket = ManageServiceSocketCollection.getAllSocket();
            for (RetainSocketThread socketThread : allSocket) {
                Message message = new Message();
                message.setMsgType(MessageType.RECIVER_SERVER_MESSAGE);
                message.setContent(line);
                message.setSendTime(LocalDate.now());
                String jsonString = JSON.toJSONString(message);
                String encode;
                try {
                    encode = ByteUtilies.encode(jsonString.getBytes());
                    ByteUtilies.writeBytes(socketThread.getSocket(), encode.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
