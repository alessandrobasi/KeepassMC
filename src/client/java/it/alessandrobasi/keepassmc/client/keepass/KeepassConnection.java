package it.alessandrobasi.keepassmc.client.keepass;

import it.alessandrobasi.keepassmc.client.KeepassmcClient;
import it.alessandrobasi.keepassmc.client.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import org.purejava.KeepassProxyAccess;
import org.purejava.KeepassProxyAccessException;

import java.util.*;

public class KeepassConnection {
    private static KeepassConnection instance = null;
    public static KeepassConnection getInstance() {
        if(!Objects.isNull(instance)) return instance;

        instance = new KeepassConnection();
        return instance;
    }


    private final KeepassProxyAccess kpa = new KeepassProxyAccess();

    // notify the user in case there are any problem with keepass integration
    public void connect() {
        // test is the proxy can connect to keepassXC
        if(!kpa.connect()) {
            // notiy the user that no connection is possibile
            KeepassmcClient.LOGGER.warn("Keepass not open");
            MinecraftClient.getInstance().getToastManager().add(
                new SystemToast(SystemToast.Type.PERIODIC_NOTIFICATION,
                    Text.of("Keepass not started"),
                    Text.of("Please verfy that keepass is open")));
            return;
        }

        // check if id or key are empty
        // if so there is no sync
        if(Objects.equals(Config.HANDLER.instance().KeePassID, "") ||
            Objects.equals(Config.HANDLER.instance().KeePassKey, "")) {
            KeepassmcClient.LOGGER.warn("Keepass not synced");
            MinecraftClient.getInstance().getToastManager().add(
                new SystemToast(SystemToast.Type.PERIODIC_NOTIFICATION,
                    Text.of("Keepass not synced"),
                    Text.of("Please open KeepassMC settings to sync")));
            return;
        }

        // check if connection is possibile
        if(!checkKey()){
            // if connection is not valid notify the user and clear stored keys
            KeepassmcClient.LOGGER.warn("previous keys for keepass were not correct");

            MinecraftClient.getInstance().getToastManager().add(
                new SystemToast(SystemToast.Type.PERIODIC_NOTIFICATION,
                    Text.of("Invalid shared key"),
                    Text.of("Retry a new connection"))
            );

            Config.HANDLER.instance().KeePassID = "";
            Config.HANDLER.instance().KeePassKey = "";
            Config.HANDLER.save();
            return;
        }

        // if it's all ok notify the user
        MinecraftClient.getInstance().getToastManager().add(
            new SystemToast(SystemToast.Type.PERIODIC_NOTIFICATION,
                Text.of("Keepass connected"),
                Text.of("Have a nice game :D")));

    }

    // return true if connection is ok, false otherwise
    private boolean checkKey() {

        // check if id or key are empty
        if(Objects.equals(Config.HANDLER.instance().KeePassID, "") ||
            Objects.equals(Config.HANDLER.instance().KeePassKey, "")) {
            return false;
        }

        // load keys
        List<Map<String, String>> idKeyMap = getKeyMap();

        // check if keys are no longer valid
        if(!kpa.testAssociate(idKeyMap.get(0).get("id"), idKeyMap.get(0).get("key"))) {
            return false;
        }

        // if it's all ok, then save the new public key
        // it's not usefull to save it, since it changes every message
        Config.HANDLER.instance().KeePassKey = kpa.getIdKeyPairPublicKey();
        Config.HANDLER.save();

        return true;

    }

    // return the updated structure
    private List<Map<String, String>> getKeyMap() {

        Config.HANDLER.instance().KeePassKey = kpa.getIdKeyPairPublicKey();
        Config.HANDLER.save();

        // returns valid key
        return List.of(Map.of(
            "id", Config.HANDLER.instance().KeePassID,
            "key", kpa.getIdKeyPairPublicKey()
        ));
    }

    // public function to test if the connection is ok or not
    public boolean isAvailable() {
        return checkKey();
    }

    public void close() {
        Config.HANDLER.instance().KeePassKey = kpa.getIdKeyPairPublicKey();
        Config.HANDLER.save();
        kpa.shutdown();
    }

    // get the password for the current server that the player is currently on
    public Optional<String> getServerPassword() {

        // check if connection is ok
        if(!checkKey()){
            KeepassmcClient.LOGGER.warn("No connection to Keepass");
            MinecraftClient.getInstance().getToastManager().add(
                new SystemToast(SystemToast.Type.PERIODIC_NOTIFICATION,
                    Text.of("No connection to Keepass"),
                    Text.of("Please open KeepassMC settings and perform a sync")));
            return Optional.empty();
        }

        ServerInfo server = MinecraftClient.getInstance().getCurrentServerEntry();

        // check if the user is in a server
        if(Objects.isNull(server)) {
            KeepassmcClient.LOGGER.warn("Not in a multiplayer server");
            MinecraftClient.getInstance().getToastManager().add(
                new SystemToast(SystemToast.Type.PERIODIC_NOTIFICATION,
                    Text.of("Not in a multiplayer server"),
                    Text.of("Please use this function to a multiplayer server")));
            return Optional.empty();
        }

        // get server address (dns/ip)
        String serverAddress = server.address;
        KeepassmcClient.LOGGER.warn(serverAddress);
        //String serverName = "%s %s".formatted(server.name, serverAddress);

        // get login object from keepass
        Map<String, Object> pass = Map.of();
        Integer passcount = 0;

        try {
            pass = kpa.getLogins("minecraft://"+serverAddress, "", false, getKeyMap());
            // get password count
            passcount = Integer.valueOf(pass.get("count").toString());

            KeepassmcClient.LOGGER.warn(pass.get("count").toString());
        }
        catch (Exception e) {
            return Optional.empty();
        }

        /*if(pass.isEmpty()) {
            // create password for the server
            if(!createPassword()) return Optional.empty();

            // re-get passwords
            pass = kpa.getLogins("minecraft://"+serverAddress, "", false, getKeyMap());
        }*/

        if(pass.isEmpty()) {
            return Optional.empty();
        }

        // get password entries
        ArrayList<Map<String, String>> entries = (ArrayList<Map<String, String>>) pass.get("entries");

        // assert all the entries are equal to expected size
        assert passcount == entries.size();

        return Optional.ofNullable(entries.getFirst().get("password"));
    }

    // return true if a new password is generated  and can continue with login
    public boolean createPassword() {

        ServerInfo server = MinecraftClient.getInstance().getCurrentServerEntry();
        // check if the user is in a server
        if(Objects.isNull(server)) {
            KeepassmcClient.LOGGER.warn("Not in a multiplayer server");
            MinecraftClient.getInstance().getToastManager().add(
                new SystemToast(SystemToast.Type.PERIODIC_NOTIFICATION,
                    Text.of("Not in a multiplayer server"),
                    Text.of("Please use this function to a multiplayer server")));
            return false;
        }

        // get server address (dns/ip)
        String serverAddress = server.address;
        String serverName = "%s %s".formatted(server.name, serverAddress);


        // create password
        String newPassword = kpa.generatePassword();

        // user rejected password
        if(Objects.equals(newPassword, "")) {
            return false;
        }

        String playerName = MinecraftClient.getInstance().getGameProfile().name();

        // save in keepass as new entry
        kpa.setLogin("minecraft://"+serverAddress, "", serverName, playerName, newPassword, null, null, null);
        return true;
    }

}
