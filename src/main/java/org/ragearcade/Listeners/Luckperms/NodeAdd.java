package org.ragearcade.Listeners.Luckperms;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.event.EventBus;
import me.lucko.luckperms.api.event.node.NodeAddEvent;
import net.dv8tion.jda.core.JDA;

public class NodeAdd {

    private static JDA jda;

    public NodeAdd(JDA jda) {
        this.jda = jda;
        LuckPermsApi api = LuckPerms.getApi();
        EventBus eventBus = api.getEventBus();
        eventBus.subscribe(NodeAddEvent.class, e -> {
            if (e.getNode().isGroupNode()) {
                System.out.println(e.getNode().getGroupName());
            }
        });
    }
}
