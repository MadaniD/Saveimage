package com.example.saveimage;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {
    private String file;
    private String path;
    public String state;
    private String imgName = "picture.jpg ";
    private Button button1;
    private Button button2;
    public ImageView imageView;
    private final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private final int REQUEST_PERMISSION = 1000;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.button1 = findViewById(R.id.button1);
        this.button2 = findViewById(R.id.button2);
        this.imageView = findViewById(R.id.imageView);
        this.path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        this.file = this.path + "/" + this.imgName;

        checkPermission();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission(){
        if (isGranted()){
            setEvent();
        }
        else {
            requestPermissions(PERMISSIONS, REQUEST_PERMISSION);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isGranted(){
        for (int i = 0; i < PERMISSIONS.length; i++){
            //La première fois, c'est PERMISSION_Retours REFUSÉS
            if (checkSelfPermission(PERMISSIONS[i]) != PackageManager.PERMISSION_GRANTED) {
                //Renvoie true si la demande est rejetée une fois. Renvoie false pour la première fois ou lorsque "Ne plus afficher" est sélectionné.
                if (shouldShowRequestPermissionRationale(PERMISSIONS[i])) {
                    Toast.makeText(this, "Autorisation requise pour exécuter l'application", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION){
            checkPermission();
        }
    }


    private void setEvent(){
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)){
                    saveFile(file);
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)){
                    readFile(file);
                }
            }
        });
    }


    private void saveFile(String file){
        try {
            AssetManager assetManager = getResources().getAssets();
            InputStream inputStream = assetManager.open(imgName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            while (true){
                int len = inputStream.read(buffer);
                if (len < 0){
                    break;
                }
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void readFile(String file){
        try {
            InputStream inputStream = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}