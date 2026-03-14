package io.github.openbagtwo.lighterend.misc;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.blocks.Shelf;
import io.github.openbagtwo.lighterend.blocks.Signs;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import java.util.Arrays;
import java.util.List;
import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class Wood {

  public static class WoodSet {

    public final String baseName;
    public final WoodType woodType;
    public final Block log;
    public final Block strippedLog;
    public final Block wood;
    public final Block strippedWood;
    public final Block planks;
    public final Block slab;
    public final Block stairs;
    public final Block door;
    public final Block trapdoor;
    public final Block fence;
    public final Block gate;
    public final Block button;
    public final Block pressurePlate;
    public final Block ladder;
    public final Block sign;
    public final Block wallSign;
    public final Block hangingSign;
    public final Block wallHangingSign;
    public final Block shelf;
    // public final Block stool;
    public final List<Block> blocks;
    private final MapColor woodColor;
    private final SoundType logSounds;

    public WoodSet(String name, MapColor barkColor, MapColor woodColor) {
      this.baseName = name;
      this.woodColor = woodColor;

      this.woodType = createWoodType(baseName);
      this.logSounds = createWoodSoundGroup(baseName + "_log");

      log = LighterEndBlocks.register(baseName + "_log",
          settings -> new RotatedPillarBlock(
              applyLogSettings(
                  settings.mapColor(
                      state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y
                          ? woodColor
                          : barkColor
                  )
              )
          )
      );
      strippedLog = LighterEndBlocks.register(baseName + "_stripped_log",
          settings -> new RotatedPillarBlock(
              applyLogSettings(settings.mapColor(woodColor))
          )
      );
      wood = LighterEndBlocks.register(baseName + "_wood",
          settings -> new RotatedPillarBlock(
              applyLogSettings(settings.mapColor(barkColor))
          )
      );
      strippedWood = LighterEndBlocks.register(baseName + "_stripped_wood",
          settings -> new RotatedPillarBlock(
              applyLogSettings(settings.mapColor(woodColor))));

      StrippableBlockRegistry.register(log, strippedLog);
      StrippableBlockRegistry.register(wood, strippedWood);

      planks = LighterEndBlocks.register(
          baseName + "_planks",
          settings -> new Block(applyPlankSettings(settings))
      );
      slab = LighterEndBlocks.register(
          baseName + "_slab",
          settings -> new SlabBlock(applyPlankSettings(settings))
      );
      stairs = LighterEndBlocks.register(
          baseName + "_stairs",
          settings -> new StairBlock(planks.defaultBlockState(), applyPlankSettings(settings))
      );

      door = LighterEndBlocks.register(
          baseName + "_door",
          settings -> new DoorBlock(
              woodType.setType(),
              settings.mapColor(planks.defaultMapColor())
                  .instrument(NoteBlockInstrument.BASS)
                  .strength(3.0F)
                  .noOcclusion()
                  .ignitedByLava()
                  .pushReaction(PushReaction.DESTROY)
          )
      );
      trapdoor = LighterEndBlocks.register(
          baseName + "_trapdoor",
          settings -> new TrapDoorBlock(
              woodType.setType(),
              settings.mapColor(planks.defaultMapColor())
                  .instrument(NoteBlockInstrument.BASS)
                  .strength(3.0F)
                  .noOcclusion()
                  .isValidSpawn(Blocks::never)
                  .ignitedByLava()
          )
      );
      fence = LighterEndBlocks.register(
          baseName + "_fence",
          settings -> new FenceBlock(
              settings.mapColor(planks.defaultMapColor())
                  .instrument(NoteBlockInstrument.BASS)
                  .strength(2.0F, 3.0F)
                  .ignitedByLava()
                  .sound(woodType.soundType())
          )
      );
      gate = LighterEndBlocks.register(
          baseName + "_fence_gate",
          settings -> new FenceGateBlock(
              woodType,
              settings
                  .mapColor(planks.defaultMapColor())
                  .forceSolidOn()
                  .instrument(NoteBlockInstrument.BASS)
                  .strength(2.0F, 3.0F)
                  .ignitedByLava()
          )
      );
      button = LighterEndBlocks.register(
          baseName + "_button",
          settings -> new ButtonBlock(
              woodType.setType(),
              30,
              settings.noCollision().strength(0.5F).pushReaction(PushReaction.DESTROY)
          )
      );
      pressurePlate = LighterEndBlocks.register(
          baseName + "_pressure_plate",
          settings -> new PressurePlateBlock(
              woodType.setType(),
              settings.mapColor(planks.defaultMapColor())
                  .forceSolidOn()
                  .instrument(NoteBlockInstrument.BASS)
                  .noCollision()
                  .strength(0.5F)
                  .ignitedByLava()
                  .pushReaction(PushReaction.DESTROY)
          )
      );
      ladder = LighterEndBlocks.register(
          baseName + "_ladder",
          settings -> new LadderBlock(
              settings
                  .strength(0.4F)
                  .sound(SoundType.LADDER)
                  .noOcclusion()
                  .pushReaction(PushReaction.DESTROY)
          )
      );
      sign = LighterEndBlocks.register(
          baseName + "_sign",
          settings -> new Signs.LighterEndStandingSignBlock(
              woodType,
              settings.mapColor(planks.defaultMapColor())
          ),
          false
      );
      wallSign = LighterEndBlocks.register(
          baseName + "_wall_sign",
          settings -> new Signs.LighterEndWallSignBlock(
              woodType,
              settings
                  .mapColor(planks.defaultMapColor())
                  .overrideLootTable(sign.getLootTable())
                  .overrideDescription(sign.getDescriptionId())
          ),
          false
      );
      Registry.register(
          BuiltInRegistries.ITEM,
          LighterEnd.of(baseName + "_sign"),
          new SignItem(
              sign,
              wallSign,
              new Item.Properties().stacksTo(16).setId(
                  ResourceKey.create(Registries.ITEM, LighterEnd.of(baseName + "_sign"))
              ).useBlockDescriptionPrefix()
          )
      );
      hangingSign = LighterEndBlocks.register(
          baseName + "_hanging_sign",
          settings -> new Signs.LighterEndCeilingHangingSignBlock(
              woodType,
              settings.mapColor(planks.defaultMapColor())
          ),
          false
      );
      wallHangingSign = LighterEndBlocks.register(
          baseName + "_wall_hanging_sign",
          settings -> new Signs.LighterEndWallHangingSignBlock(
              woodType,
              settings
                  .overrideLootTable(hangingSign.getLootTable())
                  .overrideDescription(hangingSign.getDescriptionId())
                  .mapColor(planks.defaultMapColor())
          ),
          false
      );
      Registry.register(
          BuiltInRegistries.ITEM,
          LighterEnd.of(baseName + "_hanging_sign"),
          new HangingSignItem(
              hangingSign,
              wallHangingSign,
              new Item.Properties().stacksTo(16).setId(
                  ResourceKey.create(Registries.ITEM, LighterEnd.of(baseName + "_hanging_sign"))
              ).useBlockDescriptionPrefix()
          )
      );
      shelf = LighterEndBlocks.register(
          baseName + "_shelf",
          settings -> new Shelf(
              settings.mapColor(planks.defaultMapColor())
                  .instrument(NoteBlockInstrument.BASS)
                  .sound(SoundType.SHELF)
                  .ignitedByLava()
                  .strength(2.0F, 3.0F)
          )
      );

      for (Block block : Arrays.asList(log, strippedLog, wood, strippedWood)) {
        FlammableBlockRegistry.getDefaultInstance().add(block, 5, 5);
      }
      for (Block block : Arrays.asList(planks, slab, stairs, fence, gate)) {
        FlammableBlockRegistry.getDefaultInstance().add(block, 5, 20);
      }
      FlammableBlockRegistry.getDefaultInstance().add(shelf, 30, 20);

      blocks = Arrays.asList(
          log,
          strippedLog,
          wood,
          strippedWood,
          planks,
          slab,
          stairs,
          door,
          trapdoor,
          fence,
          gate,
          button,
          pressurePlate,
          ladder,
          sign,
          hangingSign,
          shelf
      );
    }

    public Properties applyLogSettings(Properties settings) {
      return settings
          .instrument(NoteBlockInstrument.BASS)
          .sound(this.logSounds)
          .strength(2.0F)
          .ignitedByLava();
    }

    public Properties applyPlankSettings(Properties settings) {
      return settings
          .mapColor(this.woodColor)
          .instrument(NoteBlockInstrument.BASS)
          .sound(this.woodType.soundType())
          .strength(2.0F, 3.0F)
          .ignitedByLava();
    }
  }

  public static WoodType createWoodType(String name) {
    SoundType soundGroup = createWoodSoundGroup(name);

    return (new WoodTypeBuilder())
        .soundType(soundGroup)
        .hangingSignSoundType(createWoodSoundGroup(name + "_hanging_sign"))
        .fenceGateCloseSound(LighterEndSounds.register("block." + name + "_fence_gate.close"))
        .fenceGateOpenSound(LighterEndSounds.register("block." + name + "_fence_gate.open"))
        .register(LighterEnd.of(name), createWoodSetType(name, soundGroup));
  }

  private static BlockSetType createWoodSetType(String name, SoundType soundGroup) {
    return (new BlockSetTypeBuilder())
        .openableByHand(true)
        .openableByWindCharge(true)
        .buttonActivatedByArrows(true)
        .pressurePlateActivationRule(BlockSetType.PressurePlateSensitivity.EVERYTHING)
        .soundType(soundGroup)
        .doorCloseSound(LighterEndSounds.register("block." + name + "_door.close"))
        .doorOpenSound(LighterEndSounds.register("block." + name + "_door.open"))
        .trapdoorCloseSound(LighterEndSounds.register("block." + name + "_trapdoor.close"))
        .trapdoorCloseSound(LighterEndSounds.register("block." + name + "_trapdoor.open"))
        .pressurePlateClickOffSound(
            LighterEndSounds.register("block." + name + "_pressure_plate.click_off")
        ).pressurePlateClickOnSound(
            LighterEndSounds.register("block." + name + "_pressure_plate.click_on")
        ).register(LighterEnd.of(name));

  }

  private static SoundType createWoodSoundGroup(String name) {
    return new SoundType(
        1.0F,
        1.0F,
        LighterEndSounds.register("block." + name + ".break"),
        LighterEndSounds.register("block." + name + ".step"),
        LighterEndSounds.register("block." + name + ".place"),
        LighterEndSounds.register("block." + name + ".hit"),
        LighterEndSounds.register("block." + name + ".fall")
    );
  }

}
