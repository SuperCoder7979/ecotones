package supercoder79.ecotones.client;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import supercoder79.ecotones.Ecotones;

public final class ClientPacketHandler {
    public static void init() {
        ClientSidePacketRegistry.INSTANCE.register(Ecotones.WORLD_TYPE, (packetContext, data) -> {
            ClientSidedServerData.isInEcotonesWorld = data.readBoolean();

            if (ClientSidedServerData.isInEcotonesWorld) {
                FogHandler.init(data.readLong());
            }
        });
    }
}
