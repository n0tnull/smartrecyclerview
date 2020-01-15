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

package com.smartnsoft.recyclerview.attributes;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.View;

/**
 * Is responsible for creating a new {@link View}, which is able to represent the provided business object.
 *
 * @author Jocelyn Girard, Ludovic Roland
 * @since 2014.04.16
 */
public abstract class SmartRecyclerAttributes<BusinessObjectType>
    extends ViewHolder
{

  protected String intentFilterCategory;

  protected long businessObjectIdentifier = -1;

  protected Context context;

  public SmartRecyclerAttributes(View view)
  {
    super(view);

    this.context = itemView.getContext();
  }

  public void setIntentFilterCategory(String intentFilterCategory)
  {
    this.intentFilterCategory = intentFilterCategory;
  }

  public void update(BusinessObjectType businessObject, boolean isSelected)
  {
    final long businessHashCode = System.identityHashCode(businessObject);
    if (businessObjectIdentifier != businessHashCode)
    {
      onBusinessObjectUpdated(businessObject, isSelected);

      businessObjectIdentifier = businessHashCode;
    }
  }

  public void onBusinessObjectUpdated(BusinessObjectType businessObject, boolean isSelected)
  {
    // Does not perform any update by default
  }

}
