package logisticspipes.network.abstractpackets

import logisticspipes.network.exception.TargetNotFoundException
import logisticspipes.pipes.basic.LogisticsTileGenericPipe
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import network.rs485.logisticspipes.util.LPDataInput
import network.rs485.logisticspipes.util.LPDataOutput
import network.rs485.logisticspipes.world.DoubleCoordinates

abstract class CoordinatesPacket(id: Int) : ModernPacket(id) {

    internal var posX: Int = 0
    internal var posY: Int = 0
    internal var posZ: Int = 0

    fun getPosX() = posX
    fun getPosY() = posY
    fun getPosZ() = posZ

    fun setPosX(posX: Int): CoordinatesPacket {
        this.posX = posX
        return this
    }

    fun setPosY(posY: Int): CoordinatesPacket {
        this.posY = posY
        return this
    }

    fun setPosZ(posZ: Int): CoordinatesPacket {
        this.posZ = posZ
        return this
    }

    override fun writeData(output: LPDataOutput) {
        super.writeData(output)
        output.writeInt(posX)
        output.writeInt(posY)
        output.writeInt(posZ)
    }

    override fun readData(input: LPDataInput) {
        super.readData(input)
        posX = input.readInt()
        posY = input.readInt()
        posZ = input.readInt()

    }

    fun setTilePos(tile: TileEntity): CoordinatesPacket {
        setDimension(tile.world)
        posX = tile.pos.x
        posY = tile.pos.y
        posZ = tile.pos.z
        return this
    }

    fun setLPPos(pos: DoubleCoordinates): CoordinatesPacket {
        posX = pos.xInt
        posY = pos.yInt
        posZ = pos.zInt
        return this
    }

    fun setPacketPos(packet: CoordinatesPacket): CoordinatesPacket {
        posX = packet.posX
        posY = packet.posY
        posZ = packet.posZ
        return this
    }

    fun setBlockPos(pos: BlockPos): CoordinatesPacket {
        posX = pos.x
        posY = pos.y
        posZ = pos.z
        return this
    }


    fun getTile(world: World, validateResult: (TileEntity) -> Boolean): TileEntity? {
        val tile = getTile(world, TileEntity::class.java)
        if (!validateResult(tile)) {
            targetNotFound("TileEntity condition not met")
            return null
        }
        return tile
    }

    /**
     * Retrieves tileEntity at packet coordinates if any.
     *
     * @param world
     * @param clazz
     * @return TileEntity
     */
    open fun <T> getTile(world: World, clazz: Class<T>): T {
        val pos = BlockPos(posX, posY, posZ)

        if (world.isAirBlock(pos)) {
            targetNotFound("Couldn't find ${clazz.name} at: $pos")
        }

        val tile = world.getTileEntity(pos)

        if (tile != null) {
            if (!clazz.isAssignableFrom(tile.javaClass)) {
                targetNotFound("Couldn't find ${clazz.name}, found ${tile.javaClass} at: $pos")
            }
        } else {
            targetNotFound("Couldn't find ${clazz.name} at: $pos")
        }

        return tile as T
    }

    /**
     * Retrieves tileEntity or CoreUnroutedPipe at packet coordinates if any.
     *
     * @param world
     * @param clazz
     * @return TileEntity
     */
    fun <T> getTileOrPipe(world: World, clazz: Class<T>): T {
        val pos = BlockPos(posX, posY, posZ)

        if (world.isAirBlock(pos)) {
            targetNotFound("Couldn't find " + clazz.name + " at: " + pos)
        }

        val tile = world.getTileEntity(pos)
        if (tile != null) {
            if (clazz.isAssignableFrom(tile.javaClass)) {
                return tile as T
            }
            if (tile is LogisticsTileGenericPipe) {
                if (tile.pipe != null && clazz.isAssignableFrom(tile.pipe.javaClass)) {
                    return tile.pipe as T
                }
                targetNotFound("Couldn't find " + clazz.name + ", found pipe with " + tile.javaClass + " at: " + pos)
            }
        } else {
            targetNotFound("Couldn't find " + clazz.name + " at: " + pos)
        }
        targetNotFound("Couldn't find " + clazz.name + ", found " + tile.javaClass + " at: " + pos)
    }

    /**
     * Retrieves pipe at packet coordinates if any.
     *
     * @param world
     * @return
     */
    @Deprecated("")
    fun getPipe(world: World): LogisticsTileGenericPipe {
        return getPipe(world, LTGPCompletionCheck.NONE)
    }

    fun getPipe(world: World, check: LTGPCompletionCheck): LogisticsTileGenericPipe {
        val pipe = getTile(world, LogisticsTileGenericPipe::class.java)
        if (check === LTGPCompletionCheck.PIPE || check === LTGPCompletionCheck.TRANSPORT) {
            if (pipe.pipe == null) {
                targetNotFound("The found pipe didn't have a loaded pipe field")
            }
        }
        if (check === LTGPCompletionCheck.TRANSPORT) {
            if (pipe.pipe.transport == null) {
                targetNotFound("The found pipe didn't have a loaded transport field")
            }
        }
        return pipe
    }

    protected fun targetNotFound(message: String): Nothing {
        throw TargetNotFoundException(message, this)
    }

    enum class LTGPCompletionCheck {
        NONE,
        PIPE,
        TRANSPORT
    }
}


