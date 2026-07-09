package net.claustra01.tfchammer.common.items;

import net.dries007.tfc.common.items.CreativeMiningTool;
import net.dries007.tfc.common.items.ToolItem;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;

public class SledgeItem extends ToolItem implements CreativeMiningTool {
    private final Tier tier;
    private final TagKey<Block> mineableBlocks;

    public SledgeItem(Tier tier, float attackDamage, float attackSpeed, TagKey<Block> mineableBlocks, Item.Properties properties) {
        super(tier, mineableBlocks, properties.attributes(ToolItem.productAttributes(tier, attackDamage, attackSpeed)));
        this.tier = tier;
        this.mineableBlocks = mineableBlocks;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return state.is(this.mineableBlocks) ? this.tier.getSpeed() * 0.4f : 1.0f;
    }

    @Override
    public void mineBlockInCreative(ItemStack stack, Level level, BlockState state, BlockPos pos, Player player) {
        this.doSledgeMining(stack, level, state, pos, player);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        this.doSledgeMining(stack, level, state, pos, entity);
        return super.mineBlock(stack, level, state, pos, entity);
    }

    private void doSledgeMining(ItemStack stack, Level level, BlockState originState, BlockPos origin, LivingEntity entity) {
        if (!(entity instanceof ServerPlayer player)) {
            return;
        }

        final BlockHitResult hitResult = (BlockHitResult) player.pick(20.0D, 0.0F, false);
        final Direction face = hitResult.getDirection();
        final BlockPos startPos;
        final BlockPos endPos;

        switch (face) {
            case UP, DOWN -> {
                startPos = origin.offset(-1, 0, -1);
                endPos = origin.offset(1, 0, 1);
            }
            case EAST, WEST -> {
                startPos = origin.offset(0, -1, -1);
                endPos = origin.offset(0, 1, 1);
            }
            case NORTH, SOUTH -> {
                startPos = origin.offset(-1, -1, 0);
                endPos = origin.offset(1, 1, 0);
            }
            default -> {
                startPos = origin.offset(-1, 0, -1);
                endPos = origin.offset(1, 0, 1);
            }
        }

        if (!this.isCorrectToolForDrops(stack, originState)) {
            return;
        }

        for (BlockPos pos : BlockPos.betweenClosed(startPos, endPos)) {
            if (pos.equals(origin)) {
                continue;
            }

            final BlockState targetState = level.getBlockState(pos);
            if (this.isCorrectToolForDrops(stack, targetState)) {
                this.breakExtraBlock(stack, level, pos, targetState, player);
            }

            if (stack.getDamageValue() >= stack.getMaxDamage()) {
                break;
            }
        }
    }

    private void breakExtraBlock(ItemStack stack, Level level, BlockPos pos, BlockState state, ServerPlayer player) {
        final BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, pos, state, player);
        if (NeoForge.EVENT_BUS.post(event).isCanceled()) {
            return;
        }

        if (state.hasProperty(BlockStateProperties.LAYERS)) {
            final int layers = state.getValue(BlockStateProperties.LAYERS);
            if (layers > 1) {
                level.setBlock(pos, state.setValue(BlockStateProperties.LAYERS, layers - 1), 3);
            } else {
                level.destroyBlock(pos, false, player);
            }
            this.dropResourcesAndDamage(state, pos, player, stack, level);
        } else {
            this.dropResourcesAndDamage(state, pos, player, stack, level);
            level.destroyBlock(pos, false, player);
        }
    }

    private void dropResourcesAndDamage(BlockState state, BlockPos pos, Player player, ItemStack stack, Level level) {
        if (player.isCreative()) {
            return;
        }

        Block.dropResources(state, level, pos, state.hasBlockEntity() ? level.getBlockEntity(pos) : null, player, stack);
        Helpers.damageItem(stack, 1, player, player.getUsedItemHand());
    }
}
