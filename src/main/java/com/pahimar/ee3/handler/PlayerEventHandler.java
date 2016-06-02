package com.pahimar.ee3.handler;

import com.pahimar.ee3.network.PacketHandler;
import com.pahimar.ee3.network.message.MessageChalkSettings;
import com.pahimar.ee3.network.message.MessageSyncBlacklist;
import com.pahimar.ee3.network.message.MessageSyncEnergyValues;
import com.pahimar.ee3.settings.ChalkSettings;
import com.pahimar.ee3.util.EntityUtils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import static com.pahimar.ee3.api.blacklist.BlacklistRegistryProxy.Blacklist;

public class PlayerEventHandler {

    @SubscribeEvent
    public void onPlayerLoggedIn(cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {

        if (event.player != null) {
            NBTTagCompound playerCustomData = EntityUtils.getCustomEntityData(event.player);

            // Chalk Settings
            ChalkSettings chalkSettings = new ChalkSettings();
            chalkSettings.readFromNBT(playerCustomData);
            chalkSettings.writeToNBT(playerCustomData);
            EntityUtils.saveCustomEntityData(event.player, playerCustomData);
            PacketHandler.INSTANCE.sendTo(new MessageChalkSettings(chalkSettings), (EntityPlayerMP) event.player);

            PacketHandler.INSTANCE.sendTo(new MessageSyncEnergyValues(), (EntityPlayerMP) event.player);
            PacketHandler.INSTANCE.sendTo(new MessageSyncBlacklist(Blacklist.KNOWLEDGE), (EntityPlayerMP) event.player);
            PacketHandler.INSTANCE.sendTo(new MessageSyncBlacklist(Blacklist.EXCHANGE), (EntityPlayerMP) event.player);
        }
    }
}
