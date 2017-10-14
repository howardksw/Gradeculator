package edu.umd.cs.gradeculator.service;

/**
 * Created by apple on 4/24/17.
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
