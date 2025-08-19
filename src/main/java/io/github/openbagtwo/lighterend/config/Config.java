package io.github.openbagtwo.lighterend.config;


import io.github.openbagtwo.lighterend.LighterEnd;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import net.fabricmc.loader.api.FabricLoader;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * LighterEnd's configuration reader / writer
 */
public class Config {

  /**
   * Path to the config file
   */
  private static final Path config_path = FabricLoader.getInstance().getConfigDir()
      .resolve(LighterEnd.MOD_ID + ".yaml").toAbsolutePath();

  /**
   * Whether to add the mod's biomes to the worldgen
   */
  protected boolean generateBiomes;

  /**
   * Whether to generate redstone and quartz ores (including in vanilla biomes)
   */
  protected boolean generateOres;

  /**
   * Whether custom music should play in LighterEnd biomes
   */
  protected boolean playEndBiomeMusic;

  /**
   * The strength of gravity in the end (where 1.0 is normal)
   */
  protected double endGravity;

  /**
   * Whether the above gravity modification should be disabled while flying with elytra
   */
  protected boolean disableEndGravityWhileFlying;

  /**
   * Whether End trees (including lumecorn, lilies and lotuses) should be prevented from growing in
   * other dimensions
   */
  protected boolean endPlantsOnlyGrowInTheEnd;

  /**
   * Whether to add the mod's music discs to End City loot tables
   */
  protected boolean musicDiscsInEndCities;

  /**
   * Whether to use a custom loot table when fishing in The End
   */
  protected boolean customEndFishing;

  /**
   * Whether using bonemeal underwater in The End should produce modded underwater vegetation
   */
  protected boolean bonemealUnderwaterInEndMakesEndVegetation;


  public boolean generateBiomes() {
    return this.generateBiomes;
  }

  public boolean generateOres() {
    return this.generateOres;
  }

  public boolean playEndBiomeMusic() {
    return this.playEndBiomeMusic;
  }

  public double getEndGravity() {
    return this.endGravity;
  }

  public boolean disableEndGravityWhileFlying() {
    return this.disableEndGravityWhileFlying;
  }

  public boolean endPlantsOnlyGrowInTheEnd() {
    return this.endPlantsOnlyGrowInTheEnd;
  }

  public boolean musicDiscsAreFoundInEndCities() {
    return this.musicDiscsInEndCities;
  }

  public boolean endFishingHasCustomLootTable() {
    return this.customEndFishing;
  }

  public boolean bonemealingUnderwaterInEndProducesEndVegetation() {
    return this.bonemealUnderwaterInEndMakesEndVegetation;
  }

  /**
   * Default values
   */
  private static final boolean DEFAULT_BIOME_GENERATION = true;
  private static final boolean DEFAULT_GENERATE_ORES = false;
  private static final boolean DEFAULT_PLAY_END_BIOME_MUSIC = true;
  private static final double DEFAULT_END_GRAVITY = 0.3;
  private static final boolean DEFAULT_END_GRAVITY_DISABLED_WHILE_FLYING = false;
  private static final boolean DEFAULT_END_PLANTS_ONLY_GROW_IN_THE_END = true;
  private static final boolean DEFAULT_MUSIC_DISCS_IN_END_CITIES = true;
  private static final boolean DEFAULT_CUSTOM_END_FISHING = true;
  private static final boolean DEFAULT_UNDERWATER_BONEMEAL_SETTING = true;


  /**
   * Load the mod configuration, however you have to
   */
  public static Config loadConfiguration() {
    Config config;
    try {
      config = fromConfigFile();
      LighterEnd.LOGGER.debug("Loaded " + LighterEnd.MOD_NAME + " configuration.");
    } catch (FileNotFoundException e) {
      LighterEnd.LOGGER.warn("No " + LighterEnd.MOD_NAME + " configuration file found.");
      try {
        writeDefaultConfigFile();
      } catch (ConfigException writee) {
        LighterEnd.LOGGER.error(
            "Could not write " + LighterEnd.MOD_NAME + " configuration:\n" + writee);
      }
      config = getDefaultConfiguration();
    } catch (ConfigException e) {
      LighterEnd.LOGGER.error(LighterEnd.MOD_NAME + " configuration is invalid:\n" + e);
      config = getDefaultConfiguration();
    }
    return config;
  }

  private static final DumperOptions configFormat = new DumperOptions() {{
    this.setIndent(2);
    this.setPrettyFlow(true);
    this.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
  }};

