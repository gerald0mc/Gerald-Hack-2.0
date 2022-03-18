package me.gerald.hack.gui.ogGUI.oldComps.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.gui.api.HUDComponent;
import me.gerald.hack.module.modules.client.ClickGui;
import me.gerald.hack.module.modules.hud.TextRadar;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.Objects;

public class TextRadarComponent extends HUDComponent {
    public TextRadarComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) {
            updateDragPosition(mouseX, mouseY);
            super.drawScreen(mouseX, mouseY, partialTicks);
            this.width = 75;
            this.height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
            int yOffset = y;
            for (EntityPlayer entity : Minecraft.getMinecraft().world.playerEntities) {
                int ping = entity != null ? Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfo(entity.getUniqueID()).getResponseTime() : 0;
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(ChatFormatting.GRAY + "[" + getHealthColor(entity) + MathHelper.ceil(entity.getHealth() + entity.getAbsorptionAmount()) + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + entity.getDisplayNameString() + ChatFormatting.GRAY + " [" + getPingColor(entity) + MathHelper.ceil(ping) + ChatFormatting.GRAY + "]", x, yOffset, new Color((int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getR(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getG(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getB()).getRGB());
                yOffset += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
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
        GeraldHack.INSTANCE.moduleManager.getModule(TextRadar.class).x.setValue(x);
        GeraldHack.INSTANCE.moduleManager.getModule(TextRadar.class).y.setValue(y);
        stopDragging();
    }

    @Override
    public void keyTyped(char keyChar, int key) { }

    @Override
    public int getHeight() {
        return height;
    }

    public ChatFormatting getHealthColor(EntityPlayer entity) {
        if(entity.getHealth() + entity.getAbsorptionAmount() > 21) {
            return ChatFormatting.GOLD;
        }else if(entity.getHealth() + entity.getAbsorptionAmount() > 14) {
            return ChatFormatting.GREEN;
        }else if(entity.getHealth() + entity.getAbsorptionAmount() > 6){
            return ChatFormatting.YELLOW;
        }else {
            return ChatFormatting.RED;
        }
    }

    public ChatFormatting getPingColor(EntityPlayer entity) {
        if(Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfo(entity.getUniqueID()).getResponseTime() > 150) {
            return ChatFormatting.RED;
        }else if(Minecraft.getMinecraft().getConnection().getPlayerInfo(entity.getUniqueID()).getResponseTime() > 75) {
            return ChatFormatting.YELLOW;
        }else {
            return ChatFormatting.GREEN;
        }
    }
}
