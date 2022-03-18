package me.gerald.hack.module.modules.combat;

import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.util.InventoryUtil;
import me.gerald.hack.util.TimerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CrystalAura extends Module {
    public CrystalAura() {
        super("CrystalAura", Category.COMBAT, "Automatically breaks crsytals in range.");
    }

    public NumSetting range = register(new NumSetting("Range", 4, 1, 6));
    public NumSetting delayMS = register(new NumSetting("DelayMS", 50, 0, 500));
    public BoolSetting antiWeakness = register(new BoolSetting("AntiWeakness", true));

    public TimerUtil timer = new TimerUtil();

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        for(Entity entity : mc.world.loadedEntityList) {
            if(mc.player.getDistance(entity) <= range.getValue()) {
                if (entity instanceof EntityEnderCrystal) {
                    if(timer.passedMs((long) delayMS.getValue())) {
                        int originalSlot = -1;
                        if(antiWeakness.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                            int swordSlot = InventoryUtil.getItemHotbar(Items.DIAMOND_SWORD);
                            if(swordSlot != -1) {
                                originalSlot = mc.player.inventory.currentItem;
                                InventoryUtil.switchToSlot(swordSlot);
                            }
                        }
                        mc.playerController.attackEntity(mc.player, entity);
                        if(originalSlot != -1) {
                            InventoryUtil.switchToSlot(originalSlot);
                        }
                        timer.reset();
                    }
                }
            }
        }
    }
}
