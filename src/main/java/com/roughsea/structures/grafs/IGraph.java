package com.roughsea.structures.grafs;

public interface IGraph {

    void addNode(char label);

    void addEdge(int start, int end);

//    void displayNode(int n);

    public void setOriented(boolean oriented);

    void dfs();

    void bfs();

    void mst();

    void topo();

}
