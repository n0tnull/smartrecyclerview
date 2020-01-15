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
import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartnsoft.recyclerview.attributes.SmartRecyclerAttributes;

/**
 * Wraps a business object and its underlying Android {@link View} in a {@link RecyclerView}.
 *
 * @param <BusinessObjectClass> the business object class which is represented by the current wrapper
 * @author Jocelyn Girard, Ludovic Roland
 * @since 2014.04.16
 */
public abstract class SmartRecyclerViewWrapper<BusinessObjectClass>
{

  private static final int DEFAULT_SPAN_SIZE = 1;

  protected BusinessObjectClass businessObject;

  @LayoutRes
  protected int layoutResourceId;

  private int type;

  public SmartRecyclerViewWrapper(BusinessObjectClass businessObject, int type, @LayoutRes int layoutResourceId)
  {
    this.businessObject = businessObject;
    this.type = type;
    this.layoutResourceId = layoutResourceId;
  }

  public BusinessObjectClass getBusinessObject()
  {
    return businessObject;
  }

  public long getId()
  {
    return getId(businessObject);
  }

  public final int getType()
  {
    return type;
  }

  public int getSpanSize()
  {
    return SmartRecyclerViewWrapper.DEFAULT_SPAN_SIZE;
  }

  public View getNewView(ViewGroup parent, Context context)
  {
    final View view = LayoutInflater.from(context).inflate(layoutResourceId, parent, false);
    view.setTag(extractNewViewAttributes(context, view, businessObject));

    return view;
  }

  public SmartRecyclerAttributes<BusinessObjectClass> getViewAttributes(View view)
  {
    return (SmartRecyclerAttributes<BusinessObjectClass>) view.getTag();
  }

  protected long getId(BusinessObjectClass businessObject)
  {
    return businessObject == null ? 0L : businessObject.hashCode();
  }

  protected abstract Object extractNewViewAttributes(Context context, View view,
      BusinessObjectClass businessObjectClass);

}
