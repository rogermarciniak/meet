package com.baasbox.ITC_Meett.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import com.baasbox.ITC_Meett.R;

/**
 * @authors:
 * Roger Marciniak (c00169733)
 * Bartosz Zurawski(c00165634)
 */
public class NotesDetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle args = new Bundle();
            args.putParcelable(NoteDetailsFragment.CURRENTLY_SHOWN_ITEM_KEY,
                           getIntent().getParcelableExtra(NoteDetailsFragment.CURRENTLY_SHOWN_ITEM_KEY));
            NoteDetailsFragment fragment = new NoteDetailsFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_host,fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this,new Intent(this,NoteListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
