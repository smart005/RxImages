package com.cloud.images.crop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.cloud.ebus.EBus;
import com.cloud.images.MultiImageSelectorActivity;
import com.cloud.images.R;
import com.cloud.images.crop.calls.CropCallback;
import com.cloud.images.crop.calls.LoadCallback;
import com.cloud.images.crop.calls.SaveCallback;
import com.cloud.images.databinding.ClCropActivityCropBinding;
import com.cloud.objects.bases.BundleData;
import com.cloud.objects.logs.Logger;

import java.io.File;
import java.util.ArrayList;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-07-03
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class CropImageActivity extends Activity {

    private ClCropActivityCropBinding binding;
    private RectF mFrameRect = null;
    private Uri sourceUri;
    private Uri destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.cl_crop_activity_crop);
        binding.setHandler(this);
        init();
    }

    private void init() {
        try {
            BundleData bundleData = new BundleData(getIntent());
            String imagePath = bundleData.getStringBundle("srcImg");
            File file = new File(imagePath);
            sourceUri = Uri.fromFile(file);
            String destinationPath = bundleData.getStringBundle("destination");
            File destinationFile = new File(destinationPath);
            destination = Uri.fromFile(destinationFile);
            binding.cropImageCiv.setCropMode(CropImageView.CropMode.RATIO_4_3);
            binding.cropImageCiv.load(sourceUri).execute(mLoadCallback);
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    public void onCancelClick(View view) {
        //删除空文件
        try {
            if (destination != null && destination != Uri.EMPTY) {
                String path = destination.getPath();
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            Logger.error(e);
        }
        finish();
    }

    public void onCropClick(View view) {
        binding.cropImageCiv.crop(sourceUri).execute(mCropCallback);
    }

    private final LoadCallback mLoadCallback = new LoadCallback() {
        @Override
        public void onSuccess() {
        }

        @Override
        public void onError(Throwable e) {
        }
    };

    private final CropCallback mCropCallback = new CropCallback() {
        @Override
        public void onSuccess(Bitmap cropped) {
            binding.cropImageCiv.save(cropped)
                    .compressFormat(Bitmap.CompressFormat.JPEG)
                    .execute(destination, mSaveCallback);
        }

        @Override
        public void onError(Throwable e) {

        }
    };

    private final SaveCallback mSaveCallback = new SaveCallback() {
        @Override
        public void onSuccess(Uri outputUri) {
            //关闭图片选择页面
            BundleData bundleData = new BundleData(getIntent());
            boolean isTailoring = bundleData.getBooleanBundle("isTailoring");
            if (isTailoring) {
                EBus.getInstance().post("$_close_image_select_page");
            }
            ArrayList<String> paths = new ArrayList<>();
            paths.add(outputUri.getPath());
            Intent intent = new Intent();
            intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT, paths);
            EBus.getInstance().post("$_tailoring_request_complete", intent);
            finish();
        }

        @Override
        public void onError(Throwable e) {

        }
    };
}
