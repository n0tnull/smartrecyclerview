// The MIT License (MIT)
//
// Copyright (c) 2020
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

package com.n0tnull.smartrecyclerview.viewbinding.wrapper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.n0tnull.smartrecyclerview.viewbinding.attributes.SmartViewBindingRecyclerAttributes
import com.smartnsoft.smartrecyclerview.wrapper.SmartRecyclerViewWrapper


@Suppress("unused")
abstract class SmartViewBindingRecyclerWrapper<BusinessObjectClass, V : ViewBinding>
@JvmOverloads
constructor(
        businessObject: BusinessObjectClass,
        spanSize: Int = DEFAULT_SPAN_SIZE
) : SmartRecyclerViewWrapper<BusinessObjectClass>(businessObject, -1, spanSize) {

    override fun getNewView(parent: ViewGroup, context: Context): View {
        val newView = createViewBinding(LayoutInflater.from(context), parent).root
        newView.tag = extractNewViewAttributes(context, newView, businessObject)
        return newView
    }

    abstract fun createViewBinding(layoutInflater: LayoutInflater, parentView: ViewGroup): V

    abstract override fun extractNewViewAttributes(
            context: Context,
            view: View,
            businessObjectClass: BusinessObjectClass
    ): SmartViewBindingRecyclerAttributes<BusinessObjectClass, V>
}