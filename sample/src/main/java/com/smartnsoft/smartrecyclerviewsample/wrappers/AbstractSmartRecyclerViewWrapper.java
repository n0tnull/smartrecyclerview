package com.smartnsoft.smartrecyclerviewsample.wrappers;

import com.smartnsoft.smartrecyclerviewsample.wrappers.WrapperType.WrapperTypeIdentifier;

/**
 * This class goal is to force the IntDef WrapperIdentifier when creating a new Wrapper
 *
 * @author Adrien Vitti
 * @since 2018.01.22
 */

abstract class AbstractSmartRecyclerViewWrapper<T>
    extends com.smartnsoft.smartrecyclerview.wrapper.SmartRecyclerViewWrapper<T>
{

  AbstractSmartRecyclerViewWrapper(T businessObject, @WrapperTypeIdentifier int type, int layoutResourceId)
  {
    super(businessObject, type, layoutResourceId);
  }
}
