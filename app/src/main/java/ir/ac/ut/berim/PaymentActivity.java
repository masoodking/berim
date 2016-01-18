package ir.ac.ut.berim;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ir.ac.ut.util.IabHelper;
import ir.ac.ut.util.IabResult;
import ir.ac.ut.util.Inventory;
import ir.ac.ut.util.Purchase;

public class PaymentActivity extends AppCompatActivity {

    private Context mContext;

    private ProgressDialog mProgressDialog;


    // Debug tag, for logging
    static final String TAG = "PaymentActivity";

    // SKUs for our products: the premium upgrade (non-consumable)
    static final String SKU_PREMIUM = "berim_vip";

    // Does the user have the premium upgrade?
    boolean mIsPremium = false;

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 1;

    // The helper object
    IabHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_payment);

        String base64EncodedPublicKey
                = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwC+IhNkP3lGH0MGp9DGBd4OWj60iCnUk9u5zN477S6yBo1MORJ1lsDNi0cFRO/+IiuiITWDqIshnYVWErJ60bqXkrU2ulr0R9xdX+t3Tr8HZbi+JZIxe8LJoCeu1rFyV2YN7VBrkWtDYVC761v/qTrY6CHiy/Z6kxJbjZlPLcJyCZ3vv2NsySisZ6tXAbXNFvOOgnG0d31pWvJyFXpSRoL1MHEoQwXLvsIweXXMyEECAwEAAQ==";
        // You can find it in your Bazaar console, in the Dealers section.
        // It is recommended to add more security than just pasting it in your source code;
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Log.d(TAG, "Problem setting up In-app Billing: " + result);
                }
                // Hooray, IAB is fully set up!
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });

        Button buyBtn = (Button) findViewById(R.id.buy_vip_account);
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.launchPurchaseFlow(((Activity) mContext), SKU_PREMIUM, RC_REQUEST,
                        mPurchaseFinishedListener, "payload-string");
            }
        });
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");
            if (result.isFailure()) {
                Log.d(TAG, "Failed to query inventory: " + result);
                return;
            } else {
                Log.d(TAG, "Query inventory was successful.");
                // does the user have the premium upgrade?
                mIsPremium = inventory.hasPurchase(SKU_PREMIUM);

                // update UI accordingly

                Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
            }

            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()) {
                Log.d(TAG, "Error purchasing: " + result);
                Toast.makeText(mContext, "Payment faild :(", Toast.LENGTH_SHORT).show();
                return;
            } else if (purchase.getSku().equals(SKU_PREMIUM)) {
                // give user access to premium content and update the UI
                Toast.makeText(mContext, "You are VIP user now :)", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) {
            mHelper.dispose();
        }
        mHelper = null;
    }
}
