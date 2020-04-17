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

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.smartnsoft.smartrecyclerview.attributes.SmartRecyclerAttributes;
import com.smartnsoft.smartrecyclerview.wrapper.SmartRecyclerViewWrapper;

/**
 * @author Raphael Kiffer
 * @since 2016.01.12
 */
public class SmartSelectorRecyclerAdapter
    extends SmartRecyclerAdapter
{

  private boolean isMultipleSelectionEnabled;

  private Map<Long, Boolean> selectableMap;

  public SmartSelectorRecyclerAdapter(Context context)
  {
    this(context, false);
  }

  public SmartSelectorRecyclerAdapter(Context context, boolean isMultipleSelectionEnabled)
  {
    super(context);

    this.isMultipleSelectionEnabled = isMultipleSelectionEnabled;

    selectableMap = new HashMap<>();
  }

  @Override
  public void onBindViewHolder(SmartRecyclerAttributes smartRecyclerAttributes, int position)
  {
    if (isMultipleSelectionEnabled == false)
    {
      super.onBindViewHolder(smartRecyclerAttributes, position);
    }
    else
    {
      final SmartRecyclerViewWrapper<?> wrapper = wrappers.get(position);
      final Object businessObject = wrapper.getBusinessObject();
      smartRecyclerAttributes.update(businessObject,
          selectableMap.containsKey(wrapper.getId()) && selectableMap.get(wrapper.getId()));
    }

  }

  public final void toggleItemSelectedState(long businessObjectId)
  {
    if (selectableMap.containsKey(businessObjectId))
    {
      setItemSelectedState(businessObjectId, !selectableMap.get(businessObjectId));
    }
    else
    {
      setItemSelectedState(businessObjectId, true);
    }
  }

  public final void setItemSelectedState(long businessObjectId, boolean isSelected)
  {
    selectableMap.put(businessObjectId, isSelected);
    notifyItemChanged(getItemPosition(businessObjectId));
  }

}
