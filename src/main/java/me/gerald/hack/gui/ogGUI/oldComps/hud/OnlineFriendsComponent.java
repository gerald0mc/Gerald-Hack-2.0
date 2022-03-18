package me.gerald.hack.gui.ogGUI.oldComps.hud;

import me.gerald.hack.GeraldHack;
import me.gerald.hack.gui.api.HUDComponent;
import me.gerald.hack.module.modules.client.ClickGui;
import me.gerald.hack.module.modules.hud.OnlineFriends;
import me.gerald.hack.util.friend.Friend;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;

public class OnlineFriendsComponent extends HUDComponent {
    public OnlineFriendsComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateDragPosition(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.width = 75;
        this.height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        int yOffset = y + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Friends:", x, y, new Color((int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getR(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getG(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getB()).getRGB());
        if(Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null) {
            for(EntityPlayer entity : Minecraft.getMinecraft().world.playerEntities) {
                for(Friend friend : GeraldHack.INSTANCE.friendManager.getFriends()) {
                    if(entity.getDisplayNameString().equalsIgnoreCase(friend.getName())) {
                        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(friend.getName(), x, yOffset, -1);
                        yOffset += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
                    }
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
        GeraldHack.INSTANCE.moduleManager.getModule(OnlineFriends.class).x.setValue(x);
        GeraldHack.INSTANCE.moduleManager.getModule(OnlineFriends.class).y.setValue(y);
        stopDragging();
    }

    @Override
    public void keyTyped(char keyChar, int key) { }

    @Override
    public int getHeight() {
        return height;
    }
}
