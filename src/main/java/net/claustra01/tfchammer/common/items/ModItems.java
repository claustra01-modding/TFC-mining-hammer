package net.claustra01.tfchammer.common.items;

import java.util.Map;

import net.dries007.tfc.common.items.MoldItem;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
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

    private ModItems() {}
}
