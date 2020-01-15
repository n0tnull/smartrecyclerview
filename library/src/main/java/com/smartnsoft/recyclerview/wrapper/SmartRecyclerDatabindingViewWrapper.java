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

package com.smartnsoft.recyclerview.wrapper;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartnsoft.recyclerview.attributes.SmartRecyclerDatabindingAttributes;

/**
 * A {@link SmartRecyclerViewWrapper} that allows the Databinding.
 *
 * @param <BusinessObjectClass> the business object class which is represented by the current wrapper
 * @author Ludovic Roland
 * @see SmartRecyclerViewWrapper
 * @since 2018.07.04
 */
public abstract class SmartRecyclerDatabindingViewWrapper<BusinessObjectClass>
    extends SmartRecyclerViewWrapper<BusinessObjectClass>
{

  public SmartRecyclerDatabindingViewWrapper(BusinessObjectClass businessObject, int type,
      @LayoutRes int layoutResourceId)
  {
    super(businessObject, type, layoutResourceId);
  }

  public View getNewView(ViewGroup parent, Context context)
  {
    final ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutResourceId, parent, false);
    viewDataBinding.getRoot().setTag(extractNewViewAttributes(context, viewDataBinding, businessObject));

    return viewDataBinding.getRoot();
  }

  @Override
  protected Object extractNewViewAttributes(Context context, View view, BusinessObjectClass businessObjectClass)
  {
    throw new UnsupportedOperationException("Please use the extractNewViewAttributes(Context, ViewDataBinding, BusinessObjectClass) method");
  }

  protected abstract SmartRecyclerDatabindingAttributes extractNewViewAttributes(Context context,
      ViewDataBinding viewDataBinding, BusinessObjectClass businessObjectClass);

}
