package com.drive.cdlq;

public class PreTripB {

    public static String textCaption = "Part B";

    public static int[] imageResIds = {
            R.drawable.pre_trip_b_1,
            R.drawable.pre_trip_b_2,
            R.drawable.pre_trip_b_3,
            R.drawable.pre_trip_b_4,
            R.drawable.pre_trip_b_5,
            R.drawable.pre_trip_b_6,
            R.drawable.pre_trip_b_7,
            R.drawable.pre_trip_b_8,
            R.drawable.pre_trip_b_9,
            R.drawable.pre_trip_b_9,
    };  // Примеры ресурсов изображений

    // Массивы координат для точек на разных изображениях в процентном соотношении
    public static float[][] dotCoordinatesPercent = {
            // Координаты для первой картинки
            {0.13f, 0.17f, 0.58f, 0.37f, 0.68f, 0.77f, 0.17f, 0.86f, 110.85f, 110.9f, },
            // Координаты для второй картинки
            {0.41f, 0.79f, 0.79f, 0.38f, 0.40f, 0.39f, 0.66f, 0.52f, 110.85f, 110.9f},
            // Координаты для третьей картинки
            {0.14f, 0.46f, 0.25f, 0.45f, 0.30f, 0.39f, 0.86f, 0.43f, 0.68f, 0.71f, 110.85f, 110.7f},
            // Координаты для 4 картинки
            {0.07f, 0.16f, 0.29f, 0.37f, 0.86f, 0.48f, 0.52f, 0.49f, 0.41f, 0.37f, 110.85f, 110.7f},
            // Координаты для 5 картинки
            {0.28f, 0.52f, 0.30f, 0.69f, 0.86f, 0.17f, 0.73f, 0.38f,  110.9f, 110.8f, 110.85f, 110.7f},
            // Координаты для 6 картинки
            {0.39f, 0.67f, 0.89f, 0.75f,  110.85f, 110.7f,  110.85f, 110.7f,  110.85f, 110.7f,  110.85f, 110.7f,  110.85f, 110.7f},
            // Координаты для 7 картинки
            {0.49f, 0.34f, 0.79f, 0.63f, 0.15f, 0.62f, 0.37f, 0.47f, 0.35f, 0.60f,  110.85f, 110.7f},
            // Координаты для 8 картинки
            {0.57f, 0.45f, 110.85f, 110.7f, 110.85f, 110.7f, 110.85f, 110.7f, 110.85f, 110.7f, 110.85f, 110.7f, 110.85f, 110.7f},
            // Координаты для 9 картинки
            {0.40f, 0.04f, 0.60f, 0.49f, 0.48f, 0.81f, 0.29f, 0.77f, 110.85f, 110.7f, 110.85f, 110.7f},
            // Координаты для 10 картинки
            {0.20f, 0.85f, 0.23f, 0.92f,   110.9f, 110.8f, 110.85f, 110.7f, 110.85f, 110.7f},
            // Координаты для 11 картинки
            {110.9f, 110.8f, 110.85f, 110.7f,  110.9f, 110.8f, 110.85f, 110.7f, 110.85f, 110.7f},
    };


    // Массивы заголовков и описаний для всплывающих окон
    public static String[][] popupTitles = {
            {"Bracket of the mirror", "Door Hingers", "Weather seals", "Steps to the cab", "Under Engine"},
            {"Step to the catwalk", "Cross members of the frame", "Drive Shaft", "Frame", "Back Light"},
            {"Spring mounted", "Spring leaves", "U-bolt", "Stock absorber", "Air Bag"},
            {"Dual Wheels", "Sidewall of the tires", "Budd spacing", "Rim", "Lug Nuts"},
            {"Axle seal", "Air valve", "Bracket of the mud flap", "Mud Flap", ""},
            {"Rear light", "License Plate", "", "", ""},
            {"Air Hoses and ABS line", "Air brake chamber", "Slack adjuster and push road", "Brake lining", "Brake Drum"},
            {"The muffler and pips", "", "", "", ""},
            {"Clearance lights", "Back door", "Lock latch", "DOT reflective tape", ""},
            {"The rear lights", "License plate", "", "", ""},

    };

    public static String[][] popupDescriptions = {
            {"PMS, No missing parts, no twisted, Not bent, No cracks, no missing glass, all mirrors should be clean", "PMS, no missing hardware, not cracks, not bent, not broken", "PMS to the cab, no cuts, not ripped", "PMS, no missing hardware, not twisted, not bent, no cracks, no grease, Should be clean, able to hold the weight of the driver", "No oil, no any leaks"},
            {"PMS, not missing parts, no crack, not bent, no grease, clean", "PMS, not missing, not twisted, not bent, not cracked", "No cracks, not bent, not broken", "No cracks, no bent, not broken", "Описание 5"},
            {"PMS, not missing hardware, not twisted, not bent, not cracks, no illegal welds", "not shifted, not scissoring, not cracks, PMS", "PMS, not shifted, not bent, no cracks", "PMS, no leaks, rubber mounting bushings, not missing, not damages", "No bulges, no cuts, PROPERLY INFLATED, Upper and lower mounts are properly secured"},
            {"The tires should be the same size, evenly worn, tread depth a minimum of 2/32 inch", "no bulges, no cuts, between tires no garbage or rocks", "no space between rims", "no cracks, not bent, not broken, no illegal welds", "not missing, no loose, no rust, no shiny threads which indicate looseness "},
            {"No missing hardware, NO VISIBLE LEAKS", "Outside and inside of the weal - THE METAL CUP is present, not missing, no air leaks, Air pressure should be at least 100 PSI", "not missing, not twisted, not cracked", "Is not split and not dragging of the ground", "Описание5"},
            {"Turn signal lights, 4-four flasher lights, brake lights and marker lights - red color, reverse is clear color, no missing lens, PMS", "Should be visible, clean and PMS, light of the license plate working", "", "Описание4", "Описание5"},
            {"PMS, no abrasion, no bulges, no cuts, NO AIR LEAKS", "PMS to the bracket, no cracks, not bent, not broken, NO AIR LEAKS ", "Free play no more than 1 inch when release brake, no missing clevis pin and cotter pins", "no less than a quarter of an inch thickness", "No cracks, no damage, not bent, not broken, no oil debris between brake lining and brake drums"},
            {"PMS, not bent, not broken, no holes, C-clamps is present, no black shoots ", "", "", "Описание4", "Описание5"},
            {"PMS, Red color, not missing lens, clean", "locked and secured with the lock latch, no missing rivets, no holes, should open and close properly", "not cracked, not bent, not broken, no missing hardware, is visible and clean", "", ""},
            {"Turn signal lights, 4-four flasher lights, brake lights and marker lights - red color, reverse is clear color, no missing lens, PMS", "Should be visible, clean and PMS, light of the license plate working", "", "Описание4", "Описание5"},
    };
}
