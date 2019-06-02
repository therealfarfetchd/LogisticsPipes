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
import logisticspipes.utils.IPositionRotateble
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import network.rs485.logisticspipes.util.LPDataInput
import network.rs485.logisticspipes.util.LPDataOutput
import network.rs485.logisticspipes.util.LPSerializable
import kotlin.math.sqrt

open class DoubleCoordinates(internal var xCoord: Double, internal var yCoord: Double, internal var zCoord: Double) : IPositionRotateble, ICoordinates, LPSerializable {

    override val xDouble: Double
        get() = xCoord

    override val yDouble: Double
        get() = yCoord

    override val zDouble: Double
        get() = zCoord

    override val xInt: Int
        get() = xCoord.toInt()

    override val yInt: Int
        get() = yCoord.toInt()

    override val zInt: Int
        get() = zCoord.toInt()

    constructor(input: LPDataInput) : this(0.0, 0.0, 0.0) {
        read(input)
    }

    constructor(coords: ICoordinates) : this(coords.xDouble, coords.yDouble, coords.zDouble)

    constructor(tile: TileEntity) : this(tile.pos)

    constructor(pipe: CoreUnroutedPipe) : this(pipe.x.toDouble(), pipe.y.toDouble(), pipe.z.toDouble())

    constructor(pipe: IPipeInformationProvider) : this(pipe.x.toDouble(), pipe.y.toDouble(), pipe.z.toDouble())

    constructor(packet: CoordinatesPacket) : this(packet.posX.toDouble(), packet.posY.toDouble(), packet.posZ.toDouble())

    constructor(entity: Entity) : this(entity.posX, entity.posY, entity.posZ)

    constructor(pos: BlockPos) : this(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())

    fun getBlockPos(): BlockPos = BlockPos(xCoord, yCoord, zCoord)

    // Only needed because a lot of stuff relies on setXCoord returning `this`..

    fun getXCoord() = xCoord
    fun getYCoord() = yCoord
    fun getZCoord() = zCoord

    fun setXCoord(xCoord: Double): DoubleCoordinates {
        this.xCoord = xCoord
        return this
    }

    fun setYCoord(yCoord: Double): DoubleCoordinates {
        this.yCoord = yCoord
        return this
    }

    fun setZCoord(zCoord: Double): DoubleCoordinates {
        this.zCoord = zCoord
        return this
    }

    fun getTileEntity(world: IBlockAccess): TileEntity? {
        return world.getTileEntity(getBlockPos())
    }

    fun toIntBasedString(): String {
        return toString()
    }

    fun getBlock(world: IBlockAccess): Block {
        return getBlockState(world).block
    }

    fun getBlockState(world: IBlockAccess): IBlockState {
        return world.getBlockState(getBlockPos())
    }

    fun blockExists(world: World): Boolean {
        return !world.isAirBlock(getBlockPos())
    }

    fun distanceTo(targetPos: DoubleCoordinates): Double {
        val dx = targetPos.xCoord - xCoord
        val dy = targetPos.yCoord - yCoord
        val dz = targetPos.zCoord - zCoord

        return sqrt(dx * dx + dy * dy + dz * dz)
    }

    fun center(): DoubleCoordinates {
        val coords = DoubleCoordinates(xInt + 0.5, yInt + 0.5, zInt + 0.5)
        return coords
    }

    fun writeToNBT(prefix: String, nbt: NBTTagCompound) {
        nbt.setDouble(prefix + "xPos", xCoord)
        nbt.setDouble(prefix + "yPos", yCoord)
        nbt.setDouble(prefix + "zPos", zCoord)
    }

    open fun add(toAdd: DoubleCoordinates): DoubleCoordinates {
        xCoord += toAdd.xCoord
        yCoord += toAdd.yCoord
        zCoord += toAdd.zCoord
        return this
    }

    fun setBlockToAir(world: World) {
        world.setBlockToAir(getBlockPos())
    }

    override fun rotateLeft() {
        val tmp = zCoord
        zCoord = -xCoord
        xCoord = tmp
    }

    override fun rotateRight() {
        val tmp = xCoord
        xCoord = -zCoord
        zCoord = tmp
    }

    override fun mirrorX() {
        xCoord = -xCoord
    }

    override fun mirrorZ() {
        zCoord = -zCoord
    }

    fun getLength(): Double {
        return sqrt(xCoord * xCoord + yCoord * yCoord + zCoord * zCoord)
    }

    override fun read(input: LPDataInput) {
        xCoord = input.readDouble()
        yCoord = input.readDouble()
        zCoord = input.readDouble()
    }

    override fun write(output: LPDataOutput) {
        output.writeDouble(xCoord)
        output.writeDouble(yCoord)
        output.writeDouble(zCoord)
    }

    companion object {
        @JvmStatic
        fun readFromNBT(prefix: String, nbt: NBTTagCompound): DoubleCoordinates? {
            return if (
                    nbt.hasKey(prefix + "xPos") &&
                    nbt.hasKey(prefix + "yPos") &&
                    nbt.hasKey(prefix + "zPos")
            ) DoubleCoordinates(nbt.getDouble(prefix + "xPos"), nbt.getDouble(prefix + "yPos"), nbt.getDouble(prefix + "zPos"))
            else null
        }
    }

}