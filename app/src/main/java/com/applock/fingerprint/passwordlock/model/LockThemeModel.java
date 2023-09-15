package com.applock.fingerprint.passwordlock.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LockThemeModel implements Parcelable {

    private int id;
    private String title, imageURL;
    private boolean isSelected;
    @SerializedName("themes")
    private List<LockThemeModel> themes;

    public LockThemeModel( String title, String imageURL, boolean isSelected) {
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public List<LockThemeModel> getThemes() {
        return themes;
    }

    public void setThemes(List<LockThemeModel> themes) {
        this.themes = themes;
    }

    protected LockThemeModel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        imageURL = in.readString();
        isSelected = in.readByte() != 0;
        themes = in.createTypedArrayList(LockThemeModel.CREATOR);
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
        dest.writeString(imageURL);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeTypedList(themes);
    }
}
