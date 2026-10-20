package com.drive.cdlq;

import android.content.SharedPreferences;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.OnUserEarnedRewardListener;


public class VocabularyActivity extends AppCompatActivity {

    // The ad view for our banner ad unit.
    private AdView adView;
    private FrameLayout adContainerView;

    private TextView questionTextView;
    private RadioGroup optionsRadioGroup;
    private Button nextQuizButton, test1Button, test2Button;

    private HashMap<String, String> wordPairs;
    private HashMap<String, String> wordPairsTest1;
    private HashMap<String, String> wordPairsTest2;
    private HashMap<String, String> wordPairsTest3;
    private HashMap<String, String> wordPairsTest4;
    private HashMap<String, String> wordPairsTest5;
    private HashMap<String, String> wordPairsTest6;

    private String currentWord;
    private String correctAnswer;

    private boolean answerChecked = false;

    private RewardedInterstitialAd rewardedInterstitialAd;
    private boolean hasAdBeenShown = false;

    private String adThanks;
    private String  adMessage;
    private String  adButton;


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

        int launchCount = LaunchCounter.increaseLaunchCount(this);

        if (launchCount == 1 || launchCount % 5 == 0) {
            loadRewardedInterstitialAd();
        }

        questionTextView = findViewById(R.id.questionTextView);
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup);
        nextQuizButton = findViewById(R.id.nextQuizButtonQuiz);
        test1Button = findViewById(R.id.test1Button);
        test2Button = findViewById(R.id.test2Button);

        // Инициализация массивов
        wordPairs = new HashMap<>();
        wordPairsTest1 = new HashMap<>();
        wordPairsTest2 = new HashMap<>();
        wordPairsTest3 = new HashMap<>();
        wordPairsTest4 = new HashMap<>();
        wordPairsTest5 = new HashMap<>();
        wordPairsTest6 = new HashMap<>();

        // Заполнение массивов
        wordPairsTest1.put("Mounting trailer arm {Маунтинг трейлер арм}", "Кронштейн прицепного рычага");
        wordPairsTest1.put("Bracket of the mirror {Брэкэт оф зе миррор}", "Кронштейн зеркала");
        wordPairsTest1.put("Door Hingers {Дор Хиндэрс}", "Петли двери");
        wordPairsTest1.put("Weather seals {Вэзэр силс}", "Уплотнители");
        wordPairsTest1.put("Steps to the cab {Стэпс ту зе кэб}", "Ступеньки к кабине");
        wordPairsTest1.put("Under Engine {Андер энджин}", "Под двигателем");
        wordPairsTest1.put("Step to the catwalk {Стэп ту зе кэтвок}", "Ступенька к площадке");
        wordPairsTest1.put("Cross members of the frame {Кросс мембэрс оф зе фрэйм}", "Поперечные балки рамы");
        wordPairsTest1.put("Drive Shaft {Драйв Шафт}", "Карданный вал");
        wordPairsTest1.put("Frame {Фрэйм}", "Рама");
        wordPairsTest1.put("Back Light {Бэк Лайт}", "Задний свет");
        wordPairsTest1.put("Spring mounted {Спринг маунтид}", "Установлено на пружинах");
        wordPairsTest1.put("Spring leaves {Спринг лифз}", "Листовые рессоры");
        wordPairsTest1.put("U-bolt {Ю-болт}", "U-образный болт");
        wordPairsTest1.put("Shock absorber {Шок абзорбер}", "Амортизатор");
        wordPairsTest1.put("Air Bag {Эйр Бэг}", "Воздушная подушка");
        wordPairsTest1.put("Dual Wheels {Дуэл Уилс}", "Сдвоенные колеса");
        wordPairsTest1.put("Sidewall of the tires {Сайдвол оф зе тайерс}", "Боковина шин");
        wordPairsTest1.put("Budd spacing {Буд спэйсинг}", "Расстояние между колесами Budd");
        wordPairsTest1.put("Rim {Рим}", "Обод");
        wordPairsTest1.put("Lug Nuts {Лаг Нэтс}", "Гайки колесных болтов");
        wordPairsTest1.put("Axle seal {Эксл сиил}", "Сальник оси");
        wordPairsTest1.put("Air valve {Эйр вэлв}", "Воздушный клапан");
        wordPairsTest1.put("Bracket of the mud flap {Брэкэт оф зе мад флап}", "Кронштейн брызговика");
        wordPairsTest1.put("Mud Flap {Мад Флап}", "Брызговик");
        wordPairsTest1.put("Rear light {Рир Лайт}", "Задний свет");
        wordPairsTest1.put("License Plate {Лайсенс Плейт}", "Номерной знак");
        wordPairsTest1.put("Air Hoses and ABS line {Эйр Хоузез энд Эй Би Эс Лайн}", "Воздушные шланги и линия ABS");
        wordPairsTest1.put("Air brake chamber {Эйр брэйк чэмбер}", "Камера воздушного тормоза");
        wordPairsTest1.put("Slack adjuster and push road {Слэк аджастэр энд пэш роуд}", "Регулировочный рычаг и толкатель");
        wordPairsTest1.put("Brake lining {Брэйк лайнинг}", "Тормозная накладка");
        wordPairsTest1.put("Brake Drum {Брэйк Драм}", "Тормозной барабан");
        wordPairsTest1.put("The muffler and pips {Зе маффлер энд пипс}", "Глушитель и трубы");
        wordPairsTest1.put("Clearance lights {Клиэрэнс лайц}", "Габаритные огни");
        wordPairsTest1.put("Back door {Бэк дор}", "Задняя дверь");
        wordPairsTest1.put("Lock latch {Лок лэтч}", "Замок-защёлка");
        wordPairsTest1.put("DOT reflective tape {ДОТ рефлектив тейп}", "Светоотражающая лента DOT");
        wordPairsTest1.put("Headboard of the trailer {Хэдборд оф зе трейлер}", "Передняя стенка прицепа");
        wordPairsTest1.put("Clearance light front and side {Клиэрэнс лайт фронт энд сайд}", "Передние и боковые габаритные огни");
        wordPairsTest1.put("Sidewall of the trailer {Сайдвол оф зе трейлер}", "Боковина прицепа");
        wordPairsTest1.put("Side rail of the trailer {Сайд рэйл оф зе трейлер}", "Боковая рейка прицепа");
        wordPairsTest1.put("Wood floor {Вуд флор}", "Деревянный пол");
        wordPairsTest1.put("Landing gears {Лэндинг гирс}", "Опорные стойки");
        wordPairsTest1.put("Support of the landing gears {Саппорт оф зе лэндинг гирс}", "Поддержка опорных стоек");
        wordPairsTest1.put("Crack handle {Крак хэндл}", "Ручка для опускания/поднятия");
        wordPairsTest1.put("Between MUD flap and landing gear {Битвин МАД флап энд лэндинг гир}", "Между брызговиком и опорной стойкой");
        wordPairsTest1.put("Mounting trailer arm {Маунтинг трейлер арм}", "Кронштейн прицепного рычага");
        wordPairsTest1.put("Abs light {Эй Би Эс лайт}", "Свет ABS");

        /////
        wordPairsTest2.put("Rear light {Рир Лайт}", "Задний свет");
        wordPairsTest2.put("License Plate {Лайсенс Плейт}", "Номерной знак");
        wordPairsTest2.put("Brake light {Брейк Лайт}", "Стоп-сигнал");
        wordPairsTest2.put("Turn signal {Тёрн Сигнл}", "Поворотник");
        wordPairsTest2.put("Clearance light {Клиарэнс Лайт}", "Габаритный огонь");
        wordPairsTest2.put("Reflector {Рефлектор}", "Отражатель");
        wordPairsTest2.put("Running light {Раннинг Лайт}", "Ходовой огонь");
        wordPairsTest2.put("Tail light {Тейл Лайт}", "Задний огонь");
        wordPairsTest2.put("Headlight {Хэдлайт}", "Фара");
        wordPairsTest2.put("Fog light {Фог Лайт}", "Противотуманный свет");
        wordPairsTest2.put("Emergency light {Эмердженси Лайт}", "Аварийный свет");
        wordPairsTest2.put("Hazard lights {Хэзард Лайтс}", "Аварийные огни");
        wordPairsTest2.put("License Plate Light {Лайсенс Плейт Лайт}", "Подсветка номерного знака");
        wordPairsTest2.put("Bulb {Балб}", "Лампа");
        wordPairsTest2.put("Mounting bracket {Маунтинг Брекет}", "Крепежный кронштейн");
        wordPairsTest2.put("Wiring {Уайринг}", "Проводка");
        wordPairsTest2.put("Connector {Коннектор}", "Соединитель");
        wordPairsTest2.put("Voltage {Вольтэдж}", "Напряжение");
        wordPairsTest2.put("Fuse {Фьюз}", "Предохранитель");
        wordPairsTest2.put("Circuit {Сёркит}", "Цепь");
        wordPairsTest2.put("Relay {Реле}", "Реле");
        wordPairsTest2.put("no missing lens {Ноу миссинг ленз}", "нет отсутствующих линз");
        wordPairsTest2.put("amber color {Амбер колор}", "янтарный цвет");
        wordPairsTest2.put("clean {Клин}", "чисто");
        wordPairsTest2.put("properly working {Проперли воркинг}", "работает правильно");
        wordPairsTest2.put("No cracks {Ноу кракс}", "Нет трещин");
        wordPairsTest2.put("No damages {Ноу дэмэджез}", "Нет повреждений");
        wordPairsTest2.put("No chips {Ноу чипс}", "Нет сколов");
        wordPairsTest2.put("Not broken {Нот броукен}", "Не сломано");
        wordPairsTest2.put("properly install with the rubber seal {Проперли инсталл уиз зе рабер сил}", "Правильно установлено с резиновым уплотнением");
        wordPairsTest2.put("No holes {Ноу холз}", "Нет дыр");
        wordPairsTest2.put("no missing rivets {Ноу миссинг ривэтс}", "нет отсутствующих заклепок");
        wordPairsTest2.put("Amber color {Амбер колор}", "Янтарный цвет");
        wordPairsTest2.put("clean and working properly {Клин энд вёркинг проперли}", "чисто и работает правильно");
        wordPairsTest2.put("visible and clean {Визибл энд клин}", "видимо и чисто");
        wordPairsTest2.put("Straight {Стрэйт}", "Прямой");
        wordPairsTest2.put("not missing rivets {Нот миссинг ривэтс}", "не отсутствуют заклепки");
        wordPairsTest2.put("not bent {Нот бэнт}", "не согнут");
        wordPairsTest2.put("All the way to the end {Олл зе вэй ту зе энд}", "До самого конца");
        wordPairsTest2.put("not shifted {Нот шифтед}", "не смещено");
        wordPairsTest2.put("not crack {Нот крак}", "не треснут");
        wordPairsTest2.put("not loose {Нот луз}", "не ослаблено");
        wordPairsTest2.put("No visible and illegal holes {Ноу визибл энд иллигал холз}", "Нет видимых и незаконных отверстий");
        wordPairsTest2.put("Must be fully raised {Маст би фулли рейзд}", "Должно быть полностью поднято");
        wordPairsTest2.put("Is in the proper position {Из ин зе пропер позишн}", "Находится в правильном положении");
        wordPairsTest2.put("not missing {Нот миссинг}", "не отсутствует");
        wordPairsTest2.put("should be good another space {Шуд би гуд анар зе спейс}", "Должно быть достаточно места");
        wordPairsTest2.put("no abrasion {но абрейжн}", "без абразии");
        wordPairsTest2.put("no bulges {но булджез}", "без выпуклостей");
        wordPairsTest2.put("no cuts {но катс}", "без разрывов");
        wordPairsTest2.put("NO AIR LEAKS {но эйр лийкс}", "НЕТ УТЕЧЕК ВОЗДУХА");
        wordPairsTest2.put("no cracks {но креакс}", "без трещин");
        wordPairsTest2.put("not bent {нот бент}", "не согнут");
        wordPairsTest2.put("not broken {нот броукен}", "не сломан");
        wordPairsTest2.put("NO AIR LEAKS {но эйр лийкс}", "НЕТ УТЕЧЕК ВОЗДУХА");
        wordPairsTest2.put("Free play no more than 1 inch when release brake {Фри плей но мор зен 1 инч уэн рилиз брейк}", "Свободный ход не более 1 дюйма при отпускании тормоза");
        wordPairsTest2.put("no missing clevis pin {но миссинг клевис пин}", "нет отсутствующих пальцев");
        wordPairsTest2.put("and cotter pins {энд коттер пинс}", "и стопорных штифтов");
        wordPairsTest2.put("no less than a quarter of an inch thickness {но лесс зен а куортер оф эн инч сикнесс}", "толщина не менее четверти дюйма");
        wordPairsTest2.put("No cracks {но креакс}", "Без трещин");
        wordPairsTest2.put("no damage {но дэмидж}", "без повреждений");
        wordPairsTest2.put("not bent {нот бент}", "не согнут");
        wordPairsTest2.put("not broken {нот броукен}", "не сломан");
        wordPairsTest2.put("no oil debris between brake lining and brake drums {но оил дебрис битвин брейк лайнинг энд брейк драамс}", "без масла и мусора между накладкой тормоза и тормозными барабанами");
        wordPairsTest2.put("No missing parts {Ноу миссинг парц}", "Нет отсутствующих частей");
        wordPairsTest2.put("no twisted {Ноу твистед}", "не перекручено");
        wordPairsTest2.put("Not bent {Нот бент}", "Не согнуто");
        wordPairsTest2.put("No cracks {Ноу кракс}", "Нет трещин");
        wordPairsTest2.put("no missing glass {Ноу миссинг глас}", "Нет отсутствующего стекла");
        wordPairsTest2.put("all mirrors should be clean {Олл миррорс шуд би клин}", "Все зеркала должны быть чистыми");
        wordPairsTest2.put("not cracks {Нот кракс}", "нет трещин");
        wordPairsTest2.put("not bent {Нот бент}", "не согнуто");
        wordPairsTest2.put("not broken {Нот броукен}", "не сломано");
        wordPairsTest2.put("no cuts {Ноу катс}", "нет порезов");
        wordPairsTest2.put("not ripped {Нот рипт}", "не порвано");
        wordPairsTest2.put("no grease {Ноу грис}", "нет смазки");
        wordPairsTest2.put("Should be clean {Шуд би клин}", "Должно быть чистым");


        wordPairsTest3.put("Mounting trailer arm {Moun-teen tray-lehr ahrm}", "Soporte del brazo del remolque");
        wordPairsTest3.put("Bracket of the mirror {Bra-keht of the mee-rohr}", "Soporte del espejo");
        wordPairsTest3.put("Door Hingers {Dor Heen-gehrs}", "Bisagras de la puerta");
        wordPairsTest3.put("Weather seals {Weh-ther see-ls}", "Sellos contra la intemperie");
        wordPairsTest3.put("Steps to the cab {Steps tooh the cab}", "Escalones a la cabina");
        wordPairsTest3.put("Under Engine {Uhn-der en-gin}", "Debajo del motor");
        wordPairsTest3.put("Step to the catwalk {Step tooh the cat-walk}", "Escalón a la pasarela");
        wordPairsTest3.put("Cross members of the frame {Cross mem-behrs of the frame}", "Travesaños del chasis");
        wordPairsTest3.put("Drive Shaft {Drive Shaft}", "Eje de transmisión");
        wordPairsTest3.put("Frame {Frame}", "Chasis");


        wordPairsTest3.put("Back Light {Bak Layt}", "Luz trasera");
        wordPairsTest3.put("Spring mounted {Spring moun-tehd}", "Montado en resortes");
        wordPairsTest3.put("Spring leaves {Spring leevs}", "Hojas de resorte");
        wordPairsTest3.put("U-bolt {Yu-bohlt}", "Perno en forma de U");
        wordPairsTest3.put("Shock absorber {Shok ahb-sor-behr}", "Amortiguador");
        wordPairsTest3.put("Air Bag {Air Bag}", "Bolsa de aire");
        wordPairsTest3.put("Dual Wheels {Doo-ahl Weels}", "Ruedas dobles");
        wordPairsTest3.put("Sidewall of the tires {Said-wall of the tay-ers}", "Pared lateral de los neumáticos");
        wordPairsTest3.put("Budd spacing {Bood spay-sing}", "Espaciado de ruedas Budd");
        wordPairsTest3.put("Rim {Rim}", "Aro");
        wordPairsTest3.put("Lug Nuts {Luhg Nuh-ts}", "Tuercas de las ruedas");
        wordPairsTest3.put("Axle seal {Aks-el seel}", "Sello del eje");
        wordPairsTest3.put("Air valve {Air valv}", "Válvula de aire");
        wordPairsTest3.put("Bracket of the mud flap {Bra-keht of the mud flap}", "Soporte del guardabarros");
        wordPairsTest3.put("Mud Flap {Mud Flap}", "Guardabarros");
        wordPairsTest3.put("Rear light {Rear Light}", "Luz trasera");
        wordPairsTest3.put("License Plate {Lai-sens Pleit}", "Placa de matrícula");

        wordPairsTest3.put("Air Hoses and ABS line {Eyr Hoses ahnd ABS layn}", "Mangueras de aire y línea ABS");
        wordPairsTest3.put("Air brake chamber {Eyr breyk cheym-behr}", "Cámara de freno de aire");
        wordPairsTest3.put("Slack adjuster and push road {Slak ahd-jus-tehr ahnd poosh rohd}", "Ajustador de juego y varilla de empuje");
        wordPairsTest3.put("Brake lining {Breyk lay-ning}", "Forro de freno");
        wordPairsTest3.put("Brake Drum {Breyk Drum}", "Tambor de freno");
        wordPairsTest3.put("The muffler and pips {Theh muf-lehr ahnd peeps}", "Silenciador y tubos");
        wordPairsTest3.put("Clearance lights {Klee-rahns layts}", "Luces de gálibo");
        wordPairsTest3.put("Back door {Bak Dor}", "Puerta trasera");
        wordPairsTest3.put("Lock latch {Lok Latch}", "Cerradura de seguridad");
        wordPairsTest3.put("DOT reflective tape {DOT reh-flek-teev tape}", "Cinta reflectante DOT");
        wordPairsTest3.put("Headboard of the trailer {Hed-bohrd of the tray-lehr}", "Panel frontal del remolque");
        wordPairsTest3.put("Clearance light front and side {Klee-rahns layt front ahnd sahyd}", "Luz de gálibo frontal y lateral");
        wordPairsTest3.put("Sidewall of the trailer {Sahyd-wall of the tray-lehr}", "Pared lateral del remolque");
        wordPairsTest3.put("Side rail of the trailer {Sahyd rayl of the tray-lehr}", "Barra lateral del remolque");
        wordPairsTest3.put("Wood floor {Wood Floor}", "Piso de madera");
        wordPairsTest3.put("Landing gears {Lan-ding geers}", "Patines de aterrizaje");
        wordPairsTest3.put("Support of the landing gears {Soo-port of the lan-ding geers}", "Soporte de los patines");
        wordPairsTest3.put("Crack handle {Krahk han-dle}", "Manivela de ajuste");
        wordPairsTest3.put("Between MUD flap and landing gear {Be-tween Mud flap ahnd lan-ding geer}", "Entre el guardabarros y el patín de aterrizaje");
        wordPairsTest3.put("Mounting trailer arm {Moun-ting tray-lehr ahrm}", "Brazo de montaje del remolque");
        wordPairsTest3.put("Abs light {ABS Layt}", "Luz ABS");


        /////
        wordPairsTest4.put("Rear light {Rir Layt}", "Luz trasera");
        wordPairsTest4.put("License Plate {Lay-sens Playt}", "Placa de matrícula");
        wordPairsTest4.put("Brake light {Breyk Layt}", "Luz de freno");
        wordPairsTest4.put("Turn signal {Tern Sig-nal}", "Intermitente");
        wordPairsTest4.put("Clearance light {Klee-ah-rens Layt}", "Luz de gálibo");
        wordPairsTest4.put("Reflector {Reh-flek-tor}", "Reflector");
        wordPairsTest4.put("Running light {Ruh-ning Layt}", "Luz de circulación");
        wordPairsTest4.put("Tail light {Teyl Layt}", "Luz trasera");
        wordPairsTest4.put("Headlight {Hed-layt}", "Faro");
        wordPairsTest4.put("Fog light {Fohg Layt}", "Luz antiniebla");
        wordPairsTest4.put("Emergency light {Eh-mehr-gen-see Layt}", "Luz de emergencia");
        wordPairsTest4.put("Hazard lights {Haz-ard Layts}", "Luces de advertencia");
        wordPairsTest4.put("License Plate Light {Lay-sens Playt Layt}", "Luz de matrícula");
        wordPairsTest4.put("Bulb {Bahlb}", "Bombilla");
        wordPairsTest4.put("Mounting bracket {Moun-ting Brah-ket}", "Soporte de montaje");
        wordPairsTest4.put("Wiring {Wai-ring}", "Cableado");
        wordPairsTest4.put("Connector {Koh-nek-tor}", "Conector");


        wordPairsTest4.put("Voltage {Vohl-teyj}", "Voltaje");
        wordPairsTest4.put("Fuse {Fyooz}", "Fusible");
        wordPairsTest4.put("Circuit {Sir-kit}", "Circuito");
        wordPairsTest4.put("Relay {Reh-lei}", "Relé");
        wordPairsTest4.put("no missing lens {Noh mis-sing lenz}", "sin lentes faltantes");
        wordPairsTest4.put("amber color {Am-ber kol-or}", "color ámbar");
        wordPairsTest4.put("clean {Kleen}", "limpio");
        wordPairsTest4.put("properly working {Pro-per-lee wor-king}", "trabajando correctamente");
        wordPairsTest4.put("No cracks {Noh kraks}", "Sin grietas");
        wordPairsTest4.put("No damages {Noh deh-muh-jiz}", "Sin daños");
        wordPairsTest4.put("No chips {Noh chip-s}", "Sin astillas");
        wordPairsTest4.put("Not broken {Not broh-ken}", "No roto");
        wordPairsTest4.put("properly install with the rubber seal {Pro-per-lee in-stal uiz zhe rah-ber seel}", "instalado correctamente con el sello de goma");
        wordPairsTest4.put("No holes {Noh hohlz}", "Sin agujeros");
        wordPairsTest4.put("no missing rivets {Noh mis-sing riv-ets}", "sin remaches faltantes");
        wordPairsTest4.put("Amber color {Am-ber kol-or}", "Color ámbar");
        wordPairsTest4.put("clean and working properly {Kleen end wor-king pro-per-lee}", "limpio y funcionando correctamente");
        wordPairsTest4.put("visible and clean {Vih-zi-buhl end kleen}", "visible y limpio");
        wordPairsTest4.put("Straight {Streyt}", "Recto");
        wordPairsTest4.put("not missing rivets {Not mis-sing riv-ets}", "sin remaches faltantes");
        wordPairsTest4.put("not bent {Not bent}", "no doblado");
        wordPairsTest4.put("All the way to the end {All ze wei tu ze end}", "Hasta el final");
        wordPairsTest4.put("not shifted {Not shift-ed}", "no desplazado");

        wordPairsTest4.put("not crack {Noh krak}", "no agrietado");
        wordPairsTest4.put("not loose {Noh loose}", "no suelto");
        wordPairsTest4.put("No visible and illegal holes {Noh vi-zi-ble end i-li-gal holes}", "No hay agujeros visibles e ilegales");
        wordPairsTest4.put("Must be fully raised {Must bee foo-lee rays-d}", "Debe estar completamente levantado");
        wordPairsTest4.put("Is in the proper position {Iz in thuh prop-er poh-zish-un}", "Está en la posición correcta");
        wordPairsTest4.put("not missing {Noh miss-ing}", "no falta");
        wordPairsTest4.put("should be good another space {Shud bee good an-uh-thur spays}", "Debe haber suficiente espacio");
        wordPairsTest4.put("no abrasion {noh ah-bray-shun}", "sin abrasión");
        wordPairsTest4.put("no bulges {noh bool-jez}", "sin abultamientos");
        wordPairsTest4.put("no cuts {noh kut-s}", "sin cortes");
        wordPairsTest4.put("NO AIR LEAKS {noh air leeks}", "NO HAY FUGAS DE AIRE");
        wordPairsTest4.put("no cracks {noh krax}", "sin grietas");
        wordPairsTest4.put("not bent {not bent}", "no doblado");
        wordPairsTest4.put("not broken {not broh-ken}", "no roto");
        wordPairsTest4.put("NO AIR LEAKS {noh air leeks}", "NO HAY FUGAS DE AIRE");
        wordPairsTest4.put("Free play no more than 1 inch when release brake {Free play no more than 1 inch when re-lease brake}", "Juego libre no más de 1 pulgada al soltar el freno");
        wordPairsTest4.put("no missing clevis pin {noh miss-ing kleh-vis pin}", "sin pasador faltante");
        wordPairsTest4.put("and cotter pins {end koh-ter pinz}", "y pasadores");
        wordPairsTest4.put("no less than a quarter of an inch thickness {noh les than a kwar-ter of an inch thik-ness}", "no menos de un cuarto de pulgada de grosor");
        wordPairsTest4.put("No cracks {noh krax}", "Sin grietas");
        wordPairsTest4.put("no damage {noh deh-mih-j}", "sin daños");
        wordPairsTest4.put("not bent {not bent}", "no doblado");
        wordPairsTest4.put("not broken {not broh-ken}", "no roto");


        wordPairsTest4.put("no oil debris between brake lining and brake drums {noh oy-il deh-brees bee-tween brake ly-ning end brake drums}", "sin residuos de aceite entre las zapatas de freno y los tambores de freno");
        wordPairsTest4.put("No missing parts {Noh miss-ing parts}", "No hay piezas faltantes");
        wordPairsTest4.put("no twisted {noh twist-ed}", "no torcido");
        wordPairsTest4.put("Not bent {Not bent}", "No doblado");
        wordPairsTest4.put("No cracks {Noh krax}", "Sin grietas");
        wordPairsTest4.put("no missing glass {noh miss-ing glass}", "sin cristal faltante");
        wordPairsTest4.put("all mirrors should be clean {All mirrors should be kleen}", "Todos los espejos deben estar limpios");
        wordPairsTest4.put("not cracks {Not krax}", "sin grietas");
        wordPairsTest4.put("not bent {Not bent}", "no doblado");
        wordPairsTest4.put("not broken {Not broh-ken}", "no roto");
        wordPairsTest4.put("no cuts {noh kut-s}", "sin cortes");
        wordPairsTest4.put("not ripped {Not ript}", "no rasgado");
        wordPairsTest4.put("no grease {noh grees}", "sin grasa");
        wordPairsTest4.put("Should be clean {Shud bee kleen}", "Debe estar limpio");

        // Французский словарь (основные понятия)
        wordPairsTest5.put("Mounting trailer arm {Moun-ting tray-lehr ahrm}", "Bras de montage de la remorque");
        wordPairsTest5.put("Bracket of the mirror {Bra-keht of the mee-rohr}", "Support du rétroviseur");
        wordPairsTest5.put("Door Hingers {Dor Heen-gehrs}", "Charnières de porte");
        wordPairsTest5.put("Weather seals {Weh-ther see-ls}", "Joints d'étanchéité");
        wordPairsTest5.put("Steps to the cab {Steps tooh the cab}", "Marches d'accès à la cabine");
        wordPairsTest5.put("Under Engine {Uhn-der en-gin}", "Sous le moteur");
        wordPairsTest5.put("Step to the catwalk {Step tooh the cat-walk}", "Marche d'accès à la passerelle");
        wordPairsTest5.put("Cross members of the frame {Cross mem-behrs of the frame}", "Traverses du châssis");
        wordPairsTest5.put("Drive Shaft {Drive Shaft}", "Arbre de transmission");
        wordPairsTest5.put("Frame {Frame}", "Châssis");
        wordPairsTest5.put("Back Light {Bak Layt}", "Feu arrière");
        wordPairsTest5.put("Spring mounted {Spring moun-tehd}", "Supports de ressort");
        wordPairsTest5.put("Spring leaves {Spring leevs}", "Lames de ressort");
        wordPairsTest5.put("U-bolt {Yu-bohlt}", "Boulon en U (étrier)");
        wordPairsTest5.put("Shock absorber {Shok ahb-sor-behr}", "Amortisseur");
        wordPairsTest5.put("Air Bag {Air Bag}", "Coussin d'air (suspension pneumatique)");
        wordPairsTest5.put("Dual Wheels {Doo-ahl Weels}", "Roues jumelées");
        wordPairsTest5.put("Sidewall of the tires {Said-wall of the tay-ers}", "Flanc des pneus");
        wordPairsTest5.put("Budd spacing {Bood spay-sing}", "Espacement entre les roues jumelées");
        wordPairsTest5.put("Rim {Rim}", "Jante");
        wordPairsTest5.put("Lug Nuts {Luhg Nuh-ts}", "Écrous de roue");
        wordPairsTest5.put("Axle seal {Aks-el seel}", "Joint d'essieu");
        wordPairsTest5.put("Air valve {Air valv}", "Valve d'air");
        wordPairsTest5.put("Bracket of the mud flap {Bra-keht of the mud flap}", "Support de bavette");
        wordPairsTest5.put("Mud Flap {Mud Flap}", "Bavette garde-boue");
        wordPairsTest5.put("Rear light {Rear Light}", "Feu arrière");
        wordPairsTest5.put("License Plate {Lai-sens Pleit}", "Plaque d'immatriculation");
        wordPairsTest5.put("Air Hoses and ABS line {Eyr Hoses ahnd ABS layn}", "Flexibles d'air et ligne ABS");
        wordPairsTest5.put("Air brake chamber {Eyr breyk cheym-behr}", "Chambre de frein à air");
        wordPairsTest5.put("Slack adjuster and push road {Slak ahd-jus-tehr ahnd poosh rohd}", "Levier de réglage et tige de poussée");
        wordPairsTest5.put("Brake lining {Breyk lay-ning}", "Garniture de frein");
        wordPairsTest5.put("Brake Drum {Breyk Drum}", "Tambour de frein");
        wordPairsTest5.put("The muffler and pips {Theh muf-lehr ahnd peeps}", "Silencieux et tuyaux d'échappement");
        wordPairsTest5.put("Clearance lights {Klee-rahns layts}", "Feux de gabarit");
        wordPairsTest5.put("Back door {Bak Dor}", "Porte arrière");
        wordPairsTest5.put("Lock latch {Lok Latch}", "Loquet de verrouillage");
        wordPairsTest5.put("DOT reflective tape {DOT reh-flek-teev tape}", "Bande réfléchissante DOT");
        wordPairsTest5.put("Headboard of the trailer {Hed-bohrd of the tray-lehr}", "Paroi avant de la remorque");
        wordPairsTest5.put("Clearance light front and side {Klee-rahns layt front ahnd sahyd}", "Feux de gabarit avant et latéraux");
        wordPairsTest5.put("Sidewall of the trailer {Sahyd-wall of the tray-lehr}", "Paroi latérale de la remorque");
        wordPairsTest5.put("Side rail of the trailer {Sahyd rayl of the tray-lehr}", "Rail latéral de la remorque");
        wordPairsTest5.put("Wood floor {Wood Floor}", "Plancher en bois");
        wordPairsTest5.put("Landing gears {Lan-ding geers}", "Béquilles de remorque");
        wordPairsTest5.put("Support of the landing gears {Soo-port of the lan-ding geers}", "Support des béquilles");
        wordPairsTest5.put("Crack handle {Krahk han-dle}", "Manivelle des béquilles");
        wordPairsTest5.put("Between MUD flap and landing gear {Be-tween Mud flap ahnd lan-ding geer}", "Entre la bavette et la béquille");
        wordPairsTest5.put("Abs light {ABS Layt}", "Voyant ABS");

        // Французский словарь (термины и глаголы)
        wordPairsTest6.put("Brake light {Breyk Layt}", "Feu stop");
        wordPairsTest6.put("Turn signal {Tern Sig-nal}", "Clignotant");
        wordPairsTest6.put("Clearance light {Klee-ah-rens Layt}", "Feu de gabarit");
        wordPairsTest6.put("Reflector {Reh-flek-tor}", "Réflecteur");
        wordPairsTest6.put("Running light {Ruh-ning Layt}", "Feu de position");
        wordPairsTest6.put("Headlight {Hed-layt}", "Phare");
        wordPairsTest6.put("Fog light {Fohg Layt}", "Feu antibrouillard");
        wordPairsTest6.put("Emergency light {Eh-mehr-gen-see Layt}", "Feu d'urgence");
        wordPairsTest6.put("Hazard lights {Haz-ard Layts}", "Feux de détresse");
        wordPairsTest6.put("Bulb {Bahlb}", "Ampoule");
        wordPairsTest6.put("Mounting bracket {Moun-ting Brah-ket}", "Support de fixation");
        wordPairsTest6.put("Wiring {Wai-ring}", "Câblage");
        wordPairsTest6.put("Connector {Koh-nek-tor}", "Connecteur");
        wordPairsTest6.put("Voltage {Vohl-teyj}", "Tension");
        wordPairsTest6.put("Fuse {Fyooz}", "Fusible");
        wordPairsTest6.put("no missing lens {Noh mis-sing lenz}", "aucune lentille manquante");
        wordPairsTest6.put("amber color {Am-ber kol-or}", "couleur ambre");
        wordPairsTest6.put("clean and working properly {Kleen end wor-king pro-per-lee}", "propre et fonctionne correctement");
        wordPairsTest6.put("No cracks {Noh kraks}", "Pas de fissures");
        wordPairsTest6.put("No damages {Noh deh-muh-jiz}", "Pas de dommages");
        wordPairsTest6.put("No chips {Noh chip-s}", "Pas d'éclats");
        wordPairsTest6.put("Not broken {Not broh-ken}", "Non cassé");
        wordPairsTest6.put("properly install with the rubber seal {Pro-per-lee in-stal uiz zhe rah-ber seel}", "correctement installé avec le joint en caoutchouc");
        wordPairsTest6.put("No holes {Noh hohlz}", "Pas de trous");
        wordPairsTest6.put("no missing rivets {Noh mis-sing riv-ets}", "aucun rivet manquant");
        wordPairsTest6.put("not bent {Not bent}", "non plié");
        wordPairsTest6.put("not shifted {Not shift-ed}", "non déplacé");
        wordPairsTest6.put("not loose {Noh loose}", "non desserré");
        wordPairsTest6.put("Must be fully raised {Must bee foo-lee rays-d}", "Doit être complètement relevé");
        wordPairsTest6.put("no abrasion {noh ah-bray-shun}", "sans abrasion");
        wordPairsTest6.put("no bulges {noh bool-jez}", "sans bosses ni hernies");
        wordPairsTest6.put("no cuts {noh kut-s}", "sans coupures");
        wordPairsTest6.put("NO AIR LEAKS {noh air leeks}", "AUCUNE FUITE D'AIR");
        wordPairsTest6.put("Free play no more than 1 inch when release brake {Free play no more than 1 inch when re-lease brake}", "Jeu libre d'au plus 1 pouce lorsque le frein est relâché");
        wordPairsTest6.put("no missing clevis pin {noh miss-ing kleh-vis pin}", "aucun axe de chape manquant");
        wordPairsTest6.put("and cotter pins {end koh-ter pinz}", "et goupilles fendues");
        wordPairsTest6.put("no less than a quarter of an inch thickness {noh les than a kwar-ter of an inch thik-ness}", "épaisseur d'au moins un quart de pouce");
        wordPairsTest6.put("no oil debris between brake lining and brake drums {noh oy-il deh-brees bee-tween brake ly-ning end brake drums}", "pas d'huile ni de débris entre la garniture et le tambour de frein");
        wordPairsTest6.put("No missing parts {Noh miss-ing parts}", "Aucune pièce manquante");
        wordPairsTest6.put("all mirrors should be clean {All mirrors should be kleen}", "tous les rétroviseurs doivent être propres");

        // Получаем SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        String appLanguage;

        // Проверяем, существует ли переменная для языка
        appLanguage = sharedPreferences.getString("appLanguage", null); // Получаем сохранённый язык

        // Установка обработчиков кнопок
        test1Button.setOnClickListener(v -> loadTest(wordPairsTest1));
        test2Button.setOnClickListener(v -> loadTest(wordPairsTest2));

        if ("es".equals(appLanguage)) {
            // Установка обработчиков кнопок
            test1Button.setOnClickListener(v -> loadTest(wordPairsTest3));
            test2Button.setOnClickListener(v -> loadTest(wordPairsTest4));
            test1Button.setText("Conceptos principales");
            test2Button.setText("Términos y verbos");
            nextQuizButton.setText("Siguiente prueba");
            adThanks = "Desbloquear parte";
            adMessage = "Mira un anuncio para desbloquear la siguiente parte";
            adButton = "Continuar";
        } else if ("fr".equals(appLanguage)) {
            test1Button.setOnClickListener(v -> loadTest(wordPairsTest5));
            test2Button.setOnClickListener(v -> loadTest(wordPairsTest6));
            test1Button.setText("Concepts principaux");
            test2Button.setText("Termes et verbes");
            nextQuizButton.setText("Quiz suivant");
            adThanks = "Débloquer la partie";
            adMessage = "Regardez une publicité pour débloquer la partie suivante";
            adButton = "Continuer";
        } else if ("ru".equals(appLanguage)) {
            // Установка обработчиков кнопок
            test1Button.setOnClickListener(v -> loadTest(wordPairsTest1));
            test2Button.setOnClickListener(v -> loadTest(wordPairsTest2));
            test1Button.setText("Основные понятия");
            test2Button.setText("Термины и глаголы");
            nextQuizButton.setText("Следующий квиз");
            adThanks = "Разблокировать часть";
            adMessage = "Посмотрите рекламу, чтобы разблокировать следующую часть";
            adButton = "Продолжить";
        } else {
            // English / Default
            test1Button.setOnClickListener(v -> loadTest(wordPairsTest1));
            test2Button.setOnClickListener(v -> loadTest(wordPairsTest2));
            test1Button.setText("Basic concepts");
            test2Button.setText("Terms and verbs");
            nextQuizButton.setText("Next quiz");
            adThanks = "Unlock Part";
            adMessage = "Watch an ad to unlock the next part";
            adButton = "Continue";
        }







        nextQuizButton.setOnClickListener(v -> {
            nextQuizButton.setEnabled(false);
            loadNewQuestion();
        });

        // Загрузить тест 1 по умолчанию
        if ("es".equals(appLanguage)) {
            loadTest(wordPairsTest3);
        } else if ("fr".equals(appLanguage)) {
            loadTest(wordPairsTest5);
        } else {
            loadTest(wordPairsTest1);
        }

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
