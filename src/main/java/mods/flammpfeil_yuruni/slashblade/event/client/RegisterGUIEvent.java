package mods.flammpfeil_yuruni.slashblade.event.client;

import mods.flammpfeil_yuruni.slashblade.client.renderer.gui.BladeChargeOverlay;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RegisterGUIEvent {

    private static final class SingletonHolder {
        private static final RegisterGUIEvent instance = new RegisterGUIEvent();
    }

    private RegisterGUIEvent() {

    }

    public static RegisterGUIEvent getInstance() {
        return RegisterGUIEvent.SingletonHolder.instance;
    }

    @SubscribeEvent
    public static void registerGUIOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("bladecharge", BladeChargeOverlay.HUD_CHARGE);
    }

    public void register(IEventBus bus) {
        bus.register(this);
    }

}
