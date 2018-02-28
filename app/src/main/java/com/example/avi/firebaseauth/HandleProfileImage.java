package com.example.avi.firebaseauth;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by AVI on 2/14/2018.
 */

public class HandleProfileImage extends AppCompatActivity {

    public String saveToInternalStorage(Bitmap bitmapImage) {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        File file;
        FileOutputStream fileoutputstream;

        bitmapImage.compress(Bitmap.CompressFormat.PNG, 60, bytearrayoutputstream);
        file = new File(Environment.getExternalStorageDirectory() + "/SampleImage.png");

        try {
            System.out.println("fil is "+ file);
            file.createNewFile();
            fileoutputstream = new FileOutputStream(file);
            fileoutputstream.write(bytearrayoutputstream.toByteArray());
            fileoutputstream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), "Image Saved Successfully", Toast.LENGTH_LONG).show();

        return file.getAbsolutePath();
}

    private void loadImageFromStorage(String path) {
        try {
            File f = new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img = (ImageView) findViewById(R.id.img_selectImage);
            img.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
