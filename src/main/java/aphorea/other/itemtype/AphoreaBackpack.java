package aphorea.other.itemtype;

import necesse.engine.localization.Localization;
import necesse.engine.util.ComparableSequence;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.Inventory;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.Item;
import necesse.inventory.item.miscItem.PouchItem;
import necesse.level.maps.Level;

abstract public class AphoreaBackpack extends PouchItem {
    public AphoreaBackpack() {
        this.rarity = Rarity.COMMON;
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("global", "aphorea"));
        tooltips.add(Localization.translate("itemtooltip", "backpackslots", "slots", getInternalInventorySize()));
        tooltips.add(Localization.translate("itemtooltip", "backpack"));
        tooltips.add(Localization.translate("itemtooltip", "backpack2"));
        tooltips.add(Localization.translate("itemtooltip", "rclickinvopentip"));
        tooltips.add(Localization.translate("itemtooltip", "stored", "items", this.getStoredItemAmounts(item)));
        return tooltips;
    }

    public boolean isValidPouchItem(InventoryItem item) {
        if(item == null || item.item == null) return false;
        return this.isValidRequestType(item.item);
    }

    public boolean isValidRequestItem(Item item) {
        if(item == null) return false;
        return this.isValidRequestType(item);
    }

    public boolean isValidRequestType(Item item) {
        if(item == null) return false;
        return !item.getStringID().contains("backpack") && !item.getStringID().equals("coin");
    }

    public boolean isValidRequestType(Item.Type type) {
        return false;
    }


    public int getInventoryAmount(Level level, PlayerMob player, InventoryItem item, Item.Type requestType, String purpose) {
        int amount = super.getInventoryAmount(level, player, item, requestType, purpose);
        if (this.isValidRequestType(item.item)) {
            Inventory internalInventory = this.getInternalInventory(item);
            amount += internalInventory.getAmount(level, player, requestType, purpose);
        }

        return amount;
    }

    public Item getInventoryFirstItem(Level level, PlayerMob player, InventoryItem item, Item.Type requestType, String purpose) {
        if (this.isValidRequestType(item.item)) {
            Inventory internalInventory = this.getInternalInventory(item);
            Item firstItem = internalInventory.getFirstItem(level, player, requestType, purpose);
            if (firstItem != null) {
                return firstItem;
            }
        }

        return super.getInventoryFirstItem(level, player, item, requestType, purpose);
    }

    public int removeInventoryAmount(Level level, PlayerMob player, InventoryItem item, Item.Type requestType, int amount, String purpose) {
        int removed = 0;
        if (this.isValidRequestType(item.item)) {
            Inventory internalInventory = this.getInternalInventory(item);
            removed = internalInventory.removeItems(level, player, requestType, amount, purpose);
            if (removed > 0) {
                this.saveInternalInventory(item, internalInventory);
            }
        }

        return removed < amount ? removed + super.removeInventoryAmount(level, player, item, requestType, amount, purpose) : removed;
    }

    public boolean ignoreCombineStackLimit(Level level, PlayerMob player, InventoryItem me, InventoryItem them, String purpose) {
        return false;
    }

    public ComparableSequence<Integer> getInventoryAddPriority(Level level, PlayerMob player, Inventory inventory, int inventorySlot, InventoryItem item, InventoryItem input, String purpose) {
        boolean inInventory = inventory.streamSlots()
                .anyMatch(slot -> slot != null && slot.getItem() != null && slot.getItem().item.getID() == item.item.getID());

        if (inInventory) {
            return new ComparableSequence<>(inventorySlot);
        } else {
            return super.getInventoryAddPriority(level, player, inventory, inventorySlot, item, input, purpose);
        }
    }

}
