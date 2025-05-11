package com.example.turfly;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;

public class FitnessActivity extends AppCompatActivity {

    Button button1, button2;
    private NativeAd nativeAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);

        MobileAds.initialize(this);
        refreshAd();

        Toolbar fitToolbar = findViewById(R.id.fitnessToolbar);
        setSupportActionBar(fitToolbar);
        button1 = findViewById(R.id.startyoga1);
        button2 = findViewById(R.id.startyoga2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FitnessActivity.this, Before18.class);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FitnessActivity.this, After18.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fitness_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.privacy){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
            startActivity(intent);
            return true;
        }
        if (id == R.id.term){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
            startActivity(intent);
            return true;
        }
        if (id == R.id.rate){
            return true;
        }
        if (id == R.id.more){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q=fitness+apps&c=apps&hl=en"));
            startActivity(intent);
            return true;
        }
        if (id == R.id.share){
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String sharebody = "Try out, the best Turf booking and fitness app!!\n Free download now!\n" + "https://play.google.com/store/apps/details?id=com.example.turfly&hl=en";
            String sharehub = "Turfly";
            myIntent.putExtra(Intent.EXTRA_SUBJECT,sharehub);
            myIntent.putExtra(Intent.EXTRA_TEXT,sharebody);
            startActivity(Intent.createChooser(myIntent, "share using"));
            return true;
        }
        return true;
    }

    public void beforeage18(View view) {
        Intent intent = new Intent(FitnessActivity.this, Before18.class);
        startActivity(intent);
    }

    public void afterage18(View view) {
        Intent intent = new Intent(FitnessActivity.this, After18.class);
        startActivity(intent);
    }

    public void food(View view) {
        Intent intent = new Intent(FitnessActivity.this, FoodActivity.class);
        startActivity(intent);
    }

    private void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        adView.setMediaView(adView.findViewById(R.id.ad_media));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // Set headline
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // Set media content
        if (nativeAd.getMediaContent() != null) {
            adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
        }

        // Set body text
        if (nativeAd.getBody() != null) {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        } else {
            adView.getBodyView().setVisibility(View.GONE);
        }

        // Set call-to-action
        if (nativeAd.getCallToAction() != null) {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        } else {
            adView.getCallToActionView().setVisibility(View.GONE);
        }

        // Set icon
        if (nativeAd.getIcon() != null) {
            ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        } else {
            adView.getIconView().setVisibility(View.GONE);
        }

        // Set price
        if (nativeAd.getPrice() != null) {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        } else {
            adView.getPriceView().setVisibility(View.GONE);
        }

        // Set store
        if (nativeAd.getStore() != null) {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        } else {
            adView.getStoreView().setVisibility(View.GONE);
        }

        // Set star rating
        if (nativeAd.getStarRating() != null) {
            ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        } else {
            adView.getStarRatingView().setVisibility(View.GONE);
        }

        // Set advertiser
        if (nativeAd.getAdvertiser() != null) {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        } else {
            adView.getAdvertiserView().setVisibility(View.GONE);
        }

        // Set the native ad to the view
        adView.setNativeAd(nativeAd);
    }

    private void refreshAd() {
        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.ADMOB_ADS_UNIT_ID));

        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                if (FitnessActivity.this.nativeAd != null) {
                    FitnessActivity.this.nativeAd.destroy();
                }
                FitnessActivity.this.nativeAd = nativeAd;

                // Inflate native ad layout
                FrameLayout frameLayout = findViewById(R.id.fl_adplaceholder);
                NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.ad_unified, null);

                // Populate ad view with content
                populateNativeAdView(nativeAd, adView);

                // Display the ad
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }
        });

        // Set ad options
        NativeAdOptions adOptions = new NativeAdOptions.Builder().build();
        builder.withNativeAdOptions(adOptions);

        // Build ad loader with error handling
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(com.google.android.gms.ads.LoadAdError adError) {
                // Log the error (optional)
                System.out.println("Ad failed to load: " + adError.getMessage());
            }
        }).build();

        // Load ad request
        adLoader.loadAd(new AdRequest.Builder().build());
    }
}