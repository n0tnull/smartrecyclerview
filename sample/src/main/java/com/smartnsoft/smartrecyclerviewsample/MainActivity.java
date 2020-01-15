package com.smartnsoft.smartrecyclerviewsample;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity
    extends AppCompatActivity
    implements OnClickListener
{

  private View reorderingRecyclerButton;

  private View diffutilRecyclerButton;

  private View simpleRecyclerButton;

  private View databindedRecyclerButton;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    reorderingRecyclerButton = findViewById(R.id.reorderingRecyclerButton);
    diffutilRecyclerButton = findViewById(R.id.diffutilRecyclerButton);
    simpleRecyclerButton = findViewById(R.id.simpleRecyclerButton);
    databindedRecyclerButton = findViewById(R.id.databindedRecyclerButton);

    reorderingRecyclerButton.setOnClickListener(this);
    diffutilRecyclerButton.setOnClickListener(this);
    simpleRecyclerButton.setOnClickListener(this);
    databindedRecyclerButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View view)
  {
    if (view == reorderingRecyclerButton)
    {
      startActivity(new Intent(MainActivity.this, ReorderedRecyclerActivity.class));
    }
    else if (view == diffutilRecyclerButton)
    {
      startActivity(new Intent(MainActivity.this, DiffUtilRecyclerActivity.class));
    }
    else if (view == simpleRecyclerButton)
    {
      startActivity(new Intent(MainActivity.this, SimpleRecyclerActivity.class));
    }
    else if (view == databindedRecyclerButton)
    {
      startActivity(new Intent(MainActivity.this, DataBindedRecyclerActivity.class));
    }
  }
}
