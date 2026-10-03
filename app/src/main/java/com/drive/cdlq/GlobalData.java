package com.drive.cdlq;

public class GlobalData {

    // Переключатель режима рекламы: 1 — тестовая, 2 — рабочая (боевая)
    private static final int AD_MODE = 1;

    // Variable to check premium status
    public static boolean isPremium = false;

    // Автоматический выбор ID обычной рекламы в зависимости от AD_MODE
    public static String pubAdID = (AD_MODE == 1)
            ? "ca-app-pub-3940256099942544/9214589741" // тестовая реклама
            : "ca-app-pub-7073626021965181/8578191229"; // рабочая реклама

    // Автоматический выбор ID вознаграждаемой межстраничной рекламы в зависимости от AD_MODE
    public static String rewardedInterstitialAdUnitID = (AD_MODE == 1)
            ? "ca-app-pub-3940256099942544/5354046379" // тестовая реклама МЕЖДУ блоками
            : "ca-app-pub-7073626021965181/2287171770"; // настоящая реклама

}