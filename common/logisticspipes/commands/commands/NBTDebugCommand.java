package logisticspipes.commands.commands;

import ibxm.Player;
import logisticspipes.commands.LogisticsPipesCommand;
import logisticspipes.commands.abstracts.ICommandHandler;
import logisticspipes.network.PacketHandler;
import logisticspipes.network.packets.ActivateNBTDebug;
import logisticspipes.proxy.MainProxy;
import net.minecraft.command.ICommandSender;

public class NBTDebugCommand implements ICommandHandler {
	
	@Override
	public String[] getNames() {
		return new String[]{"nbtdebug"};
	}
	
	@Override
	public boolean isCommandUsableBy(ICommandSender sender) {
		return sender instanceof Player && LogisticsPipesCommand.isOP(sender);
	}
	
	@Override
	public String[] getDescription() {
		return new String[]{"Enables the Hotkey to show an debug gui", "for the howered item. (Only if NEI is installed)"};
	}
	
	@Override
	public void executeCommand(ICommandSender sender, String[] args) {
		sender.sendChatToPlayer(ChatMessageComponent.createFromText("Trying to Enable NBTDebug"));
    	MainProxy.sendPacketToPlayer(PacketHandler.getPacket(ActivateNBTDebug.class), (Player)sender);
	}
}
