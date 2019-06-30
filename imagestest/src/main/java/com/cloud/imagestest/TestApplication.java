package com.cloud.imagestest;

import android.app.Application;

import com.cloud.images.RxImage;
import com.cloud.imagestest.images.ImageSuffixCombination;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-06-06
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class TestApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //图片配置
        RxImage.getInstance().getBuilder()
                //用于glide请求远程图片时追加第三方优化后缀(如阿里、七牛等)
                .setOnImageUrlCombinationListener(new ImageSuffixCombination())
                .setImageCacheDirName("images");
    }
}
