/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils.fonts;

import im.expensive.utils.fonts.common.Lang;
import im.expensive.utils.fonts.font.StyledFont;

public class Fonts {
    public static final String FONT_DIR = "/assets/minecraft/expensive/font2/";
    public static volatile StyledFont[] minecraft = new StyledFont[24];
    public static volatile StyledFont[] verdana = new StyledFont[24];
    public static volatile StyledFont[] gilroyBold = new StyledFont[24];
    public static volatile StyledFont[] msBold = new StyledFont[24];
    public static volatile StyledFont[] msMedium = new StyledFont[24];
    public static volatile StyledFont[] msLight = new StyledFont[24];
    public static volatile StyledFont[] msRegular = new StyledFont[24];
    public static volatile StyledFont[] msSemiBold = new StyledFont[24];
    public static volatile StyledFont[] gilroy = new StyledFont[24];
    public static volatile StyledFont[] sora = new StyledFont[24];
    public static volatile StyledFont[] woveline = new StyledFont[24];
    public static volatile StyledFont[] icons = new StyledFont[24];
    public static volatile StyledFont[] configIcon = new StyledFont[24];
    public static volatile StyledFont[] icons1 = new StyledFont[131];

    public static void init() {
        int i;
        long time = System.currentTimeMillis();
        Fonts.minecraft[8] = new StyledFont("mc.ttf", 8, 0.0f, 0.0f, 0.0f, false, Lang.ENG_RU);
        Fonts.icons[16] = new StyledFont("penus.ttf", 16, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        Fonts.icons[12] = new StyledFont("penus.ttf", 12, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        Fonts.woveline[19] = new StyledFont("woveline.otf", 19, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        Fonts.icons1[130] = new StyledFont("icons.ttf", 130, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        for (i = 8; i < 24; ++i) {
            Fonts.icons1[i] = new StyledFont("icons.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (i = 8; i < 16; ++i) {
            Fonts.verdana[i] = new StyledFont("verdana.ttf", i, 0.0f, 0.0f, 0.0f, false, Lang.ENG_RU);
        }
        for (i = 10; i < 23; ++i) {
            Fonts.sora[i] = new StyledFont("sora.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (i = 10; i < 23; ++i) {
            Fonts.configIcon[i] = new StyledFont("Glyphter.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (i = 10; i < 23; ++i) {
            Fonts.gilroyBold[i] = new StyledFont("gilroy-bold.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (i = 10; i < 24; ++i) {
            Fonts.gilroy[i] = new StyledFont("gilroy.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (i = 8; i < 24; ++i) {
            Fonts.msBold[i] = new StyledFont("Montserrat-Bold.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (i = 8; i < 24; ++i) {
            Fonts.msLight[i] = new StyledFont("Montserrat-Light.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (i = 8; i < 24; ++i) {
            Fonts.msMedium[i] = new StyledFont("Montserrat-Medium.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (i = 8; i < 24; ++i) {
            Fonts.msRegular[i] = new StyledFont("Montserrat-Regular.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (i = 8; i < 24; ++i) {
            Fonts.msSemiBold[i] = new StyledFont("Montserrat-SemiBold.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
    }
}

