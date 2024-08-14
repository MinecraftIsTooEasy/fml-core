package cpw.mods.fml.client;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.toposort.ModSortingException;
import cpw.mods.fml.common.toposort.ModSortingException.SortingExceptionData;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import net.minecraft.client.gui.GuiScreen;

public class GuiSortingProblem extends GuiScreen {
    private ModSortingException modSorting;
    private SortingExceptionData<ModContainer> failedList;

    public GuiSortingProblem(ModSortingException modSorting)
    {
        this.modSorting = modSorting;
        this.failedList = modSorting.getExceptionData();
    }


    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.drawDefaultBackground();
        int offset = Math.max(85 - (failedList.getVisitedNodes().size() + 3) * 10, 10);
        this.drawCenteredString(this.fontRenderer, "Forge Mod Loader has found a problem with your minecraft installation", this.width / 2, offset, 0xFFFFFF);
        offset+=10;
        this.drawCenteredString(this.fontRenderer, "A mod sorting cycle was detected and loading cannot continue", this.width / 2, offset, 0xFFFFFF);
        offset+=10;
        this.drawCenteredString(this.fontRenderer, String.format("The first mod in the cycle is %s", failedList.getFirstBadNode()), this.width / 2, offset, 0xFFFFFF);
        offset+=10;
        this.drawCenteredString(this.fontRenderer, "The remainder of the cycle involves these mods", this.width / 2, offset, 0xFFFFFF);
        offset+=5;
        for (ModContainer mc : failedList.getVisitedNodes())
        {
            offset+=10;
            this.drawCenteredString(this.fontRenderer, String.format("%s : before: %s, after: %s", mc.toString(), mc.getDependants(), mc.getDependencies()), this.width / 2, offset, 0xEEEEEE);
        }
        offset+=20;
        this.drawCenteredString(this.fontRenderer, "The file 'ForgeModLoader-client-0.log' contains more information", this.width / 2, offset, 0xFFFFFF);
    }

}
