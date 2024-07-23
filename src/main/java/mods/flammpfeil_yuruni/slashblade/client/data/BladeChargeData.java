package mods.flammpfeil_yuruni.slashblade.client.data;

public class BladeChargeData {

    private static int playerCharge;

    public static void set(int bladeCharges) {
        BladeChargeData.playerCharge = bladeCharges;
    }

    public static int getCharges() {
        return playerCharge;
    }

}
