package com.smartnsoft.smartrecyclerviewsample.wrappers;

import android.view.View;
import android.widget.TextView;

import com.smartnsoft.smartrecyclerview.attributes.SmartRecyclerAttributes;

import com.smartnsoft.smartrecyclerviewsample.R;

/**
 * This class is a simple implementation of a SmartRecyclerAttributes with a String as business object
 *
 * @author Adrien Vitti
 * @since 2018.01.22
 */
final class SimpleTextAttributes
    extends SmartRecyclerAttributes<String>
{

  private final TextView textView;

  SimpleTextAttributes(View view)
  {
    super(view);
    textView = view.findViewById(R.id.textView);
  }

  @Override
  public void onBusinessObjectUpdated(String businessObject, boolean isSelected)
  {
    super.onBusinessObjectUpdated(businessObject, isSelected);
    textView.setText(businessObject);
  }

}
