package logisticspipes.modules;

import javax.swing.Icon;

import logisticspipes.pipes.basic.CoreRoutedPipe.ItemSendMode;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModuleProviderMk2 extends ModuleProvider {
	@Override
	protected int neededEnergy() {
		return 2;
	}
	
	@Override
	protected ItemSendMode itemSendMode() {
		return ItemSendMode.Fast;
	}
	
	@Override
	protected int itemsToExtract() {
		return 128;
	}

	@Override
	protected int stacksToExtract() {
		return 8;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconTexture(IconRegister register) {
		return register.registerIcon("logisticspipes:itemModule/ModuleProviderMk2");
	}
}
