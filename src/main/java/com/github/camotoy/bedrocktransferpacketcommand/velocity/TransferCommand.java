package com.github.camotoy.bedrocktransferpacketcommand.velocity;

import com.nukkitx.protocol.bedrock.packet.TransferPacket;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.RawCommand;
import net.kyori.adventure.text.Component;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.network.session.GeyserSession;

import java.util.NoSuchElementException;
import java.util.UUID;

public class TransferCommand implements RawCommand {

    public void execute(final Invocation invocation, String[] args) {
        CommandSource source = invocation.source();

        if (args.length == 0) {
            source.sendMessage(Component.text( "Wrong usage, /transfer <playername> <ip> <port>"));
            return;
        }
        try {
            UUID uuid = BedrockTransferPacketCommandVelocity.getPlugin().getProxyServer().getPlayer(args[0]).orElseThrow().getUniqueId();
            GeyserSession session = GeyserConnector.getInstance().getPlayerByUuid(uuid);

            if (session == null) {
                source.sendMessage(Component.text("Unable to find player!"));
                return;
            }
            // Always use default port unless args is not null which means ports has been given
            int port = 19132;
            if (!(args[2] == null)){
                // Parse safe to use since we will catch its error.
                port = Integer.parseInt(args[2]);
            }
            TransferPacket packet = new TransferPacket();
            packet.setAddress(args[1]);
            packet.setPort(port);
            session.sendUpstreamPacket(packet);
        } catch (NumberFormatException | NoSuchElementException | IndexOutOfBoundsException e) {
            source.sendMessage(Component.text("Something went wrong use /transfer <playername> <ip> <port>"));
        }
    }
}
