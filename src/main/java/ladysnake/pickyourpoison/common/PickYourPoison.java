package ladysnake.pickyourpoison.common;

import ladysnake.pickyourpoison.common.entity.PoisonDartEntity;
import ladysnake.pickyourpoison.common.entity.PoisonDartFrogEntity;
import ladysnake.pickyourpoison.common.item.PoisonDartFrogBowlItem;
import ladysnake.pickyourpoison.common.item.ThrowingDartItem;
import ladysnake.pickyourpoison.common.statuseffect.EmptyStatusEffect;
import ladysnake.pickyourpoison.common.statuseffect.NumbnessStatusEffect;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Position;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.json.JSONArray;
import org.json.JSONException;
import software.bernie.geckolib.GeckoLib;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

public class PickYourPoison implements ModInitializer {
    public static final String MODID = "pickyourpoison";
    // STATUS EFFECTS
    public static final StatusEffect VULNERABILITY = registerStatusEffect("vulnerability", new EmptyStatusEffect(StatusEffectCategory.HARMFUL, 0xFF891C));
    //    public static final ArrayList<UUID> FROGGY_PLAYERS = new ArrayList<>();
    public static final StatusEffect COMATOSE = registerStatusEffect("comatose", new EmptyStatusEffect(StatusEffectCategory.HARMFUL, 0x35A2F3));
    public static final StatusEffect NUMBNESS = registerStatusEffect("numbness", new NumbnessStatusEffect(StatusEffectCategory.HARMFUL, 0x62B229));
    public static final StatusEffect TORPOR = registerStatusEffect("torpor", new EmptyStatusEffect(StatusEffectCategory.HARMFUL, 0xD8C0B8));
    public static final StatusEffect BATRACHOTOXIN = registerStatusEffect("batrachotoxin", new EmptyStatusEffect(StatusEffectCategory.HARMFUL, 0xEAD040));
    public static final StatusEffect STIMULATION = registerStatusEffect("stimulation", new EmptyStatusEffect(StatusEffectCategory.HARMFUL, 0xD85252).addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070635", 0.2f, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
    public static boolean isTrinketsLoaded;
    // ENTITIES
    public static EntityType<PoisonDartFrogEntity> POISON_DART_FROG;
    public static EntityType<PoisonDartEntity> POISON_DART;
    // ITEMS
    public static Item POISON_DART_FROG_SPAWN_EGG;
    public static PoisonDartFrogBowlItem BLUE_POISON_DART_FROG_BOWL;
    public static PoisonDartFrogBowlItem GOLDEN_POISON_DART_FROG_BOWL;
    public static PoisonDartFrogBowlItem GREEN_POISON_DART_FROG_BOWL;
    public static PoisonDartFrogBowlItem ORANGE_POISON_DART_FROG_BOWL;
    public static PoisonDartFrogBowlItem CRIMSON_POISON_DART_FROG_BOWL;
    public static PoisonDartFrogBowlItem RED_POISON_DART_FROG_BOWL;
    public static PoisonDartFrogBowlItem LUXALAMANDER_BOWL;
    public static PoisonDartFrogBowlItem RANA_BOWL;
    public static Item THROWING_DART;
    public static Item COMATOSE_POISON_DART;
    public static Item BATRACHOTOXIN_POISON_DART;
    public static Item NUMBNESS_POISON_DART;
    public static Item VULNERABILITY_POISON_DART;
    public static Item TORPOR_POISON_DART;
    public static Item STIMULATION_POISON_DART;
    public static Item BLINDNESS_POISON_DART;
    // SOUNDS
    public static SoundEvent ENTITY_POISON_DART_FROG_AMBIENT = SoundEvent.of(id("entity.poison_dart_frog.ambient"));
    public static SoundEvent ENTITY_POISON_DART_FROG_HURT = SoundEvent.of(id("entity.poison_dart_frog.hurt"));
    public static SoundEvent ENTITY_POISON_DART_FROG_DEATH = SoundEvent.of(id("entity.poison_dart_frog.death"));
    public static SoundEvent ENTITY_POISON_DART_HIT = SoundEvent.of(id("entity.poison_dart.hit"));
    public static SoundEvent ITEM_POISON_DART_FROG_BOWL_FILL = SoundEvent.of(id("item.poison_dart_frog_bowl.fill"));
    public static SoundEvent ITEM_POISON_DART_FROG_BOWL_EMPTY = SoundEvent.of(id("item.poison_dart_frog_bowl.empty"));
    public static SoundEvent ITEM_POISON_DART_FROG_BOWL_LICK = SoundEvent.of(id("item.poison_dart_frog_bowl.lick"));
    public static SoundEvent ITEM_POISON_DART_COAT = SoundEvent.of(id("item.poison_dart.coat"));
    public static SoundEvent ITEM_POISON_DART_THROW = SoundEvent.of(id("item.poison_dart.throw"));

    public static Identifier id(String path) {
        return new Identifier(MODID, path);
    }

    private static <T extends Entity> EntityType<T> registerEntity(String name, EntityType<T> entityType) {
        return Registry.register(Registries.ENTITY_TYPE, id(name), entityType);
    }

    public static <T extends Item> T registerItem(String name, T item) {
        Registry.register(Registries.ITEM, id(name), item);
        return item;
    }

    public static Item registerDartItem(String name, Item item) {
        registerItem(name, item);

        DispenserBlock.registerBehavior(item, new ProjectileDispenserBehavior() {
            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack itemStack) {
                PoisonDartEntity throwingDart = new PoisonDartEntity(world, position.getX(), position.getY(), position.getZ());
                throwingDart.setDamage(throwingDart.getDamage());
                throwingDart.setItem(itemStack);
                StatusEffectInstance statusEffectInstance = ThrowingDartItem.class.cast(itemStack.getItem()).getStatusEffectInstance();
                if (statusEffectInstance != null) {
                    StatusEffectInstance potion = new StatusEffectInstance(statusEffectInstance);
                    throwingDart.addEffect(potion);
                    throwingDart.setColor(potion.getEffectType().getColor());
                }

                itemStack.decrement(1);
                return throwingDart;
            }
        });

        return item;
    }

