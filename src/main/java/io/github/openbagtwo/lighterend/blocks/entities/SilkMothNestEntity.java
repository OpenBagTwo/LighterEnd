package io.github.openbagtwo.lighterend.blocks.entities;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.blocks.SilkMothNest;
import io.github.openbagtwo.lighterend.mobs.SilkMoth;
import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndData;
import io.github.openbagtwo.lighterend.registries.LighterEndData.MothsComponent;
import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.netty.buffer.ByteBuf;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class SilkMothNestEntity extends BlockEntity {

  static final List<String> IRRELEVANT_NBT_TAGS = Arrays.asList(
      "Air",
      "drop_chances",
      "equipment",
      "Brain",
      "CanPickUpLoot",
      "DeathTime",
      "fall_distance",
      "FallFlying",
      "Fire",
      "HurtByTimestamp",
      "HurtTime",
      "LeftHanded",
      "Motion",
      "NoGravity",
      "OnGround",
      "PortalCooldown",
      "Pos",
      "Rotation",
      "sleeping_pos",
      "CannotEnterHiveTicks",
      "TicksSincePollination",
      "CropsGrownSincePollination",
      "hive_pos",
      "Passengers",
      "leash",
      "UUID"
  );
  public static final int MAX_MOTH_COUNT = 1;
  public static final int MIN_OCCUPATION_TICKS = 2400;
  private final List<Moth> moths = Lists.newArrayList();


  public SilkMothNestEntity(BlockPos pos, BlockState state) {
    super(LighterEndBlockEntities.SILK_MOTH_NEST, pos, state);
  }

  @Override
  public void setChanged() {
    if (this.isNearFire()) {
      this.tryReleaseMoths(this.level.getBlockState(this.getBlockPos()));
    }
    super.setChanged();
  }

  public int getOccupancy() {
    return this.moths.size();
  }

  public boolean isNearFire() {
    if (this.level != null) {
      for (BlockPos blockPos : BlockPos.betweenClosed(this.worldPosition.offset(-1, -1, -1),
          this.worldPosition.offset(1, 1, 1))) {
        if (this.level.getBlockState(blockPos).getBlock() instanceof FireBlock) {
          return true;
        }
      }
    }
    return false;
  }

  public List<Entity> tryReleaseMoths(BlockState state) {
    List<Entity> list = Lists.<Entity>newArrayList();
    this.moths.removeIf(
        moth -> releaseMoth(this.level, this.worldPosition, state, moth.createData(), list, true));
    if (!list.isEmpty()) {
      super.setChanged();
    }
    return list;
  }

  public static int getFullness(BlockState state) {
    return state.getValue(SilkMothNest.FULLNESS);
  }

  public void tryEnterHive(SilkMoth entity) {
    if (this.moths.size() < MAX_MOTH_COUNT) {
      entity.stopRiding();
      entity.ejectPassengers();
      entity.dropLeash();
      this.addMoth(MothData.of(entity));
      if (this.level != null) {

        BlockPos blockPos = this.getBlockPos();
        this.level
            .playSound(
                null, blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                LighterEndSounds.MOTH_NEST_ENTER, SoundSource.BLOCKS, 1.0F, 1.0F
            );
        this.level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos,
            GameEvent.Context.of(entity, this.getBlockState()));
      }

      entity.discard();
      super.setChanged();
    }
  }

  public void addMoth(MothData moth) {
    this.moths.add(new Moth(moth));
  }

  private static boolean releaseMoth(
      Level world,
      BlockPos pos,
      BlockState state,
      MothData moth,
      @Nullable List<Entity> entities,
      boolean emergency
  ) {

    Direction direction = state.getValue(SilkMothNest.FACING);
    BlockPos blockPos = pos.relative(direction);
    boolean bl = !world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty();
    if (bl) {
      return false;
    } else {
      Entity entity = moth.loadEntity(world, pos);

      if (entity != null) {
        if (entity instanceof SilkMoth mothEntity) {

          if (state.is(LighterEndBlocks.SILK_MOTH_NEST) && !emergency) {
            int current_fullness = getFullness(state);
            if (current_fullness < SilkMothNest.MAX_FULLNESS) {
              int additional_fullness = 1;
              if (current_fullness + additional_fullness < SilkMothNest.MAX_FULLNESS) {
                if (world.getRandom().nextInt(100) == 0) {  // 1% chance of bonus fullness
                  additional_fullness += 1;
                }
              }
              world.setBlockAndUpdate(pos,
                  state.setValue(SilkMothNest.FULLNESS, current_fullness + additional_fullness));
            }
            mothEntity.resetCannotEnterHiveTicks();
          }

          if (entities != null) {
            entities.add(mothEntity);
          }

          float f = entity.getBbWidth();
          double d = bl ? 0.0 : 0.55 + f / 2.0F;
          double e = pos.getX() + 0.5 + d * direction.getStepX();
          double g = pos.getY() + 0.5 - entity.getBbHeight() / 2.0F;
          double h = pos.getZ() + 0.5 + d * direction.getStepZ();
          entity.snapTo(e, g, h, entity.getYRot(), entity.getXRot());
        }

        world.playSound(null, pos, LighterEndSounds.MOTH_NEST_EXIT, SoundSource.BLOCKS, 1.0F,
            1.0F);
        world.gameEvent(GameEvent.BLOCK_CHANGE, pos,
            GameEvent.Context.of(entity, world.getBlockState(pos)));
        return world.addFreshEntity(entity);
      } else {
        return false;
      }
    }
  }


  private static void tickMoths(Level world, BlockPos pos, BlockState state,
      List<Moth> moths) {
    boolean bl = false;
    Iterator<Moth> iterator = moths.iterator();

    while (iterator.hasNext()) {
      Moth moth = iterator.next();
      if (moth.canExitHive()) {
        if (releaseMoth(world, pos, state, moth.createData(), null, false)) {
          bl = true;
          iterator.remove();
        }
      }
    }
    if (bl) {
      setChanged(world, pos, state);
    }
  }

  public static void serverTick(Level world, BlockPos pos, BlockState state,
      SilkMothNestEntity blockEntity) {
    tickMoths(world, pos, state, blockEntity.moths);
    if (!blockEntity.moths.isEmpty() && world.getRandom().nextDouble() < 0.005) {
      double d = pos.getX() + 0.5;
      double e = pos.getY();
      double f = pos.getZ() + 0.5;
      world.playSound(null, d, e, f, LighterEndSounds.MOTH_NEST_WORK, SoundSource.BLOCKS,
          1.0F, 1.0F);
    }
  }

  @Override
  protected void loadAdditional(ValueInput view) {
    super.loadAdditional(view);
    this.moths.clear();
    for (MothData data : view.read("moths", MothData.LIST_CODEC).orElse(List.of())) {
      this.addMoth(data);
    }
  }

  @Override
  protected void saveAdditional(ValueOutput view) {
    super.saveAdditional(view);
    view.store("moths", MothData.LIST_CODEC,
        this.createMothData());
  }

  @Override
  protected void applyImplicitComponents(DataComponentGetter components) {
    super.applyImplicitComponents(components);
    this.moths.clear();
    List<MothData> list = components.getOrDefault(
        LighterEndData.MOTHS, LighterEndData.MothsComponent.DEFAULT).moths();
    list.forEach(this::addMoth);
  }

  @Override
  protected void collectImplicitComponents(DataComponentMap.Builder builder) {
    super.collectImplicitComponents(builder);
    builder.set(LighterEndData.MOTHS, new MothsComponent(this.createMothData()));
  }

  @Override
  public void removeComponentsFromTag(ValueOutput view) {
    super.removeComponentsFromTag(view);
    view.discard("moths");
  }

  private List<MothData> createMothData() {
    return this.moths.stream().map(Moth::createData)
        .toList();
  }

  protected static class Moth {

    private final MothData data;
    private int ticksInHive;

    protected Moth(MothData data) {
      this.data = data;
      this.ticksInHive = data.ticksInHive();
    }

    public boolean canExitHive() {
      return this.ticksInHive++ > this.data.minTicksInHive;
    }

    public MothData createData() {
      return new MothData(this.data.entityData,
          this.ticksInHive, this.data.minTicksInHive);
    }
  }

  public record MothData(CustomData entityData, int ticksInHive, int minTicksInHive) {

    public static final Codec<MothData> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
                CustomData.CODEC.optionalFieldOf("entity_data", CustomData.EMPTY).forGetter(
                    MothData::entityData),
                Codec.INT.fieldOf("ticks_in_hive").forGetter(
                    MothData::ticksInHive),
                Codec.INT.fieldOf("min_ticks_in_hive").forGetter(
                    MothData::minTicksInHive)
            )
            .apply(instance, MothData::new)
    );
    public static final Codec<List<MothData>> LIST_CODEC = CODEC.listOf();
    public static final StreamCodec<ByteBuf, MothData> PACKET_CODEC = StreamCodec.composite(
        CustomData.STREAM_CODEC,
        MothData::entityData,
        ByteBufCodecs.VAR_INT,
        MothData::ticksInHive,
        ByteBufCodecs.VAR_INT,
        MothData::minTicksInHive,
        MothData::new
    );

    public static MothData create(int ticksInHive) {
      CompoundTag nbtCompound = new CompoundTag();
      nbtCompound.putString("id",
          BuiltInRegistries.ENTITY_TYPE.getKey(LighterEndMobs.SILK_MOTH.mob).toString());
      return new MothData(
          CustomData.of(nbtCompound),
          ticksInHive,
          MIN_OCCUPATION_TICKS
      );
    }

    @Nullable
    public Entity loadEntity(Level world, BlockPos pos) {
      CompoundTag nbtCompound = this.entityData.copyTag();
      SilkMothNestEntity.IRRELEVANT_NBT_TAGS.forEach(
          nbtCompound::remove);
      Entity entity = EntityType.loadEntityRecursive(nbtCompound, world, EntitySpawnReason.LOAD,
          entityx -> entityx);
      if (entity != null && entity.is(LighterEndTags.MOTH_NEST_INHABITORS)) {
        entity.setNoGravity(true);
        if (entity instanceof SilkMoth mothEntity) {
          mothEntity.setHive(pos);
          tickEntity(this.ticksInHive, mothEntity);
        }
        return entity;
      } else {
        return null;
      }
    }

    private static void tickEntity(int ticksInHive, SilkMoth moth) {
      int i = moth.getAge();
      if (i < 0) {
        moth.setAge(Math.min(0, i + ticksInHive));
      } else if (i > 0) {
        moth.setAge(Math.max(0, i - ticksInHive));
      }

      moth.setInLoveTime(Math.max(0, moth.getInLoveTime() - ticksInHive));
    }

    public static MothData of(Entity entity) {
      MothData mothData;
      try (ProblemReporter.ScopedCollector logging = new ProblemReporter.ScopedCollector(
          entity.problemPath(),
          LighterEnd.LOGGER
      )) {
        TagValueOutput nbtWriteView = TagValueOutput.createWithContext(logging,
            entity.registryAccess());
        entity.save(nbtWriteView);
        SilkMothNestEntity.IRRELEVANT_NBT_TAGS.forEach(nbtWriteView::discard);
        CompoundTag nbtCompound = nbtWriteView.buildResult();
        mothData = new MothData(CustomData.of(nbtCompound), 0, MIN_OCCUPATION_TICKS);
      }

      return mothData;
    }
  }
}
