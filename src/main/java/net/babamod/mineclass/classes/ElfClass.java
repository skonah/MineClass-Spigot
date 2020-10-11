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

public class ElfClass {

  private static final Set<Material> forbiddenItems =
      new HashSet<Material>() {
        {
          add(Material.DIAMOND_SWORD);
          add(Material.GOLDEN_SWORD);
          add(Material.IRON_SWORD);
          add(Material.NETHERITE_SWORD);
          add(Material.DIAMOND_PICKAXE);
          add(Material.GOLDEN_PICKAXE);
          add(Material.IRON_PICKAXE);
          add(Material.NETHERITE_PICKAXE);
          add(Material.DIAMOND_SHOVEL);
          add(Material.GOLDEN_SHOVEL);
          add(Material.IRON_SHOVEL);
          add(Material.NETHERITE_SHOVEL);
          add(Material.CROSSBOW);
          add(Material.TRIDENT);
        }
      };

  private static final Map<PotionEffectType, Integer> potionEffects =
      Stream.of(
              new Object[][] {
                {PotionEffectType.SPEED, 2},
                {PotionEffectType.JUMP, 3},
                {PotionEffectType.LUCK, 1},
                {PotionEffectType.NIGHT_VISION, 1},
              })
          .collect(Collectors.toMap(data -> (PotionEffectType) data[0], data -> (Integer) data[1]));

  private static final Map<Material, List<Pair<Enchantment, Integer>>> classEnchantments =
      Stream.of(
              new AbstractMap.SimpleEntry<>(
                  Material.BOW,
                  Arrays.asList(
                      new Pair<>(Enchantment.ARROW_INFINITE, 1),
                      new Pair<>(Enchantment.ARROW_DAMAGE, 8))))
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
