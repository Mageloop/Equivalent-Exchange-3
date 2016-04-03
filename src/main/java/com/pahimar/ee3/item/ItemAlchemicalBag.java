package com.pahimar.ee3.item;

import com.pahimar.ee3.item.base.ItemEE;
import com.pahimar.ee3.reference.Messages;
import com.pahimar.ee3.reference.Names;
import com.pahimar.ee3.util.IOwnable;
import com.pahimar.ee3.util.helper.ItemStackHelper;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAlchemicalBag extends ItemEE implements IOwnable, IItemColor {

    private static final String NAME = "alchemical_bag";
    private static final String[] VARIANTS = {"alchemical_bag_closed", "alchemical_bag_open"};

    public ItemAlchemicalBag() {
        super(NAME, VARIANTS);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemstack(ItemStack itemStack, int renderPass) {

        return ItemStackHelper.getColor(itemStack);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer, EnumHand hand) {

        if (!world.isRemote) {
            if (!ItemStackHelper.hasOwnerUUID(itemStack)) {
                ItemStackHelper.setOwner(itemStack, entityPlayer);
                entityPlayer.addChatComponentMessage(new TextComponentTranslation(Messages.OWNER_SET_TO_SELF, itemStack.getChatComponent()));
            }

            // Set a UUID on the Alchemical Bag, if one doesn't exist already
            if (!ItemStackHelper.hasUUID(itemStack)) {
                ItemStackHelper.setUUID(itemStack);
            }
            else {
                // TODO Scan player inventory and if we find a bag with the same UUID, change it's UUID
            }

            if (isBagOpen(itemStack)) {
                closeBag(itemStack);
            }
            else {
                openBag(itemStack);
            }

            // TODO Get the Gui working again
//            entityPlayer.openGui(EquivalentExchange3.instance, GUIs.ALCHEMICAL_BAG.ordinal(), entityPlayer.worldObj, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
        }

        return new ActionResult<>(EnumActionResult.PASS, itemStack);
    }

    @Override
    public ItemMeshDefinition getCustomMeshDefinition() {
        return itemStack -> isBagOpen(itemStack) ? getCustomModelResourceLocation(VARIANTS[1]) : getCustomModelResourceLocation(VARIANTS[0]);
    }

    private static boolean isBagOpen(ItemStack itemStack) {

        return ItemStackHelper.hasTag(itemStack, Names.NBT.IS_ALCHEMICAL_BAG_GUI_OPEN) && ItemStackHelper.getBoolean(itemStack, Names.NBT.IS_ALCHEMICAL_BAG_GUI_OPEN);
    }

    private static void openBag(ItemStack itemStack) {

        ItemStackHelper.setBoolean(itemStack, Names.NBT.IS_ALCHEMICAL_BAG_GUI_OPEN, true);
    }

    private static void closeBag(ItemStack itemStack) {

        ItemStackHelper.setBoolean(itemStack, Names.NBT.IS_ALCHEMICAL_BAG_GUI_OPEN, false);
    }
}
