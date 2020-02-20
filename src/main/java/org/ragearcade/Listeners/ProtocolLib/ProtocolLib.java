package org.ragearcade.Listeners.ProtocolLib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.ragearcade.Main;

import java.lang.reflect.Field;

public class ProtocolLib {
    public static void client_settings(Main main) {
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(main, PacketType.Play.Client.SETTINGS) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        PacketContainer packet = event.getPacket();
                        for (Field field: packet.getBooleans().getFields()) {
                            try {
                                Byte hatByte = field.getByte(0x40);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
        );
    }
}

