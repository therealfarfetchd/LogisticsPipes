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

import alexiil.mc.lib.attributes.fluid.volume.FluidKeys
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.World
import network.rs485.logisticspipes.init.CellContentTypes
import network.rs485.logisticspipes.item.FluidContainerItem

class FluidCellContent @JvmOverloads constructor(var fluid: FluidVolume = FluidKeys.EMPTY.withAmount(0)) : CellContent {

    override fun fromTag(tag: CompoundTag) {
        fluid = FluidVolume.fromTag(tag)
    }

    override fun toTag(tag: CompoundTag): CompoundTag {
        return fluid.toTag(tag)
    }

    override fun getDisplayStack(): ItemStack {
        return FluidContainerItem.makeStack(fluid)
    }

    override fun createEntity(world: World): Entity? {
        return null
    }

    override fun getType(): CellContentType<*> {
        return CellContentTypes.Fluid
    }

}