    private static <T extends StatusEffect> T registerStatusEffect(String name, T effect) {
        Registry.register(Registries.STATUS_EFFECT, id(name), effect);
        return effect;
    }

    public static PoisonDartFrogBowlItem[] getAllFrogBowls() {
        return new PoisonDartFrogBowlItem[]{
                PickYourPoison.BLUE_POISON_DART_FROG_BOWL,
                PickYourPoison.RED_POISON_DART_FROG_BOWL,
                PickYourPoison.CRIMSON_POISON_DART_FROG_BOWL,
                PickYourPoison.GREEN_POISON_DART_FROG_BOWL,
                PickYourPoison.GOLDEN_POISON_DART_FROG_BOWL,
                PickYourPoison.ORANGE_POISON_DART_FROG_BOWL,
                PickYourPoison.LUXALAMANDER_BOWL,
                PickYourPoison.RANA_BOWL
        };
    }

    // INIT
    @Override
    public void onInitialize() {
        GeckoLib.initialize();

        // is trinkets loaded?
        isTrinketsLoaded = FabricLoader.getInstance().isModLoaded("trinkets");

        // FROGGY COSMETICS
//        ServerLifecycleEvents.SERVER_STARTING.register(server -> new FroggyPlayerListLoaderThread().start());
//        ServerLifecycleEvents.SERVER_STOPPING.register(server -> FROGGY_PLAYERS.clear());

        // ENTITIES
        POISON_DART_FROG = registerEntity("poison_dart_frog", FabricEntityTypeBuilder.createMob().entityFactory(PoisonDartFrogEntity::new).spawnGroup(SpawnGroup.CREATURE).dimensions(EntityDimensions.changing(0.5F, 0.4F)).spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, PoisonDartFrogEntity::canMobSpawn).build());
        FabricDefaultAttributeRegistry.register(POISON_DART_FROG, PoisonDartFrogEntity.createPoisonDartFrogAttributes());
        BiomeModifications.addSpawn(
                biome -> biome.hasTag(ConventionalBiomeTags.JUNGLE),
                SpawnGroup.CREATURE, POISON_DART_FROG, 50, 2, 5
        );
        POISON_DART = registerEntity("poison_dart", FabricEntityTypeBuilder.<PoisonDartEntity>create(SpawnGroup.MISC, PoisonDartEntity::new).dimensions(EntityDimensions.changing(0.5f, 0.5f)).trackRangeBlocks(4).trackedUpdateRate(20).build());

