package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
// CraftBukkit end

public abstract class DispenseBehaviorProjectile extends DispenseBehaviorItem {

    public DispenseBehaviorProjectile() {}

    public ItemStack b(ISourceBlock isourceblock, ItemStack itemstack) {
        World world = isourceblock.k();
        IPosition iposition = BlockDispenser.a(isourceblock);
        EnumFacing enumfacing = EnumFacing.a(isourceblock.h());
        IProjectile iprojectile = this.a(world, iposition);

        // CraftBukkit start
        ItemStack itemstack1 = itemstack.a(1);
        org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.getBlockX(), isourceblock.getBlockY(), isourceblock.getBlockZ());
        org.bukkit.inventory.ItemStack bukkitItem = new CraftItemStack(itemstack1).clone();

        BlockDispenseEvent event = new BlockDispenseEvent(block, bukkitItem, new org.bukkit.util.Vector((double) enumfacing.c(), 0.10000000149011612D, (double) enumfacing.e()));
        if (!BlockDispenser.eventFired) {
            world.getServer().getPluginManager().callEvent(event);
        }

        if (event.isCancelled()) {
            itemstack.count++;
            return itemstack;
        }

        if (!event.getItem().equals(bukkitItem)) {
            itemstack.count++;
            // Chain to handler for new item
            ItemStack eventStack = CraftItemStack.createNMSItemStack(event.getItem());
            IDispenseBehavior idispensebehavior = (IDispenseBehavior) BlockDispenser.a.a(eventStack.getItem());
            if (idispensebehavior != IDispenseBehavior.a && idispensebehavior != this) {
                idispensebehavior.a(isourceblock, CraftItemStack.createNMSItemStack(event.getItem()));
                return itemstack;
            }
        }

        iprojectile.shoot(event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ(), this.b(), this.a());
        // CraftBukkit end

        world.addEntity((Entity) iprojectile);
        // itemstack.a(1); // CraftBukkit - Handled during event processing
        return itemstack;
    }

    protected void a(ISourceBlock isourceblock) {
        isourceblock.k().triggerEffect(1002, isourceblock.getBlockX(), isourceblock.getBlockY(), isourceblock.getBlockZ(), 0);
    }

    protected abstract IProjectile a(World world, IPosition iposition);

    protected float a() {
        return 6.0F;
    }

    protected float b() {
        return 1.1F;
    }
}
