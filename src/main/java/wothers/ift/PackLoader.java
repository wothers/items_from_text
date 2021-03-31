package wothers.ift;

import java.nio.file.Paths;
import java.util.function.Consumer;

import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProfile.Factory;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;

public class PackLoader implements ResourcePackProvider {
    public void register(Consumer<ResourcePackProfile> consumer, Factory factory) {
        String name = "Resources for the Items From Text mod.";
        ResourcePackProfile container = ResourcePackProfile.of(name, true,
                () -> new DirectoryResourcePack(Paths.get("resources", "Items From Text").toFile()), factory,
                ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.PACK_SOURCE_BUILTIN);
        if (container != null) {
            consumer.accept(container);
        }
    }
}