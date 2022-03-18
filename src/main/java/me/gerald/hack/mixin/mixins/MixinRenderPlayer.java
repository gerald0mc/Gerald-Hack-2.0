package me.gerald.hack.mixin.mixins;

import me.gerald.hack.GeraldHack;
import me.gerald.hack.module.modules.render.Nametags;
import me.gerald.hack.module.modules.render.PopChams;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({RenderPlayer.class})
public class MixinRenderPlayer {

    @Inject(method = "renderEntityName", at = @At("HEAD"), cancellable = true)
    public void renderEntityNameHook(AbstractClientPlayer entityIn, double x, double y, double z, String name, double distanceSq, CallbackInfo info) {
        if(GeraldHack.INSTANCE.moduleManager.getModule(Nametags.class).isEnabled() || GeraldHack.INSTANCE.moduleManager.getModule(PopChams.class).isEnabled()) {
            info.cancel();
        }
    }
}
