package huix.mixins.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import net.minecraft.util.StringTranslate;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

@Mixin( StringTranslate.class )
public class StringTranslateMixin implements IIStringTranslate{

    @Shadow
    @Final
    private static Pattern field_111053_a;
    @Shadow
    @Final
    private static Splitter field_135065_b;
    @Shadow
    private Map languageList;

    @Unique
    @Override
    public void localInject(InputStream inputstream) {
        try {
            if (inputstream != null) {
                for (String var3 : IOUtils.readLines(inputstream, Charsets.UTF_8)) {
                    if (!var3.isEmpty() && var3.charAt(0) != '#') {
                        String[] var4 = Iterables.toArray(field_135065_b.split(var3), String.class);
                        if (var4 != null && var4.length == 2) {
                            String var5 = var4[0];
                            String var6 = field_111053_a.matcher(var4[1]).replaceAll("%$1s");
                            this.languageList.put(var5, var6);
                        }
                    }
                }
            }
        } catch (IOException var8) {
        }

    }
}
