package pw._2pi.autofriend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

@Mod(modid = AutoFriend.MODID, version = AutoFriend.VERSION, clientSideOnly = true, acceptedMinecraftVersions = "1.8.9")
public class AutoFriend {
	public static final String MODID = "AutoFriend";
	public static final String VERSION = "1.1";

	public static Minecraft mc = Minecraft.getMinecraft();
	public static boolean hypixel = false;
	public static boolean toggle = true;
	public static boolean messages = true;
	public static List<String> blacklist = new ArrayList();
	public static List<String> recent = new ArrayList();

	@EventHandler
	public void init(FMLInitializationEvent event) throws IOException {
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new FriendListener());
		ClientCommandHandler.instance.registerCommand(new FriendCommand());
		BlacklistUtils.getBlacklist();
	}

	@SubscribeEvent
	public void playerLoggedIn(ClientConnectedToServerEvent event) {
		if (!mc.isSingleplayer()) {
			hypixel = event.manager.getRemoteAddress().toString().toLowerCase().contains("hypixel.net");
		}
	}

	@SubscribeEvent
	public void playerLoggedOut(ClientDisconnectionFromServerEvent event) {
		hypixel = false;
	}

}
