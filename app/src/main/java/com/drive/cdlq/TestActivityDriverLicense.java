package com.drive.cdlq;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Random;

public class TestActivityDriverLicense extends AppCompatActivity {

    private static final int COLOR_GREEN = Color.GREEN;
    private static final int COLOR_RED = Color.RED;
    private static final int COLOR_WHITE = Color.WHITE;

    private AdView adView;
    // Test Ad Unit ID
     private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111";
    //private static final String AD_UNIT_ID = "ca-app-pub-7073626021965181/8578191229"; // с заблокированным полиси
    // The container for our ad unit.
    private FrameLayout adContainerView;

    TextView totalQuestionsTextView;
    TextView questionTextView, textView, total_right,all_RIght;
    Button ansA, ansB, ansC, ansD;
    Button submitBtn;

    RadioGroup radiogroup;

    private String[] QuestionAnswer;
    private String[][] QuestionAnswerChoices;
    private String[] QuestionAnswerCorrect;

    private int score;
    private int totalQuestion;
    private int currentQuestionIndex;
    private String selectedAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_driver_license);

        // Initialize our ad request.
        MobileAds.initialize(this, initializationStatus -> {});
        // Set your test devices. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // Uncomment the following line to get test ads(replace device id with your own device id)...
        // MobileAds.setRequestConfiguration(new RequestConfiguration.Builder().setTestDeviceIds(Collections.singletonList("102C2C747D96B6BBFC45B9E7D9238BE9")).build());
        adContainerView = findViewById(R.id.ad_view);
        adContainerView.post(this::loadBanner);

        // Получаем текст из предыдущего меню, он же параметр
        Bundle arguments = getIntent().getExtras();
        String name = arguments.get("typeOfVariant").toString();
        textView = findViewById(R.id.textHeader);
        textView.setText(name);
        
        
        totalQuestionsTextView = findViewById(R.id.total_right);
        questionTextView = findViewById(R.id.question);
        ansA = findViewById(R.id.ans_A);
        ansB = findViewById(R.id.ans_B);
        ansC = findViewById(R.id.ans_C);
        ansD = findViewById(R.id.ans_D);
        submitBtn = findViewById(R.id.submit_btn);

        radiogroup = findViewById(R.id.RadioGroup1);

        if (name.equals("Driver License RU")) {
            QuestionAnswer = QuestionDriver.question;
            QuestionAnswerChoices = QuestionDriver.choices;
            QuestionAnswerCorrect = QuestionDriver.correctAnswers;
        } else if (name.equals("Driver License UA")) {
            QuestionAnswer = QuestionDriverUA.question;
            QuestionAnswerChoices = QuestionDriverUA.choices;
            QuestionAnswerCorrect = QuestionDriverUA.correctAnswers;
        }




        score = 0;
        totalQuestion = QuestionAnswer.length;
        currentQuestionIndex = 0;
        selectedAnswer = "";

        totalQuestionsTextView.setText("Вопрос 1 из " + totalQuestion);

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
        all_RIght.setText("Результат: 0 из 0");


        loadNewQuestion();
    }

    private void loadBanner() {
        // Create an ad request.
        if (GlobalData.isPremium) {
            adContainerView.setVisibility(View.GONE);
            return;
        }
        adView = new AdView(this);
        adView.setAdUnitId(AD_UNIT_ID);
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
        loadNewQuestion();

        ansA.setBackgroundColor(Color.TRANSPARENT);
        ansB.setBackgroundColor(Color.TRANSPARENT);
        ansC.setBackgroundColor(Color.TRANSPARENT);
        ansD.setBackgroundColor(Color.TRANSPARENT);
        ansA.setTextColor(Color.WHITE);
        ansB.setTextColor(Color.WHITE);
        ansC.setTextColor(Color.WHITE);
        ansD.setTextColor(Color.WHITE);

        radiogroup.clearCheck();

        for (int i = 0; i < radiogroup.getChildCount(); i++) {
            ((RadioButton) radiogroup.getChildAt(i)).setEnabled(true);
        }
        submitBtn.setEnabled(false);

        totalQuestionsTextView.setText("Вопрос " + (currentQuestionIndex+1)+ " из " + totalQuestion);


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
            case "ans_D":
                temp = 3;
                break;
            default:
                temp = 4;
        }

        //Log.d("",String.valueOf(temp));

        if (QuestionAnswerChoices[currentQuestionIndex][temp].contains("(TRUE)")) {
            score++;
        }

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
            all_RIght.setText("Результат: " + (score) + " из " + (currentQuestionIndex+1));
        } else if (QuestionAnswerChoices[currentQuestionIndex][1].contains("(TRUE)")) {
            ansB.setBackgroundColor(COLOR_GREEN);
            ansB.setTextColor(Color.BLACK);
            all_RIght.setText("Результат: " + (score) + " из " + (currentQuestionIndex+1));
        } else if (QuestionAnswerChoices[currentQuestionIndex][2].contains("(TRUE)")) {
            ansC.setBackgroundColor(COLOR_GREEN);
            ansC.setTextColor(Color.BLACK);
            all_RIght.setText("Результат: " + (score) + " из " + (currentQuestionIndex+1));
        } else if (QuestionAnswerChoices[currentQuestionIndex][3].contains("(TRUE)")) {
            ansD.setBackgroundColor(COLOR_GREEN);
            ansD.setTextColor(Color.BLACK);
            all_RIght.setText("Результат: " + (score) + " из " + (currentQuestionIndex+1));
        }
    }

    private void disableAnswerButtons() {
        for (int i = 0; i < radiogroup.getChildCount(); i++) {
            ((RadioButton) radiogroup.getChildAt(i)).setEnabled(false);
        }
    }

    private void loadNewQuestion() {
        if (currentQuestionIndex == totalQuestion) {
            finishQuiz();
            return;
        }

        questionTextView.setText(QuestionAnswer[currentQuestionIndex]);
        String[] choices = QuestionAnswerChoices[currentQuestionIndex];
        ansA.setText(choices[0].replace("(TRUE)", ""));
        ansB.setText(choices[1].replace("(TRUE)", ""));
        ansC.setText(choices[2].replace("(TRUE)", ""));
        ansD.setText(choices[3].replace("(TRUE)", ""));


    }

    private void finishQuiz() {
        String passStatus = "";
        if (score > totalQuestion * 0.60) {
            passStatus = "Пройдено";
        } else {
            passStatus = "Провалено";
        }

        new AlertDialog.Builder(this)
                .setTitle(passStatus)
                .setMessage("Результат: " + score + " из " + totalQuestion)
                .setPositiveButton("Перезапустить тест", (dialogInterface, i) -> restartQuiz())
                .setCancelable(false)
                .show();
    }

    private void restartQuiz() {
        score = 0;
        currentQuestionIndex = 0;
        loadNewQuestion();

        totalQuestionsTextView.setText("Вопрос 1 из " + totalQuestion);
    }

    // Кнопка возврата назад
    public void startMainActivity(View v) {
        Intent newIntent = new Intent(this, MainActivity.class);
        startActivity(newIntent);
    }

}
