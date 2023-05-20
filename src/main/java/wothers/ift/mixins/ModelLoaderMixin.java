package wothers.ift.mixins;

import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wothers.hr.HyperRegistry;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {
    @Inject(method = "loadModelFromJson", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourceManager;getResource(Lnet/minecraft/util/Identifier;)Lnet/minecraft/resource/Resource;"), cancellable = true)
    private void loadModelFromJson(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) {
        if (!HyperRegistry.INSTANCE.getRegisteredItems().containsKey(id.toString())) return;
        String modelJson = createItemModelJsonString(id.toString(), HyperRegistry.INSTANCE.getRegisteredItems().get(id.toString()));
        JsonUnbakedModel model = JsonUnbakedModel.deserialize(modelJson);
        model.id = id.toString();
        cir.setReturnValue(model);
        cir.cancel();
    }

    private String createItemModelJsonString(String id, String modelType) {
        return "{\n" + "  \"parent\": \"item/" + modelType + "\",\n" + "  \"textures\": {\n" + "    \"layer0\": \"" + id + "\"\n" + "  }\n" + "}";
    }
}
