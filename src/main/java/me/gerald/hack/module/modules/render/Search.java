package me.gerald.hack.module.modules.render;

import me.gerald.hack.GeraldHack;
import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.ColorSetting;
import me.gerald.hack.setting.settings.ModeSetting;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.util.MessageUtil;
import me.gerald.hack.util.RenderUtil;
import me.gerald.hack.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class Search extends Module {
    public Search() {
        super("Search", Category.RENDER, "Thing.");
    }

    public ModeSetting renderMode = register(new ModeSetting("RenderMode", RenderMode.Both));
    public enum RenderMode{Fill, Outline, Both}
    public ColorSetting color = register(new ColorSetting("Color", 0, 125, 255, 43));
    public NumSetting lineWidth = register(new NumSetting("LineWidth", 2f, 0f, 4f));

    public List<BlockPos> blockList = new ArrayList<>();

    @SubscribeEvent
    public void onUpdate(TickEvent.WorldTickEvent event) {
        if(nullCheck()) return;

    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(nullCheck()) return;
        if(GeraldHack.INSTANCE.searchBlocks.isEmpty()) return;
        for(BlockPos pos : WorldUtil.getSphere(mc.player.getPosition(), 100, false)) {
            for(Block block : GeraldHack.INSTANCE.searchBlocks) {
                if(mc.world.getBlockState(pos).getBlock() == block) {
                    if(!blockList.contains(pos)) {
                        blockList.add(pos);
                        MessageUtil.sendClientMessage("Found " + block.getLocalizedName() + " at [X: " + pos.getX() + " Y: " + pos.getY() + " Z: " + pos.getZ() + "]");
                    }
                    AxisAlignedBB box = mc.world.getBlockState(pos).getSelectedBoundingBox(mc.world, pos).offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
                    RenderUtil.prepare();
                    GL11.glLineWidth(lineWidth.getValue());
                    if(renderMode.getValueEnum() == RenderMode.Fill || renderMode.getValueEnum() == RenderMode.Both)
                        RenderGlobal.renderFilledBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, color.getR() / 255f, color.getG() / 255f, color.getB() / 255f, color.getA() / 255f);
                    if(renderMode.getValueEnum() == RenderMode.Outline || renderMode.getValueEnum() == RenderMode.Both)
                        RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, color.getR() / 255f, color.getG() / 255f, color.getB() / 255f, 1f);
                    RenderUtil.release();
                }
            }
        }
    }
}
