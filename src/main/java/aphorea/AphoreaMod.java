package aphorea;

import aphorea.buffs.*;
import aphorea.buffs.SetBonus.*;
import aphorea.buffs.Trinkets.Healing.*;
import aphorea.buffs.Trinkets.Periapts.*;
import aphorea.buffs.TrinketsActive.*;
import aphorea.items.GelSlimeNullifier;
import aphorea.items.UnstableCore;
import aphorea.items.armor.Gold.GoldHat;
import aphorea.items.armor.Rocky.RockyBoots;
import aphorea.items.armor.Rocky.RockyChestplate;
import aphorea.items.armor.Rocky.RockyHelmet;
import aphorea.items.armor.Swamp.SwampBoots;
import aphorea.items.armor.Swamp.SwampChestplate;
import aphorea.items.armor.Swamp.SwampHood;
import aphorea.items.armor.Swamp.SwampMask;
import aphorea.items.armor.Witch.MagicalBoots;
import aphorea.items.armor.Witch.MagicalSuit;
import aphorea.items.armor.Witch.WitchHat;
import aphorea.items.backpacks.*;
import aphorea.items.trinkets.ability_no.*;
import aphorea.items.trinkets.ability_yes.*;
import aphorea.items.weapons.healing.HealingStaff;
import aphorea.items.weapons.healing.MagicalVial;
import aphorea.items.weapons.magic.MagicalBroom;
import aphorea.items.weapons.magic.UnstableGelStaff;
import aphorea.items.weapons.melee.*;
import aphorea.items.weapons.range.Blowgun;
import aphorea.items.weapons.range.FireSling;
import aphorea.items.weapons.range.FrozenSling;
import aphorea.items.weapons.range.Sling;
import aphorea.items.weapons.throwable.GelBall;
import aphorea.items.weapons.throwable.GelBallGroup;
import aphorea.mobs.GelSlime;
import aphorea.mobs.RockyGelSlime;
import aphorea.mobs.Witch;
import aphorea.mobs.bosses.UnstableGelSlime;
import aphorea.mobs.bosses.UnstableGelSlime_Mini;
import aphorea.mobs.summon.BabyUnstableGelSlime;
import aphorea.mobs.summon.UndeadSkeleton;
import aphorea.objects.WitchStatue;
import aphorea.other.AphoreaEnchantments;
import aphorea.other.AphoreaModifiers;
import aphorea.other.data.AphoreaSwampLevelData;
import aphorea.other.data.AphoreaWorldData;
import aphorea.other.itemtype.weapons.AphoreaSaberToolItem;
import aphorea.projectiles.*;
import aphorea.tiles.GelTile;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.*;
import necesse.engine.util.GameUtils;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.buffs.staticBuffs.HiddenCooldownBuff;
import necesse.entity.mobs.buffs.staticBuffs.ShownCooldownBuff;
import necesse.gfx.gameTexture.GameTexture;
import necesse.inventory.item.Item;
import necesse.inventory.item.ItemCategory;
import necesse.inventory.item.matItem.MatItem;
import necesse.inventory.lootTable.LootTablePresets;
import necesse.inventory.lootTable.lootItem.ChanceLootItem;
import necesse.inventory.lootTable.lootItem.LootItem;
import necesse.inventory.lootTable.lootItem.LootItemList;
import necesse.inventory.recipe.Ingredient;
import necesse.inventory.recipe.Recipe;
import necesse.inventory.recipe.Recipes;
import necesse.inventory.recipe.Tech;
import necesse.level.maps.Level;
import necesse.level.maps.biomes.Biome;
import necesse.level.maps.biomes.MobChance;
import necesse.level.maps.biomes.MobSpawnTable;
import necesse.level.maps.biomes.swamp.SwampBiome;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@ModEntry
public class AphoreaMod {

    // Load modifiers and enchantments
    static AphoreaModifiers aphoreaModifiers = new AphoreaModifiers();
    static AphoreaEnchantments aphoreaEnchantments = new AphoreaEnchantments();

