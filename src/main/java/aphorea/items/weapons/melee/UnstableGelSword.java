package aphorea.items.weapons.melee;

import aphorea.other.itemtype.weapons.Secondary.AphoreaSwordSecondaryChargeToolItem;
import aphorea.projectiles.AircutProjectile;
import necesse.engine.localization.Localization;
import necesse.engine.network.PacketReader;
import necesse.engine.network.packet.PacketSpawnProjectile;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.util.GameBlackboard;
import necesse.engine.util.GameRandom;
import necesse.entity.levelEvent.toolItemEvent.ToolItemEvent;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.projectile.Projectile;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.PlayerInventorySlot;
import necesse.inventory.enchants.ToolItemModifiers;
import necesse.level.maps.Level;

import java.awt.*;
import java.awt.geom.Point2D;

public class UnstableGelSword extends AphoreaSwordSecondaryChargeToolItem {

    public UnstableGelSword() {
        super(500, getChargeLevels(500, 2.0F, new Color(255, 255, 255), new Color(191, 102, 255)));
        rarity = Rarity.UNCOMMON;
        attackDamage.setBaseValue(20)
                .setUpgradedValue(1, 95);
        attackRange.setBaseValue(80);
        knockback.setBaseValue(20);
    }

    @Override
    public ListGameTooltips getPreEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getPreEnchantmentTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("itemtooltip", "unstablegelsword"));
        tooltips.add(Localization.translate("itemtooltip", "stikybuff1"));
        return tooltips;
    }

    @Override
    public InventoryItem onAttack(Level level, int x, int y, PlayerMob player, int attackHeight, InventoryItem item, PlayerInventorySlot slot, int animAttack, int seed, PacketReader contentReader) {
        return super.onAttack(level, x, y, player, attackHeight, item, slot, animAttack, seed, contentReader);
    }

    @Override
    public void hitMob(InventoryItem item, ToolItemEvent event, Level level, Mob target, Mob attacker) {
        super.hitMob(item, event, level, target, attacker);
        ActiveBuff buff = new ActiveBuff(BuffRegistry.getBuff("stickybuff"), target, 1000, event.mob);
        target.addBuff(buff, true);
    }

    @Override
    public void endChargeAttack(PlayerMob player, Point2D.Float dir, int charge) {
        super.endChargeAttack(player, dir, charge);
        launchrojectile(player, dir, charge);
    }

    private void launchrojectile(PlayerMob player, Point2D.Float dir, int charge) {
        float velocity = charge == 1 ? 300.0F : 200.0F;
        int distanceExtra = charge == 1 ? 7 : 3;
        GameDamage damage = this.getAttackDamage(player.attackingItem);
        float finalVelocity = (float)Math.round(this.getEnchantment(player.attackingItem).applyModifierLimited(ToolItemModifiers.VELOCITY, ToolItemModifiers.VELOCITY.defaultBuffManagerValue) * velocity * player.buffManager.getModifier(BuffModifiers.PROJECTILE_VELOCITY));
        Projectile projectile = new AircutProjectile("unstablegelsaber", player.getLevel(), player, player.x, player.y, player.x + dir.x * 100.0F, player.y + dir.y * 100.0F, finalVelocity, (int)((float)this.getAttackRange(player.attackingItem)) * distanceExtra, damage, 0);
        GameRandom random = new GameRandom(player.attackSeed);
        projectile.resetUniqueID(random);
        player.getLevel().entityManager.projectiles.addHidden(projectile);
        projectile.moveDist(20.0);
        if (player.isServer()) {
            player.getLevel().getServer().network.sendToClientsAtExcept(new PacketSpawnProjectile(projectile), player.getServerClient(), player.getServerClient());
        }

    }
}
