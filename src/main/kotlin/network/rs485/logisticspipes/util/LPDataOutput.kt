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

package network.rs485.logisticspipes.util

import io.netty.buffer.ByteBuf
import logisticspipes.network.IWriteListObject
import logisticspipes.routing.channels.ChannelInformation
import logisticspipes.utils.PlayerIdentifier
import logisticspipes.utils.item.ItemIdentifier
import logisticspipes.utils.item.ItemIdentifierStack
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import java.util.*

interface LPDataOutput {

    /**
     * @see java.io.DataOutput.writeByte
     */
    fun writeByte(b: Int)

    fun writeByte(b: Byte)

    /**
     * @see java.io.DataOutput.writeShort
     */
    fun writeShort(s: Int)

    fun writeShort(s: Short)

    /**
     * @see java.io.DataOutput.writeInt
     */
    fun writeInt(i: Int)

    /**
     * @see java.io.DataOutput.writeLong
     */
    fun writeLong(l: Long)

    /**
     * @see java.io.DataOutput.writeFloat
     */
    fun writeFloat(f: Float)

    /**
     * @see java.io.DataOutput.writeDouble
     */
    fun writeDouble(d: Double)

    /**
     * @see java.io.DataOutput.writeBoolean
     */
    fun writeBoolean(b: Boolean)

    /**
     * Uses UTF-8 and not UTF-16.
     *
     * @see java.io.DataOutput.writeUTF
     */
    fun writeUTF(s: String?)

    fun writeByteArray(arr: ByteArray?)

    fun writeByteBuf(buffer: ByteBuf)

    fun writeIntArray(arr: IntArray?)

    fun writeLongArray(arr: LongArray?)

    fun writeBooleanArray(arr: BooleanArray?)

    fun writeUTFArray(arr: Array<String>?)

    fun writeFacing(direction: EnumFacing?)

    fun writeResourceLocation(resource: ResourceLocation?)

    fun <T : Enum<T>> writeEnumSet(types: EnumSet<T>, clazz: Class<T>)

    fun writeBitSet(bits: BitSet)

    fun writeNBTTagCompound(tag: NBTTagCompound?)

    fun writeItemStack(itemstack: ItemStack)

    fun writeItemIdentifier(item: ItemIdentifier?)

    fun writeItemIdentifierStack(stack: ItemIdentifierStack?)

    fun <T> writeCollection(collection: Collection<T>?, handler: IWriteListObject<T>)

    @JvmDefault
    fun <T : LPFinalSerializable> writeCollection(collection: Collection<T>) {
        writeCollection(collection, IWriteListObject { obj, finalSerializable -> obj.writeSerializable(finalSerializable) })
    }

    fun <T : Enum<T>> writeEnum(obj: T)

    fun writeBytes(arr: ByteArray)

    fun writeChannelInformation(channel: ChannelInformation)

    fun writeUUID(uuid: UUID?)

    fun writePlayerIdentifier(playerIdentifier: PlayerIdentifier)

    @JvmDefault
    fun writeSerializable(finalSerializable: LPFinalSerializable) {
        finalSerializable.write(this)
    }

    interface LPDataOutputConsumer {
        fun accept(dataOutput: LPDataOutput)
    }

}
