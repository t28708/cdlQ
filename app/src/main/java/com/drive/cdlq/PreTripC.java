package com.drive.cdlq;

public class PreTripC {

    public static String textCaption = "Part C";

    public static int[] imageResIds = {
            R.drawable.pre_trip_c_1,
            R.drawable.pre_trip_c_2,
            R.drawable.pre_trip_c_3,
            R.drawable.pre_trip_c_1,
            R.drawable.pre_trip_b_7,
            R.drawable.pre_trip_c_4,
            R.drawable.pre_trip_c_5,
            R.drawable.pre_trip_c_5,
            R.drawable.pre_trip_c_6,
    };  // Примеры ресурсов изображений

    // Массивы координат для точек на разных изображениях в процентном соотношении
    public static float[][] dotCoordinatesPercent = {
            // Координаты для первой картинки
            {0.34f, 0.37f, 0.55f, 0.06f, 0.63f, 0.60f, 0.61f, 0.90f,  110.85f, 110.9f,},
            // Координаты для второй картинки
            {0.16f, 0.86f, 0.71f, 0.14f, 0.86f, 0.05f, 110.85f, 110.9f, 110.85f, 110.9f,},
            // Координаты для третьей картинки
            {0.49f, 0.76f, 0.50f, 0.83f, 0.58f, 0.31f, 110.85f, 110.9f, 110.85f, 110.9f,},
            // Координаты для 4 картинки
            {0.67f, 0.92f, 110.85f, 110.9f,  110.85f, 110.9f, 110.85f, 110.9f, 110.85f, 110.9f,},
            // Координаты для 5 картинки
            {0.49f, 0.34f, 0.79f, 0.63f, 0.15f, 0.62f, 0.37f, 0.47f, 0.35f, 0.60f,  110.85f, 110.7f},
            // Координаты для 6 картинки
            {0.81f, 0.81f, 0.28f, 0.62f, 0.52f, 0.33f,  110.85f, 110.9f, 110.85f, 110.9f, 110.85f, 110.9f},
            // Координаты для 7 картинки
            {0.12f, 0.54f, 0.31f, 0.59f, 0.39f, 0.58f, 0.20f, 0.73f, 0.73f, 0.36f, 110.85f, 110.9f,  110.85f, 110.9f},
            // Координаты для 8 картинки
            {0.76f, 0.47f, 0.66f, 0.47f, -0.01f, 0.42f, 0.02f, 0.40f,   110.85f, 110.9f, 110.85f, 110.9f,  110.85f, 110.9f},
            // Координаты для 9 картинки
            {0.18f, 0.41f, 110.85f, 110.9f,  110.85f, 110.9f, 110.85f, 110.9f,  110.85f, 110.9f},
            // Координаты для 10 картинки
            {110.85f, 110.9f, 110.85f, 110.9f,  110.85f, 110.9f, 110.85f, 110.9f, 0.24f, 0.89f},
            // Координаты для 11 картинки
            {110.85f, 110.9f, 110.85f, 110.9f,  110.85f, 110.9f, 110.85f, 110.9f, 0.24f, 0.89f},
    };


    // Массивы заголовков и описаний для всплывающих окон
    public static String[][] popupTitles = {
            {"Headboard of the trailer", "Clearance light front and side", "Sidewall of the trailer", "DOT reflective tape", "", ""},
            {"Side rail of the trailer", "Cross members", "Wood floor", "", "", ""},
            {"Landing gears", "Support of the landing gears", "Crack handle", "", "", ""},
            {"Between MUD flap and landing gear", "", "", "", "", ""},
            {"Air Hoses and ABS line", "Air brake chamber", "Slack adjuster and push road", "Brake lining", "Brake Drum"},
            {"Mounting trailer arm", "Shock absorbers", "Air bags", "", "", ""},
            {"Dual Wheels", "Sidewall of the tires", "Budd spacing", "Rim", "Lug Nuts"},
            {"HUB seal", "Air valve", "Bracket of the mud flap", "Mud Flap", ""},
            {"Abs light", "", "", "", "", ""},

    };

    public static String[][] popupDescriptions = {
            {"No holes, no damages, no missing rivets", "Amber color, no missing lens, PMS with rubber seals, clean and working properly", "No holes, no damages, no missing rivets", "visible and clean", "", ""},
            {"Straight, not missing rivers, not bent", "All the way to the end, not shifted, not bent, not crack, not loose, PMS", "No visible and illegal holes", "", "", ""},
            {"Must be fully raised", "Not shifted, not bent, not crack, not loose, PMS", "Is in the proper position, not bent, not missing", "", "", ""},
            {"should be good another space ", "", "", "", "", ""},
            {"PMS, no abrasion, no bulges, no cuts, NO AIR LEAKS", "PMS to the bracket, no cracks, not bent, not broken, NO AIR LEAKS ", "Free play no more than 1 inch when release brake, no missing clevis pin and cotter pins", "no less than a quarter of an inch thickness", "No cracks, no damage, not bent, not broken, no oil debris between brake lining and brake drums"},
            {"is tied and secured to the frame with no cracks, not bent, not broken, no illegal welds", "PMS, NO LEAKS, Rubber mounting bushings not missing or damages", "not bulges, not cuts, properly inflated, upper and lower mounts are properly secured", "", "", ""},
            {"The tires should be the same size, evenly worn, tread depth a minimum of 2/32 inch", "no bulges, no cuts, between tires no garbage or rocks", "no space between rims", "no cracks, not bent, not broken, no illegal welds", "not missing, no loose, no rust, no shiny threads which indicate looseness "},
            {"No missing hardware, NO VISIBLE LEAKS", "Outside and inside of the weal - THE METAL CUP is present, not missing, no air leaks, Air pressure should be at least 100 PSI", "not missing, not twisted, not cracked", "Is not split and not dragging of the ground", "Описание5"},
            {"Amber color, PMS, Should be on and off when ignition turns on", "", "", "", "", ""},
    };
}
