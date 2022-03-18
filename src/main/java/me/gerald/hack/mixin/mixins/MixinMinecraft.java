package me.gerald.hack.mixin.mixins;

import me.gerald.hack.GeraldHack;
import me.gerald.hack.module.modules.misc.AntiNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({Minecraft.class})
public class MixinMinecraft {

    @Redirect(method = "dispatchKeypresses", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/GameSettings;setOptionValue(Lnet/minecraft/client/settings/GameSettings$Options;I)V"))
    public void narratorKeyPressRedirect(GameSettings instance, GameSettings.Options settingsOption, int value) {
        if (!GeraldHack.INSTANCE.moduleManager.getModule(AntiNarrator.class).isEnabled()) {
            instance.setOptionValue(settingsOption, value);
        }
    }
}
