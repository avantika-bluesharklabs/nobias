package com.nobias.businesslogic.bindingadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nobias.R;
import com.nobias.businesslogic.interactors.ObservableString;
import com.nobias.businesslogic.interfaces.OnClickConsultants;
import com.nobias.businesslogic.interfaces.OnClickPastConsultant;
import com.nobias.businesslogic.interfaces.OnClickSpecialities;
import com.nobias.businesslogic.interfaces.OnClickUpcomingConsultant;
import com.nobias.businesslogic.pojo.PojoPastConsultants;
import com.nobias.businesslogic.pojo.PojoSpeciality;
import com.nobias.businesslogic.pojo.PojoUser;
import com.nobias.utils.CustomSpannableTypeface;
import com.nobias.utils.Utils;
import com.nobias.utils.UtilsDate;
import com.nobias.view.adapter.AdapterConsultants;
import com.nobias.view.adapter.AdapterPastConsultant;
import com.nobias.view.adapter.AdapterSetSpecialities;
import com.nobias.view.adapter.AdapterSpinner;
import com.nobias.view.adapter.AdapterUpcomingConsultant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brij Dholakia on Jul, 10 2018 16:20.
 * <p>
 * Custom bindings for application
 */
public class BindingAdapterApp {

    /**
     * Shows snack bar
     *
     * @param viewLayout         Layout relative to which snack bar is to be shown
     * @param snackMessageInt    Message resource to be shown in snack bar
     * @param snackMessageString Message to be shown in snack bar
     */
    @BindingAdapter(value = {"showSnackBarInt", "showSnackBarString"}, requireAll = false)
    public static void showSnackBar(View viewLayout, ObservableInt snackMessageInt,
                                    ObservableString snackMessageString) {
        String message = "";
        if (snackMessageString != null && !TextUtils.isEmpty(snackMessageString.getTrimmed())) {
            message = snackMessageString.getTrimmed();
            snackMessageString.set("");
        } else if (snackMessageInt != null && snackMessageInt.get() != 0) {
            message = viewLayout.getResources().getString(snackMessageInt.get());
            snackMessageInt.set(0);
        }

        if (!TextUtils.isEmpty(message)) {
            Utils.showSnackBar(viewLayout, message);
        }
    }

    @BindingAdapter({"setEnabledSwipeRefresh", "refreshListener"})
    public static void setEnabledSwipeRefresh(SwipeRefreshLayout swipeRefresh, boolean isEnabled,
                                              SwipeRefreshLayout.OnRefreshListener
                                                      refreshListener) {
        if (swipeRefresh != null) {
            swipeRefresh.setEnabled(isEnabled);
            swipeRefresh.setOnRefreshListener(refreshListener);
        }
    }

    @BindingAdapter({"setSwipeRefreshing"})
    public static void setSwipeRefreshing(SwipeRefreshLayout swipeRefresh, boolean toRefresh) {
        if (swipeRefresh != null) {
            swipeRefresh.setRefreshing(toRefresh);
        }
    }

    /**
     * Sets error for textinput layout
     *
     * @param textInputLayout Textinputlayout reference
     * @param errorInt        Error message to be shown
     */
    @BindingAdapter(value = {"setError", "setTILErrorString"}, requireAll = false)
    public static void setError(TextInputLayout textInputLayout, int errorInt,
                                String errorMessage) {
        if (errorInt != 0) {
            textInputLayout.setError(textInputLayout.getContext().getResources().getString
                    (errorInt));
        } else if (!TextUtils.isEmpty(errorMessage)) {
            textInputLayout.setError(errorMessage);
        }
    }

    /**
     * Sets error for textinput layout
     *
     * @param spinner  Textinputlayout reference
     * @param errorInt Error message to be shown
     */
    @BindingAdapter(value = {"setError", "setTILErrorString"}, requireAll = false)
    public static void setError(Spinner spinner, int errorInt,
                                String errorMessage) {
        if (errorInt != 0) {
            ((TextView) spinner.getSelectedView()).setError(spinner.getContext().getResources().getString
                    (errorInt));
        } else if (!TextUtils.isEmpty(errorMessage)) {
            ((TextView) spinner.getSelectedView()).setError(errorMessage);
        }
    }

    /**
     * Sets enable flag for textinput layout
     *
     * @param textInputLayout Textinputlayout reference
     * @param isEnabled       Whether view is enabled or not
     */
    @BindingAdapter({"setEnabledTIL"})
    public static void setTextInputEnabled(TextInputLayout textInputLayout, boolean isEnabled) {
        textInputLayout.setEnabled(isEnabled);
    }

    @BindingAdapter({"passwordTransformation"})
    public static void setTextInputPasswordTransformation(TextInputEditText textInputEditText,
                                                          boolean check) {
        if (check) {
            textInputEditText.setTransformationMethod(new PasswordTransformationMethod());
        }
    }

