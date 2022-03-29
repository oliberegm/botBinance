package com.olibersystem.bot.binance.botbinance.model.graph;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Graph {
    private List<Node> nodes;
    public void addNode(Node node) {
        if(nodes == null ){
            nodes = new ArrayList<>();
        }
        nodes.add(node);
    }
}
