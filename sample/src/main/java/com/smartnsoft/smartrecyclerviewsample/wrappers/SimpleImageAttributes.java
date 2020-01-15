package com.smartnsoft.smartrecyclerviewsample.wrappers;

import androidx.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;

import com.smartnsoft.recyclerview.attributes.SmartRecyclerAttributes;

import com.smartnsoft.smartrecyclerviewsample.R;

/**
 * This class is a simple implementation of a SmartRecyclerAttributes displaying an image drawable
 *
 * @author Adrien Vitti
 * @since 2018.01.22
 */
final class SimpleImageAttributes
    extends SmartRecyclerAttributes<Integer>
{

  private final ImageView imageView;

  SimpleImageAttributes(View view)
  {
    super(view);
    imageView = view.findViewById(R.id.imageView);
  }

  @Override
  public void onBusinessObjectUpdated(@DrawableRes Integer businessObject, boolean isSelected)
  {
    super.onBusinessObjectUpdated(businessObject, isSelected);
    imageView.setImageResource(businessObject);
  }

}
