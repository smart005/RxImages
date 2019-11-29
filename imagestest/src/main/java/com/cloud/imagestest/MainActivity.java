package com.cloud.imagestest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.cloud.imagestest.databinding.ActivityMainBinding;
import com.cloud.imagestest.images.ImagesActivity;

public class MainActivity extends Activity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setHandler(this);
    }

    public void OnImageFunctionClick(View view) {
        Intent intent = new Intent();
        intent.setClass(this, ImagesActivity.class);
        startActivity(intent);
    }
}
