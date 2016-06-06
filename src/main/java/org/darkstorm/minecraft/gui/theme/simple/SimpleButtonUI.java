package org.darkstorm.minecraft.gui.theme.simple;

import static org.lwjgl.opengl.GL11.*;

import java.awt.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.darkstorm.minecraft.gui.component.Button;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.theme.AbstractComponentUI;
import org.darkstorm.minecraft.gui.util.RenderUtil;

import com.masl.mp3Jukebox.mp3Jukebox;

public class SimpleButtonUI extends AbstractComponentUI<Button> {
	private final SimpleTheme theme;

	SimpleButtonUI(SimpleTheme theme) {
		super(Button.class);
		this.theme = theme;

		foreground = Color.WHITE;
		background = new Color(128, 128, 128, 128 + 128 / 2);
	}

	@Override
	protected void renderComponent(Button button) {
		translateComponent(button, false);
		Rectangle area = button.getArea();
		
		
		glEnable(GL_BLEND);
		glDisable(GL_CULL_FACE);

		
		RenderUtil.setColor(button.getBackgroundColor());
		glDisable(GL_TEXTURE_2D);
		glBegin(GL_QUADS);
		{
			glTexCoord2d(0, 0);
			glVertex2d(0, 0);
			glTexCoord2d(1, 0);
			glVertex2d(area.width, 0);
			glTexCoord2d(1, 1);
			glVertex2d(area.width, area.height);
			glTexCoord2d(0, 1);
			glVertex2d(0, area.height);
		}
		glEnd();
		
		
		Point mouse = RenderUtil.calculateMouseLocation();
		Component parent = button.getParent();
		while(parent != null) {
			mouse.x -= parent.getX();
			mouse.y -= parent.getY();
			parent = parent.getParent();
		}
		if(area.contains(mouse)) {
			glColor4f(0.0f, 0.0f, 0.0f, Mouse.isButtonDown(0) ? 0.5f : 0.3f);
			glBegin(GL_QUADS);
			{
				glVertex2d(0, 0);
				glVertex2d(area.width, 0);
				glVertex2d(area.width, area.height);
				glVertex2d(0, area.height);
			}
			glEnd();
		}
		glEnable(GL_TEXTURE_2D);

		String text = button.getText();
		theme.getFontRenderer().drawString(
				text,
				area.width / 2 - theme.getFontRenderer().getStringWidth(text)
						/ 2,
				area.height / 2 - theme.getFontRenderer().FONT_HEIGHT / 2,
				RenderUtil.toRGBA(button.getForegroundColor()));

		glEnable(GL_CULL_FACE);
		glDisable(GL_BLEND);
		
		translateComponent(button, true);
	}

	@Override
	protected Dimension getDefaultComponentSize(Button component) {
		return new Dimension(theme.getFontRenderer().getStringWidth(
				component.getText()) + 4,
				theme.getFontRenderer().FONT_HEIGHT + 4);
	}

	@Override
	protected Rectangle[] getInteractableComponentRegions(Button component) {
		return new Rectangle[] { new Rectangle(0, 0, component.getWidth(),component.getHeight()) };
	}

	@Override
	protected void handleComponentInteraction(Button component, Point location,
			int button) {
		if(location.x <= component.getWidth()
				&& location.y <= component.getHeight() && button == 0)
			component.press();
	}
}