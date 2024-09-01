package aphorea.items.weapons.magic;

import aphorea.other.AphoreaArea;
import aphorea.other.AphoreaAreaList;
import aphorea.other.itemtype.weapons.Secondary.AphoreaMagicProjectileSecondaryAreaToolItem;
import aphorea.projectiles.UnstableGelProjectile;
import necesse.engine.localization.Localization;
import necesse.engine.network.PacketReader;
import necesse.engine.network.packet.PacketSpawnProjectile;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.sound.SoundEffect;
import necesse.engine.sound.SoundManager;
import necesse.engine.util.GameBlackboard;
import necesse.engine.util.GameRandom;
import necesse.entity.levelEvent.toolItemEvent.ToolItemEvent;
import necesse.entity.mobs.AttackAnimMob;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.projectile.Projectile;
import necesse.gfx.GameResources;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.PlayerInventorySlot;
import necesse.inventory.item.ItemInteractAction;
import necesse.inventory.item.ItemStatTipList;
import necesse.inventory.item.upgradeUtils.FloatUpgradeValue;
import necesse.level.maps.Level;

import java.awt.*;

public class UnstableGelStaff extends AphoreaMagicProjectileSecondaryAreaToolItem implements ItemInteractAction {

    public UnstableGelStaff() {
        super(500, new AphoreaAreaList(new AphoreaArea(200, new Color(191, 102, 255)).setDamageArea(new FloatUpgradeValue(0.0F, 0.2F).setBaseValue(20).setUpgradedValue(1, 90))), 800, 6.0F);
        rarity = Rarity.UNCOMMON;
        attackAnimTime.setBaseValue(800);
        attackDamage.setBaseValue(20)
                .setUpgradedValue(1, 90);
        velocity.setBaseValue(100);
        knockback.setBaseValue(0);
        attackRange.setBaseValue(600);

        manaCost.setBaseValue(3.0F);

        attackXOffset = 12;
        attackYOffset = 22;
    }

    @Override
    public ListGameTooltips getPreEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getPreEnchantmentTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("itemtooltip", "gelball"));
        tooltips.add(Localization.translate("itemtooltip", "stikybuff2"));
        return tooltips;
    }

    public void addStatTooltips(ItemStatTipList list, InventoryItem currentItem, InventoryItem lastItem, Mob perspective, boolean forceAdd) {
        this.addAttackDamageTip(list, currentItem, lastItem, perspective, forceAdd);
        this.addAttackSpeedTip(list, currentItem, lastItem, perspective);
        this.addCritChanceTip(list, currentItem, lastItem, perspective, forceAdd);
        this.addManaCostTip(list, currentItem, lastItem, perspective);
    }

    @Override
    public void showAttack(Level level, int x, int y, AttackAnimMob mob, int attackHeight, InventoryItem item, int seed, PacketReader contentReader) {
        if (level.isClient()) {
            SoundManager.playSound(GameResources.slimesplash, SoundEffect.effect(mob)
                    .volume(0.7f)
                    .pitch(GameRandom.globalRandom.getFloatBetween(1.0f, 1.1f)));
        }
    }

    @Override
    public InventoryItem onAttack(Level level, int x, int y, PlayerMob player, int attackHeight, InventoryItem item, PlayerInventorySlot slot, int animAttack, int seed, PacketReader contentReader) {

        Projectile projectile = new UnstableGelProjectile(
                level, player,
                player.x, player.y,
                x, y,
                getProjectileVelocity(item, player),
                getAttackRange(item),
                getAttackDamage(item),
                getKnockback(item, player)
        );
        GameRandom random = new GameRandom(seed);
        projectile.resetUniqueID(random);

        projectile.moveDist(40);

        level.entityManager.projectiles.addHidden(projectile);

        if (level.isServer()) {
            level.getServer().network.sendToClientsWithEntityExcept(new PacketSpawnProjectile(projectile), projectile, player.getServerClient());
        }

        this.consumeMana(player, item);

        return item;
    }

    @Override
    public void hitMob(InventoryItem item, ToolItemEvent event, Level level, Mob target, Mob attacker) {
        super.hitMob(item, event, level, target, attacker);
        ActiveBuff buff = new ActiveBuff(BuffRegistry.getBuff("stickybuff"), target, 2000, event.mob);
        target.addBuff(buff, true);
    }
}
