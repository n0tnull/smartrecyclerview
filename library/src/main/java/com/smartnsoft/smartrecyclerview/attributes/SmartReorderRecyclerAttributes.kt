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

package com.smartnsoft.smartrecyclerview.attributes

import android.view.MotionEvent
import android.view.View
import com.smartnsoft.smartrecyclerview.recyclerview.SmartReorderRecyclerView

/**
 * This class is responsible for creating a new [View], which is able to represent the provided business object
 * and can also be reordered using a view as a trigger.
 * It is similar to a simple [SmartRecyclerAttributes] if the RecyclerView is not a [SmartReorderRecyclerView]
 *
 * @author Adrien Vitti
 * @since 2019.07.15
 */
abstract class SmartReorderRecyclerAttributes<BusinessObjectType>(view: View)
  : SmartRecyclerAttributes<BusinessObjectType>(view)
{

  /**
   * Specify which view can trigger the reordering when touching it.
   *
   * @return the view which will be used by the touch listener to move the view in the list
   */
  abstract fun getTriggerViewForReorder(): View?

  private var draggingTouchListener: View.OnTouchListener? = null

  override fun update(businessObject: BusinessObjectType, isSelected: Boolean)
  {
    val triggerViewForReorder = getTriggerViewForReorder()
    if (draggingTouchListener == null)
    {
      triggerViewForReorder?.also { triggerView ->
        draggingTouchListener = View.OnTouchListener { _, event ->
          if (event.actionMasked == MotionEvent.ACTION_DOWN)
          {
            (itemView.parent as? SmartReorderRecyclerView)?.itemTouchHelper?.startDrag(this)
          }
          return@OnTouchListener true
        }
        triggerView.setOnTouchListener(draggingTouchListener)
      }
    }

    super.update(businessObject, isSelected)
  }

}