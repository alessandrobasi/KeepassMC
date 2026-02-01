package it.alessandrobasi.keepassmc.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import it.alessandrobasi.keepassmc.client.config.category.CategorySection;
import it.alessandrobasi.keepassmc.client.config.group.AbstGroup;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {

        // Crea il primo config
        if(!Config.HANDLER.load())
            Config.HANDLER.save();


        List<CategorySection> categories = Arrays.stream(CategorySection.values())
            .sorted(Comparator.comparingInt(CategorySection::getOrdinal))
            .toList();

        YetAnotherConfigLib.Builder builder = YetAnotherConfigLib.createBuilder()
            .title(Text.literal("KeepassMC Settings"))
            .save(Config.HANDLER::save);

        // iterate through Enum category
        for (CategorySection category : categories) {

            // create a category builder
            ConfigCategory.Builder catbuilder = category.getCategoryBuilder();

            // get group options
            List<Class<AbstGroup>> orderedGroup = category.getGroups();

            // for each ordered group create the instance and insert it in the current category
            for (Class<AbstGroup> aClass : orderedGroup) {

                try {
                    catbuilder.group(aClass.newInstance());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            // add the category in the main builder
            builder.category(catbuilder.build());
        }


        return parentScreen -> builder.build().generateScreen(parentScreen);
    }
}
