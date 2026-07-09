package net.claustra01.tfchammer;

import net.dries007.tfc.client.model.ContainedFluidModel;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.claustra01.tfchammer.common.items.ModItems;

public final class ClientEventHandler {
    private ClientEventHandler() {}

    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(ClientEventHandler::registerColorHandlerItems);
    }

    public static void registerColorHandlerItems(RegisterColorHandlersEvent.Item event) {
        event.register(
                ContainedFluidModel.COLOR,
                ModItems.EXCAVATOR_HEAD_MOLD.get(),
                ModItems.SLEDGEHAMMER_HEAD_MOLD.get()
        );
    }
}
