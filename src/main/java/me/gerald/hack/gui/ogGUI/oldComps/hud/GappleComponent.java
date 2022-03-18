package me.gerald.hack.gui.ogGUI.oldComps.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.gui.api.HUDComponent;
import me.gerald.hack.module.modules.client.ClickGui;
import me.gerald.hack.module.modules.hud.GappleCount;
import me.gerald.hack.util.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class GappleComponent extends HUDComponent {
    public GappleComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateDragPosition(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.width = Minecraft.getMinecraft().fontRenderer.getStringWidth("Gapples: " + InventoryUtil.getTotalAmountOfItem(Items.GOLDEN_APPLE) + (GeraldHack.INSTANCE.moduleManager.getModule(GappleCount.class).stackCount.getValue() ? " [" + (InventoryUtil.getTotalAmountOfItem(Items.GOLDEN_APPLE) / 64f) + "]" : ""));
        this.height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Gapples: " + ChatFormatting.GRAY + InventoryUtil.getTotalAmountOfItem(Items.GOLDEN_APPLE) + (GeraldHack.INSTANCE.moduleManager.getModule(GappleCount.class).stackCount.getValue() ? ChatFormatting.RESET + " [" + ChatFormatting.GRAY + (InventoryUtil.getTotalAmountOfItem(Items.END_CRYSTAL) / 64f >= 32f ? MathHelper.ceil((InventoryUtil.getTotalAmountOfItem(Items.GOLDEN_APPLE) / 64f)) : MathHelper.floor((InventoryUtil.getTotalAmountOfItem(Items.GOLDEN_APPLE) / 64f))) + ChatFormatting.RESET + "]" : ""), x, y, new Color((int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getR(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getG(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getB()).getRGB());
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
        GeraldHack.INSTANCE.moduleManager.getModule(GappleCount.class).x.setValue(x);
        GeraldHack.INSTANCE.moduleManager.getModule(GappleCount.class).y.setValue(y);
        stopDragging();
    }

    @Override
    public void keyTyped(char keyChar, int key) { }

    @Override
    public int getHeight() {
        return height;
    }
}
