package io.github.openbagtwo.lighterend.mobs;

import io.github.openbagtwo.lighterend.misc.StatusEffects;
import io.github.openbagtwo.lighterend.registries.LighterEndData;
import io.github.openbagtwo.lighterend.registries.LighterEndLootTables;
import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import java.util.List;
import net.minecraft.component.ComponentType;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Shearable;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AbstractCowEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class GlossyMooshroom extends AbstractCowEntity implements Shearable {

  private static final TrackedData<Integer> VARIANT = DataTracker.registerData(
      GlossyMooshroom.class,
      TrackedDataHandlerRegistry.INTEGER
  );
  private static final TrackedData<Boolean> SHEARED = DataTracker.registerData(
      GlossyMooshroom.class,
      TrackedDataHandlerRegistry.BOOLEAN
  );

  @Nullable
  private static final SuspiciousStewEffectsComponent STEW = new SuspiciousStewEffectsComponent(
      List.of(new SuspiciousStewEffectsComponent.StewEffect(StatusEffects.END_VEIL, 100)));

  public GlossyMooshroom(EntityType<? extends GlossyMooshroom> entityType, World world) {
    super(entityType, world);
  }

  @Nullable
  @Override
  public EntityData initialize(
      ServerWorldAccess world,
      LocalDifficulty difficulty,
      SpawnReason spawnReason,
      @Nullable EntityData entityData
  ) {
    this.setVariant(0);

    if (world.getBiome(getBlockPos()).isIn(LighterEndTags.PURPLE_MOOSHROOM_BIOMES)) {
      this.dataTracker.set(VARIANT, 1);
    }
    EntityData data = super.initialize(world, difficulty, spawnReason, entityData);

    this.calculateDimensions();
    return data;
  }

  @Override
  protected void mobTick(ServerWorld world) {
    if (this.isSheared()) {
      if (world.getRandom().nextInt(1024) == 0) {
        world.spawnParticles(ParticleTypes.POOF, this.getX(), this.getBodyY(0.5), this.getZ(), 4,
            0.0, 0.0, 0.0, 0.0);
        this.setSheared(false);
      }
    }
  }

  @Override
  public boolean isBreedingItem(ItemStack stack) {
    return stack.isIn(LighterEndTags.MOOSHROOM_FOOD);
  }

  @Override
  protected void initGoals() {
    this.goalSelector.add(0, new SwimGoal(this));
    this.goalSelector.add(1, new EscapeDangerGoal(this, 2.0));
    this.goalSelector.add(2, new AnimalMateGoal(this, 1.0));
    this.goalSelector.add(3,
        new TemptGoal(this, 1.25, stack -> stack.isIn(LighterEndTags.MOOSHROOM_FOOD), false)
    );
    this.goalSelector.add(4, new FollowParentGoal(this, 1.25));
    this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
    this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
    this.goalSelector.add(7, new LookAroundGoal(this));
  }

  @Override
  public float getPathfindingFavor(BlockPos pos, WorldView world) {
    return world.getBlockState(pos.down()).isIn(LighterEndTags.END_SOIL) ? 10.0F
        : world.getPhototaxisFavor(pos);
  }

  @Override
  protected void initDataTracker(DataTracker.Builder builder) {
    super.initDataTracker(builder);
    builder.add(VARIANT, 0);
    builder.add(SHEARED, false);
  }

  @Override
  public ActionResult interactMob(PlayerEntity player, Hand hand) {
    ItemStack itemStack = player.getStackInHand(hand);
    if (itemStack.isOf(Items.BOWL) && !this.isBaby()) {

      ItemStack stew = new ItemStack(Items.SUSPICIOUS_STEW);
      stew.set(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS, STEW);

      player.setStackInHand(hand, ItemUsage.exchangeStack(itemStack, player, stew, false));

      this.playSound(SoundEvents.ENTITY_MOOSHROOM_SUSPICIOUS_MILK, 1.0F, 1.0F);
      return ActionResult.SUCCESS;
    } else if (itemStack.isOf(Items.SHEARS) && this.isShearable()) {
      if (this.getEntityWorld() instanceof ServerWorld serverWorld) {
        this.sheared(serverWorld, SoundCategory.PLAYERS, itemStack);
        this.emitGameEvent(GameEvent.SHEAR, player);
        itemStack.damage(1, player, hand.getEquipmentSlot());
      }

      return ActionResult.SUCCESS;
    } else {
      return super.interactMob(player, hand);
    }
  }

  @Override
  public void sheared(ServerWorld world, SoundCategory shearedSoundCategory, ItemStack shears) {
    world.playSoundFromEntity(null, this, SoundEvents.ENTITY_MOOSHROOM_SHEAR, shearedSoundCategory,
        1.0F, 1.0F);
    this.dropShearedItems(world, shears);
    this.setSheared(true);
  }

  private void dropShearedItems(ServerWorld world, ItemStack shears) {
    this.forEachShearedItem(
        world,
        LighterEndLootTables.MOOSHROOM_SHEARING,
        shears,
        (worldx, stack) -> this.dropStack(worldx, stack, this.getHeight())
    );
  }

  @Override
  public boolean isShearable() {
    return this.isAlive() && !this.isBaby() && !isSheared();
  }

  public boolean isSheared() {
    return this.dataTracker.get(SHEARED);
  }

  private void setSheared(boolean sheared) {
    this.dataTracker.set(SHEARED, sheared);
  }

  @Override
  protected void writeCustomData(WriteView view) {
    super.writeCustomData(view);
    view.putInt("Variant", this.getVariant());
    view.putBoolean("Sheared", this.isSheared());
  }

  @Override
  protected void readCustomData(ReadView view) {
    super.readCustomData(view);
    this.setVariant(view.getInt("Variant", 0));
    this.setSheared(view.getBoolean("Sheared", false));
  }


  private void setVariant(int variant) {
    this.dataTracker.set(VARIANT, variant);
  }

  public int getVariant() {
    return this.dataTracker.get(VARIANT);
  }

  @Nullable
  @Override
  public <T> T get(ComponentType<? extends T> type) {
    return type == LighterEndData.VARIANT ?
        castComponentValue(type, new LighterEndData.Variant(this.getVariant())) : super.get(type);
  }

  @Override
  protected void copyComponentsFrom(ComponentsAccess from) {
    this.copyComponentFrom(from, LighterEndData.VARIANT);
    super.copyComponentsFrom(from);
  }

  @Override
  protected <T> boolean setApplicableComponent(ComponentType<T> type, T value) {
    if (type == LighterEndData.VARIANT) {
      this.setVariant(castComponentValue(LighterEndData.VARIANT, value).variant());
      return true;
    } else {
      return super.setApplicableComponent(type, value);
    }
  }

  @Nullable
  public GlossyMooshroom createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
    GlossyMooshroom GlossyMooshroom = LighterEndMobs.MOOSHROOM.mob.create(serverWorld,
        SpawnReason.BREEDING);
    if (GlossyMooshroom != null) {
      GlossyMooshroom.setVariant(this.chooseBabyVariant((GlossyMooshroom) passiveEntity));
    }

    return GlossyMooshroom;
  }

  private int chooseBabyVariant(GlossyMooshroom mate) {
    return this.random.nextBoolean() ? this.getVariant() : mate.getVariant();
  }
}
