package com.applock.photos.videos.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class LockThemeModel implements Parcelable {

    private int id;
    private String title, imageURL;
    private int bgImage;
    private boolean isSelected;

    public LockThemeModel(int id, String title, int bgImage, boolean isSelected) {
        this.id = id;
        this.title = title;
        this.bgImage = bgImage;
        this.isSelected = isSelected;
    }

    public LockThemeModel(int id, String title, String imageURL, boolean isSelected) {
        this.id = id;
        this.title = title;
        this.imageURL = imageURL;
        this.isSelected = isSelected;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBgImage() {
        return bgImage;
    }

    public void setBgImage(int bgImage) {
        this.bgImage = bgImage;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    protected LockThemeModel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        bgImage = in.readInt();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<LockThemeModel> CREATOR = new Creator<LockThemeModel>() {
        @Override
        public LockThemeModel createFromParcel(Parcel in) {
            return new LockThemeModel(in);
        }

        @Override
        public LockThemeModel[] newArray(int size) {
            return new LockThemeModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(title);
        dest.writeInt(bgImage);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}
