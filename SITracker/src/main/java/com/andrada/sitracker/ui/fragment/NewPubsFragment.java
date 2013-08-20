/*
 * Copyright 2013 Gleb Godonoga.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andrada.sitracker.ui.fragment;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;
import android.widget.ListView;

import com.andrada.sitracker.R;
import com.andrada.sitracker.ui.fragment.adapters.NewPubsAdapter;
import com.andrada.sitracker.util.actionmodecompat.ActionMode;
import com.andrada.sitracker.util.actionmodecompat.MultiChoiceModeListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;


@EFragment(R.layout.fragment_listview_with_empty)
@OptionsMenu(R.menu.newpubs_menu)
public class NewPubsFragment extends Fragment implements MultiChoiceModeListener {

    @ViewById
    ListView list;

    @ViewById
    ViewStub empty;

    @Bean
    NewPubsAdapter adapter;

    @AfterViews
    void bindAdapter() {
        list.setAdapter(adapter);
        ActionMode.setMultiChoiceMode(list, getActivity(), this);
        list.setBackgroundResource(R.drawable.authors_list_background);
        empty.setLayoutResource(R.layout.empty_newpubs);
        list.setEmptyView(empty);
    }

    @OptionsItem(R.id.action_markread)
    void markAsReadSelected() {

    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}
