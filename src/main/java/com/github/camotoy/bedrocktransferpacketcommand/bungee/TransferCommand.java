package com.github.camotoy.bedrocktransferpacketcommand.bungee;

import com.nukkitx.protocol.bedrock.packet.TransferPacket;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.network.session.GeyserSession;

import java.util.Optional;
import java.util.UUID;

public class TransferCommand extends Command {
    public TransferCommand() {
        super("transfer","bedrocktransferpacketcommand.transfer");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(new TextComponent(ChatColor.DARK_RED + "Wrong usage, /transfer <playername> <ip> <port>"));
            return;
        }
        UUID uuid = ProxyServer.getInstance().getPlayer(args[0]).getUniqueId();
        GeyserSession session = GeyserConnector.getInstance().getPlayerByUuid(uuid);

        if (session == null) {
            sender.sendMessage(new TextComponent(ChatColor.DARK_RED + "Unable to find player!"));
            return;
        }
        String ip = args[1];
        Optional<Integer> port = Optional.of(Integer.parseInt(args[2]));
        TransferPacket packet = new TransferPacket();
        packet.setAddress(ip);
        packet.setPort(port.orElse(19132));
        session.sendUpstreamPacket(packet);
    }
}
