package aphorea.items.weapons.magic;

import aphorea.other.olditemtype.AphoreaToolItem;
import necesse.engine.localization.Localization;
import necesse.engine.network.NetworkPacket;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.client.Client;
import necesse.engine.network.client.ClientClient;
import necesse.engine.network.packet.PacketRequestPlayerData;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.registries.DamageTypeRegistry;
import necesse.engine.registries.EnchantmentRegistry;
import necesse.engine.util.GameMath;
import necesse.engine.util.GameRandom;
import necesse.engine.util.LineHitbox;
import necesse.entity.levelEvent.toolItemEvent.ToolItemEvent;
import necesse.entity.mobs.AttackAnimMob;
import necesse.entity.mobs.Attacker;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.gfx.drawOptions.itemAttack.ItemAttackDrawOptions;
import necesse.inventory.InventoryItem;
import necesse.inventory.PlayerInventorySlot;
import necesse.inventory.enchants.Enchantable;
import necesse.inventory.enchants.ItemEnchantment;
import necesse.inventory.enchants.ToolItemEnchantment;
import necesse.inventory.item.ItemCategory;
import necesse.inventory.item.ItemStatTipList;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Set;
import java.util.function.Function;

public class MagicalBroom extends AphoreaToolItem {

    int currentA;

    public MagicalBroom() {
        super(500);
        this.setItemCategory("equipment", "weapons", "magicweapons");
        this.setItemCategory(ItemCategory.equipmentManager, "weapons", "magicweapons");
        this.keyWords.add("broom");
        damageType = DamageTypeRegistry.MAGIC;
        this.width = 15.0F;
        this.showAttackAllDirections = true;
        this.resilienceGain.setBaseValue(2.0F);

        rarity = Rarity.RARE;
        attackAnimTime.setBaseValue(300);
        attackDamage.setBaseValue(26)
                .setUpgradedValue(1, 120);
        attackRange.setBaseValue(160);
        knockback.setBaseValue(250);
        manaCost.setBaseValue(1.0F);

        attackYOffset = 155; // 140
        attackXOffset = 30; // 30

        currentA = 1;
    }

    public void addStatTooltips(ItemStatTipList list, InventoryItem currentItem, InventoryItem lastItem, Mob perspective, boolean forceAdd) {
        this.addAttackDamageTip(list, currentItem, lastItem, perspective, forceAdd);
        this.addAttackSpeedTip(list, currentItem, lastItem, perspective);
        this.addResilienceGainTip(list, currentItem, lastItem, perspective, forceAdd);
        this.addKnockbackTip(list, currentItem, lastItem, perspective);
        this.addCritChanceTip(list, currentItem, lastItem, perspective, forceAdd);
        this.addManaCostTip(list, currentItem, lastItem, perspective);
    }

    public int getFlatItemCooldownTime(InventoryItem item) {
        return (int)((float)this.getFlatAttackAnimTime(item) * 1.5F);
    }

    @Override
    public ItemAttackDrawOptions setupItemSpriteAttackDrawOptions(ItemAttackDrawOptions options, InventoryItem item, PlayerMob player, int mobDir, float attackDirX, float attackDirY, float attackProgress, Color itemColor, GameLight light) {

        int n;
        if(attackProgress < 0.3F) {
            n = currentA == 0 ? 9 : 4;
        } else if(attackProgress < 0.45F) {
            n = currentA == 0 ? 8 : 3;
        } else if(attackProgress < 0.55F) {
            n = currentA == 0 ? 7 : 2;
        } else if(attackProgress < 0.7F) {
            n = currentA == 0 ? 6 : 1;
        } else {
            n = currentA == 0 ? 5 : 0;
        }

        ItemAttackDrawOptions.AttackItemSprite itemSprite = options.itemSprite(attackTexture, n, 0, 320);

        int dir = player.getDir();
        itemSprite.itemRotatePoint(dir == 2 ? this.attackXOffset + 25: this.attackXOffset, dir == 2 ? this.attackYOffset - 5 : this.attackYOffset);
        if (itemColor != null) {
            itemSprite.itemColor(itemColor);
        }

        if(dir == 0 || dir == 2) {
            itemSprite.itemRotateOffset(-45);
        }

        return itemSprite.itemEnd();
    }

    @Override
    public void setDrawAttackRotation(InventoryItem item, ItemAttackDrawOptions drawOptions, float attackDirX, float attackDirY, float attackProgress) {
    }

    public float getHitboxSwingAngle(InventoryItem item, int dir) {
        return 180.0F;
    }

    public float getHitboxSwingAngleOffset(InventoryItem item, int dir, float swingAngle) {
        return 0.0F;
    }

    public Function<Float, Float> getSwingDirection(InventoryItem item, AttackAnimMob mob) {
        int attackDir = mob.getDir();
        float animSwingAngle = this.getHitboxSwingAngle(item, attackDir);
        float animSwingAngleOffset = this.getHitboxSwingAngleOffset(item, attackDir, animSwingAngle);
        Function<Float, Float> angleGetter;
        if (attackDir == 0) {
            if (this.getAnimInverted(item)) {
                angleGetter = (progress) -> -progress * animSwingAngle - animSwingAngleOffset;
            } else {
                angleGetter = (progress) -> 180.0F + progress * animSwingAngle + animSwingAngleOffset;
            }
        } else if (attackDir == 1) {
            if (this.getAnimInverted(item)) {
                angleGetter = (progress) -> 90.0F - progress * animSwingAngle - animSwingAngleOffset;
            } else {
                angleGetter = (progress) -> 270.0F + progress * animSwingAngle + animSwingAngleOffset;
            }
        } else if (attackDir == 2) {
            if (this.getAnimInverted(item)) {
                angleGetter = (progress) -> 180.0F - progress * animSwingAngle - animSwingAngleOffset;
            } else {
                angleGetter = (progress) -> progress * animSwingAngle + animSwingAngleOffset;
            }
        } else if (this.getAnimInverted(item)) {
            angleGetter = (progress) -> 90.0F + progress * animSwingAngle + animSwingAngleOffset;
        } else {
            angleGetter = (progress) -> 270.0F - progress * animSwingAngle - animSwingAngleOffset;
        }

        return angleGetter;
    }

