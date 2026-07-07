package vswe.stevescarts.Slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import ic2.core.Ic2Items;

public class SlotCropStickIC2 extends SlotBase {

    public SlotCropStickIC2(IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
    }

    public boolean isItemValid(ItemStack itemstack) {
        return itemstack.getItem() == Ic2Items.crop.getItem();
    }
}
