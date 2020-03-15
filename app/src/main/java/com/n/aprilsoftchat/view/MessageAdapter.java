package com.n.aprilsoftchat.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.n.aprilsoftchat.R;
import com.n.aprilsoftchat.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private LayoutInflater mInflater;
    private List<Message> mData = new ArrayList<>();

    public void setData(List<Message> mData) {
        this.mData = mData;
    }

    MessageAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.message, parent, false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {
        Message message = mData.get(position);

        holder.tvUsername.setText(message.getUser());
        holder.tvDate.setText(message.getTime());
        holder.tvText.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MessageHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        TextView tvDate;
        TextView tvText;

        MessageHolder(View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvText = itemView.findViewById(R.id.tv_text);
        }

    }

}

