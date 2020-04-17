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


/**
 * Wrappers use in the [com.smartnsoft.smartrecyclerview.adapter.DiffUtilSmartRecyclerAdapter] class have to implement this interface
 *
 * @author Ludovic Roland
 * @since 2017.09.27
 */
interface SmartDiffUtil {
    /**
     * Calls into the [com.smartnsoft.smartrecyclerview.adapter.DiffUtilSmartRecyclerAdapter.SmartDiffUtilCallback.areContentsTheSame]
     * methods to to check whether two items have the same data.
     *
     * @return the hashcode of the item
     * @see androidx.recyclerview.widget.DiffUtil.Callback.areContentsTheSame
     */
    val diffUtilHashCode: Long
}