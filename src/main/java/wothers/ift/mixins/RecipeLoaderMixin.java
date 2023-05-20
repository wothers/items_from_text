package wothers.ift.mixins;

import com.google.gson.JsonElement;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wothers.hr.HyperRegistry;
import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeLoaderMixin {
    @Inject(method = "apply", at = @At(value = "HEAD"))
    private void appendMap(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        map.putAll(HyperRegistry.Recipe.INSTANCE.getMap());
    }
}
