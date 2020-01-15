package com.smartnsoft.smartrecyclerviewsample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import android.view.View;
import android.view.View.OnClickListener;

import com.smartnsoft.recyclerview.adapter.DiffUtilSmartRecyclerAdapter;
import com.smartnsoft.recyclerview.adapter.SmartRecyclerAdapter;
import com.smartnsoft.recyclerview.wrapper.SmartRecyclerViewWrapper;

import com.smartnsoft.smartrecyclerviewsample.adapter.SampleDiffUtilSmartRecyclerAdapter;
import com.smartnsoft.smartrecyclerviewsample.wrappers.TextDiffUtilWrapper;

/**
 * An activity containing a RecyclerView with a DiffUtilSmartRecyclerAdapter
 *
 * @author Adrien Vitti
 * @since 2018.01.24
 */
public class DiffUtilRecyclerActivity
    extends SimpleRecyclerActivity
{

  @Override
  protected SmartRecyclerAdapter createRecyclerAdapter()
  {
    return new SampleDiffUtilSmartRecyclerAdapter(DiffUtilRecyclerActivity.this);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    final View shuffle = findViewById(R.id.shuffle);
    shuffle.setVisibility(View.VISIBLE);
    shuffle.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        ((DiffUtilSmartRecyclerAdapter) smartRecyclerAdapter).setWrappersForDiffUtil(createWrappers());
      }
    });
  }

  @NonNull
  @Override
  protected List<SmartRecyclerViewWrapper<?>> createWrappers()
  {
    final List<SmartRecyclerViewWrapper<?>> wrappers = new ArrayList<>();
    final List<String> personList = Arrays.asList(getResources().getStringArray(R.array.personNames));

    Collections.shuffle(personList);
    final int maxName = (int) Math.max(30, personList.size() - Math.random() * personList.size());

    for (int index = 0; index < maxName; index++)
    {
      wrappers.add(new TextDiffUtilWrapper(personList.get(index)));
    }
    return wrappers;
  }

  @NonNull
  @Override
  protected LayoutManager getLayoutManager()
  {
    return new GridLayoutManager(DiffUtilRecyclerActivity.this, 3);
  }

}