    @BindingAdapter(value = {"glideStream", "glideUrl", "glideFile", "glideResource",
            "glideRequestOptions"}, requireAll = false)
    public static void glideStreamUrlFile(ImageView view, ByteArrayOutputStream stream,
                                          String url, File file, int resource,
                                          RequestOptions requestOptions) {
        if (view != null) {
            Context context = view.getContext();
            RequestOptions options = requestOptions != null ? requestOptions : new RequestOptions();
            if (resource > 0) {
                view.setImageResource(resource);
            } else if (file != null) {
                Glide.with(context).load(file).apply(options).into(view);
            } else if (stream != null) {
                Glide.with(context).asBitmap().load(stream.toByteArray()).apply(options).into(view);
            } else if (!TextUtils.isEmpty(url)) {
                Glide.with(context).load(url).apply(options).into(view);
            } else {
                Glide.with(context).load("").apply(options).into(view);
            }
        }
    }

   /* @BindingAdapter({"navigationViewHeader", "navigationViewHeaderClick",
            "navigationViewItemClick"})
    public static void loadNavigationHeader(NavigationView navigationView,
                                            ViewModelMain vmMain,
                                            OnClickNavHeader onClickNavHeader,
                                            NavigationView.OnNavigationItemSelectedListener
                                                    itemSelectedListener) {
        if (navigationView != null) {
            LayoutNavheaderBinding binding = LayoutNavheaderBinding.inflate(LayoutInflater.from
                    (navigationView.getContext()));
            binding.setVmMain(vmMain);
            binding.setOnClickNavHeader(onClickNavHeader);
            binding.setRequestOptions(((MyApplication) navigationView.getContext()
                    .getApplicationContext()).glideCenterCircle());
            navigationView.addHeaderView(binding.getRoot());
            navigationView.setNavigationItemSelectedListener(itemSelectedListener);
        }
    }*/

    @BindingAdapter(value = {"arrayStringData", "arrayData", "arraySpeciality", "arrayTime", "arrayPosition"}, requireAll = false)
    public static void setSpinner(Spinner spinner, String[] arrayData, List<String> arrayList,
                                  List<PojoSpeciality> arraySpeciality,
                                  List<String> arrayTime,
                                  int position) {
        if (spinner != null) {
            if (arrayData != null) {
                spinner.setAdapter(new AdapterSpinner(spinner.getContext(), R.layout.spinner_rows,
                        arrayData));
            } else {
                List<String> arrayString = new ArrayList<>();
                if (arrayList != null) {
                    arrayString.addAll(arrayList);
                } else if (arraySpeciality != null) {
                    for (int i = 0; i < arraySpeciality.size(); i++) {
                        String category = "";
                        if (!TextUtils.isEmpty(arraySpeciality.get(i).getCategory()))
                            category = arraySpeciality.get(i).getCategory();
                        if (!TextUtils.isEmpty(arraySpeciality.get(i).getName()))
                            category += " - " + arraySpeciality.get(i).getName();
                        arrayString.add(category);
                    }
                } else if (arrayTime != null) {
                    for (int i = 0; i < arrayTime.size(); i++) {
                        arrayString.add(UtilsDate.get12hourTimeFormat(arrayTime.get(i)));
                    }
                }
                spinner.setAdapter(new AdapterSpinner(spinner.getContext(), R.layout.spinner_rows,
                        arrayString));
            }
            spinner.setSelection(position);
        }
    }

    /*@BindingAdapter({"recyclerManagerFormList", "adapterFormList", "clickListenerFormList",
            "scrollListenerFormList", "adapterFilterFormList", "adapterFilterFormListString",
            "adapterIsForFilterFormList"})
    public static void setRecyclerViewAdvFormList(RecyclerView recyclerView,
                                                  LinearLayoutManager layoutManager,
                                                  List<PojoFormList.Submission> contents,
                                                  OnClickFormList onClickContent,
                                                  RecyclerView.OnScrollListener onScrollListener,
                                                  List<PojoFormList.Submission> contentsSearch,
                                                  String filterString,
                                                  boolean isForFilter) {
        if (recyclerView != null) {
            if (recyclerView.getAdapter() == null) {
                recyclerView.setLayoutManager(layoutManager);
                AdapterFormList adapterContent = new AdapterFormList(recyclerView.getContext(),
                        contents, onClickContent);
                recyclerView.setAdapter(adapterContent);
                if (onScrollListener != null) {
                    recyclerView.addOnScrollListener(onScrollListener);
                }
            } else {
                if (isForFilter) {
                    ((AdapterFormList) recyclerView.getAdapter()).filter(filterString,
                            contentsSearch);
                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    }*/

