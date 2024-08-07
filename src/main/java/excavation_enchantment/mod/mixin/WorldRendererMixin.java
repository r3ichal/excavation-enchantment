package excavation_enchantment.mod.mixin;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BlockBreakingInfo;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import enchantments.MiningEnchantment;
import utils.BlockFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

@Mixin(WorldRenderer.class)
@Environment(EnvType.CLIENT)
public class WorldRendererMixin {

    @Shadow
    @Final
    private MinecraftClient client;
    @Shadow
    private ClientWorld world;
    @Shadow
    @Final
    private Long2ObjectMap<SortedSet<BlockBreakingInfo>> blockBreakingProgressions;

    @Inject(at = @At("HEAD"), method = "drawBlockOutline", cancellable = true)
    private void drawBlockOutline(MatrixStack stack, VertexConsumer vertexConsumer, Entity entity, double d, double e,
            double f, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        if (this.client.player == null) {
            return;
        }
        if (this.client.world == null) {
            return;
        }
        ItemStack heldStack = this.client.player.getInventory().getMainHandStack();
        if (heldStack == null || heldStack.isEmpty())
            return;
        NbtList nbtList = heldStack.getEnchantments();
        for (int i = 0; i < nbtList.size(); i++) {
            NbtCompound compound = nbtList.getCompound(i);
            Registries.ENCHANTMENT.getOrEmpty(EnchantmentHelper.getIdFromNbt(compound)).ifPresent(enchantment -> {
                if (enchantment instanceof MiningEnchantment) {

                    if (client.crosshairTarget instanceof BlockHitResult crosshairTarget) {
                        BlockPos crosshairPos = crosshairTarget.getBlockPos();
                        BlockState crosshairState = client.world.getBlockState(crosshairPos);

                        if (!crosshairState.isAir() && client.world.getWorldBorder().contains(crosshairPos)) {

                            BlockHitResult blockHitResult = (BlockHitResult) this.client.player.raycast(4.5f, 1, false);
                            var facing = blockHitResult.getSide().getOpposite();
                            Iterable<BlockPos> positions = this.getBlockFinder().findBlocks(facing,
                                    blockHitResult.getBlockPos(), 0);

                            List<VoxelShape> outlineShapes = new ArrayList<>();
                            outlineShapes.add(VoxelShapes.empty());

                            for (BlockPos position : positions) {
                                BlockPos diffPos = position.subtract(crosshairPos);
                                BlockState offsetShape = world.getBlockState(position);

                                if (!offsetShape.isAir()) {
                                    outlineShapes.set(0, VoxelShapes.union(outlineShapes.get(0), VoxelShapes.fullCube()
                                            .offset(diffPos.getX(), diffPos.getY(), diffPos.getZ())));
                                }
                            }

                            outlineShapes.forEach(shape -> {
                                drawCuboidShapeOutline(
                                        stack,
                                        vertexConsumer,
                                        shape,
                                        (double) crosshairPos.getX() - d,
                                        (double) crosshairPos.getY() - e,
                                        (double) crosshairPos.getZ() - f,
                                        0.0F,
                                        0.0F,
                                        0.0F,
                                        0.4F);
                            });
                            ci.cancel();
                        }
                    }
                }
            });
        }
    }

    public BlockFinder getBlockFinder() {
        return BlockFinder.DEFAULT;
    }

    @Invoker("drawCuboidShapeOutline")
    public static void drawCuboidShapeOutline(MatrixStack matrices, VertexConsumer vertexConsumer, VoxelShape shape,
            double offsetX, double offsetY, double offsetZ, float red, float green, float blue, float alpha) {
        throw new AssertionError();
    }
}