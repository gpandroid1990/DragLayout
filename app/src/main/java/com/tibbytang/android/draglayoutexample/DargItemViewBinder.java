package com.tibbytang.android.draglayoutexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 作者:tibbytang
 * 微信:tibbytang19900607
 * 有问题加微信
 * 创建于:2020-04-03 16:41
 */
class DargItemViewBinder extends me.drakeet.multitype.ItemViewBinder<String, DargItemViewBinder.ViewHolder> {
    private final DragViewClickListener mDragViewClickListener;

    DargItemViewBinder(DragViewClickListener dragViewClickListener) {
        this.mDragViewClickListener = dragViewClickListener;
    }

    @NonNull
    @Override
    protected DargItemViewBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_drag_main, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull final DargItemViewBinder.ViewHolder holder, @NonNull String item) {
        holder.dragNameView.setText(item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDragViewClickListener.onDragViewClick(holder.getAdapterPosition());
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView dragNameView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dragNameView = itemView.findViewById(R.id.item_drag_name_view);
        }
    }

    interface DragViewClickListener {
        void onDragViewClick(int position);
    }
}
