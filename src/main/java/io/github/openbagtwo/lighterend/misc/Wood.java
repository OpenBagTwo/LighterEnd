package io.github.openbagtwo.lighterend.misc;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.blocks.Signs;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import java.util.Arrays;
import java.util.List;
import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.Blocks;
import net.minecraft.block.ButtonBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.WoodType;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.HangingSignItem;
import net.minecraft.item.Item;
import net.minecraft.item.SignItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.Direction;

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
    // public final Block stool;
    public final List<Block> blocks;
    private final MapColor woodColor;
    private final BlockSoundGroup logSounds;

    public WoodSet(String name, MapColor barkColor, MapColor woodColor) {
      this.baseName = name;
      this.woodColor = woodColor;

      this.woodType = createWoodType(baseName);
      this.logSounds = createWoodSoundGroup(baseName + "_log");

      log = LighterEndBlocks.register(baseName + "_log",
          settings -> new PillarBlock(
              applyLogSettings(
                  settings.mapColor(
                      state -> state.get(PillarBlock.AXIS) == Direction.Axis.Y ? woodColor
                          : barkColor
                  )
              )
          )
      );
      strippedLog = LighterEndBlocks.register(baseName + "_stripped_log",
          settings -> new PillarBlock(
              applyLogSettings(settings.mapColor(woodColor))
          )
      );
      wood = LighterEndBlocks.register(baseName + "_wood",
          settings -> new PillarBlock(
              applyLogSettings(settings.mapColor(barkColor))
          )
      );
      strippedWood = LighterEndBlocks.register(baseName + "_stripped_wood",
          settings -> new PillarBlock(
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
          settings -> new StairsBlock(planks.getDefaultState(), applyPlankSettings(settings))
      );

      door = LighterEndBlocks.register(
          baseName + "_door",
          settings -> new DoorBlock(
              woodType.setType(),
              settings.mapColor(planks.getDefaultMapColor())
                  .instrument(NoteBlockInstrument.BASS)
                  .strength(3.0F)
                  .nonOpaque()
                  .burnable()
                  .pistonBehavior(PistonBehavior.DESTROY)
          )
      );
      trapdoor = LighterEndBlocks.register(
          baseName + "_trapdoor",
          settings -> new TrapdoorBlock(
              woodType.setType(),
              settings.mapColor(planks.getDefaultMapColor())
                  .instrument(NoteBlockInstrument.BASS)
                  .strength(3.0F)
                  .nonOpaque()
                  .allowsSpawning(Blocks::never)
                  .burnable()
          )
      );
      fence = LighterEndBlocks.register(
          baseName + "_fence",
          settings -> new FenceBlock(
              settings.mapColor(planks.getDefaultMapColor())
                  .instrument(NoteBlockInstrument.BASS)
                  .strength(2.0F, 3.0F)
                  .burnable()
                  .sounds(woodType.soundType())
          )
      );
      gate = LighterEndBlocks.register(
          baseName + "_fence_gate",
          settings -> new FenceGateBlock(
              woodType,
              settings
                  .mapColor(planks.getDefaultMapColor())
                  .solid()
                  .instrument(NoteBlockInstrument.BASS)
                  .strength(2.0F, 3.0F)
                  .burnable()
          )
      );
      button = LighterEndBlocks.register(
          baseName + "_button",
          settings -> new ButtonBlock(
              woodType.setType(),
              30,
              settings.noCollision().strength(0.5F).pistonBehavior(PistonBehavior.DESTROY)
          )
      );
      pressurePlate = LighterEndBlocks.register(
          baseName + "_pressure_plate",
          settings -> new PressurePlateBlock(
              woodType.setType(),
              settings.mapColor(planks.getDefaultMapColor())
                  .solid()
                  .instrument(NoteBlockInstrument.BASS)
                  .noCollision()
                  .strength(0.5F)
                  .burnable()
                  .pistonBehavior(PistonBehavior.DESTROY)
          )
      );
      ladder = LighterEndBlocks.register(
          baseName + "_ladder",
          settings -> new LadderBlock(
              settings
                  .strength(0.4F)
                  .sounds(BlockSoundGroup.LADDER)
                  .nonOpaque()
                  .pistonBehavior(PistonBehavior.DESTROY)
          )
      );
      sign = LighterEndBlocks.register(
          baseName + "_sign",
          settings -> new Signs.LighterEndStandingSignBlock(
              woodType,
              settings.mapColor(planks.getDefaultMapColor())
          ),
          false
      );
      wallSign = LighterEndBlocks.register(
          baseName + "_wall_sign",
          settings -> new Signs.LighterEndWallSignBlock(
              woodType,
              settings
                  .mapColor(planks.getDefaultMapColor())
                  .lootTable(sign.getLootTableKey())
                  .overrideTranslationKey(sign.getTranslationKey())
          ),
          false
      );
      Registry.register(
          Registries.ITEM,
          LighterEnd.of(baseName + "_sign"),
          new SignItem(
              sign,
              wallSign,
              new Item.Settings().maxCount(16).registryKey(
                  RegistryKey.of(RegistryKeys.ITEM, LighterEnd.of(baseName + "_sign"))
              ).useBlockPrefixedTranslationKey()
          )
      );
      hangingSign = LighterEndBlocks.register(
          baseName + "_hanging_sign",
          settings -> new Signs.LighterEndCeilingHangingSignBlock(
              woodType,
              settings.mapColor(planks.getDefaultMapColor())
          ),
          false
      );
      wallHangingSign = LighterEndBlocks.register(
          baseName + "_wall_hanging_sign",
          settings -> new Signs.LighterEndWallHangingSignBlock(
              woodType,
              settings
                  .lootTable(hangingSign.getLootTableKey())
                  .overrideTranslationKey(hangingSign.getTranslationKey())
                  .mapColor(planks.getDefaultMapColor())
          ),
          false
      );
      Registry.register(
          Registries.ITEM,
          LighterEnd.of(baseName + "_hanging_sign"),
          new HangingSignItem(
              hangingSign,
              wallHangingSign,
              new Item.Settings().maxCount(16).registryKey(
                  RegistryKey.of(RegistryKeys.ITEM, LighterEnd.of(baseName + "_hanging_sign"))
              ).useBlockPrefixedTranslationKey()
          )
      );

      for (Block block : Arrays.asList(log, strippedLog, wood, strippedWood)) {
        FlammableBlockRegistry.getDefaultInstance().add(block, 5, 5);
      }
      for (Block block : Arrays.asList(planks, slab, stairs, fence, gate)) {
        FlammableBlockRegistry.getDefaultInstance().add(block, 5, 20);
      }

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
          hangingSign
      );
    }

    public Settings applyLogSettings(Settings settings) {
      return settings
          .instrument(NoteBlockInstrument.BASS)
          .sounds(this.logSounds)
          .strength(2.0F)
          .burnable();
    }

    public Settings applyPlankSettings(Settings settings) {
      return settings
          .mapColor(this.woodColor)
          .instrument(NoteBlockInstrument.BASS)
          .sounds(this.woodType.soundType())
          .strength(2.0F, 3.0F)
          .burnable();
    }
  }

  public static WoodType createWoodType(String name) {
    BlockSoundGroup soundGroup = createWoodSoundGroup(name);

    return (new WoodTypeBuilder())
        .soundGroup(soundGroup)
        .hangingSignSoundGroup(createWoodSoundGroup(name + "_hanging_sign"))
        .fenceGateCloseSound(LighterEndSounds.register("block." + name + "_fence_gate.close"))
        .fenceGateOpenSound(LighterEndSounds.register("block." + name + "_fence_gate.open"))
        .register(LighterEnd.of(name), createWoodSetType(name, soundGroup));
  }

  private static BlockSetType createWoodSetType(String name, BlockSoundGroup soundGroup) {
    return (new BlockSetTypeBuilder())
        .openableByHand(true)
        .openableByWindCharge(true)
        .buttonActivatedByArrows(true)
        .pressurePlateActivationRule(BlockSetType.ActivationRule.EVERYTHING)
        .soundGroup(soundGroup)
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

  private static BlockSoundGroup createWoodSoundGroup(String name) {
    return new BlockSoundGroup(
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
