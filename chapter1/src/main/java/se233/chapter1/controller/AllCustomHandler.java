package se233.chapter1.controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import se233.chapter1.Launcher;
import se233.chapter1.model.character.BasedCharacter;
import se233.chapter1.model.item.Armor;
import se233.chapter1.model.item.BasedEquipment;
import se233.chapter1.model.item.Weapon;

import java.util.ArrayList;

public class AllCustomHandler {
    public static class GenCharacterHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            BasedCharacter currentChar = Launcher.getMainCharacter();

            // Unequip from character and return to inventory
            if (currentChar != null) {
                if (Launcher.getEquippedWeapon() != null) {
                    currentChar.equipWeapon(null);
                    Launcher.getAllEquipments().add(Launcher.getEquippedWeapon());
                    Launcher.setEquippedWeapon(null);
                }

                if (Launcher.getEquippedArmor() != null) {
                    currentChar.equipArmor(null);
                    Launcher.getAllEquipments().add(Launcher.getEquippedArmor());
                    Launcher.setEquippedArmor(null);
                }
            }

            // Generate new character
            Launcher.setMainCharacter(GenCharacter.setUpCharacter());

            // Refresh UI
            Launcher.refreshPane();
        }
    }

    public static void onDragDetected(MouseEvent event, BasedEquipment equipment, ImageView imgView) {
//        ArrayList<BasedEquipment> allEquipments = Launcher.getAllEquipments();
//        allEquipments.removeIf(eq -> eq.getName().equals(equipment.getName()));
//        Launcher.setAllEquipments(allEquipments);
//        Launcher.refreshPane();

        Dragboard db = imgView.startDragAndDrop(TransferMode.ANY);
        db.setDragView(imgView.getImage());

        ClipboardContent content = new ClipboardContent();
        content.put(equipment.DATA_FORMAT, equipment);
        db.setContent(content);
        event.consume();
    }
    public static void onDragOver(DragEvent event, String type){
        Dragboard dragboard = event.getDragboard();
        BasedEquipment retrievedEquipment = (BasedEquipment)dragboard.getContent(BasedEquipment.DATA_FORMAT);
        if (dragboard.hasContent(BasedEquipment.DATA_FORMAT) && retrievedEquipment.getClass().getSimpleName().equals(type))
            event.acceptTransferModes(TransferMode.MOVE);
    }
    public static void onDragDropped(DragEvent event, Label lbl, StackPane imgGroup){
        boolean dragCompleted = false;
        Dragboard dragboard = event.getDragboard();
        ArrayList<BasedEquipment> allEquipments = Launcher.getAllEquipments();
        if(dragboard.hasContent(BasedEquipment.DATA_FORMAT)) {
            BasedEquipment retrievedEquipment = (BasedEquipment)dragboard.getContent(BasedEquipment.DATA_FORMAT);
            BasedCharacter character = Launcher.getMainCharacter();
            if(retrievedEquipment.getClass().getSimpleName().equals("Weapon")) {
                if (Launcher.getEquippedWeapon() != null)
                    allEquipments.add(Launcher.getEquippedWeapon());
                Launcher.setEquippedWeapon((Weapon) retrievedEquipment);
                character.equipWeapon((Weapon)  retrievedEquipment);
            } else {
                if (Launcher.getEquippedArmor()!=null)
                    allEquipments.add(Launcher.getEquippedArmor());
                Launcher.setEquippedArmor((Armor) retrievedEquipment);
                character.equipArmor((Armor)  retrievedEquipment);
            }
            Launcher.setMainCharacter(character);
            Launcher.setAllEquipments(allEquipments);
            Launcher.refreshPane();
            ImageView imgView = new ImageView();
            if (imgGroup.getChildren().size()!=1) {
                imgGroup.getChildren().remove(1);
                Launcher.refreshPane();
            }
            lbl.setText(retrievedEquipment.getClass().getSimpleName() + ":\n" + retrievedEquipment.getName());
            imgView.setImage(new Image(Launcher.class.getResource(retrievedEquipment.getImagepath()).toString()));
            imgGroup.getChildren().add(imgView);
            dragCompleted = true;
        }
        event.setDropCompleted(dragCompleted);
    }
    public static void onEquipDone(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        BasedEquipment retrievedEquipment = (BasedEquipment) dragboard.getContent(BasedEquipment.DATA_FORMAT);
        ArrayList<BasedEquipment> allEquipments = Launcher.getAllEquipments();
        BasedCharacter character = Launcher.getMainCharacter();

        if (event.getTransferMode() == null) {
            // Drop failed → return to inventory and unequip
            if (retrievedEquipment instanceof Weapon) {
                if (Launcher.getEquippedWeapon() == retrievedEquipment) {
                    character.equipWeapon(null);
                    Launcher.setEquippedWeapon(null);
                }
            } else if (retrievedEquipment instanceof Armor) {
                if (Launcher.getEquippedArmor() == retrievedEquipment) {
                    character.equipArmor(null);
                    Launcher.setEquippedArmor(null);
                }
            }

            // Avoid duplicates
            if (!allEquipments.contains(retrievedEquipment)) {
                allEquipments.add(retrievedEquipment);
            }

        } else {
            // Successful drop → remove from inventory
            allEquipments.removeIf(eq -> eq == retrievedEquipment || eq.getName().equals(retrievedEquipment.getName()));
        }

        Launcher.setMainCharacter(character);
        Launcher.setAllEquipments(allEquipments);
        Launcher.refreshPane();
    }


}
