package com.banshouweng.bswBase;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.banshouweng.bswBase.ui.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
//    @Test
//    public void useAppContext() throws Exception {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getTargetContext();
//
//        assertEquals("com.banshouweng.mybaseapplication", appContext.getPackageName());
//    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule=
            new ActivityTestRule<MainActivity>(MainActivity.class){
                // 传参
                @Override
                protected Intent getActivityIntent() {
                    Intent intent=new Intent();
                    intent.putExtra("name","MyName");
                    return intent;
                }

//                /**
//                 * Activity运行之前调用
//                 */
//                @Override
//                protected void beforeActivityLaunched() {
//                    mHttpClient.setClient(new HttpURLConnectionClient());
//                }
            };


    @Test
    public void mTest() throws Exception{

        Thread.sleep(5000);
    }
}
