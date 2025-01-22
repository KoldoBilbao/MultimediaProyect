package com.example.roomtest;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder> {

    private List<Crypto> cryptoList;

    public CryptoAdapter(List<Crypto> cryptoList) {
        this.cryptoList = cryptoList;
    }

    @NonNull
    @Override
    public CryptoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cryptocurrency, parent, false);
        return new CryptoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CryptoViewHolder holder, int position) {
        Crypto crypto = cryptoList.get(position);

        holder.name.setText(crypto.getName());
        holder.symbol.setText(crypto.getSymbol());
        holder.rank.setText("Rank: " + crypto.getMarket_cap_rank());
        holder.price.setText("Price: $" + crypto.getCurrent_price());
        holder.change.setText("24h: " + crypto.getPrice_change_percentage_24h() + "%");

        Glide.with(holder.thumb.getContext())
                .load(crypto.getImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_image)
                .into(holder.thumb);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), CryptoDetailActivity.class);
            intent.putExtra("coinId", crypto.getId()); // Pass coinId
            intent.putExtra("image", crypto.getImage());
            intent.putExtra("name", crypto.getName());
            intent.putExtra("symbol", crypto.getSymbol());
            intent.putExtra("price", crypto.getCurrent_price());
            intent.putExtra("market_cap", crypto.getMarket_cap());
            intent.putExtra("max_supply", crypto.getMax_supply());
            intent.putExtra("circulating_supply", crypto.getCirculating_supply());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cryptoList.size();
    }

    public static class CryptoViewHolder extends RecyclerView.ViewHolder {
        TextView name, symbol, rank, price, change;
        ImageView thumb;

        public CryptoViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.crypto_name);
            symbol = itemView.findViewById(R.id.crypto_symbol);
            rank = itemView.findViewById(R.id.crypto_rank);
            price = itemView.findViewById(R.id.crypto_price);
            change = itemView.findViewById(R.id.crypto_change);
            thumb = itemView.findViewById(R.id.crypto_thumb);
        }
    }
}
