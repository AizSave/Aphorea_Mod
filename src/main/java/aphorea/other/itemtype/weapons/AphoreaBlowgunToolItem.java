package aphorea.other.itemtype.weapons;

import aphorea.other.utils.AphoreaTimeout;
import necesse.engine.localization.Localization;
import necesse.engine.network.PacketReader;
import necesse.engine.network.packet.PacketSpawnProjectile;
import necesse.engine.registries.DamageTypeRegistry;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.sound.SoundEffect;
import necesse.engine.sound.SoundManager;
import necesse.engine.util.GameBlackboard;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.AttackAnimMob;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.projectile.Projectile;
import necesse.gfx.GameResources;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.PlayerInventorySlot;
import necesse.inventory.item.Item;
import necesse.inventory.item.ItemCategory;
import necesse.inventory.item.ItemInteractAction;
import necesse.inventory.item.ItemStatTipList;
import necesse.inventory.item.toolItem.projectileToolItem.gunProjectileToolItem.GunProjectileToolItem;
import necesse.level.maps.Level;

import java.awt.*;

abstract public class AphoreaBlowgunToolItem extends GunProjectileToolItem implements ItemInteractAction {

    public String projectileID;

    public int cadence;

    public int mode;

    abstract public Projectile getProjectile(Level level, float x, float y, PlayerMob player, InventoryItem item);

    public AphoreaBlowgunToolItem(int enchantCost, String projectileID, int cadence) {
        super(new String[]{"riceseed", "eggplantseed", "pumpkinseed", "onionseed", "iceblossomseed", "strawberryseed", "sunflowerseed", "cabbageseed", "firemoneseed", "cornseed", "potatoseed", "chilipepperseed", "sugarbeetseed", "tomatoseed", "wheatseed", "carrotseed", "grassseed", "swampgrassseed"}, enchantCost);
        this.setItemCategory("equipment", "weapons", "rangedweapons");
        this.setItemCategory(ItemCategory.equipmentManager, "weapons", "rangedweapons");
        this.damageType = DamageTypeRegistry.RANGED;
        this.knockback.setBaseValue(25);
        this.attackRange.setBaseValue(500);
        this.enchantCost.setUpgradedValue(1.0F, 2100);

        this.projectileID = projectileID;
        this.cadence = cadence;

        this.attackAnimTime.setBaseValue(cadence * 3);

        mode = 0;
    }

    public Item getActualAmmo(PlayerMob player) {
        if (player == null) {
            return null;
        } else {
            Item firstAvaliable = null;
            String[] var3 = this.ammoTypes;

            for (String s : var3) {
                if (player.getInv().main.getAmount(player.getLevel(), player, ItemRegistry.getItem(s), "ammoseed") > 0) {
                    firstAvaliable = ItemRegistry.getItem(s);
                }
            }

            return firstAvaliable;
        }
    }

