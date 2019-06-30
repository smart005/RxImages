package com.cloud.images.view.viewer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.transition.Transition;
import com.cloud.images.glide.GlideOptimize;
import com.cloud.images.view.viewer.PhotoView;
import com.cloud.images.view.viewer.PhotoViewAttacher;
import com.cloud.images.view.viewer.PhotoViewPager;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.events.Action2;
import com.cloud.objects.logs.Logger;
import com.cloud.objects.utils.PixelUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/6/13
 * @Description:大图查看视图
 * @Modifier:
 * @ModifyContent:
 */
public class PicasaView extends RelativeLayout {

    private List<String> imgUrls = new LinkedList<>();
    private PicasaPagerAdapter curradapter = null;
    private int VIEW_PAGE_ID = 2041055581;
    private int currentPosition = 0;
    private OnPhotoViewClickListener onPhotoViewClickListener = null;
    private TextView countTv;

    public PicasaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public interface OnPhotoViewClickListener {
        public void onPhotoViewClick();

        public void onOutsidePhotoTap();
    }

    public void setOnPhotoViewClickListener(OnPhotoViewClickListener listener) {
        this.onPhotoViewClickListener = listener;
    }

    public void setCurrentPosition(int position) {
        try {
            if (position < 0) {
                position = 0;
            }
            if (position > (imgUrls.size() - 1)) {
                position = imgUrls.size() - 1;
            }
            this.currentPosition = position;
            PhotoViewPager viewPager = (PhotoViewPager) findViewById(VIEW_PAGE_ID);
            viewPager.setCurrentItem(this.currentPosition);
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    /**
     * 获取当前显示图片的索引
     *
     * @return position
     */
    public int getCurrentPosition() {
        return this.currentPosition;
    }

    /**
     * 获取当前url
     *
     * @return image url
     */
    public String getCurrentUrl() {
        if (ObjectJudge.isNullOrEmpty(imgUrls)) {
            return "";
        }
        if (currentPosition < 0) {
            currentPosition = 0;
        } else if (currentPosition >= imgUrls.size()) {
            currentPosition = imgUrls.size() - 1;
        }
        return imgUrls.get(currentPosition);
    }

    /**
     * 设置将显示的图片
     *
     * @param urls 图片地址
     */
    public void setImgUrls(String... urls) {
        if (ObjectJudge.isNullOrEmpty(urls)) {
            return;
        }
        for (String url : urls) {
            imgUrls.add(url);
        }
    }

    /**
     * 设置将显示的图片
     *
     * @param urls 图片地址
     */
    public void setImgUrls(Collection<String> urls) {
        if (urls == null) {
            return;
        }
        imgUrls.addAll(urls);
    }

    private void init() {
        this.setBackgroundColor(Color.BLACK);
        this.addView(buildViewPager());
        LayoutParams tvparam = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        tvparam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        tvparam.setMargins(0, 0, 0, PixelUtils.dip2px(getContext(), 60));
        tvparam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        countTv = new TextView(getContext());
        countTv.setTextColor(Color.WHITE);
        countTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        countTv.setGravity(Gravity.CENTER);
        this.addView(countTv, tvparam);
    }

    private ViewPager buildViewPager() {
        LayoutParams vpparam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        PhotoViewPager viewPager = new PhotoViewPager(getContext());
        viewPager.setLayoutParams(vpparam);
        viewPager.setId(VIEW_PAGE_ID);
        curradapter = new PicasaPagerAdapter();
        viewPager.setAdapter(curradapter);
        viewPager.addOnPageChangeListener(vpagelistener);
        return viewPager;
    }

    private OnPageChangeListener vpagelistener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentPosition = position;
            //记录当前显示图片
            if (countTv == null) {
                return;
            }
            countTv.setText(String.format("%s/%s", (position + 1), imgUrls.size()));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private class PicasaPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imgUrls.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            String url = imgUrls.get(position);
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setAllowParentInterceptOnEdge(true);
            final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
            attacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    if (onPhotoViewClickListener != null) {
                        onPhotoViewClickListener.onPhotoViewClick();
                    }
                }

                @Override
                public void onOutsidePhotoTap() {
                    if (onPhotoViewClickListener != null) {
                        onPhotoViewClickListener.onOutsidePhotoTap();
                    }
                }
            });
            container.addView(photoView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            GlideOptimize.with(getContext()).load(url).into(photoView, new Action2<Drawable, Transition<? super Drawable>>() {
                @Override
                public void call(Drawable drawable, Transition<? super Drawable> transition) {
                    attacher.update();
                }
            });
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    /**
     * 懒加载
     */
    public void lazyLoad() {
        curradapter.notifyDataSetChanged();
    }
}
