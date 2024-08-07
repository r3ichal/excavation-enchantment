package enchantments;

import net.minecraft.util.ActionResult;

public class MiningEventListener {

    public static void initialize() {
        MiningEventCallback.EVENT.register((player, world, pos, stack, level) -> {
            System.out.println("Block broken with Mining Enchantment at " + pos);
            return ActionResult.PASS;
        });
    }
}