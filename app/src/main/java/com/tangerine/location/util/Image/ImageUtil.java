package com.tangerine.location.util.Image;


import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;

public class ImageUtil {
    private static ImageUtil instance;
    private ImageUtil (){

    }
    public static ImageUtil getInstance(){
        if(instance == null){
            synchronized (ImageUtil.class){
                if (instance == null){
                    instance = new ImageUtil();
                }

            }
        }
        return instance;
    }
    public void loadImageView(Context context , String path, AppCompatImageView imageView){
        try{
            Glide.with(context).load(path).into(imageView);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
