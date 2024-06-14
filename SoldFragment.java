package com.example.fashionapp.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.fashionapp.Adapter.ReviewAdapter;
import com.example.fashionapp.Domain.ReviewDomain;
import com.example.fashionapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SoldFragment extends Fragment {
    private EditText edt1;
    private EditText edt2;
    private Button btn1;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edt1=(EditText) view.findViewById(R.id.editText);
        edt2=(EditText) view.findViewById(R.id.textView2);
        btn1=(Button) view.findViewById(R.id.button2);
        edt1.setInputType(InputType.TYPE_NULL);
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            edt1.setRawInputType(InputType.TYPE_CLASS_TEXT);
            edt1.setTextIsSelectable(true);
        }
        edt2.setInputType(InputType.TYPE_NULL);
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            edt2.setRawInputType(InputType.TYPE_CLASS_TEXT);
            edt2.setTextIsSelectable(true);
        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             initList(view);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sold, container, false);
    }
    private void initList(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reviewRef = database.getReference("Review");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // Người dùng chưa đăng nhập
            return;
        }
        String userId = currentUser.getUid();
        String username = currentUser.getDisplayName(); // hoặc bạn có thể lấy thông tin khác như email, avatar, ...
        Uri photoUri = currentUser.getPhotoUrl();
        // Lấy thông tin bình luận từ người dùng và cập nhật vào Firebase Realtime Database
        String comment = edt1.getText().toString().trim();
        Double rt1= Double.valueOf(edt2.getText().toString().trim());
        ReviewDomain review = new ReviewDomain(userId,comment,photoUri,rt1);
        reviewRef.push().setValue(review)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Bình luận được cập nhật thành công
                            // Tiếp tục hiển thị danh sách bình luận
                            displayReviews(view);
                        } else {
                            // Lỗi khi cập nhật bình luận
                        }
                    }
                });
    }

    private void displayReviews(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reviewRef = database.getReference("Review");
        ArrayList<ReviewDomain> list = new ArrayList<>();
        Query query = reviewRef.orderByChild("ItemId").equalTo(4);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(ReviewDomain.class));
                    }
                    RecyclerView desctxt = view.findViewById(R.id.reviewView);
                    if (list.size() > 0) {
                        desctxt.setAdapter(new ReviewAdapter(list));
                        desctxt.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }



}