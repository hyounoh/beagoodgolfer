package com.mug8.beagoodgolfer;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Retrofit retrofit;
    RetrofitAPI retrofitAPI;
    Call<AnalyzeResponse> callPoseImageTest;

    ImageView exampleIv;
    Drawable exampleDr;
    Bitmap exampleBm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRetrofit();

        exampleIv = findViewById(R.id.iv_example);
        exampleDr = exampleIv.getDrawable();
        exampleBm = ((BitmapDrawable) exampleDr).getBitmap();
    }

    public void clickAnalyze(View view) {
        Log.d(Constant.TAG, "clickAnalyze");

        try {
            File imageFile = new File(this.getApplicationContext().getFilesDir(), "example.PNG");
            FileOutputStream outStream = new FileOutputStream(imageFile);
            exampleBm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
            Log.d(Constant.TAG, "Success to store image");

            callPoseImageTest(imageFile);
            Log.d(Constant.TAG, "Call pose image test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickReset(View view) {
        Log.d(Constant.TAG, "clickReset");

        String[] files = this.getApplicationContext().fileList();

        for (String file: files) {
            Log.d(Constant.TAG, file);
        }
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
            AnalyzeResponse result = response.body();
            Log.d(Constant.TAG, result.message);
        }

        @Override
        public void onFailure(Call<AnalyzeResponse> call, Throwable t) {
            t.printStackTrace();
        }
    };
}
