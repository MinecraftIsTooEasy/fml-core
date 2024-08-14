package huix.mixins.network.packet;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet131MapData;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatList;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.WorldAchievement;
import net.minecraft.world.WorldType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Mixin(Packet1Login.class)
public class Packet1LoginMixin extends Packet {

    @Unique
    private boolean vanillaCompatible;

    @Inject(method = "<init>()V", at = @At("RETURN"))
    private void injectInit_0(CallbackInfo ci) {
        this.vanillaCompatible = FMLNetworkHandler.vanillaLoginPacketCompatibility();
    }

    @Inject(method = "<init>(ILnet/minecraft/world/WorldType;Lnet/minecraft/world/EnumGameType;ZIIIIBLjava/util/HashMap;IIZJJ)V", at = @At("RETURN"))
    private void injectInit_1(CallbackInfo ci) {
        this.vanillaCompatible = FMLNetworkHandler.vanillaLoginPacketCompatibility();
    }

    @Shadow
    public WorldType terrainType;
    @Shadow
    public HashMap achievements;


    @Shadow
    public void readPacketData(DataInput par1DataInput) throws IOException {
        this.clientEntityId = par1DataInput.readInt();
        String var2 = readString(par1DataInput, 16);
        this.terrainType = WorldType.parseWorldType(var2);
        if (this.terrainType == null) {
            this.terrainType = WorldType.DEFAULT;
        }

        byte var3 = par1DataInput.readByte();
        this.hardcoreMode = (var3 & 8) == 8;
        int var4 = var3 & -9;
        this.gameType = EnumGameType.getByID(var4);

        if (vanillaCompatible) {
            this.dimension = par1DataInput.readByte();
        } else {
            this.dimension = par1DataInput.readInt();
        }

        this.difficultySetting = par1DataInput.readByte();
        this.worldHeight = par1DataInput.readByte();
        this.maxPlayers = par1DataInput.readByte();
        this.village_conditions = par1DataInput.readByte();
        int num_achievements = par1DataInput.readByte();
        this.achievements = new HashMap();

        for(int i = 0; i < num_achievements; ++i) {
            Achievement achievement = (Achievement) StatList.getStat(par1DataInput.readInt());
            String username = readString(par1DataInput, 16);
            int day = par1DataInput.readInt();
            this.achievements.put(achievement, new WorldAchievement(achievement, username, day));
        }

        this.earliest_MITE_release_run_in = par1DataInput.readShort();
        this.latest_MITE_release_run_in = par1DataInput.readShort();
        this.are_skills_enabled = par1DataInput.readBoolean();
        this.world_creation_time = par1DataInput.readLong();
        this.total_world_time = par1DataInput.readLong();
    }

    @Overwrite
    public void writePacketData(DataOutput par1DataOutput) throws IOException {
        par1DataOutput.writeInt(this.clientEntityId);
        writeString(this.terrainType == null ? "" : this.terrainType.getWorldTypeName(), par1DataOutput);
        int var2 = this.gameType.getID();
        if (this.hardcoreMode) {
            var2 |= 8;
        }

        par1DataOutput.writeByte(var2);

        if (vanillaCompatible)
        {
            par1DataOutput.writeByte(this.dimension);
        }
        else
        {
            par1DataOutput.writeInt(this.dimension);
        }

        par1DataOutput.writeByte(this.difficultySetting);
        par1DataOutput.writeByte(this.worldHeight);
        par1DataOutput.writeByte(this.maxPlayers);
        par1DataOutput.writeByte(this.village_conditions);
        par1DataOutput.writeByte(this.achievements.size());
        Iterator i = this.achievements.entrySet().iterator();

        while(i.hasNext()) {
            Map.Entry entry = (Map.Entry)i.next();
            WorldAchievement wa = (WorldAchievement)entry.getValue();
            par1DataOutput.writeInt(wa.achievement.statId);
            writeString(wa.username, par1DataOutput);
            par1DataOutput.writeInt(wa.day);
        }

        par1DataOutput.writeShort(this.earliest_MITE_release_run_in);
        par1DataOutput.writeShort(this.latest_MITE_release_run_in);
        par1DataOutput.writeBoolean(this.are_skills_enabled);
        par1DataOutput.writeLong(this.world_creation_time);
        par1DataOutput.writeLong(this.total_world_time);
    }

    @Shadow
    public void processPacket(NetHandler netHandler) {

    }

    @Shadow
    public int clientEntityId;
    @Shadow
    public boolean hardcoreMode;
    @Shadow
    public EnumGameType gameType;
    @Shadow
    public int dimension;
    @Shadow
    public byte difficultySetting;
    @Shadow
    public byte worldHeight;
    @Shadow
    public byte maxPlayers;
    @Shadow
    public byte village_conditions;
    @Shadow
    public short earliest_MITE_release_run_in;
    @Shadow
    public short latest_MITE_release_run_in;
    @Shadow
    public boolean are_skills_enabled;
    @Shadow
    public long world_creation_time;
    @Shadow
    public long total_world_time;

    @Overwrite
    public int getPacketSize() {
        int var1 = 0;
        if (this.terrainType != null) {
            var1 = this.terrainType.getWorldTypeName().length();
        }

        int num_achievement_bytes = 1;

        WorldAchievement wa;
        for(Iterator i = this.achievements.entrySet().iterator(); i.hasNext(); num_achievement_bytes += 4 + wa.username.length() * 2 + 4) {
            Map.Entry entry = (Map.Entry)i.next();
            wa = (WorldAchievement)entry.getValue();
        }

        return 6 + 2 * var1 + 4 + 4 + 1 + 1 + 1 + 1 + 4 + 1 + num_achievement_bytes + 16 + (vanillaCompatible ? 0 : 3);
    }


}
