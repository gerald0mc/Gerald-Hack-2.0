package me.gerald.hack.module.modules.player;

import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.ModeSetting;
import me.gerald.hack.util.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

public class Pearl extends Module {
    public Pearl() {
        super("Pearl", Category.PLAYER, "Throws pearls.");
    }

    public BoolSetting silent = register(new BoolSetting("Silent", true));

    public boolean hasPressed = false;
    public int originalSlot = -1;

    @SubscribeEvent
    public void onInput(InputEvent.MouseInputEvent event) {
        if(Mouse.getEventButtonState() && Mouse.getEventButton() == 2) {
            originalSlot = mc.player.inventory.currentItem;
            hasPressed = true;
        }else if(!Mouse.getEventButtonState()) {
            hasPressed = false;
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        if(nullCheck()) return;
        if(hasPressed) {
            int slot = InventoryUtil.getItemHotbar(Items.ENDER_PEARL);
            if(slot != -1) {
                if(silent.getValue()) {
                    InventoryUtil.silentSwitchToSlot(slot);
                    Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    InventoryUtil.silentSwitchToSlot(originalSlot);
                }else {
                    InventoryUtil.switchToSlot(slot);
                    Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    InventoryUtil.switchToSlot(originalSlot);
                }
            }
        }
    }
}
