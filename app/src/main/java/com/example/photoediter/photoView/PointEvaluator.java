package com.example.photoediter.photoView;

import android.animation.TypeEvaluator;
import android.graphics.Paint;

import java.lang.reflect.Type;

public class PointEvaluator implements TypeEvaluator<Point> {
    @Override
    public Point evaluate(float fraction, Point startValue, Point endValue) {
        float x = startValue.getX() + fraction * (endValue.getX()-startValue.getX());
        float y = startValue.getY() + fraction * (endValue.getY()-startValue.getY());
        Point point = new Point(x , y);
        return point;
    }
}
