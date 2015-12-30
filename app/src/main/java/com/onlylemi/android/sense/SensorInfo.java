package com.onlylemi.android.sense;

/**
 * Created by only乐秘 on 2015-12-30.
 */
public class SensorInfo {

    private String name;
    private int imageId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return "SensorInfo{" +
                "name='" + name + '\'' +
                ", imageId=" + imageId +
                '}';
    }
}
