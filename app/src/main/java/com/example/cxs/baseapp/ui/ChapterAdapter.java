package com.example.cxs.baseapp.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.cxs.baseapp.R;
import com.example.cxs.baseapp.manager.http.response.DazhuzaiResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengxunshi on 2016/8/11.
 */
public class ChapterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<DazhuzaiResponse.MixToc.ChapterIntro> data = new ArrayList<>();

    private RecyclerViewItemListener listener;

    public ChapterAdapter(){

    }

    public void setData(List<DazhuzaiResponse.MixToc.ChapterIntro> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    public void setItemListener(RecyclerViewItemListener listener) {
        this.listener = listener;
    }

    public DazhuzaiResponse.MixToc.ChapterIntro getChapter(int position) {
        if(null != data && position < data.size()){
            return data.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.item_layout_chapter, viewGroup,
                false);
        return new ContentViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(null != data && position >= 0 && position < data.size()){
            ContentViewHolder viewHolder = (ContentViewHolder) holder;
            DazhuzaiResponse.MixToc.ChapterIntro chapter = data.get(position);
            viewHolder.title.setText(chapter.title);
            viewHolder.url = chapter.link;

        }
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        TextView title;
        String url;
        RecyclerViewItemListener listener;
        public ContentViewHolder(View itemView, RecyclerViewItemListener listener){
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_chapter_title);
            itemView.setOnClickListener(this);
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            if(null != listener) {
                listener.onClick(view, getAdapterPosition());
            }
        }

    }

    public interface RecyclerViewItemListener{
        public void onClick(View view, int position);
    }
}
