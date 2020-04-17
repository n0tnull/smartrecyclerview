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
import com.smartnsoft.smartrecyclerview.attributes.SmartRecyclerAttributes
import java.util.*

/**
 * @author Raphael Kiffer
 * @since 2016.01.12
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class SmartSelectorRecyclerAdapter
@JvmOverloads constructor(
        context: Context,
        private val isMultipleSelectionEnabled: Boolean = false
) : SmartRecyclerAdapter(context) {

    private val selectableMap: MutableMap<Long, Boolean?> = HashMap()

    override fun onBindViewHolder(smartRecyclerAttributes: SmartRecyclerAttributes<*>, position: Int) {
        if (isMultipleSelectionEnabled.not()) {
            super.onBindViewHolder(smartRecyclerAttributes, position)
        } else {
            val wrapper = wrappers[position]
            wrapper.businessObject?.also { businessObject ->
                val isSelected = selectableMap[wrapper.getId()] ?: false
                smartRecyclerAttributes.uncheckedUpdate(businessObject, isSelected)
            }
        }
    }

    fun toggleItemSelectedState(businessObjectId: Long) {
        val isSelected = selectableMap[businessObjectId]
        if (isSelected != null) {
            setItemSelectedState(businessObjectId, isSelected.not())
        } else {
            setItemSelectedState(businessObjectId, true)
        }
    }

    fun setItemSelectedState(businessObjectId: Long, isSelected: Boolean) {
        selectableMap[businessObjectId] = isSelected
        notifyItemChanged(getItemPosition(businessObjectId))
    }

}