package top.lemonsoda.gunners.data.module;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by chuanl on 2/10/17.
 */

@DatabaseTable(tableName = "tb_news")
public class News implements Parcelable {

    @DatabaseField(columnName = "fullTextUrl")
    private String fullTextUrl;
    @DatabaseField(columnName = "header")
    private String header;
    @DatabaseField(columnName = "content")
    private String content;
    @DatabaseField(columnName = "thumbnail")
    private String thumbnail;
    @DatabaseField(columnName = "source")
    private String source;
    @DatabaseField(columnName = "articleId", id = true)
    private String articleId;

    public String getFullTextUrl() {
        return fullTextUrl;
    }

    public void setFullTextUrl(String fullTextUrl) {
        this.fullTextUrl = fullTextUrl;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fullTextUrl);
        dest.writeString(this.header);
        dest.writeString(this.content);
        dest.writeString(this.thumbnail);
        dest.writeString(this.source);
        dest.writeString(this.articleId);
    }

    public News() {
    }

    protected News(Parcel in) {
        this.fullTextUrl = in.readString();
        this.header = in.readString();
        this.content = in.readString();
        this.thumbnail = in.readString();
        this.source = in.readString();
        this.articleId = in.readString();
    }

    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
        @Override
        public News createFromParcel(Parcel source) {
            return new News(source);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };
}
