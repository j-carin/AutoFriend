package pw._2pi.autofriend;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FriendListener {
	private Pattern friend = Pattern.compile(
			"\u00A7m----------------------------------------------------Friend request from (?<name>.+)\\[ACCEPT\\] - \\[DENY\\] - \\[IGNORE\\].*");

	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent event) {
		if (AutoFriend.hypixel && AutoFriend.toggle) {

			String msg = event.message.getUnformattedText();
			msg = msg.replace("\n", "");
			Matcher friendMatcher = friend.matcher(msg);
			if (friendMatcher.matches()) {
				String name = friendMatcher.group("name");
				if (name.startsWith("[")) {
					name = name.substring(name.indexOf("] ") + 2);
				}
				if (!AutoFriend.blacklist.stream().anyMatch(name::equalsIgnoreCase)) {
					System.out.println("Friending " + name);
					AutoFriend.mc.thePlayer.sendChatMessage("/friend accept " + name);
					AutoFriend.recent.add(name);
				}
				if (!AutoFriend.messages) {
					event.setCanceled(true);
				}
			}
			if (!AutoFriend.messages && msg.endsWith(" removed you from their friends list!")) {
				event.setCanceled(true);
			}
			if (!AutoFriend.messages && msg.startsWith("You are now friends with ")) {
				event.setCanceled(true);
			}
			if (!AutoFriend.messages && msg.equals("-----------------------------------------------------")) {
				event.setCanceled(true);
			}

		}
	}
}
