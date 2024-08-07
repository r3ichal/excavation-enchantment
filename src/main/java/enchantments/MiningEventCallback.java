package enchantments;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@FunctionalInterface
public interface MiningEventCallback {
    
    Event<MiningEventCallback> EVENT = EventFactory.createArrayBacked(MiningEventCallback.class,
        (listeners) -> (player, world, pos, stack, level) -> {
            for (MiningEventCallback listener : listeners) {
                ActionResult result = listener.onBlockBreak(player, world, pos, stack, level);
                System.out.println("BLOCK BROKEN SJDJISAIHDHUSCBO");
                if (result != ActionResult.PASS) {
                    return result;
                }
            }
            return ActionResult.PASS;
        });

    ActionResult onBlockBreak(PlayerEntity player, World world, BlockPos pos, ItemStack stack, int level);
}