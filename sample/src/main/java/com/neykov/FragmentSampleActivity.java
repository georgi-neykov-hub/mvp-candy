package com.neykov;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.neykov.mvp.sample.R;

public class FragmentSampleActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_sample);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(new SampleFragment(), "tag")
                    .commitNow();
        }
    }
}
