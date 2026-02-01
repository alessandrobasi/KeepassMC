package it.alessandrobasi.keepassmc.client.event;

import it.alessandrobasi.keepassmc.client.KeepassmcClient;
import lombok.Getter;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.realms.util.PeriodicRunnerFactory;
import net.minecraft.server.ServerTask;

import java.util.Timer;
import java.util.TimerTask;

public class ServerJoinEvent implements ClientPlayConnectionEvents.Join {

    @Getter
    private static boolean newlyjoined = false;

    private static final Timer timer = new Timer();

    private static final TimerTask task = new TimerTask() {
        @Override
        public void run() {
            KeepassmcClient.LOGGER.warn("task finished");
            newlyjoined = false;
            timer.cancel(); // stop timer after execution
        }
    };



    @Override
    public void onPlayReady(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {

        KeepassmcClient.LOGGER.warn("new join server");
        newlyjoined = true;
        timer.schedule(task, 8000); // schedule task with delay of 1000ms

    }

    public static void cancel() {
        newlyjoined = false;
        timer.cancel(); // stop timer after execution
    }
}
