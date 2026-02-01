package it.alessandrobasi.keepassmc.mixin.client;

import net.minecraft.client.gui.widget.SimplePositioningWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(SimplePositioningWidget.class)
public interface SimplePositioningWidgetMixin {

    @Accessor("elements")
    List<Object> keepassmc$getElements();

}
