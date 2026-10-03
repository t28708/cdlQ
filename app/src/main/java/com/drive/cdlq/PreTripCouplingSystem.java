package com.drive.cdlq;

public class PreTripCouplingSystem {

    public static String textCaption = "Coupling System";

    public static int[] imageResIds = {
            R.drawable.coupling_system,
            R.drawable.coupling_system2,
            R.drawable.coupling_system3
    };  // Примеры ресурсов изображений

    // Массивы координат для точек на разных изображениях в процентном соотношении
    public static float[][] dotCoordinatesPercent = {
            // Координаты для первой картинки
            {0.29f, 0.67f, 0.23f, 0.61f, 0.30f, 0.61f, 0.61f, 0.54f, 110.85f, 110.9f},
            // Координаты для второй картинки
            {0.08f, 0.50f, 0.60f, 0.45f, 0.11f, 0.44f, 0.21f, 0.44f, 0.87f, 0.40f, 0.72f, 0.37f, },
            {0.75f, 0.39f, 0.22f, 0.52f,  110.85f, 110.9f, 110.85f, 110.9f, 110.85f, 110.9f,  110.85f, 110.9f},
    };


    // Массивы заголовков и описаний для всплывающих окон
    public static String[][] popupTitles = {
            {"Red Line", "Blue Line", "Green Line", "Air Lines", "Under Engine"},
            {"Mounting Plate", "The platform", "Fifth Wheel", "Release Handle", "Apron of the trailer", "Locking Jaws", "Kingpin of the trailer"},
            { "Locking Jaws", "Kingpin of the trailer", "3", "4", "5"},

    };

    public static String[][] popupDescriptions = {
            {"Emergency Air Line", "Service Air Line", "No abrasion, No cuts, PMS both ends with lock latches", "No abrasion, No bulges, No cuts, No AIR LEAKS, PMS with glad handles, inside the rubber seal there is no damage", "No oil, no any leaks"},
            {"PMS, Not Shifted, Not loose, Not cracked", "both side PMS, no missing hardware, No cracks, No damage, Not bent, Not broken, Not illegal welds", "PMS with pivot pin and cotter pin from both sides", "Should be in fully locked position when the trailer is present", "No cracks, No damage, Not bent, Not broken, No illegal Holes", "Has closed around shank of kingpin", "No cracks, Not bent, Not broken"},
            {"Has closed around shank of kingpin", "No cracks, Not bent, Not broken", "3", "4", "5"},

    };
}
