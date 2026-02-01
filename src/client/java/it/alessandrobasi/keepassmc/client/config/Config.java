package it.alessandrobasi.keepassmc.client.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import it.alessandrobasi.keepassmc.client.KeepassmcClient;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Config {
    public static ConfigClassHandler<Config> HANDLER = ConfigClassHandler
        .createBuilder(Config.class)
        .id(Identifier.of(KeepassmcClient.MOD_ID, "config"))
        .serializer(config -> GsonConfigSerializerBuilder
            .create(config)
            .setPath(FabricLoader.getInstance().getConfigDir().resolve("keepassmc_config.json5"))
            .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
            .setJson5(true)
            .build()
        )
        .build();

    @SerialEntry
    public List<String> loginCommand = List.of(
        "/login",
        "/l"
    );

    @SerialEntry
    public List<String> registerCommand = List.of(
        "/register",
        "/reg",
        "/r"
    );

    @SerialEntry
    public String KeePassID = "";

    @SerialEntry
    public String KeePassKey = "";

}
