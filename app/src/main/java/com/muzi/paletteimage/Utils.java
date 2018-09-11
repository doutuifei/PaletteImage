package com.muzi.paletteimage;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: lipeng
 * 时间: 2018/9/11
 * 邮箱: lipeng@moyi365.com
 * 功能:
 */
public class Utils {

    public static int get_major_color(Bitmap bitmap) {
        //色调的总和
        double sum_hue = 0d;
        //色差的阈值
        int threshold = 30;
        //计算色调总和
        for (int h = 0; h < bitmap.getHeight(); h++) {
            for (int w = 0; w < bitmap.getWidth(); w++) {
                int hue = bitmap.getPixel(w, h);
                sum_hue += hue;
            }
        }
        double avg_hue = sum_hue / (bitmap.getWidth() * bitmap.getHeight());

        //色差大于阈值的颜色值
        List<Integer> rgbs = new ArrayList<>();
        for (int h = 0; h < bitmap.getHeight(); h++) {
            for (int w = 0; w < bitmap.getWidth(); w++) {
                int color = bitmap.getPixel(w, h);
                int hue = color;
                //如果色差大于阈值，则加入列表
                if (Math.abs(hue - avg_hue) > threshold) {
                    rgbs.add(color);
                }
            }
        }
        if (rgbs.size() == 0)
            return Color.BLACK;
        //计算列表中的颜色均值，结果即为该图片的主色调
        int sum_r = 0, sum_g = 0, sum_b = 0;
        for (int rgb : rgbs) {
            sum_r += Color.red(rgb);
            sum_g += Color.green(rgb);
            sum_b += Color.blue(rgb);
        }
        return Color.rgb(sum_r / rgbs.size(),
                sum_g / rgbs.size(),
                sum_b / rgbs.size());
    }

}
