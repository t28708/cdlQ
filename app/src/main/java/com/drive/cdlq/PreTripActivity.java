package com.drive.cdlq;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.LoadAdError;

public class PreTripActivity extends AppCompatActivity {

    private AdView adView;
    private FrameLayout adContainerView;
    private RewardedInterstitialAd rewardedInterstitialAd;
    private boolean hasAdBeenShown = false;

    TextView textView;
    private String textCaption;
    private float[][] dotCoordinatesPercent;
    private String[][] popupTitles;
    private String[][] popupDescriptions;
    private int[] imageResIds;

    private PopupWindow currentPopupWindow;
    private PopupWindow bottomPopupWindow;
    private TextView popupTextView;
    private View currentAnchorView;

    private ImageView carImageView;
    private ImageView[] dotImages;

    private int currentImageIndex = 0;
    private int imageWidthPx;
    private int imageHeightPx;

    private String appLanguage;

    private String adThanks;
    private String  adMessage;
    private String  adButton;

    private BillingManager billingManager;
    private String removeAdsString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_trip);

        billingManager = new BillingManager(this);

        MobileAds.initialize(this, initializationStatus -> {});
        adContainerView = findViewById(R.id.ad_view);
        adContainerView.post(this::loadBanner);

        int launchCount = LaunchCounter.increaseLaunchCount(this);

        if (launchCount == 1 || launchCount % 5 == 0) {
            loadRewardedInterstitialAd();
        }

        // Получаем SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Проверяем, существует ли переменная для языка
        appLanguage = sharedPreferences.getString("appLanguage", null); // Получаем сохранённый язык

        if ("eng".equals(appLanguage)) {
            adThanks = "Unlock Part";
            adMessage = "Watch an ad to unlock the next part";
            adButton = "Continue";
        } if ("ru".equals(appLanguage)) {
            adThanks = "Разблокировать часть";
            adMessage = "Посмотрите рекламу, чтобы разблокировать следующую часть";
            adButton = "Продолжить";
        } if ("es".equals(appLanguage)) {
            adThanks = "Desbloquear parte";
            adMessage = "Mira un anuncio para desbloquear la siguiente parte"; // Wait, "the next part" should be "la siguiente parte"!
            adButton = "Continuar";
        }

        Bundle arguments = getIntent().getExtras();
        String name = arguments.get("typeOfVariant").toString();

        if (name.equals("PartA")) {
            textCaption = PreTripA.textCaption;
            imageResIds = PreTripA.imageResIds;
            dotCoordinatesPercent = PreTripA.dotCoordinatesPercent;
            popupTitles = PreTripA.popupTitles;
            popupDescriptions = PreTripA.popupDescriptions;
        } else if (name.equals("PartB")) {
            textCaption = PreTripB.textCaption;
            imageResIds = PreTripB.imageResIds;
            dotCoordinatesPercent = PreTripB.dotCoordinatesPercent;
            popupTitles = PreTripB.popupTitles;
            popupDescriptions = PreTripB.popupDescriptions;
        } else if (name.equals("PartC")) {
            textCaption = PreTripC.textCaption;
            imageResIds = PreTripC.imageResIds;
            dotCoordinatesPercent = PreTripC.dotCoordinatesPercent;
            popupTitles = PreTripC.popupTitles;
            popupDescriptions = PreTripC.popupDescriptions;
        } else if (name.equals("CouplingSystem")) {
            textCaption = PreTripCouplingSystem.textCaption;
            imageResIds = PreTripCouplingSystem.imageResIds;
            dotCoordinatesPercent = PreTripCouplingSystem.dotCoordinatesPercent;
            popupTitles = PreTripCouplingSystem.popupTitles;
            popupDescriptions = PreTripCouplingSystem.popupDescriptions;
        } else if (name.equals("InCabInspection")) {
            textCaption = PreTripInCabInspection.textCaption;
            imageResIds = PreTripInCabInspection.imageResIds;
            dotCoordinatesPercent = PreTripInCabInspection.dotCoordinatesPercent;
            popupTitles = PreTripInCabInspection.popupTitles;
            popupDescriptions = PreTripInCabInspection.popupDescriptions;
        }

        textView = findViewById(R.id.textView2);
        textView.setText(textCaption);

        carImageView = findViewById(R.id.car_image);

        dotImages = new ImageView[]{
                findViewById(R.id.dot_image),
                findViewById(R.id.dot_image1),
                findViewById(R.id.dot_image2),
                findViewById(R.id.dot_image3),
                findViewById(R.id.dot_image4)
        };

        carImageView.setImageResource(imageResIds[currentImageIndex]);
        carImageView.post(() -> {
            imageWidthPx = carImageView.getWidth();
            imageHeightPx = carImageView.getHeight();
            initDots(currentImageIndex);
        });
    }

    private void showAdNoticeThenShowAd() {
        new AlertDialog.Builder(this)
                .setTitle(adThanks)
                .setMessage(adMessage)
                .setCancelable(false)
                .setPositiveButton(adButton, (dialog, which) -> {
                    if (!GlobalData.isPremium) {
                         showRewardedInterstitialAd();
                    }
                })
                .show();
    }

    private void loadRewardedInterstitialAd() {
        if (GlobalData.isPremium) {
            return;
        }
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedInterstitialAd.load(this,
                GlobalData.rewardedInterstitialAdUnitID,
                adRequest,
                new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedInterstitialAd ad) {
                        rewardedInterstitialAd = ad;
                        Log.d("AdMob", "Rewarded interstitial loaded");

                        if (!hasAdBeenShown) {
                            showAdNoticeThenShowAd();
                            hasAdBeenShown = true;
                        }
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        rewardedInterstitialAd = null;
                        Log.d("AdMob", "Failed to load rewarded interstitial: " + loadAdError.getMessage());
                    }
                });
    }

    private void showRewardedInterstitialAd() {
        if (rewardedInterstitialAd != null) {
            rewardedInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdShowedFullScreenContent() {
                    Log.d("AdMob", "Ad shown");
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    Log.d("AdMob", "Ad failed to show");
                    rewardedInterstitialAd = null;
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    Log.d("AdMob", "Ad dismissed");
                    rewardedInterstitialAd = null;
                }
            });

            rewardedInterstitialAd.show(this, rewardItem -> {
                Log.d("AdMob", "Ad watched. Reward not used.");
            });
        }
    }

    private void loadBanner() {
        if (GlobalData.isPremium) {
            adContainerView.setVisibility(View.GONE);
            return;
        }
        adView = new AdView(this);
        adView.setAdUnitId(GlobalData.pubAdID);
        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;
        float adWidthPixels = adContainerView.getWidth();
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    private void initDots(int imageIndex) {
        float[] coordsPercent = dotCoordinatesPercent[imageIndex];

        for (int i = 0; i < dotImages.length; i++) {
            if (i * 2 + 1 < coordsPercent.length) {
                int leftPx = Math.round(coordsPercent[i * 2] * imageWidthPx);
                int topPx = Math.round(coordsPercent[i * 2 + 1] * imageHeightPx);
                setDotPosition(dotImages[i], leftPx, topPx);

                final int dotIndex = i;
                dotImages[i].setOnClickListener(v -> togglePopups(v, popupTitles[imageIndex][dotIndex], popupDescriptions[imageIndex][dotIndex]));

                dotImages[i].setVisibility(View.VISIBLE);
            } else {
                dotImages[i].setVisibility(View.GONE);
            }
        }

        carImageView.setImageResource(imageResIds[imageIndex]);
    }

    private void setDotPosition(ImageView dot, int leftPx, int topPx) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) dot.getLayoutParams();
        params.leftMargin = leftPx;
        params.topMargin = topPx;
        dot.setLayoutParams(params);
    }

    public void showPreviousImage(View view) {
        closeBothPopups();
        currentImageIndex = (currentImageIndex > 0) ? currentImageIndex - 1 : imageResIds.length - 1;

        carImageView.setImageResource(imageResIds[currentImageIndex]);
        carImageView.post(() -> {
            imageWidthPx = carImageView.getWidth();
            imageHeightPx = carImageView.getHeight();
            initDots(currentImageIndex);
        });
    }

    public void showNextImage(View view) {
        closeBothPopups();
        currentImageIndex = (currentImageIndex < imageResIds.length - 1) ? currentImageIndex + 1 : 0;

        carImageView.setImageResource(imageResIds[currentImageIndex]);
        carImageView.post(() -> {
            imageWidthPx = carImageView.getWidth();
            imageHeightPx = carImageView.getHeight();
            initDots(currentImageIndex);
        });
    }

    private void togglePopups(View anchorView, String topText, String bottomText) {
        if (anchorView == currentAnchorView && currentPopupWindow != null && currentPopupWindow.isShowing()) {
            closeBothPopups();
        } else {
            closeBothPopups();
            showPopup(anchorView, topText);
            showPopupBelow(anchorView, bottomText);
            currentAnchorView = anchorView;
        }
    }

    private void showPopup(View anchorView, String text) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout, null);
        popupTextView = popupView.findViewById(R.id.popup_text);
        popupTextView.setText(text);

        currentPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupTextView.setOnClickListener(v -> closeBothPopups());
        currentPopupWindow.showAsDropDown(anchorView, 0, 0, Gravity.NO_GRAVITY);
    }

    private void showPopupBelow(View anchorView, String text) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout, null);
        popupTextView = popupView.findViewById(R.id.popup_text);
        popupTextView.setText(text);

        bottomPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupTextView.setOnClickListener(v -> closeBothPopups());
        bottomPopupWindow.showAtLocation(anchorView, Gravity.BOTTOM, 0, 0);
    }

    private void closeBothPopups() {
        if (currentPopupWindow != null && currentPopupWindow.isShowing()) {
            currentPopupWindow.dismiss();
            currentPopupWindow = null;
        }
        if (bottomPopupWindow != null && bottomPopupWindow.isShowing()) {
            bottomPopupWindow.dismiss();
            bottomPopupWindow = null;
        }
        currentAnchorView = null;
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }
}
