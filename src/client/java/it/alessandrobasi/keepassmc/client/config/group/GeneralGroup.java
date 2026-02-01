package it.alessandrobasi.keepassmc.client.config.group;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import it.alessandrobasi.keepassmc.client.config.Config;
import it.alessandrobasi.keepassmc.client.config.category.CategoryAnnotation;
import it.alessandrobasi.keepassmc.client.config.category.CategorySection;
import it.alessandrobasi.keepassmc.client.keepass.KeepassConnection;
import net.minecraft.text.Text;

@CategoryAnnotation(categoria = CategorySection.General)
@GroupAnnotation(name = "Main settings", order = 0)
public class GeneralGroup extends AbstGroup {

    public GeneralGroup() {
        options.add(LabelOption.create(
            Text.of("KeePass Connection: %s \n"
                .formatted(
                    (KeepassConnection.getInstance().isAvailable()) ? "§aConnected§r": "§cNot connected§r"
                )
            )
        ));

        options.add(ButtonOption.createBuilder()
            .name(Text.of("Connect to KeePass"))
            .description(OptionDescription.of(Text.of("")))
            .action((yaclScreen, thisOption) -> KeepassConnection.getInstance().connect())
            .build()
        );

        options.add(ListOption.<String>createBuilder()
            .name(Text.of("Login commands"))
            .binding(
                Config.HANDLER.defaults().loginCommand,
                () -> Config.HANDLER.instance().loginCommand,
                (z) -> Config.HANDLER.instance().loginCommand = z
            )
            .controller(StringControllerBuilder::create) // usual controllers, passed to every entry
            .initial("") // when adding a new entry to the list, this is the initial value it has
            .build()
        );
    }
}