  /**
   * Write the configuration to file
   *
   * @throws ConfigException If the writer encounters any sort of IO error (permissions?)
   */
  protected void writeConfigToFile() throws ConfigException {
    FileWriter configWriter;
    try {
      configWriter = new FileWriter(config_path.toFile());
    } catch (IOException e) {
      throw new ConfigException(
          "Could not open " + config_path + " for writing.", e
      );
    }
    Map<String, Object> writeme = new LinkedHashMap<>();
    writeme.put("generate_biomes", this.generateBiomes);
    writeme.put("generate_ores", this.generateOres);
    writeme.put("play_biome_music", this.playEndBiomeMusic);
    writeme.put("end_gravity", this.endGravity);
    writeme.put("disable_end_gravity_while_flying", this.disableEndGravityWhileFlying);
    writeme.put("end_plants_only_grow_in_the_end", this.endPlantsOnlyGrowInTheEnd);
    writeme.put("music_discs_found_in_end_cities", this.musicDiscsInEndCities);
    writeme.put("customize_end_fishing", this.customEndFishing);
    writeme.put("bonemealing_underwater_in_the_end_produces_end_vegetation",
        this.bonemealUnderwaterInEndMakesEndVegetation);

    (new Yaml(configFormat)).dump(writeme, configWriter);
    LighterEnd.LOGGER.info(
        "Wrote " + LighterEnd.MOD_NAME + " configuration file to " + config_path);
  }

  /**
   * If the configuration cannot be read in from file for whatever reason, generate and return the
   * default configuration
   */
  private static Config getDefaultConfiguration() {
    LighterEnd.LOGGER.info("Loading default " + LighterEnd.MOD_NAME + " configuration");
    Config config = new Config();
    config.generateBiomes = DEFAULT_BIOME_GENERATION;
    config.generateOres = DEFAULT_GENERATE_ORES;
    config.playEndBiomeMusic = DEFAULT_PLAY_END_BIOME_MUSIC;
    config.endGravity = DEFAULT_END_GRAVITY;
    config.disableEndGravityWhileFlying = DEFAULT_END_GRAVITY_DISABLED_WHILE_FLYING;
    config.endPlantsOnlyGrowInTheEnd = DEFAULT_END_PLANTS_ONLY_GROW_IN_THE_END;
    config.musicDiscsInEndCities = DEFAULT_MUSIC_DISCS_IN_END_CITIES;
    config.customEndFishing = DEFAULT_CUSTOM_END_FISHING;
    config.bonemealUnderwaterInEndMakesEndVegetation = DEFAULT_UNDERWATER_BONEMEAL_SETTING;
    return config;
  }


  /**
   * Write a new configuration file with default options
   *
   * @throws ConfigException If the writer encounters any sort of IO error (permissions?)
   */
  private static void writeDefaultConfigFile() throws ConfigException {
    getDefaultConfiguration().writeConfigToFile();
  }

  /**
   * Load the settings for the mod, either from file or from defaults
   *
   * @return map of str key to the configuration value
   * @throws ConfigException if the configuration cannot be parsed
   */
  private static Config fromConfigFile() throws FileNotFoundException, ConfigException {

    LighterEnd.LOGGER.debug(
        "Reading " + LighterEnd.MOD_NAME + " configuration from " + config_path
    );
    FileInputStream configReader = new FileInputStream(config_path.toFile());
    HashMap<String, Object> settings = new HashMap<>((new Yaml()).load(configReader));

    try {
      Config config = new Config();

      // Now we actually construct the thing
      config.generateBiomes = Boolean.parseBoolean(
          settings.getOrDefault(
              "generate_biomes",
              DEFAULT_BIOME_GENERATION
          ).toString()
      );
      config.generateOres = Boolean.parseBoolean(
          settings.getOrDefault(
              "generate_ores",
              DEFAULT_GENERATE_ORES
          ).toString()
      );
      config.playEndBiomeMusic = Boolean.parseBoolean(
          settings.getOrDefault(
              "play_biome_music",
              DEFAULT_BIOME_GENERATION
          ).toString()
      );
      config.endGravity = Double.parseDouble(
          settings.getOrDefault(
              "end_gravity",
              DEFAULT_END_GRAVITY
          ).toString()
      );
      config.disableEndGravityWhileFlying = Boolean.parseBoolean(
          settings.getOrDefault(
              "disable_end_gravity_while_flying",
              DEFAULT_END_GRAVITY_DISABLED_WHILE_FLYING
          ).toString()
      );
      config.endPlantsOnlyGrowInTheEnd = Boolean.parseBoolean(
          settings.getOrDefault(
              "end_plants_only_grow_in_the_end",
              DEFAULT_END_PLANTS_ONLY_GROW_IN_THE_END
          ).toString()
      );
      config.musicDiscsInEndCities = Boolean.parseBoolean(
          settings.getOrDefault(
              "music_discs_found_in_end_cities",
              DEFAULT_MUSIC_DISCS_IN_END_CITIES
          ).toString()
      );
      config.customEndFishing = Boolean.parseBoolean(
          settings.getOrDefault(
              "customize_end_fishing",
              DEFAULT_CUSTOM_END_FISHING
          ).toString()
      );
      config.bonemealUnderwaterInEndMakesEndVegetation = Boolean.parseBoolean(
          settings.getOrDefault(
              "bonemealing_underwater_in_the_end_produces_end_vegetation",
              DEFAULT_UNDERWATER_BONEMEAL_SETTING
          ).toString()
      );

      return config;

    } catch (Exception e) {
      throw new ConfigException(
          config_path + " is not a valid " + LighterEnd.MOD_NAME + " configuration.",
          e
      );
    }
  }

  protected static class ConfigException extends Exception {

    ConfigException(String message, Exception e) {
      super(message, e);
    }

    ConfigException(String message) {
      super(message);
    }
  }

}
