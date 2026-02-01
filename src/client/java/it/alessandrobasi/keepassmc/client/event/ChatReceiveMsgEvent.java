package it.alessandrobasi.keepassmc.client.event;

import it.alessandrobasi.keepassmc.client.KeepassmcClient;
import it.alessandrobasi.keepassmc.client.config.Config;
import it.alessandrobasi.keepassmc.client.keepass.KeepassConnection;
import it.alessandrobasi.keepassmc.client.util.StrArgChecker;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;

import java.util.*;

public class ChatReceiveMsgEvent implements ClientReceiveMessageEvents.Game {


    @Override
    public void onReceiveGameMessage(Text message, boolean overlay) {


        if(!ServerJoinEvent.isNewlyjoined()) return;


        HashSet<String> loginprompt = new HashSet<>(Config.HANDLER.instance().loginCommand);

        Set<String> registerprompt = new HashSet<>(Config.HANDLER.instance().registerCommand);

        String[] msg_array = message.getString().split(" ");

        KeepassmcClient.LOGGER.warn(Arrays.toString(msg_array));

        boolean matchlogin = loginprompt.removeAll(List.of(msg_array));
        boolean matchregister = registerprompt.removeAll(List.of(msg_array));


        if(!matchlogin && !matchregister) return;

        ServerJoinEvent.cancel();

        ClientPlayNetworkHandler handler = MinecraftClient.getInstance().getNetworkHandler();
        assert handler != null;

        if(matchlogin) processLogin(handler);

        if(matchregister) processRegister(handler, msg_array);

    }


    private void processLogin(ClientPlayNetworkHandler handler) {

        Optional<String> pass = KeepassConnection.getInstance().getServerPassword();

        if(pass.isEmpty()) {
            KeepassmcClient.LOGGER.error("KeepassMC was unable to retrive the password");
            MinecraftClient.getInstance().getToastManager().add(
                new SystemToast(SystemToast.Type.PERIODIC_NOTIFICATION,
                    Text.of("KeepassMC Error"),
                    Text.of("KeepassMC was unable to retrive the password")));
            return;
        }

        handler.sendChatCommand("login %s".formatted(pass.get()));


    }

    private void processRegister(ClientPlayNetworkHandler handler, String[] msg_array) {

        Thread threadPasswordPrompt = new Thread(() -> {

            MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().openGameMenu(false));

            if(!KeepassConnection.getInstance().createPassword()) {
                KeepassmcClient.LOGGER.error("KeepassMC was unable to generate the password");
                MinecraftClient.getInstance().getToastManager().add(
                    new SystemToast(SystemToast.Type.PERIODIC_NOTIFICATION,
                        Text.of("KeepassMC Error"),
                        Text.of("KeepassMC was unable to generate the password")));
                return;
            }

            Optional<String> pass = KeepassConnection.getInstance().getServerPassword();

            if(pass.isEmpty()) {
                KeepassmcClient.LOGGER.error("KeepassMC was unable to retrive the password");
                MinecraftClient.getInstance().getToastManager().add(
                    new SystemToast(SystemToast.Type.PERIODIC_NOTIFICATION,
                        Text.of("KeepassMC Error"),
                        Text.of("KeepassMC was unable to retrive the password")));
                return;
            }

            Screen currentscreen = MinecraftClient.getInstance().currentScreen;
            if(currentscreen instanceof GameMenuScreen) {
                MinecraftClient.getInstance().execute(() -> {
                    MinecraftClient.getInstance().setScreen(null);
                });
            }

            List<String> msg_list = List.of(msg_array);

            List<String> regcommand = Config.HANDLER.instance().registerCommand;

            List<Integer> command_indexes = regcommand.stream()
                .map(msg_list::indexOf)
                .filter(i -> i >=0)
                .toList();

            // fallback
            if(regcommand.isEmpty()) {
                handler.sendChatCommand("register %s %s".formatted(pass.get(), pass.get()));
                return;
            }

            int command_index = command_indexes.get(0);

            String param1 = null;
            String param2 = null;

            try {
                param1 = msg_list.get(command_index + 1);
                param2 = msg_list.get(command_index + 2);
            }
            catch (IndexOutOfBoundsException e) {
                handler.sendChatCommand("register %s %s".formatted(pass.get(), pass.get()));
                return;
            }

            if(StrArgChecker.check(param1, param2).equals(StrArgChecker.Result.FIRST_ARG)) {
                handler.sendChatCommand("register %s".formatted(pass.get()));
                return;
            }

            handler.sendChatCommand("register %s %s".formatted(pass.get(), pass.get()));

        });

        threadPasswordPrompt.setDaemon(true);
        threadPasswordPrompt.start();

    }
}
