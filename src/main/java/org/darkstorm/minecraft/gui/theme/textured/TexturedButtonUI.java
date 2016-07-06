package org.darkstorm.minecraft.gui.theme.textured;

import static org.lwjgl.opengl.GL11.*;

import java.awt.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.darkstorm.minecraft.gui.component.Button;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.theme.AbstractComponentUI;
import org.darkstorm.minecraft.gui.util.RenderUtil;

import com.masl.mp3Jukebox.mp3Jukebox;

public class TexturedButtonUI extends AbstractComponentUI<Button> {
	private final TexturedTheme theme;
	private final ResourceLocation texture;
	
	
	TexturedButtonUI(TexturedTheme texturedTheme, ResourceLocation bg) {
		super(Button.class);
		this.theme = texturedTheme;
		this.texture=bg;
	}

	@Override
	protected void renderComponent(Button button) {
		
		translateComponent(button, false);
		
		Rectangle area = button.getArea();
		double x = button.getX();
		double y = button.getY();
		
		//DrawBG
		
		
		
		glColor4f(1, 1, 1, 1);
		
		
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		VertexBuffer  wr = Tessellator.getInstance().getBuffer();
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		wr.pos(0, button.getHeight(), 0).tex(0, 0.5).endVertex();
		wr.pos(button.getWidth(), button.getHeight(), 0).tex(1, 0.5).endVertex();
		wr.pos(button.getWidth(), 0, 0).tex(1, 0).endVertex();;
		wr.pos(0, 0, 0).tex(0, 0).endVertex();
		Tessellator.getInstance().draw();
		
		
		
		
		Point mouse = RenderUtil.calculateMouseLocation();
		Component parent = button.getParent();
		while(parent != null) {
			mouse.x -= parent.getX();
			mouse.y -= parent.getY();
			parent = parent.getParent();
		}
		if(area.contains(mouse)) {
			//Draw Mouse on
			glColor4f(1, 1, 1, 1);
		
			wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			wr.pos(0, button.getHeight(), 0).tex(0, 1).endVertex();
			wr.pos(button.getWidth(), button.getHeight(), 0).tex(1, 1).endVertex();
			wr.pos(button.getWidth(), 0, 0).tex(1, 0.5).endVertex();;
			wr.pos(0, 0, 0).tex(0, 0.5).endVertex();
			Tessellator.getInstance().draw();
		}
		

		glPushAttrib(GL_TEXTURE_BIT); //Workaround for bug Slick2D or darkstorm made (have to investigate this further)
		String text = button.getText();
		theme.getFontRenderer().drawString(
				text,
				(area.width / 2 - theme.getFontRenderer().getStringWidth(text)
						/ 2),
				Math.round(area.height / 2.0f - theme.getFontRenderer().FONT_HEIGHT / 2.0f) - 1,
				RenderUtil.toRGBA(Color.white)); 
		glPopAttrib();
		
		
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		
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