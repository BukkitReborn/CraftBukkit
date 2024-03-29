package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

// CraftBukkit start
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.util.BlockStateListPopulator;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.Bukkit;
// CraftBukkit end

public class EntityEnderDragon extends EntityLiving implements IComplex {

    public double a;
    public double b;
    public double c;
    public double[][] d = new double[64][3];
    public int e = -1;
    public EntityComplexPart[] children;
    public EntityComplexPart g;
    public EntityComplexPart h;
    public EntityComplexPart i;
    public EntityComplexPart j;
    public EntityComplexPart bI;
    public EntityComplexPart bJ;
    public EntityComplexPart bK;
    public float bL = 0.0F;
    public float bM = 0.0F;
    public boolean bN = false;
    public boolean bO = false;
    private Entity bR;
    public int bP = 0;
    public EntityEnderCrystal bQ = null;

    public EntityEnderDragon(World world) {
        super(world);
        this.children = new EntityComplexPart[] { this.g = new EntityComplexPart(this, "head", 6.0F, 6.0F), this.h = new EntityComplexPart(this, "body", 8.0F, 8.0F), this.i = new EntityComplexPart(this, "tail", 4.0F, 4.0F), this.j = new EntityComplexPart(this, "tail", 4.0F, 4.0F), this.bI = new EntityComplexPart(this, "tail", 4.0F, 4.0F), this.bJ = new EntityComplexPart(this, "wing", 4.0F, 4.0F), this.bK = new EntityComplexPart(this, "wing", 4.0F, 4.0F)};
        this.setHealth(this.getMaxHealth());
        this.texture = "/mob/enderdragon/ender.png";
        this.a(16.0F, 8.0F);
        this.Y = true;
        this.fireProof = true;
        this.b = 100.0D;
        this.al = true;
    }

    public int getMaxHealth() {
        return 200;
    }

    protected void a() {
        super.a();
        this.datawatcher.a(16, new Integer(this.getMaxHealth()));
    }

    public double[] a(int i, float f) {
        if (this.health <= 0) {
            f = 0.0F;
        }

        f = 1.0F - f;
        int j = this.e - i * 1 & 63;
        int k = this.e - i * 1 - 1 & 63;
        double[] adouble = new double[3];
        double d0 = this.d[j][0];
        double d1 = MathHelper.g(this.d[k][0] - d0);

        adouble[0] = d0 + d1 * (double) f;
        d0 = this.d[j][1];
        d1 = this.d[k][1] - d0;
        adouble[1] = d0 + d1 * (double) f;
        adouble[2] = this.d[j][2] + (this.d[k][2] - this.d[j][2]) * (double) f;
        return adouble;
    }

