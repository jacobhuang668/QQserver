package com.huangjian.utilities;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ManageServiceSocketCollection {
    private static final ConcurrentMap<String, RetainSocketThread> socketThreadMap = new ConcurrentHashMap<>();

    public static void putThreadSocket(String userId, RetainSocketThread thread) {
        socketThreadMap.put(userId, thread);
        System.out.println("Putting userId: " + userId + ", thread: " + thread.hashCode()+",size:"+socketThreadMap.size());

    }

    public static void removeSocketThread(String userId) {
        socketThreadMap.remove(userId);
    }

    public static RetainSocketThread getThreadSocket(String userId) {
        return socketThreadMap.get(userId);
    }

    public static String getThreadCount(){
        StringBuffer sb = new StringBuffer();
        for(Map.Entry<String,RetainSocketThread>entry:socketThreadMap.entrySet()){
            sb.append(entry.getKey()).append(",");
        }
        return  sb.substring(0, sb.length() - 1);
    }

}
