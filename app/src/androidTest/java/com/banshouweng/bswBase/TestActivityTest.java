package com.banshouweng.bswBase;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.banshouweng.bswBase.ui.activity.TestActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

/**
 * @author leiming
 * @date 2018/7/10.
 */
@RunWith(AndroidJUnit4.class)
public class TestActivityTest {
    @Rule
    public final ActivityTestRule activityTestRule =
            new ActivityTestRule<>(TestActivity.class, false, false);

    @Test
    public void blockingTest() throws Exception {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("title", "TestActivityTest");
        intent.putExtra("bundle", bundle);
        activityTestRule.launchActivity(intent);
        CountDownLatch countdown = new CountDownLatch(1);
        countdown.await();
    }
}
