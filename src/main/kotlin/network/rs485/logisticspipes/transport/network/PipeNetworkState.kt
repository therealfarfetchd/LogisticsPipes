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

package network.rs485.logisticspipes.transport.network

import net.fabricmc.fabric.api.util.NbtType
import net.minecraft.block.Block
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.world.PersistentState
import net.minecraft.world.dimension.Dimension
import network.rs485.logisticspipes.pipe.shape.BlockFace
import network.rs485.logisticspipes.pipe.shape.PipeShape
import network.rs485.logisticspipes.transport.Pipe
import network.rs485.logisticspipes.transport.PipeNetwork
import java.util.*

class PipeNetworkState(val world: ServerWorld) : PersistentState(getNameForDimension(world.getDimension())) {

    private val networks = mutableMapOf<UUID, PipeNetworkImpl>()

    @JvmSynthetic
    internal val posToNetworks = mutableMapOf<BlockPos, UUID>()

    @JvmSynthetic
    internal val portLocationToNetwork = mutableMapOf<BlockFace, UUID>()

    private val cachedBlocks = mutableMapOf<BlockPos, Block>()

    fun getNetworkById(id: UUID): PipeNetwork? {
        return networks[id]
    }

    @Suppress("UNCHECKED_CAST")
    fun onBlockChanged(pos: BlockPos) {
        val oldBlock = cachedBlocks[pos]
        val state = world.getBlockState(pos)
        val newBlock = state.block

        val attr = PipeAttribute.ATTRIBUTE.getFirstOrNull(world, pos)

        if (oldBlock != null && oldBlock != newBlock || attr == null) {
            posToNetworks.remove(pos)?.also {
                val network = networks.getValue(it)
                val node = network.getNodeAt(pos)
                if (node != null) {
                    node.data.pipe.onLeaveNetwork()
                    network.removeNode(node)
                    rebuildRefs(network.id)
                }
            }
            cachedBlocks -= pos
        }

        if (attr == null || (oldBlock != null && oldBlock == newBlock)) return

        cachedBlocks[pos] = newBlock

        var net = createNetwork()

        var node = net.createNode(pos, attr.type.getBaseShape(state).translate(pos) as PipeShape<Any?>, attr.create() as Pipe<*, Any?>)

        for ((port, face) in node.data.shape.ports) {
            val other = face.opposite
            val netId = portLocationToNetwork[other] ?: continue
            if (netId != net.id) {
                val otherNet = networks.getValue(netId)
                otherNet.merge(net)
                net = otherNet
            }
            val otherNode = net.getNodeByFace(other)!!
            node = net.getNodeById(node.data.id)!!
            val otherPort = otherNode.data.shape.ports.entries.first { it.value == other }.key
            net.graph.link(node, otherNode, port, otherPort)
            (node.data.pipe as Pipe<*, Any?>).onConnectTo(port, otherNode.data.pipe)
            (otherNode.data.pipe as Pipe<*, Any?>).onConnectTo(otherPort, node.data.pipe)
        }

        rebuildRefs(net.id)
    }

    fun createNetwork(): PipeNetworkImpl {
        markDirty()
        val net = PipeNetworkImpl(world, UUID.randomUUID(), this)
        networks[net.id] = net
        return net
    }

    fun destroyNetwork(id: UUID) {
        networks.remove(id) ?: return

        for ((k, v) in posToNetworks.entries.toSet()) {
            if (v == id) posToNetworks.remove(k)
        }

        markDirty()
        // TODO drop cells
    }

    fun rebuildRefs(network: UUID) {
        markDirty()
        posToNetworks -= posToNetworks.filterValues { it == network }.keys
        portLocationToNetwork -= portLocationToNetwork.filterValues { it == network }.keys

        networks[network]?.also { net ->
            net.rebuildRefs()
            net.graph.nodes
                    .map { it.data.pos }
                    .forEach { posToNetworks[it] = net.id }
            net.graph.nodes
                    .flatMap { it.data.shape.ports.values }
                    .forEach { portLocationToNetwork[it] = net.id }
        }
    }

    fun rebuildRefs() = networks.keys.forEach(::rebuildRefs)

    fun rebuildCachedBlocks() {
        cachedBlocks.clear()
        cachedBlocks += networks.values
                .flatMap { it.graph.nodes }
                .map { it.data.pos }
                .associateWith { world.getBlockState(it).block }
        markDirty()
    }

    fun cleanup() {
        for (net in networks.values.toSet()) {
            if (net.pipes.none()) {
                destroyNetwork(net.id)
            }
        }
    }

    fun tick() {
        networks.values.forEach(PipeNetworkImpl::tick)
    }

    override fun toTag(tag: CompoundTag): CompoundTag {
        tag.put("networks", ListTag().apply {
            for (net in networks.values) {
                add(CompoundTag().apply {
                    putUuid("id", net.id)
                    net.toTag(this)
                })
            }
        })
        return tag
    }

    override fun fromTag(tag: CompoundTag) {
        networks.clear()
        posToNetworks.clear()
        portLocationToNetwork.clear()

        networks += tag.getList("networks", NbtType.COMPOUND).let { (0 until it.size).map(it::getCompound) }
                .associate { it.getUuid("id") to PipeNetworkImpl.fromTag(world, this, it) }

        rebuildRefs()
        cleanup()
        rebuildCachedBlocks()
    }

    fun getPipeAt(pos: BlockPos): Pipe<*, *>? {
        return getNetworkAt(pos)?.getPipeAt(pos)
    }

    fun getNetworkAt(pos: BlockPos): PipeNetwork? {
        return posToNetworks[pos]?.let(::getNetworkById)
    }

    companion object {
        fun getNameForDimension(dimension: Dimension) = "pipenet${dimension.type.suffix}"
    }

}

fun ServerWorld.getPipeNetworkState(): PipeNetworkState {
    return this.persistentStateManager.getOrCreate({ PipeNetworkState(this) }, PipeNetworkState.getNameForDimension(dimension))
}