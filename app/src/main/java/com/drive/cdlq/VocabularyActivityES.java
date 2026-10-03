package com.drive.cdlq;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class VocabularyActivityES extends AppCompatActivity {

    // The ad view for our banner ad unit.
    private AdView adView;
    private FrameLayout adContainerView;

    private TextView questionTextView;
    private RadioGroup optionsRadioGroup;
    private Button nextQuizButton, test1Button, test2Button;

    private HashMap<String, String> wordPairs;
    private HashMap<String, String> wordPairsTest1;
    private HashMap<String, String> wordPairsTest2;

    private String currentWord;
    private String correctAnswer;

    private boolean answerChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);



        // Initialize our ad request.
        MobileAds.initialize(this, initializationStatus -> {});
        // Set your test devices. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // Uncomment the following line to get test ads(replace device id with your own device id)...
        // MobileAds.setRequestConfiguration(new RequestConfiguration.Builder().setTestDeviceIds(Collections.singletonList("102C2C747D96B6BBFC45B9E7D9238BE9")).build());
        adContainerView = findViewById(R.id.ad_view);
        adContainerView.post(this::loadBanner);

        questionTextView = findViewById(R.id.questionTextView);
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup);
        nextQuizButton = findViewById(R.id.nextQuizButtonQuiz);
        test1Button = findViewById(R.id.test1Button);
        test2Button = findViewById(R.id.test2Button);

        // Инициализация массивов
        wordPairs = new HashMap<>();
        wordPairsTest1 = new HashMap<>();
        wordPairsTest2 = new HashMap<>();

        // Заполнение массивов
        wordPairsTest1.put("Mounting trailer arm {Moun-teen tray-lehr ahrm}", "Soporte del brazo del remolque");
        wordPairsTest1.put("Bracket of the mirror {Bra-keht of the mee-rohr}", "Soporte del espejo");
        wordPairsTest1.put("Door Hingers {Dor Heen-gehrs}", "Bisagras de la puerta");
        wordPairsTest1.put("Weather seals {Weh-ther see-ls}", "Sellos contra la intemperie");
        wordPairsTest1.put("Steps to the cab {Steps tooh the cab}", "Escalones a la cabina");
        wordPairsTest1.put("Under Engine {Uhn-der en-gin}", "Debajo del motor");
        wordPairsTest1.put("Step to the catwalk {Step tooh the cat-walk}", "Escalón a la pasarela");
        wordPairsTest1.put("Cross members of the frame {Cross mem-behrs of the frame}", "Travesaños del chasis");
        wordPairsTest1.put("Drive Shaft {Drive Shaft}", "Eje de transmisión");
        wordPairsTest1.put("Frame {Frame}", "Chasis");


        wordPairsTest1.put("Back Light {Bak Layt}", "Luz trasera");
        wordPairsTest1.put("Spring mounted {Spring moun-tehd}", "Montado en resortes");
        wordPairsTest1.put("Spring leaves {Spring leevs}", "Hojas de resorte");
        wordPairsTest1.put("U-bolt {Yu-bohlt}", "Perno en forma de U");
        wordPairsTest1.put("Shock absorber {Shok ahb-sor-behr}", "Amortiguador");
        wordPairsTest1.put("Air Bag {Air Bag}", "Bolsa de aire");
        wordPairsTest1.put("Dual Wheels {Doo-ahl Weels}", "Ruedas dobles");
        wordPairsTest1.put("Sidewall of the tires {Said-wall of the tay-ers}", "Pared lateral de los neumáticos");
        wordPairsTest1.put("Budd spacing {Bood spay-sing}", "Espaciado de ruedas Budd");
        wordPairsTest1.put("Rim {Rim}", "Aro");
        wordPairsTest1.put("Lug Nuts {Luhg Nuh-ts}", "Tuercas de las ruedas");
        wordPairsTest1.put("Axle seal {Aks-el seel}", "Sello del eje");
        wordPairsTest1.put("Air valve {Air valv}", "Válvula de aire");
        wordPairsTest1.put("Bracket of the mud flap {Bra-keht of the mud flap}", "Soporte del guardabarros");
        wordPairsTest1.put("Mud Flap {Mud Flap}", "Guardabarros");
        wordPairsTest1.put("Rear light {Rear Light}", "Luz trasera");
        wordPairsTest1.put("License Plate {Lai-sens Pleit}", "Placa de matrícula");

        wordPairsTest1.put("Air Hoses and ABS line {Eyr Hoses ahnd ABS layn}", "Mangueras de aire y línea ABS");
        wordPairsTest1.put("Air brake chamber {Eyr breyk cheym-behr}", "Cámara de freno de aire");
        wordPairsTest1.put("Slack adjuster and push road {Slak ahd-jus-tehr ahnd poosh rohd}", "Ajustador de juego y varilla de empuje");
        wordPairsTest1.put("Brake lining {Breyk lay-ning}", "Forro de freno");
        wordPairsTest1.put("Brake Drum {Breyk Drum}", "Tambor de freno");
        wordPairsTest1.put("The muffler and pips {Theh muf-lehr ahnd peeps}", "Silenciador y tubos");
        wordPairsTest1.put("Clearance lights {Klee-rahns layts}", "Luces de gálibo");
        wordPairsTest1.put("Back door {Bak Dor}", "Puerta trasera");
        wordPairsTest1.put("Lock latch {Lok Latch}", "Cerradura de seguridad");
        wordPairsTest1.put("DOT reflective tape {DOT reh-flek-teev tape}", "Cinta reflectante DOT");
        wordPairsTest1.put("Headboard of the trailer {Hed-bohrd of the tray-lehr}", "Panel frontal del remolque");
        wordPairsTest1.put("Clearance light front and side {Klee-rahns layt front ahnd sahyd}", "Luz de gálibo frontal y lateral");
        wordPairsTest1.put("Sidewall of the trailer {Sahyd-wall of the tray-lehr}", "Pared lateral del remolque");
        wordPairsTest1.put("Side rail of the trailer {Sahyd rayl of the tray-lehr}", "Barra lateral del remolque");
        wordPairsTest1.put("Wood floor {Wood Floor}", "Piso de madera");
        wordPairsTest1.put("Landing gears {Lan-ding geers}", "Patines de aterrizaje");
        wordPairsTest1.put("Support of the landing gears {Soo-port of the lan-ding geers}", "Soporte de los patines");
        wordPairsTest1.put("Crack handle {Krahk han-dle}", "Manivela de ajuste");
        wordPairsTest1.put("Between MUD flap and landing gear {Be-tween Mud flap ahnd lan-ding geer}", "Entre el guardabarros y el patín de aterrizaje");
        wordPairsTest1.put("Mounting trailer arm {Moun-ting tray-lehr ahrm}", "Brazo de montaje del remolque");
        wordPairsTest1.put("Abs light {ABS Layt}", "Luz ABS");



        /////
        wordPairsTest2.put("Rear light {Rir Layt}", "Luz trasera");
        wordPairsTest2.put("License Plate {Lay-sens Playt}", "Placa de matrícula");
        wordPairsTest2.put("Brake light {Breyk Layt}", "Luz de freno");
        wordPairsTest2.put("Turn signal {Tern Sig-nal}", "Intermitente");
        wordPairsTest2.put("Clearance light {Klee-ah-rens Layt}", "Luz de gálibo");
        wordPairsTest2.put("Reflector {Reh-flek-tor}", "Reflector");
        wordPairsTest2.put("Running light {Ruh-ning Layt}", "Luz de circulación");
        wordPairsTest2.put("Tail light {Teyl Layt}", "Luz trasera");
        wordPairsTest2.put("Headlight {Hed-layt}", "Faro");
        wordPairsTest2.put("Fog light {Fohg Layt}", "Luz antiniebla");
        wordPairsTest2.put("Emergency light {Eh-mehr-gen-see Layt}", "Luz de emergencia");
        wordPairsTest2.put("Hazard lights {Haz-ard Layts}", "Luces de advertencia");
        wordPairsTest2.put("License Plate Light {Lay-sens Playt Layt}", "Luz de matrícula");
        wordPairsTest2.put("Bulb {Bahlb}", "Bombilla");
        wordPairsTest2.put("Mounting bracket {Moun-ting Brah-ket}", "Soporte de montaje");
        wordPairsTest2.put("Wiring {Wai-ring}", "Cableado");
        wordPairsTest2.put("Connector {Koh-nek-tor}", "Conector");


        wordPairsTest2.put("Voltage {Vohl-teyj}", "Voltaje");
        wordPairsTest2.put("Fuse {Fyooz}", "Fusible");
        wordPairsTest2.put("Circuit {Sir-kit}", "Circuito");
        wordPairsTest2.put("Relay {Reh-lei}", "Relé");
        wordPairsTest2.put("no missing lens {Noh mis-sing lenz}", "sin lentes faltantes");
        wordPairsTest2.put("amber color {Am-ber kol-or}", "color ámbar");
        wordPairsTest2.put("clean {Kleen}", "limpio");
        wordPairsTest2.put("properly working {Pro-per-lee wor-king}", "trabajando correctamente");
        wordPairsTest2.put("No cracks {Noh kraks}", "Sin grietas");
        wordPairsTest2.put("No damages {Noh deh-muh-jiz}", "Sin daños");
        wordPairsTest2.put("No chips {Noh chip-s}", "Sin astillas");
        wordPairsTest2.put("Not broken {Not broh-ken}", "No roto");
        wordPairsTest2.put("properly install with the rubber seal {Pro-per-lee in-stal uiz zhe rah-ber seel}", "instalado correctamente con el sello de goma");
        wordPairsTest2.put("No holes {Noh hohlz}", "Sin agujeros");
        wordPairsTest2.put("no missing rivets {Noh mis-sing riv-ets}", "sin remaches faltantes");
        wordPairsTest2.put("Amber color {Am-ber kol-or}", "Color ámbar");
        wordPairsTest2.put("clean and working properly {Kleen end wor-king pro-per-lee}", "limpio y funcionando correctamente");
        wordPairsTest2.put("visible and clean {Vih-zi-buhl end kleen}", "visible y limpio");
        wordPairsTest2.put("Straight {Streyt}", "Recto");
        wordPairsTest2.put("not missing rivets {Not mis-sing riv-ets}", "sin remaches faltantes");
        wordPairsTest2.put("not bent {Not bent}", "no doblado");
        wordPairsTest2.put("All the way to the end {All ze wei tu ze end}", "Hasta el final");
        wordPairsTest2.put("not shifted {Not shift-ed}", "no desplazado");

        wordPairsTest2.put("not crack {Noh krak}", "no agrietado");
        wordPairsTest2.put("not loose {Noh loose}", "no suelto");
        wordPairsTest2.put("No visible and illegal holes {Noh vi-zi-ble end i-li-gal holes}", "No hay agujeros visibles e ilegales");
        wordPairsTest2.put("Must be fully raised {Must bee foo-lee rays-d}", "Debe estar completamente levantado");
        wordPairsTest2.put("Is in the proper position {Iz in thuh prop-er poh-zish-un}", "Está en la posición correcta");
        wordPairsTest2.put("not missing {Noh miss-ing}", "no falta");
        wordPairsTest2.put("should be good another space {Shud bee good an-uh-thur spays}", "Debe haber suficiente espacio");
        wordPairsTest2.put("no abrasion {noh ah-bray-shun}", "sin abrasión");
        wordPairsTest2.put("no bulges {noh bool-jez}", "sin abultamientos");
        wordPairsTest2.put("no cuts {noh kut-s}", "sin cortes");
        wordPairsTest2.put("NO AIR LEAKS {noh air leeks}", "NO HAY FUGAS DE AIRE");
        wordPairsTest2.put("no cracks {noh krax}", "sin grietas");
        wordPairsTest2.put("not bent {not bent}", "no doblado");
        wordPairsTest2.put("not broken {not broh-ken}", "no roto");
        wordPairsTest2.put("NO AIR LEAKS {noh air leeks}", "NO HAY FUGAS DE AIRE");
        wordPairsTest2.put("Free play no more than 1 inch when release brake {Free play no more than 1 inch when re-lease brake}", "Juego libre no más de 1 pulgada al soltar el freno");
        wordPairsTest2.put("no missing clevis pin {noh miss-ing kleh-vis pin}", "sin pasador faltante");
        wordPairsTest2.put("and cotter pins {end koh-ter pinz}", "y pasadores");
        wordPairsTest2.put("no less than a quarter of an inch thickness {noh les than a kwar-ter of an inch thik-ness}", "no menos de un cuarto de pulgada de grosor");
        wordPairsTest2.put("No cracks {noh krax}", "Sin grietas");
        wordPairsTest2.put("no damage {noh deh-mih-j}", "sin daños");
        wordPairsTest2.put("not bent {not bent}", "no doblado");
        wordPairsTest2.put("not broken {not broh-ken}", "no roto");


        wordPairsTest2.put("no oil debris between brake lining and brake drums {noh oy-il deh-brees bee-tween brake ly-ning end brake drums}", "sin residuos de aceite entre las zapatas de freno y los tambores de freno");
        wordPairsTest2.put("No missing parts {Noh miss-ing parts}", "No hay piezas faltantes");
        wordPairsTest2.put("no twisted {noh twist-ed}", "no torcido");
        wordPairsTest2.put("Not bent {Not bent}", "No doblado");
        wordPairsTest2.put("No cracks {Noh krax}", "Sin grietas");
        wordPairsTest2.put("no missing glass {noh miss-ing glass}", "sin cristal faltante");
        wordPairsTest2.put("all mirrors should be clean {All mirrors should be kleen}", "Todos los espejos deben estar limpios");
        wordPairsTest2.put("not cracks {Not krax}", "sin grietas");
        wordPairsTest2.put("not bent {Not bent}", "no doblado");
        wordPairsTest2.put("not broken {Not broh-ken}", "no roto");
        wordPairsTest2.put("no cuts {noh kut-s}", "sin cortes");
        wordPairsTest2.put("not ripped {Not ript}", "no rasgado");
        wordPairsTest2.put("no grease {noh grees}", "sin grasa");
        wordPairsTest2.put("Should be clean {Shud bee kleen}", "Debe estar limpio");



        // Установка обработчиков кнопок
        test1Button.setOnClickListener(v -> loadTest(wordPairsTest1));
        test2Button.setOnClickListener(v -> loadTest(wordPairsTest2));

        nextQuizButton.setOnClickListener(v -> {
            nextQuizButton.setEnabled(false);
            loadNewQuestion();
        });

        // Загрузить тест 1 по умолчанию
        loadTest(wordPairsTest1);
    }

    private void loadTest(HashMap<String, String> selectedWordPairs) {
        wordPairs.clear();
        wordPairs.putAll(selectedWordPairs);
        loadNewQuestion();
    }

    private void loadNewQuestion() {
        optionsRadioGroup.removeAllViews();
        answerChecked = false;

        // Получить случайное слово
        List<String> keys = new ArrayList<>(wordPairs.keySet());
        Collections.shuffle(keys);

        currentWord = keys.get(0);
        correctAnswer = wordPairs.get(currentWord);

        questionTextView.setText(currentWord);

        // Генерировать варианты ответа
        List<String> options = new ArrayList<>(wordPairs.values());
        options.remove(correctAnswer);
        Collections.shuffle(options);
        options = options.subList(0, Math.min(3, options.size())); // Берём до 3 случайных вариантов
        options.add(correctAnswer);
        Collections.shuffle(options);

        // Создать радиокнопки
        for (String option : options) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            radioButton.setTextSize(16);
            radioButton.setPadding(16, 16, 16, 16);

            // Установить обработчик нажатия
            radioButton.setOnClickListener(v -> checkAnswer(radioButton));
            optionsRadioGroup.addView(radioButton);
        }
    }

    private void checkAnswer(RadioButton selectedRadioButton) {
        if (answerChecked) return; // Игнорировать повторное нажатие

        answerChecked = true;

        // Заблокировать все кнопки после выбора
        for (int i = 0; i < optionsRadioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) optionsRadioGroup.getChildAt(i);
            radioButton.setEnabled(false);

            // Подсветить правильный ответ
            if (radioButton.getText().toString().equals(correctAnswer)) {
                radioButton.setBackgroundColor(Color.GREEN);
                radioButton.setTextColor(Color.WHITE);
            }
        }

        // Подсветить выбранный вариант красным, если он неверный
        if (!selectedRadioButton.getText().toString().equals(correctAnswer)) {
            selectedRadioButton.setBackgroundColor(Color.RED);
            selectedRadioButton.setTextColor(Color.WHITE);
        } else {

        }

        // Показать кнопку "Следующий квиз"
        nextQuizButton.setEnabled(true);
    }

    private void loadBanner() {
        // Create an ad request.
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
}
