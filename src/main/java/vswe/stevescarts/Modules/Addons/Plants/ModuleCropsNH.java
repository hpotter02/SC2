package vswe.stevescarts.Modules.Addons.Plants;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.gtnewhorizon.cropsnh.api.ICropCard;
import com.gtnewhorizon.cropsnh.api.ICropStickTile;
import com.gtnewhorizon.cropsnh.farming.registries.CropRegistry;
import com.gtnewhorizon.cropsnh.utility.CropsNHUtils;

import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.Addons.ModuleAddon;
import vswe.stevescarts.Modules.ICropModule;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotCropStick;

public class ModuleCropsNH extends ModuleAddon implements ICropModule {

    public ModuleCropsNH(MinecartModular cart) {
        super(cart);
    }

    @Override
    public boolean isSeedValid(ItemStack seed) {
        return CropRegistry.instance.get(seed) != null;
    }

    @Override
    public Block getCropFromSeed(ItemStack seed) {
        // This won't be called with this module
        return null;
    }

    @Override
    public boolean isReadyToHarvest(int x, int y, int z) {
        TileEntity te = getCart().worldObj.getTileEntity(x, y, z);
        if (te instanceof ICropStickTile ct) {
            if (!ct.hasCrop()) {
                return false;
            }
            if (ct.hasWeed()) {
                return true;
            }
            return ct.isMature();
        }
        return false;
    }

    @Override
    public List<ItemStack> harvestCrop(int x, int y, int z, int fortune) {
        TileEntity te = getCart().worldObj.getTileEntity(x, y, z);
        if (te instanceof ICropStickTile ct) {
            // handle Weeds borrowed from ItemSpadeNH
            if (ct.hasWeed()) {
                // drop tall grass if it's mature
                ItemStack returnStack = null;
                if (ct.isMature()) {
                    returnStack = CropsNHUtils.getWeedDrop(1);
                }
                ct.clear();
                return returnStack != null ? Arrays.asList(new ItemStack[] { returnStack }) : null;
            }
            return ct.harvest(1.0d);
        }
        return null;
    }

    @Override
    public int guiWidth() {
        return 15 + getInventoryWidth() * 18;
    }

    @Override
    public int guiHeight() {
        return 20 + getInventoryHeight() * 18;
    }

    @Override
    public boolean hasGui() {
        return true;
    }

    @Override
    public int getInventorySize() {
        return 3;
    }

    @Override
    public void drawForeground(GuiMinecart gui) {
        drawString(gui, Localization.GUI.CARGO.AREA_CROPSTICKS.translate(), 8, 6, 0x404040);
    }

    @Override
    protected SlotBase getSlot(int slotId, int x, int y) {
        return new SlotCropStick(getCart(), slotId, 8 + x * 18, 16 + y * 18);
    }

    @Override
    public void placeCrop(int x, int y, int z, ItemStack seed) {
        ICropCard cc = CropRegistry.instance.get(seed);
        if (cc == null) {
            return;
        }
        TileEntity te = getCart().worldObj.getTileEntity(x, y, z);
        if (te instanceof ICropStickTile cst) {
            cst.tryPlantSeed(seed);
        }
    }

    @Override
    public boolean isSeedPlaceable(int x, int y, int z, ItemStack seed) {
        int hasCropSticks = -1;
        for (int i = 0; i < getInventorySize(); i++) {
            if (getStack(i) != null) {
                hasCropSticks = i;
            }
        }
        ICropCard cc = CropRegistry.instance.get(seed);
        if (cc == null) {
            return false;
        }

        if (hasCropSticks != -1) {
            if (getCart().worldObj.isAirBlock(x, y, z)) {
                Block b = Block.getBlockFromItem(getStack(hasCropSticks).getItem());
                if (b.canPlaceBlockAt(getWorld(), x, y, z)) {
                    getCart().worldObj.setBlock(x, y, z, b);
                    getStack(hasCropSticks).stackSize--;
                    if (getStack(hasCropSticks).stackSize <= 0) {
                        setStack(hasCropSticks, null);
                    }
                }
            }
        }

        TileEntity te = getCart().worldObj.getTileEntity(x, y, z);
        if (te instanceof ICropStickTile cst) {
            return cst.isValidSoilForCrop(cc) && cst.canPlantSeed();
        }
        return false;
    }

    @Override
    public World getWorld() {
        return getCart().worldObj;
    }

}
