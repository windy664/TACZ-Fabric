package com.tacz.guns.client.input;

import com.tacz.guns.api.client.event.InputEvent;
import com.tacz.guns.api.client.gameplay.IClientPlayerGunOperator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import static com.tacz.guns.util.InputExtraCheck.isInGame;

@Environment(EnvType.CLIENT)
public class MeleeKey {
    public static final KeyBinding MELEE_KEY = new KeyBinding("key.tacz.melee.desc",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "key.category.tacz");

    public static void onMeleeKeyPress(InputEvent.Key event) {
        if (isInGame() && event.getAction() == GLFW.GLFW_PRESS && MELEE_KEY.matchesKey(event.getKey(), event.getScanCode())) {
            doMeleeLogic();
        }
    }

    public static void onMeleeMousePress(InputEvent.MouseButton.Post event) {
        if (isInGame() && event.getAction() == GLFW.GLFW_PRESS && MELEE_KEY.matchesMouse(event.getButton())) {
            doMeleeLogic();
        }
    }

    private static void doMeleeLogic() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null || player.isSpectator()) {
            return;
        }
        IClientPlayerGunOperator operator = IClientPlayerGunOperator.fromLocalPlayer(player);
        if (!operator.isAim()) {
            operator.melee();
        }
    }
}
