package com.drive.cdlq;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.OnUserEarnedRewardListener;


public class VocabularyActivitySign extends AppCompatActivity {

    private AdView adView;
    private FrameLayout adContainerView;

    private String appLanguage;
    private String localizeNextButton;

    private String localizePrevButton;
    private String localizeShuffleButton;

    private ImageView signImageView;
    private Button nextButton;
    private Button prevButton;
    private Button shuffleButton;
    private TextView questionTextView;
    private TextView answerTextView;

    private List<String> imageNames = new ArrayList<>();
    private List<String> descriptions = new ArrayList<>();
    private List<String> localizedDescriptions = new ArrayList<>();

    private int currentIndex = 0;

    private String typeOfVariant;

    private RewardedInterstitialAd rewardedInterstitialAd;
    private boolean hasAdBeenShown = false;

    private String adThanks;
    private String  adMessage;
    private String  adButton;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_test);

        MobileAds.initialize(this, initializationStatus -> {});
        adContainerView = findViewById(R.id.ad_view);
        adContainerView.post(this::loadBanner);

        int launchCount = LaunchCounter.increaseLaunchCount(this);

        if (launchCount == 1 || launchCount % 5 == 0) {
            loadRewardedInterstitialAd();
        }


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        appLanguage = sharedPreferences.getString("appLanguage", "ru");
        Log.d("LanguageSign", "We use saved language: " + appLanguage);

        signImageView = findViewById(R.id.signImageView);
        nextButton = findViewById(R.id.nextQuizButtonQuiz);
        prevButton = findViewById(R.id.prevQuizButtonQuiz);
        shuffleButton = findViewById(R.id.shuffleButtonQuiz);
        questionTextView = findViewById(R.id.questionTextView);
        answerTextView = findViewById(R.id.answerTextView);

        prevButton = findViewById(R.id.prevQuizButtonQuiz);
        shuffleButton = findViewById(R.id.shuffleButtonQuiz);

        // Получаем переданное значение typeOfVariant
        typeOfVariant = "signs_"+ getIntent().getStringExtra("typeOfVariant").toLowerCase() + ".json" ;

        // Логирование для проверки, что значение передано
        Log.d("VocabularyActivitySignTypeOfVaryant", "Received typeOfVariant: " + typeOfVariant);


        loadJsonData();

        if (!imageNames.isEmpty()) {
            showCurrentItem();
        }

        nextButton.setEnabled(true);
        nextButton.setOnClickListener(v -> showNextImage());

        prevButton.setOnClickListener(v -> showPrevImage());

        shuffleButton.setOnClickListener(v -> {
            shuffleData();
            nextButton.performClick();
        });



    }

    private void loadJsonData() {
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open(typeOfVariant);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String jsonStr = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                String imageName = obj.getString("image").replace(".png", "");
                imageNames.add(imageName);

                String description = obj.optString("description", "");
                descriptions.add(description);

                String localized;
                switch (appLanguage) {
                    case "ru":
                        localized = obj.optString("description_ru", "");
                        localizeNextButton = "Далее";
                        localizePrevButton = "Назад";
                        localizeShuffleButton = "Перемешать";
                        adThanks = "Разблокировать часть";
                        adMessage = "Посмотрите рекламу, чтобы разблокировать следующую часть";
                        adButton = "Продолжить";
                        break;
                    case "es":
                        localized = obj.optString("description_es", "");
                        localizeNextButton = "Siguiente";
                        localizePrevButton = "Atrás";
                        localizeShuffleButton = "Mezclar";
                        adThanks = "Desbloquear parte";
                        adMessage = "Mira un anuncio para desbloquear la siguiente parte";
                        adButton = "Continuar";
                        break;
                    case "fr":
                        localized = obj.optString("description_fr", "");
                        localizeNextButton = "Suivant";
                        localizePrevButton = "Précédent";
                        localizeShuffleButton = "Mélanger";
                        adThanks = "Débloquer la partie";
                        adMessage = "Regardez une publicité pour débloquer la partie suivante";
                        adButton = "Continuer";
                        break;
                    default:
                        localized = description;
                        localizeNextButton = "Next";
                        localizePrevButton = "Prev";
                        localizeShuffleButton = "Shuffle";
                        adThanks = "Unlock Part";
                        adMessage = "Watch an ad to unlock the next part";
                        adButton = "Continue";
                        break;
                }
                localizedDescriptions.add(localized);
                nextButton.setText(localizeNextButton);
                prevButton.setText(localizePrevButton);
                shuffleButton.setText(localizeShuffleButton);

            }

        } catch (Exception e) {
            Log.e("VocabularyActivity", "Ошибка чтения JSON: ", e);
        }
    }

    private void showCurrentItem() {
        String imageName = imageNames.get(currentIndex);
        String description = descriptions.get(currentIndex);
        String localized = localizedDescriptions.get(currentIndex);

        int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        if (resId != 0) {
            signImageView.setImageResource(resId);
        } else {
            Log.w("VocabularyActivity", "Картинка не найдена: " + imageName);
        }

        questionTextView.setText(description);      // всегда на английском
        answerTextView.setText(localized);          // на выбранном языке
    }

    private void showNextImage() {
        currentIndex++;
        if (currentIndex >= imageNames.size()) {
            currentIndex = 0;
        }
        showCurrentItem();
    }

    private void showPrevImage() {
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = imageNames.size() - 1;
        }
        showCurrentItem();
    }

    private void shuffleData() {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < imageNames.size(); i++) {
            indices.add(i);
        }

        java.util.Collections.shuffle(indices);

        List<String> shuffledImages = new ArrayList<>();
        List<String> shuffledDescriptions = new ArrayList<>();
        List<String> shuffledLocalized = new ArrayList<>();

        for (int index : indices) {
            shuffledImages.add(imageNames.get(index));
            shuffledDescriptions.add(descriptions.get(index));
            shuffledLocalized.add(localizedDescriptions.get(index));
        }

        imageNames = shuffledImages;
        descriptions = shuffledDescriptions;
        localizedDescriptions = shuffledLocalized;

        currentIndex = 0; // начать с первого после перемешивания
    }


    private void loadBanner() {
        if (GlobalData.isPremium) {
            adContainerView.setVisibility(android.view.View.GONE);
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

    @Override
    protected void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    private void showAdNoticeThenShowAd() {
        new AlertDialog.Builder(this)
                .setTitle(adThanks)
                .setMessage(adMessage)
                .setCancelable(false)
                .setPositiveButton(adButton, (dialog, which) -> {
                    showRewardedInterstitialAd();
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
                //int rewardAmount = rewardItem.getAmount();
                //String rewardType = rewardItem.getType();
                Log.d("AdMob", "Ad watched. Reward not used.");
            });
        }
    }

}
