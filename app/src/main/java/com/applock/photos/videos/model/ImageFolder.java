package com.applock.photos.videos.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ImageFolder implements Serializable {

    private String bucketId;
    private String bucketName;
    private String imagePath;
    private String dateTaken;
    private int imageCount;
    private ArrayList<String> imagePaths;

    public ImageFolder(String bucketId, String bucketName, String imagePath, String dateTaken, int imageCount, ArrayList<String> imagePaths) {
        this.bucketId = bucketId;
        this.bucketName = bucketName;
        this.imagePath = imagePath;
        this.dateTaken = dateTaken;
        this.imageCount = imageCount;
        this.imagePaths = imagePaths;
    }

    public ArrayList<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(ArrayList<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public void addImagePath(String imagePath) {
        imagePaths.add(imagePath);
    }

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(String dateTaken) {
        this.dateTaken = dateTaken;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

}
