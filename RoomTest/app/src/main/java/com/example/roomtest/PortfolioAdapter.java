package com.example.roomtest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.PortfolioViewHolder> {

    private List<Portfolio> portfolioList;
    private final OnEditPortfolioListener editListener;
    private final OnDeletePortfolioListener deleteListener;

    public PortfolioAdapter(List<Portfolio> portfolioList, OnEditPortfolioListener editListener, OnDeletePortfolioListener deleteListener) {
        this.portfolioList = portfolioList;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public PortfolioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_portfolio, parent, false);
        return new PortfolioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PortfolioViewHolder holder, int position) {
        Portfolio portfolio = portfolioList.get(position);

        holder.name.setText(portfolio.getName());
        holder.symbol.setText(portfolio.getSymbol());
        holder.quantity.setText("Quantity: " + portfolio.getQuantity());
        holder.price.setText("Price: $" + portfolio.getPrice());

        holder.itemView.setOnClickListener(v -> editListener.onEdit(portfolio));
        holder.itemView.setOnLongClickListener(v -> {
            deleteListener.onDelete(portfolio);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return portfolioList.size();
    }

    public void updatePortfolioList(List<Portfolio> newPortfolioList) {
        this.portfolioList = newPortfolioList;
        notifyDataSetChanged();
    }

    public static class PortfolioViewHolder extends RecyclerView.ViewHolder {
        TextView name, symbol, quantity, price;

        public PortfolioViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.portfolioName);
            symbol = itemView.findViewById(R.id.portfolioSymbol);
            quantity = itemView.findViewById(R.id.portfolioQuantity);
            price = itemView.findViewById(R.id.portfolioPrice);
        }
    }

    public interface OnEditPortfolioListener {
        void onEdit(Portfolio portfolio);
    }

    public interface OnDeletePortfolioListener {
        void onDelete(Portfolio portfolio);
    }
}
