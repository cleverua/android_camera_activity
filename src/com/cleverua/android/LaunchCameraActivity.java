package com.cleverua.android;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class LaunchCameraActivity extends Activity implements OnClickListener {
    private static final String TAG = "Android Camera Activity: LaunchCameraActivity";
    private static final int PICK_PICTURE_CAMERA_REQUEST = 1;
    private static final int IMAGE_SCALE_OPTION = 2;

    private Button launchButton;
    private ImageView imageTaken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        launchButton = (Button) findViewById(R.id.launch_button);
        launchButton.setOnClickListener(this);

        imageTaken = (ImageView) findViewById(R.id.image_taken);
    }
    @Override
    public void onClick(View v) {
        if (v == launchButton) {
            Intent i = new Intent(this, CameraActivity.class);
            startActivityForResult(i, PICK_PICTURE_CAMERA_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PICTURE_CAMERA_REQUEST) {
            Uri imageUri = null;
            if (resultCode == RESULT_OK) {
                imageUri = data.getData();
            } else {
                showAlertDialog(getString(R.string.app_name), "No image taken!");
            }
            setImageTaken(imageUri);
        }
    }

    private void setImageTaken(Uri data) {
        Bitmap b = null;
        if (data != null) {
            try {
                Log.d(TAG, "Going to load image...");
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = IMAGE_SCALE_OPTION; // to save memory
                b = BitmapFactory.decodeStream(getContentResolver().openInputStream(data), null, options);
                Log.d(TAG, "image size: " + b.getWidth() + "x" + b.getHeight());
            } catch (FileNotFoundException e) {
                Log.e(TAG, "failed to open image file: " + e);
                showAlertDialog(getString(R.string.error_label), "Exception caught: " + e);
            }
        }
        imageTaken.setImageBitmap(b);     
    }

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton(R.string.ok_label, null).show();
    }
}