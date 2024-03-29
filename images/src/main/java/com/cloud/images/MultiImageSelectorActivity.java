package com.cloud.images;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;

import com.cloud.coms.events.OnThemeViewClickListener;
import com.cloud.coms.themes.ActionType;
import com.cloud.ebus.EBus;
import com.cloud.ebus.SubscribeEBus;
import com.cloud.images.databinding.ClMisActivityDefaultBinding;
import com.cloud.objects.bases.BundleData;
import com.cloud.objects.utils.GlobalUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Multi image selector
 * Created by Nereo on 2015/4/7.
 * Updated by nereo on 2016/1/19.
 * Updated by nereo on 2016/5/18.
 */
public class MultiImageSelectorActivity extends FragmentActivity
        implements MultiImageSelectorFragment.Callback, OnThemeViewClickListener {

    // Single choice
    public static final int MODE_SINGLE = 0;
    // Multi choice
    public static final int MODE_MULTI = 1;

    /**
     * Max image size，int，{@link #DEFAULT_IMAGE_SIZE} by default
     */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /**
     * Select mode，{@link #MODE_MULTI} by default
     */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /**
     * Whether show camera，true by default
     */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /**
     * Result data set，ArrayList&lt;String&gt;
     */
    public static final String EXTRA_RESULT = "select_result";
    /**
     * Original data set
     */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";
    // Default image size
    private static final int DEFAULT_IMAGE_SIZE = 9;

    private ArrayList<String> resultList = new ArrayList<>();
    private int mDefaultCount = DEFAULT_IMAGE_SIZE;
    private ClMisActivityDefaultBinding binding = null;
    //页面标识
    private String imageSelectorPageTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.cl_mis_activity_default);
        binding.headTtv.setOnThemeViewClickListener(this);

        //android 7.0系统解决拍照的问题
        //https://blog.csdn.net/guiping_ding/article/details/78502290
        if (Build.VERSION.SDK_INT >= 18) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }

        final Intent intent = getIntent();
        mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, DEFAULT_IMAGE_SIZE);
        final int mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
        final boolean isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        if (mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
            resultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
        }

        if (mode == MODE_MULTI) {
            updateDoneText(resultList);
            binding.headTtv.setRightViewVisibility(View.VISIBLE);
        } else {
            binding.headTtv.setRightViewVisibility(View.GONE);
        }

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount);
            bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
            bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
            bundle.putStringArrayList(MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST, resultList);
            BundleData bundleData = new BundleData(getIntent());
            bundle.putBoolean("isTailoring", bundleData.getBooleanBundle("isTailoring"));

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.image_grid, Fragment.instantiate(this, MultiImageSelectorFragment.class.getName(), bundle))
                    .commit();
        }
        imageSelectorPageTag = GlobalUtils.getNewGuid();
        EBus.getInstance().registered(this, imageSelectorPageTag);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EBus.getInstance().unregister(this, imageSelectorPageTag);
    }

    @Override
    public void onThemeViewClick(View view, int id, ActionType actionType, String iconTag) {
        if (actionType == ActionType.leftView) {
            finish();
        } else if (actionType == ActionType.rightView) {
            if (resultList != null && resultList.size() > 0) {
                // Notify success
                Intent data = new Intent();
                data.putStringArrayListExtra(EXTRA_RESULT, resultList);
                setResult(RESULT_OK, data);
            } else {
                setResult(RESULT_CANCELED);
            }
            finish();
        }
    }

    @SubscribeEBus(receiveKey = "$_close_image_select_page")
    public void onCloseImageSelector() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Update done button by select image data
     *
     * @param resultList selected image data
     */
    private void updateDoneText(ArrayList<String> resultList) {
        int size = 0;
        if (resultList == null || resultList.size() <= 0) {
            //%1$s(%2$d/%3$d)
            binding.headTtv.setRightViewEnable(false);
        } else {
            size = resultList.size();
            binding.headTtv.setRightViewEnable(true);
        }
        binding.headTtv.setRightViewText(String.format("完成(%s/%s)", size, mDefaultCount));
    }

    @Override
    public void onSingleImageSelected(String path) {
        resultList.clear();
        Intent data = new Intent();
        resultList.add(path);
        data.putStringArrayListExtra(EXTRA_RESULT, resultList);
        BundleData bundleData = new BundleData(getIntent());
        boolean isTailoring = bundleData.getBooleanBundle("isTailoring");
        if (isTailoring) {
            EBus.getInstance().post("$_image_sel_tailoring_bus", data);
        } else {
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void onImageSelected(String path) {
        if (!resultList.contains(path)) {
            resultList.add(path);
        }
        updateDoneText(resultList);
    }

    @Override
    public void onImageUnselected(String path) {
        if (resultList.contains(path)) {
            resultList.remove(path);
        }
        updateDoneText(resultList);
    }

    @Override
    public void onCameraShot(File imageFile) {
        if (imageFile != null) {
            // notify system the image has change
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)));

            Intent data = new Intent();
            resultList.add(imageFile.getAbsolutePath());
            data.putStringArrayListExtra(EXTRA_RESULT, resultList);
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
