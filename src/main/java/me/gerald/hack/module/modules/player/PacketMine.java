package me.gerald.hack.module.modules.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.ColorSetting;
import me.gerald.hack.setting.settings.ModeSetting;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.util.InventoryUtil;
import me.gerald.hack.util.NumberUtil;
import me.gerald.hack.util.RenderUtil;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import java.util.Objects;

//#TODO ADD SILENT SWITCH
public class PacketMine extends Module {
    public PacketMine() {
        super("PacketMine", Category.PLAYER, "Will break blocks on click.");
    }

    public BoolSetting pickaxeOnly = register(new BoolSetting("PickaxeOnly", true));
    public ModeSetting renderMode = register(new ModeSetting("RenderMode", RenderMode.Both));
    public enum RenderMode{Fill, Outline, Both}
    public ColorSetting color = register(new ColorSetting("Color", 0, 125, 255, 45));
    public NumSetting lineWidth = register(new NumSetting("LineWidth", 2f, 0f, 4f));

    public BlockPos targetPos = null;
    public NumberUtil timeBreaking = new NumberUtil(0);

    @Override
    public String getMetaData() {
        return timeBreaking.getValue() != 0 ? "[" + ChatFormatting.GRAY + timeBreaking.getValue() + ChatFormatting.RESET + "]" : "";
    }

    @SubscribeEvent
    public void onClick(PlayerInteractEvent.LeftClickBlock event) {
        if(canBreak(event.getPos())) {
            if (pickaxeOnly.getValue() && mc.player.getHeldItemMainhand().getItem() != Items.DIAMOND_PICKAXE) return;
            mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), Objects.requireNonNull(event.getFace())));
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFace()));
            timeBreaking.increase(1);
            targetPos = event.getPos();
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(targetPos != null) {
            AxisAlignedBB box = new AxisAlignedBB(targetPos).offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
            RenderUtil.prepare();
            GL11.glLineWidth(lineWidth.getValue());
            if(renderMode.getValueEnum() == RenderMode.Fill || renderMode.getValueEnum() == RenderMode.Both)
                RenderGlobal.renderFilledBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, color.getR() / 255f, color.getG() / 255f, color.getB() / 255f, color.getA() / 255f);
            if(renderMode.getValueEnum() == RenderMode.Outline || renderMode.getValueEnum() == RenderMode.Both)
                RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, color.getR() / 255f, color.getG() / 255f, color.getB() / 255f, 1f);
            RenderUtil.release();
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        if(targetPos != null && mc.world.getBlockState(targetPos).getBlock() == Blocks.AIR) {
            timeBreaking.zero();
            targetPos = null;
        }
    }

    @Override
    public void onDisable() {
        if(targetPos != null) {
            targetPos = null;
            timeBreaking.zero();
        }
    }

    public boolean canBreak(BlockPos pos) {
        if(mc.world.getBlockState(pos).getBlock() == Blocks.AIR) return false;
        return mc.world.getBlockState(pos).getBlockHardness(mc.world, pos) != -1;
    }
}
