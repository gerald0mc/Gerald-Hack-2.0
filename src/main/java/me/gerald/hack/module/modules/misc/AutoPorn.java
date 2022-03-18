package me.gerald.hack.module.modules.misc;

import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.StringSetting;
import net.minecraftforge.client.event.ClientChatEvent;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class AutoPorn extends Module {
    public AutoPorn() {
        super("AutoPorn", Category.MISC, "Porn things.");
    }

    public StringSetting pornType = register(new StringSetting("PornType", "Milf"));

    @Override
    public void onEnable() {
        try {
            Desktop.getDesktop().browse(URI.create("https://www.pornhub.com/video/search?search=" + pornType.getString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        toggle();
    }
}
