// The MIT License (MIT)
//
// Copyright (c) 2019 Smart&Soft
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

package com.smartnsoft.recyclerview.recyclerview

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.util.AttributeSet
import com.smartnsoft.recyclerview.adapter.SmartRecyclerAdapter

/**
 * A [androidx.recyclerview.widget.RecyclerView] that provides reordering with drag&amp;drop.
 * The Adapter has to be of type [SmartRecyclerAdapter]. Furthermore you have to provide stable ids
 * [androidx.recyclerview.widget.RecyclerView.Adapter.setHasStableIds]
 *
 * @author Adrien Vitti
 * @since 2019.07.15
 */
class SmartReorderRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr)
{

  val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
      ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
      0
  )
  {

    override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder,
                        target: ViewHolder): Boolean
    {
      (recyclerView.adapter as? SmartRecyclerAdapter)?.moveWrapper(viewHolder.adapterPosition, target.adapterPosition)

      return true
    }

    override fun onSwiped(viewHolder: ViewHolder, direction: Int)
    {
      // Callback when swiping horizontally or vertically, we won't do anything here
    }

    override fun onSelectedChanged(viewHolder: ViewHolder?,
                                   actionState: Int)
    {
      super.onSelectedChanged(viewHolder, actionState)

      viewHolder?.itemView?.takeIf { _ ->
        actionState == ItemTouchHelper.ACTION_STATE_DRAG
      }?.apply { alpha = 0.5f }
    }

    override fun clearView(recyclerView: RecyclerView,
                           viewHolder: ViewHolder)
    {
      super.clearView(recyclerView, viewHolder)

      viewHolder.itemView.alpha = 1.0f
    }
  })

  init
  {
    itemTouchHelper.attachToRecyclerView(this)
  }

  override fun setAdapter(adapter: Adapter<*>?)
  {
    adapter?.takeUnless { recyclerAdapter ->
      recyclerAdapter is SmartRecyclerAdapter && recyclerAdapter.hasStableIds()
    }?.also { _ ->
      throw IllegalStateException("SmartReorderRecyclerView only works with SmartRecyclerAdapter and must have stable ids!")
    }

    super.setAdapter(adapter)
  }

  /**
   * Start to drag a specific ViewHolder in the RecyclerView for reordering purpose
   *
   * @param viewHolder The viewHolder which is being reordered
   */
  @Suppress("unused")
  fun startReordering(viewHolder: ViewHolder)
  {
    itemTouchHelper.startDrag(viewHolder)
  }
}