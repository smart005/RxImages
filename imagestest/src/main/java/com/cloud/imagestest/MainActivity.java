package com.cloud.imagestest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cloud.imagestest.images.ImagesActivity;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnImageFunctionClick(View view) {
        Intent intent = new Intent();
        intent.setClass(this, ImagesActivity.class);
        startActivity(intent);
    }
}
