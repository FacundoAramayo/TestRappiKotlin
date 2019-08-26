package com.facundoaramayo.testrappikotlin.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import com.facundoaramayo.testrappikotlin.ActivityMainPlaces;
import com.facundoaramayo.testrappikotlin.ActivityMaps;
import com.facundoaramayo.testrappikotlin.ActivityPlaceDetail;
import com.facundoaramayo.testrappikotlin.R;
import com.facundoaramayo.testrappikotlin.adapter.AdapterPlaceGrid;
import com.facundoaramayo.testrappikotlin.connection.RestAdapter;
import com.facundoaramayo.testrappikotlin.connection.callbacks.CallbackListPlace;
import com.facundoaramayo.testrappikotlin.data.Constant;
import com.facundoaramayo.testrappikotlin.data.DatabaseHandler;
import com.facundoaramayo.testrappikotlin.data.SharedPref;
import com.facundoaramayo.testrappikotlin.data.ThisApplication;
import com.facundoaramayo.testrappikotlin.model.Place;
import com.facundoaramayo.testrappikotlin.utils.PermissionUtil;
import com.facundoaramayo.testrappikotlin.utils.Tools;
import com.facundoaramayo.testrappikotlin.widget.SpacingItemDecoration;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import static com.facundoaramayo.testrappikotlin.utils.Tools.getLastKnownLocation;

public class FragmentCategory extends Fragment {

    public static String TAG_CATEGORY = "key.TAG_CATEGORY";

    private int results_found = 0;
    private int results_start = 0;
    private int results_shown = 0;

    private int category_id;

    private View root_view;
    private RecyclerView recyclerView;
    private View lyt_progress;
    private View lyt_not_found;
    private TextView text_progress;
    private Snackbar snackbar_retry;

    private DatabaseHandler db;
    private SharedPref sharedPref;
    private AdapterPlaceGrid adapter;

    private Call<CallbackListPlace> callback;
    List<Place> placesToInsert;

    private boolean onProcess = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_category, null);

        setHasOptionsMenu(true);

        db = new DatabaseHandler(getActivity());
        sharedPref = new SharedPref(getActivity());
        category_id = getArguments().getInt(TAG_CATEGORY);

        recyclerView = root_view.findViewById(R.id.recycler);
        lyt_progress = root_view.findViewById(R.id.lyt_progress);
        lyt_not_found = root_view.findViewById(R.id.lyt_not_found);
        text_progress = root_view.findViewById(R.id.text_progress);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(Tools.getGridSpanCount(getActivity()), StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new SpacingItemDecoration(Tools.getGridSpanCount(getActivity()), Tools.dpToPx(getActivity(), 4), true));

        adapter = new AdapterPlaceGrid(getActivity(), recyclerView, new ArrayList<Place>());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AdapterPlaceGrid.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Place obj) {
                ActivityPlaceDetail.navigate((ActivityMainPlaces) getActivity(), v.findViewById(R.id.lyt_content), obj);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView v, int state) {
                super.onScrollStateChanged(v, state);
                if (state == RecyclerView.SCROLL_STATE_DRAGGING || state == RecyclerView.SCROLL_STATE_SETTLING) {
                    ActivityMainPlaces.animateFab(true);
                } else {
                    ActivityMainPlaces.animateFab(false);
                }
            }
        });
        //startLoadMoreAdapter();
        return root_view;
    }

    @Override
    public void onDestroyView() {
        if (snackbar_retry != null) snackbar_retry.dismiss();
        if (callback != null && callback.isExecuted()) {
            callback.cancel();
        }
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (category_id != 0) {
            //TODO: implementar la nueva búsqueda por categoría
            Log.d("LOG-", "Nueva búsqueda");
            actionRefresh(1, category_id);



        }
        if (sharedPref.isRefreshPlaces() || db.getPlacesSize() == 0) {
            actionRefresh(sharedPref.getLastPlacePage(), 0);
            //startLoadMoreAdapter();
        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_category, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            ThisApplication.getInstance().setLocation(null);
            sharedPref.setLastPlacePage(1);
            sharedPref.setRefreshPlaces(true);
            text_progress.setText("");
            if (snackbar_retry != null) snackbar_retry.dismiss();
            actionRefresh(sharedPref.getLastPlacePage(), 0);
        }
        return super.onOptionsItemSelected(item);
    }

