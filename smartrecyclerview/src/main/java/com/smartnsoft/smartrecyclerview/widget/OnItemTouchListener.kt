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
package com.smartnsoft.smartrecyclerview.widget

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * An OnItemTouchListener allows the application to intercept touch events in progress at the
 * view hierarchy level of the RecyclerView before those touch events are considered for
 * RecyclerView's own scrolling behavior.
 *
 *
 *
 * This can be useful for applications that wish to implement an OnItemClickListener callback directly in a Fragment or in an Activity.
 * OnItemTouchListeners may intercept a touch interaction already in progress even if the RecyclerView is already handling that
 * gesture stream itself for the purposes of scrolling.
 *
 * @author Ludovic Roland, Anthony Msihid
 * @since 2017.11.28
 */
@Suppress("unused")
class OnItemTouchListener(context: Context, private val itemClickListener: OnItemClickListener?)
    : GestureDetector(context, object : SimpleOnGestureListener() {
    override fun onSingleTapUp(event: MotionEvent): Boolean = true
}), RecyclerView.OnItemTouchListener {
    /**
     * Interface definition for a callback to be invoked when an item in this SmartRecyclerAdapter has been clicked.
     */
    interface OnItemClickListener {
        /**
         * Callback method to be invoked when an item in the Adapter has been clicked.
         *
         * @param parent   The Adapter where the click happened.
         * @param view     The view within the Adapter that was clicked (this will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         * @param id       The row id of the item that was clicked.
         */
        fun onItemClick(parent: RecyclerView.Adapter<*>, view: View, position: Int, id: Long)
    }

    override fun onInterceptTouchEvent(recyclerView: RecyclerView, event: MotionEvent): Boolean {
        recyclerView.findChildViewUnder(event.x, event.y)?.also { child ->
            val adapter = recyclerView.adapter
            if (onTouchEvent(event) && adapter != null) {
                itemClickListener?.onItemClick(adapter, child, recyclerView.getChildLayoutPosition(child), recyclerView.getChildItemId(child))
            }
        }
        return false
    }

    override fun onTouchEvent(recyclerView: RecyclerView, event: MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

}