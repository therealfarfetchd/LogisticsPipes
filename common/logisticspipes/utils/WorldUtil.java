/** 
 * Copyright (c) Krapht, 2011
 * 
 * "LogisticsPipes" is distributed under the terms of the Minecraft Mod Public 
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package logisticspipes.utils;

import java.util.LinkedList;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class WorldUtil {
	private int _x;
	private int _y;
	private int _z;
	
	private World _worldObj;

	public WorldUtil(World worldObj, int x, int y, int z) {
		this._worldObj = worldObj;
		this._x = x;
		this._y = y;
		this._z = z;
	}

	public WorldUtil(TileEntity tile) {
		this._worldObj = tile.worldObj;
		this._x = tile.xCoord;
		this._y = tile.yCoord;
		this._z = tile.zCoord;
	}

	public LinkedList<AdjacentTile> getAdjacentTileEntities() {
		return getAdjacentTileEntities(false);
	}

	public LinkedList<AdjacentTile> getAdjacentTileEntities(boolean flag) {
		LinkedList<AdjacentTile> foundTiles = new LinkedList<AdjacentTile>();
		TileEntity tilePipe = null;
		if(flag) {
			tilePipe = _worldObj.getBlockTileEntity(_x, _y, _z);
		}
		for (ForgeDirection o : ForgeDirection.values()) {
			if (o == ForgeDirection.UNKNOWN) continue;
			
			TileEntity tile = getAdjacentTileEntitie(o);
			
			if (tile == null) continue;
			
			if(flag) {
				if(tilePipe instanceof TileGenericPipe) {
					if(((TileGenericPipe)tilePipe).pipe != null) {
						if(!((TileGenericPipe)tilePipe).pipe.canPipeConnect(tile, o)) {
							continue;
						}
					}
				}
			}
			foundTiles.add(new AdjacentTile(tile, o));
		}
		return foundTiles;
	}
	
	public TileEntity getAdjacentTileEntitie(ForgeDirection direction) {
		Position p = new Position(_x, _y, _z, direction);
		p.moveForwards(1);
		return _worldObj.getBlockTileEntity((int)p.x, (int)p.y, (int)p.z);
	}
}
