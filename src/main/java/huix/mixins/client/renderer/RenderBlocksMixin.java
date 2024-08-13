package huix.mixins.client.renderer;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import net.minecraft.block.*;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.src.FMLRenderAccessLibrary;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( RenderBlocks.class )
public class RenderBlocksMixin {

    @Shadow
    public IBlockAccess blockAccess;
    @Shadow
    private Icon overrideBlockTexture;

    @Inject(method = "renderBlockByRenderType", at = @At(value = "RETURN"), cancellable = true)
    private void injectRenderBlockByRenderType(Block par1Block, int par2, int par3, int par4, CallbackInfoReturnable<Boolean> cir) {
        int[] ints = new int[]{0, 4, 31, 1, 2, 20 ,11, 39, 5, 13, 9, 19, 23, 6, 3, 8, 7, 10, 27, 12, 30, 15, 37, 17, 21, 44, 25, 28, 38,
                32, 29, 14, 36, 16, 18, 24, 35, 26, 34};

        for (int anInt : ints) {
            int renderType = par1Block.getRenderType();
            if (renderType != anInt) {
                boolean renderWorldBlock = FMLRenderAccessLibrary.renderWorldBlock(ReflectHelper.dyCast(this), blockAccess, par2, par3, par4, par1Block, renderType);
                cir.setReturnValue(renderWorldBlock);
            }
        }
    }

    @Inject(method = "renderBlockAsItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getRenderType()I"))
    private void injectRenderBlockByRenderType(Block par1Block, int par2, float par3, CallbackInfo ci) {
        int[] ints = new int[]{0, 31, 39, 16, 26, 1, 19, 23, 13, 22, 6, 2, 10, 27, 11, 21, 32, 35, 34, 38};

        for (int anInt : ints) {
            int renderType = par1Block.getRenderType();
            if (renderType != anInt) {
                FMLRenderAccessLibrary.renderInventoryBlock(ReflectHelper.dyCast(this), par1Block, par2, renderType);
            }
        }
    }

    @Overwrite
    public static boolean renderItemIn3d(int par0) {
        return switch (par0) {
            case 0, 27, 31, 39, 13, 10, 11, 22, 21, 16, 26, 32, 34, 35 -> true;
            default -> FMLRenderAccessLibrary.renderItemAsFull3DBlock(par0);
        };
    }


