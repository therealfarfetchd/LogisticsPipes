/*
 * Copyright (c) 2019  RS485
 *
 * "LogisticsPipes" is distributed under the terms of the Minecraft Mod Public
 * License 1.0.1, or MMPL. Please check the contents of the license located in
 * https://github.com/RS485/LogisticsPipes/blob/dev/LICENSE.md
 *
 * This file can instead be distributed under the license terms of the
 * MIT license:
 *
 * Copyright (c) 2019  RS485
 *
 * This MIT license was reworded to only match this file. If you use the regular
 * MIT license in your project, replace this copyright notice (this line and any
 * lines below and NOT the copyright line above) with the lines from the original
 * MIT license located here: http://opensource.org/licenses/MIT
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this file and associated documentation files (the "Source Code"), to deal in
 * the Source Code without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Source Code, and to permit persons to whom the Source Code is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Source Code, which also can be
 * distributed under the MIT.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package network.rs485.logisticspipes.world

import logisticspipes.network.abstractpackets.CoordinatesPacket
import logisticspipes.pipes.basic.CoreUnroutedPipe
import logisticspipes.routing.pathfinder.IPipeInformationProvider
import net.minecraft.entity.Entity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos

class DoubleCoordinatesType<T>(xCoord: Double, yCoord: Double, zCoord: Double, val type: T) : DoubleCoordinates(xCoord, yCoord, zCoord) {

    constructor(coords: ICoordinates, type: T) : this(coords.xDouble, coords.yDouble, coords.zDouble, type)

    constructor(tile: TileEntity, type: T) : this(tile.pos, type)

    constructor(pipe: CoreUnroutedPipe, type: T) : this(pipe.x.toDouble(), pipe.y.toDouble(), pipe.z.toDouble(), type)

    constructor(pipe: IPipeInformationProvider, type: T) : this(pipe.x.toDouble(), pipe.y.toDouble(), pipe.z.toDouble(), type)

    constructor(packet: CoordinatesPacket, type: T) : this(packet.posX.toDouble(), packet.posY.toDouble(), packet.posZ.toDouble(), type)

    constructor(entity: Entity, type: T) : this(entity.posX, entity.posY, entity.posZ, type)

    constructor(pos: BlockPos, type: T) : this(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), type)

    @Deprecated("Only called by reflection")
    constructor(xCoord: Double, yCoord: Double, zCoord: Double) : this(xCoord, yCoord, zCoord, TODO("uhh what?"))

    override fun add(toAdd: DoubleCoordinates): DoubleCoordinatesType<T> {
        setXCoord(getXCoord() + toAdd.getXCoord())
        setYCoord(getYCoord() + toAdd.getYCoord())
        setZCoord(getZCoord() + toAdd.getZCoord())
        return this
    }

}