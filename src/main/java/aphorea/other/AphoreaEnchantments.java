package aphorea.other;

import necesse.engine.modLoader.LoadedMod;
import necesse.engine.modifiers.ModifierValue;
import necesse.engine.registries.EnchantmentRegistry;
import necesse.inventory.enchants.*;

import java.util.*;

public class AphoreaEnchantments {
    public static Set<Integer> healingItemEnchantments = new HashSet<>();
    public static Set<Integer> healingEquipmentEnchantments = new HashSet<>();
    public static Set<Integer> areaItemEnchantments = new HashSet<>();

    public static int godly;
    public static int absent;
    public static int auxiliary;
    public static int vain;
    public static int gentle;
    public static int selfish;

    public static int friendly;
    public static int graceful;
    public static int wonderful;
    public static int ecologic;
    public static int exalted;
    public static int cursed;

    public static int booming;
    public static int dimmed;


    public AphoreaEnchantments() {
    }

    public static void registerCore() {
        godly = registerEnchantment(healingEquipmentEnchantments, "godly", new EquipmentItemEnchant(20, new ModifierValue<>(AphoreaModifiers.MAGIC_HEALING, 0.05F)));
        absent = registerEnchantment(healingEquipmentEnchantments, "absent", new EquipmentItemEnchant(20, new ModifierValue<>(AphoreaModifiers.MAGIC_HEALING, -0.05F)));
        auxiliary = registerEnchantment(healingEquipmentEnchantments, "auxiliary", new EquipmentItemEnchant(-20, new ModifierValue<>(AphoreaModifiers.MAGIC_HEALING_RECEIVED, 0.1F)));
        vain = registerEnchantment(healingEquipmentEnchantments, "vain", new EquipmentItemEnchant(-20, new ModifierValue<>(AphoreaModifiers.MAGIC_HEALING_RECEIVED, -0.1F)));
        gentle = registerEnchantment(healingEquipmentEnchantments, "gentle", new EquipmentItemEnchant(0, new ModifierValue<>(AphoreaModifiers.MAGIC_HEALING, 0.1F), new ModifierValue<>(AphoreaModifiers.MAGIC_HEALING_RECEIVED, -0.2F)));
        selfish = registerEnchantment(healingEquipmentEnchantments, "selfish", new EquipmentItemEnchant(0, new ModifierValue<>(AphoreaModifiers.MAGIC_HEALING, -0.1F), new ModifierValue<>(AphoreaModifiers.MAGIC_HEALING_RECEIVED, 0.3F)));

        friendly = registerEnchantment(healingItemEnchantments, "friendly", new ToolItemEnchantment(20, new ModifierValue<>(AphoreaModifiers.TOOL_MAGIC_HEALING, 0.2F)));
        graceful = registerEnchantment(healingItemEnchantments, "graceful", new ToolItemEnchantment(20, new ModifierValue<>(AphoreaModifiers.TOOL_MAGIC_HEALING_GRACE, 0.1F)));
        wonderful = registerEnchantment(healingItemEnchantments, "wonderful", new ToolItemEnchantment(10, new ModifierValue<>(AphoreaModifiers.TOOL_MAGIC_HEALING, 0.1F), new ModifierValue<>(ToolItemModifiers.MANA_USAGE, -0.1F)));
        ecologic = registerEnchantment(healingItemEnchantments, "ecologic", new ToolItemEnchantment(10, new ModifierValue<>(ToolItemModifiers.MANA_USAGE, -0.3F)));
        exalted = registerEnchantment(healingItemEnchantments, "exalted", new ToolItemEnchantment(-20, new ModifierValue<>(ToolItemModifiers.MANA_USAGE, 0.2F)));
        cursed = registerEnchantment(healingItemEnchantments, "cursed", new ToolItemEnchantment(-60, new ModifierValue<>(AphoreaModifiers.TOOL_MAGIC_HEALING_RECEIVED, -0.8F), new ModifierValue<>(AphoreaModifiers.TOOL_MAGIC_HEALING, 0.2F), new ModifierValue<>(AphoreaModifiers.TOOL_MAGIC_HEALING_GRACE, 0.2F)));

        booming = registerEnchantment(areaItemEnchantments, "booming", new ToolItemEnchantment(20, new ModifierValue<>(AphoreaModifiers.TOOL_AREA_RANGE, 0.3F)));
        dimmed = registerEnchantment(areaItemEnchantments, "dimmed", new ToolItemEnchantment(-20, new ModifierValue<>(AphoreaModifiers.TOOL_AREA_RANGE, -0.2F)));
    }

    public static int registerEnchantment(Set<Integer> list, String stringID, ItemEnchantment enchantment) {
        if (LoadedMod.isRunningModClientSide()) {
            throw new IllegalStateException("Client/server only mods cannot register enchantments");
        } else {
            int id = EnchantmentRegistry.registerEnchantment(stringID, enchantment);
            list.add(id);
            return id;
        }
    }
}
