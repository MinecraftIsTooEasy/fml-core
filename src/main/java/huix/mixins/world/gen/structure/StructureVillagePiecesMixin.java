package huix.mixins.world.gen.structure;

import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.util.MathHelper;
import net.minecraft.world.gen.structure.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Mixin(StructureVillagePieces.class)
public class StructureVillagePiecesMixin {


    @Overwrite
    public static List getStructureVillageWeightedPieceList(Random random, int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new StructureVillagePieceWeight(ComponentVillageHouse4_Garden.class, 4, MathHelper.getRandomIntegerInRange(random, 2 + i, 4 + i * 2)));
        arrayList.add(new StructureVillagePieceWeight(ComponentVillageChurch.class, 20, MathHelper.getRandomIntegerInRange(random, 0 + i, 1 + i)));
        arrayList.add(new StructureVillagePieceWeight(ComponentVillageHouse1.class, 20, MathHelper.getRandomIntegerInRange(random, 0 + i, 2 + i)));
        arrayList.add(new StructureVillagePieceWeight(ComponentVillageWoodHut.class, 3, MathHelper.getRandomIntegerInRange(random, 2 + i, 5 + i * 3)));
        arrayList.add(new StructureVillagePieceWeight(ComponentVillageHall.class, 15, MathHelper.getRandomIntegerInRange(random, 0 + i, 2 + i)));
        arrayList.add(new StructureVillagePieceWeight(ComponentVillageField.class, 3, MathHelper.getRandomIntegerInRange(random, 1 + i, 4 + i)));
        arrayList.add(new StructureVillagePieceWeight(ComponentVillageField2.class, 3, MathHelper.getRandomIntegerInRange(random, 2 + i, 4 + i * 2)));
        arrayList.add(new StructureVillagePieceWeight(ComponentVillageHouse2.class, 15, MathHelper.getRandomIntegerInRange(random, 0, 1 + i)));
        arrayList.add(new StructureVillagePieceWeight(ComponentVillageHouse3.class, 8, MathHelper.getRandomIntegerInRange(random, 0 + i, 3 + i * 2)));
        VillagerRegistry.addExtraVillageComponents(arrayList, random, i);

        arrayList.removeIf(o -> ((StructureVillagePieceWeight) o).villagePiecesLimit == 0);

        return arrayList;
    }

    @Overwrite
    private static ComponentVillage func_75083_a(ComponentVillageStartPiece componentVillageStartPiece, StructureVillagePieceWeight structureVillagePieceWeight, List list, Random random, int i, int j, int k, int l, int m) {
        Class var9 = structureVillagePieceWeight.villagePieceClass;
        Object var10;
        if (var9 == ComponentVillageHouse4_Garden.class) {
            var10 = ComponentVillageHouse4_Garden.func_74912_a(componentVillageStartPiece, list, random, i, j, k, l, m);
        } else if (var9 == ComponentVillageChurch.class) {
            var10 = ComponentVillageChurch.func_74919_a(componentVillageStartPiece, list, random, i, j, k, l, m);
        } else if (var9 == ComponentVillageHouse1.class) {
            var10 = ComponentVillageHouse1.func_74898_a(componentVillageStartPiece, list, random, i, j, k, l, m);
        } else if (var9 == ComponentVillageWoodHut.class) {
            var10 = ComponentVillageWoodHut.func_74908_a(componentVillageStartPiece, list, random, i, j, k, l, m);
        } else if (var9 == ComponentVillageHall.class) {
            var10 = ComponentVillageHall.func_74906_a(componentVillageStartPiece, list, random, i, j, k, l, m);
        } else if (var9 == ComponentVillageField.class) {
            var10 = ComponentVillageField.func_74900_a(componentVillageStartPiece, list, random, i, j, k, l, m);
        } else if (var9 == ComponentVillageField2.class) {
            var10 = ComponentVillageField2.func_74902_a(componentVillageStartPiece, list, random, i, j, k, l, m);
        } else if (var9 == ComponentVillageHouse2.class) {
            var10 = ComponentVillageHouse2.func_74915_a(componentVillageStartPiece, list, random, i, j, k, l, m);
        } else if (var9 == ComponentVillageHouse3.class) {
            var10 = ComponentVillageHouse3.func_74921_a(componentVillageStartPiece, list, random, i, j, k, l, m);
        } else {
            var10 = VillagerRegistry.getVillageComponent(structureVillagePieceWeight, componentVillageStartPiece , list,
                    random, i, j, k, l, m);
        }

        return (ComponentVillage)var10;
    }



}
