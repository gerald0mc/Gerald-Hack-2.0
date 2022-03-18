package me.gerald.hack.module.modules.combat;

import me.gerald.hack.mixin.mixins.IMinecraft;
import me.gerald.hack.module.Module;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class XPFast extends Module {
    public XPFast() {
        super("XPFast", Category.PLAYER, "Uses stuff fast.");
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        if(nullCheck()) return;
        if(mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE) {
            ((IMinecraft) mc).setRightClickDelayTimerAccessor(0);
        }
    }
}
