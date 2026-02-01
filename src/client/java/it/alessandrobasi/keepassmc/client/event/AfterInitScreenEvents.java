package it.alessandrobasi.keepassmc.client.event;

import it.alessandrobasi.keepassmc.client.keepass.KeepassConnection;
import it.alessandrobasi.keepassmc.mixin.client.DialogScreenMixin;
import it.alessandrobasi.keepassmc.mixin.client.SimplePositioningWidgetElementMixin;
import it.alessandrobasi.keepassmc.mixin.client.SimplePositioningWidgetMixin;
import it.alessandrobasi.keepassmc.mixin.client.ThreePartsLayoutWidgetMixin;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.dialog.DialogScreen;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.dialog.type.ConfirmationDialog;
import net.minecraft.dialog.type.Dialog;

import java.util.List;
import java.util.Objects;

public class AfterInitScreenEvents implements ScreenEvents.AfterInit {

    private Thread initscreen = null;

    @Override
    public synchronized void afterInit(MinecraftClient client, Screen screen, int scaledWidth, int scaledHeight) {

        // title screen is called 2 times
        if (screen instanceof TitleScreen) {

            if(!Objects.isNull(initscreen)) return;


            initscreen = new Thread(() -> KeepassConnection.getInstance().connect());

            initscreen.start();


        }
        // register/login with Dialog Screen
        /*else if (screen instanceof DialogScreen<?> dialogScreen) {

            // get dialog class
            Dialog dialog_generic = ((DialogScreenMixin) dialogScreen).keepassmc$getDialog();

            // check if is a confirmation dialog with yes/no
            if(!(dialog_generic instanceof ConfirmationDialog)) return;
            //assert dialog_generic instanceof ConfirmationDialog;

            //ConfirmationDialog confirmationDialog = (ConfirmationDialog) dialog_generic;

            ThreePartsLayoutWidget threePartsLayoutWidget = ((DialogScreenMixin) dialog_generic).keepassmc$getLayout();

            SimplePositioningWidget simplePositioningWidget = ((ThreePartsLayoutWidgetMixin) threePartsLayoutWidget).keepassmc$getBody();

            List<Object> simplePositionWidget = ((SimplePositioningWidgetMixin) simplePositioningWidget).keepassmc$getElements();


            Widget login = null;

            simplePositioningWidget.forEachElement(w -> {
                w.
            });


            //List<SimplePositioningWidgetElementMixin> test = simplePositionWidget;





        }*/
    }
}
