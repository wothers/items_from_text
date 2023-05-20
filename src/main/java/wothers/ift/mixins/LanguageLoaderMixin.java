package wothers.ift.mixins;

import com.google.gson.JsonObject;
import net.minecraft.util.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import wothers.hr.HyperRegistry;
import java.util.Map;

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
