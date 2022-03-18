package me.gerald.hack.gui.api;

import me.gerald.hack.gui.HUDEditor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public abstract class HUDComponent extends DragComponent {
    public HUDComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(Minecraft.getMinecraft().currentScreen instanceof HUDEditor) {
            Gui.drawRect(x, y, x + width, y + height, isInside(mouseX, mouseY) ? 0x50ffffff : 0x90000000);
        }
    }
}
