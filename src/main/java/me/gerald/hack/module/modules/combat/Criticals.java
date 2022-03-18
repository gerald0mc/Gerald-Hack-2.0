package me.gerald.hack.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.ModeSetting;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Criticals extends Module {
    public Criticals() {
        super("Criticals", Category.COMBAT, "Hits crits for you.");
    }

    public ModeSetting criticalMode = register(new ModeSetting("Mode", Mode.Packet));
    public enum Mode {Packet, Jump, MiniJump}

    @Override
    public String getMetaData() {
        return "[" + ChatFormatting.GRAY + criticalMode.getValueEnum().toString() + ChatFormatting.RESET + "]";
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if(mc.player.isInWater() || mc.player.isInLava()) return;
        if(mc.player.onGround) {
            if(criticalMode.getValueEnum() == Mode.Packet) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1625, mc.player.posZ, false));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
            }else {
                mc.player.jump();
                if(criticalMode.getValueEnum() == Mode.MiniJump)
                    mc.player.motionY /= 2.0;
            }
            mc.player.onCriticalHit(event.getTarget());
        }
    }
}
