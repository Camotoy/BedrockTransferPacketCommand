package com.github.camotoy.bedrocktransferpacketcommand.velocity;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.velocity.CloudInjectionModule;
import cloud.commandframework.velocity.VelocityCommandManager;
import cloud.commandframework.velocity.arguments.PlayerArgument;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.nukkitx.protocol.bedrock.packet.TransferPacket;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.network.session.GeyserSession;

import java.util.Optional;
import java.util.function.Function;

@Plugin(id = "bedrocktransferpacketcommand", name = "BedrockTransferPacketCommand", version = "1.0.1", description = "Transfer a Bedrock player to a new Bedrock server..", authors = {"Camotoy"},
        dependencies = {@Dependency(id = "geyser")})

public final class BedrockTransferPacketCommandVelocity {

    @Inject
    public Injector injector;

    @Subscribe
    public void onProxyInitialization(final @NonNull ProxyInitializeEvent event) {
        final Injector childInjector = this.injector.createChildInjector(
                new CloudInjectionModule<>(
                        CommandSource.class,
                        CommandExecutionCoordinator.simpleCoordinator(),
                        Function.identity(),
                        Function.identity()
                )
        );
        final VelocityCommandManager<CommandSource> commandManager = childInjector.getInstance(
                Key.get(new TypeLiteral<VelocityCommandManager<CommandSource>>() {
                })
        );

        commandManager.command(commandManager.commandBuilder("transfer", ArgumentDescription.of("Transfer a Bedrock player to a new Bedrock server"))
                .argument(PlayerArgument.of("target"))
                .argument(StringArgument.of("address"))
                .argument(IntegerArgument.<CommandSource>newBuilder("port").withMin(1).withMax(65535).asOptional().build())
                .handler(context -> {
                    final Player player = context.get("target");
                    GeyserSession session = GeyserConnector.getInstance().getPlayerByUuid(player.getUniqueId());
                    if (session == null) {
                        context.getSender().sendMessage(Component.text("Target must be a Bedrock player!"));
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
