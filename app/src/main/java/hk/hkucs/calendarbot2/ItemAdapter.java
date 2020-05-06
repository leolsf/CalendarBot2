package hk.hkucs.calendarbot2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter <ItemAdapter.MsgViewHolder> {

    public Context context;
    private List<Msg> list;
    public ItemAdapter(Context context, List<Msg> list){
        this.context = context;
        this.list = list;
    }

    class MsgViewHolder extends RecyclerView.ViewHolder{
        LinearLayout llLeft,llRight;
        TextView tvLeft,tvRight;
        ImageView imageLeft, imageRight;

        public MsgViewHolder(@NonNull View itemView) {
            super(itemView);
            llLeft = itemView.findViewById(R.id.ll_left);
            llRight = itemView.findViewById(R.id.ll_right);
            tvLeft = itemView.findViewById(R.id.tv_left);
            tvRight = itemView.findViewById(R.id.tv_right);
            imageLeft = itemView.findViewById(R.id.image_left);
            imageRight = itemView.findViewById(R.id.image_right);

        }
    }

    @NonNull
    @Override
    public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent,false);
        MsgViewHolder viewHolder = new MsgViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MsgViewHolder holder, int position) {
        Msg msg = list.get(position);
        // 根据消息的类型来选择不同的布局
        if (msg.getType() == Msg.TYPE_RECEIVE){
            holder.llLeft.setVisibility(View.VISIBLE);
            holder.llRight.setVisibility(View.GONE);
            holder.tvLeft.setText(msg.getContent());

            Glide.with(context)
                    .load(msg.getPicture())
//                    .placeholder(R.drawable.wiki)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .override(400, 400)
                    .into(holder.imageLeft);
        }else {
            holder.llRight.setVisibility(View.VISIBLE);
            holder.llLeft.setVisibility(View.GONE);
            holder.tvRight.setText(msg.getContent());

            Glide.with(context)
                    .load(msg.getPicture())
//                    .placeholder(R.drawable.wiki)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .override(400, 400)
                    .into(holder.imageRight);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
