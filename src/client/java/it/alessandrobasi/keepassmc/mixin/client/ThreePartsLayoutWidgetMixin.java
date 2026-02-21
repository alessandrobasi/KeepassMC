package it.alessandrobasi.keepassmc.mixin.client;

import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ThreePartsLayoutWidget.class)
public interface ThreePartsLayoutWidgetMixin {


    @Accessor("header")
    SimplePositioningWidget keepassmc$getHeader();

    @Accessor("body")
    SimplePositioningWidget keepassmc$getBody();

    @Accessor("footer")
    SimplePositioningWidget keepassmc$getFooter();


}