    public void c() {
        float f;
        float f1;

        if (!this.world.isStatic) {
            this.datawatcher.watch(16, Integer.valueOf(this.health));
        } else {
            f = MathHelper.cos(this.bM * 3.1415927F * 2.0F);
            f1 = MathHelper.cos(this.bL * 3.1415927F * 2.0F);
            if (f1 <= -0.3F && f >= -0.3F) {
                this.world.b(this.locX, this.locY, this.locZ, "mob.enderdragon.wings", 5.0F, 0.8F + this.random.nextFloat() * 0.3F);
            }
        }

        this.bL = this.bM;
        float f2;

        if (this.health <= 0) {
            f = (this.random.nextFloat() - 0.5F) * 8.0F;
            f1 = (this.random.nextFloat() - 0.5F) * 4.0F;
            f2 = (this.random.nextFloat() - 0.5F) * 8.0F;
            this.world.addParticle("largeexplode", this.locX + (double) f, this.locY + 2.0D + (double) f1, this.locZ + (double) f2, 0.0D, 0.0D, 0.0D);
        } else {
            this.h();
            f = 0.2F / (MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 10.0F + 1.0F);
            f *= (float) Math.pow(2.0D, this.motY);
            if (this.bO) {
                this.bM += f * 0.5F;
            } else {
                this.bM += f;
            }

            this.yaw = MathHelper.g(this.yaw);
            if (this.e < 0) {
                for (int d05 = 0; d05 < this.d.length; ++d05) {
                    this.d[d05][0] = (double) this.yaw;
                    this.d[d05][1] = this.locY;
                }
            }

            if (++this.e == this.d.length) {
                this.e = 0;
            }

            this.d[this.e][0] = (double) this.yaw;
            this.d[this.e][1] = this.locY;
            double d0;
            double d1;
            double d2;
            double d3;
            float f3;

            if (this.world.isStatic) {
                if (this.bs > 0) {
                    d0 = this.locX + (this.bt - this.locX) / (double) this.bs;
                    d1 = this.locY + (this.bu - this.locY) / (double) this.bs;
                    d2 = this.locZ + (this.bv - this.locZ) / (double) this.bs;
                    d3 = MathHelper.g(this.bw - (double) this.yaw);
                    this.yaw = (float) ((double) this.yaw + d3 / (double) this.bs);
                    this.pitch = (float) ((double) this.pitch + (this.bx - (double) this.pitch) / (double) this.bs);
                    --this.bs;
                    this.setPosition(d0, d1, d2);
                    this.b(this.yaw, this.pitch);
                }
            } else {
                d0 = this.a - this.locX;
                d1 = this.b - this.locY;
                d2 = this.c - this.locZ;
                d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (this.bR != null) {
                    this.a = this.bR.locX;
                    this.c = this.bR.locZ;
                    double d4 = this.a - this.locX;
                    double d5 = this.c - this.locZ;
                    double d6 = Math.sqrt(d4 * d4 + d5 * d5);
                    double d7 = 0.4000000059604645D + d6 / 80.0D - 1.0D;

                    if (d7 > 10.0D) {
                        d7 = 10.0D;
                    }

                    this.b = this.bR.boundingBox.b + d7;
                } else {
                    this.a += this.random.nextGaussian() * 2.0D;
                    this.c += this.random.nextGaussian() * 2.0D;
                }

                if (this.bN || d3 < 100.0D || d3 > 22500.0D || this.positionChanged || this.G) {
                    this.i();
                }

                d1 /= (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
                f3 = 0.6F;
                if (d1 < (double) (-f3)) {
                    d1 = (double) (-f3);
                }

                if (d1 > (double) f3) {
                    d1 = (double) f3;
                }

                this.motY += d1 * 0.10000000149011612D;
                this.yaw = MathHelper.g(this.yaw);
                double d8 = 180.0D - Math.atan2(d0, d2) * 180.0D / 3.1415927410125732D;
                double d9 = MathHelper.g(d8 - (double) this.yaw);

                if (d9 > 50.0D) {
                    d9 = 50.0D;
                }

                if (d9 < -50.0D) {
                    d9 = -50.0D;
                }

                Vec3D vec3d = this.world.getVec3DPool().create(this.a - this.locX, this.b - this.locY, this.c - this.locZ).a();
                Vec3D vec3d1 = this.world.getVec3DPool().create((double) MathHelper.sin(this.yaw * 3.1415927F / 180.0F), this.motY, (double) (-MathHelper.cos(this.yaw * 3.1415927F / 180.0F))).a();
                float f4 = (float) (vec3d1.b(vec3d) + 0.5D) / 1.5F;

                if (f4 < 0.0F) {
                    f4 = 0.0F;
                }

                this.bD *= 0.8F;
                float f5 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 1.0F + 1.0F;
                double d10 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 1.0D + 1.0D;

                if (d10 > 40.0D) {
                    d10 = 40.0D;
                }

                this.bD = (float) ((double) this.bD + d9 * (0.699999988079071D / d10 / (double) f5));
                this.yaw += this.bD * 0.1F;
                float f6 = (float) (2.0D / (d10 + 1.0D));
                float f7 = 0.06F;

                this.a(0.0F, -1.0F, f7 * (f4 * f6 + (1.0F - f6)));
                if (this.bO) {
                    this.move(this.motX * 0.800000011920929D, this.motY * 0.800000011920929D, this.motZ * 0.800000011920929D);
                } else {
                    this.move(this.motX, this.motY, this.motZ);
                }

                Vec3D vec3d2 = this.world.getVec3DPool().create(this.motX, this.motY, this.motZ).a();
                float f8 = (float) (vec3d2.b(vec3d1) + 1.0D) / 2.0F;

                f8 = 0.8F + 0.15F * f8;
                this.motX *= (double) f8;
                this.motZ *= (double) f8;
                this.motY *= 0.9100000262260437D;
            }

            this.aw = this.yaw;
            this.g.width = this.g.length = 3.0F;
            this.i.width = this.i.length = 2.0F;
            this.j.width = this.j.length = 2.0F;
            this.bI.width = this.bI.length = 2.0F;
            this.h.length = 3.0F;
            this.h.width = 5.0F;
            this.bJ.length = 2.0F;
            this.bJ.width = 4.0F;
            this.bK.length = 3.0F;
            this.bK.width = 4.0F;
            f1 = (float) (this.a(5, 1.0F)[1] - this.a(10, 1.0F)[1]) * 10.0F / 180.0F * 3.1415927F;
            f2 = MathHelper.cos(f1);
            float f9 = -MathHelper.sin(f1);
            float f10 = this.yaw * 3.1415927F / 180.0F;
            float f11 = MathHelper.sin(f10);
            float f12 = MathHelper.cos(f10);

            this.h.j_();
            this.h.setPositionRotation(this.locX + (double) (f11 * 0.5F), this.locY, this.locZ - (double) (f12 * 0.5F), 0.0F, 0.0F);
            this.bJ.j_();
            this.bJ.setPositionRotation(this.locX + (double) (f12 * 4.5F), this.locY + 2.0D, this.locZ + (double) (f11 * 4.5F), 0.0F, 0.0F);
            this.bK.j_();
            this.bK.setPositionRotation(this.locX - (double) (f12 * 4.5F), this.locY + 2.0D, this.locZ - (double) (f11 * 4.5F), 0.0F, 0.0F);
            if (!this.world.isStatic && this.hurtTicks == 0) {
                this.a(this.world.getEntities(this, this.bJ.boundingBox.grow(4.0D, 2.0D, 4.0D).d(0.0D, -2.0D, 0.0D)));
                this.a(this.world.getEntities(this, this.bK.boundingBox.grow(4.0D, 2.0D, 4.0D).d(0.0D, -2.0D, 0.0D)));
                this.b(this.world.getEntities(this, this.g.boundingBox.grow(1.0D, 1.0D, 1.0D)));
            }

            double[] adouble = this.a(5, 1.0F);
            double[] adouble1 = this.a(0, 1.0F);

            f3 = MathHelper.sin(this.yaw * 3.1415927F / 180.0F - this.bD * 0.01F);
            float f13 = MathHelper.cos(this.yaw * 3.1415927F / 180.0F - this.bD * 0.01F);

            this.g.j_();
            this.g.setPositionRotation(this.locX + (double) (f3 * 5.5F * f2), this.locY + (adouble1[1] - adouble[1]) * 1.0D + (double) (f9 * 5.5F), this.locZ - (double) (f13 * 5.5F * f2), 0.0F, 0.0F);

            for (int j = 0; j < 3; ++j) {
                EntityComplexPart entitycomplexpart = null;

                if (j == 0) {
                    entitycomplexpart = this.i;
                }

                if (j == 1) {
                    entitycomplexpart = this.j;
                }

                if (j == 2) {
                    entitycomplexpart = this.bI;
                }

                double[] adouble2 = this.a(12 + j * 2, 1.0F);
                float f14 = this.yaw * 3.1415927F / 180.0F + this.b(adouble2[0] - adouble[0]) * 3.1415927F / 180.0F * 1.0F;
                float f15 = MathHelper.sin(f14);
                float f16 = MathHelper.cos(f14);
                float f17 = 1.5F;
                float f18 = (float) (j + 1) * 2.0F;

                entitycomplexpart.j_();
                entitycomplexpart.setPositionRotation(this.locX - (double) ((f11 * f17 + f15 * f18) * f2), this.locY + (adouble2[1] - adouble[1]) * 1.0D - (double) ((f18 + f17) * f9) + 1.5D, this.locZ + (double) ((f12 * f17 + f16 * f18) * f2), 0.0F, 0.0F);
            }

            if (!this.world.isStatic) {
                this.bO = this.a(this.g.boundingBox) | this.a(this.h.boundingBox);
            }
        }
    }

    private void h() {
        if (this.bQ != null) {
            if (this.bQ.dead) {
                if (!this.world.isStatic) {
                    this.a(this.g, DamageSource.EXPLOSION, 10);
                }

                this.bQ = null;
            } else if (this.ticksLived % 10 == 0 && this.health < this.getMaxHealth()) {
                // CraftBukkit start
                EntityRegainHealthEvent event = new EntityRegainHealthEvent(this.getBukkitEntity(), 1, EntityRegainHealthEvent.RegainReason.ENDER_CRYSTAL);
                this.world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.health += event.getAmount();
                }
                // CraftBukkit end
            }
        }

        if (this.random.nextInt(10) == 0) {
            float f = 32.0F;
            List list = this.world.a(EntityEnderCrystal.class, this.boundingBox.grow((double) f, (double) f, (double) f));
            EntityEnderCrystal entityendercrystal = null;
            double d0 = Double.MAX_VALUE;
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityEnderCrystal entityendercrystal1 = (EntityEnderCrystal) iterator.next();
                double d1 = entityendercrystal1.e(this);

                if (d1 < d0) {
                    d0 = d1;
                    entityendercrystal = entityendercrystal1;
                }
            }

            this.bQ = entityendercrystal;
        }
    }

    private void a(List list) {
        double d0 = (this.h.boundingBox.a + this.h.boundingBox.d) / 2.0D;
        double d1 = (this.h.boundingBox.c + this.h.boundingBox.f) / 2.0D;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            if (entity instanceof EntityLiving) {
                double d2 = entity.locX - d0;
                double d3 = entity.locZ - d1;
                double d4 = d2 * d2 + d3 * d3;

                entity.g(d2 / d4 * 4.0D, 0.20000000298023224D, d3 / d4 * 4.0D);
            }
        }
    }

    private void b(List list) {
        for (int i = 0; i < list.size(); ++i) {
            Entity entity = (Entity) list.get(i);

            if (entity instanceof EntityLiving) {
                // CraftBukkit start - throw damage events when the dragon attacks
                // The EntityHuman case is handled in EntityHuman, so don't throw it here
                if (!(entity instanceof EntityHuman)) {
                    EntityDamageByEntityEvent damageEvent = new EntityDamageByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), org.bukkit.event.entity.EntityDamageEvent.DamageCause.ENTITY_ATTACK, 10);
                    Bukkit.getPluginManager().callEvent(damageEvent);

                    if (!damageEvent.isCancelled()) {
                        entity.getBukkitEntity().setLastDamageCause(damageEvent);
                        entity.damageEntity(DamageSource.mobAttack(this), damageEvent.getDamage());
                    }
                } else {
                    entity.damageEntity(DamageSource.mobAttack(this), 10);
                }
                // CraftBukkit end
            }
        }
    }

    private void i() {
        this.bN = false;
        if (this.random.nextInt(2) == 0 && !this.world.players.isEmpty()) {
            this.bR = (Entity) this.world.players.get(this.random.nextInt(this.world.players.size()));
        } else {
            boolean flag = false;

            do {
                this.a = 0.0D;
                this.b = (double) (70.0F + this.random.nextFloat() * 50.0F);
                this.c = 0.0D;
                this.a += (double) (this.random.nextFloat() * 120.0F - 60.0F);
                this.c += (double) (this.random.nextFloat() * 120.0F - 60.0F);
                double d0 = this.locX - this.a;
                double d1 = this.locY - this.b;
                double d2 = this.locZ - this.c;

                flag = d0 * d0 + d1 * d1 + d2 * d2 > 100.0D;
            } while (!flag);

            this.bR = null;
        }
    }

    private float b(double d0) {
        return (float) MathHelper.g(d0);
    }

    private boolean a(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.b);
        int k = MathHelper.floor(axisalignedbb.c);
        int l = MathHelper.floor(axisalignedbb.d);
        int i1 = MathHelper.floor(axisalignedbb.e);
        int j1 = MathHelper.floor(axisalignedbb.f);
        boolean flag = false;
        boolean flag1 = false;

        // CraftBukkit start - create a list to hold all the destroyed blocks
        List<org.bukkit.block.Block> destroyedBlocks = new java.util.ArrayList<org.bukkit.block.Block>();
        org.bukkit.craftbukkit.CraftWorld craftWorld = this.world.getWorld();
        // CraftBukkit end

        for (int k1 = i; k1 <= l; ++k1) {
            for (int l1 = j; l1 <= i1; ++l1) {
                for (int i2 = k; i2 <= j1; ++i2) {
                    int j2 = this.world.getTypeId(k1, l1, i2);

                    if (j2 != 0) {
                        if (j2 != Block.OBSIDIAN.id && j2 != Block.WHITESTONE.id && j2 != Block.BEDROCK.id) {
                            flag1 = true;
                            // CraftBukkit start - add blocks to list rather than destroying them
                            // this.world.setTypeId(k1, l1, i2, 0);
                            destroyedBlocks.add(craftWorld.getBlockAt(k1, l1, i2));
                            // CraftBukkit end
                        } else {
                            flag = true;
                        }
                    }
                }
            }
        }

        if (flag1) {
            // CraftBukkit start - set off an EntityExplodeEvent for the dragon exploding all these blocks
            org.bukkit.entity.Entity bukkitEntity = this.getBukkitEntity();
            EntityExplodeEvent event = new EntityExplodeEvent(bukkitEntity, bukkitEntity.getLocation(), destroyedBlocks, 0F);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                // this flag literally means 'Dragon hit something hard' (Obsidian, White Stone or Bedrock) and will cause the dragon to slow down.
                // We should consider adding an event extension for it, or perhaps returning true if the event is cancelled.
                return flag;
            } else {
                for (org.bukkit.block.Block block : event.blockList()) {
                    craftWorld.explodeBlock(block, event.getYield());
                }
            }
            // CraftBukkit end

            double d0 = axisalignedbb.a + (axisalignedbb.d - axisalignedbb.a) * (double) this.random.nextFloat();
            double d1 = axisalignedbb.b + (axisalignedbb.e - axisalignedbb.b) * (double) this.random.nextFloat();
            double d2 = axisalignedbb.c + (axisalignedbb.f - axisalignedbb.c) * (double) this.random.nextFloat();

            this.world.addParticle("largeexplode", d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }

        return flag;
    }

    public boolean a(EntityComplexPart entitycomplexpart, DamageSource damagesource, int i) {
        if (entitycomplexpart != this.g) {
            i = i / 4 + 1;
        }

        float f = this.yaw * 3.1415927F / 180.0F;
        float f1 = MathHelper.sin(f);
        float f2 = MathHelper.cos(f);

        this.a = this.locX + (double) (f1 * 5.0F) + (double) ((this.random.nextFloat() - 0.5F) * 2.0F);
        this.b = this.locY + (double) (this.random.nextFloat() * 3.0F) + 1.0D;
        this.c = this.locZ - (double) (f2 * 5.0F) + (double) ((this.random.nextFloat() - 0.5F) * 2.0F);
        this.bR = null;
        if (damagesource.getEntity() instanceof EntityHuman || damagesource == DamageSource.EXPLOSION) {
            this.dealDamage(damagesource, i);
        }

        return true;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        return false;
    }

    public boolean dealDamage(DamageSource damagesource, int i) { // CraftBukkit - protected -> public
        return super.damageEntity(damagesource, i);
    }

    protected void aP() {
        ++this.bP;
        if (this.bP >= 180 && this.bP <= 200) {
            float f = (this.random.nextFloat() - 0.5F) * 8.0F;
            float f1 = (this.random.nextFloat() - 0.5F) * 4.0F;
            float f2 = (this.random.nextFloat() - 0.5F) * 8.0F;

            this.world.addParticle("hugeexplosion", this.locX + (double) f, this.locY + 2.0D + (double) f1, this.locZ + (double) f2, 0.0D, 0.0D, 0.0D);
        }

        int i;
        int j;

        if (!this.world.isStatic) {
            if (this.bP > 150 && this.bP % 5 == 0) {
                i = expToDrop / 12; // CraftBukkit - drop experience as dragon falls from sky. use experience drop from death event. This is now set in getExpReward()

                while (i > 0) {
                    j = EntityExperienceOrb.getOrbValue(i);
                    i -= j;
                    this.world.addEntity(new EntityExperienceOrb(this.world, this.locX, this.locY, this.locZ, j));
                }
            }

            if (this.bP == 1) {
                this.world.e(1018, (int) this.locX, (int) this.locY, (int) this.locZ, 0);
            }
        }

        this.move(0.0D, 0.10000000149011612D, 0.0D);
        this.aw = this.yaw += 20.0F;
        if (this.bP == 200 && !this.world.isStatic) {
            i = expToDrop - 10 * (expToDrop / 12); // CraftBukkit - drop the remaining experience

            while (i > 0) {
                j = EntityExperienceOrb.getOrbValue(i);
                i -= j;
                this.world.addEntity(new EntityExperienceOrb(this.world, this.locX, this.locY, this.locZ, j));
            }

            this.c(MathHelper.floor(this.locX), MathHelper.floor(this.locZ));
            this.die();
        }
    }

    private void c(int i, int j) {
        byte b0 = 64;

        BlockEnderPortal.a = true;
        byte b1 = 4;

        // CraftBukkit start - Replace any "this.world" in the following with just "world"!
        BlockStateListPopulator world = new BlockStateListPopulator(this.world.getWorld());

        for (int k = b0 - 1; k <= b0 + 32; ++k) {
            for (int l = i - b1; l <= i + b1; ++l) {
                for (int i1 = j - b1; i1 <= j + b1; ++i1) {
                    double d0 = (double) (l - i);
                    double d1 = (double) (i1 - j);
                    double d2 = d0 * d0 + d1 * d1;

                    if (d2 <= ((double) b1 - 0.5D) * ((double) b1 - 0.5D)) {
                        if (k < b0) {
                            if (d2 <= ((double) (b1 - 1) - 0.5D) * ((double) (b1 - 1) - 0.5D)) {
                                world.setTypeId(l, k, i1, Block.BEDROCK.id);
                            }
                        } else if (k > b0) {
                            world.setTypeId(l, k, i1, 0);
                        } else if (d2 > ((double) (b1 - 1) - 0.5D) * ((double) (b1 - 1) - 0.5D)) {
                            world.setTypeId(l, k, i1, Block.BEDROCK.id);
                        } else {
                            world.setTypeId(l, k, i1, Block.ENDER_PORTAL.id);
                        }
                    }
                }
            }
        }

        world.setTypeId(i, b0 + 0, j, Block.BEDROCK.id);
        world.setTypeId(i, b0 + 1, j, Block.BEDROCK.id);
        world.setTypeId(i, b0 + 2, j, Block.BEDROCK.id);
        world.setTypeId(i - 1, b0 + 2, j, Block.TORCH.id);
        world.setTypeId(i + 1, b0 + 2, j, Block.TORCH.id);
        world.setTypeId(i, b0 + 2, j - 1, Block.TORCH.id);
        world.setTypeId(i, b0 + 2, j + 1, Block.TORCH.id);
        world.setTypeId(i, b0 + 3, j, Block.BEDROCK.id);
        world.setTypeId(i, b0 + 4, j, Block.DRAGON_EGG.id);

        EntityCreatePortalEvent event = new EntityCreatePortalEvent((org.bukkit.entity.LivingEntity) this.getBukkitEntity(), java.util.Collections.unmodifiableList(world.getList()), org.bukkit.PortalType.ENDER);
        this.world.getServer().getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            for (BlockState state : event.getBlocks()) {
                state.update(true);
            }
        } else {
            for (BlockState state : event.getBlocks()) {
                Packet53BlockChange packet = new Packet53BlockChange(state.getX(), state.getY(), state.getZ(), this.world);
                for (Iterator it = this.world.players.iterator(); it.hasNext();) {
                    EntityHuman entity = (EntityHuman) it.next();
                    if (entity instanceof EntityPlayer) {
                        ((EntityPlayer) entity).netServerHandler.sendPacket(packet);
                    }
                }
            }
        }
        // CraftBukkit end

        BlockEnderPortal.a = false;
    }

    protected void bk() {}

    public Entity[] ao() {
        return this.children;
    }

    public boolean L() {
        return false;
    }

    public World d() {
        return this.world;
    }

    protected String aY() {
        return "mob.enderdragon.growl";
    }

    protected String aZ() {
        return "mob.enderdragon.hit";
    }

    protected float aX() {
        return 5.0F;
    }

    // CraftBukkit start
    public int getExpReward() {
        // This value is equal to the amount of experience dropped while falling from the sky (10 * 1000)
        // plus what is dropped when the dragon hits the ground (2000)
        return 12000;
    }
    // CraftBukkit end
}
