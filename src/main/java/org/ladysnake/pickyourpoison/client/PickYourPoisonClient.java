package org.ladysnake.pickyourpoison.client;

import org.ladysnake.pickyourpoison.client.render.entity.PoisonDartEntityRenderer;
import org.ladysnake.pickyourpoison.client.render.entity.PoisonDartFrogEntityRenderer;
import org.ladysnake.pickyourpoison.client.render.model.FrogOnHeadModel;
import org.ladysnake.pickyourpoison.common.PickYourPoison;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

public class PickYourPoisonClient implements ClientModInitializer {
    public static final ArrayList<UUID> FROGGY_PLAYERS_CLIENT = new ArrayList<>();
    private static final String FROGGY_PLAYERS_URL = "https://doctor4t.ladysnake.org/pyp-data";
    private static final ManagedShaderEffect BLACK_SCREEN = ShaderEffectManager.getInstance()
            .manage(new Identifier("pickyourpoison", "shaders/post/blackscreen.json"));

    @Override
    public void onInitializeClient() {
        // FROGGY COSMETICS
        new ClientFroggyPlayerListLoaderThread().start();

        // MODEL LAYERS
        EntityRendererRegistry.register(PickYourPoison.POISON_DART_FROG, PoisonDartFrogEntityRenderer::new);
        EntityRendererRegistry.register(PickYourPoison.POISON_DART, PoisonDartEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(FrogOnHeadModel.MODEL_LAYER, FrogOnHeadModel::getTexturedModelData);

        // COMA SHADER
        ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
            if (MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.hasStatusEffect(PickYourPoison.COMATOSE) && !MinecraftClient.getInstance().player.isSpectator() && !MinecraftClient.getInstance().player.isCreative() && !MinecraftClient.getInstance().player.isSpectator() && !MinecraftClient.getInstance().player.isCreative()) {
                BLACK_SCREEN.render(tickDelta);
            }
        });

        // TRINKETS COMPAT
        if (PickYourPoison.isTrinketsLoaded) {
            TrinketsCompat.registerFrogTrinketRenderers(PickYourPoison.getAllFrogBowls());
        }
    }

    private static class ClientFroggyPlayerListLoaderThread extends Thread {
        public ClientFroggyPlayerListLoaderThread() {
            setName("Pick Your Poison Equippable Frogs Thread");
            setDaemon(true);
        }

        @Override
        public void run() {
            try (BufferedInputStream stream = IOUtils.buffer(new URL(FROGGY_PLAYERS_URL).openStream())) {
                Properties properties = new Properties();
                properties.load(stream);
                synchronized (FROGGY_PLAYERS_CLIENT) {
                    FROGGY_PLAYERS_CLIENT.clear();
                    for (Object o : PickYourPoison.JsonReader.readJsonFromUrl(FROGGY_PLAYERS_URL).toList()) {
                        FROGGY_PLAYERS_CLIENT.add(UUID.fromString((String) o));
                    }
//                    System.out.println(FROGGY_PLAYERS);
                }
            } catch (IOException e) {
                System.out.println("Failed to load froggy list.");
            }
        }

    }

}
