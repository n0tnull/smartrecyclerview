// The MIT License (MIT)
//
// Copyright (c) 2017 Smart&Soft
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

package com.smartnsoft.recyclerview.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build.VERSION_CODES;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import android.util.SparseArray;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

import com.smartnsoft.recyclerview.attributes.SmartRecyclerAttributes;
import com.smartnsoft.recyclerview.wrapper.SmartRecyclerViewWrapper;

/**
 * A {@link RecyclerView} adapter, which works closely with the {@link SmartRecyclerViewWrapper}.
 *
 * @author Jocelyn Girard, Ludovic Roland, Adrien Vitti
 * @since 2014.04.16
 */
@SuppressWarnings("unused")
public class SmartRecyclerAdapter
    extends Adapter<SmartRecyclerAttributes>
{

  /**
   * Update behaviour that can be used:
   * <li>{@link #NONE}</li>
   * <li>{@link #REMOVE_OLD_DATA_AT_ONCE}</li>
   * <li>{@link #REMOVE_OLD_DATA_ONE_BY_ONE}</li>
   * <li>{@link #IGNORE_NEW_DUPLICATES}</li>
   * <li>{@link #REMOVE_OLD_DUPLICATES}</li>
   */
  public enum UpdateType
  {
    /**
     * Does nothing
     */
    NONE,
    /**
     * Removes every items in one batch
     */
    REMOVE_OLD_DATA_AT_ONCE,
    /**
     * Removes items one by one to benefit from single deletion animation
     */
    REMOVE_OLD_DATA_ONE_BY_ONE,
    /**
     * Removes duplicates in the new list before adding them
     */
    IGNORE_NEW_DUPLICATES,
    /**
     * Removes duplicates in the current list before adding new ones
     */
    REMOVE_OLD_DUPLICATES,
    /**
     * Replaces duplicates in place before adding new ones
     */
    REPLACE_DUPLICATES
  }

  /**
   * Comparison behaviour that can be used:
   * <li>{@link #CLASSIC}</li>
   * <li>{@link #BUSINESS_OBJECT_TYPE}</li>
   * <li>{@link #WRAPPER_TYPE}</li>
   * <li>{@link #BUSINESS_OBJECT_AND_WRAPPER_TYPE}</li>
   */
  public enum ComparisonType
  {
    /**
     * Classic comparison of id
     */
    CLASSIC,
    /**
     * Compare ids and business object type
     */
    BUSINESS_OBJECT_TYPE,
    /**
     * Compare ids and wrapper type
     */
    WRAPPER_TYPE,
    /**
     * Compare ids, business object type and wrapper type
     */
    BUSINESS_OBJECT_AND_WRAPPER_TYPE
  }

  public static final class SnappyScrollListener
      extends RecyclerView.OnScrollListener
  {

    final AtomicBoolean autoSet = new AtomicBoolean(true);

    final int screenCenterX;

    @TargetApi(VERSION_CODES.HONEYCOMB_MR2)
    public SnappyScrollListener(Activity activity)
    {
      final Display display = activity.getWindowManager().getDefaultDisplay();
      final Point size = new Point();
      display.getSize(size);
      screenCenterX = size.x / 2;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState)
    {
      super.onScrollStateChanged(recyclerView, newState);

      if (autoSet.get() == false && newState == RecyclerView.SCROLL_STATE_IDLE)
      {
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        final View view = findCenterView(layoutManager);
        if (view != null)
        {
          final int scrollXNeeded = (screenCenterX - (view.getLeft() + view.getRight()) / 2);
          recyclerView.smoothScrollBy(scrollXNeeded * (view.getRight() < screenCenterX ? 1 : -1), 0);
        }
        autoSet.set(true);
      }

      if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING)
      {
        autoSet.set(false);
      }
    }

    private View findCenterView(LinearLayoutManager layoutManager)
    {
      int minDistance = 0;
      View centeredView = null;
      for (int itemPosition = layoutManager.findFirstVisibleItemPosition(); itemPosition <= layoutManager.findLastVisibleItemPosition(); itemPosition++)
      {
        final View view = layoutManager.findViewByPosition(itemPosition);
        final int leastDiff = Math.abs(screenCenterX - (view.getLeft() + view.getRight()) / 2);
        if (leastDiff <= minDistance || itemPosition == layoutManager.findFirstVisibleItemPosition())
        {
          minDistance = leastDiff;
          centeredView = view;
        }
        else
        {
          break;
        }
      }

      return centeredView;
    }

  }

  protected final Context context;

  protected List<SmartRecyclerViewWrapper<?>> wrappers = new ArrayList<>();

  private final SparseArray<SmartRecyclerViewWrapper<?>> viewTypeAttributesDictionary = new SparseArray<>();

  private final boolean shouldNotifyBeCalled;

  private String intentFilterCategory;

  private int selectedPositionItem = -1;

  public SmartRecyclerAdapter(Context context)
  {
    this(context, false);
  }

  public SmartRecyclerAdapter(Context context, boolean shouldNotifyChangesAutomatically)
  {
    this.context = context;
    this.shouldNotifyBeCalled = shouldNotifyChangesAutomatically;
  }

  @Override
  public SmartRecyclerAttributes onCreateViewHolder(ViewGroup viewGroup, int viewType)
  {
    final SmartRecyclerViewWrapper wrapper = viewTypeAttributesDictionary.get(viewType);
    final View view = wrapper.getNewView(viewGroup, context);
    final SmartRecyclerAttributes viewAttributes = wrapper.getViewAttributes(view);
    viewAttributes.setIntentFilterCategory(intentFilterCategory);

    return viewAttributes;
  }

  @Override
  public void onBindViewHolder(SmartRecyclerAttributes smartRecyclerAttributes, int position)
  {
    final Object businessObject = wrappers.get(position).getBusinessObject();
    smartRecyclerAttributes.update(businessObject, selectedPositionItem == position);
  }

  @Override
  public void onViewRecycled(SmartRecyclerAttributes holder)
  {
    super.onViewRecycled(holder);
  }

  /**
   * Allows you to get the unique identifier of an item in the adapter
   *
   * @param position The position of the item
   * @return The unique identifier of the item
   */
  @Override
  public final long getItemId(int position)
  {
    return wrappers.get(position).getId();
  }

  /**
   * @return the numbers of item in the adapter
   */
  @Override
  public final int getItemCount()
  {
    return wrappers.size();
  }

  @Override
  public final int getItemViewType(int position)
  {
    return wrappers.get(position).getType();
  }

  /**
   * Initializes the wrapper list in the adapter and call notifyDataSetChanged
   * Should be called only the first time to avoid flick.
   * It MUST be used on the UI thread.
   *
   * @param wrappers The list of wrappers to use in the adapter
   */
  public void setWrappers(List<? extends SmartRecyclerViewWrapper<?>> wrappers)
  {
    this.wrappers = new ArrayList<>(wrappers);

    for (SmartRecyclerViewWrapper<?> wrapper : wrappers)
    {
      final int wrapperType = wrapper.getType();
      if (viewTypeAttributesDictionary.get(wrapperType) == null)
      {
        viewTypeAttributesDictionary.append(wrapperType, wrapper);
      }
    }
    if (shouldNotifyBeCalled)
    {
      notifyDataSetChanged();
    }
  }

  /**
   * It MUST be used on the UI thread.
   *
   * @param position The position which will be selected in the list
   */
  @UiThread
  public final void setSelectedPositionItem(final int position)
  {
    final int lastSelectedPositionItem = selectedPositionItem;
    this.selectedPositionItem = position;
    notifyItemChanged(position);
    notifyItemChanged(lastSelectedPositionItem);
  }

  public int getSpanSizeForPosition(int position)
  {
    if (position >= 0 && position < wrappers.size())
    {
      return wrappers.get(position).getSpanSize();
    }

    return 1;
  }

  public void setIntentFilterCategory(String intentFilterCategory)
  {
    this.intentFilterCategory = intentFilterCategory;
  }

  /**
   * Removes only a single item in the adapter and call notifyItemRemoved
   * It MUST be used on the UI thread.
   *
   * @param position the position of the item to remove
   */
  @UiThread
  public final void removeItem(final int position)
  {
    wrappers.remove(position);
    if (shouldNotifyBeCalled)
    {
      notifyItemRemoved(position);
    }
  }

  /**
   * Removes every wrapper in the adapter and call notifyItemRangeRemoved
   * It MUST be used on the UI thread.
   */
  @UiThread
  public final void removeAll()
  {
    final int initialSize = this.wrappers.size();
    wrappers.clear();
    if (shouldNotifyBeCalled)
    {
      notifyItemRangeRemoved(0, initialSize);
    }
  }

  /**
   * Adds an item to the adapter and call notifyItemInserted
   * It MUST be used on the UI thread.
   *
   * @param item the wrapper you want to add to the adapter
   */
  @UiThread
  public final void addItem(SmartRecyclerViewWrapper<?> item)
  {
    addItem(wrappers.size(), item, true);
  }

  /**
   * Adds an item to the adapter at a specified position
   * and call notifyItemInserted
   * It MUST be used on the UI thread.
   *
   * @param position The index where we want to add the wrapper
   * @param item     the wrapper you want to add to the adapter
   */
  @UiThread
  public final void addItem(int position, SmartRecyclerViewWrapper<?> item)
  {
    addItem(position, item, true);
  }

  /**
   * Adds a list of wrapper to the adapter without any verification and call notifyItemRangeInserted
   * It MUST be used on the UI thread.
   *
   * @param wrappersToAdd the list of wrappers to add
   */
  @UiThread
  public final void addAll(List<? extends SmartRecyclerViewWrapper<?>> wrappersToAdd)
  {
    final int initialSize = this.wrappers.size();
    if (wrappersToAdd != null && wrappersToAdd.size() > 0)
    {
      for (SmartRecyclerViewWrapper<?> item : wrappersToAdd)
      {
        addItem(wrappers.size(), item, false);
      }
      if (shouldNotifyBeCalled)
      {
        notifyItemRangeInserted(initialSize, wrappersToAdd.size());
      }
    }
  }

  /**
   * Adds a list of wrapper to the adapter at the specified position
   * without any verification and call notifyItemRangeInserted
   * It MUST be used on the UI thread.
   *
   * @param position      The index where we want to add the list
   * @param wrappersToAdd the list of wrappers to add
   */
  @UiThread
  public final void addAll(final int position, List<? extends SmartRecyclerViewWrapper<?>> wrappersToAdd)
  {
    int index = position;
    if (wrappersToAdd != null && wrappersToAdd.size() > 0 && position >= 0 && position <= this.wrappers.size())
    {
      for (SmartRecyclerViewWrapper<?> item : wrappersToAdd)
      {
        addItem(index++, item, false);
      }
      if (shouldNotifyBeCalled)
      {
        notifyItemRangeInserted(position, wrappersToAdd.size());
      }
    }
  }

  /**
   * If the adapter has already been set, use this method to update data.
   * It MUST be used on the UI thread.
   *
   * @param newWrappers The list of wrappers you want to use for the update
   */
  @UiThread
  public void updateWrappers(List<? extends SmartRecyclerViewWrapper<?>> newWrappers)
  {
    addAll(newWrappers);
  }

  @UiThread
  public void updateWrappers(List<? extends SmartRecyclerViewWrapper<?>> newWrappers, UpdateType removeType)
  {
    updateWrappers(newWrappers, removeType, ComparisonType.CLASSIC);
  }

  /**
   * If the adapter has already been set, use this method to update data.
   * It MUST be used on the UI thread.
   *
   * @param newWrappers    The list of wrappers you want to use for the update
   * @param removeType     One of {@link UpdateType#NONE}, {@link UpdateType#REMOVE_OLD_DATA_AT_ONCE}, {@link UpdateType#REMOVE_OLD_DATA_ONE_BY_ONE},
   *                       {@link UpdateType#IGNORE_NEW_DUPLICATES}, or {@link UpdateType#REMOVE_OLD_DUPLICATES}.
   * @param comparisonType One of {@link ComparisonType#CLASSIC}, {@link ComparisonType#BUSINESS_OBJECT_TYPE}, {@link ComparisonType#WRAPPER_TYPE}
   *                       or {@link ComparisonType#BUSINESS_OBJECT_AND_WRAPPER_TYPE}.
   */
  @UiThread
  public void updateWrappers(List<? extends SmartRecyclerViewWrapper<?>> newWrappers, UpdateType removeType,
      ComparisonType comparisonType)
  {
    if (newWrappers != null)
    {
      switch (removeType)
      {
        case REMOVE_OLD_DATA_AT_ONCE:
          removeAll();
          break;
        case REMOVE_OLD_DATA_ONE_BY_ONE:
          final List<SmartRecyclerViewWrapper<?>> oldWrapperToRemove = new ArrayList<>();
          for (int index = 0; index < wrappers.size(); index++)
          {
            oldWrapperToRemove.add(wrappers.get(index));
          }
          for (final SmartRecyclerViewWrapper<?> smartRecyclerViewWrapper : oldWrapperToRemove)
          {
            removeItem(getItemPosition(smartRecyclerViewWrapper.getId(),
                smartRecyclerViewWrapper.getBusinessObject() != null ? smartRecyclerViewWrapper.getBusinessObject().getClass() : null,
                smartRecyclerViewWrapper.getClass(), comparisonType));
          }
          break;
        case IGNORE_NEW_DUPLICATES:
          final List<SmartRecyclerViewWrapper<?>> wrapperToRemove = new ArrayList<>();
          for (SmartRecyclerViewWrapper<?> item : newWrappers)
          {
            if (contains(item.getId(), item.getBusinessObject() != null ? item.getBusinessObject().getClass() : null,
                item.getClass(), comparisonType))
            {
              wrapperToRemove.add(item);
            }
          }
          newWrappers.removeAll(wrapperToRemove);
          break;
        case REMOVE_OLD_DUPLICATES:
          for (final SmartRecyclerViewWrapper<?> wrapper : newWrappers)
          {
            final long businessObjectId = wrapper.getId();
            final Class<?> businessObjectType = wrapper.getBusinessObject() != null ? wrapper.getBusinessObject().getClass() : null;
            final Class<? extends SmartRecyclerViewWrapper> wrapperType = wrapper.getClass();
            if (contains(businessObjectId, businessObjectType, wrapperType, comparisonType))
            {
              removeItem(getItemPosition(businessObjectId, businessObjectType, wrapperType, comparisonType));
            }
          }
          break;
        case REPLACE_DUPLICATES:
          final List<SmartRecyclerViewWrapper<?>> replacedWrappersToRemove = new ArrayList<>();
          for (final SmartRecyclerViewWrapper<?> wrapper : newWrappers)
          {
            final long businessObjectId = wrapper.getId();
            final Class<?> businessObjectType = wrapper.getBusinessObject() != null ? wrapper.getBusinessObject().getClass() : null;
            final Class<? extends SmartRecyclerViewWrapper> wrapperType = wrapper.getClass();
            if (contains(businessObjectId, businessObjectType, wrapperType, comparisonType))
            {
              final int position = getItemPosition(businessObjectId, businessObjectType, wrapperType, comparisonType);
              set(position, wrapper);
              replacedWrappersToRemove.add(wrapper);
            }
          }
          newWrappers.removeAll(replacedWrappersToRemove);
          break;
        case NONE:
        default:
          break;
      }

      addAll(newWrappers);
    }
  }

  /**
   * Allows you to know if an adapter contains a specific object.
   * The identifier must be unique, for example the object's hashcode.
   *
   * @param businessObjectID The unique identifier of an object
   * @return true if the adapter as an item with the same identifier, false otherwise
   */
  public final boolean contains(long businessObjectID)
  {
    if (this.wrappers != null && this.wrappers.size() > 0 && businessObjectID != -1)
    {
      for (int index = 0; index < this.wrappers.size(); index++)
      {
        if (businessObjectID == getItemId(index))
        {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Allows you to know if an adapter contains a specific object.
   * The identifier must be unique, for example the object's hashcode.
   *
   * @param businessObjectID   The unique identifier of an object
   * @param businessObjectType The business object type
   * @param wrapperType        The wrapper type
   * @return true if the adapter as an item with the same identifier and other parameters depending on comparisonType, false otherwise
   */
  public final boolean contains(long businessObjectID, Class<?> businessObjectType, Class<?> wrapperType,
      ComparisonType comparisonType)
  {
    if (this.wrappers != null && this.wrappers.size() > 0 && businessObjectID != -1)
    {
      for (int index = 0; index < this.wrappers.size(); index++)
      {
        final boolean areEqual;
        final SmartRecyclerViewWrapper<?> wrapper = this.wrappers.get(index);
        final Object businessObject = wrapper.getBusinessObject();
        switch (comparisonType)
        {
          default:
          case CLASSIC:
            areEqual = businessObjectID == getItemId(index);
            break;
          case BUSINESS_OBJECT_TYPE:
            areEqual = businessObjectID == getItemId(
                index) && businessObjectType == (businessObject != null ? businessObject.getClass() : null);
            break;
          case WRAPPER_TYPE:
            areEqual = businessObjectID == getItemId(index) && wrapperType == wrapper.getClass();
            break;
          case BUSINESS_OBJECT_AND_WRAPPER_TYPE:
            areEqual = businessObjectID == getItemId(
                index) && businessObjectType == (businessObject != null ? businessObject.getClass() : null) && wrapperType == wrapper.getClass();
            break;
        }

        if (areEqual)
        {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Allows you to recover the business object used by the adapter.
   * It should be used with caution as the type return is a list of Object and not a list using the correct type of object.
   *
   * @return the list of Business Object which are used by wrappers
   */
  public final List<Object> getItemList()
  {
    final List<Object> items = new ArrayList<>();

    for (SmartRecyclerViewWrapper<?> item : wrappers)
    {
      items.add(item.getBusinessObject());
    }

    return items;
  }

  /**
   * Allows you to get a specific wrapper via its unique identifier.
   *
   * @param businessObjectID The unique identifier of the object
   * @return the object if found, null otherwise
   */
  public final SmartRecyclerViewWrapper<?> getItemWrapper(long businessObjectID)
  {
    if (this.wrappers != null && this.wrappers.isEmpty() == false && businessObjectID != -1)
    {
      for (int index = 0; index < this.wrappers.size(); index++)
      {
        if (businessObjectID == getItemId(index))
        {
          return wrappers.get(index);
        }
      }
    }

    return null;
  }

  /**
   * Allows you to get a specific wrapper via its unique identifier.
   *
   * @param businessObjectID   The unique identifier of the object
   * @param businessObjectType The business object type
   * @param wrapperType        The wrapper type
   * @return the object if found, null otherwise
   */
  public final SmartRecyclerViewWrapper<?> getItemWrapper(long businessObjectID, Class<?> businessObjectType,
      Class<?> wrapperType, ComparisonType comparisonType)
  {
    if (this.wrappers != null && this.wrappers.isEmpty() == false && businessObjectID != -1)
    {
      for (int index = 0; index < this.wrappers.size(); index++)
      {
        final boolean areEqual;
        final SmartRecyclerViewWrapper<?> wrapper = this.wrappers.get(index);
        final Object businessObject = wrapper.getBusinessObject();
        switch (comparisonType)
        {
          default:
          case CLASSIC:
            areEqual = businessObjectID == getItemId(index);
            break;
          case BUSINESS_OBJECT_TYPE:
            areEqual = businessObjectID == getItemId(
                index) && businessObjectType == (businessObject != null ? businessObject.getClass() : null);
            break;
          case WRAPPER_TYPE:
            areEqual = businessObjectID == getItemId(index) && wrapperType == wrapper.getClass();
            break;
          case BUSINESS_OBJECT_AND_WRAPPER_TYPE:
            areEqual = businessObjectID == getItemId(
                index) && businessObjectType == (businessObject != null ? businessObject.getClass() : null) && wrapperType == wrapper.getClass();
            break;
        }

        if (areEqual)
        {
          return wrappers.get(index);
        }
      }
    }

    return null;
  }

  /**
   * Allows you to get a specific object position via its unique identifier.
   *
   * @param businessObjectID The unique identifier of the object
   * @return the object position if found, -1 otherwise
   */
  public final int getItemPosition(long businessObjectID)
  {
    if (this.wrappers != null && this.wrappers.isEmpty() == false && businessObjectID != -1)
    {
      for (int index = 0; index < this.wrappers.size(); index++)
      {
        if (businessObjectID == getItemId(index))
        {
          return index;
        }
      }
    }

    return -1;
  }

  /**
   * Allows you to get a specific object position via its unique identifier.
   *
   * @param businessObjectID   The unique identifier of the object
   * @param businessObjectType The business object type
   * @param wrapperType        The wrapper type
   * @return the object position if found, -1 otherwise
   */
  public final int getItemPosition(long businessObjectID, Class<?> businessObjectType, Class<?> wrapperType,
      ComparisonType comparisonType)
  {
    if (this.wrappers != null && this.wrappers.isEmpty() == false && businessObjectID != -1)
    {
      for (int index = 0; index < this.wrappers.size(); index++)
      {
        final boolean areEqual;
        final SmartRecyclerViewWrapper<?> wrapper = this.wrappers.get(index);
        final Object businessObject = wrapper.getBusinessObject();
        switch (comparisonType)
        {
          default:
          case CLASSIC:
            areEqual = businessObjectID == getItemId(index);
            break;
          case BUSINESS_OBJECT_TYPE:
            areEqual = businessObjectID == getItemId(
                index) && businessObjectType == (businessObject != null ? businessObject.getClass() : null);
            break;
          case WRAPPER_TYPE:
            areEqual = businessObjectID == getItemId(index) && wrapperType == wrapper.getClass();
            break;
          case BUSINESS_OBJECT_AND_WRAPPER_TYPE:
            areEqual = businessObjectID == getItemId(
                index) && businessObjectType == (businessObject != null ? businessObject.getClass() : null) && wrapperType == wrapper.getClass();
            break;
        }

        if (areEqual)
        {
          return index;
        }
      }
    }

    return -1;
  }

  /**
   * Replaces the element at the specified location in this List with the specified object. This operation does not change the size of the List.
   *
   * @param position the index at which to put the specified object.
   * @param item     the object to insert.
   * @return Returns the previous element at the index.
   */
  public SmartRecyclerViewWrapper<?> set(int position, SmartRecyclerViewWrapper<?> item)
  {
    if (this.wrappers != null && this.wrappers.isEmpty() == false && position >= 0 && position <= this.wrappers.size())
    {
      final int wrapperType = item.getType();
      if (viewTypeAttributesDictionary.get(wrapperType) == null)
      {
        viewTypeAttributesDictionary.append(wrapperType, item);
      }
      final SmartRecyclerViewWrapper<?> wrapper = this.wrappers.set(position, item);
      if (shouldNotifyBeCalled)
      {
        notifyItemChanged(position);
      }

      return wrapper;
    }
    return null;
  }

  /**
   * Allows you to move a wrapper present at a specific position to a new given position.
   * If at least one of the parameters is out of bounds for the current list of wrapper, this method won't do anything.
   *
   * @param fromIndex The initial position of the wrapper to move
   * @param toIndex   The final position of the wrapper to move
   */
  public void moveWrapper(int fromIndex, int toIndex)
  {
    if (fromIndex >= 0 && fromIndex < wrappers.size()
        && toIndex >= 0 && toIndex < wrappers.size())
    {
      final SmartRecyclerViewWrapper<?> wrapperToMove = wrappers.get(fromIndex);
      if (wrapperToMove != null)
      {
        wrappers.remove(fromIndex);
        wrappers.add(toIndex, wrapperToMove);
        if (shouldNotifyBeCalled)
        {
          notifyItemMoved(fromIndex, toIndex);
        }
      }
    }
  }

  /**
   * Allows you to know if the given wrapper list contains a specific id.
   * The identifier must be unique, for example the object's hashcode.
   *
   * @param wrappers         The list of wrappers to look into
   * @param businessObjectID The unique identifier of an object
   * @return true if the adapter as an item with the same identifier, false otherwise
   */
  public final boolean isObjectIDContainedInList(final List<SmartRecyclerViewWrapper<?>> wrappers,
      long businessObjectID)
  {
    if (wrappers != null && wrappers.isEmpty() == false && businessObjectID != -1)
    {
      for (int index = 0; index < wrappers.size(); index++)
      {
        if (businessObjectID == wrappers.get(index).getId())
        {
          return true;
        }
      }
    }

    return false;
  }

  public SparseArray<SmartRecyclerViewWrapper<?>> getViewTypeAttributesDictionary()
  {
    return viewTypeAttributesDictionary;
  }

  /**
   * Adds an item to the adapter at a specified position
   * and call notifyItemInserted
   * It MUST be used on the UI thread.
   *
   * @param position The index where we want to add the wrapper
   * @param item     the wrapper you want to add to the adapter
   */
  private void addItem(int position, SmartRecyclerViewWrapper<?> item, boolean shouldNotify)
  {
    wrappers.add(position, item);

    final int wrapperType = item.getType();
    if (viewTypeAttributesDictionary.get(wrapperType) == null)
    {
      viewTypeAttributesDictionary.append(wrapperType, item);
    }

    if (shouldNotify && shouldNotifyBeCalled)
    {
      notifyItemInserted(position);
    }
  }

}
