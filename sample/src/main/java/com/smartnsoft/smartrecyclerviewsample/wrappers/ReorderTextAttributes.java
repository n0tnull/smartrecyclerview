package com.smartnsoft.smartrecyclerviewsample.wrappers;

import android.view.View;
import android.widget.TextView;

import com.smartnsoft.smartrecyclerview.attributes.SmartReorderRecyclerAttributes;

import com.smartnsoft.smartrecyclerviewsample.R;
import org.jetbrains.annotations.Nullable;

/**
 * This class is a simple implementation of a SmartReorderRecyclerAttributes with a String as business object
 *
 * @author Adrien Vitti
 * @since 2019.07.15
 */
final class ReorderTextAttributes
    extends SmartReorderRecyclerAttributes<String>
{

  private final TextView textView;

  private final View reorderIcon;

  ReorderTextAttributes(View view)
  {
    super(view);
    textView = view.findViewById(R.id.textView);
    reorderIcon = view.findViewById(R.id.reorderIcon);
  }

  @Override
  public void onBusinessObjectUpdated(String businessObject, boolean isSelected)
  {
    super.onBusinessObjectUpdated(businessObject, isSelected);
    textView.setText(businessObject);
  }

  @Nullable
  @Override
  public View getTriggerViewForReorder()
  {
    return reorderIcon;
  }

}
