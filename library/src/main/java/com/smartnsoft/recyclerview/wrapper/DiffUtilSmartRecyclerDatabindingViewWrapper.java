package com.smartnsoft.recyclerview.wrapper;

import androidx.annotation.LayoutRes;

import com.smartnsoft.recyclerview.adapter.DiffUtilSmartRecyclerAdapter;

/**
 * A {@link SmartRecyclerDatabindingViewWrapper} that implements the {@link SmartDiffUtil} interface in order to be used with the {@link DiffUtilSmartRecyclerAdapter}.
 *
 * @param <BusinessObjectClass> the business object class which is represented by the current wrapper
 * @author Ludovic Roland
 * @see SmartRecyclerDatabindingViewWrapper
 * @since 2018.04.07
 */
public abstract class DiffUtilSmartRecyclerDatabindingViewWrapper<BusinessObjectClass>
    extends SmartRecyclerDatabindingViewWrapper<BusinessObjectClass>
    implements SmartDiffUtil
{

  protected DiffUtilSmartRecyclerDatabindingViewWrapper(BusinessObjectClass businessObject, int type,
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
