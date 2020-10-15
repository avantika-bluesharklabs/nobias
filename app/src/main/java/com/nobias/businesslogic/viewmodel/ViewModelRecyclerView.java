package com.nobias.businesslogic.viewmodel;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.request.RequestOptions;
import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.interactors.ObservableString;
import com.nobias.businesslogic.pojo.PojoCommonResponse;
import com.nobias.businesslogic.preferences.AppSharedPreferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nobias.utils.constants.ConstantCodes.TOTAL_RECORDS;

/**
 * Created by Brij Dholakia on Sep, 18 2018 09:50.
 * <p>
 * View model for recyclerview
 */
public abstract class ViewModelRecyclerView<X, T> {

    protected MyApplication mApplication;
    protected AppSharedPreferences mPreferences;
    public ObservableBoolean observerProgressBar = new ObservableBoolean(false);
    public LinearLayoutManager layoutManager;
    protected int mPageIndex = 1;
    private int mTotalCount = 0;
    protected boolean mIsClearData = false;
    private boolean mIsFromLastItem = false;
    private boolean mShowCentreProgress = true;
    private boolean mIsOfflineEnabled;
    public ObservableBoolean observerEnabledSwipeRefresh = new ObservableBoolean(true);
    public ObservableBoolean observerSwipeRefreshing = new ObservableBoolean(false);
    public ObservableInt observerSnackBarInt = new ObservableInt();
    public ObservableString observerSnackBarString = new ObservableString("");
    public ObservableInt observerNoRecords = new ObservableInt(R.string.text_norecordfound);
    public ObservableBoolean observerIsFilterActive = new ObservableBoolean(false);
    public ObservableString observerFilterString = new ObservableString("");
    public RequestOptions requestOptionCentreProfilePic;
    public ObservableList<T> observerContent = new ObservableArrayList<>();
    public ObservableList<T> observerContentSearch = new ObservableArrayList<>();

    public ViewModelRecyclerView(MyApplication application, boolean isOfflineEnabled) {
        mApplication = application;
        mPreferences = mApplication.getAppComponent().getPreferences();
        mIsOfflineEnabled = isOfflineEnabled;
        layoutManager = new LinearLayoutManager(mApplication);
        requestOptionCentreProfilePic = application.glideCenterCircle(R.drawable.placeholder_circle_small);
    }

    public <Y> boolean isNotNullEmptyRandom(List<Y> array) {
        return array != null && !array.isEmpty();
    }

    private boolean isNotNullEmpty(ObservableList<T> array) {
        return array != null && !array.isEmpty();
    }

    /**
     * Updates parameters for on scroll
     */
    private void updateOnScroll() {
        mIsFromLastItem = true;
        mShowCentreProgress = false;
        mIsClearData = false;
        mPageIndex++;
    }

    /**
     * Updates parameters for on refresh
     */
    private void updateOnRefresh() {
        mIsFromLastItem = false;
        mShowCentreProgress = false;
        mIsClearData = true;
    }

    /**
     * Updates parameters for network call
     */
    private void updateRefreshContents() {
        if (mShowCentreProgress) {
            observerProgressBar.set(true);
        }
        if (mIsClearData) {
            mPageIndex = 1;
        } else if (mIsFromLastItem) {
            observerContent.add(null);
        }
    }

    /**
     * Checks for onScroll possibility
     *
     * @param dy Difference in Y for the scroll
     * @return Possibility of scroll
     */
    private boolean checkForScroll(int dy) {
        int totalItemCount = layoutManager.getItemCount();
        int pastVisibleItems = layoutManager.findLastCompletelyVisibleItemPosition();
        return dy > 0 && !observerProgressBar.get() && totalItemCount < mTotalCount &&
                (mPageIndex * TOTAL_RECORDS <= (pastVisibleItems + 2));
    }

    /**
     * Scroll change listener for nested scroll view
     */
    public NestedScrollView.OnScrollChangeListener scrollChangeListener =
            (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (v.getChildAt(v.getChildCount() - 1) != null &&
                        (scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v
                                .getMeasuredHeight())) && scrollY > oldScrollY && checkForScroll
                        (1)) {
                    onScroll();
                }
            };

    /**
     * Scroll listener for recycler view
     */
    public RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            observerEnabledSwipeRefresh.set(layoutManager.findFirstCompletelyVisibleItemPosition
                    () <= 0);
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (checkForScroll(dy)) {
                onScroll();
            }
        }
    };

    /**
     * Takes necessary action for onScroll
     */
    private void onScroll() {
        if (mApplication.isInternetConnected()) {
            updateOnScroll();
            fetchDataList();
        } else {
            observerSnackBarInt.set(R.string.message_noconnection);
        }
    }

    /**
     * Refresh listener for swipe to refresh
     */
    public SwipeRefreshLayout.OnRefreshListener refreshListener = this::refreshContent;

    /**
     * Refreshes content for the list depending on the connectivity
     * <p>
     * If connected then from api, else gets data offline
     */
    public void refreshContent() {
        observerSwipeRefreshing.set(true);
        if (mApplication.isInternetConnected()) {
            if (!observerProgressBar.get()) {
                refreshListUpdate();
                updateOnRefresh();
                fetchDataList();
            } else {
                observerSwipeRefreshing.set(false);
            }
        } else {
            observerSwipeRefreshing.set(false);
            offlineDataList();
        }
    }

    public abstract void refreshListUpdate();

    public abstract void networkCallList();

    /**
     * Takes care of data fetch, if net connected then network call else checks for offline is
     * enabled or not, if enabled then takes data from database else shows no connection
     */
    public void fetchDataList() {
        if (mApplication.isInternetConnected()) {
            updateRefreshContents();
            networkCallList();
        } else {
            if (mIsOfflineEnabled) {
                offlineDataList();
            } else {
                observerSnackBarInt.set(R.string.message_noconnection);
            }

        }
    }

    public abstract void offlineDataList();

    public abstract void sendResponseBodyList(X list);

    /**
     * Callback for network call
     */
    protected Callback<X> mCallbackList = new Callback<X>() {
        @Override
        public void onResponse(Call<X> call, Response<X> response) {
            removeFooter();
            if (response != null && response.isSuccessful() && response.body() != null) {
                X body = response.body();
                PojoCommonResponse commonResponse = (PojoCommonResponse) body;
                if (commonResponse.getSuccess() || commonResponse.getMessage().startsWith("This appointment"))
                {
                    //clearDataOnResponse();
                   // mTotalCount = commonResponse.getTotalCount();
                    sendResponseBodyList(response.body());
                } else {
                    observerSnackBarInt.set(R.string.message_something_wrong);
                }
            } else {
                observerSnackBarInt.set(R.string.message_something_wrong);
            }
            observerProgressBar.set(false);
            observerSwipeRefreshing.set(false);
        }

        @Override
        public void onFailure(Call<X> call, Throwable t) {
            removeFooter();
            observerSnackBarInt.set(R.string.message_something_wrong);
            observerProgressBar.set(false);
            observerSwipeRefreshing.set(false);
        }

        /**
         * Clears data on response success if its is to be cleared
         */
        private void clearDataOnResponse() {
            if (mIsClearData) {
                mTotalCount = 0;
                observerContent.clear();
                observerContentSearch.clear();
            }
        }

        /**
         * Removes footer depending on the last item in contention or not
         */
        private void removeFooter() {
            if (mIsFromLastItem && isNotNullEmpty(observerContent)) {
                observerContent.remove(observerContent.size() - 1);
            }
        }
    };

    public void setProgressBar(Boolean visible)
    {
        observerProgressBar.set(visible);
    }
}