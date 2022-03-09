package com.example.datastructures2ca1;


public class DisjointSetNode<T>{
    public DisjointSetNode<?> parent = null;
   // public DisjointNode<?> node = null;
    public T objects;
    public int size = 1, height = 1;
  //  private DisjointSetNode<T> node;

    public DisjointSetNode<?> getParent() {
        return parent;
    }
  /*  public int size() {
        int size = 0;
        DisjointSetNode<T> Current = parent;
        while (Current != null) {
            Current = Current.node;
            size++;
        }
        return size;
    }*/

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setParent(DisjointSetNode<?> parent) {
        this.parent = parent;
    }

    public T getObjects() {
        return objects;
    }

    public void setObjects(T objects) {
        this.objects = objects;
    }

    public DisjointSetNode(T objects) {
        this.objects = objects;

    }
        /**
         * Iterative method of find
         */
        public static DisjointSetNode<?> find(DisjointSetNode<?> node, int h){
            while (node.parent != null && h >=0) node = node.parent;
            return node;
        }
    /**
     * Recursive method of find that uses the ternary operator where it recursively calls itself
     * Feed this method a set and the id
     */
    public static DisjointSetNode<?> findRec(DisjointSetNode<?> node){
        return  node.parent == null ? node : findRec(node.parent);
    }

    /**
     * A quick union of disjoints from elements in p and q
     */
    public static void union(DisjointSetNode<?> p, DisjointSetNode<?> q, int h){
        find(q,h).parent=p; // The root of q is made reference of p
    }

   /* public static void unionByHeight(DisjointSetNode<?> p, DisjointSetNode<?> q, int h){
        int rootp = find(p,h).height;
        int rootq = find(q,h).height;

        int deeperRoot = rootp <rootq ? rootp : rootq;
        int shallowRoot = deeperRoot == rootp ? rootq : rootp;

        int temp = find(p,shallowRoot).height;
            temp = deeperRoot;
        if(deeperRoot == temp) {
            deeperRoot --;
            temp=deeperRoot;
        }

    }*/


}
