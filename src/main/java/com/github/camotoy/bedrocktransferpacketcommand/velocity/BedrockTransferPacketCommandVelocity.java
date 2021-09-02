package com.github.camotoy.bedrocktransferpacketcommand.velocity;


import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

@Plugin(id = "bedrocktransferpacketcommand", name = "BedrockTransferPacketCommand", version = "1.0.1", description = "Transfer a Bedrock player to a new Bedrock server..", authors = {"Camotoy"},
        dependencies = {@Dependency(id = "geyser")})

public class BedrockTransferPacketCommandVelocity {

    private static BedrockTransferPacketCommandVelocity plugin;
    private final ProxyServer server;

    @Inject
    public BedrockTransferPacketCommandVelocity(ProxyServer server) {
        BedrockTransferPacketCommandVelocity.plugin = this;
        this.server = server;
    }
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getCommandManager().register("transfer", new TransferCommand());
    }
    public static BedrockTransferPacketCommandVelocity getPlugin() {
        return plugin;
    }
    public ProxyServer getProxyServer() {
        return server;
    }

}
