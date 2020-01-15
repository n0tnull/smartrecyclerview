package com.smartnsoft.smartrecyclerviewsample.wrappers;

import androidx.annotation.IntDef;

/**
 * We have to register each type of wrapper in the IntDef to avoid recycling issues
 *
 * @author Adrien Vitti
 * @since 2018.01.22
 */

public final class WrapperType
{

  @IntDef({SimpleTextWrapper, SimpleImageWrapper, PersonWrapper, TextDiffUtilWrapper, ReorderTextWrapper})
  public @interface WrapperTypeIdentifier {}
  static final int SimpleTextWrapper = 0;
  static final int SimpleImageWrapper = 1;
  static final int PersonWrapper = 2;
  static final int TextDiffUtilWrapper = 3;
  static final int ReorderTextWrapper = 4;

}
