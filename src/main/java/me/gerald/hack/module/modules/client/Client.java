package me.gerald.hack.module.modules.client;

import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.StringSetting;

public class Client extends Module {
    public Client() {
        super("Client", Category.CLIENT, "Sheesh.");
    }

    public BoolSetting clientMessageWatermark = register(new BoolSetting("ClientMessageWatermark", false));
    public StringSetting prefix = register(new StringSetting("Prefix", "-"));
}
