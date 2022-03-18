package me.gerald.hack.module.modules.movement;

import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.NumSetting;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class HoleTP extends Module {
    public HoleTP() {
        super("HoleTP", Category.MOVEMENT, "Fuck you.");
    }

    public NumSetting stepPower = register(new NumSetting("StepPower", 9, 0, 10));

    @SubscribeEvent
    public void onUpdate(TickEvent.WorldTickEvent event) {
        if(nullCheck()) return;
        if(mc.player.onGround && isHole(mc.player.getPosition().down())) {
            mc.player.motionY -= stepPower.getValue();
        }
    }

    public boolean isHole(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock() == Blocks.AIR
                && (mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN)
                && mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR
                && mc.world.getBlockState(pos.up().up()).getBlock() == Blocks.AIR
                && (mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.north()).getBlock() == Blocks.OBSIDIAN)
                && (mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.south()).getBlock() == Blocks.OBSIDIAN)
                && (mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.west()).getBlock() == Blocks.OBSIDIAN)
                && (mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.east()).getBlock() == Blocks.OBSIDIAN);
    }
}
