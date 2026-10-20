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
import android.widget.ImageView;
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

    // reviews goople-play / rating card
    private static final String PREFS_NAME = "RateAppPrefs";
    private static final String FIRST_LAUNCH_DATE = "firstLaunchDate";
    private static final String LAST_PROMPT_DATE = "lastPromptDate";
    private static final String KEY_HAS_RATED = "hasRated";
    private static final int DAYS_UNTIL_NEXT_PROMPT = 5; // Повторный показ через 5 дней

    private String appLanguage;
    private String ChapterText;
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

            // Определяем язык
            if ("ru".equals(systemLanguage)) {
                appLanguage = "ru";
            } else if ("es".equals(systemLanguage)) {
                appLanguage = "es";
            } else if ("fr".equals(systemLanguage)) {
                appLanguage = "fr";
            } else {
                appLanguage = "eng";
            }

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
        LinearLayout linearLayoutTheoryFR = findViewById(R.id.linear_theoryFR);
        Button learnEnglishButton = findViewById(R.id.learnEnglish);
        Button learnEnglishSpanishButton = findViewById(R.id.learnEnglishSpanish);
        Button learnEnglishFrenchButton = findViewById(R.id.learnEnglishFrench);


        if ("eng".equals(appLanguage)) {
            // Или скрыть полностью (не занимает место в макете)
            linearLayoutTheoryRU.setVisibility(View.GONE);
            linearLayoutTheoryES.setVisibility(View.GONE);
            if (linearLayoutTheoryFR != null) linearLayoutTheoryFR.setVisibility(View.GONE);
            linearLayoutTheoryUA.setVisibility(View.VISIBLE);
            learnEnglishButton.setVisibility(View.GONE);
            learnEnglishSpanishButton.setVisibility(View.GONE);
            if (learnEnglishFrenchButton != null) learnEnglishFrenchButton.setVisibility(View.GONE);
            String titleCaption = "Change language";
            ChapterText = "Chapter";
            RemoveAdsText = "Remove Ads";
        }
        if ("ru".equals(appLanguage)) {
            linearLayoutTheoryUA.setVisibility(View.GONE);
            linearLayoutTheoryES.setVisibility(View.GONE);
            if (linearLayoutTheoryFR != null) linearLayoutTheoryFR.setVisibility(View.GONE);
            linearLayoutTheoryRU.setVisibility(View.VISIBLE);
            learnEnglishButton.setVisibility(View.VISIBLE);
            learnEnglishSpanishButton.setVisibility(View.GONE);
            if (learnEnglishFrenchButton != null) learnEnglishFrenchButton.setVisibility(View.GONE);
            String titleCaption = "Выберите язык";
            ChapterText = "Часть ";
            RemoveAdsText = "Убрать рекламу";
        }
        if ("es".equals(appLanguage)) {
            linearLayoutTheoryUA.setVisibility(View.GONE);
            linearLayoutTheoryRU.setVisibility(View.GONE);
            if (linearLayoutTheoryFR != null) linearLayoutTheoryFR.setVisibility(View.GONE);
            linearLayoutTheoryES.setVisibility(View.VISIBLE);
            learnEnglishButton.setVisibility(View.GONE);
            learnEnglishSpanishButton.setVisibility(View.VISIBLE);
            if (learnEnglishFrenchButton != null) learnEnglishFrenchButton.setVisibility(View.GONE);
            String titleCaption = "Выберите язык";
            ChapterText = "Parte ";
            RemoveAdsText = "Quitar anuncios";
        }
        if ("fr".equals(appLanguage)) {
            linearLayoutTheoryUA.setVisibility(View.GONE);
            linearLayoutTheoryRU.setVisibility(View.GONE);
            linearLayoutTheoryES.setVisibility(View.GONE);
            if (linearLayoutTheoryFR != null) linearLayoutTheoryFR.setVisibility(View.VISIBLE);
            learnEnglishButton.setVisibility(View.GONE);
            learnEnglishSpanishButton.setVisibility(View.GONE);
            if (learnEnglishFrenchButton != null) learnEnglishFrenchButton.setVisibility(View.VISIBLE);
            String titleCaption = "Choisir la langue";
            ChapterText = "Partie ";
            RemoveAdsText = "Supprimer les pubs";
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
        
        if ("ru".equals(appLanguage)) {
            tvRestorePurchases.setText("Уже покупали? Восстановить доступ");
        } else if ("es".equals(appLanguage)) {
            tvRestorePurchases.setText("¿Ya lo compraste? Restaurar acceso");
        } else if ("fr".equals(appLanguage)) {
            tvRestorePurchases.setText("Déjà acheté ? Restaurer l'accès");
        } else {
            tvRestorePurchases.setText("Already purchased? Restore access");
        }
        tvRestorePurchases.setOnClickListener(v -> billingManager.restorePurchases());
        
        updateUI();


        ///////////////////////////////

        // Сохранить дату первого запуска, если это первый запуск
        saveFirstLaunchDate();

        // Настройка карточки оценки приложения
        setupRateCard(appLanguage);

        // Настройка промо-карточек приложений и их локализация
        setupPromoToolsSection(appLanguage);

        // Настройка локализации секции дорожных знаков
        setupRoadSignsSection(appLanguage);

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

        setupToggle(findViewById(R.id.tvTitleFR4), findViewById(R.id.FRContent4));
        setupToggle(findViewById(R.id.tvTitleFR5), findViewById(R.id.FRContent5));
        setupToggle(findViewById(R.id.tvTitleFR6), findViewById(R.id.FRContent6));
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
        refreshAllTestButtons();

    }

    private void refreshAllTestButtons() {
        processQuestionType(QuestionGeneral.question, "GENERAL KNOWLEDGE", "btn1_", 1);
        processQuestionType(QuestionAir.question, "AIR BRAKES", "btn2_", 1);
        processQuestionType(QuestionCombinated.question, "COMBINATION VEHICLE", "btn3_", 1);

        processQuestionType(QuestionGeneral.question, "GENERAL KNOWLEDGE UA", "btnUA1_", 1);
        processQuestionType(QuestionAir.question, "AIR BRAKES UA", "btnUA2_", 1);
        processQuestionType(QuestionCombinated.question, "COMBINATION VEHICLE UA", "btnUA3_", 1);

        processQuestionType(QuestionGeneral.question, "GENERAL KNOWLEDGE ES", "btnES1_", 1);
        processQuestionType(QuestionAir.question, "AIR BRAKES ES", "btnES2_", 1);
        processQuestionType(QuestionCombinated.question, "COMBINATION VEHICLE ES", "btnES3_", 1);

        processQuestionType(QuestionGeneral.question, "GENERAL KNOWLEDGE FR", "btnFR1_", 1);
        processQuestionType(QuestionAir.question, "AIR BRAKES FR", "btnFR2_", 1);
        processQuestionType(QuestionCombinated.question, "COMBINATION VEHICLE FR", "btnFR3_", 1);
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
        refreshAllTestButtons();
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

    public void startNewActivityGeneralFR1(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE FR");
        newIntent.putExtra("part", "1");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralFR2(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE FR");
        newIntent.putExtra("part", "2");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralFR3(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE FR");
        newIntent.putExtra("part", "3");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralFR4(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE FR");
        newIntent.putExtra("part", "4");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralFR5(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE FR");
        newIntent.putExtra("part", "5");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralFR6(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE FR");
        newIntent.putExtra("part", "6");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralFR7(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE FR");
        newIntent.putExtra("part", "7");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralFR8(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE FR");
        newIntent.putExtra("part", "8");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralFR9(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE FR");
        newIntent.putExtra("part", "9");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralFR10(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE FR");
        newIntent.putExtra("part", "10");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralFR11(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE FR");
        newIntent.putExtra("part", "11");
        startActivity(newIntent);
    }

    public void startNewActivityGeneralFR12(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "GENERAL KNOWLEDGE FR");
        newIntent.putExtra("part", "12");
        startActivity(newIntent);
    }

    public void startNewActivityAirFR1(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "AIR BRAKES FR");
        newIntent.putExtra("part", "1");
        startActivity(newIntent);
    }

    public void startNewActivityAirFR2(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "AIR BRAKES FR");
        newIntent.putExtra("part", "2");
        startActivity(newIntent);
    }

    public void startNewActivityAirFR3(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "AIR BRAKES FR");
        newIntent.putExtra("part", "3");
        startActivity(newIntent);
    }

    public void startNewActivityAirFR4(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "AIR BRAKES FR");
        newIntent.putExtra("part", "4");
        startActivity(newIntent);
    }

    public void startNewActivityCombinatedFR1(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "COMBINATION VEHICLE FR");
        newIntent.putExtra("part", "1");
        startActivity(newIntent);
    }

    public void startNewActivityCombinatedFR2(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "COMBINATION VEHICLE FR");
        newIntent.putExtra("part", "2");
        startActivity(newIntent);
    }

    public void startNewActivityCombinatedFR3(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "COMBINATION VEHICLE FR");
        newIntent.putExtra("part", "3");
        startActivity(newIntent);
    }

    public void startNewActivityCombinatedFR4(View v) {
        Intent newIntent = new Intent(this, TestActivity.class);
        newIntent.putExtra("typeOfVariant", "COMBINATION VEHICLE FR");
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
    // Настройка верхней и нижней карточек оценки приложения
    private void setupRateCard(String language) {
        View cardRateApp = findViewById(R.id.card_rate_app);
        View cardRateAppBottom = findViewById(R.id.card_rate_app_bottom);
        if (cardRateApp == null) return;

        TextView tvTitle = findViewById(R.id.tv_rate_card_title);
        TextView tvSubtitle = findViewById(R.id.tv_rate_card_subtitle);
        Button btnAction = findViewById(R.id.btn_rate_card_action);
        ImageView btnClose = findViewById(R.id.btn_close_rate_card);

        TextView tvBottomTitle = findViewById(R.id.tv_rate_card_bottom_title);
        TextView tvBottomSubtitle = findViewById(R.id.tv_rate_card_bottom_subtitle);
        Button btnBottomAction = findViewById(R.id.btn_rate_card_bottom_action);

        String titleText;
        String subtitleText;
        String actionText;

        if ("ru".equals(language)) {
            titleText = "Оцените наше приложение";
            subtitleText = "Оставь отзыв в Google Play";
            actionText = "Оценить в 1 клик";
        } else if ("es".equals(language)) {
            titleText = "Califica nuestra app";
            subtitleText = "Deja una reseña en Google Play";
            actionText = "Calificar en 1 clic";
        } else if ("fr".equals(language)) {
            titleText = "Évaluez notre application";
            subtitleText = "Laissez un avis sur Google Play";
            actionText = "Noter en 1 clic";
        } else { // "eng" or default
            titleText = "Rate our app";
            subtitleText = "Leave a review on Google Play";
            actionText = "Rate in 1 click";
        }

        if (tvTitle != null) tvTitle.setText(titleText);
        if (tvSubtitle != null) tvSubtitle.setText(subtitleText);
        if (btnAction != null) btnAction.setText(actionText);

        if (tvBottomTitle != null) tvBottomTitle.setText(titleText);
        if (tvBottomSubtitle != null) tvBottomSubtitle.setText(subtitleText);
        if (btnBottomAction != null) btnBottomAction.setText(actionText);

        if (btnBottomAction != null) {
            btnBottomAction.setOnClickListener(v -> {
                markAsRated();
                openAppInGooglePlay();
            });
        }

        // Если верхний блок должен быть открыт -> верхний открыт, нижний закрыт.
        // Если верхний блок закрыт -> верхний закрыт, нижний открыт постоянно.
        if (shouldShowRateBlock()) {
            cardRateApp.setVisibility(View.VISIBLE);
            if (cardRateAppBottom != null) {
                cardRateAppBottom.setVisibility(View.GONE);
            }
        } else {
            cardRateApp.setVisibility(View.GONE);
            if (cardRateAppBottom != null) {
                cardRateAppBottom.setVisibility(View.VISIBLE);
            }
        }

        btnClose.setOnClickListener(v -> {
            cardRateApp.setVisibility(View.GONE);
            if (cardRateAppBottom != null) {
                cardRateAppBottom.setVisibility(View.VISIBLE);
            }
            saveLastPromptDate(); // Сохраняем дату закрытия крестиком, отсчитываем 5 дней для верхнего блока
        });

        btnAction.setOnClickListener(v -> {
            markAsRated(); // Пользователь нажал оценить — верхний блок больше никогда не выводится
            cardRateApp.setVisibility(View.GONE);
            if (cardRateAppBottom != null) {
                cardRateAppBottom.setVisibility(View.VISIBLE);
            }
            openAppInGooglePlay();
        });
    }

    // Настройка и локализация секции CDL Practical Tools & Simulator
    private void setupPromoToolsSection(String language) {
        View cardTruckParking = findViewById(R.id.card_truck_parking);
        if (cardTruckParking != null) {
            cardTruckParking.setOnClickListener(this::openTruckParkingApp);
        }
        View cardAxleWeight = findViewById(R.id.card_axle_weight);
        if (cardAxleWeight != null) {
            cardAxleWeight.setOnClickListener(this::openAxleWeightApp);
        }

        TextView tvSectionTitle = findViewById(R.id.tv_tools_section_title);
        TextView tvParkingTitle = findViewById(R.id.tv_parking_title);
        TextView tvParkingBadge = findViewById(R.id.tv_parking_badge);
        TextView tvParkingDesc = findViewById(R.id.tv_parking_desc);
        Button btnParkingApp = findViewById(R.id.btn_parking_app);

        TextView tvAxleTitle = findViewById(R.id.tv_axle_title);
        TextView tvAxleBadge = findViewById(R.id.tv_axle_badge);
        TextView tvAxleDesc = findViewById(R.id.tv_axle_desc);
        Button btnAxleApp = findViewById(R.id.btn_axle_app);

        if ("ru".equals(language)) {
            if (tvSectionTitle != null) tvSectionTitle.setText("Практические инструменты");
            if (tvParkingTitle != null) tvParkingTitle.setText("🚛  Симулятор парковки трака CDL");
            if (tvParkingBadge != null) tvParkingBadge.setText("Маневры и площадка");
            if (tvParkingDesc != null) tvParkingDesc.setText("Освойте парковку 90° Alley Dock, Offset Backing и параллельную парковку перед экзаменом. Реальная физика 53-футового прицепа!");
            if (btnParkingApp != null) btnParkingApp.setText("Тренировать парковку");

            if (tvAxleTitle != null) tvAxleTitle.setText("⚖️  Калькулятор веса по осям");
            if (tvAxleBadge != null) tvAxleBadge.setText("Расчет тандема и DOT");
            if (tvAxleDesc != null) tvAxleDesc.setText("Хватит гадать на весах CAT. Рассчитайте сдвиг тандема по отверстиям (шаг 4″ и 6″) и мгновенно сбалансируйте вес на осях.");
            if (btnAxleApp != null) btnAxleApp.setText("Открыть калькулятор");
        } else if ("es".equals(language)) {
            if (tvSectionTitle != null) tvSectionTitle.setText("Herramientas prácticas");
            if (tvParkingTitle != null) tvParkingTitle.setText("🚛  Simulador de estacionamiento CDL");
            if (tvParkingBadge != null) tvParkingBadge.setText("Maniobras en patio");
            if (tvParkingDesc != null) tvParkingDesc.setText("Domina el estacionamiento en 90° Alley Dock, retroceso desplazado y paralelo antes de tu examen. ¡Física real de remolque de 53 pies!");
            if (btnParkingApp != null) btnParkingApp.setText("Practicar maniobras");

            if (tvAxleTitle != null) tvAxleTitle.setText("⚖️  Calculadora de peso por eje");
            if (tvAxleBadge != null) tvAxleBadge.setText("Ajuste de tándem y DOT");
            if (tvAxleDesc != null) tvAxleDesc.setText("Deja de adivinar en las básculas CAT. Calcula el ajuste de los pines del tándem (4″ y 6″) y equilibra el peso de los ejes al instante.");
            if (btnAxleApp != null) btnAxleApp.setText("Abrir calculadora");
        } else if ("fr".equals(language)) {
            if (tvSectionTitle != null) tvSectionTitle.setText("Outils pratiques");
            if (tvParkingTitle != null) tvParkingTitle.setText("🚛  Simulateur de stationnement CDL");
            if (tvParkingBadge != null) tvParkingBadge.setText("Manœuvres sur piste");
            if (tvParkingDesc != null) tvParkingDesc.setText("Maîtrisez le stationnement à 90° Alley Dock, le recul décalé et le créneau avant votre examen. Physique réelle de remorque de 53 pieds !");
            if (btnParkingApp != null) btnParkingApp.setText("S'entraîner aux manœuvres");

            if (tvAxleTitle != null) tvAxleTitle.setText("⚖️  Calculateur de poids par essieu");
            if (tvAxleBadge != null) tvAxleBadge.setText("Réglage tandem et DOT");
            if (tvAxleDesc != null) tvAxleDesc.setText("Ne devinez plus sur les balances CAT. Calculez le décalage des trous du tandem (4″ et 6″) et équilibrez le poids des essieux instantanément.");
            if (btnAxleApp != null) btnAxleApp.setText("Ouvrir le calculateur");
        } else { // "eng" or default
            if (tvSectionTitle != null) tvSectionTitle.setText("Practical Tools");
            if (tvParkingTitle != null) tvParkingTitle.setText("🚛  CDL Truck Parking Simulator");
            if (tvParkingBadge != null) tvParkingBadge.setText("Yard Skills & Maneuvers");
            if (tvParkingDesc != null) tvParkingDesc.setText("Master 90° Alley Dock, Offset Backing, and Parallel Parking before your road test. Real 53ft trailer articulated physics!");
            if (btnParkingApp != null) btnParkingApp.setText("Practice Backing");

            if (tvAxleTitle != null) tvAxleTitle.setText("⚖️  Axle Weight Scale for Trucker");
            if (tvAxleBadge != null) tvAxleBadge.setText("DOT & Tandem Slide Tool");
            if (tvAxleDesc != null) tvAxleDesc.setText("Stop guessing at CAT scales. Calculate tandem hole shifts (4″ & 6″ spacing) and balance drive/trailer axle weights instantly.");
            if (btnAxleApp != null) btnAxleApp.setText("Open Calculator");
        }
    }

    // Локализация секции дорожных знаков (Road signs in the USA)
    private void setupRoadSignsSection(String language) {
        TextView tvRoadSignsTitle = findViewById(R.id.tv_road_signs_title);
        Button btnSignR = findViewById(R.id.check_sign_r);
        Button btnSignW = findViewById(R.id.check_sign_w);
        Button btnSignO = findViewById(R.id.check_sign_o);
        Button btnSignM = findViewById(R.id.check_sign_m);
        Button btnSignD = findViewById(R.id.check_sign_d);
        Button btnSignG = findViewById(R.id.check_sign_g);
        Button btnSignD9 = findViewById(R.id.check_sign_d9);
        Button btnSignI = findViewById(R.id.check_sign_i);
        Button btnSignE = findViewById(R.id.check_sign_e);
        Button btnSignS = findViewById(R.id.check_sign_s);

        if ("fr".equals(language)) {
            if (tvRoadSignsTitle != null) tvRoadSignsTitle.setText("Panneaux routiers aux USA");
            if (btnSignR != null) btnSignR.setText("Panneaux de réglementation");
            if (btnSignW != null) btnSignW.setText("Panneaux d'avertissement");
            if (btnSignO != null) btnSignO.setText("Balises d'obstacles");
            if (btnSignM != null) btnSignM.setText("Balises d'itinéraire");
            if (btnSignD != null) btnSignD.setText("Panneaux de direction");
            if (btnSignG != null) btnSignG.setText("Travaux routiers");
            if (btnSignD9 != null) btnSignD9.setText("Services aux conducteurs");
            if (btnSignI != null) btnSignI.setText("Informations générales");
            if (btnSignE != null) btnSignE.setText("Autoroutes et voies rapides");
            if (btnSignS != null) btnSignS.setText("Zone scolaire");
        } else {
            if (tvRoadSignsTitle != null) tvRoadSignsTitle.setText("Road signs in the USA");
            if (btnSignR != null) btnSignR.setText("Regulatory signs");
            if (btnSignW != null) btnSignW.setText("Warning signs");
            if (btnSignO != null) btnSignO.setText("Object markers");
            if (btnSignM != null) btnSignM.setText("Route marker");
            if (btnSignD != null) btnSignD.setText("Destination signs");
            if (btnSignG != null) btnSignG.setText("Construction");
            if (btnSignD9 != null) btnSignD9.setText("Motorist services");
            if (btnSignI != null) btnSignI.setText("General");
            if (btnSignE != null) btnSignE.setText("Freeway, expressway");
            if (btnSignS != null) btnSignS.setText("School");
        }
    }

    // Проверяем, нужно ли показывать карточку оценки
    private boolean shouldShowRateBlock() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        // Если пользователь уже нажал оценить — блок больше никогда не выводится
        if (prefs.getBoolean(KEY_HAS_RATED, false)) {
            return false;
        }

        long lastPromptTime = prefs.getLong(LAST_PROMPT_DATE, 0);
        long currentTime = Calendar.getInstance().getTimeInMillis();

        // Если крестик нажимался, проверяем, прошло ли 5 дней
        if (lastPromptTime > 0) {
            return (currentTime - lastPromptTime) >= (DAYS_UNTIL_NEXT_PROMPT * 24 * 60 * 60 * 1000L);
        }

        return true;
    }

    // Отметка о том, что пользователь нажал оценить
    private void markAsRated() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_HAS_RATED, true);
        editor.apply();
    }

    // Метод для открытия страницы приложения в Google Play
    private void openAppInGooglePlay() {
        openPlayStoreApp(getPackageName());
    }

    // Переход на приложение симулятора парковки
    public void openTruckParkingApp(View view) {
        openPlayStoreApp("com.drive.cdlracetruck");
    }

    // Переход на приложение калькулятора развесовки
    public void openAxleWeightApp(View view) {
        openPlayStoreApp("com.drive.axleweightcalc");
    }

    private void openPlayStoreApp(String packageName) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
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
        String[] languages = {"Русский", "English", "Español", "Français"};
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
                        case 3:
                            selectedLanguage = "fr";
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
                testButton.setTextColor(Color.WHITE);
                break;
            case "not pass":
                testButton.setBackgroundColor(Color.RED); // Красная кнопка, если тест не сдан
                testButton.setTextColor(Color.WHITE);
                break;
            case "in_progress":
                testButton.setBackgroundColor(Color.YELLOW); // Жёлтая кнопка, если тест начат, но не завершён
                testButton.setTextColor(Color.BLACK);
                break;
            case "not_taken":
            default:
                // Цвет кнопки по умолчанию (например, серый)
                testButton.setBackgroundColor(Color.GRAY);
                testButton.setTextColor(Color.WHITE);
                break;
        }
    }

}
