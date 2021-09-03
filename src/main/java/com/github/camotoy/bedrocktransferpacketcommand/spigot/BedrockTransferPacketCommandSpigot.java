package com.github.camotoy.bedrocktransferpacketcommand.spigot;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.bukkit.arguments.selector.SinglePlayerSelector;
import cloud.commandframework.bukkit.parsers.selector.SinglePlayerSelectorArgument;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import com.nukkitx.protocol.bedrock.packet.TransferPacket;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.network.session.GeyserSession;

import java.util.Optional;
import java.util.function.Function;

public final class BedrockTransferPacketCommandSpigot extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        BukkitCommandManager<CommandSender> commandManager;
        try {
            commandManager = new BukkitCommandManager<>(this, CommandExecutionCoordinator.simpleCoordinator(), Function.identity(), Function.identity());
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        commandManager.registerBrigadier();

        commandManager.command(commandManager.commandBuilder("transfer", ArgumentDescription.of("Transfer a Bedrock player to a new Bedrock server"))
        .argument(SinglePlayerSelectorArgument.of("target"))
        .argument(StringArgument.of("address"))
        .argument(IntegerArgument.<CommandSender>newBuilder("port").withMin(1).withMax(65535).asOptional().build())
        .handler(context -> {
            SinglePlayerSelector playerArgument = context.get("target");
            if (playerArgument.getPlayer() == null) {
                context.getSender().sendMessage("Player not found!");
                return;
            }
            GeyserSession session = GeyserConnector.getInstance().getPlayerByUuid(playerArgument.getPlayer().getUniqueId());
            if (session == null) {
                context.getSender().sendMessage("Target must be a Bedrock player!");
                return;
            }

            Optional<Integer> port = context.getOptional("port");
            TransferPacket packet = new TransferPacket();
            packet.setAddress(context.get("address"));
            packet.setPort(port.orElse(19132));
            session.sendUpstreamPacket(packet);
        }));
    }
}