    /**
     * Sets enable flag for spinner
     *
     * @param spinner   Spinner reference
     * @param isEnabled Whether view is enabled or not
     */
    @BindingAdapter({"spinnerEnabled"})
    public static void setSpinnerEnabled(Spinner spinner, boolean isEnabled) {
        if (spinner != null) {
            spinner.setEnabled(isEnabled);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @BindingAdapter({"textInputEditTextMultiLineParentScroll"})
    public static void setTextInputEditTextScroll(TextInputEditText editText, boolean isToScroll) {
        if (editText != null && isToScroll) {
            editText.setOnTouchListener((view, motionEvent) -> {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                if ((motionEvent.getAction() & MotionEvent.ACTION_MASK) == MotionEvent
                        .ACTION_UP) {
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            });
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @BindingAdapter({"editTextMultiLineParentScroll"})
    public static void setEditTextScroll(EditText editText, boolean isToScroll) {
        if (editText != null && isToScroll) {
            editText.setOnTouchListener((view, motionEvent) -> {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                if ((motionEvent.getAction() & MotionEvent.ACTION_MASK) == MotionEvent
                        .ACTION_UP) {
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            });
        }
    }

    @BindingAdapter({"loadText"})
    public static void loadText(WebView view, String text) {
        if (!TextUtils.isEmpty(text))
            view.loadData("<html><body style=\"text-align:justify;\">" + text + "</html></body>", "text/html", "UTF-8");
    }

    @BindingAdapter({"loadUrl"})
    public static void loadUrl(WebView view, String url) {
        if (!TextUtils.isEmpty(url))
            view.loadUrl(url);
    }

    @BindingAdapter({"floatingRecycler"})
    public static void setFloatingRecycler(RecyclerView recyclerView, boolean isToFloat) {
        if (recyclerView != null) {
            recyclerView.setClipToPadding(!isToFloat);
            recyclerView.setPadding(0, 0, 0, isToFloat ? recyclerView.getContext().getResources()
                    .getDimensionPixelSize(R.dimen.margin_eighty) : 0);
        }
    }

    @BindingAdapter({"recyclerManagerUpcomingConsultant", "adapterUpcomingConsultant", "clickListenerUpcomingConsultant",
            "scrollListenerUpcomingConsultant"})
    public static void setRecyclerViewUpcomingConsultantAdapter(RecyclerView recyclerView,
                                                                LinearLayoutManager layoutManager,
                                                                List<PojoPastConsultants> contents,
                                                                OnClickUpcomingConsultant onClickContent,
                                                                RecyclerView.OnScrollListener onScrollListener) {
        if (recyclerView != null) {
            if (recyclerView.getAdapter() == null) {
                recyclerView.setLayoutManager(layoutManager);
                AdapterUpcomingConsultant adapterContent = new AdapterUpcomingConsultant(
                        recyclerView.getContext(), contents, onClickContent);
                recyclerView.setAdapter(adapterContent);
                if (onScrollListener != null) {
                    recyclerView.addOnScrollListener(onScrollListener);
                }
            } else {
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    }

    @BindingAdapter({"recyclerManagerSetSpeciality", "adapterSetSpeciality", "clickListenerSetSpeciality",
            "scrollListenerSetSpeciality"})
    public static void setRecyclerViewSetSpecialityAdapter(RecyclerView recyclerView,
                                                           LinearLayoutManager layoutManager,
                                                           List<PojoSpeciality> contents,
                                                           OnClickSpecialities onClickContent,
                                                           RecyclerView.OnScrollListener onScrollListener) {
        if (recyclerView != null) {
            if (recyclerView.getAdapter() == null) {
                recyclerView.setLayoutManager(layoutManager);
                AdapterSetSpecialities adapterContent = new AdapterSetSpecialities(recyclerView.getContext(), contents,
                        onClickContent);
                recyclerView.setAdapter(adapterContent);
                if (onScrollListener != null) {
                    recyclerView.addOnScrollListener(onScrollListener);
                }
            } else {
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    }


    @BindingAdapter({"recyclerManagerPastConsultant", "adapterPastConsultant", "clickListenerPastConsultant",
            "scrollListenerPastConsultant"})
    public static void setRecyclerViewPastConsultantAdapter(RecyclerView recyclerView,
                                                            LinearLayoutManager layoutManager,
                                                            List<PojoPastConsultants> contents,
                                                            OnClickPastConsultant onClickContent,
                                                            RecyclerView.OnScrollListener onScrollListener) {
        if (recyclerView != null) {
            if (recyclerView.getAdapter() == null) {
                recyclerView.setLayoutManager(layoutManager);
                AdapterPastConsultant adapterContent = new AdapterPastConsultant(
                        recyclerView.getContext(), contents, onClickContent);
                recyclerView.setAdapter(adapterContent);
                if (onScrollListener != null) {
                    recyclerView.addOnScrollListener(onScrollListener);
                }
            } else {
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    }

    @BindingAdapter({"recyclerManagerConsultants", "adapterConsultants", "clickListenerConsultants",
            "scrollListenerConsultants"})
    public static void setRecyclerViewConsultantsAdapter(RecyclerView recyclerView,
                                                         LinearLayoutManager layoutManager,
                                                         List<PojoUser> contents,
                                                         OnClickConsultants onClickContent,
                                                         RecyclerView.OnScrollListener onScrollListener) {
        if (recyclerView != null) {
            if (recyclerView.getAdapter() == null) {
                recyclerView.setLayoutManager(layoutManager);
                AdapterConsultants adapterContent = new AdapterConsultants(recyclerView.getContext(), contents,
                        onClickContent);
                recyclerView.setAdapter(adapterContent);
                if (onScrollListener != null) {
                    recyclerView.addOnScrollListener(onScrollListener);
                }
            } else {
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    }

    @BindingAdapter({"nestedScroll"})
    public static void setNestedScroll(NestedScrollView scrollView, NestedScrollView
            .OnScrollChangeListener onScrollChangeListener) {
        if (scrollView != null) {
            scrollView.setOnScrollChangeListener(onScrollChangeListener);
        }
    }

    @BindingAdapter({"setTextHtml"})
    public static void setTextHtml(TextView textView, String text) {
        if (textView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                textView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
            } else {
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                textView.setText(Html.fromHtml(text));
            }
        }
    }

    @BindingAdapter({"regularText", "boldText"})
    public static void setFont(TextView view, String regularText, String boldText) {
        if (view != null) {
            SpannableStringBuilder SS = new SpannableStringBuilder(regularText + " " + boldText);
            if (TextUtils.equals(regularText, view.getContext().getResources().getString(R.string.app_no)) &&
                    TextUtils.equals(boldText, view.getContext().getResources().getString(R.string.app_bias))) {
                SS = new SpannableStringBuilder(regularText + boldText);
            }
            Typeface regularFont = ResourcesCompat.getFont(view.getContext(), R.font.hypatiasanspro_regular);
            Typeface boldFont = ResourcesCompat.getFont(view.getContext(), R.font.hypatiasanspro_bold);
            SS.setSpan(new CustomSpannableTypeface("", regularFont), 0, regularText.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            SS.setSpan(new CustomSpannableTypeface("", boldFont), regularText.length(), SS.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            view.setText(SS);
        }
    }

    @BindingAdapter({"setRating"})
    public static void setRating(RatingBar view, String rating) {
        if (view != null) {
            if (!TextUtils.isEmpty(rating))
                view.setRating(Integer.parseInt(rating));
            else
                view.setRating(3);
        }
    }

    /* @BindingAdapter({"recyclerManagerDashboard", "adapterDashboard", "clickListenerDashboard"})
     public static void setRecyclerDashboard(RecyclerView recyclerView,
                                             GridLayoutManager layoutManager,
                                             List<PojoDashboard> contents,
                                             OnClickDashboard onClickDashboard) {
         if (recyclerView != null) {
             if (recyclerView.getAdapter() == null) {
                 recyclerView.setLayoutManager(layoutManager);
                 AdapterDashboard adapterDashboard = new AdapterDashboard(contents,
                         onClickDashboard);
                 recyclerView.setAdapter(adapterDashboard);
             } else {
                 recyclerView.getAdapter().notifyDataSetChanged();
             }
         }
     }
 */
    @BindingAdapter({"addViewToLinear"})
    public static void addToLinearLayout(LinearLayout linearLayout, List<View> view) {
        if (linearLayout != null && view != null && !view.isEmpty()) {
            linearLayout.removeAllViews();
            for (int i = 0; i < view.size(); i++) {
                linearLayout.addView(view.get(i));
            }
        }
    }

    @BindingAdapter({"scrollToPositionScroll"})
    public static void scrollToPositionScroll(ScrollView scrollView, int position) {
        if (scrollView != null) {
            scrollView.smoothScrollTo(0, position);
        }
    }

    @BindingAdapter({"setTextInt", "setTextString", "setTextIntFirst"})
    public static void scrollToPositionScroll(TextView textView, int contentInt,
                                              String contentString, boolean isIntFirst) {
        if (textView != null) {
            String dataInt = "";
            String data = "";
            if (contentInt != 0) {
                dataInt = textView.getContext().getResources().getString(contentInt);
            }
            if (!TextUtils.isEmpty(contentString)) {
                if (!TextUtils.isEmpty(dataInt)) {
                    data = isIntFirst ? (dataInt + " " + contentString) :
                            (contentString + " " + dataInt);
                } else {
                    data = contentString;
                }
            } else {
                if (!TextUtils.isEmpty(dataInt)) {
                    data = dataInt;
                }
            }
            textView.setText(data);
        }
    }


}