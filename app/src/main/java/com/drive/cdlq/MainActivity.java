package com.drive.cdlq;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements BillingManager.OnPremiumListener {

    // The ad view for our banner ad unit.

    private AdView adView;
    private FrameLayout adContainerView;
    private BillingManager billingManager;
    private Button btnRemoveAds;
    private TextView tvPremiumLabel;

    private Button buttonGeneralKnowledges, buttonAirBrakes, buttonCombinated ;

    // reviews goople-play
    private static final String PREFS_NAME = "RateAppPrefs";
    private static final String FIRST_LAUNCH_DATE = "firstLaunchDate";
    private static final String LAST_PROMPT_DATE = "lastPromptDate";
    private static final int DAYS_UNTIL_NEXT_PROMPT = 3; // Интервал в днях
    private static final int FIRST_PROMPT_DELAY = 1; // Задержка первого показа в днях

    private String appLanguage;
    private String ChapterText;

    private String TitleCompareText;
    private String MessageCompareText;

    private String PositiveCompareText;
    private String NegativeCompareText;
    private String RemoveAdsText;
    
    // UI Elements
    private TextView tvRestorePurchases;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ///////////////////////////////
        // Получаем SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Проверяем, существует ли переменная для языка
        appLanguage = sharedPreferences.getString("appLanguage", null); // Получаем сохранённый язык

        // Если переменная для языка не существует, сохраняем текущий язык системы
        if (appLanguage == null) {
            // Получаем текущий язык системы
            String systemLanguage = Locale.getDefault().getLanguage();

            // Определяем язык (если ru, то сохраняем 'ru', иначе 'eng')
            appLanguage = systemLanguage.equals("ru") ? "ru" : "eng";

            // Сохраняем язык в SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("appLanguage", appLanguage);  // Сохраняем язык
            editor.apply();
        } else {
            // Log.d("Language", "We use save language");
        }

        // Здесь вы можете использовать appLanguage для настройки UI или других параметров
        // Log.d("Language", "App Language: " + appLanguage);


        LinearLayout linearLayoutTheoryRU = findViewById(R.id.linear_theoryRU);
        LinearLayout linearLayoutTheoryUA = findViewById(R.id.linear_theoryUA);
        LinearLayout linearLayoutTheoryES = findViewById(R.id.linear_theoryES);
        TextView textViewReviews = findViewById(R.id.textReviews);
        Button reviewButton = findViewById(R.id.reviewButton);
        Button learnEnglishButton = findViewById(R.id.learnEnglish);
        Button learnEnglishSpanishButton = findViewById(R.id.learnEnglishSpanish);


        if ("eng".equals(appLanguage)) {
            // Или скрыть полностью (не занимает место в макете)
            linearLayoutTheoryRU.setVisibility(View.GONE);
            linearLayoutTheoryES.setVisibility(View.GONE);
            linearLayoutTheoryUA.setVisibility(View.VISIBLE);
            textViewReviews.setText("If our app has been helpful to you, please take a moment to leave a review.");
            reviewButton.setText("Leave a review");
            learnEnglishButton.setVisibility(View.GONE);
            learnEnglishSpanishButton.setVisibility(View.GONE);
            String titleCaption = "Change language";
            ChapterText = "Chapter";
            ////
            TitleCompareText = "Rate our app";
            MessageCompareText = "If you enjoyed using the app, please rate us on Google Play!";
            PositiveCompareText = "Rate now";
            NegativeCompareText = "Later";
            RemoveAdsText = "Remove Ads";
        }
        if ("ru".equals(appLanguage)) {
            linearLayoutTheoryUA.setVisibility(View.GONE);
            linearLayoutTheoryES.setVisibility(View.GONE);
            linearLayoutTheoryRU.setVisibility(View.VISIBLE);
            textViewReviews.setText("Если наше приложение оказалось полезным для вас, пожалуйста, уделите одну минуту на оставление отзыва.");
            reviewButton.setText("Оставить отзыв");
            learnEnglishButton.setVisibility(View.VISIBLE);
            learnEnglishSpanishButton.setVisibility(View.GONE);
            String titleCaption = "Выберите язык";
            ChapterText = "Часть ";
            //
            TitleCompareText = "Оцените наше приложение";
            MessageCompareText = "Если вам понравилось приложение, пожалуйста, оцените его в Google Play!";
            PositiveCompareText = "Оценить";
            NegativeCompareText = "Позже";
            RemoveAdsText = "Убрать рекламу";
        }
        if ("es".equals(appLanguage)) {
            linearLayoutTheoryUA.setVisibility(View.GONE);
            linearLayoutTheoryRU.setVisibility(View.GONE);
            linearLayoutTheoryES.setVisibility(View.VISIBLE);
            textViewReviews.setText("Si nuestra aplicación le ha sido útil, por favor, tómese un minuto para dejar una reseña.");
            reviewButton.setText("Dejar reseña");
            learnEnglishButton.setVisibility(View.GONE);
            learnEnglishSpanishButton.setVisibility(View.VISIBLE);
            String titleCaption = "Выберите язык";
            ChapterText = "Parte ";
            //
            TitleCompareText = "Califica nuestra aplicación";
            MessageCompareText = "Si te gustó la aplicación, por favor califícanos en Google Play";
            PositiveCompareText = "Calificar ahora";
            NegativeCompareText = "Más tarde";
            RemoveAdsText = "Quitar anuncios";
        }



        Button btnChangeLanguage = findViewById(R.id.btn_change_language);
        btnChangeLanguage.setText(appLanguage);
        btnChangeLanguage.setOnClickListener(view -> showLanguageDialog());

        // Initialize BillingManager
        billingManager = new BillingManager(this);
        // Check premium status on launch
        BillingManager.checkPremiumStatus(this);

        btnRemoveAds = findViewById(R.id.btn_remove_ads);
        tvPremiumLabel = findViewById(R.id.tv_premium_label);
        tvRestorePurchases = findViewById(R.id.tv_restore_purchases); // Init field
        
        btnRemoveAds.setText(RemoveAdsText);
        btnRemoveAds.setOnClickListener(v -> billingManager.launchPurchaseFlow());
        
        tvRestorePurchases.setOnClickListener(v -> billingManager.restorePurchases());
        
        updateUI();


        ///////////////////////////////

        // Сохранить дату первого запуска, если это первый запуск
        saveFirstLaunchDate();

        // Проверяем, нужно ли показывать диалог
        if (shouldShowRateDialog()) {
            showRateAppDialog(TitleCompareText, MessageCompareText, PositiveCompareText, NegativeCompareText);
        }

        // Расскоментируй чтобы проверить стартовое меню для отзыва
        //showRateAppDialog(TitleCompareText, MessageCompareText, PositiveCompareText, NegativeCompareText);

        //
        // Настраиваем все заголовки и их контент для переключения видимости
        setupToggle(findViewById(R.id.tvTitle1), findViewById(R.id.llContent1));
        setupToggle(findViewById(R.id.tvTitle2), findViewById(R.id.llContent2));
        setupToggle(findViewById(R.id.tvTitle3), findViewById(R.id.llContent3));
        setupToggle(findViewById(R.id.tvTitle4), findViewById(R.id.llContent1UA));
        setupToggle(findViewById(R.id.tvTitle5), findViewById(R.id.llContent5));
        setupToggle(findViewById(R.id.tvTitle6), findViewById(R.id.llContent6));

        setupToggle(findViewById(R.id.tvTitleES4), findViewById(R.id.ESContent4));
        setupToggle(findViewById(R.id.tvTitleES5), findViewById(R.id.ESContent5));
        setupToggle(findViewById(R.id.tvTitleES6), findViewById(R.id.ESContent6));
        //
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Initialize our ad request.
        MobileAds.initialize(this, initializationStatus -> {});
        adContainerView = findViewById(R.id.ad_view);
        adContainerView.post(this::loadBanner);


        // Вызовы метода для различных типов вопросов
        processQuestionType(QuestionGeneral.question, "GENERAL KNOWLEDGE", "btn1_", 1);
        processQuestionType(QuestionAir.question, "AIR BRAKES", "btn2_", 1);
        processQuestionType(QuestionCombinated.question, "COMBINATION VEHICLE", "btn3_", 1);

        processQuestionType(QuestionGeneral.question, "GENERAL KNOWLEDGE UA", "btnUA1_", 1);
        processQuestionType(QuestionAir.question, "AIR BRAKES UA", "btnUA2_", 1);
        processQuestionType(QuestionCombinated.question, "COMBINATION VEHICLE UA", "btnUA3_", 1);

        processQuestionType(QuestionGeneral.question, "GENERAL KNOWLEDGE ES", "btnES1_", 1);
        processQuestionType(QuestionAir.question, "AIR BRAKES ES", "btnES2_", 1);
        processQuestionType(QuestionCombinated.question, "COMBINATION VEHICLE ES", "btnES3_", 1);

    }

    private void setupToggle(final TextView title, final LinearLayout content) {
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Если контент скрыт, то показываем его. Иначе - скрываем.
                if (content.getVisibility() == View.GONE) {
                    content.setVisibility(View.VISIBLE);
                } else {
                    content.setVisibility(View.GONE);
                }
            }
        });
    }

    private void loadBanner() {
        // Create an ad request.
        // Create an ad request.
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
        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;
        float adWidthPixels = adContainerView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    // Pause ad view when leaving the activity.
    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        // Use fields initialized in onCreate
        // But re-finding them is safer if updateUI called before onCreate finishes? No, onCreate calls it at end.
        // However, let's trust the fields.
        
        if (GlobalData.isPremium) {
            if (tvPremiumLabel != null) tvPremiumLabel.setVisibility(View.VISIBLE);
            if (btnRemoveAds != null) btnRemoveAds.setVisibility(View.GONE);
            if (tvRestorePurchases != null) tvRestorePurchases.setVisibility(View.GONE);
        
            // Скрываем баннер
            if (adView != null) {
                adView.setVisibility(View.GONE);
                adView.destroy();
            }
        } else {
            if (tvPremiumLabel != null) tvPremiumLabel.setVisibility(View.GONE);
            if (btnRemoveAds != null) btnRemoveAds.setVisibility(View.VISIBLE);
            if (tvRestorePurchases != null) tvRestorePurchases.setVisibility(View.VISIBLE);

            // Показываем баннер
            if (adView != null && adView.getVisibility() == View.GONE) {
                 adView.setVisibility(View.VISIBLE);
                 AdRequest adRequest = new AdRequest.Builder().build();
                 adView.loadAd(adRequest);
            }
        }
    }

    public void startNewActivityGeneral1(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE");
        newIntent.putExtra("part", "1");
        startActivity(newIntent);
    }

    public void startNewActivityGeneral2(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE");
        newIntent.putExtra("part", "2");
        startActivity(newIntent);
    }

    public void startNewActivityGeneral3(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE");
        newIntent.putExtra("part", "3");
        startActivity(newIntent);
    }

    public void startNewActivityGeneral4(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE");
        newIntent.putExtra("part", "4");
        startActivity(newIntent);
    }

    public void startNewActivityGeneral5(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE");
        newIntent.putExtra("part", "5");
        startActivity(newIntent);
    }

    public void startNewActivityGeneral6(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE");
        newIntent.putExtra("part", "6");
        startActivity(newIntent);
    }

    public void startNewActivityGeneral7(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE");
        newIntent.putExtra("part", "7");
        startActivity(newIntent);
    }

    public void startNewActivityGeneral8(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE");
        newIntent.putExtra("part", "8");
        startActivity(newIntent);
    }

    public void startNewActivityGeneral9(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE");
        newIntent.putExtra("part", "9");
        startActivity(newIntent);
    }

    public void startNewActivityGeneral10(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE");
        newIntent.putExtra("part", "10");
        startActivity(newIntent);
    }

    public void startNewActivityGeneral11(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE");
        newIntent.putExtra("part", "11");
        startActivity(newIntent);
    }

    public void startNewActivityGeneral12(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE");
        newIntent.putExtra("part", "12");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralUA1(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE UA");
        newIntent.putExtra("part", "1");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralUA2(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE UA");
        newIntent.putExtra("part", "2");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralUA3(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE UA");
        newIntent.putExtra("part", "3");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralUA4(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE UA");
        newIntent.putExtra("part", "4");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralUA5(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE UA");
        newIntent.putExtra("part", "5");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralUA6(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE UA");
        newIntent.putExtra("part", "6");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralUA7(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE UA");
        newIntent.putExtra("part", "7");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralUA8(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE UA");
        newIntent.putExtra("part", "8");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralUA9(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE UA");
        newIntent.putExtra("part", "9");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralUA10(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE UA");
        newIntent.putExtra("part", "10");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralUA11(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE UA");
        newIntent.putExtra("part", "11");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralUA12(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE UA");
        newIntent.putExtra("part", "12");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralES1(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE ES");
        newIntent.putExtra("part", "1");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralES2(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE ES");
        newIntent.putExtra("part", "2");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralES3(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE ES");
        newIntent.putExtra("part", "3");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralES4(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE ES");
        newIntent.putExtra("part", "4");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralES5(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE ES");
        newIntent.putExtra("part", "5");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralES6(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE ES");
        newIntent.putExtra("part", "6");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralES7(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE ES");
        newIntent.putExtra("part", "7");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralES8(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE ES");
        newIntent.putExtra("part", "8");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralES9(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE ES");
        newIntent.putExtra("part", "9");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralES10(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE ES");
        newIntent.putExtra("part", "10");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralES11(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE ES");
        newIntent.putExtra("part", "11");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralES12(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE ES");
        newIntent.putExtra("part", "12");
        startActivity(newIntent);
    }


    public void startNewActivityAir1(View v) {
        Intent newIntent = new Intent(this,TestActivity.class);
        newIntent.putExtra("typeOfVariant", "AIR BRAKES");
        newIntent.putExtra("part", "1");
        startActivity(newIntent);
    }

    public void startNewActivityAir2(View v) {
        Intent newIntent = new Intent(this,TestActivity.class);
        newIntent.putExtra("typeOfVariant", "AIR BRAKES");
        newIntent.putExtra("part", "2");
        startActivity(newIntent);
    }

    public void startNewActivityAir3(View v) {
        Intent newIntent = new Intent(this,TestActivity.class);
        newIntent.putExtra("typeOfVariant", "AIR BRAKES");
        newIntent.putExtra("part", "3");
        startActivity(newIntent);
    }

    public void startNewActivityAir4(View v) {
        Intent newIntent = new Intent(this,TestActivity.class);
        newIntent.putExtra("typeOfVariant", "AIR BRAKES");
        newIntent.putExtra("part", "4");
        startActivity(newIntent);
    }

    //////
    public void startNewActivityAirUA1(View v) {
        Intent newIntent = new Intent(this,TestActivity.class);
        newIntent.putExtra("typeOfVariant", "AIR BRAKES UA");
        newIntent.putExtra("part", "1");
        startActivity(newIntent);
    }

    public void startNewActivityAirUA2(View v) {
        Intent newIntent = new Intent(this,TestActivity.class);
        newIntent.putExtra("typeOfVariant", "AIR BRAKES UA");
        newIntent.putExtra("part", "2");
        startActivity(newIntent);
    }

    public void startNewActivityAirUA3(View v) {
        Intent newIntent = new Intent(this,TestActivity.class);
        newIntent.putExtra("typeOfVariant", "AIR BRAKES UA");
        newIntent.putExtra("part", "3");
        startActivity(newIntent);
    }

    public void startNewActivityAirUA4(View v) {
        Intent newIntent = new Intent(this,TestActivity.class);
        newIntent.putExtra("typeOfVariant", "AIR BRAKES UA");
        newIntent.putExtra("part", "4");
        startActivity(newIntent);
    }

    public void startNewActivityAirES1(View v) {
        Intent newIntent = new Intent(this,TestActivity.class);
        newIntent.putExtra("typeOfVariant", "AIR BRAKES ES");
        newIntent.putExtra("part", "1");
        startActivity(newIntent);
    }

    public void startNewActivityAirES2(View v) {
        Intent newIntent = new Intent(this,TestActivity.class);
        newIntent.putExtra("typeOfVariant", "AIR BRAKES ES");
        newIntent.putExtra("part", "2");
        startActivity(newIntent);
    }

    public void startNewActivityAirES3(View v) {
        Intent newIntent = new Intent(this,TestActivity.class);
        newIntent.putExtra("typeOfVariant", "AIR BRAKES ES");
        newIntent.putExtra("part", "3");
        startActivity(newIntent);
    }

    public void startNewActivityAirES4(View v) {
        Intent newIntent = new Intent(this,TestActivity.class);
        newIntent.putExtra("typeOfVariant", "AIR BRAKES ES");
        newIntent.putExtra("part", "4");
        startActivity(newIntent);
    }

    ////////////

    /////////////////////////////
    public void startNewActivityCombinated1(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "COMBINATION VEHICLE");
        newIntent.putExtra("part", "1");
        startActivity(newIntent);
    }

    public void startNewActivityCombinated2(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "COMBINATION VEHICLE");
        newIntent.putExtra("part", "2");
        startActivity(newIntent);
    }

    public void startNewActivityCombinated3(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "COMBINATION VEHICLE");
        newIntent.putExtra("part", "3");
        startActivity(newIntent);
    }

    public void startNewActivityCombinated4(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "COMBINATION VEHICLE");
        newIntent.putExtra("part", "4");
        startActivity(newIntent);
    }
    ///////////////////////
    public void startNewActivityCombinatedUA1(View v) {
        Intent newIntent = new Intent(this,TestActivity.class);
        newIntent.putExtra("typeOfVariant", "COMBINATION VEHICLE UA");
        newIntent.putExtra("part", "1");
        startActivity(newIntent);
    }

    public void startNewActivityCombinatedUA2(View v) {
        Intent newIntent = new Intent(this,TestActivity.class);
        newIntent.putExtra("typeOfVariant", "COMBINATION VEHICLE UA");
        newIntent.putExtra("part", "2");
        startActivity(newIntent);
    }

    public void startNewActivityCombinatedUA3(View v) {
        Intent newIntent = new Intent(this,TestActivity.class);
        newIntent.putExtra("typeOfVariant", "COMBINATION VEHICLE UA");
        newIntent.putExtra("part", "3");
        startActivity(newIntent);
    }

    public void startNewActivityCombinatedUA4(View v) {
        Intent newIntent = new Intent(this,TestActivity.class);
        newIntent.putExtra("typeOfVariant", "COMBINATION VEHICLE UA");
        newIntent.putExtra("part", "4");
        startActivity(newIntent);
    }

    public void startNewActivityCombinatedES1(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "COMBINATION VEHICLE ES");
        newIntent.putExtra("part", "1");
        startActivity(newIntent);
    }

    public void startNewActivityCombinatedES2(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "COMBINATION VEHICLE ES");
        newIntent.putExtra("part", "2");
        startActivity(newIntent);
    }

    public void startNewActivityCombinatedES3(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "COMBINATION VEHICLE ES");
        newIntent.putExtra("part", "3");
        startActivity(newIntent);
    }

    public void startNewActivityCombinatedES4(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "COMBINATION VEHICLE ES");
        newIntent.putExtra("part", "4");
        startActivity(newIntent);
    }

    public void startNewActivityDriverLicense(View v) {
        Intent newIntent = new Intent(this,TestActivityDriverLicense.class);
        newIntent.putExtra("typeOfVariant", "Driver License RU");
        startActivity(newIntent);
    }

    public void startNewActivityDriverLicenseUA(View v) {
        Intent newIntent = new Intent(this,TestActivityDriverLicense.class);
        newIntent.putExtra("typeOfVariant", "Driver License UA");
        startActivity(newIntent);
    }

    public void openPreTripPage(View v) {
        Intent newIntent = new Intent(this,PreTripActivity.class);
        newIntent.putExtra("typeOfVariant", "PartA");
        startActivity(newIntent);
    }

    public void openPreTripPageB(View v) {
        Intent newIntent = new Intent(this,PreTripActivity.class);
        newIntent.putExtra("typeOfVariant", "PartB");
        startActivity(newIntent);
    }

    public void openPreTripPageC(View v) {
        Intent newIntent = new Intent(this,PreTripActivity.class);
        newIntent.putExtra("typeOfVariant", "PartC");
        startActivity(newIntent);
    }

    public void openPreTripCouplingSystem(View v) {
        Intent newIntent = new Intent(this,PreTripActivity.class);
        newIntent.putExtra("typeOfVariant", "CouplingSystem");
        startActivity(newIntent);
    }

    public void openVocabularyActivity(View v) {
        Intent intent = new Intent(this, VocabularyActivity.class);
        startActivity(intent);
    }

    public void openVocabularyActivityES(View v) {
        Intent intent = new Intent(this, VocabularyActivityES.class);
        // Log.d("DiagNN", "DiagNN1");
        startActivity(intent);
    }
    
    public void openPreTripInCabInspection(View v) {
        Intent newIntent = new Intent(this,PreTripActivity.class);
        newIntent.putExtra("typeOfVariant", "InCabInspection");
        startActivity(newIntent);
    }

    public void openCheckSignR(View v) {
        // Log.d("openCheckSign", "Try to open signR ");
        Intent newIntent = new Intent(this,VocabularyActivitySign.class);
        newIntent.putExtra("typeOfVariant", "R");
        startActivity(newIntent);
    }

    @Override
    public void onPremiumStatusChanged(boolean isPremium) {
        updateUI();
        // Toast removed to avoid spam on startup. Success toasts are handled in BillingManager.
    }

    public void openCheckSignW(View v) {
        // Log.d("openCheckSign", "Try to open signW ");
        Intent newIntent = new Intent(this,VocabularyActivitySign.class);
        newIntent.putExtra("typeOfVariant", "W");
        startActivity(newIntent);
    }

    public void openCheckSignO(View v) {
        // Log.d("openCheckSign", "Try to open signO ");
        Intent newIntent = new Intent(this, VocabularyActivitySign.class);
        newIntent.putExtra("typeOfVariant", "OM");
        startActivity(newIntent);
    }
    
    public void openCheckSignM(View v) {
        // Log.d("openCheckSign", "Try to open signM ");
        Intent newIntent = new Intent(this, VocabularyActivitySign.class);
        newIntent.putExtra("typeOfVariant", "M");
        startActivity(newIntent);
    }

    public void openCheckSignD(View v) {
        // Log.d("openCheckSign", "Try to open signD ");
        Intent newIntent = new Intent(this, VocabularyActivitySign.class);
        newIntent.putExtra("typeOfVariant", "D");
        startActivity(newIntent);
    }

    public void openCheckSignG(View v) {
        // Log.d("openCheckSign", "Try to open signG ");
        Intent newIntent = new Intent(this, VocabularyActivitySign.class);
        newIntent.putExtra("typeOfVariant", "G");
        startActivity(newIntent);
    }

    public void openCheckSignD9(View v) {
        // Log.d("openCheckSign", "Try to open signD9 ");
        Intent newIntent = new Intent(this, VocabularyActivitySign.class);
        newIntent.putExtra("typeOfVariant", "D9");
        startActivity(newIntent);
    }

    public void openCheckSignI(View v) {
        // Log.d("openCheckSign", "Try to open signI ");
        Intent newIntent = new Intent(this, VocabularyActivitySign.class);
        newIntent.putExtra("typeOfVariant", "I");
        startActivity(newIntent);
    }

    public void openCheckSignE(View v) {
        // Log.d("openCheckSign", "Try to open signE ");
        Intent newIntent = new Intent(this, VocabularyActivitySign.class);
        newIntent.putExtra("typeOfVariant", "E");
        startActivity(newIntent);
    }

    public void openCheckSignS(View v) {
        // Log.d("openCheckSign", "Try to open signS ");
        Intent newIntent = new Intent(this, VocabularyActivitySign.class);
        newIntent.putExtra("typeOfVariant", "S");
        startActivity(newIntent);
    }



    // Это открытие ссылки на отзыв чтобы оставить
    public void openReviewPage(View view) {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        try {
            startActivity(goToMarket);
        } catch (Exception e) {
            // Если не удается открыть Play Store, откроется веб-страница
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    // Метод для отображения диалога
    private void showRateAppDialog(String TitleCompareText, String MessageCompareText, String PositiveCompareText, String NegativeCompareText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(TitleCompareText)
                .setMessage(MessageCompareText)
                .setPositiveButton(PositiveCompareText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openAppInGooglePlay();
                        saveLastPromptDate(); // Сохранить дату показа
                    }
                })
                .setNegativeButton(NegativeCompareText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveLastPromptDate(); // Сохранить дату показа
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    // Метод для открытия страницы приложения в Google Play
    private void openAppInGooglePlay() {
        String packageName = getPackageName(); // Получение имени пакета текущего приложения
        try {
            // Попробовать открыть через приложение Google Play
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        } catch (ActivityNotFoundException e) {
            // Если Google Play недоступен, открыть через браузер
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    // Проверяем, нужно ли показывать диалог
    private boolean shouldShowRateDialog() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        long firstLaunchTime = prefs.getLong(FIRST_LAUNCH_DATE, 0);
        long lastPromptTime = prefs.getLong(LAST_PROMPT_DATE, 0);

        long currentTime = Calendar.getInstance().getTimeInMillis();

        // Проверяем, прошло ли достаточно времени с момента первого запуска
        if (currentTime - firstLaunchTime < FIRST_PROMPT_DELAY * 24 * 60 * 60 * 1000L) {
            return false;
        }

        // Проверяем, прошло ли достаточно времени с момента последнего показа
        return (currentTime - lastPromptTime) >= (DAYS_UNTIL_NEXT_PROMPT * 24 * 60 * 60 * 1000L);
    }

    // Сохраняем текущую дату как дату первого запуска
    private void saveFirstLaunchDate() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (!prefs.contains(FIRST_LAUNCH_DATE)) {
            SharedPreferences.Editor editor = prefs.edit();
            long currentTime = Calendar.getInstance().getTimeInMillis();
            editor.putLong(FIRST_LAUNCH_DATE, currentTime);
            editor.apply();
        }
    }

    // Сохраняем текущую дату как последнюю дату показа
    private void saveLastPromptDate() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        long currentTime = Calendar.getInstance().getTimeInMillis();
        editor.putLong(LAST_PROMPT_DATE, currentTime);
        editor.apply();
    }

    /// Метод для отображения диалогового окна
    private void showLanguageDialog() {
        String[] languages = {"Русский", "English", "Español"};
        new AlertDialog.Builder(this)
                .setTitle("Language (язык)")
                .setItems(languages, (dialog, which) -> {
                    String selectedLanguage;
                    switch (which) {
                        case 0:
                            selectedLanguage = "ru";
                            break;
                        case 1:
                            selectedLanguage = "eng";
                            break;
                        case 2:
                            selectedLanguage = "es";
                            break;
                        default:
                            selectedLanguage = "eng"; // По умолчанию
                    }
                    saveLanguagePreference(selectedLanguage);
                    updateAppLanguage(selectedLanguage);
                })
                .setNegativeButton("Cancel (Отмена)", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Метод для сохранения языка в SharedPreferences
    private void saveLanguagePreference(String language) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("appLanguage", language);
        editor.apply();
        Log.d("Language", "We change language: " + language);
    }

    // Метод для обновления локали приложения (обновление интерфейса)
    private void updateAppLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        getResources().getConfiguration().setLocale(locale);
        recreate(); // Перезапускаем активность для применения языка
    }

    private void processQuestionType(String[] questions, String keyPrefix, String buttonPrefix, int buttonRangeStart) {
        // Рассчитать количество вопросов на основе длины массива и диапазона
        int countQuestionOfType = (questions.length + 20 - 1) / 20;
        Log.d("CountQuestion", "Count " + keyPrefix + ": " + countQuestionOfType);

        // Обойти кнопки и обновить их статус
        for (int i = buttonRangeStart; i <= countQuestionOfType; i++) {
            String testKey = keyPrefix + "_" + i;
            Log.d("CountQuestionTestKey", testKey);

            int buttonId = getResources().getIdentifier(buttonPrefix + i, "id", getPackageName());
            updateTestButton(testKey, buttonId, i);
        }
    }

    private void updateTestButton(String testKey, int buttonId, int i) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String testStatus = sharedPreferences.getString(testKey, "not_taken");

        // Находим кнопку
        Button testButton = findViewById(buttonId);
        testButton.setVisibility(View.VISIBLE); // Делает кнопку видимой
        testButton.setEnabled(true);
        testButton.setText(ChapterText + " " + i);

        // Log.d("testStatus---", testKey);

        // Устанавливаем цвет кнопки на основе статуса
        switch (testStatus) {
            case "pass":
                testButton.setBackgroundColor(Color.GREEN); // Зелёная кнопка, если тест сдан
                break;
            case "not pass":
                testButton.setBackgroundColor(Color.RED); // Красная кнопка, если тест не сдан
                break;
            case "not_taken":
            default:
                // Цвет кнопки по умолчанию (например, серый)
                testButton.setBackgroundColor(Color.GRAY);
                break;
        }
    }

}
