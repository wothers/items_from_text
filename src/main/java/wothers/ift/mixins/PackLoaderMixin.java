package wothers.ift.mixins;

import java.util.HashSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProvider;
import wothers.ift.PackLoader;

@Mixin(ResourcePackManager.class)
public abstract class PackLoaderMixin {
    @Shadow
    private Set<ResourcePackProvider> providers;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void registerLoader(CallbackInfo info) {
        this.providers = new HashSet<ResourcePackProvider>(this.providers);
        this.providers.add(new PackLoader());
    }
}