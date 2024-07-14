package com.tacz.guns.client.input;

import com.tacz.guns.api.item.IGun;
import com.tacz.guns.config.util.InteractKeyConfigRead;
import com.tacz.guns.util.InputExtraCheck;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

public class InteractKey implements ClientTickEvents.EndTick {
    public static final KeyBinding INTERACT_KEY = new KeyBinding("key.tacz.interact.desc",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            "key.category.tacz");

    @Override
    public void onEndTick(MinecraftClient client) {
        if (InputExtraCheck.isInGame()) {
            while (INTERACT_KEY.wasPressed()) {
                doInteractLogic();
            }
        }
    }

    private static void doInteractLogic() {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player == null || player.isSpectator()) {
            return;
        }
        if (!IGun.mainhandHoldGun(player)) {
            return;
        }
        HitResult hitResult = mc.crosshairTarget;
        if (hitResult == null) {
            return;
        }
        if (hitResult instanceof BlockHitResult blockHitResult) {
            interactBlock(blockHitResult, player, mc);
            return;
        }
        if (hitResult instanceof EntityHitResult entityHitResult) {
            interactEntity(entityHitResult, mc);
        }
    }

    private static void interactBlock(BlockHitResult blockHitResult, ClientPlayerEntity player, MinecraftClient mc) {
        BlockPos blockPos = blockHitResult.getBlockPos();
        BlockState block = player.getWorld().getBlockState(blockPos);
        if (InteractKeyConfigRead.canInteractBlock(block)) {
            mc.doItemUse();
        }
    }

    private static void interactEntity(EntityHitResult entityHitResult, MinecraftClient mc) {
        Entity entity = entityHitResult.getEntity();
        if (InteractKeyConfigRead.canInteractEntity(entity)) {
            mc.doItemUse();
        }
    }
}
