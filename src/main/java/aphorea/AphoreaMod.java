package aphorea;

import aphorea.buffs.*;
import aphorea.buffs.SetBonus.*;
import aphorea.buffs.Trinkets.FloralRingBuff;
import aphorea.buffs.Trinkets.GelRingBuff;
import aphorea.buffs.Trinkets.HeartRingBuff;
import aphorea.buffs.Trinkets.Periapts.*;
import aphorea.buffs.Trinkets.RingOfHealthBuff;
import aphorea.buffs.TrinketsActive.*;
import aphorea.items.*;
import aphorea.items.armor.Gold.*;
import aphorea.items.armor.Rocky.*;
import aphorea.items.armor.Swamp.*;
import aphorea.items.armor.Witch.*;
import aphorea.items.backpacks.*;
import aphorea.objects.WitchStatue;
import aphorea.other.AphoreaEnchantments;
import aphorea.other.AphoreaModifiers;
import aphorea.other.data.AphoreaSwampLevelData;
import aphorea.other.data.AphoreaWorldData;
import aphorea.items.trinkets.ability_no.*;
import aphorea.items.trinkets.ability_yes.*;
import aphorea.items.weapons.healing.HealingStaff;
import aphorea.items.weapons.healing.MagicalVial;
import aphorea.items.weapons.magic.*;
import aphorea.items.weapons.melee.*;
import aphorea.items.weapons.range.*;
import aphorea.items.weapons.throwable.*;
import aphorea.mobs.*;
import aphorea.mobs.bosses.*;
import aphorea.mobs.summon.*;
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
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@ModEntry
public class AphoreaMod {

    static AphoreaModifiers aphoreaModifiers = new AphoreaModifiers();
    static AphoreaEnchantments aphoreaEnchantments = new AphoreaEnchantments();

