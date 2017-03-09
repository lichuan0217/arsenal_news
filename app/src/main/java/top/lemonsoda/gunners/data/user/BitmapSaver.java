package top.lemonsoda.gunners.data.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Chuan on 09/03/2017.
 */

public class BitmapSaver {

    private static final String TAG = BitmapSaver.class.getCanonicalName();

    private String dirName = "images";
    private String fileName = "image.png";
    private Context context;
    private static BitmapSaver instance;

    public static BitmapSaver getInstance(Context context) {
        if (instance == null) {
            instance = new BitmapSaver(context);
        }
        return instance;
    }

    private BitmapSaver(Context cxt) {
        context = cxt;
    }

    public BitmapSaver setDirName(String dirName) {
        this.dirName = dirName;
        return this;
    }

    public BitmapSaver setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void save(Bitmap bitmap) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(createFile());
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Bitmap load() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(createFile());
            return BitmapFactory.decodeStream(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public Observable<Bitmap> loadBitmap() {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bitmap = load();
                subscriber.onNext(bitmap);
                subscriber.onCompleted();
            }
        });
    }

    public void delete() {
        File file = createFile();
        if (file.exists())
            file.delete();
    }

    public boolean exists() {
        File file = createFile();
        Log.d(TAG, "Name: " + file.getName());
        Log.d(TAG, "Exist: " + file.exists());
        return createFile().exists();
    }

    private File createFile() {
        File dir = context.getDir(dirName, Context.MODE_PRIVATE);
        return new File(dir, fileName);
    }
}
