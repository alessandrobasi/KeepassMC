package it.alessandrobasi.keepassmc.client.config.group;

import com.google.common.collect.ImmutableList;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstGroup implements OptionGroup {
    List<Option<?>> options = new ArrayList<>();

    @Override
    public Text name() {
        return Text.of(this.getClass().getAnnotation(GroupAnnotation.class).name());
    }

    @Override
    public OptionDescription description() {
        return OptionDescription.of(Text.of(this.getClass().getAnnotation(GroupAnnotation.class).description()));
    }

    @Override
    public Text tooltip() {
        return Text.of(this.getClass().getAnnotation(GroupAnnotation.class).tooltip());
    }

    @Override
    public boolean collapsed() {
        return this.getClass().getAnnotation(GroupAnnotation.class).collapsed();
    }

    @Override
    public boolean isRoot() {
        return this.getClass().getAnnotation(GroupAnnotation.class).isRoot();
    }

    @Override
    public @NotNull ImmutableList<Option<?>> options() {
        return ImmutableList.copyOf(options);
    }

}
