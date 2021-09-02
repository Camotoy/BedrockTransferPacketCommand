package com.github.camotoy.bedrocktransferpacketcommand.bungee;

import net.md_5.bungee.api.plugin.Plugin;

public class BedrockTransferPacketCommandBungee extends Plugin {


    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerCommand(this, new TransferCommand());

    }
}