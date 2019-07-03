package com.cloud.images.crop.animation;

public interface SimpleValueAnimatorListener {
    void onAnimationStarted();

    void onAnimationUpdated(float scale);

    void onAnimationFinished();
}
