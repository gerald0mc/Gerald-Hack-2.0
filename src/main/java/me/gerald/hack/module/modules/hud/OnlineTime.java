package me.gerald.hack.module.modules.hud;

import me.gerald.hack.gui.ogGUI.oldComps.hud.OnlineTimeComponent;
import me.gerald.hack.module.HUDModule;
import me.gerald.hack.setting.settings.NumSetting;

public class OnlineTime extends HUDModule {
    public OnlineTime() {
        super(new OnlineTimeComponent(1, 1, 1,1), "OnlineTime", Category.HUD, "Shows how long you have been on a server.");
    }

    public NumSetting x = register(new NumSetting("X", 1, 0,  mc.displayWidth));
    public NumSetting y = register(new NumSetting("Y", 1, 0,  mc.displayHeight));
}
