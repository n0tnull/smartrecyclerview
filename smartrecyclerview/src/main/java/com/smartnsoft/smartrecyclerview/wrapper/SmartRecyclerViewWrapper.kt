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
package com.smartnsoft.smartrecyclerview.wrapper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.smartnsoft.smartrecyclerview.attributes.SmartRecyclerAttributes

/**
 * Wraps a business object and its underlying Android [View] in a [androidx.recyclerview.widget.RecyclerView].
 *
 * @param <BusinessObjectClass> the business object class which is represented by the current wrapper
 * @author Jocelyn Girard, Ludovic Roland
 * @since 2014.04.16
 */
abstract class SmartRecyclerViewWrapper<BusinessObjectClass>
@JvmOverloads
constructor(
        var businessObject: BusinessObjectClass,
        @LayoutRes protected var layoutResourceId: Int,
        val spanSize: Int = DEFAULT_SPAN_SIZE
) {

    fun getType(): Int =
            this.javaClass.name.hashCode()

    open fun getNewView(parent: ViewGroup, context: Context): View {
        val view = LayoutInflater.from(context).inflate(layoutResourceId, parent, false)
        view.tag = extractNewViewAttributes(context, view, businessObject)
        return view
    }

    fun getViewAttributes(view: View): SmartRecyclerAttributes<BusinessObjectClass> {
        return view.tag as SmartRecyclerAttributes<BusinessObjectClass>
    }

    open fun getId(): Long = getId(businessObject)

    protected open fun getId(businessObject: BusinessObjectClass): Long =
            businessObject?.hashCode()?.toLong() ?: 0L

    protected abstract fun extractNewViewAttributes(
            context: Context,
            view: View,
            businessObjectClass: BusinessObjectClass
    ): SmartRecyclerAttributes<BusinessObjectClass>

    companion object {
        const val DEFAULT_SPAN_SIZE = 1
    }

}