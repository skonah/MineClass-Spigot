package net.babamod.mineclass.classes;

import net.babamod.mineclass.utils.Pair;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NagaClass {

    private static final Set<Material> forbiddenItems = new HashSet<Material>() {{
        add(Material.DIAMOND_SWORD);
        add(Material.GOLDEN_SWORD);
        add(Material.IRON_SWORD);
        add(Material.NETHERITE_SWORD);
        add(Material.DIAMOND_AXE);
        add(Material.GOLDEN_AXE);
        add(Material.IRON_AXE);
        add(Material.NETHERITE_AXE);
        add(Material.CROSSBOW);
        add(Material.BOW);
        add(Material.FLINT_AND_STEEL);
    }};

    private static final Map<PotionEffectType, Integer> potionEffects = Stream.of(new Object[][]{
            {PotionEffectType.DOLPHINS_GRACE, 1},
            {PotionEffectType.CONDUIT_POWER, 1},
            {PotionEffectType.WATER_BREATHING, 1},
            {PotionEffectType.SLOW, 2},
            {PotionEffectType.WEAKNESS, 1},
    }).collect(Collectors.toMap(data -> (PotionEffectType) data[0], data -> (Integer) data[1]));

    private static final Map<Material, List<Pair<Enchantment, Integer>>> classEnchantment = Stream.of(
            new AbstractMap.SimpleEntry<>(Material.TRIDENT, Arrays.asList(
                    new Pair<>(Enchantment.LOYALTY, 3),
                    new Pair<>(Enchantment.IMPALING, 5),
                    new Pair<>(Enchantment.CHANNELING, 1)
            )),
            new AbstractMap.SimpleEntry<>(Material.NETHERITE_HOE, Collections.singletonList(
                    new Pair<>(Enchantment.DAMAGE_ALL, 5)
            )),
            new AbstractMap.SimpleEntry<>(Material.DIAMOND_HOE, Collections.singletonList(
                    new Pair<>(Enchantment.DAMAGE_ALL, 5)
            )),
            new AbstractMap.SimpleEntry<>(Material.IRON_HOE, Collections.singletonList(
                    new Pair<>(Enchantment.DAMAGE_ALL, 5)
            )),
            new AbstractMap.SimpleEntry<>(Material.WOODEN_HOE, Collections.singletonList(
                    new Pair<>(Enchantment.DAMAGE_ALL, 5)
            )),
            new AbstractMap.SimpleEntry<>(Material.GOLDEN_HOE, Collections.singletonList(
                    new Pair<>(Enchantment.DAMAGE_ALL, 5)
            )),
            new AbstractMap.SimpleEntry<>(Material.STONE_HOE, Collections.singletonList(
                    new Pair<>(Enchantment.DAMAGE_ALL, 5)
            )),
            new AbstractMap.SimpleEntry<>(Material.FISHING_ROD, Arrays.asList(
                    new Pair<>(Enchantment.LUCK, 3),
                    new Pair<>(Enchantment.LURE, 3)
            ))
    ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    public static boolean is(Player player) {
        return player.getActivePotionEffects().stream().map(PotionEffect::getType).collect(Collectors.toList()).containsAll(potionEffects.keySet());
    }

    public static void reapplyEffects(Player player) {
        potionEffects.forEach((key, value) -> {
            player.removePotionEffect(key);
            player.addPotionEffect(new PotionEffect(key, Integer.MAX_VALUE, value - 1, false, false));
        });
    }

    public static boolean isItemForbidden(Material type) {
        return forbiddenItems.contains(type);
    }
}
