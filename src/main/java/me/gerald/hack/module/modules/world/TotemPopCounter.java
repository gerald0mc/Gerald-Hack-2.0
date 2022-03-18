package me.gerald.hack.module.modules.world;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.event.events.PlayerDeathEvent;
import me.gerald.hack.event.events.TotemPopEvent;
import me.gerald.hack.module.Module;
import me.gerald.hack.util.MessageUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TotemPopCounter extends Module {
    public TotemPopCounter() {
        super("TotemPopCounter", Category.WORLD, "Says in chat when someone pops.");
    }

    @SubscribeEvent
    public void onPop(TotemPopEvent event) {
        if(event.getPopCount() == 1) {
            MessageUtil.sendRemovableMessage(ChatFormatting.AQUA + event.getEntity().getDisplayName().getFormattedText() + ChatFormatting.GRAY + " has just popped a totem.", event.getEntity().getEntityId(), false);
        }else if(event.getPopCount() > 1) {
            MessageUtil.sendRemovableMessage(ChatFormatting.AQUA + event.getEntity().getDisplayName().getFormattedText() + ChatFormatting.GRAY + " has just popped " + ChatFormatting.RED + event.getPopCount() + ChatFormatting.GRAY + " totems.", event.getEntity().getEntityId(), false);
        }
    }

    @SubscribeEvent
    public void onDeath(PlayerDeathEvent event) {
        MessageUtil.sendRemovableMessage(ChatFormatting.AQUA + event.getEntity().getDisplayName().getFormattedText() + ChatFormatting.GRAY + " has just died what a retard.", event.getEntity().getEntityId(), false);
    }
}
