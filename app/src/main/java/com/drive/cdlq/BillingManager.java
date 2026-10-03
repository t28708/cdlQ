package com.drive.cdlq;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.PendingPurchasesParams;
import com.android.billingclient.api.QueryPurchasesParams;


import java.util.List;

public class BillingManager {

    private static final String TAG = "BillingManager";
    public static final String PRODUCT_ID_REMOVE_ADS = "remove_ads"; // Change this to your actual Product ID in Play Console
    private static final String PREF_FILE = "MyPrefs";
    private static final String KEY_IS_PREMIUM = "isPremium";

    private BillingClient billingClient;
    private final Activity activity;

    private OnPremiumListener onPremiumListener;
    private PurchaseCallback purchaseCallback;

    public interface OnPremiumListener {
        void onPremiumStatusChanged(boolean isPremium);
    }

    public interface PurchaseCallback {
        void onPurchaseResult(boolean isSuccess);
    }

    public BillingManager(Activity activity) {
        this.activity = activity;
        if (activity instanceof OnPremiumListener) {
            this.onPremiumListener = (OnPremiumListener) activity;
        }
        initialize();
    }

    private void initialize() {
        billingClient = BillingClient.newBuilder(activity)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build())
                .build();

        startConnection();
    }

    private void startConnection() {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // Log.d(TAG, "Billing Setup Finished. Querying purchases...");
                    queryPurchases();
                } else {
                    // Log.e(TAG, "Billing Setup Failed: " + billingResult.getDebugMessage());
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                // Log.d(TAG, "Billing Service Disconnected. Retrying...");
                // Note: It's recommended to implement a retry policy here (e.g., exponential backoff)
                // For simplicity, we just leave it disconnected for now, retry happens on purchase attempt
            }
        });
    }

    private final PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(@NonNull BillingResult billingResult, List<Purchase> purchases) {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (Purchase purchase : purchases) {
                    handlePurchase(purchase);
                }
                // Success is handled in handlePurchase properly after ack, 
                // but technical success of flow is here. 
                // Ideally we wait for Ack, but UI might want immediate feedback.
                // handlePurchase calls setPremiumStatus which updates UI.
                // We'll call callback(true) in handlePurchase to be sure.
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Log.d(TAG, "Purchase Canceled by user");
                if (purchaseCallback != null) {
                    activity.runOnUiThread(() -> {
                         if (purchaseCallback != null) {
                             purchaseCallback.onPurchaseResult(false);
                             purchaseCallback = null;
                         }
                    });
                }
            } else {
                // Log.e(TAG, "Purchase Error: " + billingResult.getDebugMessage());
                if (purchaseCallback != null) {
                    activity.runOnUiThread(() -> {
                        if (purchaseCallback != null) {
                            purchaseCallback.onPurchaseResult(false);
                            purchaseCallback = null;
                        }
                    });
                }
            }
        }
    };

    public void launchPurchaseFlow() {
        launchPurchaseFlow(null);
    }

    public void launchPurchaseFlow(PurchaseCallback callback) {
        this.purchaseCallback = callback;
        if (!billingClient.isReady()) {
            // Log.e(TAG, "Billing Client not ready. Connecting...");
            startConnection();
             // Toast.makeText(activity, "Connecting to Google Play... Please try again in a moment.", Toast.LENGTH_SHORT).show();
             if (purchaseCallback != null) purchaseCallback.onPurchaseResult(false);
            return;
        }


        List<QueryProductDetailsParams.Product> productList = new java.util.ArrayList<>();
        productList.add(QueryProductDetailsParams.Product.newBuilder()
                .setProductId(PRODUCT_ID_REMOVE_ADS)
                .setProductType(BillingClient.ProductType.INAPP)
                .build());

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build();

        billingClient.queryProductDetailsAsync(params, (billingResult, productDetailsResult) -> {
            List<ProductDetails> productDetailsList = productDetailsResult.getProductDetailsList();
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && productDetailsList != null && !productDetailsList.isEmpty()) {
                ProductDetails productDetails = productDetailsList.get(0);
                List<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                        new java.util.ArrayList<>();
                productDetailsParamsList.add(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails)
                                .build()
                );

                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build();

                billingClient.launchBillingFlow(activity, billingFlowParams);
            } else {
                // Log.e(TAG, "Product not found or error querying details: " + billingResult.getDebugMessage());
                // activity.runOnUiThread(() -> Toast.makeText(activity, "Product not found", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                // Determine if we need to acknowledge the purchase
                // For a consumable, we consume. For non-consumable (like remove ads), we acknowledge.
                // Assuming non-consumable here.
                com.android.billingclient.api.AcknowledgePurchaseParams acknowledgePurchaseParams =
                        com.android.billingclient.api.AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();

                billingClient.acknowledgePurchase(acknowledgePurchaseParams, billingResult -> {
                   if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                       setPremiumStatus(true);
                       activity.runOnUiThread(() -> {
                           Toast.makeText(activity, "Purchase Successful! Ads Removed.", Toast.LENGTH_LONG).show();
                           if (purchaseCallback != null) {
                               purchaseCallback.onPurchaseResult(true);
                               purchaseCallback = null;
                           }
                       });
                       // Ideally trigger a UI refresh to remove ads immediately
                   }
                });
            } else {
                 setPremiumStatus(true);
                 if (purchaseCallback != null) {
                    purchaseCallback.onPurchaseResult(true);
                    purchaseCallback = null;
                 }
            }
        }
    }
    
    public void queryPurchases() {
         billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build(),
            (billingResult, purchases) -> {
                 if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                     boolean isPremium = false;
                     for (Purchase purchase : purchases) {
                         if (purchase.getProducts().contains(PRODUCT_ID_REMOVE_ADS) && purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                             isPremium = true;
                             break;
                         }
                     }
                     setPremiumStatus(isPremium);
                 }
            }
        );
    }

    private void setPremiumStatus(boolean isPremium) {
        GlobalData.isPremium = isPremium;
        SharedPreferences prefs = activity.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_IS_PREMIUM, isPremium);
        editor.apply();
        // Log.d(TAG, "Premium Status Set to: " + isPremium);
        
        if (onPremiumListener != null) {
            activity.runOnUiThread(() -> onPremiumListener.onPremiumStatusChanged(isPremium));
        }
    }

    // Helper to check existing shared prefs syncronously if needed quickly on startup
    public static void checkPremiumStatus(Context context) {
         SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
         GlobalData.isPremium = prefs.getBoolean(KEY_IS_PREMIUM, false);
    }

    public void restorePurchases() {
        if (!billingClient.isReady()) {
            startConnection();
            Toast.makeText(activity, "Restoring... Check connection and try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder()
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build(),
                (billingResult, purchases) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        boolean isPremium = false;
                        for (Purchase purchase : purchases) {
                            if (purchase.getProducts().contains(PRODUCT_ID_REMOVE_ADS) && purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                                isPremium = true;
                                break;
                            }
                        }
                        if (isPremium) {
                            setPremiumStatus(true);
                            activity.runOnUiThread(() -> Toast.makeText(activity, "Purchases restored", Toast.LENGTH_SHORT).show());
                        } else {
                            // Feedback for user when nothing is found, so they know it worked but result is empty
                            activity.runOnUiThread(() -> Toast.makeText(activity, "No active purchases found", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        Log.e(TAG, "Restore Error: " + billingResult.getDebugMessage());
                        activity.runOnUiThread(() -> Toast.makeText(activity, "Error restoring purchases", Toast.LENGTH_SHORT).show());
                    }
                }
        );
    }
}
