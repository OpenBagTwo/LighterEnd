package io.github.openbagtwo.lighterend.blocks;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.blocks.entities.SilkMothNestEntity;
import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import java.util.List;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.FireBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

public class SilkMothNest extends BlockWithEntity {

  public static final MapCodec<SilkMothNest> CODEC = createCodec(
      SilkMothNest::new);
  public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;
  public static final IntProperty FULLNESS = IntProperty.of("fullness", 0, 3);


  @Override
  public MapCodec<SilkMothNest> getCodec() {
    return CODEC;
  }

  public SilkMothNest(AbstractBlock.Settings settings) {
    super(
        settings
            .mapColor(MapColor.TERRACOTTA_YELLOW)
            .instrument(NoteBlockInstrument.BASS)
            .strength(0.3F)
            .sounds(BlockSoundGroup.WOOD)
            .nonOpaque()
            .burnable()
    );
    this.setDefaultState(
        this.stateManager.getDefaultState().with(FULLNESS, 0).with(FACING, Direction.NORTH));
  }

  @Override
  protected boolean hasComparatorOutput(BlockState state) {
    return true;
  }

  @Override
  protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
    return state.get(FULLNESS);
  }

  @Override
  public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state,
      @Nullable BlockEntity blockEntity, ItemStack tool) {
    super.afterBreak(world, player, pos, state, blockEntity, tool);
    if (!world.isClient && blockEntity instanceof SilkMothNestEntity nestEntity) {
      if (!EnchantmentHelper.hasAnyEnchantmentsIn(tool,
          EnchantmentTags.PREVENTS_BEE_SPAWNS_WHEN_MINING)) {
        ItemScatterer.onStateReplaced(state, world, pos);
        nestEntity.tryReleaseMoths(state);
      }
    }
  }


  public static void dropSilk(World world, BlockPos pos) {
    dropStack(world, pos, new ItemStack(LighterEndItems.SILK, 3));
  }

  @Override
  protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos,
      PlayerEntity player, Hand hand, BlockHitResult hit) {
    int i = state.get(FULLNESS);
    boolean bl = false;
    if (i >= 3) {
      Item item = stack.getItem();
      if (stack.isOf(Items.SHEARS)) {
        world.playSound(player, player.getX(), player.getY(), player.getZ(),
            SoundEvents.BLOCK_BEEHIVE_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
        dropSilk(world, pos);
        stack.damage(1, player, LivingEntity.getSlotForHand(hand));
        bl = true;
        world.emitGameEvent(player, GameEvent.SHEAR, pos);
      }

      if (!world.isClient() && bl) {
        player.incrementStat(Stats.USED.getOrCreateStat(item));
      }
    }

    if (bl) {

      this.takeSilk(world, state, pos);

      return ActionResult.SUCCESS;
    } else {
      return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }
  }

  public void takeSilk(World world, BlockState state, BlockPos pos) {
    world.setBlockState(pos, state.with(FULLNESS, 0), Block.NOTIFY_ALL);
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    builder.add(FULLNESS, FACING);
  }

  @Nullable
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new SilkMothNestEntity(pos, state);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state,
      BlockEntityType<T> type) {
    return world.isClient ? null
        : validateTicker(
            type,
            LighterEndBlockEntities.SILK_MOTH_NEST,
            SilkMothNestEntity::serverTick
        );
  }

  @Override
  public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
    if (world instanceof ServerWorld serverWorld
        && player.shouldSkipBlockDrops()
        && serverWorld.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)
        && world.getBlockEntity(pos) instanceof SilkMothNestEntity nestEntity) {
      int i = state.get(FULLNESS);
      boolean bl = nestEntity.getOccupancy() > 0;
      if (bl || i > 0) {
        ItemStack itemStack = new ItemStack(this);
        itemStack.applyComponentsFrom(nestEntity.createComponentMap());
        itemStack.set(
            DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT.with(FULLNESS, i));
        ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
            itemStack);
        itemEntity.setToDefaultPickupDelay();
        world.spawnEntity(itemEntity);
      }
    }

    return super.onBreak(world, pos, state, player);
  }

  @Override
  protected List<ItemStack> getDroppedStacks(BlockState state, LootWorldContext.Builder builder) {
    Entity entity = builder.getOptional(LootContextParameters.THIS_ENTITY);
    if (entity instanceof TntEntity
        || entity instanceof CreeperEntity
        || entity instanceof WitherSkullEntity
        || entity instanceof WitherEntity
        || entity instanceof TntMinecartEntity) {
      BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
      if (blockEntity instanceof SilkMothNestEntity nestEntity) {
        nestEntity.tryReleaseMoths(state);
      }
    }

    return super.getDroppedStacks(state, builder);
  }

  @Override
  protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state,
      boolean includeData) {
    ItemStack itemStack = super.getPickStack(world, pos, state, includeData);
    if (includeData) {
      itemStack.set(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT.with(FULLNESS,
          state.get(FULLNESS)));
    }

    return itemStack;
  }

  @Override
  protected BlockState getStateForNeighborUpdate(
      BlockState state,
      WorldView world,
      ScheduledTickView tickView,
      BlockPos pos,
      Direction direction,
      BlockPos neighborPos,
      BlockState neighborState,
      Random random
  ) {
    if (world.getBlockState(neighborPos).getBlock() instanceof FireBlock && world.getBlockEntity(
        pos) instanceof SilkMothNestEntity nestEntity) {
      nestEntity.tryReleaseMoths(state);
    }

    return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos,
        neighborState, random);
  }

  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return state.with(FACING, rotation.rotate(state.get(FACING)));
  }

  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return state.rotate(mirror.getRotation(state.get(FACING)));
  }
}
