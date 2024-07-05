package com.example.fashionapp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.fashionapp.Domain.ItemDomain;
import com.example.fashionapp.Domain.Order;
import com.example.fashionapp.Helper.ManagmentCart;
import com.example.fashionapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

import io.reactivex.rxjava3.annotations.NonNull;

public class GioHangFragmentbottomsheet extends BottomSheetDialogFragment {

    private ManagmentCart managmentCart;
    private ArrayList<ItemDomain> cartItems;
    private double totalFee;
    private EditText edtsdt, edtdiachi;
    private TextView txtsoluong, txtgia;
    private Button btncancel, btndathang;
    private String chitietdonhang = "";
    private FirebaseAuth auth;
    private DatabaseReference ordersRef;
    public static final String sandboxUrl = "https://sandbox.paypal.com";

    public static final String clientKey = "AUMPZPeSr6yDHXqbxfbl2BDLCj2ARubGNy5b5YZiCLJAzzfc4kXQxgz9FYDsU8HsljsMWsEauvczuHLR"; // Replace with Client ID from PayPal Developer
    public static final int PAYPAL_REQUEST_CODE = 123;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // Change to ENVIRONMENT_PRODUCTION for live
            .clientId(clientKey).sandboxUserPin(sandboxUrl);


    private double total;

    public static GioHangFragmentbottomsheet newInstance() {
        return new GioHangFragmentbottomsheet();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        managmentCart = new ManagmentCart(requireContext());
        cartItems = managmentCart.getListCart();
        totalFee = managmentCart.getTotalFee();
        auth = FirebaseAuth.getInstance();
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        // Start PayPal service
        Intent intent = new Intent(requireContext(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        requireActivity().startService(intent);
    }

    @Override
    public void onDestroy() {
        requireActivity().stopService(new Intent(requireContext(), PayPalService.class));
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.giohangbottomsheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtgia = view.findViewById(R.id.txtprice);
        txtsoluong = view.findViewById(R.id.txtsoluong);
        edtdiachi = view.findViewById(R.id.edtdiachi);
        edtsdt = view.findViewById(R.id.edtsdt);
        btncancel = view.findViewById(R.id.btncancel);
        btndathang = view.findViewById(R.id.btndathang);

        getData();
        calculateCard();

        btndathang.setOnClickListener(v -> placeOrder());
        btncancel.setOnClickListener(v -> getPayment());
    }

    private void getPayment() {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(total)), "USD", "Course Fees",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(requireContext(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (confirm != null) {
                    try {
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        JSONObject payObj = new JSONObject(paymentDetails);
                        String payID = payObj.getJSONObject("response").getString("id");
                        String state = payObj.getJSONObject("response").getString("state");
                        txtgia.setText("Payment " + state + "\n with payment id is " + payID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(requireContext(), "Payment canceled", Toast.LENGTH_SHORT).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(requireContext(), "Invalid payment configuration", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getData() {
        for (ItemDomain item : cartItems) {
            chitietdonhang += item.getTitle() + "\t" + "1x" + item.getNumberinCard() + "\t" + "size" + "\t" + item.getSize() + ".\n";
        }
        txtsoluong.setText(chitietdonhang);
    }

    private void calculateCard() {
        double percentTax = 0.02;
        double delivery = 10;
        double tax = Math.round((managmentCart.getTotalFee() * percentTax * 100.0)) / 100.0;
        total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100.0) / 100.0;
        txtgia.setText("$" + total);
    }

    private void placeOrder() {
        String phoneNumber = edtsdt.getText().toString().trim();
        String address = edtdiachi.getText().toString().trim();

        if (phoneNumber.isEmpty() || address.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            Order order = new Order(userId, phoneNumber, address, totalFee, chitietdonhang, "chờ xác nhận", "");
            ordersRef.child(userId).push().setValue(order)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "Order placed successfully", Toast.LENGTH_SHORT).show();
                            dismiss();
                        } else {
                            Toast.makeText(requireContext(), "Failed to place order", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
}
