package com.level_sense.app.Fragment;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.level_sense.app.R;
import com.level_sense.app.view.ScreenRouter;
import com.level_sense.app.view.cart.CartClickActionEvent;
import com.level_sense.app.view.collections.CollectionListView;
import com.level_sense.app.view.collections.CollectionListViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GetHardwareFragment extends Fragment {
    @BindView(R.id.collection_list)
    CollectionListView collectionListView;

    public GetHardwareFragment() {
    }

    //TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static GetHardwareFragment newInstance() {
        GetHardwareFragment fragment = new GetHardwareFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_collection_list, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        collectionListView.bindViewModel(ViewModelProviders.of(this).get(CollectionListViewModel.class));
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dash_board,menu);
        MenuItem menuItem=menu.findItem(R.id.cart);
        menuItem.setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.cart){
            ScreenRouter.route(getActivity(), new CartClickActionEvent());

        }
        return super.onOptionsItemSelected(item);
    }


}


