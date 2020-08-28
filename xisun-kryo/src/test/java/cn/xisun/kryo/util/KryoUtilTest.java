package cn.xisun.kryo.util;

import cn.xisun.kryo.pojo.SomeClass;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author XiSun
 * @Date 2020/8/12 9:33
 */
class KryoUtilTest {
    @Test
    void testByte() throws IOException {
        SomeClass someClass = new SomeClass();
        someClass.setValue("Hello, kryo!");

        byte[] bytes = KryoUtil.writeObjectToByteArray(someClass);
        SomeClass o = KryoUtil.readObjectFromByteArray(bytes, SomeClass.class);
        System.out.println("o.value = " + o.getValue());// o.value = Hello, kryo!

        someClass.setValue("Hello, kryo2!");
        byte[] bytes1 = KryoUtil.writeToByteArray(someClass);
        SomeClass o1 = KryoUtil.readFromByteArray(bytes1);
        System.out.println("o1.value = " + o1.getValue());// o1.value = Hello, kryo2!
    }

    @Test
    void testString() throws IOException {
        SomeClass someClass = new SomeClass();
        someClass.setValue("Hello, kryo!");

        String string = KryoUtil.writeToString(someClass);
        System.out.println("string = " + string);// string = AQBjbi54aXN1bi5rcnlvLnBvam8uU29tZUNsYXPzAQFIZWxsbywga3J5b6E=
        SomeClass o = KryoUtil.readFromString(string);
        System.out.println("o.getValue() = " + o.getValue());// o.getValue() = Hello, kryo!

        String string1 = KryoUtil.writeObjectToString(someClass);
        System.out.println("string1 = " + string1);// string1 = AQFIZWxsbywga3J5b6E=
        SomeClass o1 = KryoUtil.readObjectFromString(string1, SomeClass.class);
        System.out.println("o1.getValue() = " + o1.getValue());// o1.getValue() = Hello, kryo!
    }
}