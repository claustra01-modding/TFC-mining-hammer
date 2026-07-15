package net.claustra01.tfchammer.common.items;

import java.util.Map;
import java.util.Locale;

import net.dries007.tfc.common.items.MoldItem;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.claustra01.tfchammer.HammerTime;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HammerTime.MOD_ID);

    public static final Map<Metal, DeferredItem<Item>> SLEDGEHAMMERS = Helpers.mapOf(
            Metal.class,
            Metal.ItemType.PICKAXE::has,
            metal -> ITEMS.register(
                    "metal/sledgehammer/" + metal.getSerializedName(),
                    () -> new SledgeItem(
                            metal.toolTier(),
                            1.0f,
                            -3.1f,
                            BlockTags.MINEABLE_WITH_PICKAXE,
                            new Item.Properties().rarity(metal.rarity())
                    )
            )
    );

    public static final Map<Metal, DeferredItem<Item>> EXCAVATORS = Helpers.mapOf(
            Metal.class,
            Metal.ItemType.SHOVEL::has,
            metal -> ITEMS.register(
                    "metal/excavator/" + metal.getSerializedName(),
                    () -> new SledgeItem(
                            metal.toolTier(),
                            0.9f,
                            -3.1f,
                            BlockTags.MINEABLE_WITH_SHOVEL,
                            new Item.Properties().rarity(metal.rarity())
                    )
            )
    );

    public static final Map<Metal, DeferredItem<Item>> SLEDGEHAMMER_HEADS = Helpers.mapOf(
            Metal.class,
            Metal.ItemType.PICKAXE_HEAD::has,
            metal -> ITEMS.register(
                    "metal/sledgehammer_head/" + metal.getSerializedName(),
                    () -> new Item(new Item.Properties().rarity(metal.rarity()))
            )
    );

    public static final Map<Metal, DeferredItem<Item>> EXCAVATOR_HEADS = Helpers.mapOf(
            Metal.class,
            Metal.ItemType.SHOVEL_HEAD::has,
            metal -> ITEMS.register(
                    "metal/excavator_head/" + metal.getSerializedName(),
                    () -> new Item(new Item.Properties().rarity(metal.rarity()))
            )
    );

    public static final Map<String, DeferredItem<Item>> TFCM_SLEDGEHAMMERS = registerTfcmItems("sledgehammer", true, false);
    public static final Map<String, DeferredItem<Item>> TFCM_EXCAVATORS = registerTfcmItems("excavator", true, true);
    public static final Map<String, DeferredItem<Item>> TFCM_SLEDGEHAMMER_HEADS = registerTfcmItems("sledgehammer_head", false, false);
    public static final Map<String, DeferredItem<Item>> TFCM_EXCAVATOR_HEADS = registerTfcmItems("excavator_head", false, true);

    public static final DeferredItem<Item> UNFIRED_EXCAVATOR_HEAD_MOLD = ITEMS.registerSimpleItem("ceramic/unfired_excavator_head_mold");
    public static final DeferredItem<Item> UNFIRED_SLEDGEHAMMER_HEAD_MOLD = ITEMS.registerSimpleItem("ceramic/unfired_sledgehammer_head_mold");
    public static final DeferredItem<Item> EXCAVATOR_HEAD_MOLD = ITEMS.register(
            "ceramic/excavator_head_mold",
            () -> new MoldItem(Metal.ItemType.SHOVEL_HEAD, new Item.Properties())
    );
    public static final DeferredItem<Item> SLEDGEHAMMER_HEAD_MOLD = ITEMS.register(
            "ceramic/sledgehammer_head_mold",
            () -> new MoldItem(Metal.ItemType.PICKAXE_HEAD, new Item.Properties())
    );

    private static Map<String, DeferredItem<Item>> registerTfcmItems(String type, boolean tool, boolean excavator) {
        if (!ModList.get().isLoaded("tfcm")) {
            return Map.of();
        }

        return Map.of(
                "invar", registerTfcmItem(type, "invar", Rarity.COMMON, tool, excavator),
                "titanium", registerTfcmItem(type, "titanium", Rarity.UNCOMMON, tool, excavator),
                "tungsten_steel", registerTfcmItem(type, "tungsten_steel", Rarity.EPIC, tool, excavator),
                "netherite", registerTfcmItem(type, "netherite", Rarity.RARE, tool, excavator)
        );
    }

    private static DeferredItem<Item> registerTfcmItem(
            String type,
            String metal,
            Rarity rarity,
            boolean tool,
            boolean excavator
    ) {
        return ITEMS.register("metal/" + type + "/" + metal, () -> tool
                ? new SledgeItem(
                        tfcmTier(metal),
                        excavator ? 0.9f : 1.0f,
                        -3.1f,
                        excavator ? BlockTags.MINEABLE_WITH_SHOVEL : BlockTags.MINEABLE_WITH_PICKAXE,
                        new Item.Properties().rarity(rarity)
                )
                : new Item(new Item.Properties().rarity(rarity))
        );
    }

    private static Tier tfcmTier(String metal) {
        try {
            return (Tier) Class.forName("net.claustra01.tfcm.TfcmTiers")
                    .getField(metal.toUpperCase(Locale.ROOT))
                    .get(null);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Unable to load tfcm tool tier: " + metal, exception);
        }
    }

    private ModItems() {}
}
