package net.dxzzz.noadvancementbroadcast;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

public final class NoAdvancementBroadcast extends JavaPlugin {

    @Override
    public void onEnable() {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();

        manager.addPacketListener(new PacketAdapter(this, PacketType.Play.Server.CHAT) {
            private boolean errorLogged = false;

            @Override
            public void onPacketSending(PacketEvent event) {
                try {
                    if (event.isCancelled()) return;

                    if (event.getPacket().getChatComponents().size() > 0) {
                        WrappedChatComponent component = event.getPacket().getChatComponents().read(0);

                        if (component != null && component.getJson() != null) {
                            String json = component.getJson().toLowerCase(Locale.ROOT);

                            if (
                                    json.contains("has made the advancement") ||
                                            json.contains("has reached the goal") ||
                                            json.contains("completed the challenge") ||
                                            json.contains("获得了进度") ||
                                            json.contains("达成了目标") ||
                                            json.contains("完成了挑战")
                            ) {
                                event.setCancelled(true);
                            }
                        }
                    }
                } catch (Exception e) {
                    if (!errorLogged) {
                        getLogger().warning("拦截 advancement 消息时出错:");
                        e.printStackTrace();
                        errorLogged = true;
                    }
                }
            }
        });

        getLogger().info("§aNoAdvancementBroadcast 插件已启用");
        getLogger().info("§b作者: Wyuu101");
    }

    @Override
    public void onDisable() {
    }
}
