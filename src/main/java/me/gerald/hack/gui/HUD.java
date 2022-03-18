package me.gerald.hack.gui;

import me.gerald.hack.GeraldHack;
import me.gerald.hack.module.HUDModule;
import me.gerald.hack.module.Module;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HUD {
    public HUD() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onGameOverlay(RenderGameOverlayEvent.Text event) {
        for(Module module : GeraldHack.INSTANCE.moduleManager.getModulesByCategory(Module.Category.HUD)) {
            if(module.isEnabled()) {
                final HUDModule hudMod = (HUDModule) module;
                hudMod.getComponent().drawScreen(-1, -1, event.getPartialTicks());
            }
        }
    }
}
