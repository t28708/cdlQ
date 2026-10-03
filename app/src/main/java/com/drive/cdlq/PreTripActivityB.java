package com.drive.cdlq;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class PreTripActivityB extends AppCompatActivity {

    // The ad view for our banner ad unit.
    private AdView adView;
    // Test Ad Unit ID
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741";
    // private static final String AD_UNIT_ID = "ca-app-pub-7073626021965181/8578191229"; // с заблокированным полиси
    // ca-app-pub-7071445382430696/3095427778
    // The container for our ad unit.
    private FrameLayout adContainerView;

    TextView textView;

    private PopupWindow currentPopupWindow;  // Окно под кнопкой
    private PopupWindow bottomPopupWindow;   // Окно снизу
    private TextView popupTextView;
    private View currentAnchorView; // Добавлено для отслеживания текущей точки

    private ImageView carImageView;
    private ImageView[] dotImages;
    private int[] imageResIds = {
            R.drawable.cascadia,
            R.drawable.pre_trip_a_left,
            R.drawable.hood,
            R.drawable.hood,
            R.drawable.pre_trip_a_steering_system,
            R.drawable.pre_trip_a_suspension_system,
            R.drawable.pre_trip_a_air_brakes,
            R.drawable.pre_trip_a_wheels
    };  // Примеры ресурсов изображений
    private int currentImageIndex = 0;

    // Массивы координат для точек на разных изображениях в процентном соотношении
    private float[][] dotCoordinatesPercent = {
            // Координаты для первой картинки
            {0.38f, 0.2f, 0.4f, 0.33f,  0.38f, 0.6f, 0.30f, 0.61f, 0.24f, 0.89f},
            // Координаты для второй картинки
            {0.66f, 0.55f, 0.73f, 0.90f, 0.78f, 0.61f, 0.25f, 0.24f, 110.85f, 110.9f},
            // Координаты для третьей картинки
            {0.2f, 0.6f, 0.25f, 0.85f, 0.9f, 0.4f, 110.85f, 110.7f, 110.85f, 110.7f},
            // Координаты для 4 картинки
            {0.28f, 0.37f, 0.27f, 0.73f, 0.54f, 0.65f,   110.9f, 110.8f, 110.85f, 110.7f},
            // Координаты для 5 картинки
            {0.49f, 0.30f, 0.18f, 0.69f, 0.70f, 0.66f,  110.9f, 110.8f, 110.85f, 110.7f},
            // Координаты для 6 картинки
            {0.38f, 0.34f, 0.88f, 0.35f, 0.47f, 0.38f, 0.74f, 0.12f,  110.85f, 110.7f},
            // Координаты для 7 картинки
            {0.31f, 0.03f, 0.10f, 0.21f, 0.20f, 0.52f, 0.57f, 0.38f, 0.66f, 0.44f,  110.85f, 110.7f},
            // Координаты для 8 картинки
            {0.70f, 0.68f, 0.72f, 0.58f, 0.29f, 0.44f, 0.12f, 0.52f, 0.25f, 0.85f, 110.85f, 110.7f},
            // Координаты для 9 картинки
            {110.9f, 110.8f, 110.85f, 110.7f,  110.9f, 110.8f, 110.85f, 110.7f, 110.85f, 110.7f},
            // Координаты для 10 картинки
            {110.9f, 110.8f, 110.85f, 110.7f,  110.9f, 110.8f, 110.85f, 110.7f, 110.85f, 110.7f},
            // Координаты для 11 картинки
            {110.9f, 110.8f, 110.85f, 110.7f,  110.9f, 110.8f, 110.85f, 110.7f, 110.85f, 110.7f},
    };

    // Массивы заголовков и описаний для всплывающих окон
    private String[][] popupTitles = {
            {"Clearance Lights", "Windshields", "Turn signal lights, Four-way flasher and Marker lights", "Assembly Lights", "Under Engine"},
            {"Alternator", "Water pump", "Serpentine belt", "All rubber hoses and the wires", "Back Light"},
            {"Air Compressor", "Power steering pump", "General overview, all rubber hoses and the wires", "Предмет4", "Предмет5"},
            {"Coolant reservoir", "Steering fluid reservoir", "Oil level dipstick", "Предмет4", "Предмет5"},
            {"Steering Wheel Shaft", "Gear Box", "Pitman Arm, Drag Link, Upper and lower control arms, Tie rod", "Предмет4", "Предмет5"},
            {"Spring Leaves", "Spring Hangers", "U-Bolts", "Shock Absorber", "Предмет5"},
            {"Air brake hoses and Abs lines", "Air brake chamber", "Push road and slack adjuster", "Brake lining", "Brake Drum"},
            {"Outside and Inside the tire", "Tread Depth", "Lug Nuts", "Hub Seal", "Air Valve"},

    };

    private String[][] popupDescriptions = {
            {"PMS, no missing lens, amber color, clean, properly working", "No cracks, No damages, No chips, clean, Not broken, properly install with the rubber seal", "Amber color, No missing lens, Clean, no chips, not loose", "Low and High beam, no cracks, no chips, not broken, clean, PMS", "No oil, no any leaks"},
            {"Belt driven, PMS, no cracks, no bent or broken. Wires are properly connected, not loose, not corrosion, clean", "Belt driven, PMS, No cracks, No damages, NO VISIBLE LEAKS", "Not frayed, not cut, no loose, tension between 1/2-1/4 an inch", "Properly connected, both ends: clamps, no abrasion, no buldges, no cuts, no visible and audible leaks", "Описание 5"},
            {"Gear driven, PMS, not cracked, not bent or broken, NO AUDIBLE LEAKS", "Gear driven, PMS, not cracked, not bent or broken, no visible leaks", "Properly connected, both ends: clamps, no abrasion, no bulges, no cuts, no visible and audible leaks", "Описание антифриза", "Описание шины"},
            {"PMS, Not shifted, Not loose, NO VISIBLE LEAKS, cap is present, not broken, no cracks, no cheaps, LEVEL BETWEEN MIN and MAX", "PMS, NO LEAKS, level between MIN and MAX, Cap is present - not broken, not loose, no missing", "Level between MIN and MAX", "Описание4", "Описание5"},
            {"Free play is no more 10 degrees, Not twisted, not bent, no cracks, secured with both side the U-joints, lubricated, not missing any parts", "PMS, no missing hardware, No cracks, not bent, Not broken, NO VISIBLE LEAKS", "All parts not twisted, not bent,  No cracks, PMS with castle nuts and cotter pins", "Описание4", "Описание5"},
            // suspension system
            {"Not shifted, Not scissoring, No cracks, Not bent, Not broken, tightly secured both ends with the spring hangers", "Attached to the frame, No missing hardware, No cracks, Not bent, Not broken, no illegal welds", "PMS, Not shifted, Not rusted, No cracks", "NO VISIBLE LEAKS, Not bent, PMS with the hardware and rubber bushings, No dry rot", "Описание5"},
            // air brakes
            {"PMS, No abrasion, No bulges, No cuts, NO AIR LEAKS", "PMS, No cracks, Not bent, Not broken, NO AIR LEAKS", "Free play no more than 1 inch when brake is released. Push road not bent, no missing clevis pins or cotter pins", "The thickness is no less than 1/4 of an inch", "No cracks, not bent or broken, NO OIL OR DEBRIS between the lining"},
            // wheels
            {"No cracks, Not bent, Not broken, no illegal welds, No bulges, No cuts", "Minimum 4/32 inch. Tread is even worn, no recap. Check pressure by gauge at least 100 PSI", "Not missing, Not loose, No rusty, NO SHINY THREADS which indicates looseness", "No missing hardware and NO VISIBLE LEAKS", "Metal cap is present, No cracks, NO AIR LEAKS"}, // Wheels

    };

    private int imageWidthPx;
    private int imageHeightPx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_trip);

        textView = findViewById(R.id.textView2);
        textView.setText("Pre-trip (PART B)");

        carImageView = findViewById(R.id.car_image);

        dotImages = new ImageView[]{
                findViewById(R.id.dot_image),
                findViewById(R.id.dot_image1),
                findViewById(R.id.dot_image2),
                findViewById(R.id.dot_image3),
                findViewById(R.id.dot_image4)
        };

        // Установка начального изображения и координат точек
        carImageView.setImageResource(imageResIds[currentImageIndex]);
        carImageView.post(new Runnable() {
            @Override
            public void run() {
                imageWidthPx = carImageView.getWidth();
                imageHeightPx = carImageView.getHeight();
                initDots(currentImageIndex);
            }
        });

        // Initialize our ad request.
        MobileAds.initialize(this, initializationStatus -> {});
        // Set your test devices. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // Uncomment the following line to get test ads(replace device id with your own device id)...
        // MobileAds.setRequestConfiguration(new RequestConfiguration.Builder().setTestDeviceIds(Collections.singletonList("102C2C747D96B6BBFC45B9E7D9238BE9")).build());
        adContainerView = findViewById(R.id.ad_view);
        adContainerView.post(this::loadBanner);

    }

    private void initDots(int imageIndex) {
        // Устанавливаем координаты для текущего изображения
        float[] coordsPercent = dotCoordinatesPercent[imageIndex];

        // Обновляем координаты для точек относительно изображения
        for (int i = 0; i < dotImages.length; i++) {
            if (i * 2 + 1 < coordsPercent.length) {
                int leftPx = Math.round(coordsPercent[i * 2] * imageWidthPx);
                int topPx = Math.round(coordsPercent[i * 2 + 1] * imageHeightPx);
                setDotPosition(dotImages[i], leftPx, topPx);

                // Устанавливаем обработчик клика для каждой точки
                final int dotIndex = i; // Номер точки
                dotImages[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        togglePopups(v, popupTitles[currentImageIndex][dotIndex], popupDescriptions[currentImageIndex][dotIndex]);
                    }
                });

                dotImages[i].setVisibility(View.VISIBLE);
            } else {
                dotImages[i].setVisibility(View.GONE);
            }
        }

        // Устанавливаем изображение в зависимости от текущего индекса
        carImageView.setImageResource(imageResIds[imageIndex]);
    }

    private void setDotPosition(ImageView dot, int leftPx, int topPx) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) dot.getLayoutParams();
        params.leftMargin = leftPx;
        params.topMargin = topPx;
        dot.setLayoutParams(params);
    }

    public void showPreviousImage(View view) {
        closeBothPopups();  // Закрываем все всплывающие окна перед сменой изображения

        if (currentImageIndex > 0) {
            currentImageIndex--;
        } else {
            currentImageIndex = imageResIds.length - 1;
        }
        carImageView.setImageResource(imageResIds[currentImageIndex]);
        carImageView.post(new Runnable() {
            @Override
            public void run() {
                imageWidthPx = carImageView.getWidth();
                imageHeightPx = carImageView.getHeight();
                initDots(currentImageIndex);
            }
        });
    }

    public void showNextImage(View view) {
        closeBothPopups();  // Закрываем все всплывающие окна перед сменой изображения

        if (currentImageIndex < imageResIds.length - 1) {
            currentImageIndex++;
        } else {
            currentImageIndex = 0;
        }
        carImageView.setImageResource(imageResIds[currentImageIndex]);
        carImageView.post(new Runnable() {
            @Override
            public void run() {
                imageWidthPx = carImageView.getWidth();
                imageHeightPx = carImageView.getHeight();
                initDots(currentImageIndex);
            }
        });
    }

    private void togglePopups(View anchorView, String topText, String bottomText) {
        if (anchorView == currentAnchorView && (currentPopupWindow != null && currentPopupWindow.isShowing())) {
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
        popupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBothPopups();
            }
        });

        currentPopupWindow.showAsDropDown(anchorView, 0, 0, Gravity.NO_GRAVITY);
    }

    private void showPopupBelow(View anchorView, String text) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout, null);

        popupTextView = popupView.findViewById(R.id.popup_text);
        popupTextView.setText(text);

        bottomPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBothPopups();
            }
        });

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
}
