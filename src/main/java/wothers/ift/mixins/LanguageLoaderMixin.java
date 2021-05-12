package wothers.ift.mixins;

import java.util.Map;
import com.google.gson.JsonObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import net.minecraft.util.Language;
import wothers.hr.HyperRegistry;

@Mixin(Language.class)
public class LanguageLoaderMixin {
    @ModifyVariable(method = "load", at = @At(value = "INVOKE_ASSIGN"), ordinal = 0)
    private static JsonObject appendJson(JsonObject jsonObject) {
        for (Map.Entry<String, String> entry : HyperRegistry.INSTANCE.getLangMap().entrySet()) {
            jsonObject.addProperty(entry.getKey(), entry.getValue());
        }
        return jsonObject;
    }
}
