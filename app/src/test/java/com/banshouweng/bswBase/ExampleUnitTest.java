package com.banshouweng.bswBase;

import com.banshouweng.bswBase.utils.CommonUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void listReplace() {
        final List<String> stringList = new ArrayList<>();
        stringList.add("a");
        stringList.add("b");
        stringList.add("c");
        stringList.add("d");
        stringList.add("e");

        final List<String> replaceStringList = new ArrayList<>();
        replaceStringList.add("1");
        replaceStringList.add("2");
        replaceStringList.add("3");

        int stringListSize = stringList.size();

        System.out.println("listReplace   1    " + stringList);

        for (int i = 1; i <= replaceStringList.size(); i++) {
            stringList.remove(stringListSize - i);
        }

        System.out.println("listReplace   2    " + stringList);

        stringList.addAll(replaceStringList);
        System.out.println("listReplace   3    " + stringList);
    }

    @Test
    public void linePrint() {
        LinePrintUtils.print("行号打印测试");
    }

//    @Test
//    public void replaceTest() {
//        List<Integer> beforeData = new ArrayList<>();
//        List<Integer> mData = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            beforeData.add(i);
//            if (i > 2) {
//                continue;
//            }
//            mData.add(i);
//        }
//        int pageSize = 3;
//        int pageNumber = 2;
//        int dataSize = CommonUtils.judgeListNull(mData);
//        int startPosition = pageSize * (pageNumber - 1);
//        for (int i = startPosition; i < startPosition + dataSize; i++) {
//            beforeData.remove(i);
//            beforeData.add(i, mData.get(i - startPosition));
//        }
//        System.out.println(beforeData);
//    }

    @Test
    public void replaceTest() {
        List<Integer> beforeData = new ArrayList<>();
        List<Integer> mData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            beforeData.add(i);
            if (i > 2) {
                continue;
            }
            mData.add(i);
        }
        int pageSize = 3;
        int pageNumber = 2;
        int dataSize = CommonUtils.judgeListNull(beforeData);
        int startPosition = pageSize * (pageNumber - 1);
        for (int i = dataSize - 1; i >= startPosition; i--) {
            beforeData.remove(i);
        }
        beforeData.addAll(mData);
        System.out.println(beforeData);
    }
}