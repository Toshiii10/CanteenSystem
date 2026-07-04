package com.csis;

import java.awt.Color;
import java.awt.Font;

public final class Theme {
    // Minimalist Academic Palette: High contrast, low visual noise
    public static final Color BG = new Color(250, 250, 252);
    public static final Color SURFACE = Color.WHITE;
    public static final Color BORDER = new Color(220, 222, 224);
    
    public static final Color TEXT_PRIMARY = new Color(24, 24, 27);
    public static final Color TEXT_SECONDARY = new Color(82, 82, 91);
    public static final Color TEXT_MUTED = new Color(161, 161, 170);
    
    // Kiosk Accents
    public static final Color ACCENT = new Color(39, 39, 42); // Deep Charcoal
    public static final Color ACCENT_FG = Color.WHITE;
    public static final Color DANGER = new Color(185, 28, 28);
    public static final Color DANGER_FG = Color.WHITE;
    public static final Color STATUS_SUCCESS = new Color(21, 128, 61);
    public static final Color STATUS_WARNING = new Color(194, 65, 12);

    // Grid System Spacing
    public static final int SPACE_XS = 4, SPACE_SM = 8, SPACE_MD = 16, SPACE_LG = 24, SPACE_XL = 32;

    private Theme() {}

    // Typography: Modern Sans-Serif
    public static Font displayTitle() { return new Font("Segoe UI", Font.BOLD, 26); }
    public static Font heading() { return new Font("Segoe UI", Font.BOLD, 18); }
    public static Font subheading() { return new Font("Segoe UI", Font.BOLD, 14); }
    public static Font body() { return new Font("Segoe UI", Font.PLAIN, 14); }
    public static Font bodyBold() { return new Font("Segoe UI", Font.BOLD, 14); }
    public static Font caption() { return new Font("Segoe UI", Font.PLAIN, 12); }
    public static Font tableHeader() { return new Font("Segoe UI", Font.BOLD, 13); }
}