package com.bessarez.ecommercemobile.ui.models;

public abstract class ListOrderItem {
    public static final int TYPE_ORDER = 0;
    public static final int TYPE_ORDER_ITEM = 1;

    abstract public int getType();
}
