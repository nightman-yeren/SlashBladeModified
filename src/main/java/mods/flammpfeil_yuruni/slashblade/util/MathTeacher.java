package mods.flammpfeil_yuruni.slashblade.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathTeacher {
    public static double advancedRound(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
