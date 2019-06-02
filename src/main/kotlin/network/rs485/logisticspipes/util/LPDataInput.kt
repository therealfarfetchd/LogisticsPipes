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
import logisticspipes.network.IReadListObject
import logisticspipes.routing.channels.ChannelInformation
import logisticspipes.utils.PlayerIdentifier
import logisticspipes.utils.item.ItemIdentifier
import logisticspipes.utils.item.ItemIdentifierStack
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import java.util.*

interface LPDataInput {

    fun readByteArray(): ByteArray?

    /**
     * @see java.io.DataInput.readByte
     */
    fun readByte(): Byte

    /**
     * @see java.io.DataInput.readShort
     */
    fun readShort(): Short

    /**
     * @see java.io.DataInput.readInt
     */
    fun readInt(): Int

    /**
     * @see java.io.DataInput.readLong
     */
    fun readLong(): Long

    /**
     * @see java.io.DataInput.readFloat
     */
    fun readFloat(): Float

    /**
     * @see java.io.DataInput.readDouble
     */
    fun readDouble(): Double

    /**
     * @see java.io.DataInput.readBoolean
     */
    fun readBoolean(): Boolean

    /**
     * @see java.io.DataInput.readUTF
     */
    fun readUTF(): String?

    fun readFacing(): EnumFacing?

    fun readResourceLocation(): ResourceLocation?

    fun <T : Enum<T>> readEnumSet(clazz: Class<T>): EnumSet<T>

    fun readBitSet(): BitSet

    fun readNBTTagCompound(): NBTTagCompound?

    fun readBooleanArray(): BooleanArray?

    fun readIntArray(): IntArray?

    fun readBytes(length: Int): ByteArray

    fun readItemIdentifier(): ItemIdentifier?

    fun readItemIdentifierStack(): ItemIdentifierStack?

    fun readItemStack(): ItemStack

    fun <T> readArrayList(reader: IReadListObject<T>): ArrayList<T>?

    fun <T> readLinkedList(reader: IReadListObject<T>): LinkedList<T>?

    fun <T> readSet(handler: IReadListObject<T>): Set<T>?

    fun <T : Enum<T>> readEnum(clazz: Class<T>): T?

    fun readByteBuf(): ByteBuf

    fun readLongArray(): LongArray?

    fun readChannelInformation(): ChannelInformation

    fun readUUID(): UUID?

    fun readPlayerIdentifier(): PlayerIdentifier

    // fun readSerializable(serializableClass: Class<out LPSerializable>): LPSerializable

    @JvmDefault
    fun readSerializable(serializable: LPSerializable) {
        serializable.read(this)
    }

    interface LPDataInputConsumer {
        fun accept(dataInput: LPDataInput)
    }

}
