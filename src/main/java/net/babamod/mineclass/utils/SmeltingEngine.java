package net.babamod.mineclass.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.Iterator;

public class SmeltingEngine {

  public static ItemStack smelt(ItemStack itemStack) {
    ItemStack result = null;
    Iterator<Recipe> iter = Bukkit.recipeIterator();
    while (iter.hasNext()) {
      Recipe recipe = iter.next();
      if (!(recipe instanceof FurnaceRecipe)) continue;
      if (((FurnaceRecipe) recipe).getInput().getType() != itemStack.getType()) continue;
      result = recipe.getResult();
      break;
    }
    return result;
  }
}
