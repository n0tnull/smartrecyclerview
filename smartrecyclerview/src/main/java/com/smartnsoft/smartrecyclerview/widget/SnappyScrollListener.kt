package com.smartnsoft.smartrecyclerview.widget

import android.app.Activity
import android.graphics.Point
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.abs

@Suppress("unused")
class SnappyScrollListener(activity: Activity) : RecyclerView.OnScrollListener() {

    val autoSet = AtomicBoolean(true)
    val screenCenterX: Int

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (autoSet.get().not() && newState == RecyclerView.SCROLL_STATE_IDLE) {
            (recyclerView.layoutManager as LinearLayoutManager)?.also { layoutManager ->
                findCenterView(layoutManager)?.also { view ->
                    val scrollXNeeded = screenCenterX - (view.left + view.right) / 2
                    recyclerView.smoothScrollBy(scrollXNeeded * if (view.right < screenCenterX) 1 else -1, 0)
                }
            }
            autoSet.set(true)
        }
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
            autoSet.set(false)
        }
    }

    private fun findCenterView(layoutManager: LinearLayoutManager): View? {
        var minDistance = 0
        var centeredView: View? = null
        for (itemPosition in layoutManager.findFirstVisibleItemPosition()..layoutManager.findLastVisibleItemPosition()) {
            val view = layoutManager.findViewByPosition(itemPosition) ?: break
            val leastDiff = abs(screenCenterX - (view.left + view.right) / 2)
            if (leastDiff <= minDistance || itemPosition == layoutManager.findFirstVisibleItemPosition()) {
                minDistance = leastDiff
                centeredView = view
            } else {
                break
            }
        }
        return centeredView
    }

    init {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenCenterX = size.x / 2
    }
}