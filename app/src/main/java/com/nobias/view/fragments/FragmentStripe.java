package com.nobias.view.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.nobias.R;
import com.nobias.businesslogic.pojo.PojoSaveRequest;
import com.nobias.businesslogic.viewmodel.fragments.ViewModelStripe;
import com.nobias.databinding.FragmentStripeBinding;
import com.nobias.utils.Utils;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.CustomerSession;
import com.stripe.android.EphemeralKeyProvider;
import com.stripe.android.EphemeralKeyUpdateListener;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.PaymentSession;
import com.stripe.android.PaymentSessionConfig;
import com.stripe.android.PaymentSessionData;
import com.stripe.android.Stripe;
import com.stripe.android.model.CardBrand;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.ShippingInformation;
import com.stripe.android.view.BillingAddressFields;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

/**
 * Created by Smit Shah on Aug 22 2018, 11:38 AM
 * <p>
 * Fragment for attendance log
 */
public class FragmentStripe extends FragmentBase {
    private ViewModelStripe mVMCreditCardInfo;
    private View mParentView;
    private PojoSaveRequest mPojoSaveRequest;
    private PaymentSession paymentSession;
    private ImageView mImgCardType;
    private PaymentMethod paymentMethod;
    private int mRequestCode = 0;
    private String roomName = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentStripeBinding binding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_stripe, container, false);
        mVMCreditCardInfo = new ViewModelStripe(mApplication);
        mVMCreditCardInfo.observerIsCheckout.set(getArguments().getBoolean(getString(R.string.bundleIsCheckout)));
        mVMCreditCardInfo.appointmentID=(getArguments().getString(getString(R.string.bundleAppointmentId)));
        roomName = getArguments().getString(getString(R.string.bundleRoomName));
        if (!mVMCreditCardInfo.observerIsCheckout.get())
            mPojoSaveRequest = (PojoSaveRequest) getArguments().getSerializable(getString(R.string.bundleSaveRequest));

        binding.setVmCreditCardInfo(mVMCreditCardInfo);
        mParentView = binding.getRoot();
        mImgCardType = (ImageView) mParentView.findViewById(R.id.imgCardType);
        observerEvents();
        EphemeralKeyProvider keyProvider = new EphemeralKeyProvider() {
            @Override
            public void createEphemeralKey(@NotNull String s, @NotNull EphemeralKeyUpdateListener ephemeralKeyUpdateListener) {
                String rawJson = new Gson().toJson(mApplication.getmPojoEphemeralKey());
                ephemeralKeyUpdateListener.onKeyUpdate(rawJson);
            }
        };
        CustomerSession.initCustomerSession(mMainActivity, keyProvider);
        paymentSession = new PaymentSession(this,
                new PaymentSessionConfig.Builder()
                        // collect shipping information
                        .setShippingInfoRequired(false)
                        // collect shipping method
                        .setShippingMethodsRequired(false)
                        // specify the payment method types that the customer can use;
                        // defaults to PaymentMethod.Type.Card
                        .setPaymentMethodTypes(
                                Arrays.asList(PaymentMethod.Type.Card)
                        )
                        // only allow US and Canada shipping addresses
                        .setAllowedShippingCountryCodes(new HashSet<>(
                                Arrays.asList("US", "CA")
                        ))
                        .setPrepopulatedShippingInfo(getDefaultShippingInfo())
                        .setBillingAddressFields(BillingAddressFields.Full)
                        .setShouldShowGooglePay(false)
                        .build());
        setupPaymentSession();
        return binding.getRoot();
    }

    /**
     * Observes events provided by live data single events from login and forgot password view
     * models
     */
    private void observerEvents() {
        mVMCreditCardInfo.getLiveEventSave().observe(this, aVoid -> {
            Utils.hideKeyboard(mMainActivity);
            if (mVMCreditCardInfo.observerIsCheckout.get()) {
                mRequestCode = 1234;
                mVMCreditCardInfo.observerProgressBar.set(true);
                startCheckout();
            } else {
                Fragment fragment = new FragmentConsultantSaveRequest();
                Bundle bundle = new Bundle();
                bundle.putSerializable(getString(R.string.bundleSaveRequest), mPojoSaveRequest);
                fragment.setArguments(bundle);
                mMainActivity.addFragment(fragment, R.string.tagFragmentConsultantSaveRequest, R.string.tagFragmentConsultantSaveRequest);
            }
        });

        mVMCreditCardInfo.getLiveEventSelectPaymentMethod().observe(this, aVoid -> {
            Utils.hideKeyboard(mMainActivity);
            paymentSession.presentPaymentMethodSelection("");
        });
        mVMCreditCardInfo.getSingleLiveEventTwilio().observe(this, aVoid -> {
            Utils.hideKeyboard(mMainActivity);
            callTwilio();
        });
    }

    private void callTwilio() {
        Fragment fragment = new FragmentTwilio();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.bundleTwilioRoomName), roomName);
        bundle.putBoolean(getString(R.string.bundleTwilioFromStripe), true);
        fragment.setArguments(bundle);
        mMainActivity.addFragment(fragment, R.string.tagFragmentTwilio, R.string.tagFragmentTwilio);
    }


    private void setupPaymentSession() {
        paymentSession.init(new PaymentSession.PaymentSessionListener() {
                                @Override
                                public void onCommunicatingStateChanged(boolean isCommunicating) {
                                    // update UI, such as hiding or showing a progress bar
                                    mVMCreditCardInfo.observerProgressBar.set(isCommunicating);
                                }

                                @Override
                                public void onError(int errorCode, @Nullable String errorMessage) {
                                    mVMCreditCardInfo.observerSnackBarString.set(errorMessage);
                                }

                                @Override
                                public void onPaymentSessionDataChanged(@NonNull PaymentSessionData data) {
                                    paymentMethod = data.getPaymentMethod();
                                    // use paymentMethod
                                    if (paymentMethod != null) {
                                        mVMCreditCardInfo.observableCardNumber.set(paymentMethod.card.last4);
                                        mVMCreditCardInfo.observerCardAvailable.set(true);
                                        mVMCreditCardInfo.observableCardType.set(paymentMethod.card.brand);
                                        if (!TextUtils.isEmpty(paymentMethod.card.brand)) {
                                            if (TextUtils.equals(paymentMethod.card.brand.toLowerCase(), CardBrand.Visa.getDisplayName().toLowerCase()))
                                                mImgCardType.setImageResource(CardBrand.Visa.getIcon());
                                            else if (TextUtils.equals(paymentMethod.card.brand.toLowerCase(), CardBrand.AmericanExpress.getDisplayName().toLowerCase()))
                                                mImgCardType.setImageResource(CardBrand.AmericanExpress.getIcon());
                                            else if (TextUtils.equals(paymentMethod.card.brand.toLowerCase(), CardBrand.DinersClub.getDisplayName().toLowerCase()))
                                                mImgCardType.setImageResource(CardBrand.DinersClub.getIcon());
                                            else if (TextUtils.equals(paymentMethod.card.brand.toLowerCase(), CardBrand.Discover.getDisplayName().toLowerCase()))
                                                mImgCardType.setImageResource(CardBrand.Discover.getIcon());
                                            else if (TextUtils.equals(paymentMethod.card.brand.toLowerCase(), CardBrand.JCB.getDisplayName().toLowerCase()))
                                                mImgCardType.setImageResource(CardBrand.JCB.getIcon());
                                            else if (TextUtils.equals(paymentMethod.card.brand.toLowerCase(), CardBrand.MasterCard.getDisplayName().toLowerCase()))
                                                mImgCardType.setImageResource(CardBrand.MasterCard.getIcon());
                                            else if (TextUtils.equals(paymentMethod.card.brand.toLowerCase(), CardBrand.UnionPay.getDisplayName().toLowerCase()))
                                                mImgCardType.setImageResource(CardBrand.UnionPay.getIcon());
                                        }
                                    }
                                }
                            }
        );
    }

    private Stripe stripe;

    private void startCheckout() {
        if (paymentMethod != null && !TextUtils.isEmpty(mApplication.getmClientSecret())) {
            // mVMCreditCardInfo.observerProgressBar.set(true);
            //pi_1H5wiyG2g0FLTf9YFchiFfmG_secret_6WPDgmKu1LehSMn6tMtwLgx2K
            ConfirmPaymentIntentParams confirmParams =
                    ConfirmPaymentIntentParams.createWithPaymentMethodId(paymentMethod.id, mApplication.getmClientSecret());

            stripe = new Stripe(mMainActivity,
                    PaymentConfiguration.getInstance(mMainActivity).getPublishableKey()
            );
            stripe.confirmPayment(this, confirmParams);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            paymentSession.handlePaymentData(requestCode, resultCode, data);
            if (mRequestCode == 1234)
                stripe.onPaymentResult(requestCode, data, new PaymentResultCallback());
        }
    }

    private class PaymentResultCallback implements ApiResultCallback<PaymentIntentResult> {
        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            mVMCreditCardInfo.observerProgressBar.set(false);
            if (status == PaymentIntent.Status.Succeeded) {
                showSuccessDialog("Payment completed");
                mVMCreditCardInfo.networkCallData();
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                showSuccessDialog("Payment failed " + Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage());
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            showSuccessDialog("Error" + e.toString());
        }
    }

    private void showSuccessDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(getString(R.string.message_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                Intent intent = new Intent(mApplication.getResources().getString(R
                        .string.broadcastNobiasChangeIndex));
                mApplication.getAppComponent().getLocalBroadcastManager().sendBroadcast(intent);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle("Payment Information");
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @NonNull
    private ShippingInformation getDefaultShippingInfo() {
        // optionally specify default shipping address
        return new ShippingInformation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
