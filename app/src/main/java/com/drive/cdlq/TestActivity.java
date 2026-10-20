package com.drive.cdlq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Arrays;

import java.util.Locale;
import java.util.Random;

import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.OnUserEarnedRewardListener;

public class TestActivity extends AppCompatActivity implements BillingManager.OnPremiumListener {
    
    private BillingManager billingManager;

    private String part;
    private String keyName;

    private static final int COLOR_GREEN = Color.GREEN;
    private static final int COLOR_RED = Color.RED;
    private static final int COLOR_WHITE = Color.WHITE;

    private AdView adView;
    // Test Ad Unit ID
    //private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741";
    //private static final String AD_UNIT_ID = "ca-app-pub-7073626021965181/8578191229"; // с заблокированным полиси
    // The container for our ad unit. ca-app-pub-7073626021965181/8578191229
    private FrameLayout adContainerView;

    TextView totalQuestionsTextView;
    TextView questionTextView, textView, total_right,all_RIght;
    Button ansA, ansB, ansC;
    Button submitBtn;

    RadioGroup radiogroup;

    private String[] QuestionAnswer;
    private String[][] QuestionAnswerChoices;
    private String[] QuestionAnswerCorrect;

    private int score;
    private int totalQuestion;
    private int currentQuestionIndex;
    private String selectedAnswer;

    private String captionQuestion;
    private String  captionResult;
    private String  captionPart;
    private String  captionQuestionOnly;
    private String  captionOf;
    private String  CaptionResultStatus;
    private String captionStatusPass;
    private String captionStatusNotPass;
    private String captionRestart;
    private String captionNext = "Next";
    private String captionArrayQuestions = "To the list of questions";

    private String appLanguage;

    private RewardedInterstitialAd rewardedInterstitialAd;
    private boolean hasAdBeenShown = false;

    private String adThanks;
    private String  adMessage;
    private String  adButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        MobileAds.initialize(this, initializationStatus -> {});
        
        // Init Billing
        billingManager = new BillingManager(this);
        
        adContainerView = findViewById(R.id.ad_view);
        adContainerView.post(this::loadBanner);

        int launchCount = LaunchCounter.increaseLaunchCount(this);

        if (launchCount == 1 || launchCount % 5 == 0) {
            loadRewardedInterstitialAd(); // и покажи, как уже настроено
        }

        // Получаем SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Проверяем, существует ли переменная для языка
        appLanguage = sharedPreferences.getString("appLanguage", null); // Получаем сохранённый язык

        if ("eng".equals(appLanguage)) {
            captionQuestion = "Question 1 of ";
            captionResult = "Result: 0 of 0";
            captionPart = "Part ";
            captionQuestionOnly = "Question ";
            captionOf = " of ";
            CaptionResultStatus = "Result: ";
            captionStatusPass = "Pass";
            captionStatusNotPass = "Not Pass";
            captionRestart = "Restart Test";
            captionNext = "Next";
            captionArrayQuestions = "Back to Questions";
                        adThanks = "Unlock Part";
            adMessage = "Watch an ad to unlock the next part";
            adButton = "Continue";
        } if ("ru".equals(appLanguage)) {
            captionQuestion = "Вопрос 1 из ";
            captionResult = "Результат: 0 из 0";
            captionPart = "Часть ";
            captionQuestionOnly = "Вопрос ";
            captionOf = " из ";
            CaptionResultStatus = "Результат: ";
            captionStatusPass = "Пройдено";
            captionStatusNotPass = "Не пройдено";
            captionRestart = "Перезапустить тест";
            captionNext = "Далее";
            captionArrayQuestions = "К списку вопросов";
            adThanks = "Разблокировать часть";
            adMessage = "Посмотрите рекламу, чтобы разблокировать следующую часть";
            adButton = "Продолжить";
        } if ("es".equals(appLanguage)) {
            captionQuestion = "Pregunta 1 de ";
            captionResult = "Resultado: 0 de 0";
            captionPart = "Parte ";
            captionQuestionOnly = "Pregunta ";
            captionOf = " de ";
            CaptionResultStatus = "Resultado: ";
            captionStatusPass = "Aprobado";
            captionStatusNotPass = "No aprobado";
            captionRestart = "Reiniciar prueba";
            captionNext = "Siguiente";
            captionArrayQuestions = "A la lista de preguntas";
            adThanks = "Desbloquear parte";
            adMessage = "Mira un anuncio para desbloquear la siguiente parte";
            adButton = "Continuar";
        } if ("fr".equals(appLanguage)) {
            captionQuestion = "Question 1 sur ";
            captionResult = "Résultat : 0 sur 0";
            captionPart = "Partie ";
            captionQuestionOnly = "Question ";
            captionOf = " sur ";
            CaptionResultStatus = "Résultat : ";
            captionStatusPass = "Réussi";
            captionStatusNotPass = "Échoué";
            captionRestart = "Recommencer le test";
            captionNext = "Suivant";
            captionArrayQuestions = "Retour aux questions";
            adThanks = "Débloquer la partie";
            adMessage = "Regardez une publicité pour débloquer la partie suivante";
            adButton = "Continuer";
        }

