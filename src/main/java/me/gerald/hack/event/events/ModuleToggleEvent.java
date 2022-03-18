package me.gerald.hack.event.events;

import me.gerald.hack.module.Module;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ModuleToggleEvent extends Event {
    public Module module;

    public ModuleToggleEvent(Module module) {
        this.module = module;
    }

    public static class Pre extends ModuleToggleEvent {
        public Pre(Module module) {
            super(module);
        }

        public Module getModule() {
            return module;
        }
    }

    public static class Post extends ModuleToggleEvent {
        public Post(Module module) {
            super(module);
        }

        public Module getModule() {
            return module;
        }
    }
}
