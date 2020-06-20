package com.boardgames.jaipur.ui.rounds;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class DraggedItemsListViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<String>> draggedItemList;
    private ArrayList<String> dragAndDroppedOrder;

    public DraggedItemsListViewModel(Application application) {
        super(application);
    }


    public MutableLiveData<ArrayList<String>> getDraggedItemList() {
        if (draggedItemList == null) {
            draggedItemList = new MutableLiveData<>();

        }

        draggedItemList.setValue(dragAndDroppedOrder);
        return draggedItemList;
    }

    public void deleteSelectedItem() {
        ArrayList<String> list = draggedItemList.getValue();
        if (!list.isEmpty()) {
            list.remove(list.size() - 1);
            draggedItemList.setValue(list);
        }
    }

    public void setDragAndDroppedOrder(ArrayList<String> dragAndDroppedOrder) {
        this.dragAndDroppedOrder = dragAndDroppedOrder;
    }

    public MutableLiveData<ArrayList<String>> resetDraggedItemList() {
        if (draggedItemList == null) {
            draggedItemList = new MutableLiveData<>();
        }

        draggedItemList.setValue(new ArrayList<String>());
        return draggedItemList;
    }
}