        Button submitButton = findViewById(R.id.submit_btn);
        submitButton.setText(captionNext);

        Button buttonMainMenu = findViewById(R.id.buttonMainMenu);
        buttonMainMenu.setText(captionArrayQuestions);

        // Получаем текст из предыдущего меню, он же параметр
        Bundle arguments = getIntent().getExtras();
        String name = arguments.get("typeOfVariant").toString();

        // получаем часть того что нам надо отобразить PART
        part = arguments.get("part").toString();

        keyName = name + "_" + part;

        // Помечаем начатое задание жёлтым ("in_progress"), пока тест не пройден до конца
        sharedPreferences.edit().putString(keyName, "in_progress").apply();

        Integer start;
        Integer end;

        start = 0; end = 19;

        if (part.equals("1")) {
            start = 0;
            end = 20;
        } else if (part.equals("2")) {
            start = 20;
            end = 40;
        } else if (part.equals("3")) {
            start = 40;
            end = 60;
        } else if (part.equals("4")) {
            start = 60;
            end = 80;
        } else if (part.equals("5")) {
            start = 80;
            end = 100;
        } else if (part.equals("6")) {
            start = 100;
            end = 120;
        } else if (part.equals("7")) {
            start = 120;
            end = 140;
        } else if (part.equals("8")) {
            start = 140;
            end = 160;
        } else if (part.equals("9")) {
            start = 160;
            end = 180;
        } else if (part.equals("10")) {
            start = 180;
            end = 200;
        } else if (part.equals("11")) {
            start = 200;
            end = 220;
        } else if (part.equals("12")) {
            start = 220;
            end = 240;
        } else if (part.equals("13")) {
            start = 240;
            end = 260;
        } else if (part.equals("14")) {
            start = 260;
            end = 280;
        } else if (part.equals("15")) {
            start = 280;
            end = 300;
        }

        if (name.equals("AIR BRAKES") && (part.equals("4"))) {  start = 60; end = 70;}
        if (name.equals("AIR BRAKES UA") && (part.equals("4"))) {  start = 60; end = 70;}
        if (name.equals("AIR BRAKES ES") && (part.equals("4"))) {  start = 60; end = 70;}
        if (name.equals("AIR BRAKES FR") && (part.equals("4"))) {  start = 60; end = 70;}

        if (name.equals("COMBINATION VEHICLE") && (part.equals("4"))) {  start = 60; end = 70;}
        if (name.equals("COMBINATION VEHICLE UA") && (part.equals("4"))) {  start = 60; end = 70;}
        if (name.equals("COMBINATION VEHICLE ES") && (part.equals("4"))) {  start = 60; end = 70;}
        if (name.equals("COMBINATION VEHICLE FR") && (part.equals("4"))) {  start = 60; end = 70;}

        textView = findViewById(R.id.textHeader);
        String newName = name;
        newName = newName.replace(" UA", "").replace(" FR", "").replace(" ES", "");
        textView.setText(newName);

        textView = findViewById(R.id.textPart);
        textView.setText(captionPart + part);


        totalQuestionsTextView = findViewById(R.id.total_right);
        questionTextView = findViewById(R.id.question);
        ansA = findViewById(R.id.ans_A);
        ansB = findViewById(R.id.ans_B);
        ansC = findViewById(R.id.ans_C);
        submitBtn = findViewById(R.id.submit_btn);

        radiogroup = findViewById(R.id.RadioGroup1);

