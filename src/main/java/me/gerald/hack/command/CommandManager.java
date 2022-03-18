package me.gerald.hack.command;

import me.gerald.hack.command.commands.*;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    public List<Command> commands;

    public CommandManager() {
        commands = new ArrayList<>();
        commands.add(new Bind());
        commands.add(new Config());
        commands.add(new Copy());
        commands.add(new FakePlayer());
        commands.add(new Friend());
        commands.add(new Help());
        commands.add(new ListModules());
        commands.add(new ListSettings());
        commands.add(new Ping());
        commands.add(new Prefix());
        commands.add(new Search());
        commands.add(new SetSetting());
        commands.add(new Toggle());
    }

    public List<Command> getCommands() {
        return commands;
    }
}
