package com.example.cartpvpmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityEvents;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CartPvPMod implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof PersistentProjectileEntity arrow) {
                new Thread(() -> {
                    try {
                        while (!arrow.inGround) {
                            Thread.sleep(50);
                        }
                        if (!arrow.getWorld().isClient) {
                            World serverWorld = arrow.getWorld();
                            BlockPos pos = BlockPos.ofFloored(arrow.getX(), arrow.getY() - 1, arrow.getZ());

                            if (serverWorld.getBlockState(pos).isAir()) {
                                serverWorld.setBlockState(pos, Blocks.RAIL.getDefaultState());
                            }

                            TntMinecartEntity cart = new TntMinecartEntity(serverWorld, arrow.getX(), arrow.getY(), arrow.getZ());
                            serverWorld.spawnEntity(cart);
                            arrow.discard();
                        }
                    } catch (InterruptedException ignored) {}
                }).start();
            }
        });
    }
}
