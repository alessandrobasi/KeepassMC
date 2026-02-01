package it.alessandrobasi.keepassmc.client.config.category;

import dev.isxander.yacl3.api.ConfigCategory;
import it.alessandrobasi.keepassmc.client.config.group.AbstGroup;
import it.alessandrobasi.keepassmc.client.config.group.GroupAnnotation;
import it.alessandrobasi.keepassmc.client.util.AnnotationSearch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.text.Text;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum CategorySection {
    General("General", "General settings", 0),

    Not_sorted("Other", "not organized settings",999);



    @Getter
    private final String name;
    @Getter
    private final String tooltip;
    @Getter
    private final int ordinal;


    public ConfigCategory.Builder getCategoryBuilder() {
        return ConfigCategory.createBuilder()
            .name(Text.of(this.getName()))
            .tooltip(Text.of(this.getTooltip()));
    }

    public List<Class<AbstGroup>> getGroups() {

        Set<Class<?>> resultgroup = AnnotationSearch.search(
            CategoryAnnotation.class,
            "it.alessandrobasi.keepassmc.client.config.group"
        );

        return resultgroup.stream()
            .filter(
                group -> group.getAnnotation(CategoryAnnotation.class).categoria().equals(this))
            .map(cl -> (Class<AbstGroup>) cl)
            .sorted(
                Comparator.comparingInt(cl -> cl.getAnnotation(GroupAnnotation.class).order())
            )
            .collect(Collectors.toList());
    }


    @Override
    public String toString() {
        return this.getName();
    }
}