    public ListGameTooltips getPreEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getPreEnchantmentTooltips(item, perspective, blackboard);
        this.addExtraGunTooltips(tooltips, item, perspective, blackboard);
        this.addAmmoTooltips(tooltips, item);
        int ammoAmount = this.getAvailableAmmo(perspective);
        tooltips.add(Localization.translate("itemtooltip", "ammotip", "value", ammoAmount));
        tooltips.add(Localization.translate("itemtooltip", "blowgun2"));
        return tooltips;
    }

    @Override
    public ListGameTooltips getPostEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getPostEnchantmentTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("global", "aphorea"));
        return tooltips;
    }

    public void addStatTooltips(ItemStatTipList list, InventoryItem currentItem, InventoryItem lastItem, Mob perspective, boolean forceAdd) {
        this.addAttackDamageTip(list, currentItem, lastItem, perspective, forceAdd);
        this.addAttackSpeedTip(list, currentItem, lastItem, perspective);
        this.addResilienceGainTip(list, currentItem, lastItem, perspective, forceAdd);
        this.addCritChanceTip(list, currentItem, lastItem, perspective, forceAdd);
    }

    protected void addAmmoTooltips(ListGameTooltips tooltips, InventoryItem item) {
        tooltips.add(Localization.translate("itemtooltip", "blowgun"));
    }

    public Point getControllerAttackLevelPos(Level level, float aimDirX, float aimDirY, PlayerMob player, InventoryItem item) {
        if (this.controlledRange) {
            int range = this.getAttackRange(item);
            return new Point((int)(player.x + aimDirX * (float)range), (int)(player.y + aimDirY * (float)range));
        } else {
            return super.getControllerAttackLevelPos(level, aimDirX, aimDirY, player, item);
        }
    }

    public String canAttack(Level level, int x, int y, PlayerMob player, InventoryItem item) {
        return this.getAvailableAmmo(player) > 0 ? null : "";
    }

    public float[] perpendicularVector(float p1x, float p1y, float p2x, float p2y) {
        float Vx = p1y - p2y;
        float Vy = -p1x + p2x;
        float[] vector = new float[2];
        vector[0] = Vx;
        vector[1] = Vy;

        return vector;
    }

    public InventoryItem onAttack(Level level, int x, int y, PlayerMob player, int attackHeight, InventoryItem item, PlayerInventorySlot slot, int animAttack, int seed, PacketReader contentReader) {

        Item ammoseed = getActualAmmo(player);

        if (ammoseed != null && player.getInv().main.removeItems(level, player, ammoseed, 1, "ammoseed") >= 1) {

            if(mode == 0) {
                float initialX = player.x;
                float initialY = player.y;
                for (int i = 0; i < 3; i++) {
                    AphoreaTimeout.setTimeout(() -> {
                        if(level.isClient()) {
                            SoundManager.playSound(GameResources.run, SoundEffect.effect(player));
                        }

                        Projectile projectile = this.getProjectile(level, x + player.x - initialX, y + player.y - initialY, player, item);
                        GameRandom random = new GameRandom(seed);
                        projectile.resetUniqueID(random);

                        projectile.moveDist(82);

                        level.entityManager.projectiles.addHidden(projectile);

                        if (level.isServer()) {
                            level.getServer().network.sendToClientsWithEntityExcept(new PacketSpawnProjectile(projectile), projectile, player.getServerClient());
                        }
                    }, cadence * i);

                }
            } else {
                if(level.isClient()) {
                    SoundManager.playSound(GameResources.run, SoundEffect.effect(player));
                }
                for (int i = 0; i < 3; i++) {

                    float endX = x;
                    float endY = y;

                    float[] vector = perpendicularVector(x, y, player.x, player.y);

                    if(i == 1) {
                        endX = x + vector[0] / 6;
                        endY = y + vector[1] / 6;
                    } else if(i == 2) {
                        endX = x - vector[0] / 6;
                        endY = y - vector[1] / 6;
                    }

                    Projectile projectile = this.getProjectile(level, endX, endY, player, item);
                    GameRandom random = new GameRandom(seed);
                    projectile.resetUniqueID(random);

                    projectile.moveDist(82);

                    level.entityManager.projectiles.addHidden(projectile);

                    if (level.isServer()) {
                        level.getServer().network.sendToClientsWithEntityExcept(new PacketSpawnProjectile(projectile), projectile, player.getServerClient());
                    }
                }
            }
        }

        return item;
    }

    public void showAttack(Level level, int x, int y, AttackAnimMob mob, int attackHeight, InventoryItem item, int seed, PacketReader contentReader) {
    }

    public String getTranslatedTypeName() {
        return Localization.translate("itemtype", "blowgun");
    }

    public boolean canLevelInteract(Level level, int x, int y, PlayerMob player, InventoryItem item) {
        return !player.isRiding() && !player.isAttacking && getAvailableAmmo(player) > 0;
    }

    public InventoryItem onLevelInteract(Level level, int x, int y, PlayerMob player, int attackHeight, InventoryItem item, PlayerInventorySlot slot, int seed, PacketReader contentReader) {

        mode = 1;

        if(player.isClient()) {
            player.runAttack(player.attackSlot, x, y, 10);
        }

        mode = 0;

        return item;
    }
}
