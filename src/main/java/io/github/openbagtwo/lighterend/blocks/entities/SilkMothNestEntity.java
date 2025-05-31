package io.github.openbagtwo.lighterend.blocks.entities;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.blocks.SilkMothNest;
import io.github.openbagtwo.lighterend.mobs.SilkMoth;
import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities;
import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities.MothsComponent;
import io.github.openbagtwo.lighterend.tags.LighterEndTags;
import io.netty.buffer.ByteBuf;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.NbtWriteView;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ErrorReporter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
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
  public void markDirty() {
    if (this.isNearFire()) {
      this.tryReleaseMoths(this.world.getBlockState(this.getPos()));
    }

    super.markDirty();
  }

  public int getOccupancy() {
    return this.moths.size();
  }

  public boolean isNearFire() {
    if (this.world == null) {
      return false;
    } else {
      for (BlockPos blockPos : BlockPos.iterate(this.pos.add(-1, -1, -1), this.pos.add(1, 1, 1))) {
        if (this.world.getBlockState(blockPos).getBlock() instanceof FireBlock) {
          return true;
        }
      }

      return false;
    }
  }

  public List<Entity> tryReleaseMoths(BlockState state) {
    List<Entity> list = Lists.<Entity>newArrayList();
    this.moths.removeIf(
        moth -> releaseMoth(this.world, this.pos, state, moth.createData(), list));
    if (!list.isEmpty()) {
      super.markDirty();
    }
    return list;
  }

  public static int getFullness(BlockState state) {
    return state.get(SilkMothNest.FULLNESS);
  }

  public void tryEnterHive(BeeEntity entity) {
    if (this.moths.size() < MAX_MOTH_COUNT) {
      entity.stopRiding();
      entity.removeAllPassengers();
      entity.detachLeash();
      this.addMoth(MothData.of(entity));
      if (this.world != null) {

        BlockPos blockPos = this.getPos();
        this.world
            .playSound(
                null, blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                SoundEvents.BLOCK_BEEHIVE_ENTER, SoundCategory.BLOCKS, 1.0F, 1.0F
            );
        this.world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos,
            GameEvent.Emitter.of(entity, this.getCachedState()));
      }

      entity.discard();
      super.markDirty();
    }
  }

  public void addMoth(MothData moth) {
    this.moths.add(new Moth(moth));
  }

  private static boolean releaseMoth(
      World world,
      BlockPos pos,
      BlockState state,
      MothData moth,
      @Nullable List<Entity> entities
  ) {

    Direction direction = state.get(BeehiveBlock.FACING);
    BlockPos blockPos = pos.offset(direction);
    boolean bl = !world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty();
    if (bl) {
      return false;
    } else {
      Entity entity = moth.loadEntity(world, pos);
      if (entity != null) {
        if (entity instanceof SilkMoth mothEntity) {

          if (state.contains(SilkMothNest.FULLNESS)) {
            int i = getFullness(state);
            if (i < 5) {
              int j = world.random.nextInt(100) == 0 ? 2 : 1;
              if (i + j > 5) {
                j--;
              }

              world.setBlockState(pos, state.with(SilkMothNest.FULLNESS, i + j));
            }
          }

          if (entities != null) {
            entities.add(mothEntity);
          }

          float f = entity.getWidth();
          double d = bl ? 0.0 : 0.55 + f / 2.0F;
          double e = pos.getX() + 0.5 + d * direction.getOffsetX();
          double g = pos.getY() + 0.5 - entity.getHeight() / 2.0F;
          double h = pos.getZ() + 0.5 + d * direction.getOffsetZ();
          entity.refreshPositionAndAngles(e, g, h, entity.getYaw(), entity.getPitch());
        }

        world.playSound(null, pos, SoundEvents.BLOCK_BEEHIVE_EXIT, SoundCategory.BLOCKS, 1.0F,
            1.0F);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos,
            GameEvent.Emitter.of(entity, world.getBlockState(pos)));
        return world.spawnEntity(entity);
      } else {
        return false;
      }
    }
  }


  private static void tickMoths(World world, BlockPos pos, BlockState state,
      List<Moth> moths) {
    boolean bl = false;
    Iterator<Moth> iterator = moths.iterator();

    while (iterator.hasNext()) {
      Moth moth = iterator.next();
      if (moth.canExitHive()) {
        if (releaseMoth(world, pos, state, moth.createData(), null)) {
          bl = true;
          iterator.remove();
        }
      }
    }
    if (bl) {
      markDirty(world, pos, state);
    }
  }

  public static void serverTick(World world, BlockPos pos, BlockState state,
      SilkMothNestEntity blockEntity) {
    tickMoths(world, pos, state, blockEntity.moths);
    if (!blockEntity.moths.isEmpty() && world.getRandom().nextDouble() < 0.005) {
      double d = pos.getX() + 0.5;
      double e = pos.getY();
      double f = pos.getZ() + 0.5;
      world.playSound(null, d, e, f, SoundEvents.BLOCK_BEEHIVE_WORK, SoundCategory.BLOCKS, 1.0F,
          1.0F);
    }
  }

  @Override
  protected void readData(ReadView view) {
    super.readData(view);
    this.moths.clear();
    for (MothData data : view.read("moths", MothData.LIST_CODEC).orElse(List.of())) {
      this.addMoth(data);
    }
  }

  @Override
  protected void writeData(WriteView view) {
    super.writeData(view);
    view.put("moths", MothData.LIST_CODEC,
        this.createMothData());
  }

  @Override
  protected void readComponents(ComponentsAccess components) {
    super.readComponents(components);
    this.moths.clear();
    List<MothData> list = components.getOrDefault(
        LighterEndBlockEntities.MOTHS, MothsComponent.DEFAULT).moths();
    list.forEach(this::addMoth);
  }

  @Override
  protected void addComponents(ComponentMap.Builder builder) {
    super.addComponents(builder);
    builder.add(LighterEndBlockEntities.MOTHS, new MothsComponent(this.createMothData()));
  }

  @Override
  public void removeFromCopiedStackData(WriteView view) {
    super.removeFromCopiedStackData(view);
    view.remove("moths");
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

  public record MothData(NbtComponent entityData, int ticksInHive, int minTicksInHive) {

    public static final Codec<MothData> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
                NbtComponent.CODEC.optionalFieldOf("entity_data", NbtComponent.DEFAULT).forGetter(
                    MothData::entityData),
                Codec.INT.fieldOf("ticks_in_hive").forGetter(
                    MothData::ticksInHive),
                Codec.INT.fieldOf("min_ticks_in_hive").forGetter(
                    MothData::minTicksInHive)
            )
            .apply(instance, MothData::new)
    );
    public static final Codec<List<MothData>> LIST_CODEC = CODEC.listOf();
    public static final PacketCodec<ByteBuf, MothData> PACKET_CODEC = PacketCodec.tuple(
        NbtComponent.PACKET_CODEC,
        MothData::entityData,
        PacketCodecs.VAR_INT,
        MothData::ticksInHive,
        PacketCodecs.VAR_INT,
        MothData::minTicksInHive,
        MothData::new
    );

    public static MothData create(int ticksInHive) {
      NbtCompound nbtCompound = new NbtCompound();
      nbtCompound.putString("id", Registries.ENTITY_TYPE.getId(EntityType.BEE).toString());
      return new MothData(NbtComponent.of(nbtCompound),
          ticksInHive, 600);
    }

    @Nullable
    public Entity loadEntity(World world, BlockPos pos) {
      NbtCompound nbtCompound = this.entityData.copyNbt();
      SilkMothNestEntity.IRRELEVANT_NBT_TAGS.forEach(
          nbtCompound::remove);
      Entity entity = EntityType.loadEntityWithPassengers(nbtCompound, world, SpawnReason.LOAD,
          entityx -> entityx);
      if (entity != null && entity.getType().isIn(LighterEndTags.MOTH_NEST_INHABITORS)) {
        entity.setNoGravity(true);
        if (entity instanceof SilkMoth mothEntity) {
          mothEntity.setHive(mothEntity.getWorld(), pos);
          tickEntity(this.ticksInHive, mothEntity);
        }
        return entity;
      } else {
        return null;
      }
    }

    private static void tickEntity(int ticksInHive, SilkMoth moth) {
      int i = moth.getBreedingAge();
      if (i < 0) {
        moth.setBreedingAge(Math.min(0, i + ticksInHive));
      } else if (i > 0) {
        moth.setBreedingAge(Math.max(0, i - ticksInHive));
      }

      moth.setLoveTicks(Math.max(0, moth.getLoveTicks() - ticksInHive));
    }

    public static MothData of(Entity entity) {
      MothData mothData;
      try (ErrorReporter.Logging logging = new ErrorReporter.Logging(
          entity.getErrorReporterContext(),
          LighterEnd.LOGGER
      )) {
        NbtWriteView nbtWriteView = NbtWriteView.create(logging, entity.getRegistryManager());
        entity.saveData(nbtWriteView);
        SilkMothNestEntity.IRRELEVANT_NBT_TAGS.forEach(nbtWriteView::remove);
        NbtCompound nbtCompound = nbtWriteView.getNbt();
        mothData = new MothData(NbtComponent.of(nbtCompound), 0, MIN_OCCUPATION_TICKS);
      }

      return mothData;
    }
  }
}
