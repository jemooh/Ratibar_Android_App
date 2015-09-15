package com.br.timetabler.model;

import android.graphics.Bitmap;

public class Rowdownload {
 
    private Bitmap bitmapImage;
     
    public Rowdownload(Bitmap bitmapImage) {
        this.bitmapImage =  bitmapImage;
    }
 
    public Bitmap getBitmapImage() {
        return bitmapImage;
    }
 
    public void setBitmapImage(Bitmap bitmapImage) {
        this.bitmapImage = bitmapImage;
    }      
}