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

package network.rs485.logisticspipes.transport

import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

/**
 * Describes the path of a cell in a pipe. Provided by the pipe itself when a cell enters it to make the cell's path match its shape.
 */
interface CellPath {

    /**
     * Returns the cell's position based on progress (in range 0..1) relative to the center of the pipe
     */
    fun getItemPosition(progress: Float): Vec3d

    /**
     * Returns the length of this path (the distance the cell travels between progress=0 and progress=1).
     */
    fun getLength(): Float

}

interface LinearCellPath : CellPath {

    @JvmDefault
    override fun getLength(): Float {
        return getItemPosition(1f).distanceTo(getItemPosition(0f)).toFloat()
    }

}

class StandardPipeCellPath(val side: Direction, val inwards: Boolean) : LinearCellPath {

    override fun getItemPosition(progress: Float): Vec3d {
        val actualProgress = if (inwards) 1 - progress else progress
        return Vec3d(side.vector).multiply(progress.toDouble() * 0.5)
    }

}

class SCurvePath() : CellPath {

    override fun getItemPosition(progress: Float): Vec3d {
        TODO("what the fuck do I know")
    }

    override fun getLength(): Float {
        TODO("not implemented")
    }

}