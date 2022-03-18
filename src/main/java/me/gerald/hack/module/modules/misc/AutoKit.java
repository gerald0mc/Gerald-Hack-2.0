package me.gerald.hack.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.event.events.PlayerDeathEvent;
import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.setting.settings.StringSetting;
import me.gerald.hack.util.TimerUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoKit extends Module {
    public AutoKit() {
        super("AutoKit", Category.MISC, "Automatically does /kit [kitName] for you");
    }

    public StringSetting kitName = register(new StringSetting("KitName", "autoKit"));
    public NumSetting delay = register(new NumSetting("Delay", 50, 0, 250));

    public boolean hasDied = false;
    public TimerUtil timer =  new TimerUtil();

    @Override
    public String getMetaData() {
        return "[" + ChatFormatting.GRAY + kitName.getString() + ChatFormatting.RESET + "]";
    }

    @SubscribeEvent
    public void onDeath(PlayerDeathEvent event) {
        if(event.getEntity() == mc.player && !hasDied) {
            hasDied = true;
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        if(hasDied && mc.player.isEntityAlive() && !mc.player.isDead && mc.player.getHealth() > 1) {
            if(timer.passedMs((long) delay.getValue())) {
                mc.player.sendChatMessage("/kit " + kitName.getString());
                timer.reset();
                hasDied = false;
            }
        }
    }

    @Override
    public void onDisable() {
        if(hasDied)
            hasDied = false;
    }
}
