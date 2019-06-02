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

@file:JvmName("CoordinateUtils")

package network.rs485.logisticspipes.world

import net.minecraft.util.EnumFacing

fun IntegerCoordinates.add(toAdd: IntegerCoordinates): IntegerCoordinates {
    xCoord = xCoord + toAdd.xCoord
    yCoord = yCoord + toAdd.yCoord
    zCoord = zCoord + toAdd.zCoord
    return this
}

fun sum(first: DoubleCoordinates, second: DoubleCoordinates): DoubleCoordinates {
    return DoubleCoordinates(first.xCoord + second.xCoord, first.yCoord + second.yCoord, first.zCoord + second.zCoord)
}

fun sum(first: IntegerCoordinates, second: IntegerCoordinates): IntegerCoordinates {
    val ret = IntegerCoordinates()
    ret.xCoord = first.xCoord + second.xCoord
    ret.yCoord = first.yCoord + second.yCoord
    ret.zCoord = first.zCoord + second.zCoord
    return ret
}

fun add(coords: DoubleCoordinates, direction: EnumFacing): DoubleCoordinates {
    return add(coords, direction, 1.0)
}

fun add(coords: IntegerCoordinates, direction: EnumFacing): IntegerCoordinates {
    return add(coords, direction, 1)
}

fun add(coords: DoubleCoordinates, direction: EnumFacing, times: Double): DoubleCoordinates {
    coords.xCoord = coords.xCoord + direction.directionVec.x * times
    coords.yCoord = coords.yCoord + direction.directionVec.y * times
    coords.zCoord = coords.zCoord + direction.directionVec.z * times
    return coords
}

fun add(coords: IntegerCoordinates, direction: EnumFacing, times: Int): IntegerCoordinates {
    coords.xCoord = coords.xCoord + direction.directionVec.x * times
    coords.yCoord = coords.yCoord + direction.directionVec.y * times
    coords.zCoord = coords.zCoord + direction.directionVec.z * times
    return coords
}

fun sum(coords: DoubleCoordinates, direction: EnumFacing): DoubleCoordinates {
    return sum(coords, direction, 1.0)
}

fun sum(coords: IntegerCoordinates, direction: EnumFacing): IntegerCoordinates {
    return sum(coords, direction, 1)
}

fun sum(coords: DoubleCoordinates, direction: EnumFacing, times: Double): DoubleCoordinates {
    val ret = DoubleCoordinates(coords)
    return add(ret, direction, times)
}

fun sum(coords: IntegerCoordinates, direction: EnumFacing, times: Int): IntegerCoordinates {
    val ret = IntegerCoordinates(coords)
    return add(ret, direction, times)
}

fun getDirectionFromTo(source: DoubleCoordinates, target: DoubleCoordinates): EnumFacing? {
    val xDiff = target.xCoord - source.xCoord
    val yDiff = target.yCoord - source.yCoord
    val zDiff = target.zCoord - source.zCoord

    return if (xDiff != 0.0 && yDiff == 0.0 && zDiff == 0.0) {
        if (xDiff > 0) {
            EnumFacing.EAST
        } else {
            EnumFacing.WEST
        }
    } else if (xDiff == 0.0 && yDiff != 0.0 && zDiff == 0.0) {
        if (yDiff > 0) {
            EnumFacing.UP
        } else {
            EnumFacing.DOWN
        }
    } else if (xDiff == 0.0 && yDiff == 0.0 && zDiff != 0.0) {
        if (zDiff > 0) {
            EnumFacing.SOUTH
        } else {
            EnumFacing.NORTH
        }
    } else {
        null
    }
}