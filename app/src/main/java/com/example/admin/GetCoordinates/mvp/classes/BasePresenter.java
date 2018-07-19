package com.example.admin.GetCoordinates.mvp.classes;

import com.example.admin.GetCoordinates.main.MainActivity;
import com.example.admin.GetCoordinates.mvp.interfaces.ModelInterface;
import com.example.admin.GetCoordinates.mvp.interfaces.PresenterInterface;
import com.example.admin.GetCoordinates.mvp.interfaces.ViewInterface;

public abstract class BasePresenter <V extends ViewInterface, M extends ModelInterface> implements PresenterInterface<V, M> {

    private M mModel;
    private V mView;
    private MainActivity activity;

    protected BasePresenter(){

    }

    protected BasePresenter(MainActivity activity){
        this.activity = activity;
    }

    public void initComponents(M model, V view) {
        applyModel(model);
        applyView(view);
        model.applyPresenter(this);
    }

    @Override
    public void applyModel(M model) {
        mModel = model;
    }

    @Override
    public void applyView(V view) {
        mView = view;
    }

    protected V getView() {
        return mView;
    }

    protected M getModel() {
        return mModel;
    }

    protected MainActivity getActivity(){
        return activity;
    }

    @Override
    public void showMessage(String message) {
        getView().showMessage(message);
    }


}