    public void init() throws Exception {
        System.out.println("AphoreaMod starting...");

        ItemCategory.createCategory("A-A-E", "equipment", "tools", "healing");
        ItemCategory.equipmentManager.createCategory("C-A-A", "tools");
        ItemCategory.equipmentManager.createCategory("C-B-A", "tools", "healingtools");

        // Data
        WorldDataRegistry.registerWorldData("aphoreaworlddata", AphoreaWorldData.class);
        LevelDataRegistry.registerLevelData("aphoreaswampleveldata", AphoreaSwampLevelData.class);

        // Tiles
        TileRegistry.registerTile("geltile", new GelTile("geltile", new Color(20, 80, 255)), -1.0F, true);

        // Objects
        ObjectRegistry.registerObject("witchstatue", new WitchStatue(), -1.0F, true);

        // Tech
        RecipeTechRegistry.registerTech("nebula", "nebulaworkstation");

        // Items

        Object[][] newItems = new Object[][]{
                {"gelball", new GelBall(), 2.0F},

                {"gelballgroup", new GelBallGroup()},
                {"gelsword", new GelSword()},
                {"unstablecore", new UnstableCore(), 20.0F},
                {"unstablegel", (new MatItem(100, Item.Rarity.UNCOMMON)).setItemCategory("materials"), 10.0F},
                {"unstablegelsword", new UnstableGelSword()},
                {"unstablegelstaff", new UnstableGelStaff()},
                {"unstablegelgreatsword", new UnstableGelGreatsword()},

                {"blowgun", new Blowgun()},

                {"sling", new Sling()},
                {"firesling", new FireSling()},
                {"frozensling", new FrozenSling()},

                {"rockyperiapt", new RockyPeriapt()},
                {"unstableperiapt", new UnstablePeriapt(), 100.0F},
                {"bloodyperiapt", new BloodyPeriapt()},
                {"demonicperiapt", new DemonicPeriapt()},
                {"frozenperiapt", new FrozenPeriapt()},
                {"necromancyperiapt", new NecromancyPeriapt()},

                {"rockygel", (new MatItem(100)).setItemCategory("materials"), 5.0F},
                {"rockyboots", new RockyBoots()},
                {"rockychestplate", new RockyChestplate()},
                {"rockyhelmet", new RockyHelmet()},

                {"goldhat", new GoldHat()},

                {"unstablegelbattleaxe", new UnstableGelBattleaxe()},
                {"demonicbattleaxe", new DemonicBattleaxe()},

                {"coppersaber", new CopperSaber()},
                {"ironsaber", new IronSaber()},
                {"goldsaber", new GoldSaber()},
                {"unstablegelsaber", new UnstableGelSaber()},
                {"demonicsaber", new DemonicSaber()},

                {"basicbackpack", new BasicBackpack()},
                {"sapphirebackpack", new SapphireBackpack()},
                {"amethystbackpack", new AmethystBackpack()},
                {"rubybackpack", new RubyBackpack()},
                {"emeraldbackpack", new EmeraldBackpack()},
                {"diamondbackpack", new DiamondBackpack()},

                {"gelslimenullifier", new GelSlimeNullifier()},

                {"broom", new Broom(), 50.0F},
                {"magicalbroom", new MagicalBroom()},
                {"witchhat", new WitchHat(), 200.0F},

                {"stardust", (new MatItem(100, Item.Rarity.UNCOMMON)).setItemCategory("materials"), 30.0F},
                {"healingstaff", new HealingStaff()},

                {"magicalsuit", new MagicalSuit()},
                {"magicalboots", new MagicalBoots()},

                {"floralring", new FloralRing(), 50.0F},
                {"gelring", new GelRing(), 50.0F},
                {"heartring", new HeartRing()},
                {"ringofhealth", new RingOfHealth()},
                {"magicalvial", new MagicalVial()},

                {"swampboots", new SwampBoots()},
                {"swampchestplate", new SwampChestplate()},
                {"swampmask", new SwampMask()},
                {"swamphood", new SwampHood()},
                {"swampshield", new SwampShield()},
        };

        for (Object[] newItem : newItems) {
            try {
                String stringID = (String) newItem[0];
                Item item = (Item) newItem[1];
                float brokerValue = newItem.length < 3 ? -1.0F : (float) newItem[2];
                boolean isObtainable = (boolean) (newItem.length < 4 ? true : newItem[3]);

                ItemRegistry.registerItem(stringID, item, brokerValue, isObtainable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Mobs
        MobRegistry.registerMob("gelslime", GelSlime.class, true);
        MobRegistry.registerMob("rockygelslime", RockyGelSlime.class, true);

        MobRegistry.registerMob("witch", Witch.class, true);

        // Bosses [Mobs]
        MobRegistry.registerMob("unstablegelslime", UnstableGelSlime.class, true);
        MobRegistry.registerMob("unstablegelslime_mini", UnstableGelSlime_Mini.class, true);

        // Summon [Mobs]
        MobRegistry.registerMob("babyunstablegelslime", BabyUnstableGelSlime.class, false);
        MobRegistry.registerMob("undeadskeleton", UndeadSkeleton.class, false);

        // Projectiles
        ProjectileRegistry.registerProjectile("gelprojectile", GelProjectile.class, "gelprojectile", "ball_shadow");
        ProjectileRegistry.registerProjectile("unstablegelprojectile", UnstableGelProjectile.class, "unstablegelprojectile", "ball_shadow");

        ProjectileRegistry.registerProjectile("aircutprojectile", AircutProjectile.class, "none", "none");

        ProjectileRegistry.registerProjectile("stoneprojectile", StoneProjectile.class, "stoneprojectile", "ball_shadow");
        ProjectileRegistry.registerProjectile("firestoneprojectile", FireStoneProjectile.class, "firestoneprojectile", "ball_shadow");
        ProjectileRegistry.registerProjectile("frozenstoneprojectile", FrozenStoneProjectile.class, "frozenstoneprojectile", "ball_shadow");

        ProjectileRegistry.registerProjectile("seedprojectile", SeedProjectile.class, "seedprojectile", "none");

        // Projectiles [Mobs]
        ProjectileRegistry.registerProjectile("witchprojectile", WitchProjectile.class, "none", "none");


        // Buffs
        BuffRegistry.registerBuff("inmortalbuff", new InmortalBuff());
        BuffRegistry.registerBuff("inmortalcooldown", new ShownCooldownBuff());

        BuffRegistry.registerBuff("cooldownbuff", new HiddenCooldownBuff());
        BuffRegistry.registerBuff("stopbuff", new StopBuff());
        BuffRegistry.registerBuff("stunbuff", new StunBuff());

        BuffRegistry.registerBuff("noattackmovementreductionbuff", new NoAttackMovementReductionBuff());

        BuffRegistry.registerBuff("stickybuff", new StickyBuff());

        BuffRegistry.registerBuff("unstableperiaptbuff", new UnstablePeriaptBuff());
        BuffRegistry.registerBuff("necromancyperiaptbuff", new NecromancyPeriaptBuff());

        BuffRegistry.registerBuff("swordchargeattackcooldown", new ShownCooldownBuff());
        BuffRegistry.registerBuff("greatswordareaattackcooldown", new ShownCooldownBuff());

        BuffRegistry.registerBuff("berserkerrushbuff", new BerserkerRushBuff());
        BuffRegistry.registerBuff("berserkerrushcooldownbuff", new ShownCooldownBuff());

        BuffRegistry.registerBuff("periaptcooldownbuff", new ShownCooldownBuff());
        BuffRegistry.registerBuff("periaptactivebuff", new PeriaptActiveBuff());

        BuffRegistry.registerBuff("rockyperiaptbuff", new RockyPeriaptBuff());
        BuffRegistry.registerBuff("rockyperiaptactivebuff", new RockyPeriaptActiveBuff());

        BuffRegistry.registerBuff("bloodyperiaptbuff", new BloodyPeriaptBuff());
        BuffRegistry.registerBuff("bloodyperiaptactivebuff", new BloodyPeriaptActiveBuff());

        BuffRegistry.registerBuff("demonicperiaptbuff", new DemonicPeriaptBuff());
        BuffRegistry.registerBuff("demonicperiaptactivebuff", new DemonicPeriaptActiveBuff());

        BuffRegistry.registerBuff("frozenperiaptbuff", new FrozenPeriaptBuff());
        BuffRegistry.registerBuff("frozenperiaptactivebuff", new FrozenPeriaptActiveBuff());

        BuffRegistry.registerBuff("goldhatsetbonusbuff", new GoldHatSetBonusBuff());
        BuffRegistry.registerBuff("rockysetbonusbuff", new RockySetBonusBuff());
        BuffRegistry.registerBuff("witchsetbonusbuff", new WitchSetBonusBuff());
        BuffRegistry.registerBuff("swampmasksetbonusbuff", new SwampMaskSetBonusBuff());
        BuffRegistry.registerBuff("swamphoodsetbonusbuff", new SwampHoodSetBonusBuff());

        BuffRegistry.registerBuff("saberdashcooldown", new ShownCooldownBuff());
        BuffRegistry.registerBuff("saberdashactive", new HiddenCooldownBuff());

        BuffRegistry.registerBuff("floralringbuff", new FloralRingBuff());
        BuffRegistry.registerBuff("gelringbuff", new GelRingBuff());
        BuffRegistry.registerBuff("heartringbuff", new HeartRingBuff());
        BuffRegistry.registerBuff("ringofhealthbuff", new RingOfHealthBuff());

        // LevelEvent

        LevelEventRegistry.registerEvent("saberdashlevelevent", AphoreaSaberToolItem.SaberDashLevelEvent.class);

        // Packet

        PacketRegistry.registerPacket(MagicalBroom.PacketMagicalBroom.class);

        // Enchantments
        AphoreaEnchantments.registerCore();

        // Biomes & Levels

        // Worlds

        System.out.println("AphoreaMod started");
    }

    public void initResources() {

        GelSlime.texture = GameTexture.fromFile("mobs/gelslime");
        RockyGelSlime.texture = GameTexture.fromFile("mobs/rockygelslime");

        Witch.texture = GameTexture.fromFile("mobs/witch");

        UnstableGelSlime.texture = GameTexture.fromFile("mobs/unstablegelslime");
        UnstableGelSlime.icon = GameTexture.fromFile("mobs/icons/unstablegelslime");
        UnstableGelSlime_Mini.texture = GameTexture.fromFile("mobs/unstablegelslime_mini");

        BabyUnstableGelSlime.texture = GameTexture.fromFile("mobs/babyunstablegelslime");

        FireStoneProjectile.texture_2 = GameTexture.fromFile("projectiles/firestoneprojectile_2");

        AircutProjectile.addNone();

        AircutProjectile.addTexture("copper", new Color(202, 108, 91));
        AircutProjectile.addTexture("iron", new Color(192, 192, 192));
        AircutProjectile.addTexture("gold", new Color(228, 176, 77));
        AircutProjectile.addTexture("unstablegel", new Color(191, 102, 255));
        AircutProjectile.addTexture("demonic", new Color(53, 46, 66));
    }

    public void postInit() {
        // Recipes

        addCraftingList("woodstaff", RecipeTechRegistry.WORKSTATION,
                new AphoreaCraftingRecipe("healingstaff", 1,
                        new Ingredient("woodstaff", 1),
                        new Ingredient("stardust", 5)
                ),
                new AphoreaCraftingRecipe("gelsword", 1,
                        new Ingredient("gelball", 15)
                ),
                new AphoreaCraftingRecipe("gelballgroup", 1,
                        new Ingredient("gelball", 12),
                        new Ingredient("mysteriousportal", 1)
                ),
                new AphoreaCraftingRecipe("gelslimenullifier", 1,
                        new Ingredient("gelball", 20),
                        new Ingredient("unstablegel", 5)
                ),
                new AphoreaCraftingRecipe("unstablegelsword", 1,
                        new Ingredient("gelsword", 1),
                        new Ingredient("unstablegel", 10)
                ),
                new AphoreaCraftingRecipe("unstablegelstaff", 1,
                        new Ingredient("gelballgroup", 1),
                        new Ingredient("unstablegel", 15)
                ),

                new AphoreaCraftingRecipe("sling", 1,
                        new Ingredient("leather", 4),
                        new Ingredient("rope", 2)
                ),
                new AphoreaCraftingRecipe("sling", 1,
                        new Ingredient("leather", 4),
                        new Ingredient("rope", 2)
                ),
                new AphoreaCraftingRecipe("firesling", 1,
                        new Ingredient("sling", 1),
                        new Ingredient("torch", 60)
                ),
                new AphoreaCraftingRecipe("frozensling", 1,
                        new Ingredient("sling", 1),
                        new Ingredient("frostshard", 10)
                ),

                new AphoreaCraftingRecipe("blowgun", 1,
                        new Ingredient("anysapling", 10)
                )
        );

        addCraftingList("trackerboot", RecipeTechRegistry.WORKSTATION,
                new AphoreaCraftingRecipe("basicbackpack", 1,
                        new Ingredient("leather", 6),
                        new Ingredient("ironbar", 1),
                        new Ingredient("rope", 1)
                ),
                new AphoreaCraftingRecipe("sapphirebackpack", 1,
                        new Ingredient("basicbackpack", 1),
                        new Ingredient("sapphire", 4)
                ),
                new AphoreaCraftingRecipe("amethystbackpack", 1,
                        new Ingredient("sapphirebackpack", 1),
                        new Ingredient("amethyst", 4)
                ),
                new AphoreaCraftingRecipe("rubybackpack", 1,
                        new Ingredient("amethystbackpack", 1),
                        new Ingredient("ruby", 4)
                ),
                new AphoreaCraftingRecipe("emeraldbackpack", 1,
                        new Ingredient("rubybackpack", 1),
                        new Ingredient("emerald", 4)
                ),
                new AphoreaCraftingRecipe("diamondbackpack", 1,
                        new Ingredient("emeraldbackpack", 1),
                        new Ingredient("pearlescentdiamond", 4)
                )
        );

        addCraftingList("clothboots", RecipeTechRegistry.WORKSTATION,
                new AphoreaCraftingRecipe("magicalsuit", 1,
                        new Ingredient("clothrobe", 1),
                        new Ingredient("stardust", 4)
                ),
                new AphoreaCraftingRecipe("magicalboots", 1,
                        new Ingredient("clothboots", 1),
                        new Ingredient("stardust", 3)
                ),
                new AphoreaCraftingRecipe("swampmask", 1,
                        new Ingredient("willowlog", 10),
                        new Ingredient("stardust", 3)
                ),
                new AphoreaCraftingRecipe("swamphood", 1,
                        new Ingredient("swampsludge", 5),
                        new Ingredient("stardust", 3)
                ),
                new AphoreaCraftingRecipe("swampchestplate", 1,
                        new Ingredient("willowlog", 5),
                        new Ingredient("swampsludge", 3),
                        new Ingredient("stardust", 2)
                ),
                new AphoreaCraftingRecipe("swampboots", 1,
                        new Ingredient("willowlog", 8),
                        new Ingredient("stardust", 1)
                ),
                new AphoreaCraftingRecipe("swampshield", 1,
                        new Ingredient("willowlog", 10),
                        new Ingredient("swampsludge", 3),
                        new Ingredient("stardust", 2)
                )
        );

        addCraftingList("stonepathtile", RecipeTechRegistry.IRON_ANVIL,
                new AphoreaCraftingRecipe("geltile", 1,
                        new Ingredient("stonepathtile", 1),
                        new Ingredient("gelball", 3)
                )
        );

        addCraftingList("goldgreatbow", RecipeTechRegistry.IRON_ANVIL,
                new AphoreaCraftingRecipe("rockyhelmet", 1,
                        new Ingredient("stone", 30),
                        new Ingredient("rockygel", 7)
                ),
                new AphoreaCraftingRecipe("rockychestplate", 1,
                        new Ingredient("stone", 40),
                        new Ingredient("rockygel", 10)
                ),
                new AphoreaCraftingRecipe("rockyboots", 1,
                        new Ingredient("stone", 20),
                        new Ingredient("rockygel", 5)
                ),
                new AphoreaCraftingRecipe("rockyperiapt", 1,
                        new Ingredient("stone", 10),
                        new Ingredient("rockygel", 4)
                ),

                new AphoreaCraftingRecipe("unstablegelsaber", 1,
                        new Ingredient("unstablegel", 10),
                        new Ingredient("rockygel", 3)
                ),
                new AphoreaCraftingRecipe("unstablegelgreatsword", 1,
                        new Ingredient("unstablegel", 20),
                        new Ingredient("rockygel", 5)
                ),
                new AphoreaCraftingRecipe("unstablegelbattleaxe", 1,
                        new Ingredient("unstablegel", 20),
                        new Ingredient("rockygel", 15)
                ),
                new AphoreaCraftingRecipe("witchstatue", 1,
                        new Ingredient("goldbar", 10),
                        new Ingredient("stardust", 3)
                )
        );

        addCraftingList("coppersword", RecipeTechRegistry.IRON_ANVIL,
                new AphoreaCraftingRecipe("coppersaber", 1,
                        new Ingredient("copperbar", 10),
                        new Ingredient("anylog", 1)
                )
        );

        addCraftingList("ironsword", RecipeTechRegistry.IRON_ANVIL,
                new AphoreaCraftingRecipe("ironsaber", 1,
                        new Ingredient("ironbar", 10),
                        new Ingredient("anylog", 1)
                )
        );

        addCraftingList("goldsword", RecipeTechRegistry.IRON_ANVIL,
                new AphoreaCraftingRecipe("goldsaber", 1,
                        new Ingredient("goldbar", 10),
                        new Ingredient("anylog", 1)
                )
        );

        addCraftingList("goldsword", RecipeTechRegistry.IRON_ANVIL,
                new AphoreaCraftingRecipe("goldsaber", 1,
                        new Ingredient("goldbar", 10),
                        new Ingredient("anylog", 1)
                )
        );

        addCraftingList("demonicsword", RecipeTechRegistry.DEMONIC,
                new AphoreaCraftingRecipe("demonicsaber", 1,
                        new Ingredient("demonicbar", 12)
                ),
                new AphoreaCraftingRecipe("demonicbattleaxe", 1,
                        new Ingredient("demonicbar", 20),
                        new Ingredient("rockygel", 10)
                )
        );

        addCraftingList("sapphirerevolver", RecipeTechRegistry.DEMONIC,
                new AphoreaCraftingRecipe("goldhat", 1,
                        new Ingredient("goldbar", 8),
                        new Ingredient("sapphire", 5)
                )
        );

        addCraftingList("froststaff", RecipeTechRegistry.IRON_ANVIL,
                new AphoreaCraftingRecipe("frozenperiapt", 1,
                        new Ingredient("frostshard", 10),
                        new Ingredient("goldbar", 5)
                )
        );

        addCraftingList("bloodplateboots", RecipeTechRegistry.DEMONIC,
                new AphoreaCraftingRecipe("bloodyperiapt", 1,
                        new Ingredient("voidshard", 10),
                        new Ingredient("batwing", 10)
                ),
                new AphoreaCraftingRecipe("demonicperiapt", 1,
                        new Ingredient("bloodyperiapt", 1),
                        new Ingredient("demonicbar", 5)
                ),
                new AphoreaCraftingRecipe("necromancyperiapt", 1,
                        new Ingredient("unstableperiapt", 1),
                        new Ingredient("bone", 10),
                        new Ingredient("demonicbar", 5)
                ),
                new AphoreaCraftingRecipe("magicalbroom", 1,
                        new Ingredient("broom", 1),
                        new Ingredient("voidshard", 8),
                        new Ingredient("stardust", 3)
                ),
                new AphoreaCraftingRecipe("magicalvial", 1,
                        new Ingredient("greaterhealthpotion", 2),
                        new Ingredient("healthpotion", 8),
                        new Ingredient("stardust", 3)
                ),
                new AphoreaCraftingRecipe("heartring", 1,
                        new Ingredient("greaterhealthpotion", 1),
                        new Ingredient("firemone", 10),
                        new Ingredient("voidshard", 6)
                ),
                new AphoreaCraftingRecipe("ringofhealth", 1,
                        new Ingredient("floralring", 1),
                        new Ingredient("gelring", 1),
                        new Ingredient("heartring", 1),
                        new Ingredient("goldbar", 1)
                ),
                new AphoreaCraftingRecipe("ringofhealth", 1,
                        new Ingredient("floralring", 1),
                        new Ingredient("heartring", 1),
                        new Ingredient("goldbar", 1),
                        new Ingredient("gelball", 10),
                        new Ingredient("stardust", 1)
                )
        );

        // Spawn tables

        Biome.defaultSurfaceMobs
                .add(addLessChanceDayMob(40, "gelslime", 0.8F, 32 * 32, 2));

        Biome.forestCaveMobs
                .add(30, "rockygelslime");

        SwampBiome.surfaceMobs
                .add(addLessChanceMob(1, "witch", 0.3F, 256 * 32, 1));


        // Server chat commands

        // LootTables

        LootTablePresets.caveCryptCoffin.items.add(
                ChanceLootItem.between(0.1f, "bloodyperiapt", 1, 1)
        );

        LootTablePresets.snowCaveChest.items.add(
                ChanceLootItem.between(0.05f, "frozenperiapt", 1, 1)
        );

        LootTablePresets.startChest.items.addAll(
                new LootItemList(
                        new LootItem("sling", 1),
                        new LootItem("basicbackpack", 1)
                )
        );

        LootTablePresets.surfaceRuinsChest.items.addAll(
                new LootItemList(
                        ChanceLootItem.between(0.05f, "blowgun", 1, 1),
                        ChanceLootItem.between(0.05f, "sling", 1, 1)
                )
        );

        LootTablePresets.basicCaveChest.items.addAll(
                new LootItemList(
                        ChanceLootItem.between(0.05f, "blowgun", 1, 1),
                        ChanceLootItem.between(0.05f, "sling", 1, 1)
                )
        );

        LootTablePresets.hunterChest.items.addAll(
                new LootItemList(
                        ChanceLootItem.between(0.05f, "blowgun", 1, 1),
                        ChanceLootItem.between(0.05f, "sling", 1, 1)
                )
        );

        LootTablePresets.dungeonChest.items.addAll(
                new LootItemList(
                        ChanceLootItem.between(0.05f, "heartring", 1, 1)
                )
        );
    }

    public static MobChance addLessChanceMob(int tickets, String mobStringID, float chance, int searchRange, long maxMobs) {

        MobSpawnTable.CanSpawnPredicate canSpawn = (level, client, spawnTile) -> {
            if (Math.random() > chance) return false;
            if (searchRange == 0 || maxMobs == 0) {
                return true;
            } else {
                Point spawnPos = new Point(spawnTile.x * 32 + 16, spawnTile.y * 32 + 16);
                long count = level.entityManager.mobs.streamInRegionsShape(GameUtils.rangeBounds(spawnPos.x, spawnPos.y, searchRange), 0).filter((m) -> m.getDistance((float) spawnPos.x, (float) spawnPos.y) <= (float) searchRange && Objects.equals(mobStringID, m.getStringID())).count();
                return count < maxMobs;
            }
        };

        MobSpawnTable.MobProducer mobProducer = (level, client, spawnTile) -> MobRegistry.getMob(mobStringID, level);

        return new MobChance(tickets) {
            public boolean canSpawn(Level level, ServerClient client, Point spawnTile) {
                return canSpawn.canSpawn(level, client, spawnTile);
            }

            public Mob getMob(Level level, ServerClient client, Point spawnTile) {
                return mobProducer.getMob(level, client, spawnTile);
            }
        };
    }

    public static MobChance addLessChanceDayMob(int tickets, String mobStringID, float chance, int searchRange, long maxMobs) {

        MobSpawnTable.CanSpawnPredicate canSpawn = (level, client, spawnTile) -> {
            if (level.getWorldEntity().isNight()) return false;
            if (Math.random() > chance) return false;
            if (searchRange == 0 || maxMobs == 0) {
                return true;
            } else {
                Point spawnPos = new Point(spawnTile.x * 32 + 16, spawnTile.y * 32 + 16);
                long count = level.entityManager.mobs.streamInRegionsShape(GameUtils.rangeBounds(spawnPos.x, spawnPos.y, searchRange), 0).filter((m) -> m.getDistance((float) spawnPos.x, (float) spawnPos.y) <= (float) searchRange && Objects.equals(mobStringID, m.getStringID())).count();
                return count < maxMobs;
            }

        };

        MobSpawnTable.MobProducer mobProducer = (level, client, spawnTile) -> MobRegistry.getMob(mobStringID, level);

        return new MobChance(tickets) {
            public boolean canSpawn(Level level, ServerClient client, Point spawnTile) {
                return canSpawn.canSpawn(level, client, spawnTile);
            }

            public Mob getMob(Level level, ServerClient client, Point spawnTile) {
                return mobProducer.getMob(level, client, spawnTile);
            }
        };
    }

    public static void addCraftingList(String previousItem, Tech tech, AphoreaCraftingRecipe... recipes) {
        AtomicReference<String> lastRecipe = new AtomicReference<>(previousItem);
        Arrays.stream(recipes).forEach(r -> {
            r.registerRecipe(lastRecipe.get(), tech);
            lastRecipe.set(r.item);
        });
    }

    public static class AphoreaCraftingRecipe {
        public String item;
        public int amount;
        private final Ingredient[] ingredients;

        public AphoreaCraftingRecipe(String item, int amount, Ingredient... ingredients) {
            this.item = item;
            this.amount = amount;
            this.ingredients = ingredients;
        }

        public void registerRecipe(String previousItem, Tech tech) {
            Recipe recipe = new Recipe(item, amount, tech, ingredients);

            if (previousItem != null) {
                recipe.showAfter(previousItem);
            }
            Recipes.registerModRecipe(recipe);
        }
    }

}
