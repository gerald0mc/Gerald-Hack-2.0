package me.gerald.hack.module.modules.hud;

import me.gerald.hack.gui.ogGUI.oldComps.hud.TotemComponent;
import me.gerald.hack.module.HUDModule;
import me.gerald.hack.setting.settings.NumSetting;

public class TotemCount extends HUDModule {
    public TotemCount() {
        super(new TotemComponent(1, 61, 1, 1), "TotemCount", Category.HUD, "Counts all the totems you have.");
    }

    public NumSetting x = register(new NumSetting("X", 1, 0,  mc.displayWidth));
    public NumSetting y = register(new NumSetting("Y", 1, 0,  mc.displayHeight));
}
