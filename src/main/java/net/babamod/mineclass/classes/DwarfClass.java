package net.babamod.mineclass.classes;

import net.babamod.mineclass.utils.Pair;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DwarfClass {

  private static final Set<Material> forbiddenItems =
      new HashSet<Material>() {
        {
          add(Material.DIAMOND_AXE);
          add(Material.GOLDEN_AXE);
          add(Material.IRON_AXE);
          add(Material.NETHERITE_AXE);
          add(Material.DIAMOND_HOE);
          add(Material.GOLDEN_HOE);
          add(Material.IRON_HOE);
          add(Material.NETHERITE_HOE);
          add(Material.BOW);
          add(Material.TRIDENT);
        }
      };

  private static final Map<PotionEffectType, Integer> potionEffects =
      Stream.of(
              new Object[][] {
                {PotionEffectType.HEALTH_BOOST, 2},
                {PotionEffectType.DAMAGE_RESISTANCE, 1},
                {PotionEffectType.HERO_OF_THE_VILLAGE, 1},
                {PotionEffectType.FAST_DIGGING, 1},
                {PotionEffectType.NIGHT_VISION, 1},
              })
          .collect(Collectors.toMap(data -> (PotionEffectType) data[0], data -> (Integer) data[1]));

  private static final Map<Material, List<Pair<Enchantment, Integer>>> classEnchantments =
      Stream.of(
              new AbstractMap.SimpleEntry<>(
                  Material.NETHERITE_PICKAXE,
                  Arrays.asList(
                      new Pair<>(Enchantment.DIG_SPEED, 8),
                      new Pair<>(Enchantment.LOOT_BONUS_BLOCKS, 2))),
              new AbstractMap.SimpleEntry<>(
                  Material.DIAMOND_PICKAXE,
                  Arrays.asList(
                      new Pair<>(Enchantment.DIG_SPEED, 8),
                      new Pair<>(Enchantment.LOOT_BONUS_BLOCKS, 2))),
              new AbstractMap.SimpleEntry<>(
                  Material.IRON_PICKAXE,
                  Arrays.asList(
                      new Pair<>(Enchantment.DIG_SPEED, 8),
                      new Pair<>(Enchantment.LOOT_BONUS_BLOCKS, 2))),
              new AbstractMap.SimpleEntry<>(
                  Material.GOLDEN_PICKAXE,
                  Arrays.asList(
                      new Pair<>(Enchantment.DIG_SPEED, 8),
                      new Pair<>(Enchantment.LOOT_BONUS_BLOCKS, 2))),
              new AbstractMap.SimpleEntry<>(
                  Material.STONE_PICKAXE,
                  Arrays.asList(
                      new Pair<>(Enchantment.DIG_SPEED, 8),
                      new Pair<>(Enchantment.LOOT_BONUS_BLOCKS, 2))),
              new AbstractMap.SimpleEntry<>(
                  Material.WOODEN_PICKAXE,
                  Arrays.asList(
                      new Pair<>(Enchantment.DIG_SPEED, 8),
                      new Pair<>(Enchantment.LOOT_BONUS_BLOCKS, 2))))
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

  public static boolean is(Player player) {
    return player.getActivePotionEffects().stream()
        .map(PotionEffect::getType)
        .collect(Collectors.toList())
        .containsAll(potionEffects.keySet());
  }

  public static void reapplyEffects(Player player) {
    potionEffects.forEach(
        (key, value) -> {
          if (player.hasPotionEffect(key)) {
            player.removePotionEffect(key);
          }
          player.addPotionEffect(new PotionEffect(key, Integer.MAX_VALUE, value - 1, false, false));
        });
  }

  public static boolean isItemForbidden(Material type) {
    return forbiddenItems.contains(type);
  }

  public static boolean isItemEnchantable(Material type) {
    return classEnchantments.containsKey(type);
  }

  public static void enchantItem(ItemStack itemStack) {
    classEnchantments
        .getOrDefault(itemStack.getType(), new ArrayList<>())
        .forEach(
            enchantmentIntegerPair ->
                itemStack.addUnsafeEnchantment(
                    enchantmentIntegerPair.getFirst(), enchantmentIntegerPair.getSecond()));
  }
}
