package com.neykov;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

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
