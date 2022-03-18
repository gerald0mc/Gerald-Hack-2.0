package me.gerald.hack.module.modules.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.util.MessageUtil;
import me.gerald.hack.util.TimerUtil;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Respawn extends Module {
    public Respawn() {
        super("Respawn", Category.PLAYER, "Respawns the player on death.");
    }

    public NumSetting delay = register(new NumSetting("Delay", 0, 0, 1000));
    public BoolSetting cancelScreen = register(new BoolSetting("CancelScreen", true));

    TimerUtil timer = new TimerUtil();
    String deathCoords;
    boolean hasAnnounced = false;

    @Override
    public String getMetaData() {
        return deathCoords != null ? "[" + deathCoords + ChatFormatting.RESET + "]" : "";
    }

    @SubscribeEvent
    public void onDeath(GuiOpenEvent event) {
        if(event.getGui() instanceof GuiGameOver) {
            if(timer.passedMs((long) delay.getValue())) {
                if(cancelScreen.getValue()) event.setCanceled(true);
                ChatFormatting formatting = ChatFormatting.GRAY;
                if(mc.player.dimension == -1)
                    formatting = ChatFormatting.RED;
                else if(mc.player.dimension == 0)
                    formatting = ChatFormatting.GREEN;
                else if(mc.player.dimension == 1)
                    formatting = ChatFormatting.LIGHT_PURPLE;
                deathCoords = ChatFormatting.GRAY + "X:" + formatting + mc.player.getPosition().getX() + ChatFormatting.GRAY + " Y:" + formatting + mc.player.getPosition().getY() + ChatFormatting.GRAY + " Z:" + formatting + mc.player.getPosition().getZ();
                if(!hasAnnounced) MessageUtil.sendClientMessage(ChatFormatting.GRAY + "You died at [" + deathCoords + ChatFormatting.GRAY + "]");
                hasAnnounced = true;
                mc.player.respawnPlayer();
                timer.reset();
                hasAnnounced = false;
            }
        }
    }
}
