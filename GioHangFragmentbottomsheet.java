package com.example.fashionapp.Fragment;

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
import com.example.fashionapp.Helper.ManagmentCart;
import com.example.fashionapp.R;
import com.example.fashionapp.Domain.Order;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.giohangbottomsheet, container, false);
        return view;
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
        btncancel.setOnClickListener(v -> dismiss());
    }

    private void getData() {
        for (int i = 0; i < cartItems.size(); i++) {
            chitietdonhang += cartItems.get(i).getTitle() + "\t" + "1x" + cartItems.get(i).getNumberinCard() + "\t" + "size" + "\t" + cartItems.get(i).getSize() + ".\n";
        }
        txtsoluong.setText(chitietdonhang);
    }

    private void calculateCard() {
        double percentTax = 0.02;
        double delivery = 10;
        double tax = Math.round((managmentCart.getTotalFee() * percentTax * 100.0)) / 100.0;
         total = (double) Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;
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
            Order order = new Order(userId, phoneNumber, address, totalFee,chitietdonhang,"chờ xác nhận");
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
