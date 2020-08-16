package com.boymask.freepo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.boymask.freepo.network.GetDataService;
import com.boymask.freepo.network.RetroFoto;
import com.boymask.freepo.network.RetrofitClientInstance;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ImageView simpleImageView;
    private GetDataService service;
    private int counterInterstitial = 0;
    private InterstitialAd mInterstitialAd;
    private final String interID_TEST = "ca-app-pub-3940256099942544/1033173712";
    private final String interID_PROD = "ca-app-pub-6114671792914206/9958086850";
    private final String interID = interID_TEST;
    private String query;
    private String gay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPreferences();

        // Menu Inizio ---------------------------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Menu fine
        // Pubblicita Inizio ----------------------------------------------------------------
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(interID);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
// Pubblicita fine

        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        simpleImageView = (ImageView) findViewById(R.id.imageview);
        getImage(service);

        final Button button = findViewById(R.id.button_id);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                showInterstitial();

                getImage(service);
            }
        });


    }

    private void getPreferences() {
        SharedPreferences sharedPref =
                PreferenceManager
                        .getDefaultSharedPreferences(this);
//        String v = sharedPref.getString("reply_entries", "");
//        String v1 = sharedPref.getString("reply_values", "");
        Map<String, ?> v2 = sharedPref.getAll();
        Set<String> keys = v2.keySet();
        for (String k : keys) {
            System.out.println(k);
            String val = (String) v2.get(k);
            System.out.println(val);
            setParam(val);
        }
    }

    private void setParam(String val) {
        switch (val) {
            case "gay":
                query="gay";
                gay ="2";
                break;
            case "etero":
                query="teen";
                gay ="0";
                break;
            default:
                query="teen";
                gay ="1";
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);

            startActivity(intent);
            getPreferences();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showInterstitial() {
        counterInterstitial = (counterInterstitial + 1) % 10;

        if (counterInterstitial == 0) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        }
    }

    private int nextPage = 100;

    private void getImage(GetDataService service) {
        Call<RetroFoto> call = service.getAllPhotos(nextPage, query, gay);
        call.enqueue(new Callback<RetroFoto>() {
            @Override
            public void onResponse(Call<RetroFoto> call, Response<RetroFoto> response) {

                RetroFoto f = response.body();

                nextPage = getNextPage(f.getTotal_count());

                if (f.getVideos() != null && f.getVideos().size() > 0) {
                    Map p = (Map) f.getVideos().get(0);
                    String embed = (String) p.get("embed");
                    Map defaultThumb = (Map) p.get("default_thumb");

                    String src = (String) defaultThumb.get("src");


                  /*  List<?> thumbs = (List<?>) p.get("thumbs");
                    Random rand = new Random();

                    // Generate random integers in range 0 to 999
                    int rand_int1 = rand.nextInt(thumbs.size());
                    Map thumb = (Map) thumbs.get(rand_int1);
                    String src = (String) thumb.get("src");*/

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
    private Random rand = new Random();
    private int getNextPage(int n) {

        int rand_int1 = rand.nextInt(n);
        if (rand_int1 == 0) rand_int1 = 1;
        return rand_int1;
    }

    private void show(String embed) {


        ImageDownloader runner = new ImageDownloader(embed, simpleImageView, this);

        runner.execute("");


    }

    public void setImg(Bitmap bmp) {
        simpleImageView.setImageBitmap(bmp);
    }

}