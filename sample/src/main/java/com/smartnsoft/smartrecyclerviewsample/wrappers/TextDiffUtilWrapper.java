package com.smartnsoft.smartrecyclerviewsample.wrappers;

import android.content.Context;
import android.view.View;

import com.smartnsoft.smartrecyclerview.wrapper.SmartDiffUtil;

import com.smartnsoft.smartrecyclerviewsample.R;

/**
 * A RecyclerViewWrapper with a simple String as business object
 * which can be used with DiffUtil
 *
 * @author Adrien Vitti
 * @since 2018.01.24
 */

public final class TextDiffUtilWrapper
    extends AbstractSmartRecyclerViewWrapper<String>
  implements SmartDiffUtil
{

  public TextDiffUtilWrapper(String businessObject)
  {
    super(businessObject, WrapperType.TextDiffUtilWrapper, R.layout.simple_text_item);
  }

  @Override
  protected Object extractNewViewAttributes(Context context, View view, String businessObject)
  {
    return new SimpleTextAttributes(view);
  }

  @Override
  public long getDiffUtilHashCode()
  {
    return getId();
  }

}