//    private void startLoadMoreAdapter() {
//        adapter.resetListData();
//        List<Place> items = db.getPlacesByPage(category_id, Constant.LIMIT_LOADMORE, 0);
//        adapter.insertData(items);
//        showNoItemView();
//        final int item_count = db.getPlacesSize(category_id);
//        adapter.setOnLoadMoreListener(new AdapterPlaceGrid.OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(final int current_page) {
//                if (item_count > adapter.getItemCount() && current_page != 0) {
//                    displayDataByPage(current_page);
//                } else {
//                    adapter.setLoaded();
//                }
//            }
//        });
//    }

    private void displayDataByPage(final int next_page) {
        adapter.setLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Place> items = db.getPlacesByPage(category_id, Constant.LIMIT_LOADMORE, (next_page * Constant.LIMIT_LOADMORE));
                adapter.insertData(items);
                showNoItemView();
            }
        }, 500);
    }

    private void actionRefresh(int page_no, int category) {
        boolean conn = Tools.checkConnection(getActivity());
        if (conn) {
            if (!onProcess) {
                onRefresh(page_no, category);
            } else {
                Snackbar.make(root_view, R.string.task_running, Snackbar.LENGTH_SHORT).show();
            }
        } else {
            onFailureRetry(page_no, getString(R.string.no_internet));
        }
    }




    private void onRefresh(final int page_no, int category) {
        onProcess = true;
        showProgress(onProcess);
        Location loc = getLastKnownLocation(getContext());
        if (loc != null){
            callback = RestAdapter.createAPI().getPlacesByPage(loc.getLatitude(),loc.getLongitude(),2,20, "real_distance", "asc");
        } else {
            callback = RestAdapter.createAPI().getPlacesByPage(40.28422,-84.1555,2,40, "real_distance", "asc");
        }
        callback.enqueue(new retrofit2.Callback<CallbackListPlace>() {
                @Override
                public void onResponse(Call<CallbackListPlace> call, Response<CallbackListPlace> response) {
                    CallbackListPlace resp = response.body();

                    Log.d("LOG-CONVERSION", "START");
                    placesToInsert = Tools.convertRestaurantContainerToPlace(resp.getRestaurants());

                    if (resp != null) {
                        results_found = resp.getResults_shown();
                        if (page_no == 1) db.refreshTablePlace();
                        db.insertListPlace(placesToInsert);  // save result into database
                        sharedPref.setLastPlacePage(page_no + 1);
                        delayNextRequest(page_no);
                        String str_progress = String.format(getString(R.string.load_of), (page_no * Constant.LIMIT_PLACE_REQUEST), results_found);
                        text_progress.setText(str_progress);

                        adapter.setLoading();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //List<Place> items = db.getPlacesByPage(category_id, Constant.LIMIT_LOADMORE, (next_page * Constant.LIMIT_LOADMORE));
                                adapter.insertData(placesToInsert);
                                showNoItemView();
                            }
                        }, 500);

                    } else {
                        onFailureRetry(page_no, getString(R.string.refresh_failed));
                    }
                }

                @Override
                public void onFailure(Call<CallbackListPlace> call, Throwable t) {
                    if (call != null && !call.isCanceled()) {
                        Log.e("onFailure", t.getMessage());
                        boolean conn = Tools.checkConnection(getActivity());
                        if (conn) {
                            onFailureRetry(page_no, getString(R.string.refresh_failed));
                        } else {
                            onFailureRetry(page_no, getString(R.string.no_internet));
                        }
                    }
                }
            });

    }

    private void showProgress(boolean show) {
        if (show) {
            lyt_progress.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            lyt_not_found.setVisibility(View.GONE);
        } else {
            lyt_progress.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void showNoItemView() {
        if (adapter.getItemCount() == 0) {
            lyt_not_found.setVisibility(View.VISIBLE);
        } else {
            lyt_not_found.setVisibility(View.GONE);
        }
    }

    private void onFailureRetry(final int page_no, String msg) {
        onProcess = false;
        showProgress(onProcess);
        showNoItemView();
        //startLoadMoreAdapter();
        snackbar_retry = Snackbar.make(root_view, msg, Snackbar.LENGTH_INDEFINITE);
        snackbar_retry.setAction(R.string.RETRY, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionRefresh(page_no, 0);
            }
        });
        snackbar_retry.show();
    }

    private void delayNextRequest(final int page_no) {
        if (results_found == 0) {
            onFailureRetry(page_no, getString(R.string.refresh_failed));
            return;
        }
        if ((page_no * Constant.LIMIT_PLACE_REQUEST) > results_found) { // when all data loaded
            onProcess = false;
            showProgress(onProcess);
            //startLoadMoreAdapter();
            sharedPref.setRefreshPlaces(false);
            text_progress.setText("");
            Snackbar.make(root_view, R.string.load_success, Snackbar.LENGTH_LONG).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onRefresh(page_no + 1, 0);
            }
        }, 500);
    }
}
