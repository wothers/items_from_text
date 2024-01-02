package wothers.ift.mixins;

import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wothers.ift.ItemRegistry;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {
    @Inject(method = "loadModelFromJson", at = @At(value = "INVOKE", target="Lnet/minecraft/resource/ResourceManager;openAsReader(Lnet/minecraft/util/Identifier;)Ljava/io/BufferedReader;"), cancellable = true)
    private void loadModelFromJson(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) {
        if (!ItemRegistry.INSTANCE.getRegisteredItems().containsKey(id.toString())) return;
        String modelJson = ift$createItemModelJsonString(id.toString(), ItemRegistry.INSTANCE.getRegisteredItems().get(id.toString()));
        JsonUnbakedModel model = JsonUnbakedModel.deserialize(modelJson);
        model.id = id.toString();
        cir.setReturnValue(model);
        cir.cancel();
    }

    @Unique
    private String ift$createItemModelJsonString(String id, String modelType) {
        return "{\n" + "  \"parent\": \"item/" + modelType + "\",\n" + "  \"textures\": {\n" + "    \"layer0\": \"" + id + "\"\n" + "  }\n" + "}";
    }
}
