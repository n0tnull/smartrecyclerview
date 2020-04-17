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

package com.smartnsoft.smartrecyclerview.adapter;

import java.util.List;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.smartnsoft.smartrecyclerview.attributes.SmartRecyclerAttributes;
import com.smartnsoft.smartrecyclerview.wrapper.DiffUtilSmartRecyclerViewWrapper;
import com.smartnsoft.smartrecyclerview.wrapper.DiffUtilSmartSpanRecyclerViewWrapper;
import com.smartnsoft.smartrecyclerview.wrapper.SmartDiffUtil;
import com.smartnsoft.smartrecyclerview.wrapper.SmartRecyclerViewWrapper;

/**
 * A {@link SmartRecyclerAdapter} adapter, which works closely with the {@link DiffUtilSmartRecyclerViewWrapper} and the {@link DiffUtilSmartSpanRecyclerViewWrapper} classes.
 *
 * @author Ludovic Roland
 * @see SmartRecyclerAdapter
 * @since 2017.09.27
 */
public abstract class DiffUtilSmartRecyclerAdapter
    extends SmartRecyclerAdapter
{

  public abstract static class SmartDiffUtilCallback
      extends DiffUtil.Callback
  {

    public static final int ITEM_CHANGED_PAYLOAD = 1;

    protected List<? extends SmartRecyclerViewWrapper<?>> oldWrappers;

    protected List<? extends SmartRecyclerViewWrapper<?>> newWrappers;

    public SmartDiffUtilCallback(List<? extends SmartRecyclerViewWrapper<?>> oldWrappers,
        List<? extends SmartRecyclerViewWrapper<?>> newWrappers)
    {
      this.oldWrappers = oldWrappers;
      this.newWrappers = newWrappers;
    }

    @Override
    public int getOldListSize()
    {
      return oldWrappers == null ? 0 : oldWrappers.size();
    }

    @Override
    public int getNewListSize()
    {
      return newWrappers == null ? 0 : newWrappers.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition)
    {
      final SmartRecyclerViewWrapper<?> oldSmartRecyclerViewWrapper = oldWrappers.get(oldItemPosition);
      final SmartRecyclerViewWrapper<?> newSmartRecyclerViewWrapper = newWrappers.get(newItemPosition);

      return oldSmartRecyclerViewWrapper.getId() == newSmartRecyclerViewWrapper.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition)
    {
      final SmartRecyclerViewWrapper<?> oldSmartRecyclerViewWrapper = oldWrappers.get(oldItemPosition);
      final SmartRecyclerViewWrapper<?> newSmartRecyclerViewWrapper = newWrappers.get(newItemPosition);

      if ((oldSmartRecyclerViewWrapper instanceof SmartDiffUtil) == false || (newSmartRecyclerViewWrapper instanceof SmartDiffUtil) == false)
      {
        throw new IllegalArgumentException("Wrappers have to implement the IDiffUtil interface");
      }

      return ((SmartDiffUtil) oldSmartRecyclerViewWrapper).getDiffUtilHashCode() == ((SmartDiffUtil) newSmartRecyclerViewWrapper).getDiffUtilHashCode();
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition)
    {
      final Object oldBusinessObject = oldWrappers.get(oldItemPosition).getBusinessObject();
      final Object newBusinessObject = newWrappers.get(newItemPosition).getBusinessObject();

      final Object result = getChangePayloadCustom(oldItemPosition, newItemPosition, oldBusinessObject, newBusinessObject);

      return result == null ? SmartDiffUtilCallback.ITEM_CHANGED_PAYLOAD : result;
    }

    /**
     * When {@link SmartDiffUtilCallback#areItemsTheSame(int, int)} returns true for two items and {@link SmartDiffUtilCallback#areContentsTheSame(int, int)}returns false for them,
     * the {@link SmartDiffUtilCallback#getChangePayload(int, int)} is called which which call this method to get a payload about the change.
     *
     * @param oldItemPosition   The position of the item in the old list
     * @param newItemPosition   The position of the item in the new list
     * @param oldBusinessObject the old item
     * @param newBusinessObject the new item
     * @return {@code null} if and only if the method has NOT handled the payload so the {@link SmartDiffUtilCallback#getChangePayload(int, int)} returns {@link SmartDiffUtilCallback#ITEM_CHANGED_PAYLOAD} in order to refresh the whole item ;
     * otherwise a non null value in order to handled the payload
     */
    protected abstract Object getChangePayloadCustom(int oldItemPosition, int newItemPosition, Object oldBusinessObject,
        Object newBusinessObject);

  }

  public DiffUtilSmartRecyclerAdapter(Context context)
  {
    super(context);
  }

  @Override
  public void setWrappers(List<? extends SmartRecyclerViewWrapper<?>> newWrappers)
  {
    throw new UnsupportedOperationException("Use the setWrappersForDiffUtil() method instead");
  }

  @Override
  public void onBindViewHolder(SmartRecyclerAttributes holder, int position, List<Object> payloads)
  {
    if (payloads.isEmpty() == false)
    {
      for (final Object payload : payloads)
      {
        if (onBindViewHolderCustom(holder, position, payload) == false)
        {
          onBindViewHolder(holder, position);
        }
      }
    }
    else
    {
      onBindViewHolder(holder, position);
    }
  }

  /**
   * {@link DiffUtilSmartRecyclerAdapter#onBindViewHolder(SmartRecyclerAttributes, int, List)} calls this method when the {@code payloads} list is not empty.
   *
   * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
   * @param position The position of the item within the adapter's data set.
   * @param payload  a non-null payload
   * @return {@code true} if the payload has been handled locally or {@code false} if the holder requires full update
   */
  public abstract boolean onBindViewHolderCustom(SmartRecyclerAttributes holder, int position, Object payload);

  /**
   * Returns the {@link DiffUtil.Callback} that should be used by the {@link DiffUtilSmartRecyclerAdapter}
   *
   * @param oldWrappers the old wrappers
   * @param newWrappers the new wrappers
   * @return a class that extends {@link SmartDiffUtilCallback}
   */
  public abstract <T extends SmartDiffUtilCallback> T getDiffUtilCallback(
      List<? extends SmartRecyclerViewWrapper<?>> oldWrappers, List<? extends SmartRecyclerViewWrapper<?>> newWrappers);

  /**
   * Replaces the {@link SmartRecyclerAdapter#setWrappers(List)} methods in order to the manage the diff util algorithm
   *
   * @param newWrappers the new wrappers
   */
  public void setWrappersForDiffUtil(List<? extends SmartRecyclerViewWrapper<?>> newWrappers)
  {
    final SmartDiffUtilCallback diffCallback = getDiffUtilCallback(wrappers, newWrappers);
    final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback, isDetectMoves());

    super.setWrappers(newWrappers);

    diffResult.dispatchUpdatesTo(this);
  }

  protected boolean isDetectMoves()
  {
    return true;
  }

}