        if (name.equals("GENERAL KNOWLEDGE")) {
            QuestionAnswer = QuestionGeneral.question;
            QuestionAnswerChoices = QuestionGeneral.choices;
            QuestionAnswerCorrect = QuestionGeneral.correctAnswers;
        } else if (name.equals("AIR BRAKES")) {
            QuestionAnswer = QuestionAir.question;
            QuestionAnswerChoices = QuestionAir.choices;
            QuestionAnswerCorrect = QuestionAir.correctAnswers;
        } else if (name.equals("COMBINATION VEHICLE")) {
            QuestionAnswer = QuestionCombinated.question;
            QuestionAnswerChoices = QuestionCombinated.choices;
            QuestionAnswerCorrect = QuestionCombinated.correctAnswers;
        } else if (name.equals("GENERAL KNOWLEDGE UA")) {
            QuestionAnswer = QuestionGeneralUA.question;
            QuestionAnswerChoices = QuestionGeneralUA.choices;
            QuestionAnswerCorrect = QuestionGeneralUA.correctAnswers;
        } else if (name.equals("AIR BRAKES UA")) {
            QuestionAnswer = QuestionAirUA.question;
            QuestionAnswerChoices = QuestionAirUA.choices;
            QuestionAnswerCorrect = QuestionAirUA.correctAnswers;
        } else if (name.equals("COMBINATION VEHICLE UA")) {
            QuestionAnswer = QuestionCombinatedUA.question;
            QuestionAnswerChoices = QuestionCombinatedUA.choices;
            QuestionAnswerCorrect = QuestionCombinatedUA.correctAnswers;
        } else if (name.equals("GENERAL KNOWLEDGE ES")) {
            QuestionAnswer = QuestionGeneralES.question;
            QuestionAnswerChoices = QuestionGeneralES.choices;
            QuestionAnswerCorrect = QuestionGeneralES.correctAnswers;
        } else if (name.equals("AIR BRAKES ES")) {
            QuestionAnswer = QuestionAirES.question;
            QuestionAnswerChoices = QuestionAirES.choices;
            QuestionAnswerCorrect = QuestionAirES.correctAnswers;
        } else if (name.equals("COMBINATION VEHICLE ES")) {
            QuestionAnswer = QuestionCombinatedES.question;
            QuestionAnswerChoices = QuestionCombinatedES.choices;
            QuestionAnswerCorrect = QuestionCombinatedES.correctAnswers;
        } else if (name.equals("GENERAL KNOWLEDGE FR")) {
            QuestionAnswer = QuestionGeneralFR.question;
            QuestionAnswerChoices = QuestionGeneralFR.choices;
            QuestionAnswerCorrect = QuestionGeneralFR.correctAnswers;
        } else if (name.equals("AIR BRAKES FR")) {
            QuestionAnswer = QuestionAirFR.question;
            QuestionAnswerChoices = QuestionAirFR.choices;
            QuestionAnswerCorrect = QuestionAirFR.correctAnswers;
        } else if (name.equals("COMBINATION VEHICLE FR")) {
            QuestionAnswer = QuestionCombinatedFR.question;
            QuestionAnswerChoices = QuestionCombinatedFR.choices;
            QuestionAnswerCorrect = QuestionCombinatedFR.correctAnswers;
        }



        score = 0;

        // Считываем элементы массива по номеру

        //start = 60; end = 70;

        String[] QuestionAnswer2 = Arrays.copyOfRange(QuestionAnswer, start, end);
        QuestionAnswer = QuestionAnswer2;

        String[][] QuestionAnswerChoices2 = Arrays.copyOfRange(QuestionAnswerChoices, start, end);
        QuestionAnswerChoices = QuestionAnswerChoices2;
        // ---Считываем элементы массива по номеру

        totalQuestion = QuestionAnswer.length;
        currentQuestionIndex = 0;
        selectedAnswer = "";

        totalQuestionsTextView.setText(captionQuestion + (totalQuestion - 0));

        // Перемешиваем массивы ответов и вопросов
        Random random = new Random();

        // Применяем алгоритм Фишера-Йетса к обоим массивам
        for (int i = QuestionAnswer.length - 1; i > 0; i--) {
            // Генерируем случайный индекс из диапазона [0, i+1)
            int j = random.nextInt(i + 1);

            // Меняем местами элементы в массиве questions
            String tempQuestion = QuestionAnswer[i];
            QuestionAnswer[i] = QuestionAnswer[j];
            QuestionAnswer[j] = tempQuestion;

            // Меняем местами элементы в массиве answers
            String tempAnswer[] = QuestionAnswerChoices[i];
            QuestionAnswerChoices[i] = QuestionAnswerChoices[j];
            QuestionAnswerChoices[j] = tempAnswer;
        }

        // *Перемешиваем массивы ответов и вопросов


        all_RIght = findViewById(R.id.all_RIght);
        all_RIght.setText(CaptionResultStatus);


