package org.darkstorm.minecraft.gui.theme.textured;

import java.awt.Font;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

import org.darkstorm.minecraft.gui.font.UnicodeFontRenderer;
import org.darkstorm.minecraft.gui.theme.AbstractTheme;
import org.darkstorm.minecraft.gui.theme.simple.SimpleFrameUI;

import com.masl.mp3Jukebox.mp3Jukebox;

public class TexturedTheme extends AbstractTheme {
	private final FontRenderer fontRenderer;

	public TexturedTheme() {
		fontRenderer = new UnicodeFontRenderer(new Font("Trebuchet MS", Font.PLAIN, 15));

		installUI(new TexturedFrameUI(this, new ResourceLocation(mp3Jukebox.MODID, "gui/background.png"), new ResourceLocation(mp3Jukebox.MODID, "gui/minBtn.png"), new ResourceLocation(mp3Jukebox.MODID, "gui/pinBtn.png"), new ResourceLocation(mp3Jukebox.MODID, "gui/closeBtn.png")));
		installUI(new SimplePanelUI(this));
		installUI(new SimpleLabelUI(this));
		installUI(new TexturedButtonUI(this, new ResourceLocation(mp3Jukebox.MODID, "gui/button.png")));
		installUI(new SimpleCheckButtonUI(this));
		installUI(new SimpleComboBoxUI(this));
		installUI(new SimpleSliderUI(this));
		installUI(new SimpleProgressBarUI(this));
	}

	public FontRenderer getFontRenderer() {
		return fontRenderer;
	}
}
