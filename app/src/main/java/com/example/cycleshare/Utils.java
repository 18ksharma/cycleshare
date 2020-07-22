package com.example.cycleshare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import java.io.File;

import static android.app.Activity.RESULT_OK;
public class Utils {

    public static final String TAG = "Utils";

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    //Variable for image
    public static File photoFile;
    public static String photoFileName = "photo.jpg";


    private Utils() {
    }


}
