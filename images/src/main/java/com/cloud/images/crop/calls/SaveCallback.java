package com.cloud.images.crop.calls;

import android.net.Uri;

public interface SaveCallback extends Callback {
    void onSuccess(Uri uri);
}