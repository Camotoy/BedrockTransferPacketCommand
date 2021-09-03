package com.github.camotoy.bedrocktransferpacketcommand.bungee;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.CommandTree;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bungee.BungeeCommandManager;
import cloud.commandframework.bungee.arguments.PlayerArgument;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import com.nukkitx.protocol.bedrock.packet.TransferPacket;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.network.session.GeyserSession;

import java.util.Optional;
import java.util.function.Function;

public class BedrockTransferPacketCommandBungee extends Plugin {


    @Override
    public void onEnable() {
        final Function<CommandTree<CommandSender>, CommandExecutionCoordinator<CommandSender>> executionCoordinatorFunction =
                AsynchronousCommandExecutionCoordinator.<CommandSender>newBuilder().build();

        final Function<CommandSender, CommandSender> mapperFunction = Function.identity();
        BungeeCommandManager<CommandSender> manager;
        try {
            manager = new BungeeCommandManager<>(
                    this,
                    executionCoordinatorFunction,
                    mapperFunction,
                    mapperFunction
            );
        } catch (final Exception e) {
            this.getLogger().severe("Failed to initialize the command manager");
            return;
        }
        manager.command(manager.commandBuilder("transfer", ArgumentDescription.of("Transfer a Bedrock player to a new Bedrock server"))
                .argument(PlayerArgument.of("target"))
                .argument(StringArgument.of("address"))
                .argument(IntegerArgument.<CommandSender>newBuilder("port").withMin(1).withMax(65535).asOptional().build())
                .handler(context -> {
                    final ProxiedPlayer player = context.get("target");
                    GeyserSession session = GeyserConnector.getInstance().getPlayerByUuid(player.getUniqueId());
                    if (session == null) {
                        context.getSender().sendMessage(new TextComponent(ChatColor.RED + "Target must be a Bedrock player!"));
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