        loadNewQuestion();
    }

    private void loadBanner() {
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



    public void submit(View v) {
        if (selectedAnswer.contains("(TRUE)")) {
            score++;
        }
        currentQuestionIndex++;
        totalQuestionsTextView.setText(captionQuestionOnly + (currentQuestionIndex+1) + captionOf + totalQuestion);
        loadNewQuestion();

        ansA.setBackgroundColor(Color.TRANSPARENT);
        ansB.setBackgroundColor(Color.TRANSPARENT);
        ansC.setBackgroundColor(Color.TRANSPARENT);
        ansA.setTextColor(Color.WHITE);
        ansB.setTextColor(Color.WHITE);
        ansC.setTextColor(Color.WHITE);

        radiogroup.clearCheck();

        for (int i = 0; i < radiogroup.getChildCount(); i++) {
            ((RadioButton) radiogroup.getChildAt(i)).setEnabled(true);
        }
        submitBtn.setEnabled(false);




    }

    public void selectAnswer(View v) {
        Button selectedButton = (Button) v;
        int selectedAnswerInt = (selectedButton.getId());
        selectedAnswer = getResources().getResourceEntryName(selectedAnswerInt);
        int temp;

        //Log.d("",selectedAnswer);

        switch(selectedAnswer) {
            case "ans_A":
                temp = 0;
                break;
            case "ans_B":
                temp = 1;
                break;
            case "ans_C":
                temp = 2;
                break;
            default:
                temp = 3;
        }

        //Log.d("",String.valueOf(temp));

        if (QuestionAnswerChoices[currentQuestionIndex][temp].contains("(TRUE)")) {
            score++;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        sharedPreferences.edit().putString(keyName, "in_progress").apply();

        questionTextView.setText(QuestionAnswer[currentQuestionIndex]);
        String[] choices = QuestionAnswerChoices[currentQuestionIndex];

        selectedButton.setBackgroundColor(COLOR_RED);
        selectedButton.setTextColor(Color.BLACK);
        highlightCorrectAnswer();

        radiogroup.clearCheck();
        disableAnswerButtons();
        submitBtn.setEnabled(true);
    }

    private void highlightCorrectAnswer() {
        if (QuestionAnswerChoices[currentQuestionIndex][0].contains("(TRUE)")) {
            ansA.setBackgroundColor(COLOR_GREEN);
            ansA.setTextColor(Color.BLACK);
            all_RIght.setText(CaptionResultStatus + (score) + captionOf + (currentQuestionIndex+1));
        } else if (QuestionAnswerChoices[currentQuestionIndex][1].contains("(TRUE)")) {
            ansB.setBackgroundColor(COLOR_GREEN);
            ansB.setTextColor(Color.BLACK);
            all_RIght.setText(CaptionResultStatus + (score) + captionOf + (currentQuestionIndex+1));
        } else if (QuestionAnswerChoices[currentQuestionIndex][2].contains("(TRUE)")) {
            ansC.setBackgroundColor(COLOR_GREEN);
            ansC.setTextColor(Color.BLACK);
            all_RIght.setText(CaptionResultStatus + (score) + captionOf + (currentQuestionIndex+1));
        }
    }

    private void disableAnswerButtons() {
        for (int i = 0; i < radiogroup.getChildCount(); i++) {
            ((RadioButton) radiogroup.getChildAt(i)).setEnabled(false);
        }
    }

    private void loadNewQuestion() {
        if (currentQuestionIndex == totalQuestion) {
            totalQuestionsTextView.setText("");
            finishQuiz();
            return;
        }

        questionTextView.setText(QuestionAnswer[currentQuestionIndex]);
        String[] choices = QuestionAnswerChoices[currentQuestionIndex];
        ansA.setText(choices[0].replace("(TRUE)", ""));
        ansB.setText(choices[1].replace("(TRUE)", ""));
        ansC.setText(choices[2].replace("(TRUE)", ""));


    }

    private void finishQuiz() {
        String passStatus = "";

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Открываем редактор
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (score > totalQuestion * 0.60) {
            passStatus = captionStatusPass;
            editor.putString(keyName, "pass");
        } else {
            passStatus = captionStatusNotPass;
            editor.putString(keyName, "not pass");
        }

        editor.apply();

        new AlertDialog.Builder(this)
                .setTitle(passStatus)
                .setMessage(CaptionResultStatus + score + captionOf + totalQuestion)
                .setPositiveButton(captionRestart, (dialogInterface, i) -> restartQuiz())
                .setCancelable(false)
                .show();
    }

    private void restartQuiz() {
        score = 0;
        currentQuestionIndex = 0;
        loadNewQuestion();

        totalQuestionsTextView.setText(captionQuestion + totalQuestion);
    }

    // Кнопка возврата назад (закрывает тест и возвращает в открытое меню на том же месте)
    public void startMainActivity(View v) {
        finish();
    }

    private void showAdNoticeThenShowAd() {
        // If already premium, don't show (failsafe)
        if(GlobalData.isPremium) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(adThanks)
               .setMessage(adMessage)
               .setCancelable(false)
               .setPositiveButton(adButton, (dialog, which) -> {
                   showRewardedInterstitialAd();
               });
        
        // Remove Ads button removed as per request
        
        builder.show();
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

    @Override
    public void onPremiumStatusChanged(boolean isPremium) {
        if(isPremium) {
            // If user bought premium while in this activity (e.g. from the dialog)
            // Hide banners
            if (adContainerView != null) {
                adContainerView.setVisibility(View.GONE);
                adContainerView.removeAllViews();
            }
        }
    }
}
