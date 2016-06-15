package com.testapp.hv.strippers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentMethodTokenizationType;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;

import java.util.HashMap;
import java.util.Map;

//https://stripe.com/docs/mobile/android
public class MainActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String PUBLISHABLE_KEY = "pk_test_DnzXsvMmhTvcby0isNcYe1qx";//"pk_live_O7HGuiRiTLYZe46aAoDfIg5S";
    //    pk_test_DnzXsvMmhTvcby0isNcYe1qx;
    // Unique identifiers for asynchronous requests:
    private static final int LOAD_MASKED_WALLET_REQUEST_CODE = 1000;
    private static final int LOAD_FULL_WALLET_REQUEST_CODE = 1001;

    private SupportWalletFragment walletFragment;
    private GoogleApiClient googleApiClient;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
                        .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                        .setTheme(WalletConstants.THEME_LIGHT)
                        .build())
                .build();
        Wallet.Payments.isReadyToPay(googleApiClient).setResultCallback(
                new ResultCallback<BooleanResult>() {
                    @Override
                    public void onResult(@NonNull BooleanResult booleanResult) {
                        if (booleanResult.getStatus().isSuccess()) {
                            if (booleanResult.getValue()) {
                                showAndroidPay();
                            } else {
                                // Hide Android Pay buttons, show a message that Android Pay
                                // cannot be used yet, and display a traditional checkout button
                            }
                        } else {
                            // Error making isReadyToPay call
                        }
                    }
                });
        tv = (TextView) findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     showAndroidPay();
///////////|||||||||||||||||||| OWN FORM
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final EditText text = new EditText(MainActivity.this);
                EditText cvc = new EditText(MainActivity.this);
                FrameLayout f = new FrameLayout(MainActivity.this);
                f.addView(text);
                builder.setTitle("Payment").setMessage("Card").setView(f);
                builder.setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface di, int i) {
                                String cardnumber = text.getText().toString();
//                                cardnumber = "4000000000000002";//declined
                                cardnumber = "5555555555554444"; //MasterCard
//                        Toast.makeText(getApplicationContext(),name,Toast.LENGTH_LONG).show();
                                Card card = new Card(cardnumber, 12, 2017, "123");
//                                Card card = new Card("4242424242424242", 12, 2017, "123");
                                Stripe stripe = null;
                                try {
                                    stripe = new Stripe(PUBLISHABLE_KEY);
                                } catch (AuthenticationException e) {
                                    e.printStackTrace();
                                }
                                if (stripe != null) {
                                    stripe.createToken(
                                            card,
                                            new TokenCallback() {
                                                public void onError(Exception error) {
                                                    // Show localized error message
                                                    Toast.makeText(getApplicationContext(),
                                                            "Error",
                                                            Toast.LENGTH_LONG
                                                    ).show();
                                                }

                                                @Override
                                                public void onSuccess(com.stripe.android.model.Token token) {
                                                   int randomNum = 0 + (int)(Math.random() * 5);
                                                   Toast.makeText(getApplicationContext(),
                                                            "Success\n"+randomNum,
                                                            Toast.LENGTH_LONG
                                                    ).show();
                                                    String apiKey = "sk_live_sisAonY9aA3zhgBl0U6Gc37s";
                                                    String CONNECTED_STRIPE_ACCOUNT_ID = "jet-404";//"cus_8dprsEVnH8F2LL";//cUSTOMER ID
                                                    RequestOptions requestOptions = RequestOptions.builder().setStripeAccount(CONNECTED_STRIPE_ACCOUNT_ID).build();
                                                    Map<String, Object> chargeParams = new HashMap<String, Object>();
                                                    chargeParams.put("amount", 10);
                                                    chargeParams.put("currency", "usd");
                                                    chargeParams.put("source", token);
                                                    try {
                                                        Charge.create(chargeParams, requestOptions);
                                                    } catch (AuthenticationException e) {
                                                        e.printStackTrace();
                                                    } catch (InvalidRequestException e) {
                                                        e.printStackTrace();
                                                    } catch (APIConnectionException e) {
                                                        e.printStackTrace();
                                                    } catch (CardException e) {
                                                        e.printStackTrace();
                                                    } catch (APIException e) {
                                                        e.printStackTrace();
                                                    }
/*
                                            */

                                                    chargeParams = new HashMap<String, Object>();
                                                    chargeParams.put("amount", 400);
                                                    chargeParams.put("currency", "usd");
                                                    Map<String, Object> sourceParams = new HashMap<String, Object>();
                                                    sourceParams.put("number", "4242424242424242");
                                                    sourceParams.put("exp_month", 6);
                                                    sourceParams.put("exp_year", 2017);
                                                    sourceParams.put("cvc", "314");
                                                    chargeParams.put("source", sourceParams);
                                                    chargeParams.put("description", "Charge for test@example.com");

                                                    try {
                                                        Charge.create(chargeParams);
                                                    } catch (AuthenticationException e) {
                                                        e.printStackTrace();
                                                    } catch (InvalidRequestException e) {
                                                        e.printStackTrace();
                                                    } catch (APIConnectionException e) {
                                                        e.printStackTrace();
                                                    } catch (CardException e) {
                                                        e.printStackTrace();
                                                    } catch (APIException e) {
                                                        e.printStackTrace();
                                                    }




                                            /*
                                            Stripe.apiKey = "sk_test_BQokikJOvBiI2HlWgH4olfQ2";

// Get the credit card details submitted by the form
                                            String token = request.getParameter("stripeToken");

// Create a Customer
                                            Map<String, Object> customerParams = new HashMap<String, Object>();
                                            customerParams.put("source", token);
                                            customerParams.put("description", "Example customer");

                                            Customer customer = Customer.create(customerParams);

// Charge the Customer instead of the card
                                            Map<String, Object> chargeParams = new HashMap<String, Object>();
                                            chargeParams.put("amount", 1000); // amount in cents, again
                                            chargeParams.put("currency", "usd");
                                            chargeParams.put("customer", customer.getId());

                                            Charge.create(chargeParams);

// YOUR CODE: Save the customer ID and other info in a database for later!

// YOUR CODE: When it's time to charge the customer again, retrieve the customer ID!

                                            Map<String, Object> otherChargeParams = new HashMap<String, Object>();
                                            otherChargeParams.put("amount", 1500); // $15.00 this time
                                            otherChargeParams.put("currency", "usd");
                                            otherChargeParams.put("customer", customerId); // Previously stored, then retrieved

                                            Charge.create(otherChargeParams);

*/
                                                }
                                            }
                                    );
                                }
                                //do something with it
                            }
                        });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface di, int i) {
                    }
                });
                builder.create().show();
