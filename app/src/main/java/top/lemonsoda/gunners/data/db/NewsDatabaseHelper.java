package top.lemonsoda.gunners.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import top.lemonsoda.gunners.data.module.News;

/**
 * Created by chuanl on 2/24/17.
 */

public class NewsDatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = NewsDatabaseHelper.class.getCanonicalName();
    private static final String DATABASE_NAME = "arsenal-news.db";
    private static final int DATABASE_VERSION = 1;

    private static volatile NewsDatabaseHelper instance;

    public NewsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, News.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, News.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static NewsDatabaseHelper getInstance(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (NewsDatabaseHelper.class) {
                if (instance == null) {
                    instance = new NewsDatabaseHelper(context);
                }
            }
        }
        return instance;
    }

    public <D extends Dao<T, ?>, T> D getNewsDao(Class<T> clazz) {
        try {
            return getDao(clazz);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() {
        super.close();
        DaoManager.clearCache();
    }
}
