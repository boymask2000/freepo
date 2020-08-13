package com.boymask.freepo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.boymask.freepo.network.GetDataService;
import com.boymask.freepo.network.RetroFoto;
import com.boymask.freepo.network.RetrofitClientInstance;

import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ImageView simpleImageView;
    private GetDataService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/





        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        simpleImageView = (ImageView) findViewById(R.id.imageview);
        getImage(service);

        final Button button = findViewById(R.id.button_id);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                getImage(service);
            }
        });



    }

    private void getImage(GetDataService service) {
        Call<RetroFoto> call = service.getAllPhotos();
        call.enqueue(new Callback<RetroFoto>() {
            @Override
            public void onResponse(Call<RetroFoto> call, Response<RetroFoto> response) {

                RetroFoto f = response.body();

                if (f.getVideos() != null && f.getVideos().size() > 0) {
                    Map p = (Map) f.getVideos().get(0);
                    String embed = (String) p.get("embed");

                    List<?> thumbs = (List<?>) p.get("thumbs");
                    Random rand = new Random();

                    // Generate random integers in range 0 to 999
                    int rand_int1 = rand.nextInt(thumbs.size());
                    Map thumb = (Map)thumbs.get(rand_int1);
                    String src =(String) thumb.get("src");

                    show(src);
                }
            }

            @Override
            public void onFailure(Call<RetroFoto> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void show(String embed) {


            ImageDownloader runner = new ImageDownloader(embed, simpleImageView, this);

            runner.execute("");




    }
    public void setImg( Bitmap bmp){
        simpleImageView.setImageBitmap(bmp);
    }
/*
    private String readUrl( String url) throws Exception {
        String out="";
        URL yahoo = new URL(url);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        yahoo.openStream()));

        String inputLine;

        while ((inputLine = in.readLine()) != null)
            out +=inputLine;

        in.close();
return out;
    }*/
}