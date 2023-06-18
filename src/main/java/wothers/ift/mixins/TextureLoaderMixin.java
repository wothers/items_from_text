package wothers.ift.mixins;

import net.minecraft.resource.DefaultResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wothers.ift.ItemRegistry;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mixin(DefaultResourcePack.class)
public class TextureLoaderMixin {
    @Final
    @Shadow
    public Set<String> namespaces;

    @Inject(method = "getNamespaces", at = @At(value = "HEAD"), cancellable = true)
    private void getAppendedNamespaces(ResourceType type, CallbackInfoReturnable<Set<String>> cir) {
        Set<String> namespaces = new HashSet<>(this.namespaces);
        namespaces.addAll(ItemRegistry.INSTANCE.getNamespaces());
        cir.setReturnValue(Collections.unmodifiableSet(namespaces));
        cir.cancel();
    }

    @Inject(method = "contains", at = @At(value = "HEAD"), cancellable = true)
    private void overrideContains(ResourceType type, Identifier id, CallbackInfoReturnable<Boolean> cir) {
        if (!ItemRegistry.Texture.INSTANCE.getMap().containsKey(id.toString())) return;
        cir.setReturnValue(true);
        cir.cancel();
    }

    @Inject(method = "open", at = @At(value = "HEAD"), cancellable = true)
    private void openExternal(ResourceType type, Identifier id, CallbackInfoReturnable<InputStream> cir) throws IOException {
        Map<String, File> textureMap = ItemRegistry.Texture.INSTANCE.getMap();
        if (!textureMap.containsKey(id.toString())) return;
        cir.setReturnValue(new FileInputStream(textureMap.get(id.toString())));
        cir.cancel();
    }
}
