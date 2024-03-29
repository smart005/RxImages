package com.cloud.imagestest.images;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.cloud.images.RxImage;
import com.cloud.images.beans.SelectImageProperties;
import com.cloud.images.figureset.ImageSelectDialog;
import com.cloud.imagestest.R;
import com.cloud.imagestest.databinding.ImagesViewBinding;
import com.cloud.objects.manager.ObjectManager;
import com.cloud.objects.utils.ResUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/8
 * Description:图片相关功能
 * Modifier:
 * ModifyContent:
 */
public class ImagesActivity extends FragmentActivity {

    private ImagesViewBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.images_view);
        binding.setHandler(this);

        glideLoadImage();
    }

    private void glideLoadImage() {
        String url = "http://img2.imgtn.bdimg.com/it/u=3685170612,1820641236&fm=26&gp=0.jpg";
        String url2 = "http://pic206.nipic.com/pic/20190219/22116547_213112169000_4.jpg";
        String url3 = "http://img1.imgtn.bdimg.com/it/u=873265023,1618187578&fm=26&gp=0.jpg";
        String gifurl = "http://img.zcool.cn/community/0128ee5bc73ab5a8012099c8745c1a.gif";
        File dir = RxImage.getInstance().getBuilder().getImageCacheDir();
        File file = new File(dir, "test_image.jpg");
        String uri = ResUtils.getResourcesUri(this, R.drawable.timg);
//        GlideOptimize.with(this)
//                .load(gifurl)
//                //可自定义宽度和高度，不设置默认取控件的宽高;
////                .setWidth(100)
////                .setHeight(200)
//                //glide图片加载完成之前的占位图片
//                //未设置时取RxImage.setDefImage()设置的默认图片
//                //.setPlaceholder(R.drawable.def_image)
//                //图片加载过程中优化级(一般用于比较重要位置或优化要显示的图片)
//                .setPriority(Priority.HIGH)
//                //true-将图片转为正圆再渲染;false-不作转换处理;
//                //对于网络图片若尺寸或文件大小太大会导致不能转成圆形
////                .setRound(false)
//                //先加载相对于原图的缩放比例(按宽高比)的缩略图
////                .setThumbnailScale(0.5f)
//                //具体说明看{@link ScaleType}
//                .setScaleType(ScaleType.fitCenter)
//                //图片圆角弧度(由第三方规则处理)
//                .setRoundCorners(20)
//                //图片规则(根据第三方文档设定,如阿里、七牛)
//                .setImageRule(ImgRuleType.GeometricForWidth.getRule())
//                //缓存模式
//                .setCacheMode(CacheMode.onlyMemory)
//                //散列key,适用于请求url不变但图片已更新情况
//                //.setHashKey(GlobalUtils.getNewGuid())
//                //gif图片需要设置此属性
//                .asGif()
//                .into(binding.testIv1);
        Glide.with(this).load(gifurl).into(binding.testIv1);
        //所有的参数配置与上面一样
//        GlideOptimize.with(this)
//                .load(url2)
//                .into(new GBitmapCallback() {
//                    @Override
//                    public void call(Bitmap bitmap) {
//                        binding.testIv1.setImageBitmap(bitmap);
//                    }
//                });
        //文件类型
//        GlideOptimize.with(this)
//                .load(url)
//                //将文件移动至此目录下,如果不设置则为glide缓存默认路径
//                .toMove(DirectoryNames.forum.name())
//                .into(new GFileCallback<File>() {
//                    @Override
//                    public void call(File file) {
//                        //这里做移动文件、加载等操作
//                    }
//                });
    }

    //图片选择
    public void OnImageSelectClick(View view) {
        //选择后图片最大压缩大小
        imageSelectDialog.setMaxFileSize(1024);
        //最多选择图片数量
        imageSelectDialog.setMaxSelectNumber(6);
        //是否显示拍照选项
        imageSelectDialog.setShowTakingPictures(false);
        //选择后压缩图片最大宽度
        imageSelectDialog.setMaxImageWidth(720);
        //选择后压缩图片最大高度
        imageSelectDialog.setMaxImageHeight(1920);
        //选择图片后是否进行裁剪处理
        imageSelectDialog.setTailoring(false);
        //设置裁剪最大宽高
        int screenWidth = ObjectManager.getScreenWidth(this) * 2;
        int height = screenWidth * 83 / 345;
//        imageSelectDialog.withMaxSize(screenWidth, height);
        //设置裁剪宽高比
        imageSelectDialog.withAspect(16, 9);
        //显示图片选择
        imageSelectDialog.show(this);
    }

    //直接显示拍照窗口
    public void OnTakingClick(View view) {
        //选择后图片最大压缩大小
        imageSelectDialog.setMaxFileSize(1024);
        //选择后压缩图片最大宽度
        imageSelectDialog.setMaxImageWidth(720);
        //选择后压缩图片最大高度
        imageSelectDialog.setMaxImageHeight(1920);
        //选择图片后是否进行裁剪处理
        imageSelectDialog.setTailoring(true);
        //设置裁剪最大宽高
        int screenWidth = ObjectManager.getScreenWidth(this) * 2;
        int height = screenWidth * 83 / 345;
        imageSelectDialog.withMaxSize(screenWidth, height);
        //设置裁剪宽高比
        imageSelectDialog.withAspect(345, 83);
        //显示图片选择
        imageSelectDialog.showTaking(this);
    }

    //图片选择+拍照
    public void OnImageSelectTakingClick(View view) {
//        //选择后图片最大压缩大小
//        imageSelectDialog.setMaxFileSize(1024);
//        //最多选择图片数量
//        imageSelectDialog.setMaxSelectNumber(1);
//        //是否显示拍照选项
//        imageSelectDialog.setShowTakingPictures(true);
//        //选择后压缩图片最大宽度
//        imageSelectDialog.setMaxImageWidth(720);
//        //选择后压缩图片最大高度
//        imageSelectDialog.setMaxImageHeight(1920);
//        //选择图片后是否进行裁剪处理
//        imageSelectDialog.setTailoring(true);
//        //设置裁剪最大宽高
//        int screenWidth = ObjectManager.getScreenWidth(this) * 2;
//        int height = screenWidth * 83 / 345;
//        imageSelectDialog.withMaxSize(screenWidth, height);
//        //设置裁剪宽高比
//        imageSelectDialog.withAspect(345, 83);
//        //扩展数据，选择时传入可以回调中取出;
//        imageSelectDialog.setExtra(null);
//        //显示图片选择
//        imageSelectDialog.show(this);

        int screenWidth = ObjectManager.getScreenWidth(this);
        int screenHeight = ObjectManager.getScreenHeight(this);
        File file = new File("/storage/emulated/0/cloudapp/images/1111.jpg");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File srcfile = new File("/storage/emulated/0/Mob/com.changshuo.ui/cache/images/utf-8' '13505285248872121122463103.thumb.jpg");
    }

    private ImageSelectDialog imageSelectDialog = new ImageSelectDialog() {
        @Override
        protected void onSelectCompleted(List<SelectImageProperties> selectImageProperties, Object extra) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageSelectDialog.onActivityResult(this, requestCode, resultCode, data);
    }
}
