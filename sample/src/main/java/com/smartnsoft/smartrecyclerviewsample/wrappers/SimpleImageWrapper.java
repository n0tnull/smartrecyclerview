package com.smartnsoft.smartrecyclerviewsample.wrappers;

import android.content.Context;
import androidx.annotation.DrawableRes;
import android.view.View;

import com.smartnsoft.smartrecyclerviewsample.R;

/**
 * A RecyclerViewWrapper with an DrawableRes integer as business object
 *
 * @author Adrien Vitti
 * @since 2018.01.22
 */

public final class SimpleImageWrapper
    extends AbstractSmartRecyclerViewWrapper<Integer>
{

  public SimpleImageWrapper(@DrawableRes Integer businessObject)
  {
    super(businessObject, WrapperType.SimpleImageWrapper, R.layout.simple_image_item);
  }

  @Override
  protected Object extractNewViewAttributes(Context context, View view, @DrawableRes Integer businessObject)
  {
    return new SimpleImageAttributes(view);
  }

}
