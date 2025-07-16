package se233.chapter1.model.character;

import se233.chapter1.Launcher;
import se233.chapter1.model.item.Armor;

public class BattleMageCharacter extends BasedCharacter{
    public BattleMageCharacter(String name, String imgpath, int basedDef, int basedRes){
        this.name = name;
        this.type = null;
        this.imgpath = imgpath;
        this.fullHp = 40;
        this.basedPow = 40;
        this.basedDef = basedDef;
        this.basedRes = basedRes;
        this.hp = this.fullHp;
        this.power = this.basedPow;
        this.defense = basedDef;
        this.resistance = basedRes;
    }


    @Override
    public void equipArmor(Armor armor) {
        System.out.println("BattleMage cannot equip armor!");
        if (armor != null) {
            Launcher.getAllEquipments().add(armor); // Send it back to inventory
            Launcher.setEquippedArmor(null);
            Launcher.refreshPane(); // Make sure it shows up
        }
    }
}
