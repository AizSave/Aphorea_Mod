package aphorea.items.weapons.throwable;


import aphorea.other.utils.AphoreaCustomPushPacket;
import aphorea.other.vanillaitemtypes.weapons.AphoreaThrowToolItem;
import aphorea.projectiles.UnstableGelvelineProjectile;
import necesse.engine.localization.Localization;
import necesse.engine.network.PacketReader;
import necesse.engine.network.packet.PacketForceOfWind;
import necesse.engine.network.packet.PacketSpawnProjectile;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.registries.ProjectileRegistry;
import necesse.engine.util.GameBlackboard;
import necesse.engine.util.GameMath;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.projectile.Projectile;
import necesse.entity.projectile.modifiers.ResilienceOnHitProjectileModifier;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.PlayerInventorySlot;
import necesse.inventory.item.Item;
import necesse.inventory.item.toolItem.ToolItem;
import necesse.level.maps.Level;

import java.awt.geom.Point2D;

public class UnstableGelveline extends AphoreaThrowToolItem {
    public UnstableGelveline() {
        super(500);
        this.rarity = Item.Rarity.UNCOMMON;
        this.attackAnimTime.setBaseValue(250);
        this.itemCooldownTime.setBaseValue(1000);
        this.attackDamage.setBaseValue(20.0F).setUpgradedValue(1, 60.0F);
        this.velocity.setBaseValue(200);
        this.incinerationTimeMillis = 5000;
    }

    public InventoryItem onAttack(Level level, int x, int y, PlayerMob player, int attackHeight, InventoryItem item, PlayerInventorySlot slot, int animAttack, int seed, PacketReader contentReader) {
        int strength = 100;
        Point2D.Float dir = GameMath.normalize((float)x - player.x, (float)y - player.y);
        AphoreaCustomPushPacket.applyToPlayer(level, player, dir.x, dir.y, (float)strength);
        player.buffManager.addBuff(new ActiveBuff(BuffRegistry.FOW_ACTIVE, player, 0.2F, null), level.isServer());
        player.buffManager.forceUpdateBuffs();

        this.attackDamage.setBaseValue(20.0F).setUpgradedValue(1, 60.0F);
        Projectile projectile = new UnstableGelvelineProjectile(attackDamage, this, item, level, player, player.x, player.y, x, y, this.getProjectileVelocity(item, player), 1000, 0.02F, 100);
        projectile.setModifier(new ResilienceOnHitProjectileModifier(this.getResilienceGain(item)));
        projectile.resetUniqueID(new GameRandom((long)seed));
        projectile.moveDist(30.0);
        level.entityManager.projectiles.addHidden(projectile);
        if (level.isServer()) {
            level.getServer().network.sendToClientsWithEntityExcept(new PacketSpawnProjectile(projectile), projectile, player.getServerClient());
        }

        return item;
    }

    @Override
    public ListGameTooltips getPreEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getPreEnchantmentTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("itemtooltip", "stikybuff2"));
        return tooltips;
    }
}
