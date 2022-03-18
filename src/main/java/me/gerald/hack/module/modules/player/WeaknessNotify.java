package me.gerald.hack.module.modules.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.module.Module;
import me.gerald.hack.util.MessageUtil;
import net.minecraft.init.MobEffects;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class WeaknessNotify extends Module {
    public WeaknessNotify() {
        super("WeaknessNotify", Category.PLAYER, "Alerts you when you get weaknessed.");
    }

    public boolean hasAnnounced = false;

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        if(mc.player.isPotionActive(MobEffects.WEAKNESS) && !hasAnnounced) {
            MessageUtil.sendClientMessage("You now have " + ChatFormatting.DARK_GRAY + "WEAKNESS!");
            hasAnnounced = true;
        }
        if (!mc.player.isPotionActive(MobEffects.WEAKNESS) && hasAnnounced) {
            MessageUtil.sendClientMessage("You no longer have " + ChatFormatting.DARK_GRAY + "WEAKNESS!");
            hasAnnounced = false;
        }
    }

    @Override
    public void onDisable() {
        if(hasAnnounced)
            hasAnnounced = false;
    }
}
