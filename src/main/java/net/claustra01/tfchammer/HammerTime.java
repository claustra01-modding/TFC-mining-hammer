package net.claustra01.tfchammer;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.claustra01.tfchammer.common.items.ModItems;

@Mod(HammerTime.MOD_ID)
public final class HammerTime {
    public static final String MOD_ID = "tfchammer";

    public HammerTime(IEventBus modEventBus) {
        ModItems.ITEMS.register(modEventBus);
        modEventBus.addListener(HammerTime::addCreative);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientEventHandler.init(modEventBus);
        }
    }

    private static void addCreative(BuildCreativeModeTabContentsEvent event) {
        ModCreativeModeTab.addCreative(event);
    }
}
