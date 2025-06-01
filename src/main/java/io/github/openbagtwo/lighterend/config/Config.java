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
   * Whether to add the mod's music discs to End City loot tables
   */
  protected boolean musicDiscsInEndCities;

  /**
   * Whether using bonemeal underwater in The End should produce modded underwater vegetation
   */
  protected boolean bonemealUnderwaterInEndMakesEndVegetation;


  public boolean musicDiscsAreFoundInEndCities() {
    return this.musicDiscsInEndCities;
  }

  public boolean bonemealingUnderwaterInEndProducesEndVegetation() {
    return this.bonemealUnderwaterInEndMakesEndVegetation;
  }


  /**
   * Default values
   */
  private static final boolean DEFAULT_MUSIC_DISCS_IN_END_CITIES = true;
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
    writeme.put("music_discs_found_in_end_cities", this.musicDiscsInEndCities);
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
    config.musicDiscsInEndCities = DEFAULT_MUSIC_DISCS_IN_END_CITIES;
    config.bonemealUnderwaterInEndMakesEndVegetation = DEFAULT_UNDERWATER_BONEMEAL_SETTING;
    return config;
  }


  /**
   * Write a new configuration file with default options
   *
   * @throws ConfigException If the writer encounters any sort of IO error (permissions?)
   */
  private static void writeDefaultConfigFile() throws ConfigException {
    FileWriter configWriter;
    try {
      configWriter = new FileWriter(config_path.toFile());
    } catch (IOException e) {
      throw new ConfigException(
          "Could not open " + config_path + " for writing.", e
      );
    }
    Map<String, Object> writeme = new LinkedHashMap<>();
    writeme.put("music_discs_found_in_end_cities", DEFAULT_MUSIC_DISCS_IN_END_CITIES);
    writeme.put("bonemealing_underwater_in_the_end_produces_end_vegetation",
        DEFAULT_UNDERWATER_BONEMEAL_SETTING);

    (new Yaml(configFormat)).dump(writeme, configWriter);
    LighterEnd.LOGGER.info(
        "Wrote " + LighterEnd.MOD_NAME + " configuration file to " + config_path
    );
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
      // Now we actually construct the thing
      boolean musicDiscsInEndCities = Boolean.parseBoolean(
          settings.getOrDefault(
              "music_discs_found_in_end_cities",
              DEFAULT_MUSIC_DISCS_IN_END_CITIES
          ).toString()
      );
      boolean underwaterBonemealSetting = Boolean.parseBoolean(
          settings.getOrDefault(
              "bonemealing_underwater_in_the_end_produces_end_vegetation",
              DEFAULT_UNDERWATER_BONEMEAL_SETTING
          ).toString()
      );

      Config config = new Config();
      config.musicDiscsInEndCities = musicDiscsInEndCities;
      config.bonemealUnderwaterInEndMakesEndVegetation = underwaterBonemealSetting;
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
