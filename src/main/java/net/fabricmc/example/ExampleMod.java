package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.item.*;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class ExampleMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("test_mod");

	public static final Block GRADIENT_DIRT = new OreBlock(FabricBlockSettings.copyOf(Blocks.DIRT).luminance(15), UniformIntProvider.create(30, 70));
	public static final Item GRADIENT_BERRIES = new GradientBerriesItem((new FabricItemSettings()).group(ItemGroup.FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).alwaysEdible().build()));
	public static final Item GOLDEN_BERRIES = new GoldenBerriesItem((new FabricItemSettings()).group(ItemGroup.FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(2.4F).alwaysEdible().build()));

	private static OreFeatureConfig OVERWORLD_GRADIENT_DIRT_CONFIGURATION = new OreFeatureConfig(new BlockMatchRuleTest(Blocks.DIRT), GRADIENT_DIRT.getDefaultState(),4);
	private static ConfiguredFeature<?, ?> OVERWORLD_GRADIENT_DIRT_CONFIGURED_FEATURE = new ConfiguredFeature(Feature.ORE, OVERWORLD_GRADIENT_DIRT_CONFIGURATION);
	private static List<PlacementModifier> OVERWORLD_GRADIENT_DIRT_MODIFIERS = Arrays.asList(CountPlacementModifier.of(40), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.fixed(48), YOffset.fixed(80)));
	public static PlacedFeature OVERWORLD_GRADIENT_DIRT_PLACED_FEATURE = new PlacedFeature(RegistryEntry.of(OVERWORLD_GRADIENT_DIRT_CONFIGURED_FEATURE), OVERWORLD_GRADIENT_DIRT_MODIFIERS);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		Registry.register(Registry.BLOCK, new Identifier("test_mod", "gradient_dirt"), GRADIENT_DIRT);
		Registry.register(Registry.ITEM, new Identifier("test_mod", "gradient_dirt"), new BlockItem(GRADIENT_DIRT, new FabricItemSettings().group(ItemGroup.MISC)));
		Registry.register(Registry.ITEM, new Identifier("test_mod", "gradient_berries"), GRADIENT_BERRIES);
		Registry.register(Registry.ITEM, new Identifier("test_mod", "golden_berries"), GOLDEN_BERRIES);

		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier("test_mod", "gradient_dirt_ore"), OVERWORLD_GRADIENT_DIRT_CONFIGURED_FEATURE);
		Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier("test_mod", "gradient_dirt_ore"), OVERWORLD_GRADIENT_DIRT_PLACED_FEATURE);
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier("test_mod", "gradient_dirt_ore")));
	}
}
