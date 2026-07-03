package io.github.openbagtwo.lighterend.config;


import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.config.Config.ConfigException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class ConfigScreen extends OptionsSubScreen {

  private OptionsList widgets;

  private static String REQUIRES_RESTART = "Restart Minecraft to apply changes";

  public ConfigScreen(Screen previous) {
    super(previous, Minecraft.getInstance().options, Component.nullToEmpty(LighterEnd.MOD_NAME));
  }

  @Override
  protected void addOptions() {
    if (this.list != null) {
      this.list.addBig(
          OptionInstance.createBoolean(
              "Generate Modded Biomes",
              OptionInstance.cachedConstantTooltip(Component.nullToEmpty(REQUIRES_RESTART)),
              LighterEnd.CONFIG.generateBiomes,
              (value) -> {
                LighterEnd.CONFIG.generateBiomes = value;
              }
          )
      );
      this.list.addBig(
          OptionInstance.createBoolean(
              "Generate Ores",
              OptionInstance.cachedConstantTooltip(Component.nullToEmpty(
                  "Whether to generate redstone and quartz ores (including in vanilla biomes)"
                      + "\n\n" + REQUIRES_RESTART)),
              LighterEnd.CONFIG.generateOres,
              (value) -> {
                LighterEnd.CONFIG.generateOres = value;
              }
          )
      );
      this.list.addBig(
          OptionInstance.createBoolean(
              "Play Modded Music in End Biomes",
              LighterEnd.CONFIG.playEndBiomeMusic,
              (value) -> {
                LighterEnd.CONFIG.playEndBiomeMusic = value;
              }
          )
      );
      this.list.addBig(
          new OptionInstance<>(
              "End Gravity",
              OptionInstance.cachedConstantTooltip(
                  Component.nullToEmpty("Set to 1.0 for vanilla" + "\n\n" + REQUIRES_RESTART)
              ),
              (optionText, value) -> Options.genericValueLabel(optionText,
                  Component.literal(String.valueOf(.01 * value))),
              new OptionInstance.IntRange(5, 100, false),
              Mth.floor(100 * LighterEnd.CONFIG.endGravity),
              value -> {
                LighterEnd.CONFIG.endGravity = 0.01 * value;
              }
          )
      );
      this.list.addBig(
          OptionInstance.createBoolean(
              "Disable End Gravity While Flying",
              OptionInstance.cachedConstantTooltip(
                  Component.nullToEmpty(
                      "Lowering gravity nerfs unpowered glide speed otherwise"
                          + "\n\n" + REQUIRES_RESTART
                  )
              ),
              LighterEnd.CONFIG.disableEndGravityWhileFlying,
              (value) -> {
                LighterEnd.CONFIG.disableEndGravityWhileFlying = value;
              }
          )
      );
      this.list.addBig(
          OptionInstance.createBoolean(
              "Modded Plants Can Only Grow in The End",
              LighterEnd.CONFIG.endPlantsOnlyGrowInTheEnd, (value) -> {
                LighterEnd.CONFIG.endPlantsOnlyGrowInTheEnd = value;
              }
          )
      );
      this.list.addBig(
          OptionInstance.createBoolean(
              "Mod Music Discs Can Be Found in End Cities",
              OptionInstance.cachedConstantTooltip(Component.nullToEmpty(REQUIRES_RESTART)),
              LighterEnd.CONFIG.musicDiscsInEndCities, (value) -> {
                LighterEnd.CONFIG.musicDiscsInEndCities = value;
              }
          )
      );
      this.list.addBig(
          OptionInstance.createBoolean(
              "Use Custom Fishing Loot in The End",
              LighterEnd.CONFIG.customEndFishing, (value) -> {
                LighterEnd.CONFIG.customEndFishing = value;
              }
          )
      );
      this.list.addBig(
          OptionInstance.createBoolean(
              "Custom Aquatic Vegetation Bonemealing",
              OptionInstance.cachedConstantTooltip(Component.nullToEmpty(
                  "With this option enabled, bonemealing underwater in The End will grow modded flora"
              )),
              LighterEnd.CONFIG.bonemealUnderwaterInEndMakesEndVegetation, (value) -> {
                LighterEnd.CONFIG.bonemealUnderwaterInEndMakesEndVegetation = value;
              }
          )
      );
      this.list.addBig(
          OptionInstance.createBoolean(
              "Silence Minecraft Warnings about 'Unsafe Terrain Reads'",
              OptionInstance.cachedConstantTooltip(Component.nullToEmpty(
                  "LighterEnd generates terrain at a distance larger than Minecraft is comfortable with."
                      + "\nThis produces a lot of log spam."
                      + "\nWhen this option is enabled, those warnings will be suppressed."
              )),
              LighterEnd.CONFIG.silenceUnsafeTerrainReadWarnings, (value) -> {
                LighterEnd.CONFIG.silenceUnsafeTerrainReadWarnings = value;
              }
          )
      );
      this.list.addBig(
          OptionInstance.createBoolean(
              "End World Spawn",
              OptionInstance.cachedConstantTooltip(Component.nullToEmpty(
                  "New worlds will have their spawn points in The End."
                      + "\n\nHighly experimental.\nEnable at your own risk."
              )),
              LighterEnd.CONFIG.endSpawn, (value) -> {
                LighterEnd.CONFIG.endSpawn = value;
              }
          )
      );
    }
  }

  @Override
  public void removed() {
    try {
      LighterEnd.CONFIG.writeConfigToFile();
    } catch (ConfigException e) {
      LighterEnd.LOGGER.error(String.valueOf(e));
    }
  }
}
