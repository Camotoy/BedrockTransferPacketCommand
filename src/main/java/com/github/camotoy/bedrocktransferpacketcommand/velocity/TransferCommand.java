package com.github.camotoy.bedrocktransferpacketcommand.velocity;

import com.nukkitx.protocol.bedrock.packet.TransferPacket;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.RawCommand;
import net.kyori.adventure.text.Component;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.network.session.GeyserSession;

import java.util.NoSuchElementException;
import java.util.Optional;
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
            ;
            GeyserSession session = GeyserConnector.getInstance().getPlayerByUuid(uuid);

            if (session == null) {
                source.sendMessage(Component.text("Unable to find player!"));
                return;
            }
            String ip = args[1];
            Optional<Integer> port = Optional.of(Integer.parseInt(args[2]));
            TransferPacket packet = new TransferPacket();
            packet.setAddress(ip);
            packet.setPort(port.orElse(19132));
            session.sendUpstreamPacket(packet);
        } catch (NumberFormatException | NoSuchElementException e) {
            source.sendMessage(Component.text("Something went wrong while fetching playerdata"));
        }
    }
}
