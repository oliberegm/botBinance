package com.olibersystem.bot.binance.botbinance.model.graph;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class Node {
    private String symbol;
    private List<Edge> edges;
    public Node(String symbol) {
        this.symbol = symbol;
    }
    public void addEdge(Edge edge){
        if ( edges == null) {
            edges = new ArrayList<>();
        }
        edges.add(edge);
    }
}
