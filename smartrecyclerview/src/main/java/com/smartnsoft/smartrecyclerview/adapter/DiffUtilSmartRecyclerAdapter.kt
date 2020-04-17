// The MIT License (MIT)
//
// Copyright (c) 2017
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
package com.smartnsoft.smartrecyclerview.adapter

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import com.smartnsoft.smartrecyclerview.attributes.SmartRecyclerAttributes
import com.smartnsoft.smartrecyclerview.wrapper.SmartDiffUtil
import com.smartnsoft.smartrecyclerview.wrapper.SmartRecyclerViewWrapper

/**
 * A [SmartRecyclerAdapter] adapter, which works closely with the
 * [com.smartnsoft.smartrecyclerview.wrapper.DiffUtilSmartRecyclerViewWrapper] class.
 *
 * @author Ludovic Roland
 * @see SmartRecyclerAdapter
 *
 * @since 2017.09.27
 */
@Suppress("MemberVisibilityCanBePrivate")
abstract class DiffUtilSmartRecyclerAdapter(context: Context) : SmartRecyclerAdapter(context) {

    abstract class SmartDiffUtilCallback(
            protected var oldWrappers: List<SmartRecyclerViewWrapper<*>>?,
            protected var newWrappers: List<SmartRecyclerViewWrapper<*>>?)
        : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldWrappers?.size ?: 0

        override fun getNewListSize(): Int = newWrappers?.size ?: 0

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldSmartRecyclerViewWrapper = oldWrappers?.getOrNull(oldItemPosition)
            val newSmartRecyclerViewWrapper = newWrappers?.getOrNull(newItemPosition)
            return oldSmartRecyclerViewWrapper?.getId() == newSmartRecyclerViewWrapper?.getId()
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldSmartRecyclerViewWrapper = oldWrappers?.getOrNull(oldItemPosition)
            val newSmartRecyclerViewWrapper = newWrappers?.getOrNull(newItemPosition)

            require((oldSmartRecyclerViewWrapper is SmartDiffUtil && newSmartRecyclerViewWrapper is SmartDiffUtil)) {
                "Wrappers have to implement the IDiffUtil interface"
            }
            return (oldSmartRecyclerViewWrapper as SmartDiffUtil).diffUtilHashCode == (newSmartRecyclerViewWrapper as SmartDiffUtil).diffUtilHashCode
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldBusinessObject = oldWrappers?.getOrNull(oldItemPosition)?.businessObject
            val newBusinessObject = newWrappers?.getOrNull(newItemPosition)?.businessObject
            val result = getChangePayloadCustom(oldItemPosition, newItemPosition, oldBusinessObject, newBusinessObject)
            return result ?: ITEM_CHANGED_PAYLOAD
        }

        /**
         * When [SmartDiffUtilCallback.areItemsTheSame] returns true for two items and
         * [SmartDiffUtilCallback.areContentsTheSame]returns false for them,
         * the [SmartDiffUtilCallback.getChangePayload] is called which which call this method to get a payload about the change.
         *
         * @param oldItemPosition   The position of the item in the old list
         * @param newItemPosition   The position of the item in the new list
         * @param oldBusinessObject the old item
         * @param newBusinessObject the new item
         * @return `null` if and only if the method has NOT handled the payload so the
         * [SmartDiffUtilCallback.getChangePayload] returns [SmartDiffUtilCallback.ITEM_CHANGED_PAYLOAD] in order to refresh the whole item ;
         * otherwise a non null value in order to handled the payload
         */
        protected abstract fun getChangePayloadCustom(
                oldItemPosition: Int,
                newItemPosition: Int,
                oldBusinessObject: Any?,
                newBusinessObject: Any?
        ): Any?

        companion object {
            const val ITEM_CHANGED_PAYLOAD = 1
        }

    }

    override fun setNewWrapperList(wrappers: List<SmartRecyclerViewWrapper<*>>) {
        throw UnsupportedOperationException("Use the setWrappersForDiffUtil() method instead")
    }

    override fun onBindViewHolder(holder: SmartRecyclerAttributes<*>, position: Int, payloads: List<Any>) {
        if (payloads.isNotEmpty()) {
            for (payload in payloads) {
                if (onBindViewHolderCustom(holder, position, payload).not()) {
                    onBindViewHolder(holder, position)
                }
            }
        } else {
            onBindViewHolder(holder, position)
        }
    }

    /**
     * [DiffUtilSmartRecyclerAdapter.onBindViewHolder] calls this method when the `payloads` list is not empty.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     * @param payload  a non-null payload
     * @return `true` if the payload has been handled locally or `false` if the holder requires full update
     */
    abstract fun onBindViewHolderCustom(holder: SmartRecyclerAttributes<*>?, position: Int, payload: Any?): Boolean

    /**
     * Returns the [DiffUtil.Callback] that should be used by the [DiffUtilSmartRecyclerAdapter]
     *
     * @param oldWrappers the old wrappers
     * @param newWrappers the new wrappers
     * @return a class that extends [SmartDiffUtilCallback]
     */
    abstract fun <T : SmartDiffUtilCallback> getDiffUtilCallback(
            oldWrappers: List<SmartRecyclerViewWrapper<*>>, newWrappers: List<SmartRecyclerViewWrapper<*>>): T

    /**
     * Replaces the [SmartRecyclerAdapter.setNewWrapperList] methods in order to the manage the diff util algorithm
     *
     * @param newWrappers the new wrappers
     */
    fun setWrappersForDiffUtil(newWrappers: List<SmartRecyclerViewWrapper<*>>) {
        val diffCallback = getDiffUtilCallback<SmartDiffUtilCallback>(wrappers, newWrappers)
        val diffResult = DiffUtil.calculateDiff(diffCallback, isDetectMoves)
        super.setNewWrapperList(newWrappers)
        diffResult.dispatchUpdatesTo(this)
    }

    protected val isDetectMoves: Boolean
        get() = true
}