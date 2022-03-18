package me.gerald.hack.gui.ogGUI.oldComps.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.gui.api.HUDComponent;
import me.gerald.hack.module.modules.hud.PotionEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.PotionEffect;

import java.awt.*;

public class PotionEffectComponent extends HUDComponent {
    public PotionEffectComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) {
            updateDragPosition(mouseX, mouseY);
            super.drawScreen(mouseX, mouseY, partialTicks);
            this.width = 100;
            this.height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
            int yOffset = y;
            if(Minecraft.getMinecraft().player.getActivePotionEffects().size() > 0) {
                for (PotionEffect effect : Minecraft.getMinecraft().player.getActivePotionEffects()) {
                    String effectName = I18n.format(effect.getEffectName());
                    int durationTicks = effect.getDuration();
                    int durationSeconds = durationTicks / 20;
                    int durationMinutes = durationSeconds / 60;
                    durationSeconds -= durationMinutes * 60;
                    Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(effectName + " " + getEffectNumber(effect) + ChatFormatting.WHITE + " " + durationMinutes + ":" + (durationSeconds > 9 ? "" : "0") + durationSeconds, x, yOffset, effect.getPotion().getLiquidColor());
                    yOffset += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
                }
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isInside(mouseX, mouseY)) {
            if(mouseButton == 0)
                beginDragging(mouseX, mouseY);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        GeraldHack.INSTANCE.moduleManager.getModule(PotionEffects.class).x.setValue(x);
        GeraldHack.INSTANCE.moduleManager.getModule(PotionEffects.class).y.setValue(y);
        stopDragging();
    }

    @Override
    public void keyTyped(char keyChar, int key) { }

    @Override
    public int getHeight() {
        return height;
    }

    public Color getPotionColor(String effectName) {
        if(effectName.equalsIgnoreCase("weakness")) {
            return new Color(72, 77, 72);
        }else if(effectName.equalsIgnoreCase("strength")) {
            return new Color(147,36,35);
        }else if(effectName.equalsIgnoreCase("speed")) {
            return new Color(124,175,198);
        }else if(effectName.equalsIgnoreCase("fire resistance")) {
            return new Color(228,154,58);
        }else if(effectName.equalsIgnoreCase("resistance")) {
            return new Color(153,69,58);
        }else if(effectName.equalsIgnoreCase("regeneration")) {
            return new Color(205,92,171);
        }else if(effectName.equalsIgnoreCase("water breathing")) {
            return new Color(46,82,153);
        }else if(effectName.equalsIgnoreCase("night vision")) {
            return new Color(31,31,161);
        }else if(effectName.equalsIgnoreCase("jump boost")) {
            return new Color(34,255,76);
        }else if(effectName.equalsIgnoreCase("luck")) {
            return new Color(51,153,0);
        }else if(effectName.equalsIgnoreCase("invisibility")) {
            return new Color(127,131,146);
        }else if(effectName.equalsIgnoreCase("absorption")) {
            return new Color(37,82,165);
        }else if(effectName.equalsIgnoreCase("slowness")) {
            return new Color(90,108,129);
        }
        return new Color(255, 255, 255);
    }

    public String getEffectNumber(PotionEffect effect) {
        switch (effect.getAmplifier()) {
            case 0:
                return "";
            case 1:
                return "II";
            case 2:
                return "III";
            case 3:
                return "IV";
            case 4:
                return "V";
            case 5:
                return "VI";
            case 6:
                return "VII";
            case 7:
                return "VIII";
            case 8:
                return "IX";
            case 9:
                return "X";
        }
        return null;
    }
}
