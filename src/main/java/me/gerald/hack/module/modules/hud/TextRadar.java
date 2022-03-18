package me.gerald.hack.module.modules.hud;

import me.gerald.hack.gui.ogGUI.oldComps.hud.TextRadarComponent;
import me.gerald.hack.module.HUDModule;
import me.gerald.hack.setting.settings.NumSetting;

public class TextRadar extends HUDModule {
    public TextRadar() {
        super(new TextRadarComponent(150, 1, 1, 1), "TextRadar", Category.HUD, "Sheesh.");
    }

    public NumSetting x = register(new NumSetting("X", 1, 0,  mc.displayWidth));
    public NumSetting y = register(new NumSetting("Y", 1, 0,  mc.displayHeight));
}
