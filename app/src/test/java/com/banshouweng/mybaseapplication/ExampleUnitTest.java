package com.banshouweng.mybaseapplication;

import android.gesture.Prediction;

import com.banshouweng.mybaseapplication.utils.Logger;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

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
}