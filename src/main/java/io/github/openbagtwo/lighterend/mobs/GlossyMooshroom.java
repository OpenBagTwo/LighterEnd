package io.github.openbagtwo.lighterend.mobs;

import io.github.openbagtwo.lighterend.misc.StatusEffects;
import io.github.openbagtwo.lighterend.registries.LighterEndData;
import io.github.openbagtwo.lighterend.registries.LighterEndLootTables;
import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.cow.AbstractCow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class GlossyMooshroom extends AbstractCow implements Shearable {

  private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(
      GlossyMooshroom.class,
      EntityDataSerializers.INT
  );
  private static final EntityDataAccessor<Boolean> SHEARED = SynchedEntityData.defineId(
      GlossyMooshroom.class,
      EntityDataSerializers.BOOLEAN
  );

  @Nullable
  private static final SuspiciousStewEffects STEW = new SuspiciousStewEffects(
      List.of(new SuspiciousStewEffects.Entry(StatusEffects.END_VEIL, 100)));

  public GlossyMooshroom(EntityType<? extends GlossyMooshroom> entityType, Level world) {
    super(entityType, world);
  }

  @Nullable
  @Override
  public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor world,
      DifficultyInstance difficulty,
      EntitySpawnReason spawnReason,
      @Nullable SpawnGroupData entityData
  ) {
    this.setVariant(0);

    if (world.getBiome(blockPosition()).is(LighterEndTags.PURPLE_MOOSHROOM_BIOMES)) {
      this.entityData.set(VARIANT, 1);
    }
    SpawnGroupData data = super.finalizeSpawn(world, difficulty, spawnReason, entityData);

    this.refreshDimensions();
    return data;
  }

  @Override
  protected void customServerAiStep(ServerLevel world) {
    if (this.isSheared()) {
      if (world.getRandom().nextInt(1024) == 0) {
        world.sendParticles(ParticleTypes.POOF, this.getX(), this.getY(0.5), this.getZ(), 4,
            0.0, 0.0, 0.0, 0.0);
        this.setSheared(false);
      }
    }
  }

  @Override
  public boolean isFood(ItemStack stack) {
    return stack.is(LighterEndTags.MOOSHROOM_FOOD);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new PanicGoal(this, 2.0));
    this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
    this.goalSelector.addGoal(3,
        new TemptGoal(this, 1.25, stack -> stack.is(LighterEndTags.MOOSHROOM_FOOD), false)
    );
    this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
    this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
    this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
    this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
  }

  @Override
  public float getWalkTargetValue(BlockPos pos, LevelReader world) {
    return world.getBlockState(pos.below()).is(LighterEndTags.END_SOIL) ? 10.0F
        : world.getPathfindingCostFromLightLevels(pos);
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    super.defineSynchedData(builder);
    builder.define(VARIANT, 0);
    builder.define(SHEARED, false);
  }

  @Override
  public InteractionResult mobInteract(Player player, InteractionHand hand) {
    ItemStack itemStack = player.getItemInHand(hand);
    if (itemStack.is(Items.BOWL) && !this.isBaby()) {

      ItemStack stew = new ItemStack(Items.SUSPICIOUS_STEW);
      stew.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, STEW);

      player.setItemInHand(hand, ItemUtils.createFilledResult(itemStack, player, stew, false));

      this.playSound(SoundEvents.MOOSHROOM_MILK_SUSPICIOUSLY, 1.0F, 1.0F);
      return InteractionResult.SUCCESS;
    } else if (itemStack.is(Items.SHEARS) && this.readyForShearing()) {
      if (this.level() instanceof ServerLevel serverWorld) {
        this.shear(serverWorld, SoundSource.PLAYERS, itemStack);
        this.gameEvent(GameEvent.SHEAR, player);
        itemStack.hurtAndBreak(1, player, hand.asEquipmentSlot());
      }

      return InteractionResult.SUCCESS;
    } else {
      return super.mobInteract(player, hand);
    }
  }

  @Override
  public void shear(ServerLevel world, SoundSource shearedSoundCategory, ItemStack shears) {
    world.playSound(null, this, SoundEvents.MOOSHROOM_SHEAR, shearedSoundCategory,
        1.0F, 1.0F);
    this.dropShearedItems(world, shears);
    this.setSheared(true);
  }

  private void dropShearedItems(ServerLevel world, ItemStack shears) {
    this.dropFromShearingLootTable(
        world,
        LighterEndLootTables.MOOSHROOM_SHEARING,
        shears,
        (worldx, stack) -> this.spawnAtLocation(worldx, stack, this.getBbHeight())
    );
  }

  @Override
  public boolean readyForShearing() {
    return this.isAlive() && !this.isBaby() && !isSheared();
  }

  public boolean isSheared() {
    return this.entityData.get(SHEARED);
  }

  private void setSheared(boolean sheared) {
    this.entityData.set(SHEARED, sheared);
  }

  @Override
  protected void addAdditionalSaveData(ValueOutput view) {
    super.addAdditionalSaveData(view);
    view.putInt("Variant", this.getVariant());
    view.putBoolean("Sheared", this.isSheared());
  }

  @Override
  protected void readAdditionalSaveData(ValueInput view) {
    super.readAdditionalSaveData(view);
    this.setVariant(view.getIntOr("Variant", 0));
    this.setSheared(view.getBooleanOr("Sheared", false));
  }


  private void setVariant(int variant) {
    this.entityData.set(VARIANT, variant);
  }

  public int getVariant() {
    return this.entityData.get(VARIANT);
  }

  @Nullable
  @Override
  public <T> T get(DataComponentType<? extends T> type) {
    return type == LighterEndData.VARIANT ?
        castComponentValue(type, new LighterEndData.Variant(this.getVariant())) : super.get(type);
  }

  @Override
  protected void applyImplicitComponents(DataComponentGetter from) {
    this.applyImplicitComponentIfPresent(from, LighterEndData.VARIANT);
    super.applyImplicitComponents(from);
  }

  @Override
  protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
    if (type == LighterEndData.VARIANT) {
      this.setVariant(castComponentValue(LighterEndData.VARIANT, value).variant());
      return true;
    } else {
      return super.applyImplicitComponent(type, value);
    }
  }

  @Nullable
  public GlossyMooshroom getBreedOffspring(ServerLevel serverWorld, AgeableMob passiveEntity) {
    GlossyMooshroom GlossyMooshroom = LighterEndMobs.MOOSHROOM.mob.create(serverWorld,
        EntitySpawnReason.BREEDING);
    if (GlossyMooshroom != null) {
      GlossyMooshroom.setVariant(this.chooseBabyVariant((GlossyMooshroom) passiveEntity));
    }

    return GlossyMooshroom;
  }

  private int chooseBabyVariant(GlossyMooshroom mate) {
    return this.random.nextBoolean() ? this.getVariant() : mate.getVariant();
  }
}
