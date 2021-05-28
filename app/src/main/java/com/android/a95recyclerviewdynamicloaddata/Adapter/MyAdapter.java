package com.android.a95recyclerviewdynamicloaddata.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.a95recyclerviewdynamicloaddata.Interface.ILoadMore;
import com.android.a95recyclerviewdynamicloaddata.Model.Item;
import com.android.a95recyclerviewdynamicloaddata.R;

import java.util.List;

//todo 1
class LoadingViewHolder extends RecyclerView.ViewHolder{

    public ProgressBar progressBar;
    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progressBar);
    }
}

//todo 2
class ItemViewHolder extends RecyclerView.ViewHolder{

    public TextView name,length;
    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.txtName);
        length = itemView.findViewById(R.id.txtLength);
    }
}

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //todo 3
    private final int VIEW_TYPE_ITEM=0,VIEW_TYPE_LOADING=1;
    ILoadMore loadMore;
    boolean isLoading;
    Activity activity;
    List<Item> items;
    int visibleThreshold=5;
    int lastVisibleItem,totalItemCount;

    //todo 4
    public MyAdapter(RecyclerView recyclerView,Activity activity, List<Item> items) {
        this.activity = activity;
        this.items = items;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem+visibleThreshold)){
                    if (loadMore != null){
                        loadMore.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    //todo 5
    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_ITEM;
    }

    //todo 6
    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }

    //todo 7
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(activity).inflate(R.layout.item_layout,parent,false);
            return new ItemViewHolder(view);
        }
        else if (viewType==VIEW_TYPE_LOADING){
            View view = LayoutInflater.from(activity).inflate(R.layout.item_loading,parent,false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    //todo 8
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder){
            Item item = items.get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.name.setText(items.get(position).getName());
            viewHolder.length.setText(String.valueOf(items.get(position).getLength()));
        }else if (holder instanceof LoadingViewHolder){
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder)holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    //todo 9
    @Override
    public int getItemCount() {
        return items.size();
    }

    //todo 10
    public void setLoaded(){
        isLoading = false;
    }
}
