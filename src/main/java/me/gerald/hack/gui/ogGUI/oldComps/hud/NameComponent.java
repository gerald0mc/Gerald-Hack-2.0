package me.gerald.hack.gui.ogGUI.oldComps.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.gui.api.HUDComponent;
import me.gerald.hack.module.modules.client.ClickGui;
import me.gerald.hack.module.modules.hud.Name;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class NameComponent extends HUDComponent {
    public NameComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateDragPosition(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.width = Minecraft.getMinecraft().fontRenderer.getStringWidth("Name: " + Minecraft.getMinecraft().player.getDisplayNameString());
        this.height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Name: " + ChatFormatting.GRAY + Minecraft.getMinecraft().player.getDisplayNameString(), x, y, new Color((int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getR(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getG(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getB()).getRGB());
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
        GeraldHack.INSTANCE.moduleManager.getModule(Name.class).x.setValue(x);
        GeraldHack.INSTANCE.moduleManager.getModule(Name.class).y.setValue(y);
        stopDragging();
    }

    @Override
    public void keyTyped(char keyChar, int key) { }

    @Override
    public int getHeight() {
        return height;
    }
}
