package com.example.admin.GetCoordinates.mvp.interfaces;

public interface PresenterInterface<T extends ViewInterface, P extends ModelInterface> {
    void applyModel(P model);

    void applyView(T view);

    void showMessage(String message);
}