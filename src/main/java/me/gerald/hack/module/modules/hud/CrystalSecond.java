package me.gerald.hack.module.modules.hud;

import me.gerald.hack.gui.ogGUI.oldComps.hud.CrystalSecondComponent;
import me.gerald.hack.module.HUDModule;
import me.gerald.hack.setting.settings.NumSetting;

public class CrystalSecond extends HUDModule {
    public CrystalSecond() {
        super(new CrystalSecondComponent(1, 81, 1, 1), "CrystalPerSecond", Category.HUD, "Crystals per second.");
    }

    public NumSetting x = register(new NumSetting("X", 1, 0,  mc.displayWidth));
    public NumSetting y = register(new NumSetting("Y", 1, 0,  mc.displayHeight));
}
