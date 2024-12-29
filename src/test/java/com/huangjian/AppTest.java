package com.huangjian;

import com.alibaba.fastjson.JSON;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() throws Exception{
        Gerneric<Integer> gerneric = new Gerneric<>();
        gerneric.setName("huangjian");
        gerneric.setContent(3);
        String jsonString = JSON.toJSONString(gerneric);
        System.out.println(jsonString);

        Gerneric gerneric1 = JSON.parseObject(jsonString, Gerneric.class);
        System.out.println(gerneric1);

    }
}

class Gerneric<T> {
    private T content;
    private String name;

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Gerneric{" +
                "content=" + content +
                ", name='" + name + '\'' +
                '}';
    }
}