package net.claustra01.tfchammer;

import net.dries007.tfc.common.TFCCreativeTabs;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.claustra01.tfchammer.common.items.ModItems;

public final class ModCreativeModeTab {
    private ModCreativeModeTab() {}

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == TFCCreativeTabs.METAL.tab().getKey()) {
            ModItems.SLEDGEHAMMERS.values().forEach(event::accept);
            ModItems.SLEDGEHAMMER_HEADS.values().forEach(event::accept);
            ModItems.EXCAVATORS.values().forEach(event::accept);
            ModItems.EXCAVATOR_HEADS.values().forEach(event::accept);
            ModItems.TFCM_SLEDGEHAMMERS.values().forEach(event::accept);
            ModItems.TFCM_SLEDGEHAMMER_HEADS.values().forEach(event::accept);
            ModItems.TFCM_EXCAVATORS.values().forEach(event::accept);
            ModItems.TFCM_EXCAVATOR_HEADS.values().forEach(event::accept);
        }

        if (event.getTabKey() == TFCCreativeTabs.MISC.tab().getKey()) {
            event.accept(ModItems.UNFIRED_EXCAVATOR_HEAD_MOLD);
            event.accept(ModItems.EXCAVATOR_HEAD_MOLD);
            event.accept(ModItems.UNFIRED_SLEDGEHAMMER_HEAD_MOLD);
            event.accept(ModItems.SLEDGEHAMMER_HEAD_MOLD);
        }
    }
}
