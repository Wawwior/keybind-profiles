package me.wawwior.keybind_profiles.util;

import me.wawwior.keybind_profiles.compat.AmecsCompat;
import me.wawwior.keybind_profiles.config.KeybindEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBind;
import net.minecraft.util.annotation.Debug;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.function.Consumer;

public class KeybindUtil {

	private static final GameOptions gameOptions = MinecraftClient.getInstance().options;

	private static final HashMap<String, KeybindEntry> tempKeybinds = new HashMap<>();

	public static void iterateKeybinds(Consumer<KeyBind> consumer) {
		for (KeyBind keyBind : gameOptions.allKeys) {
			consumer.accept(keyBind);
		}
	}

	public static void storeKeybinds(HashMap<String, KeybindEntry> keyBinds) {
		KeybindUtil.iterateKeybinds(keyBind -> keyBinds.put(keyBind.getTranslationKey(), new KeybindEntry(keyBind.getKeyTranslationKey(), AmecsCompat.getModifiers(keyBind))));
	}

	@Debug
	@SuppressWarnings("unused")
	public static void printTempKeys(Logger logger) {
		tempKeybinds.forEach((key, value) -> logger.info(key.replaceAll("key\\.", "") + ": " + value.getKey().toString().replaceAll("key\\.", "") + " / " + value.getModifiers()));
	}

	public static void loadKeybinds(HashMap<String, KeybindEntry> keyBinds) {
		KeybindUtil.iterateKeybinds(
			keyBind -> {
				KeybindEntry entry = keyBinds.get(keyBind.getTranslationKey());
				if (entry != null) {
					keyBind.setBoundKey(entry.getKey());
					AmecsCompat.setModifiers(keyBind, entry.getModifiers());
				}
			}
		);
		KeyBind.updateBoundKeys();
	}

	public static void updateTemporaryKeybinds() {
		KeybindUtil.storeKeybinds(tempKeybinds);
	}

	public static void applyTemporaryKeybinds() {
		KeybindUtil.loadKeybinds(tempKeybinds);
	}

}
