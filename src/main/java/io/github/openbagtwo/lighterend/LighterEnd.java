package io.github.openbagtwo.lighterend;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LighterEnd implements ModInitializer {
	public static final String MOD_ID = "lighterend";
	public static final String MOD_NAME = "LighterEnd";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LighterEndBlocks.initialize();
	}
}