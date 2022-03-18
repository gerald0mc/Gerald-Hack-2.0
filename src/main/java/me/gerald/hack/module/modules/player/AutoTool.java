package me.gerald.hack.module.modules.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.module.Module;
import me.gerald.hack.util.InventoryUtil;
import me.gerald.hack.util.TimerUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoTool extends Module {
    public AutoTool() {
        super("AutoTool", Category.PLAYER, "Automatically puts the correct tool in your hand.");
    }

    String currentItem = null;
    public TimerUtil timer = new TimerUtil();

    @Override
    public String getMetaData() {
        return currentItem != null ? "[" + ChatFormatting.GRAY + currentItem + ChatFormatting.RESET + "]" : "";
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent.LeftClickBlock event) {
        for (int i = 0; i < 9; ++i) {
            int bestSlot = -1;
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                float speed = stack.getDestroySpeed(mc.world.getBlockState(event.getPos()));
                if (speed > 1f) {
                    int eff;
                    speed += (((eff = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack)) > 0) ? (Math.pow(eff, 2.0) + 1.0) : 0.0);
                    if (speed > 0.0) {
                        bestSlot = i;
                    }
                }
            }
            if (bestSlot != -1) {
                if(mc.player.inventory.currentItem != bestSlot)
                    InventoryUtil.switchToSlot(bestSlot);
                if(mc.player.inventory.getStackInSlot(bestSlot).getItem() instanceof ItemSword)
                    currentItem = "Sword";
                else if(mc.player.inventory.getStackInSlot(bestSlot).getItem() instanceof ItemPickaxe)
                    currentItem = "Pickaxe";
                else if(mc.player.inventory.getStackInSlot(bestSlot).getItem() instanceof ItemAxe)
                    currentItem = "Axe";
                else if(mc.player.inventory.getStackInSlot(bestSlot).getItem() instanceof ItemHoe)
                    currentItem = "Hoe";
                else if(mc.player.inventory.getStackInSlot(bestSlot).getItem() instanceof ItemSpade)
                    currentItem = "Shovel";
                timer.reset();
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        if(timer.passedMs(5000) && currentItem != null) {
            currentItem = null;
        }
    }

    @Override
    public void onDisable() {
        if(currentItem != null)
            currentItem = null;
    }
}