/*
*/

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void showAndroidPay() {
        setContentView(R.layout.wallet);
        walletFragment =
                (SupportWalletFragment) getSupportFragmentManager().findFragmentById(R.id.wallet_fragment);
        MaskedWalletRequest maskedWalletRequest = MaskedWalletRequest.newBuilder()
                // Request credit card tokenization with Stripe by specifying tokenization parameters:
                .setPaymentMethodTokenizationParameters(PaymentMethodTokenizationParameters.newBuilder()
                        .setPaymentMethodTokenizationType(PaymentMethodTokenizationType.PAYMENT_GATEWAY)
                        .addParameter("gateway", "stripe")
                        .addParameter("stripe:publishableKey", PUBLISHABLE_KEY)
                        .addParameter("stripe:version", com.stripe.Stripe.VERSION)
                        .build())
                // You want the shipping address:
                .setShippingAddressRequired(true)
                // Price set as a decimal:
                .setEstimatedTotalPrice("20.00")
                .setCurrencyCode("USD")
                .build();
        // Set the parameters:
        WalletFragmentInitParams initParams = WalletFragmentInitParams.newBuilder()
                .setMaskedWalletRequest(maskedWalletRequest)
                .setMaskedWalletRequestCode(LOAD_MASKED_WALLET_REQUEST_CODE)
                .build();
        // Initialize the fragment:
        walletFragment.initialize(initParams);
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public static final int mEnvironment = WalletConstants.ENVIRONMENT_TEST;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOAD_MASKED_WALLET_REQUEST_CODE) {
        } else if (requestCode == LOAD_FULL_WALLET_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                FullWallet fullWallet = data.getParcelableExtra(WalletConstants.EXTRA_FULL_WALLET);
                String tokenJSON = fullWallet.getPaymentMethodToken().getToken();
                Toast.makeText(getApplicationContext(), "Result Ok", Toast.LENGTH_LONG).show();
                //A token will only be returned in production mode,
                //i.e. WalletConstants.ENVIRONMENT_PRODUCTION
                if (mEnvironment == WalletConstants.ENVIRONMENT_PRODUCTION) {
                    com.stripe.model.Token token = com.stripe.model.Token.GSON.fromJson(
                            tokenJSON, com.stripe.model.Token.class);
                    // TODO: send token to your server
                } else if (mEnvironment == WalletConstants.ENVIRONMENT_TEST) {
                    Toast.makeText(getApplicationContext(), "Test\nSuccessfull Buy", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
//6513272172142173  MASTERCARD