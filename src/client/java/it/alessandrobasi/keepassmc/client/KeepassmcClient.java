package it.alessandrobasi.keepassmc.client;

import it.alessandrobasi.keepassmc.client.event.AfterInitScreenEvents;
import it.alessandrobasi.keepassmc.client.event.ChatReceiveMsgEvent;
import it.alessandrobasi.keepassmc.client.event.ClientClosingEvent;
import it.alessandrobasi.keepassmc.client.event.ServerJoinEvent;
import it.alessandrobasi.keepassmc.client.keepass.KeepassConnection;
import lombok.Getter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeepassmcClient implements ClientModInitializer {

    @Getter
    private static KeepassmcClient instance;
    private KeepassConnection kpc = KeepassConnection.getInstance();

    public static final String MOD_ID = "keepassmc";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        instance = this;

        ClientLifecycleEvents.CLIENT_STOPPING.register(new ClientClosingEvent());
        ScreenEvents.AFTER_INIT.register(new AfterInitScreenEvents());
        ClientPlayConnectionEvents.JOIN.register(new ServerJoinEvent());
        ClientReceiveMessageEvents.GAME.register(new ChatReceiveMsgEvent());

    }


}
