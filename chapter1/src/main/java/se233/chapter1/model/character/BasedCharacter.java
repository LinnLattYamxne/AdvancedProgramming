package se233.chapter1.model.character;

import se233.chapter1.Launcher;
import se233.chapter1.model.DamageType;
import se233.chapter1.model.item.Armor;
import se233.chapter1.model.item.Weapon;

public abstract class BasedCharacter {
    protected String name, imgpath;
    protected DamageType type;
    protected Integer fullHp, basedPow, basedDef, basedRes;
    protected Integer hp, power, defense,resistance;
    protected Weapon weapon;
    protected Armor armor;
    public String getName() { return name; }
    public String getImagepath() { return imgpath; }
    public Integer getHp() { return hp; }
    public Integer getFullHp() { return fullHp; }
    public Integer getPower() { return power; }
    public Integer getDefense() { return defense; }
    public Integer getResistance() { return resistance; }

    // Inside BasedCharacter.java
    public void equipWeapon(Weapon weapon) {
        if (weapon == null) {
            this.weapon = null;
            this.power = this.basedPow;
            return;
        }

        // BattleMage can equip anything
        if (this instanceof BattleMageCharacter || this.type == DamageType.both || this.type == weapon.getDamageType()) {
            this.weapon = weapon;
            this.power = this.basedPow + weapon.getPower();
        } else {
            // Incompatible type: return to inventory
            // Put weapon back in inventory
            if (Launcher.getAllEquipments() != null) {
                Launcher.getAllEquipments().add(weapon);
            }

            // Make sure the character doesn’t hold the weapon
            this.weapon = null;
            this.power = this.basedPow;

            // Optional: call UI update
            Launcher.setEquippedWeapon(null);
            Launcher.refreshPane();
        }
    }


    public void equipArmor(Armor armor) {
        if (armor == null) {
            this.armor = null;
            this.defense = this.basedDef;
            this.resistance = this.basedRes;
            return;
        }

        if (this instanceof BattleMageCharacter) {
            Launcher.getAllEquipments().add(armor);
            return;
        }

        this.armor = armor;
        this.defense = this.basedDef + armor.getDefense();
        this.resistance = this.basedRes + armor.getResistance();

        Launcher.setEquippedArmor(armor);
        Launcher.refreshPane();
    }


    @Override
    public String toString() { return name; }
    public DamageType getType() {
        return type;
    }

    public void unequipWeapon () {
        this.weapon = null;
    }
    public void unequipArmor () {
        this.armor = null;
    }
}
