package com.drive.cdlq;

public class PreTripInCabInspection {

    public static String textCaption = "In Cab Inspection";

    public static int[] imageResIds = {
            R.drawable.coupling_system,
            R.drawable.no_item,
            R.drawable.no_item,
            R.drawable.no_item,
            R.drawable.no_item,
            R.drawable.no_item,
            R.drawable.no_item,
            R.drawable.no_item,
    };  // Примеры ресурсов изображений

    // Массивы координат для точек на разных изображениях в процентном соотношении
    public static float[][] dotCoordinatesPercent = {
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
    public static String[][] popupTitles = {
            {"Clearance Lights", "Windshields", "Turn signal lights, Four-way flasher and Marker lights", "Assembly Lights", "Under Engine"},
            {"Alternator", "Water pump", "Serpentine belt", "All rubber hoses and the wires", "Back Light"},
            {"Air Compressor", "Power steering pump", "General overview, all rubber hoses and the wires", "Предмет4", "Предмет5"},
            {"Coolant reservoir", "Steering fluid reservoir", "Oil level dipstick", "Предмет4", "Предмет5"},
            {"Steering Wheel Shaft", "Gear Box", "Pitman Arm, Drag Link, Upper and lower control arms, Tie rod", "Предмет4", "Предмет5"},
            {"Spring Leaves", "Spring Hangers", "U-Bolts", "Shock Absorber", "Предмет5"},
            {"Air brake hoses and Abs lines", "Air brake chamber", "Push road and slack adjuster", "Brake lining", "Brake Drum"},
            {"Outside and Inside the tire", "Tread Depth", "Lug Nuts", "Hub Seal", "Air Valve"}

    };

    public static String[][] popupDescriptions = {
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
}
