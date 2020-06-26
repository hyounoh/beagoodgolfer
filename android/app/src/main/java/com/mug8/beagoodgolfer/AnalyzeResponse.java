package com.mug8.beagoodgolfer;

import androidx.annotation.NonNull;

import java.util.List;

public class AnalyzeResponse {
    String message;
    List<Pose> results;

    class Pose {
        Double area;
        List<Double> bbox;
        Integer category_id;
        List<Double> keypoints;
        Double score;
    }

    @NonNull
    @Override
    public String toString() {
        Pose pose = results.get(0);
        String retMessage =
                message + " " +
                        pose.area + " " +
                        pose.score + " " +
                        pose.bbox + " " +
                        pose.keypoints + " ";

        return retMessage;
    }
}
