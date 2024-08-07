package excavation_enchantment.mod;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import enchantments.init.ModEnchantments;

public class ExcavationEnchantment implements ModInitializer {
	public static final String MOD_ID = "excavation_enchantment";
	public static final Logger LOGGER = LoggerFactory.getLogger("excavation-enchantment");

	@Override
	public void onInitialize() {
		ModEnchantments.registerModEnchantments();
	}
}