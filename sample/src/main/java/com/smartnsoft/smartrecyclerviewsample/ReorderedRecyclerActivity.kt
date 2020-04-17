package com.smartnsoft.smartrecyclerviewsample

import com.smartnsoft.smartrecyclerview.adapter.SmartRecyclerAdapter

/**
 * An activity containing a simple way to use a ReorderRecyclerView
 *
 * @author Adrien Vitti
 * @since 2019.07.15
 */
class ReorderedRecyclerActivity : AbstractRecyclerActivity()
{

  override fun getLayoutId(): Int
  {
    return R.layout.reordered_recycler_activity
  }

  override fun createRecyclerAdapter(): SmartRecyclerAdapter
  {
    return SmartRecyclerAdapter(this, true).apply { setHasStableIds(true) }
  }

  override fun shouldActivateTextReordering(): Boolean
  {
    return true
  }

}