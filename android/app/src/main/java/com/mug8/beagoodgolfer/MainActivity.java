package com.mug8.beagoodgolfer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // APIs
    Retrofit retrofit;
    RetrofitAPI retrofitAPI;
    Call<AnalyzeResponse> callPoseImageTest;

    // Images
    ImageView exampleIv;
    Drawable exampleDr;
    Bitmap exampleBm;
    File imageFile;

    // Flags
    Boolean isShowRaw = false;
    int retryCount = 0;
    int RETRY_COUNT_MAX = 5;

    // Components
    TextView tvRaw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRetrofit();

        exampleIv = findViewById(R.id.iv_example);
        exampleDr = exampleIv.getDrawable();
        exampleBm = ((BitmapDrawable) exampleDr).getBitmap();

        tvRaw = findViewById(R.id.tv_raw);

        // Store test image file into internal storage
        try {
            imageFile = new File(this.getApplicationContext().getFilesDir(), "example.PNG");
            FileOutputStream outStream = new FileOutputStream(imageFile);
            exampleBm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
            Log.d(Constant.TAG, "Success to store image");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickAnalyze(View view) {
        Log.d(Constant.TAG, "clickAnalyze");

        callPoseImageTest(imageFile);
    }

    public void clickReset(View view) {
        Log.d(Constant.TAG, "clickReset");

        exampleIv.setImageBitmap(exampleBm);
    }

    public void clickShowRaw(View view) {
        Log.d(Constant.TAG, "clickShowRaw");

        if (isShowRaw) {
            tvRaw.setVisibility(View.INVISIBLE);
        } else {
            tvRaw.setVisibility(View.VISIBLE);
        }

        isShowRaw = !isShowRaw;
    }

    private void initRetrofit() {
        String baseUrl = getString(R.string.baseUrl);
        retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitAPI = retrofit.create(RetrofitAPI.class);
    }

    private void callPoseImageTest(File imageFile) {

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);

        callPoseImageTest = retrofitAPI.poseImageTest(body);
        callPoseImageTest.enqueue(retrofitCallback);
    }

    private Callback<AnalyzeResponse> retrofitCallback = new Callback<AnalyzeResponse>() {
        @Override
        public void onResponse(Call<AnalyzeResponse> call, Response<AnalyzeResponse> response) {
            Log.d(Constant.TAG, "onResponse");

            AnalyzeResponse result = response.body();

            // Set paintCircle
            Paint paintCircle = new Paint();
            paintCircle.setAntiAlias(true);
            paintCircle.setColor(Color.GREEN);

            // Set paintRect
            Paint paintRect = new Paint();
            paintRect.setAntiAlias(true);
            paintRect.setColor(Color.BLUE);
            paintRect.setStyle(Paint.Style.STROKE);
            paintRect.setStrokeWidth(3.0f);

            // Set paintPoint
            Paint paintPoint = new Paint();
            paintPoint.setAntiAlias(true);
            paintPoint.setColor(Color.RED);
            paintPoint.setStrokeWidth(5.0f);

            // Set bitmap
            Bitmap workingBitmap = Bitmap.createBitmap(exampleBm);
            Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

            // Set canvas
            Canvas canvas = new Canvas(mutableBitmap);

            // Draw bbox
            List<Double> coordBbox = result.results.get(0).bbox;
            Rect rect = new Rect(
                    coordBbox.get(0).intValue(),
                    coordBbox.get(1).intValue(),
                    coordBbox.get(0).intValue() + coordBbox.get(2).intValue(),
                    coordBbox.get(1).intValue() + coordBbox.get(3).intValue()
            );
            canvas.drawRect(rect, paintRect);

            // Draw points
            List<Double> coordPoint = result.results.get(0).keypoints;
            for (int i = 0; i < coordPoint.size(); i += 3) {
                canvas.drawPoint(coordPoint.get(i).floatValue(), coordPoint.get(i + 1).floatValue(), paintPoint);
            }

            // Replace image
            exampleIv.setImageBitmap(mutableBitmap);

            // Replace raw text view
            tvRaw.setText(result.toString());
        }

        @Override
        public void onFailure(Call<AnalyzeResponse> call, Throwable t) {
            if (retryCount < RETRY_COUNT_MAX) {
                Log.d(Constant.TAG, "onFailure - retry " + retryCount);
                retryCount++;
                callPoseImageTest(imageFile);
            } else {
                retryCount = 0;
                t.printStackTrace();
            }

        }
    };
}
