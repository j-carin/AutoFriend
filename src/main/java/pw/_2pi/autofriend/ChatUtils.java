package pw._2pi.autofriend;

import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.event.HoverEvent.Action;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

/**
 * Utility class to easily build IChatComponents (Text).
 *
 * @author Semx11
 */
public class ChatUtils {

	private final IChatComponent parent;

	private String text;
	private ChatStyle style;

	private ChatUtils(String text) {
		this(text, null, Inheritance.SHALLOW);
	}

	private ChatUtils(String text, IChatComponent parent, Inheritance inheritance) {
		this.parent = parent;
		this.text = text;

		switch (inheritance) {
		case DEEP:
			this.style = parent != null ? parent.getChatStyle() : new ChatStyle();
			break;
		default:
		case SHALLOW:
			this.style = new ChatStyle();
			break;
		case NONE:
			this.style = new ChatStyle().setColor(null).setBold(false).setItalic(false).setStrikethrough(false)
					.setUnderlined(false).setObfuscated(false).setChatClickEvent(null).setChatHoverEvent(null)
					.setInsertion(null);
			break;
		}
	}

	public static ChatUtils of(String text) {
		return new ChatUtils(text);
	}

	public ChatUtils setColor(EnumChatFormatting color) {
		style.setColor(color);
		return this;
	}

	public ChatUtils setBold(boolean bold) {
		style.setBold(bold);
		return this;
	}

	public ChatUtils setItalic(boolean italic) {
		style.setItalic(italic);
		return this;
	}

	public ChatUtils setStrikethrough(boolean strikethrough) {
		style.setStrikethrough(strikethrough);
		return this;
	}

	public ChatUtils setUnderlined(boolean underlined) {
		style.setUnderlined(underlined);
		return this;
	}

	public ChatUtils setObfuscated(boolean obfuscated) {
		style.setObfuscated(obfuscated);
		return this;
	}

	public ChatUtils setClickEvent(ClickEvent.Action action, String value) {
		style.setChatClickEvent(new ClickEvent(action, value));
		return this;
	}

	public ChatUtils setHoverEvent(String value) {
		return this.setHoverEvent(new ChatComponentText(value));
	}

	public ChatUtils setHoverEvent(IChatComponent value) {
		return this.setHoverEvent(Action.SHOW_TEXT, value);
	}

	public ChatUtils setHoverEvent(HoverEvent.Action action, IChatComponent value) {
		style.setChatHoverEvent(new HoverEvent(action, value));
		return this;
	}

	public ChatUtils setInsertion(String insertion) {
		style.setInsertion(insertion);
		return this;
	}

	public ChatUtils append(String text) {
		return this.append(text, Inheritance.SHALLOW);
	}

	public ChatUtils append(String text, Inheritance inheritance) {
		return new ChatUtils(text, this.build(), inheritance);
	}

	public IChatComponent build() {
		IChatComponent thisComponent = new ChatComponentText(text).setChatStyle(style);
		return parent != null ? parent.appendSibling(thisComponent) : thisComponent;
	}

	public enum Inheritance {
		DEEP, SHALLOW, NONE
	}

}