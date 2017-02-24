package top.lemonsoda.gunners.data.module;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chuanl on 2/21/17.
 */

public class NewsDetail implements Parcelable {

    private String picture_src;
    private String source;
    private String content;
    private String header;
    private String editor;
    private String date;
    private String type;
    private String video;
    private Boolean favorite;

    public String getPicture_src() {
        return picture_src;
    }

    public void setPicture_src(String picture_src) {
        this.picture_src = picture_src;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.picture_src);
        dest.writeString(this.source);
        dest.writeString(this.content);
        dest.writeString(this.header);
        dest.writeString(this.editor);
        dest.writeString(this.date);
        dest.writeString(this.type);
        dest.writeString(this.video);
        dest.writeValue(this.favorite);
    }

    public NewsDetail() {
    }

    protected NewsDetail(Parcel in) {
        this.picture_src = in.readString();
        this.source = in.readString();
        this.content = in.readString();
        this.header = in.readString();
        this.editor = in.readString();
        this.date = in.readString();
        this.type = in.readString();
        this.video = in.readString();
        this.favorite = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<NewsDetail> CREATOR = new Parcelable.Creator<NewsDetail>() {
        @Override
        public NewsDetail createFromParcel(Parcel source) {
            return new NewsDetail(source);
        }

        @Override
        public NewsDetail[] newArray(int size) {
            return new NewsDetail[size];
        }
    };
}
