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
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.smartnsoft.smartrecyclerview.attributes.SmartRecyclerAttributes
import com.smartnsoft.smartrecyclerview.attributes.SmartRecyclerDatabindingAttributes

/**
 * A [SmartRecyclerViewWrapper] that allows the Databinding.
 *
 * @param <BusinessObjectClass> the business object class which is represented by the current wrapper
 * @author Ludovic Roland
 * @see SmartRecyclerViewWrapper
 *
 * @since 2018.07.04
 */
@Suppress("unused")
abstract class SmartRecyclerDatabindingViewWrapper<BusinessObjectClass>
@JvmOverloads
constructor(
        businessObject: BusinessObjectClass,
        @LayoutRes layoutResourceId: Int,
        spanSize: Int = DEFAULT_SPAN_SIZE
) : SmartRecyclerViewWrapper<BusinessObjectClass>(businessObject, layoutResourceId, spanSize) {

    override fun getNewView(parent: ViewGroup, context: Context): View {
        val layoutInflater = LayoutInflater.from(context)
        val viewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, layoutResourceId, parent, false)
        viewDataBinding.root.tag = extractNewViewAttributes(context, viewDataBinding, businessObject)
        return viewDataBinding.root
    }

    override fun extractNewViewAttributes(context: Context, view: View, businessObjectClass: BusinessObjectClass): SmartRecyclerAttributes<BusinessObjectClass> {
        throw UnsupportedOperationException("Please use the extractNewViewAttributes(Context, ViewDataBinding, BusinessObjectClass) method")
    }

    protected abstract fun extractNewViewAttributes(
            context: Context?,
            viewDataBinding: ViewDataBinding?,
            businessObjectClass: BusinessObjectClass
    ): SmartRecyclerDatabindingAttributes<*>?
}