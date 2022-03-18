package me.gerald.hack.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.command.Command;
import me.gerald.hack.util.MessageUtil;
import net.minecraft.block.Block;

import java.io.IOException;

public class Search extends Command {
    public Search() {
        super("Search", "Command for search.", new String[]{"search", "[add/del/list]", "[blockName]"});
    }

    @Override
    public void onCommand(String[] args) throws IOException {
        if(args.length < 2) {
            MessageUtil.sendErrorMessage("Please specify add or delete.");
            return;
        }
        String secondArg = args[1];
        if(args.length < 3 && !secondArg.equalsIgnoreCase("list")) {
            MessageUtil.sendErrorMessage("Please specify the block name.");
            return;
        }
        if(secondArg.equalsIgnoreCase("add")) {
            String blockName = args[2];
            for(Block block : Block.REGISTRY) {
                if(block.getLocalizedName().equalsIgnoreCase(blockName)) {
                    if(GeraldHack.INSTANCE.searchBlocks.contains(block)) {
                        MessageUtil.sendErrorMessage(block.getLocalizedName() + " is already in the search list.");
                        return;
                    }
                    MessageUtil.sendClientMessage("Added " + ChatFormatting.AQUA + block.getLocalizedName() + ChatFormatting.RESET + " to search list.");
                    GeraldHack.INSTANCE.searchBlocks.add(block);
                    return;
                }
            }
        }else if(secondArg.equalsIgnoreCase("del")) {
            String blockName = args[2];
            for(Block block : GeraldHack.INSTANCE.searchBlocks) {
                if(block.getLocalizedName().equalsIgnoreCase(blockName)) {
                    MessageUtil.sendClientMessage("Removing " + ChatFormatting.AQUA + block.getLocalizedName() + ChatFormatting.RESET + " from search list.");
                    GeraldHack.INSTANCE.searchBlocks.remove(block);
                    return;
                }
            }
        }else if(secondArg.equalsIgnoreCase("list")){
            MessageUtil.sendClientMessage(ChatFormatting.GREEN + "SearchBlockList" + ChatFormatting.GRAY + "()" + ChatFormatting.RESET + " {");
            for(Block block : GeraldHack.INSTANCE.searchBlocks) {
                MessageUtil.sendClientMessage("    " + ChatFormatting.GRAY + "[" + block.getLocalizedName() + "]");
            }
            MessageUtil.sendClientMessage("}");
        }else {
            MessageUtil.sendErrorMessage("Second argument can only be add/del/list");
        }
    }
}
