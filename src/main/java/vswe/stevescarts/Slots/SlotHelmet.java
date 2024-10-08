package vswe.stevescarts.Slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class SlotHelmet extends SlotBase {

    public SlotHelmet(IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
    }

    public boolean isItemValid(ItemStack itemstack) {
        return itemstack.getItem() instanceof ItemArmor && ((ItemArmor) itemstack.getItem()).armorType == 0;
    }
}