        // ITEMS
        POISON_DART_FROG_SPAWN_EGG = registerItem("poison_dart_frog_spawn_egg", new SpawnEggItem(POISON_DART_FROG, 0x5BBCF4, 0x22286B, (new Item.Settings())));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register((entries) -> entries.add(POISON_DART_FROG_SPAWN_EGG));

        BLUE_POISON_DART_FROG_BOWL = registerItem("blue_poison_dart_frog_bowl", new PoisonDartFrogBowlItem((new Item.Settings()).maxCount(1), id("textures/entity/blue.png")));
        GOLDEN_POISON_DART_FROG_BOWL = registerItem("golden_poison_dart_frog_bowl", new PoisonDartFrogBowlItem((new Item.Settings()).maxCount(1), id("textures/entity/golden.png")));
        GREEN_POISON_DART_FROG_BOWL = registerItem("green_poison_dart_frog_bowl", new PoisonDartFrogBowlItem((new Item.Settings()).maxCount(1), id("textures/entity/green.png")));
        ORANGE_POISON_DART_FROG_BOWL = registerItem("orange_poison_dart_frog_bowl", new PoisonDartFrogBowlItem((new Item.Settings()).maxCount(1), id("textures/entity/orange.png")));
        CRIMSON_POISON_DART_FROG_BOWL = registerItem("crimson_poison_dart_frog_bowl", new PoisonDartFrogBowlItem((new Item.Settings()).maxCount(1), id("textures/entity/crimson.png")));
        RED_POISON_DART_FROG_BOWL = registerItem("red_poison_dart_frog_bowl", new PoisonDartFrogBowlItem((new Item.Settings()).maxCount(1), id("textures/entity/red.png")));
        LUXALAMANDER_BOWL = registerItem("luxalamander_bowl", new PoisonDartFrogBowlItem((new Item.Settings()).maxCount(1).rarity(Rarity.RARE), id("textures/entity/luxintrus.png")));
        RANA_BOWL = registerItem("rana_bowl", new PoisonDartFrogBowlItem((new Item.Settings()).maxCount(1).rarity(Rarity.RARE), id("textures/entity/rana.png")));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register((entries) -> entries.addAll(Stream.of(
                BLUE_POISON_DART_FROG_BOWL,
                GOLDEN_POISON_DART_FROG_BOWL,
                GREEN_POISON_DART_FROG_BOWL,
                ORANGE_POISON_DART_FROG_BOWL,
                CRIMSON_POISON_DART_FROG_BOWL,
                RED_POISON_DART_FROG_BOWL,
                LUXALAMANDER_BOWL,
                RANA_BOWL
        ).map(Item::getDefaultStack).toList()));
        THROWING_DART = registerDartItem("throwing_dart", new ThrowingDartItem((new Item.Settings()).maxCount(64), null));
        COMATOSE_POISON_DART = registerDartItem("comatose_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(1), new StatusEffectInstance(PickYourPoison.COMATOSE, 100))); // 5s
        BATRACHOTOXIN_POISON_DART = registerDartItem("batrachotoxin_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(1), new StatusEffectInstance(PickYourPoison.BATRACHOTOXIN, 80))); // 4s
        NUMBNESS_POISON_DART = registerDartItem("numbness_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(1), new StatusEffectInstance(PickYourPoison.NUMBNESS, 200))); // 10s
        VULNERABILITY_POISON_DART = registerDartItem("vulnerability_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(1), new StatusEffectInstance(PickYourPoison.VULNERABILITY, 200))); // 10s
        TORPOR_POISON_DART = registerDartItem("torpor_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(1), new StatusEffectInstance(PickYourPoison.TORPOR, 200))); // 10s
        STIMULATION_POISON_DART = registerDartItem("stimulation_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(1), new StatusEffectInstance(PickYourPoison.STIMULATION, 600))); // 30s
        BLINDNESS_POISON_DART = registerDartItem("blindness_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(1), new StatusEffectInstance(StatusEffects.BLINDNESS, 200))); // 10s
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((entries) -> entries.addAll(Stream.of(
                THROWING_DART,
                COMATOSE_POISON_DART,
                BATRACHOTOXIN_POISON_DART,
                NUMBNESS_POISON_DART,
                VULNERABILITY_POISON_DART,
                TORPOR_POISON_DART,
                STIMULATION_POISON_DART,
                BLINDNESS_POISON_DART
        ).map(Item::getDefaultStack).toList()));

        // SOUNDS
        ENTITY_POISON_DART_FROG_AMBIENT = Registry.register(Registries.SOUND_EVENT, ENTITY_POISON_DART_FROG_AMBIENT.getId(), ENTITY_POISON_DART_FROG_AMBIENT);
        ENTITY_POISON_DART_FROG_HURT = Registry.register(Registries.SOUND_EVENT, ENTITY_POISON_DART_FROG_HURT.getId(), ENTITY_POISON_DART_FROG_HURT);
        ENTITY_POISON_DART_FROG_DEATH = Registry.register(Registries.SOUND_EVENT, ENTITY_POISON_DART_FROG_DEATH.getId(), ENTITY_POISON_DART_FROG_DEATH);
        ENTITY_POISON_DART_HIT = Registry.register(Registries.SOUND_EVENT, ENTITY_POISON_DART_HIT.getId(), ENTITY_POISON_DART_HIT);
        ITEM_POISON_DART_FROG_BOWL_FILL = Registry.register(Registries.SOUND_EVENT, ITEM_POISON_DART_FROG_BOWL_FILL.getId(), ITEM_POISON_DART_FROG_BOWL_FILL);
        ITEM_POISON_DART_FROG_BOWL_EMPTY = Registry.register(Registries.SOUND_EVENT, ITEM_POISON_DART_FROG_BOWL_EMPTY.getId(), ITEM_POISON_DART_FROG_BOWL_EMPTY);
        ITEM_POISON_DART_FROG_BOWL_LICK = Registry.register(Registries.SOUND_EVENT, ITEM_POISON_DART_FROG_BOWL_LICK.getId(), ITEM_POISON_DART_FROG_BOWL_LICK);
        ITEM_POISON_DART_COAT = Registry.register(Registries.SOUND_EVENT, ITEM_POISON_DART_COAT.getId(), ITEM_POISON_DART_COAT);
        ITEM_POISON_DART_THROW = Registry.register(Registries.SOUND_EVENT, ITEM_POISON_DART_THROW.getId(), ITEM_POISON_DART_THROW);

        // TICK
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            for (ServerPlayerEntity player : world.getPlayers()) {
                if (player.hasStatusEffect(TORPOR) && (player.age % (100 / (MathHelper.clamp(player.getStatusEffect(TORPOR).getAmplifier() + 1, 1, 20))) == 0)) {
                    player.getHungerManager().add(1, 0);
                }
            }
        });
    }

//    private static class FroggyPlayerListLoaderThread extends Thread {
//        public FroggyPlayerListLoaderThread() {
//            setName("Pick Your Poison Equippable Frogs Thread");
//            setDaemon(true);
//        }
//
//        @Override
//        public void run() {
//            try (BufferedInputStream stream = IOUtils.buffer(new URL(FROGGY_PLAYERS_URL).openStream())) {
//                Properties properties = new Properties();
//                properties.load(stream);
//                synchronized (FROGGY_PLAYERS) {
//                    FROGGY_PLAYERS.clear();
//                    for (Object o : JsonReader.readJsonFromUrl(FROGGY_PLAYERS_URL).toList()) {
//                        FROGGY_PLAYERS.add(UUID.fromString((String) o));
//                    }
////                    System.out.println(FROGGY_PLAYERS);
//                }
//            } catch (IOException e) {
//                System.out.println("Failed to load froggy list.");
//            }
//        }
//    }

    public static class JsonReader {
        private static String readAll(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }

        public static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
            try (InputStream is = new URL(url).openStream()) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String jsonText = readAll(rd);
                return new JSONArray(jsonText);
            }
        }
    }
}