    public ArrayList<Shape> getHitboxes(InventoryItem item, AttackAnimMob mob, int aimX, int aimY, ToolItemEvent event, boolean forDebug) {
        ArrayList<Shape> out = new ArrayList();
        int attackRange = this.getAttackRange(item);
        float lastProgress = event.lastHitboxProgress;
        float nextProgress = mob.getAttackAnimProgress();
        float circumference = (float)(Math.PI * (double)attackRange);
        float percPerWidth = Math.max(10.0F, this.width) / circumference;
        Point2D.Float base = new Point2D.Float(mob.x, mob.y);
        int attackDir = mob.getDir();
        if (attackDir == 0) {
            base.x += 8.0F;
        } else if (attackDir == 2) {
            base.x -= 8.0F;
        }

        for(float progress = lastProgress; progress <= nextProgress; progress += percPerWidth) {
            float angle = (Float)this.getSwingDirection(item, mob).apply(progress);
            Point2D.Float dir = GameMath.getAngleDir(angle);
            Line2D.Float attackLine = new Line2D.Float(base.x, base.y, dir.x * (float)attackRange + mob.x, dir.y * (float)attackRange + mob.y);
            if (this.width > 0.0F) {
                out.add(new LineHitbox(attackLine, this.width));
            } else {
                out.add(attackLine);
            }

            if (!forDebug) {
                event.lastHitboxProgress = progress;
            }
        }

        return out;
    }

    public InventoryItem onAttack(Level level, int x, int y, PlayerMob player, int attackHeight, InventoryItem item, PlayerInventorySlot slot, int animAttack, int seed, PacketReader contentReader) {
        if (animAttack == 0) {
            int strength = 50;
            Point2D.Float dir = GameMath.normalize((float)x - player.x, (float)y - player.y);
            PacketMagicalBroom.applyToPlayer(level, player, dir.x, dir.y, (float)strength);
            player.buffManager.addBuff(new ActiveBuff(BuffRegistry.FOW_ACTIVE, player, 0.15F, (Attacker)null), level.isServer());
            player.buffManager.forceUpdateBuffs();

            if(player.isServer()) {
                ServerClient serverClient = player.getServerClient();
                level.getServer().network.sendToClientsAtExcept(new PacketMagicalBroom(serverClient.slot, dir.x, dir.y, (float)strength), serverClient, serverClient);
            } else if(player.isClient()) {
                currentA = currentA == 0 ? 1 : 0;
                animInverted = currentA == 1;
            }

            int animTime = this.getAttackAnimTime(item, player);
            int aimX = x - player.getX();
            int aimY = y - player.getY() + attackHeight;
            ToolItemEvent event = new ToolItemEvent(player, seed, item, aimX, aimY, animTime, animTime);
            level.entityManager.addLevelEventHidden(event);

            this.consumeMana(player, item);
        }

        return item;
    }

    public ToolItemEnchantment getRandomEnchantment(GameRandom random, InventoryItem item) {
        return (ToolItemEnchantment) Enchantable.getRandomEnchantment(random, EnchantmentRegistry.meleeItemEnchantments, this.getEnchantmentID(item), ToolItemEnchantment.class);
    }

    public boolean isValidEnchantment(InventoryItem item, ItemEnchantment enchantment) {
        return EnchantmentRegistry.meleeItemEnchantments.contains(enchantment.getID());
    }

    public Set<Integer> getValidEnchantmentIDs(InventoryItem item) {
        return EnchantmentRegistry.meleeItemEnchantments;
    }

    public String getTranslatedTypeName() {
        return Localization.translate("item", "broom");
    }

    public static class PacketMagicalBroom extends Packet {
        public final int slot;
        public final float dirX;
        public final float dirY;
        public final float strength;

        public PacketMagicalBroom(byte[] data) {
            super(data);
            PacketReader reader = new PacketReader(this);
            this.slot = reader.getNextByteUnsigned();
            this.dirX = reader.getNextFloat();
            this.dirY = reader.getNextFloat();
            this.strength = reader.getNextFloat();
        }

        public PacketMagicalBroom(int slot, float dirX, float dirY, float strength) {
            this.slot = slot;
            this.dirX = dirX;
            this.dirY = dirY;
            this.strength = strength;
            PacketWriter writer = new PacketWriter(this);
            writer.putNextByteUnsigned(slot);
            writer.putNextFloat(dirX);
            writer.putNextFloat(dirY);
            writer.putNextFloat(strength);
        }

        public void processClient(NetworkPacket packet, Client client) {
            if (client.getLevel() != null) {
                ClientClient target = client.getClient(this.slot);
                if (target != null && target.isSamePlace(client.getLevel())) {
                    applyToPlayer(target.playerMob.getLevel(), target.playerMob, this.dirX, this.dirY, this.strength);
                } else {
                    client.network.sendPacket(new PacketRequestPlayerData(this.slot));
                }

            }
        }

        public static void applyToPlayer(Level level, Mob mob, float dirX, float dirY, float strength) {
            float forceX = dirX * strength;
            float forceY = dirY * strength;
            if (Math.abs(mob.dx) < Math.abs(forceX)) {
                mob.dx = forceX;
            }

            if (Math.abs(mob.dy) < Math.abs(forceY)) {
                mob.dy = forceY;
            }
        }
    }


}
