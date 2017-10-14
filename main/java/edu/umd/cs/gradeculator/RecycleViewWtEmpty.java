package edu.umd.cs.gradeculator;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;


//    Credits
//    This application uses Open Source components.
//    You can find the source code of their open source projects along with license information below.
//    We acknowledge and are grateful to these developers for their contributions to open source.
//
//    Project: https://gist.github.com/adelnizamutdinov/31c8f054d1af4588dc5c
//    Use EmptyRecyclerView.java by Nizamutdinov Adel
//

// Implement the empty view featuer by adding empty featuer into recyclerview
public class RecycleViewWtEmpty extends RecyclerView {
    private View emptyView;

    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {


        @Override
        public void onChanged() {
            Adapter<?> adapter =  getAdapter();
            if(adapter != null && emptyView != null) {
                if(adapter.getItemCount() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                    RecycleViewWtEmpty.this.setVisibility(View.GONE);
                }
                else {
                    emptyView.setVisibility(View.GONE);
                    RecycleViewWtEmpty.this.setVisibility(View.VISIBLE);
                }
            }

        }
    };

    public RecycleViewWtEmpty(Context context) {
        super(context);
    }

    public RecycleViewWtEmpty(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecycleViewWtEmpty(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if(adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }

        emptyObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }
}