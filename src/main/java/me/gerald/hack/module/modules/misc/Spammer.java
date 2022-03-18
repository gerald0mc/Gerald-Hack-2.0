package me.gerald.hack.module.modules.misc;

import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.setting.settings.StringSetting;
import me.gerald.hack.util.TimerUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.RandomStringUtils;

public class Spammer extends Module {
    public Spammer() {
        super("Spammer", Category.MISC, "Spams chat.");
    }

    public NumSetting delay = register(new NumSetting("Delay", 3, 0, 5));
    public BoolSetting randomNumbers = register(new BoolSetting("RandomNumbers", true));
    public BoolSetting customString = register(new BoolSetting("CustomString", true));
    public StringSetting string = register(new StringSetting("String", "Gerald(Hack) on top!"));

    public TimerUtil timer = new TimerUtil();

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(timer.passedMs((long) delay.getValue() * 1000)) {
            mc.player.sendChatMessage((customString.getValue() ? string.getString() : "Gerald(Hack) on top!") + (randomNumbers.getValue() ? " | " + RandomStringUtils.random(6, true, true) : ""));
            timer.reset();
        }
    }
}
