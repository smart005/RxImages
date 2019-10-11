package com.cloud.images.glide;

import android.text.TextUtils;

import com.cloud.images.RxImage;
import com.cloud.images.enums.GlideRequestType;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/9
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class CusGlideUrl {

    private ImageRuleProperties properties = null;
    private String url = "";
    private String originalUrl = "";

    public CusGlideUrl(String url) {
        this.url = url;
        this.originalUrl = url;
    }

    public void setProperties(ImageRuleProperties properties) {
        this.properties = properties;
    }

    public String getOriginalUrl() {
        return this.originalUrl;
    }

    public String getUrl() {
        //如果gif图片或本地图片则用默认地址
        if (properties == null ||
                properties.isGif() ||
                properties.getImageType() == GlideRequestType.fileImage ||
                properties.getImageType() == GlideRequestType.resImage ||
                properties.getImageType() == GlideRequestType.uriImage) {
            return this.url;
        }
        RxImage.ImagesBuilder builder = RxImage.getInstance().getBuilder();
        OnImageUrlCombinationListener combinationListener = builder.getOnImageUrlCombinationListener();
        if (combinationListener != null && properties.getWidth() > 0 && properties.getHeight() > 0) {
            String url = combinationListener.onUrlCombination(this.url, properties);
            if (TextUtils.isEmpty(url)) {
                return this.url;
            } else {
                return url;
            }
        } else {
            return this.url;
        }
    }
}
