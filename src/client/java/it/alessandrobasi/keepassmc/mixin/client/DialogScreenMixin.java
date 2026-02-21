package it.alessandrobasi.keepassmc.mixin.client;

import net.minecraft.client.gui.screen.dialog.DialogScreen;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.dialog.type.Dialog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DialogScreen.class)
public interface DialogScreenMixin {

    @Accessor("dialog")
    Dialog keepassmc$getDialog();

    @Accessor("layout")
    ThreePartsLayoutWidget keepassmc$getLayout();


}
