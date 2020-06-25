package com.mug8.beagoodgolfer;

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
}
