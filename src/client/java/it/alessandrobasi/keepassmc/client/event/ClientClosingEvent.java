package it.alessandrobasi.keepassmc.client.event;

import it.alessandrobasi.keepassmc.client.keepass.KeepassConnection;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;

public class ClientClosingEvent implements ClientLifecycleEvents.ClientStopping {
    @Override
    public void onClientStopping(MinecraftClient minecraftClient) {
        KeepassConnection.getInstance().close();
    }
}
