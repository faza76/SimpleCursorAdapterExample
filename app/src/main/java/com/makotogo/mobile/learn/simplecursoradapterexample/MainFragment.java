/*
 *  Copyright 2016 Makoto Consulting Group, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.makotogo.mobile.learn.simplecursoradapterexample;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The Main fragment. Okay, the only fragment. The app is simple.
 * <p>
 * Uses the Configure/Init/Update pattern. What is that, you ask? Android
 * is awesome, but is so open and flexible that writing apps of anything
 * but the simplest complexity causes the code to become quickly unmanageable.
 * <p>
 * So I invented the Configure/Init/Update pattern, a design pattern comprised
 * of three parts:
 * <p>
 * <ol>
 * <li>
 * Configure - the UI is configured in its initial state.
 * </li>
 * <li>
 * Init - this is done using separate (usually private) methods, one for
 * each UI control (e.g., TextView, ListView, etc). This keeps the code manageable
 * and modular (rather than trying to configure each control in one giant monolithic
 * onCreateCiew() method).
 * </li>
 * <li>
 * Update - as the app makes changes to the underlying model, the UI needs to
 * be updated to reflect this. Again, these updates are made from a single updateUI()
 * method, which can handle all udpates itself, or if the code is complex, should
 * delegate out to private methods for each control. This keeps the code manageable
 * and modular.
 * </li>
 * </ol>
 * Enjoy the free code!
 * <p>
 * Makoto Go Apps.
 * </p>
 * <p>http://makotoconsutling.com</p>
 * <p>https://github.com/makotogo</p>
 */
public class MainFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        processFragmentArguments();

        View ret = configureUI(inflater, container, savedInstanceState);

        // If this Fragment already exists, then reconstitute it.
        /// Yep, sounds pretty gross (Soylent Green is people, man!)
        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        }

        // Return the View
        return ret;
    }

    //**************************************
    //* L I F E C Y C L E    M E T H O D S *
    //**************************************

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveInstanceState(outState);
    }

    //********************************
    //* H E L P E R    M E T H O D S *
    //********************************

    private void processFragmentArguments() {
        // Nothing to do (yet)
    }

    private void saveInstanceState(Bundle outState) {
        // Nothing to do (yet)
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        // Nothing to do (yet)
    }

    /**
     * Configures the UI.
     * <p>
     * Part of the Configure/Init/Update pattern.
     *
     * @param layoutInflater
     * @param container
     * @param savedInstanceState
     * @return View - The View, configured, and ready to use.
     */
    private View configureUI(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        final String METHOD = "configureUI(..., Bundle {" + savedInstanceState + "})";

        // Inflate the layout from the XML definition
        View ret = layoutInflater.inflate(R.layout.fragment_main, container, false);

        // Initialize the TextView
        initTextView(ret);

        // Initialize the ListView
        initListView(ret);

        // Initialize the Add Person Button
        initAddPersonButton(ret);

        // Initialize the Delete All Button
        initDeleteAllButton(ret);

        // Return the View
        return ret;
    }

    /**
     * Updates the UI.
     * <p>
     * Part of the Configure/Init/Update pattern.
     */
    private void updateUI() {
        // Update the ListView with the current contents of the
        /// DB so it's always up-to-date
        ListView listView = (ListView) getView(getView(), R.id.listView_people);
        SimpleCursorAdapter listAdapter = (SimpleCursorAdapter) listView.getAdapter();
        Cursor newCursor = DbHelper.instance(getActivity()).fetchAll();
        listAdapter.changeCursor(newCursor);
        listAdapter.notifyDataSetChanged();
    }

    /**
     * Initializes the Text View that acts as a label for the People
     * ListView.
     * <p>
     * Part of the Configure/Init/Update pattern.
     *
     * @param view The Parent View that contains the TextView to be initialized.
     */
    private void initTextView(View view) {
        // Set the title. Yep that's it. Hey man, it's a text view, it's
        /// pretty simple.
        TextView textView = (TextView) getView(view, R.id.textView_people);
        textView.setText(R.string.title_people);
    }

    /**
     * Initializes the ListView that contains the Person objects from the DB.
     * <p>
     * Part of the Configure/Init/Update pattern.
     *
     * @param view The Parent View that contains the ListView to be initialized.
     */
    private void initListView(View view) {
        // Set the Adapter
        ListView listView = (ListView) getView(view, R.id.listView_people);
        // Columns
        String[] columns = {
                DbHelper.DbSchema.PersonTable.Column.LAST_NAME,
                DbHelper.DbSchema.PersonTable.Column.FIRST_NAME,
                DbHelper.DbSchema.PersonTable.Column.AGE,
                DbHelper.DbSchema.PersonTable.Column.EYE_COLOR,
                DbHelper.DbSchema.PersonTable.Column.GENDER
        };
        int[] resourceIds = {
                R.id.textview_list_row_firstName,
                R.id.textview_list_row_lastName,
                R.id.textview_list_row_age,
                R.id.textview_list_row_eyeColor,
                R.id.textview_list_row_gender
        };
        //
        Cursor cursor = DbHelper.instance(getActivity()).fetchAll();
        ListAdapter listAdapter = new SimpleCursorAdapter(getActivity(), R.layout.listview_row, cursor, columns, resourceIds, 0);
        listView.setAdapter(listAdapter);
    }

    /**
     * Initializes the Add Person button. When the user clicks it, a new Person
     * object is added to the DB.
     * <p>
     * Part of the Configure/Init/Update pattern.
     *
     * @param view
     */
    private void initAddPersonButton(View view) {
        //
        Button button = (Button) getView(view, R.id.button_addPerson);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add a new Person
                // Create a random Person
                Person person = PersonGenerator.createPerson();
                DbHelper.instance(getActivity()).create(person);
                updateUI();
                Toast.makeText(getActivity(), "Person created: " + person.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initDeleteAllButton(View view) {
        Button button = (Button) getView(view, R.id.button_deleteAll);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete all Person records
                int rowsAffected = DbHelper.instance(getActivity()).deleteAllPeople();
                updateUI();
                Toast.makeText(getActivity(), "All (" + rowsAffected + ") people deleted!", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * NPEs suck almost as much as null checks.
     * <p>
     * Returns the requested view by ID, or throws a RuntimeException if
     * the view can not be located.
     * <p>
     * But NEVER returns null! Goodbye, NPEs.
     *
     * @param parentView      The parent view, or rather, the view that
     *                        contains this view, if that helps.
     * @param requestedViewId The ID of the requested view
     * @return The requested View (you must cast this appropriately,
     * hey, I can't do everything)
     */
    private View getView(View parentView, int requestedViewId) {
        View ret = parentView.findViewById(requestedViewId);
        if (ret == null) {
            throw new RuntimeException("View with ID: " + requestedViewId + " could not be found!");
        }
        return ret;
    }
}
