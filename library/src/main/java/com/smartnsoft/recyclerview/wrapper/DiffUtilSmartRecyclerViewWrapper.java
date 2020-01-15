package com.smartnsoft.recyclerview.wrapper;

import androidx.annotation.LayoutRes;

import com.smartnsoft.recyclerview.adapter.DiffUtilSmartRecyclerAdapter;

/**
 * A {@link SmartRecyclerViewWrapper} that implements the {@link SmartDiffUtil} interface in order to be used with the {@link DiffUtilSmartRecyclerAdapter}.
 *
 * @param <BusinessObjectClass> the business object class which is represented by the current wrapper
 * @author Ludovic Roland
 * @see SmartRecyclerViewWrapper
 * @since 2017.09.27
 */
public abstract class DiffUtilSmartRecyclerViewWrapper<BusinessObjectClass>
    extends SmartRecyclerViewWrapper<BusinessObjectClass>
    implements SmartDiffUtil
{

  protected DiffUtilSmartRecyclerViewWrapper(BusinessObjectClass businessObject, int type,
      @LayoutRes int layoutResourceId)
  {
    super(businessObject, type, layoutResourceId);
  }

  @Override
  public long getId()
  {
    throw new UnsupportedOperationException("You have to override the 'getId' method in order to return the unique identifier the of the item in the adapter");
  }

}
