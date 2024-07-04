package com.example.fashionapp.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.fashionapp.Domain.ItemDomain;


import java.util.ArrayList;

public class ManagmentCart {

    private Context context;
    private TinyDB tinyDB;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertFood(ItemDomain item, String size) {
        ArrayList<ItemDomain> listfood = getListCart();
        boolean existAlready = false;
        int n = 0;
        for (int y = 0; y < listfood.size(); y++) {
            if (listfood.get(y).getTitle().equals(item.getTitle()) && listfood.get(y).getSize().equals(size)) {
                existAlready = true;
                n = y;
                break;
            }
        }
        if (existAlready) {
            listfood.get(n).setNumberinCard(listfood.get(n).getNumberinCard() + item.getNumberinCard());
        } else {
            item.setSize(size); // Set the size of the item
            listfood.add(item);
        }
        tinyDB.putListObject("CartList", listfood);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<ItemDomain> getListCart() {
        return tinyDB.getListObject("CartList");
    }

    public void minusItem(ArrayList<ItemDomain> listfood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if (listfood.get(position).getNumberinCard() == 1) {
            listfood.remove(position);
        } else {
            listfood.get(position).setNumberinCard(listfood.get(position).getNumberinCard() - 1);
        }
        tinyDB.putListObject("CartList", listfood);
        changeNumberItemsListener.changed();
    }

    public void plusItem(ArrayList<ItemDomain> listfood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        listfood.get(position).setNumberinCard(listfood.get(position).getNumberinCard() + 1);
        tinyDB.putListObject("CartList", listfood);
        changeNumberItemsListener.changed();
    }

    public Double getTotalFee() {
        ArrayList<ItemDomain> listfood2 = getListCart();
        double fee = 0;
        for (int i = 0; i < listfood2.size(); i++) {
            fee = fee + (listfood2.get(i).getPrice() * listfood2.get(i).getNumberinCard());
        }
        return fee;
    }
}