    public void init() {
        ItemCategory.createCategory("A-A-E", "equipment", "tools", "healing");
        ItemCategory.equipmentManager.createCategory("C-A-A", "tools");
        ItemCategory.equipmentManager.createCategory("C-B-A", "tools", "healingtools");

        VersionMigration.oldItemStringIDs = GameUtils.concat(VersionMigration.oldItemStringIDs, new String[][] {
                { "unstablecore", "unestablecore" },
                { "unstablegel", "unestablegel" },
                { "unstablegelsword", "unestablegelsword" },
                { "unstablegelstaff", "unestablegelstaff" },
                { "unstablegelgreatsword", "unestablegelgreatsword" },
                { "unstablegelbattleaxe", "unestablegelbattleaxe" },

                { "rockyboots", "stoneboots" },
                { "rockychestplate", "stonechestplate" },
                { "rockyhelmet", "stonehelmet" }
        });

        System.out.println("Aphorea started");

        // Data
        WorldDataRegistry.registerWorldData("aphoreaworlddata", AphoreaWorldData.class);
        LevelDataRegistry.registerLevelData("aphoreaswampleveldata", AphoreaSwampLevelData.class);

        // Tiles
        TileRegistry.registerTile("geltile", new GelTile("geltile", new Color(20, 80, 255)), 1, true);

        // Objects
        ObjectRegistry.registerObject("witchstatue", new WitchStatue(), 1, true);

        // Items

        Object[][] itemsNuevos = new Object[][] {
                {"gelball", GelBall.class, 1, true},

                {"gelballgroup", GelBallGroup.class, 1, true},
                {"gelsword", GelSword.class, 1, true},
                {"unstablecore", UnstableCore.class, 1, true},
                {"unstablegel", UnstableGel.class, 1, true},
                {"unstablegelsword", UnstableGelSword.class, 1, true},
                {"unstablegelstaff", UnstableGelStaff.class, 1, true},
                {"unstablegelgreatsword", UnstableGelGreatsword.class, 1, true},

                {"blowgun", Blowgun.class, 1, true},

                {"sling", Sling.class, 1, true},
                {"firesling", FireSling.class, 1, true},
                {"frozensling", FrozenSling.class, 1, true},

                {"rockyperiapt", RockyPeriapt.class, 1, true},
                {"unstableperiapt", UnstablePeriapt.class, 1, true},
                {"bloodyperiapt", BloodyPeriapt.class, 1, true},
                {"demonicperiapt", DemonicPeriapt.class, 1, true},
                {"frozenperiapt", FrozenPeriapt.class, 1, true},
                {"necromancyperiapt", NecromancyPeriapt.class, 1, true},

                {"rockygel", RockyGel.class, 1, true},
                {"rockyboots", RockyBoots.class, 1, true},
                {"rockychestplate", RockyChestplate.class, 1, true},
                {"rockyhelmet", RockyHelmet.class, 1, true},

                {"goldhat", GoldHat.class, 1, true},

                {"unstablegelbattleaxe", UnstableGelBattleaxe.class, 1, true},
                {"demonicbattleaxe", DemonicBattleaxe.class, 1, true},

                {"coppersaber", CopperSaber.class, 1, true},
                {"ironsaber", IronSaber.class, 1, true},
                {"goldsaber", GoldSaber.class, 1, true},
                {"unstablegelsaber", UnstableGelSaber.class, 1, true},
                {"demonicsaber", DemonicSaber.class, 1, true},

                {"basicbackpack", BasicBackpack.class, 1, true},
                {"sapphirebackpack", SapphireBackpack.class, 1, true},
                {"amethystbackpack", AmethystBackpack.class, 1, true},
                {"rubybackpack", RubyBackpack.class, 1, true},
                {"emeraldbackpack", EmeraldBackpack.class, 1, true},
                {"diamondbackpack", DiamondBackpack.class, 1, true},

                {"gelslimenullifier", GelSlimeNullifier.class, 1, true},

                {"broom", Broom.class, 1, true},
                {"magicalbroom", MagicalBroom.class, 1, true},
                {"witchhat", WitchHat.class, 1, true},

                {"stardust", Stardust.class, 1, true},
                {"healingstaff", HealingStaff.class, 1, true},

                {"magicalsuit", MagicalSuit.class, 1, true},
                {"magicalboots", MagicalBoots.class, 1, true},

                {"floralring", FloralRing.class, 1, true},
                {"gelring", GelRing.class, 1, true},
                {"heartring", HeartRing.class, 1, true},
                {"ringofhealth", RingOfHealth.class, 1, true},
                {"magicalvial", MagicalVial.class, 1, true},

                {"swampboots", SwampBoots.class, 1, true},
                {"swampchestplate", SwampChestplate.class, 1, true},
                {"swampmask", SwampMask.class, 1, true},
                {"swamphood", SwampHood.class, 1, true},
                {"swampshield", SwampShield.class, 1, true},

        };

        for (Object[] itemNuevo : itemsNuevos) {
            try {
                String stringID = (String) itemNuevo[0];
                Class<?> itemClass = (Class<?>) itemNuevo[1];
                int brokerValue = (int) itemNuevo[2];
                boolean isObtainable = (boolean) itemNuevo[3];

                Constructor<?> constructor = itemClass.getConstructor();
                Item item = (Item) constructor.newInstance();

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
    }

    public void initResources() {

        GelSlime.texture = GameTexture.fromFile("mobs/gelslime");
        RockyGelSlime.texture = GameTexture.fromFile("mobs/rockygelslime");

        Witch.texture = GameTexture.fromFile("mobs/witch");

        UnstableGelSlime.texture = GameTexture.fromFile("mobs/unstablegelslime");
        UnstableGelSlime.icon =  GameTexture.fromFile("mob_icons/unstablegelslime");
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

        Biome.defaultCaveMobs = Biome.defaultCaveMobs
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
            if(Math.random() > chance) return false;
            if(searchRange == 0 || maxMobs == 0) {
                return true;
            } else {
                Point spawnPos = new Point(spawnTile.x * 32 + 16, spawnTile.y * 32 + 16);
                long count = level.entityManager.mobs.streamInRegionsShape(GameUtils.rangeBounds(spawnPos.x, spawnPos.y, searchRange), 0).filter((m) -> m.getDistance((float)spawnPos.x, (float)spawnPos.y) <= (float)searchRange && Objects.equals(mobStringID, m.getStringID())).count();
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
            if(level.getWorldEntity().isNight()) return false;
            if(Math.random() > chance) return false;
            if(searchRange == 0 || maxMobs == 0) {
                return true;
            } else {
                Point spawnPos = new Point(spawnTile.x * 32 + 16, spawnTile.y * 32 + 16);
                long count = level.entityManager.mobs.streamInRegionsShape(GameUtils.rangeBounds(spawnPos.x, spawnPos.y, searchRange), 0).filter((m) -> m.getDistance((float)spawnPos.x, (float)spawnPos.y) <= (float)searchRange && Objects.equals(mobStringID, m.getStringID())).count();
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
        public Ingredient[] ingredients;
        public AphoreaCraftingRecipe(String item, int amount, Ingredient... ingredients) {
            this.item = item;
            this.amount = amount;
            this.ingredients = ingredients;
        }

        public void registerRecipe(String previousItem, Tech tech) {
            Recipe recipe = new Recipe(item, amount, tech, ingredients);
            if(previousItem != null) {
                recipe.showAfter(previousItem);
            }
            Recipes.registerModRecipe(recipe);
        }
    }

}
