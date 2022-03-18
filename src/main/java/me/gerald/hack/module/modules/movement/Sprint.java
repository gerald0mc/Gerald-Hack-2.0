package me.gerald.hack.module.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.ModeSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", Category.MOVEMENT, "Automatically sprints for you.");
    }

    public ModeSetting mode = register(new ModeSetting("Mode", Mode.Rage));
    public enum Mode {Rage, Legit}

    @Override
    public String getMetaData() {
        return "[" + ChatFormatting.GRAY + mode.getValueEnum() + ChatFormatting.RESET + "]";
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        if(nullCheck()) return;
        if(mode.getValueEnum() == Mode.Rage) {
            if(!mc.player.isSprinting())
                mc.player.setSprinting(true);
        }else if(mode.getValueEnum() == Mode.Legit) {
            if(!mc.player.collidedHorizontally && !mc.player.isSneaking() && mc.player.moveForward != 0 || mc.player.moveStrafing != 0) {
                if (!mc.player.isSprinting())
                    mc.player.setSprinting(true);
            }
        }
    }
}
