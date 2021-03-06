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

package network.rs485.logisticspipes.pipe

import net.minecraft.util.math.Direction
import network.rs485.logisticspipes.block.PipeBlockInterface
import network.rs485.logisticspipes.transport.Cell
import network.rs485.logisticspipes.transport.Pipe
import network.rs485.logisticspipes.transport.PipeNetwork
import network.rs485.logisticspipes.transport.StandardPipe

// Unrouted pipes (and routed pipes) have 12 different paths in them that items can go
// (6 sides, either from center -> edge or edge -> center), as opposed to highspeed tubes, which only have 2 possible paths
// (either "forwards" or "backwards"), since those don't have any intersections that items can branch off of.

class DummyPipe(itf: PipeBlockInterface) : StandardPipe(itf) {

    override fun routeCell(network: PipeNetwork, from: Direction, cell: Cell<*>): Direction? {
        // Take a random side out of the sides that the cell does not come from (so that it doesn't go backwards), and send it in that direction.
        val possibleSides = Direction.values().filter { network.isPortConnected(this, it) } - from
        val outputSide = if (possibleSides.isNotEmpty()) possibleSides[network.random.nextInt(possibleSides.size)] else null
        return outputSide
    }

    override fun onConnectTo(port: Direction, other: Pipe<*, *>) {
        super.onConnectTo(port, other)
        itf.setConnection(port, true)
    }

    override fun onDisconnect(port: Direction, other: Pipe<*, *>) {
        super.onDisconnect(port, other)
        itf.setConnection(port, false)
    }

}