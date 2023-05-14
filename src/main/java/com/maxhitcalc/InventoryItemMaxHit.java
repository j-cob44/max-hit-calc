package com.maxhitcalc;

import net.runelite.api.*;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.game.ItemManager;
public class InventoryItemMaxHit{
    public static AttackStyle determineAttackStyle(Client client, Item weapon, int weaponID){
        AttackStyle attackStyle = null;

        // Ranged
        if (client.getItemDefinition(weaponID).getName().contains("bow")
                || client.getItemDefinition(weaponID).getName().contains("Bow")
                || client.getItemDefinition(weaponID).getName().contains("chinchompa")
                || client.getItemDefinition(weaponID).getName().contains("Chinchompa")
                || client.getItemDefinition(weaponID).getName().contains("ballista")
                || client.getItemDefinition(weaponID).getName().contains("dart")
                || client.getItemDefinition(weaponID).getName().contains("knife")
                || client.getItemDefinition(weaponID).getName().contains("thrownaxe")
                || client.getItemDefinition(weaponID).getName().contains("Toktz-xil-ul"))
        {
            attackStyle = AttackStyle.RANGING;
        }
        // Magic
        else if (client.getItemDefinition(weaponID).getName().contains("sceptre")
                || client.getItemDefinition(weaponID).getName().contains("staff")
                || client.getItemDefinition(weaponID).getName().contains("Trident")
                || client.getItemDefinition(weaponID).getName().contains("Tumeken's Shadow")
                || client.getItemDefinition(weaponID).getName().contains("Staff")
                || client.getItemDefinition(weaponID).getName().contains("wand")
                || client.getItemDefinition(weaponID).getName().contains("crozier")
                || client.getItemDefinition(weaponID).getName().contains("Void knight mace"))
        {

            attackStyle = AttackStyle.CASTING;
        }
        else {
            // Assume Melee
            attackStyle = AttackStyle.AGGRESSIVE;
        }

        return attackStyle;
    }
    public static Item[] changeEquipment(int slotID, int itemID, Item[] currentEquipment)
    {
        Item[] newEquipment = currentEquipment;

        newEquipment[slotID] = new Item(itemID, 1);

        return newEquipment;
    }

    public static double calculateMeleeMaxHit(Client client, ItemManager itemManager, Item[] currentPlayerEquipment, AttackStyle weaponAttackStyle, int attackStyleID, int slotID, int itemID)
    {
        // Change equipment slot to new item
        Item[] playerEquipment = changeEquipment(slotID, itemID, currentPlayerEquipment);

        // Calculate Melee Max Hit
        // Step 1: Calculate effective Strength
        int strengthLevel = client.getBoostedSkillLevel(Skill.STRENGTH);
        double prayerBonus = MaxHit.getPrayerBonus(client, weaponAttackStyle);
        int styleBonus = MaxHit.getAttackStyleBonus(weaponAttackStyle, attackStyleID);
        double voidBonus = MaxHit.getVoidMeleeBonus(client, playerEquipment); // default 1;

        double effectiveStrength = Math.floor((Math.floor(Math.floor(strengthLevel) * prayerBonus) + styleBonus + 8) * voidBonus);

        // Step 2: Calculate the base damage
        double strengthBonus = MaxHit.getMeleeStrengthBonus(client, itemManager, playerEquipment); // default 0

        double baseDamage = (0.5 + effectiveStrength * ((strengthBonus + 64)/640));
        double flooredBaseDamage = Math.floor(baseDamage);

        // Step 3: Calculate the bonus damage
        double specialBonusMultiplier = MaxHit.getMeleeSpecialBonusMultiplier(client, playerEquipment); // default 1

        double maxHit = (flooredBaseDamage * specialBonusMultiplier);

        // Complete
        return maxHit;
    }

    public static double calculateRangedMaxHit(Client client, ItemManager itemManager, Item[] currentPlayerEquipment, AttackStyle weaponAttackStyle, int attackStyleID, int slotID, int itemID)
    {
        // Change equipment slot to new item
        Item[] playerEquipment = changeEquipment(slotID, itemID, currentPlayerEquipment);

        // Calculate Ranged Max Hit
        // Step 1: Calculate effective ranged Strength
        int rangedLevel = client.getBoostedSkillLevel(Skill.RANGED);
        double prayerBonus = MaxHit.getPrayerBonus(client, weaponAttackStyle);
        int styleBonus = MaxHit.getAttackStyleBonus(weaponAttackStyle, attackStyleID);
        double voidBonus = MaxHit.getVoidRangedBonus(client, playerEquipment); // default 1;

        double effectiveRangedStrength = Math.floor((Math.floor(Math.floor(rangedLevel) * prayerBonus) + styleBonus + 8) * voidBonus);

        // Step 2: Calculate the max hit
        double equipmentRangedStrength = MaxHit.getRangedStrengthBonus(client, itemManager, playerEquipment);

        double maxHit = (0.5 + ((effectiveRangedStrength * (equipmentRangedStrength + 64))/640));

        // Step 3: Bonus damage from special attack and effects
        // Not used here.

        return maxHit;
    }

    public static double calculateMagicMaxHit(Client client, ItemManager itemManager, Item[] currentPlayerEquipment, AttackStyle weaponAttackStyle, int attackStyleID, int slotID, int itemID)
    {
        // Change equipment slot to new item
        Item[] playerEquipment = changeEquipment(slotID, itemID, currentPlayerEquipment);

        // Calculate Magic Max Hit
        // Step 1: Find the base hit of the spell
        double spellBaseMaxHit = MaxHit.getSpellBaseHit(client, playerEquipment, weaponAttackStyle, client.getBoostedSkillLevel(Skill.MAGIC));

        if (spellBaseMaxHit == 0)
        {
            return -1;
        }

        // Step 2: Calculate the Magic Damage Bonus
        double magicDmgBonus = MaxHit.getMagicEquipmentBoost(client, itemManager, playerEquipment);

        double maxDamage = (spellBaseMaxHit * magicDmgBonus);

        // Step 3: Calculate Type Bonuses
        // Not used here.

        return maxDamage;
    }
}
