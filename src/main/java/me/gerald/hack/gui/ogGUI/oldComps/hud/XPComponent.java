package me.gerald.hack.gui.ogGUI.oldComps.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.gui.api.HUDComponent;
import me.gerald.hack.module.modules.client.ClickGui;
import me.gerald.hack.module.modules.hud.XPCount;
import me.gerald.hack.util.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class XPComponent extends HUDComponent {
    public XPComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateDragPosition(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.width = Minecraft.getMinecraft().fontRenderer.getStringWidth("XP: " + InventoryUtil.getTotalAmountOfItem(Items.EXPERIENCE_BOTTLE) + (GeraldHack.INSTANCE.moduleManager.getModule(XPCount.class).stackCount.getValue() ? " [" + (InventoryUtil.getTotalAmountOfItem(Items.EXPERIENCE_BOTTLE) / 64f) + "]" : ""));
        this.height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("XP: " + ChatFormatting.GRAY + InventoryUtil.getTotalAmountOfItem(Items.EXPERIENCE_BOTTLE) + (GeraldHack.INSTANCE.moduleManager.getModule(XPCount.class).stackCount.getValue() ? ChatFormatting.RESET + " [" + ChatFormatting.GRAY + (InventoryUtil.getTotalAmountOfItem(Items.END_CRYSTAL) / 64f >= 32f ? MathHelper.ceil((InventoryUtil.getTotalAmountOfItem(Items.EXPERIENCE_BOTTLE) / 64f)) : MathHelper.floor((InventoryUtil.getTotalAmountOfItem(Items.EXPERIENCE_BOTTLE) / 64f))) + ChatFormatting.RESET + "]" : ""), x, y, new Color((int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getR(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getG(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getB()).getRGB());
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
        GeraldHack.INSTANCE.moduleManager.getModule(XPCount.class).x.setValue(x);
        GeraldHack.INSTANCE.moduleManager.getModule(XPCount.class).y.setValue(y);
        stopDragging();
    }

    @Override
    public void keyTyped(char keyChar, int key) { }

    @Override
    public int getHeight() {
        return height;
    }
}
