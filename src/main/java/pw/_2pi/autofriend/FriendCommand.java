package pw._2pi.autofriend;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class FriendCommand extends CommandBase {
	private Pattern username = Pattern.compile("\\w{1,16}");

	public String getCommandName() {
		return "autofriend";
	}

	public int getRequiredPermissionLevel() {
		return 0;
	}

	public boolean canSenderUseCommand(ICommandSender sender) {
		return true;
	}

	public void throwError(String error, ICommandSender sender) {
		sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Error: " + error));
	}

	public void sendMessage(String message, ICommandSender sender) {
		sender.addChatMessage(new ChatComponentText(message));
	}

	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length > 0) {
			if (args[0].equals("toggle")) {
				AutoFriend.toggle = !AutoFriend.toggle;
				sendMessage(EnumChatFormatting.BLUE + "AutoFriend: "
						+ (AutoFriend.toggle ? EnumChatFormatting.GREEN + "On" : EnumChatFormatting.RED + "Off"),
						sender);
			} else if (args[0].equals("messages")) {
				AutoFriend.messages = !AutoFriend.messages;
				BlacklistUtils.writeBlacklist();
				if (!AutoFriend.messages)
					sender.addChatMessage(ChatUtils
							.of("NOTE: This will also remove divider messages which may affect other messages on Hypixel. Hover on this message to see what they look like.")
							.setColor(EnumChatFormatting.RED).setBold(true)
							.setHoverEvent(HoverEvent.Action.SHOW_TEXT,
									ChatUtils.of("-----------------------------------------------------")
											.setColor(EnumChatFormatting.WHITE).build())
							.build());
				sendMessage(EnumChatFormatting.BLUE + "Friend Messages: "
						+ (AutoFriend.messages ? EnumChatFormatting.GREEN + "On" : EnumChatFormatting.RED + "Off"),
						sender);

			} else if (args[0].equals("recent")) {
				if (AutoFriend.recent.size() > 0) {
					int page = 1;
					int pages = (int) Math.ceil((double) AutoFriend.recent.size() / 7.0);

					if (args.length > 1) {
						try {
							page = Integer.parseInt(args[1]);
						} catch (NumberFormatException ignored) {
							page = -1;
						}
					}

					if (page < 1 || page > pages) {
						throwError("Invalid page number", sender);
					} else {
						sendMessage(EnumChatFormatting.GRAY + "----------------------", sender);
						sendMessage(EnumChatFormatting.BLUE + "Friend History " + EnumChatFormatting.DARK_AQUA
								+ "(Page " + page + " of " + pages + ")", sender);

						AutoFriend.recent.stream().skip((page - 1) * 7).limit(7)
								.forEach(name -> sender.addChatMessage(ChatUtils
										.of(EnumChatFormatting.BLUE + name + EnumChatFormatting.GRAY + " - ")
										.append("[REMOVE]").setColor(EnumChatFormatting.RED)
										.setClickEvent(ClickEvent.Action.RUN_COMMAND, "/f remove " + name)
										.setHoverEvent(HoverEvent.Action.SHOW_TEXT,
												ChatUtils.of("Remove " + name).setColor(EnumChatFormatting.RED).build())
										.append(" - ").setColor(EnumChatFormatting.GRAY).append("[BLACKLIST]")
										.setColor(EnumChatFormatting.DARK_GRAY)
										.setClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
												"/autofriend blacklist add " + name)
										.setHoverEvent(HoverEvent.Action.SHOW_TEXT, ChatUtils.of("Blacklist " + name)
												.setColor(EnumChatFormatting.RED).build())
										.build()));

						sendMessage(EnumChatFormatting.GRAY + "----------------------", sender);

					}

				} else {
					throwError("You haven't friended anyone", sender);
				}
			} else if (args[0].equals("blacklist")) {
				if (args.length == 1 || (!args[1].equals("add") && !args[1].equals("remove"))) {
					if (AutoFriend.blacklist.size() > 0) {
						int page = 1;
						int pages = (int) Math.ceil((double) AutoFriend.blacklist.size() / 7.0);

						if (args.length > 1) {
							try {
								page = Integer.parseInt(args[1]);
							} catch (NumberFormatException ignored) {
								page = -1;
							}
						}

						if (page < 1 || page > pages) {
							throwError("Invalid page number", sender);
						} else {
							sendMessage(EnumChatFormatting.GRAY + "----------------------", sender);
							sendMessage(EnumChatFormatting.BLUE + "Blacklist " + EnumChatFormatting.DARK_AQUA + "(Page "
									+ page + " of " + pages + ")", sender);

							AutoFriend.blacklist.stream().skip((page - 1) * 7).limit(7)
									.forEach(name -> sender.addChatMessage(ChatUtils
											.of(EnumChatFormatting.BLUE + name + EnumChatFormatting.GRAY + " - ")
											.append("[REMOVE]").setColor(EnumChatFormatting.RED)
											.setClickEvent(ClickEvent.Action.RUN_COMMAND, "/f remove " + name)
											.setHoverEvent(HoverEvent.Action.SHOW_TEXT,
													ChatUtils.of("Remove " + name).setColor(EnumChatFormatting.RED)
															.build())
											.append(" - ").setColor(EnumChatFormatting.GRAY).append("[UNBLACKLIST]")
											.setColor(EnumChatFormatting.GREEN)
											.setClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
													"/autofriend blacklist remove " + name)
											.setHoverEvent(HoverEvent.Action.SHOW_TEXT, ChatUtils
													.of("Blacklist " + name).setColor(EnumChatFormatting.RED).build())
											.build()));

							sendMessage(EnumChatFormatting.GRAY + "----------------------", sender);

						}

					} else {
						throwError("You haven't blacklisted anyone", sender);
					}

				} else if (args[1].equals("add") || args[1].equals("remove")) {
					if (args.length > 2 && username.matcher(args[2]).matches()) {
						if (args[1].equals("add")) {
							AutoFriend.blacklist.add(args[2]);
						} else {
							AutoFriend.blacklist.remove(args[2]);
						}
						sendMessage(EnumChatFormatting.BLUE + (args[1].equals("add")
								? "Added " + EnumChatFormatting.RED + args[2] + EnumChatFormatting.BLUE + " to"
								: "Removed " + EnumChatFormatting.GREEN + args[2] + EnumChatFormatting.BLUE + " from")
								+ " your blacklist.", sender);
						BlacklistUtils.writeBlacklist();
					} else {
						throwError("Invalid username. Usage: /autofriend blacklist add/remove <username>", sender);
					}
				} else {
					throwError("/autofriend blacklist <add, remove, [number]>", sender);
				}
			} else {
				throwError(getCommandUsage(sender), sender);
			}
		} else {
			throwError(getCommandUsage(sender), sender);

		}

	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/autofriend <toggle, messages, recent, blacklist>";
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		switch (args.length) {
		case 1:
			return getListOfStringsMatchingLastWord(args, "toggle", "messages", "recent", "blacklist");
		case 2:
			if (args[0].equalsIgnoreCase("blacklist")) {
				return getListOfStringsMatchingLastWord(args, "add", "remove");
			}
		}
		return Collections.emptyList();
	}

}
