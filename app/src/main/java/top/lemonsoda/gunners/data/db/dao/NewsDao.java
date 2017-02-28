package top.lemonsoda.gunners.data.db.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import top.lemonsoda.gunners.data.db.NewsDatabaseHelper;
import top.lemonsoda.gunners.data.module.News;

/**
 * Created by chuanl on 2/24/17.
 */

public class NewsDao {

    private static final String TAG = NewsDao.class.getCanonicalName();

    private Context context;
    private Dao<News, String> newsDaoOperation;
    private NewsDatabaseHelper helper;

    public NewsDao(Context context) {
        this.context = context;
        helper = NewsDatabaseHelper.getInstance(context);
        this.newsDaoOperation = helper.getNewsDao(News.class);
    }

    public List<News> queryNews() throws SQLException {
        List<News> newsList = newsDaoOperation.queryForAll();
        Log.d(TAG, "QueryNews: NewsList ---> " + newsList.size());
//        for (News news: newsList) {
//            Log.d(TAG, "item: " + news.getHeader());
//        }
        return newsList;
    }

    public void updateNews(List<News> newsList) throws SQLException {

        // Delete the old data
        helper.clearTable();

        // Insert the new data
        for (News news : newsList) {
            newsDaoOperation.create(news);
        }
    }
}
