package enchantments.init;

import excavation_enchantment.mod.ExcavationEnchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import enchantments.MiningEnchantment;

public class ModEnchantments {
  public static Enchantment MINING = register("mining",
      new MiningEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.DIGGER, EquipmentSlot.MAINHAND));

  private static Enchantment register(String name, Enchantment enchantment) {
    return Registry.register(Registries.ENCHANTMENT, new Identifier(ExcavationEnchantment.MOD_ID, name), enchantment);
  }

  public static void registerModEnchantments() {
    ExcavationEnchantment.LOGGER.info("Registering ModEnchantments for " + ExcavationEnchantment.MOD_ID);
  }
}