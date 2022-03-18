package me.gerald.hack.command;

import me.gerald.hack.GeraldHack;
import me.gerald.hack.module.modules.client.Client;

import java.io.IOException;

public class Command {
    private String name, description;
    private String[] usage;

    public Command(String name, String description, String[] usage) {
        this.name = name;
        this.description = description;
        this.usage = usage;
    }

    public void onCommand(String[] args) throws IOException {}

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getUsage() {
        return usage;
    }

    public String getPrefix() {
        return GeraldHack.INSTANCE.moduleManager.getModule(Client.class).prefix.getString();
    }
}
