package com.smartnsoft.recyclerview.widget;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * An OnItemTouchListener allows the application to intercept touch events in progress at the
 * view hierarchy level of the RecyclerView before those touch events are considered for
 * RecyclerView's own scrolling behavior.
 * <p>
 * <p>This can be useful for applications that wish to implement an OnItemClickListener callback directly in a Fragment or in an Activity.
 * OnItemTouchListeners may intercept a touch interaction already in progress even if the RecyclerView is already handling that
 * gesture stream itself for the purposes of scrolling.</p>
 *
 * @author Ludovic Roland, Anthony Msihid
 * @since 2017.11.28
 */
public final class OnItemTouchListener
    extends GestureDetector
    implements RecyclerView.OnItemTouchListener
{

  /**
   * Interface definition for a callback to be invoked when an item in this SmartRecyclerAdapter has been clicked.
   */
  public interface OnItemClickListener
  {

    /**
     * Callback method to be invoked when an item in the Adapter has been clicked.
     *
     * @param parent   The Adapter where the click happened.
     * @param view     The view within the Adapter that was clicked (this will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    void onItemClick(Adapter<?> parent, View view, int position, long id);

  }

  private OnItemClickListener itemClickListener;

  public OnItemTouchListener(Context context, OnItemClickListener itemClickListener)
  {
    super(context, new SimpleOnGestureListener()
    {
      @Override
      public boolean onSingleTapUp(MotionEvent event)
      {
        return true;
      }
    });

    this.itemClickListener = itemClickListener;
  }

  @Override
  public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent event)
  {
    final View child = recyclerView.findChildViewUnder(event.getX(), event.getY());

    if (child != null && itemClickListener != null && onTouchEvent(event))
    {
      itemClickListener.onItemClick(recyclerView.getAdapter(), child, recyclerView.getChildLayoutPosition(child), recyclerView.getChildItemId(child));
    }

    return false;
  }

  @Override
  public void onTouchEvent(RecyclerView recyclerView, MotionEvent event)
  {

  }

  @Override
  public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
  {

  }

}
