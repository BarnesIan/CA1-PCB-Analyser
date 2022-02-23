package com.example.datastructures2ca1;


public class DisjointArray<T>{
    public DisjointNode<?> parent = null;
    public T objects;

    public DisjointArray(T objects){
        this.objects = objects;


    }

    private class DisjointNode<T> {

    }
}
