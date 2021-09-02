package com.github.camotoy.bedrocktransferpacketcommand.bungee;

import com.nukkitx.protocol.bedrock.packet.TransferPacket;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.network.session.GeyserSession;

import java.util.NoSuchElementException;
import java.util.UUID;

public class TransferCommand extends Command {
    public TransferCommand() {
        super("transfer", "bedrocktransferpacketcommand.transfer");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(new TextComponent(ChatColor.DARK_RED + "Wrong usage, /transfer <playername> <ip> <port>"));
            return;
        }
        try {
            UUID uuid = ProxyServer.getInstance().getPlayer(args[0]).getUniqueId();
            GeyserSession session = GeyserConnector.getInstance().getPlayerByUuid(uuid);

            if (session == null) {
                sender.sendMessage(new TextComponent(ChatColor.DARK_RED + "Unable to find player!"));
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
            sender.sendMessage((new TextComponent(ChatColor.DARK_RED + "Wrong usage, /transfer <playername> <ip> <port>")));
        }
    }
}