//    @Overwrite
//    public boolean renderBlockByRenderType(Block par1Block, int par2, int par3, int par4) {
//        int var5 = par1Block.getRenderType();
//        if (var5 == -1) {
//            return false;
//        } else {
//            if (this.overrideBlockTexture != null && var5 == 22) {
//                var5 = 0;
//            }
//
//            if (par1Block.isAlwaysStandardFormCube()) {
//                this.setRenderBoundsForStandardFormBlock();
//            } else {
//                par1Block.setBlockBoundsBasedOnStateAndNeighbors(this.blockAccess, par2, par3, par4);
//                this.setRenderBoundsForNonStandardFormBlock(par1Block);
//            }
//
//
//
//            return var5 == 0 ? this.renderStandardBlock(par1Block, par2, par3, par4) : (var5 == 4 ? this.renderBlockFluids(par1Block, par2, par3, par4) :
//                    (var5 == 31 ? this.renderBlockLog(par1Block, par2, par3, par4) : (var5 == 1 ? this.renderCrossedSquares(par1Block, par2, par3, par4) :
//                    (var5 == 2 ? this.renderBlockTorch(par1Block, par2, par3, par4) : (var5 == 20 ? this.renderBlockVine(par1Block, par2, par3, par4) :
//                    (var5 == 11 ? this.renderBlockFence((BlockFence)par1Block, par2, par3, par4) : (var5 == 39 ? this.renderBlockQuartz(par1Block, par2, par3, par4) :
//                    (var5 == 5 ? this.renderBlockRedstoneWire(par1Block, par2, par3, par4) : (var5 == 13 ? this.renderBlockCactus(par1Block, par2, par3, par4) :
//                    (var5 == 9 ? this.renderBlockMinecartTrack((BlockRailBase)par1Block, par2, par3, par4) : (var5 == 19 ? this.renderBlockStem(par1Block, par2, par3, par4) :
//                    (var5 == 23 ? this.renderBlockLilyPad(par1Block, par2, par3, par4) : (var5 == 6 ? this.renderBlockCrops(par1Block, par2, par3, par4) :
//                    (var5 == 3 ? this.renderBlockFire((BlockFire)par1Block, par2, par3, par4) : (var5 == 8 ? this.renderBlockLadder(par1Block, par2, par3, par4) :
//                    (var5 == 7 ? this.renderBlockDoor(par1Block, par2, par3, par4) : (var5 == 10 ? this.renderBlockStairs((BlockStairs)par1Block, par2, par3, par4) :
//
//                    (var5 == 27 ? this.renderBlockDragonEgg((BlockDragonEgg)par1Block, par2, par3, par4) : (var5 == 32 ? this.renderBlockWall((BlockWall)par1Block, par2, par3, par4) :
//                    (var5 == 12 ? this.renderBlockLever(par1Block, par2, par3, par4) : (var5 == 29 ? this.renderBlockTripWireSource(par1Block, par2, par3, par4) :
//                    (var5 == 30 ? this.renderBlockTripWire(par1Block, par2, par3, par4) : (var5 == 14 ? this.renderBlockBed(par1Block, par2, par3, par4) :
//                    (var5 == 15 ? this.renderBlockRepeater((BlockRedstoneRepeater)par1Block, par2, par3, par4) : (var5 == 36 ? this.renderBlockRedstoneLogic((BlockRedstoneLogic)par1Block, par2, par3, par4) :
//                    (var5 == 37 ? this.renderBlockComparator((BlockComparator)par1Block, par2, par3, par4) : (var5 == 16 ? this.renderPistonBase(par1Block, par2, par3, par4, false) :
//                    (var5 == 17 ? this.renderPistonExtension(par1Block, par2, par3, par4, true) : (var5 == 18 ? this.renderBlockPane((BlockPane)par1Block, par2, par3, par4) :
//                    (var5 == 21 ? this.renderBlockFenceGate((BlockFenceGate)par1Block, par2, par3, par4) : (var5 == 24 ? this.renderBlockCauldron((BlockCauldron)par1Block, par2, par3, par4) :
//                    (var5 == 33 ? this.renderBlockFlowerpot((BlockFlowerPot)par1Block, par2, par3, par4) : (var5 == 35 ? this.renderBlockAnvil((BlockAnvil)par1Block, par2, par3, par4) :
//                    (var5 == 25 ? this.renderBlockBrewingStand((BlockBrewingStand)par1Block, par2, par3, par4) : (var5 == 26 ? this.renderBlockEndPortalFrame((BlockEndPortalFrame)par1Block, par2, par3, par4) :
//                    (var5 == 28 ? this.renderBlockCocoa((BlockCocoa)par1Block, par2, par3, par4) : (var5 == 34 ? this.renderBlockBeacon((BlockBeacon)par1Block, par2, par3, par4) :
//                    (var5 == 38 ? this.renderBlockHopper((BlockHopper)par1Block, par2, par3, par4) :
//                            FMLRenderAccessLibrary.renderWorldBlock(ReflectHelper.dyCast(this), blockAccess, par2, par3, par4, par1Block, var5)))))))))))))))))))))))))))))))))))))));
//        }
//    }
//
//
//    @Shadow
//    public boolean renderBlockLilyPad(Block par1Block, int par2, int par3, int par4) {
//        return false;
//    }
//    @Shadow
//    public boolean renderBlockCrops(Block par1Block, int par2, int par3, int par4) {
//        return false;
//    }
//    @Shadow
//    public boolean renderBlockLadder(Block par1Block, int par2, int par3, int par4) {
//        return false;
//    }
//
//
//    @Shadow
//    public boolean renderBlockLilyPad(Block par1Block, int par2, int par3, int par4) {
//        return false;
//    }
//    @Shadow
//    public boolean renderBlockCrops(Block par1Block, int par2, int par3, int par4) {
//        return false;
//    }
//    @Shadow
//    public boolean renderBlockLadder(Block par1Block, int par2, int par3, int par4) {
//        return false;
//    }
//    @Shadow
//    public boolean renderBlockCactus(Block par1Block, int par2, int par3, int par4) {
//        return false;
//    }
//    @Shadow
//    private boolean renderPistonBase(Block par1Block, int par2, int par3, int par4, boolean par5) {
//        return false;
//    }
//    @Shadow
//    public boolean renderBlockRedstoneWire(Block par1Block, int par2, int par3, int par4) {
//        return false;
//    }
//    @Shadow
//    public boolean renderBlockQuartz(Block par1Block, int par2, int par3, int par4) {
//        return false;
//    }
//    @Shadow
//    public boolean renderBlockVine(Block par1Block, int par2, int par3, int par4) {
//        return false;
//    }
//    @Shadow
//    public boolean renderBlockTorch(Block par1Block, int par2, int par3, int par4) {
//        return false;
//    }
//    @Shadow
//    public boolean renderCrossedSquares(Block par1Block, int par2, int par3, int par4) {
//        return false;
//    }
//    @Shadow
//    public boolean renderBlockLog(Block par1Block, int par2, int par3, int par4) {
//        return false;
//    }
//    @Shadow
//    public boolean renderBlockFluids(Block par1Block, int par2, int par3, int par4) {
//        return false;
//    }
//    @Shadow
//    public boolean renderStandardBlock(Block par1Block, int par2, int par3, int par4) {
//        return false;
//    }
//    @Shadow
//    public void setRenderBoundsForStandardFormBlock() {
//    }
//    @Shadow
//    public void setRenderBoundsForNonStandardFormBlock(Block block) {
//    }

}
