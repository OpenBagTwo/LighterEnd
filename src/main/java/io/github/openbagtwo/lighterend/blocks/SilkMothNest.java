package io.github.openbagtwo.lighterend.blocks;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.blocks.entities.SilkMothNestEntity;
import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndData;
import io.github.openbagtwo.lighterend.registries.LighterEndData.SilkLevelComponent;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import io.github.openbagtwo.lighterend.utils.Flags;
import io.github.openbagtwo.lighterend.utils.GlobalState;
import io.github.openbagtwo.lighterend.utils.PosInfo;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SilkMothNest extends BaseEntityBlock {

  public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
  public static final int MAX_FULLNESS = 3;
  public static final IntegerProperty FULLNESS = IntegerProperty.create("fullness", 0,
      MAX_FULLNESS);

  public static final VoxelShape OUTLINE_SHAPE = Shapes.or(
      Block.box(0, 0, 0, 16, 13, 16),
      Block.box(3, 12, 3, 13, 16, 13)
  );

  public SilkMothNest(BlockBehaviour.Properties settings) {
    super(
        settings
            .mapColor(MapColor.TERRACOTTA_YELLOW)
            .instrument(NoteBlockInstrument.BASS)
            .strength(0.3F)
            .sound(SoundType.WOOD)
            .noOcclusion()
            .ignitedByLava()
    );
    this.registerDefaultState(
        this.stateDefinition.any().setValue(FULLNESS, 0).setValue(FACING, Direction.NORTH));
  }

  @Override
  protected boolean hasAnalogOutputSignal(BlockState state) {
    return true;
  }

  @Override
  protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos, Direction dir) {
    return state.getValue(FULLNESS);
  }

  @Override
  public void playerDestroy(
      final ServerLevel world,
      final ServerPlayer player,
      final BlockPos pos,
      final BlockState state,
      final @org.jspecify.annotations.Nullable BlockEntity blockEntity,
      final ItemStack tool
  ) {
    super.playerDestroy(world, player, pos, state, blockEntity, tool);
    if (!world.isClientSide() && blockEntity instanceof SilkMothNestEntity nestEntity) {
      if (!EnchantmentHelper.hasTag(
          tool, EnchantmentTags.PREVENTS_BEE_SPAWNS_WHEN_MINING
      )) {
        nestEntity.tryReleaseMoths(state);
        Containers.updateNeighboursAfterDestroy(state, world, pos);

      }
    }
  }

  public static void dropSilk(Level world, BlockPos pos) {
    popResource(world, pos, new ItemStack(LighterEndItems.SILK, 3));
  }

  @Override
  protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world,
      BlockPos pos,
      Player player, InteractionHand hand, BlockHitResult hit) {
    int i = state.getValue(FULLNESS);
    boolean bl = false;
    if (i >= MAX_FULLNESS) {
      Item item = stack.getItem();
      if (stack.is(Items.SHEARS)) {
        world.playSound(player, player.getX(), player.getY(), player.getZ(),
            LighterEndSounds.MOTH_NEST_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
        dropSilk(world, pos);
        stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
        bl = true;
        world.gameEvent(player, GameEvent.SHEAR, pos);
      }

      if (!world.isClientSide() && bl) {
        player.awardStat(Stats.ITEM_USED.get(item));
      }
    }

    if (bl) {

      this.takeSilk(world, state, pos);

      return InteractionResult.SUCCESS;
    } else {
      return super.useItemOn(stack, state, world, pos, player, hand, hit);
    }
  }

  public void takeSilk(Level world, BlockState state, BlockPos pos) {
    world.setBlock(pos, state.setValue(FULLNESS, 0), Block.UPDATE_ALL);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FULLNESS, FACING);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new SilkMothNestEntity(pos, state);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state,
      BlockEntityType<T> type) {
    return world.isClientSide() ? null
        : createTickerHelper(
            type,
            LighterEndBlockEntities.SILK_MOTH_NEST,
            SilkMothNestEntity::serverTick
        );
  }

  @Override
  public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
    if (
        world instanceof ServerLevel serverWorld
            && player.preventsBlockDrops()
            && serverWorld.getGameRules().get(GameRules.BLOCK_DROPS)
            && world.getBlockEntity(pos) instanceof SilkMothNestEntity nestEntity
    ) {
      int fullness = state.getValue(FULLNESS);
      boolean occupied = nestEntity.getOccupancy() > 0;
      if (occupied || fullness > 0) {
        ItemStack itemStack = new ItemStack(this);
        itemStack.applyComponents(nestEntity.collectComponents());
        itemStack.set(
            LighterEndData.SILK_LEVEL, new SilkLevelComponent(fullness));
        ItemEntity itemEntity = new ItemEntity(
            world,
            pos.getX(),
            pos.getY(),
            pos.getZ(),
            itemStack
        );
        itemEntity.setDefaultPickUpDelay();
        world.addFreshEntity(itemEntity);
      }
    }

    return super.playerWillDestroy(world, pos, state, player);
  }

  @Override
  protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
    Entity entity = builder.getOptionalParameter(LootContextParams.THIS_ENTITY);
    if (entity instanceof PrimedTnt
        || entity instanceof Creeper
        || entity instanceof WitherSkull
        || entity instanceof WitherBoss
        || entity instanceof MinecartTNT) {
      BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
      if (blockEntity instanceof SilkMothNestEntity nestEntity) {
        nestEntity.tryReleaseMoths(state);
      }
    }

    return super.getDrops(state, builder);
  }

  @Override
  protected ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state,
      boolean includeData) {
    ItemStack itemStack = super.getCloneItemStack(world, pos, state, includeData);
    if (includeData) {
      itemStack.set(LighterEndData.SILK_LEVEL,
          new SilkLevelComponent(state.getValueOrElse(FULLNESS, 0)));
    }

    return itemStack;
  }

  @Override
  protected BlockState updateShape(
      BlockState state,
      LevelReader world,
      ScheduledTickAccess tickView,
      BlockPos pos,
      Direction direction,
      BlockPos neighborPos,
      BlockState neighborState,
      RandomSource random
  ) {
    if (world.getBlockState(neighborPos).getBlock() instanceof FireBlock && world.getBlockEntity(
        pos) instanceof SilkMothNestEntity nestEntity) {
      nestEntity.tryReleaseMoths(state);
    }

    return super.updateShape(state, world, tickView, pos, direction, neighborPos,
        neighborState, random);
  }

  @Override
  public BlockState rotate(BlockState state, Rotation rotation) {
    return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
  }

  @Override
  public BlockState mirror(BlockState state, Mirror mirror) {
    return state.rotate(mirror.getRotation(state.getValue(FACING)));
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos,
      CollisionContext context) {
    return SilkMothNest.OUTLINE_SHAPE;
  }

  public static class SilkMothNestFeature implements Feature {

    public SilkMothNestFeature() {
    }

    public static final MapCodec<SilkMothNestFeature> CODEC = MapCodec.unit(
        SilkMothNestFeature::new
    );

    @Override
    public MapCodec<SilkMothNestFeature> codec() {
      return CODEC;
    }

    private boolean canGenerate(WorldGenLevel world, BlockPos pos) {
      BlockState state = world.getBlockState(pos.above());
      if (state.is(BlockTags.LEAVES) || state.is(BlockTags.LOGS)) {
        state = world.getBlockState(pos);
        if (state.isAir() && world.isEmptyBlock(pos.below())) {
          for (Direction dir : Plane.HORIZONTAL) {
            return !world.getBlockState(pos.below().relative(dir)).is(BlockTags.BLOCKS_MOTION);
          }
        }
      }
      return false;
    }

    @Override
    public boolean place(
        final WorldGenLevel world,
        final ChunkGenerator chunkGenerator,
        final RandomSource random,
        final BlockPos center
    ) {
      final MutableBlockPos POS = GlobalState.stateForThread().POS;
      int maxY = world.getHeight(Heightmap.Types.WORLD_SURFACE, center.getX(), center.getZ());
      int minY = PosInfo.upRay(world, new BlockPos(center.getX(), 0, center.getZ()), maxY);
      POS.set(center);
      for (int y = maxY; y > minY; y--) {
        POS.setY(y);
        if (canGenerate(world, POS)) {
          Direction dir = Plane.HORIZONTAL.getRandomDirection(random);
          world.setBlock(
              POS,
              LighterEndBlocks.SILK_MOTH_NEST.defaultBlockState()
                  .setValue(BlockStateProperties.HORIZONTAL_FACING, dir),
              Flags.SILENT
          );
          world.getBlockEntity(POS, LighterEndBlockEntities.SILK_MOTH_NEST).ifPresent(nest ->
              nest.addMoth(SilkMothNestEntity.MothData.create(
                  random.nextInt(SilkMothNestEntity.MIN_OCCUPATION_TICKS))));

          POS.setY(y - 1);
          world.setBlock(
              POS,
              LighterEndBlocks.SILK_MOTH_NEST.defaultBlockState()
                  .setValue(BlockStateProperties.HORIZONTAL_FACING, dir),
              Flags.SILENT
          );
          world.getBlockEntity(POS, LighterEndBlockEntities.SILK_MOTH_NEST).ifPresent(nest ->
              nest.addMoth(SilkMothNestEntity.MothData.create(
                  random.nextInt(SilkMothNestEntity.MIN_OCCUPATION_TICKS))));
          return true;
        }
      }
      return false;
    }
  }
}
