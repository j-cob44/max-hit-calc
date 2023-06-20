package com.maxhitcalc;

import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;

public class EquipmentItems
{
    // All Item Slots Name Strings
    public String headName = "";
    public String capeName = "";
    public String amuletName = "";
    public String ammoName = "";
    public String weaponName = "";
    public String bodyName = "";
    public String shieldName = "";
    public String legsName = "";
    public String glovesName = "";
    public String bootName = "";
    public String ringName = "";

    public void getCurrentEquipment(Client client, Item[] playerEquipment){
        // Head
        String headItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.HEAD.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()] != null)
        {
            headItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.HEAD.getSlotIdx()].getId()).getName();
        }
        this.headName = headItemName;

        // Cape
        String capeItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.CAPE.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.CAPE.getSlotIdx()] != null)
        {
            capeItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.CAPE.getSlotIdx()].getId()).getName();
        }
        this.capeName = capeItemName;

        // Amulet
        String amuletItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.AMULET.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.AMULET.getSlotIdx()] != null)
        {
            amuletItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMULET.getSlotIdx()].getId()).getName();
        }
        this.amuletName = amuletItemName;

        // Ammo
        String ammoItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.AMMO.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.AMMO.getSlotIdx()] != null)
        {
            ammoItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.AMMO.getSlotIdx()].getId()).getName();
        }
        this.ammoName = ammoItemName;

        // Weapon
        String weaponItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.WEAPON.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()] != null)
        {
            weaponItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId()).getName();
        }
        this.weaponName = weaponItemName;

        // Body
        String bodyItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.BODY.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.BODY.getSlotIdx()] != null)
        {
            bodyItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.BODY.getSlotIdx()].getId()).getName();
        }
        this.bodyName = bodyItemName;

        // Shield
        String shieldItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.SHIELD.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.SHIELD.getSlotIdx()] != null)
        {
            shieldItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.SHIELD.getSlotIdx()].getId()).getName();
        }
        this.shieldName = shieldItemName;

        // Legs
        String legsItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.LEGS.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.LEGS.getSlotIdx()] != null)
        {
            legsItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.LEGS.getSlotIdx()].getId()).getName();
        }
        this.legsName = legsItemName;

        // Gloves
        String glovesItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.GLOVES.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.GLOVES.getSlotIdx()] != null)
        {
            glovesItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.GLOVES.getSlotIdx()].getId()).getName();
        }
        this.glovesName = glovesItemName;

        // Boot
        String bootItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.BOOTS.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.BOOTS.getSlotIdx()] != null)
        {
            bootItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.BOOTS.getSlotIdx()].getId()).getName();
        }
        this.bootName = bootItemName;

        // Ring
        String ringItemName = "";
        if(playerEquipment.length > EquipmentInventorySlot.RING.getSlotIdx()
                && playerEquipment[EquipmentInventorySlot.RING.getSlotIdx()] != null)
        {
            ringItemName = client.getItemDefinition(playerEquipment[EquipmentInventorySlot.RING.getSlotIdx()].getId()).getName();
        }
        this.ringName = ringItemName;

    }

}
