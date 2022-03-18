package me.gerald.hack.module.modules.hud;

import me.gerald.hack.gui.ogGUI.oldComps.hud.OnlineFriendsComponent;
import me.gerald.hack.module.HUDModule;
import me.gerald.hack.setting.settings.NumSetting;

public class OnlineFriends extends HUDModule {
    public OnlineFriends() {
        super(new OnlineFriendsComponent(250, 1, 1, 1), "OnlineFriends", Category.HUD, "Shows online friends.");
    }

    public NumSetting x = register(new NumSetting("X", 1, 0,  mc.displayWidth));
    public NumSetting y = register(new NumSetting("Y", 1, 0,  mc.displayHeight));
}
