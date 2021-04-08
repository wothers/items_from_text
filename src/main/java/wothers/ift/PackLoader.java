package wothers.ift;

import java.util.function.Consumer;

import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProfile.Factory;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;

public class PackLoader implements ResourcePackProvider {
    public void register(Consumer<ResourcePackProfile> consumer, Factory factory) {
        ResourcePackProfile container = ResourcePackProfile.of(ItemsFromText.DESCRIPTION, true,
                () -> new DirectoryResourcePack(ItemsFromText.RESOURCES_FOLDER.toFile()), factory,
                ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.PACK_SOURCE_BUILTIN);
        if (container != null) {
            consumer.accept(container);
        }
    }
}