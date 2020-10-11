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

public class FireDwarfClass {

  private static final Set<Material> forbiddenItems =
      new HashSet<Material>() {
        {
          add(Material.DIAMOND_SWORD);
          add(Material.GOLDEN_SWORD);
          add(Material.IRON_SWORD);
          add(Material.NETHERITE_SWORD);
          add(Material.DIAMOND_HOE);
          add(Material.GOLDEN_HOE);
          add(Material.IRON_HOE);
          add(Material.NETHERITE_HOE);
          add(Material.DIAMOND_SHOVEL);
          add(Material.GOLDEN_SHOVEL);
          add(Material.IRON_SHOVEL);
          add(Material.NETHERITE_SHOVEL);
          add(Material.BOW);
          add(Material.TRIDENT);
        }
      };

  private static final Map<PotionEffectType, Integer> potionEffects =
      Stream.of(
              new Object[][] {
                {PotionEffectType.FIRE_RESISTANCE, 1},
                {PotionEffectType.FAST_DIGGING, 1},
                {PotionEffectType.JUMP, 2},
                {PotionEffectType.NIGHT_VISION, 1},
                {PotionEffectType.HEALTH_BOOST, 2},
              })
          .collect(Collectors.toMap(data -> (PotionEffectType) data[0], data -> (Integer) data[1]));

  private static final Map<Material, List<Pair<Enchantment, Integer>>> classEnchantments =
      Stream.of(
              new AbstractMap.SimpleEntry<>(
                  Material.NETHERITE_AXE,
                  Collections.singletonList(new Pair<>(Enchantment.FIRE_ASPECT, 2))),
              new AbstractMap.SimpleEntry<>(
                  Material.DIAMOND_AXE,
                  Collections.singletonList(new Pair<>(Enchantment.FIRE_ASPECT, 2))),
              new AbstractMap.SimpleEntry<>(
                  Material.IRON_AXE,
                  Collections.singletonList(new Pair<>(Enchantment.FIRE_ASPECT, 2))),
              new AbstractMap.SimpleEntry<>(
                  Material.GOLDEN_AXE,
                  Collections.singletonList(new Pair<>(Enchantment.FIRE_ASPECT, 2))),
              new AbstractMap.SimpleEntry<>(
                  Material.STONE_AXE,
                  Collections.singletonList(new Pair<>(Enchantment.FIRE_ASPECT, 2))),
              new AbstractMap.SimpleEntry<>(
                  Material.WOODEN_AXE,
                  Collections.singletonList(new Pair<>(Enchantment.FIRE_ASPECT, 2))),
              new AbstractMap.SimpleEntry<>(
                  Material.NETHERITE_PICKAXE,
                  Collections.singletonList(new Pair<>(Enchantment.DIG_SPEED, 5))),
              new AbstractMap.SimpleEntry<>(
                  Material.DIAMOND_PICKAXE,
                  Collections.singletonList(new Pair<>(Enchantment.DIG_SPEED, 5))),
              new AbstractMap.SimpleEntry<>(
                  Material.IRON_PICKAXE,
                  Collections.singletonList(new Pair<>(Enchantment.DIG_SPEED, 5))),
              new AbstractMap.SimpleEntry<>(
                  Material.GOLDEN_PICKAXE,
                  Collections.singletonList(new Pair<>(Enchantment.DIG_SPEED, 5))),
              new AbstractMap.SimpleEntry<>(
                  Material.STONE_PICKAXE,
                  Collections.singletonList(new Pair<>(Enchantment.DIG_SPEED, 5))),
              new AbstractMap.SimpleEntry<>(
                  Material.WOODEN_PICKAXE,
                  Collections.singletonList(new Pair<>(Enchantment.DIG_SPEED, 5))),
              new AbstractMap.SimpleEntry<>(
                  Material.CROSSBOW,
                  Collections.singletonList(new Pair<>(Enchantment.ARROW_INFINITE, 1))),
              new AbstractMap.SimpleEntry<>(
                  Material.FLINT_AND_STEEL, new ArrayList<Pair<Enchantment, Integer>>())
              // See to make infinity working on crossbow
              // Inventory auto smelt
              )
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
