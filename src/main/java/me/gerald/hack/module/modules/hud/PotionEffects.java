package me.gerald.hack.module.modules.hud;

import me.gerald.hack.gui.ogGUI.oldComps.hud.PotionEffectComponent;
import me.gerald.hack.module.HUDModule;
import me.gerald.hack.setting.settings.NumSetting;

public class PotionEffects extends HUDModule {
    public PotionEffects() {
        super(new PotionEffectComponent(mc.displayWidth, 1, 1, 1), "PotionEffects", Category.HUD, "Renders potion effects.");
    }

    public NumSetting x = register(new NumSetting("X", 1, 0,  mc.displayWidth));
    public NumSetting y = register(new NumSetting("Y", 1, 0,  mc.displayHeight));
}
