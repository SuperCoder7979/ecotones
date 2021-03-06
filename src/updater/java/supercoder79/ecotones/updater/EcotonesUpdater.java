package supercoder79.ecotones.updater;

import supercoder79.ecotones.updater.mc117.AddAmethystGeodesUpdate;
import supercoder79.ecotones.updater.mc117.RangeOfUpdate;
import supercoder79.ecotones.updater.mc117.UniformIntUpdate;
import supercoder79.updater.Updater;

import java.nio.file.Paths;

public class EcotonesUpdater {
    public static void main(String[] args) {
        Updater updater = new Updater();
        updater.loadPath(Paths.get(args[0]));
//        updater.register(new UniformIntUpdate());
//        updater.register(new RangeOfUpdate());
//        updater.register(new AddAmethystGeodesUpdate());
        updater.run();
    }
}
