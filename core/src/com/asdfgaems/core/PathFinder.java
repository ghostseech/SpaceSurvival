package com.asdfgaems.core;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class PathFinder {
    LinkedList<Node> openList = new LinkedList<Node>();
    LinkedList<Node> closeList = new LinkedList<Node>();
    Node nodes[][];
    World world;

    public PathFinder(World world) {
        this.world = world;
    }
    public List<Vector2> findPath(int startx, int starty, int endx, int endy) {
        if(endx >= world.getMapWidth() || endx < 0 || endy >= world.getMapHeight() || endy < 0) return new LinkedList<Vector2>();
        nodes = prepareMap(world);
        nodes[endx][endy] = new Node(endx, endy);
        nodes[startx][starty] = new Node(startx, starty);
        //if(nodes[endx][endy] == null) return new LinkedList<Vector2>();

        openList = new LinkedList<Node>();
        closeList = new LinkedList<Node>();

        openList.add(nodes[startx][starty]);

        boolean done = false;
        while (!done) {
            Node current = findLowestF(openList);
            closeList.add(current);
            openList.remove(current);
            if(current.x == endx && current.y == endy) {
                List<Node> resultPath = calculatePath(nodes[startx][starty], current);
                List<Vector2> resultCoords = new LinkedList<Vector2>();
                for(Node n : resultPath) {
                    resultCoords.add(new Vector2(n.x, n.y));
                }
                return resultCoords;
            }
            else {
                LinkedList<Node> adjacent = getAdjacent(nodes, current, world.getMapWidth(), world.getMapHeight());
                for(Node currentAdj : adjacent) {
                    if(!openList.contains(currentAdj)) {
                        currentAdj.prev = current;
                        currentAdj.sethcost(nodes[endx][endy]);
                        currentAdj.setgcost(current);
                        openList.add(currentAdj);
                    }
                    else {
                        if(currentAdj.g > currentAdj.calculategcost(current)) {
                            currentAdj.prev = current;
                            currentAdj.setgcost(current);
                        }
                    }
                }

                if(openList.isEmpty()) {
                    return new LinkedList<Vector2>();
                }
            }
        }

        return null;
    }

    public List<Node> calculatePath(Node start, Node goal) {
        LinkedList<Node> path = new LinkedList<Node>();

        if(goal.x == start.x && goal.y == start.y) return path;

        Node cur = goal;

        boolean done = false;
        while(!done) {
            path.addFirst(cur);
            cur = cur.prev;
            if(cur.x == start.x && cur.y == start.y) done = true;
        }

        return path;
    }
    public Node findLowestF(LinkedList<Node> nodes) {
        Node result = nodes.get(0);
        for(int i = 0; i < nodes.size(); i++) {
            if(nodes.get(i).getfcost() < result.getfcost()) result = nodes.get(i);
        }
        return result;
    }
    public LinkedList<Node> getAdjacent(Node[][] nodes, Node node, int width, int height) {
        LinkedList<Node> result = new LinkedList<Node>();
        if(node.x > 0) {
            int tmpx = node.x - 1;
            int tmpy = node.y;
            if(nodes[tmpx][tmpy] != null && !closeList.contains(nodes[tmpx][tmpy])) {
                result.add(nodes[tmpx][tmpy]);
                nodes[tmpx][tmpy].diagonaly = false;
            }
        }
        if(node.y > 0) {
            int tmpx = node.x;
            int tmpy = node.y - 1;
            if(nodes[tmpx][tmpy] != null && !closeList.contains(nodes[tmpx][tmpy])) {
                result.add(nodes[tmpx][tmpy]);
                nodes[tmpx][tmpy].diagonaly = false;
            }
        }
        if(node.x < width) {
            int tmpx = node.x + 1;
            int tmpy = node.y;
            if(nodes[tmpx][tmpy] != null && !closeList.contains(nodes[tmpx][tmpy])) {
                result.add(nodes[tmpx][tmpy]);
                nodes[tmpx][tmpy].diagonaly = false;
            }
        }
        if(node.y < height) {
            int tmpx = node.x;
            int tmpy = node.y + 1;
            if(nodes[tmpx][tmpy] != null && !closeList.contains(nodes[tmpx][tmpy])) {
                result.add(nodes[tmpx][tmpy]);
                nodes[tmpx][tmpy].diagonaly = false;
            }
        }
        if(node.x > 0 && node.y > 0) {
            int tmpx = node.x - 1;
            int tmpy = node.y - 1;
            if(nodes[tmpx][tmpy] != null && !closeList.contains(nodes[tmpx][tmpy])) {
                result.add(nodes[tmpx][tmpy]);
                nodes[tmpx][tmpy].diagonaly = true;
            }
        }
        if(node.x > 0 && node.y < height) {
            int tmpx = node.x - 1;
            int tmpy = node.y + 1;
            if(nodes[tmpx][tmpy] != null && !closeList.contains(nodes[tmpx][tmpy])) {
                result.add(nodes[tmpx][tmpy]);
                nodes[tmpx][tmpy].diagonaly = true;
            }
        }
        if(node.x < width && node.y > 0) {
            int tmpx = node.x + 1;
            int tmpy = node.y - 1;
            if(nodes[tmpx][tmpy] != null && !closeList.contains(nodes[tmpx][tmpy])) {
                result.add(nodes[tmpx][tmpy]);
                nodes[tmpx][tmpy].diagonaly = true;
            }
        }
        if(node.x < width && node.y < height) {
            int tmpx = node.x + 1;
            int tmpy = node.y + 1;
            if(nodes[tmpx][tmpy] != null && !closeList.contains(nodes[tmpx][tmpy])) {
                result.add(nodes[tmpx][tmpy]);
                nodes[tmpx][tmpy].diagonaly = true;
            }

        }
        return result;
    }

    private Node[][] prepareMap(World world) {
        Node nodes[][] = new Node[world.getMapWidth()][world.getMapHeight()];

        for(int i = 0; i < world.getMapWidth(); i++) {
            for(int j = 0; j < world.getMapWidth(); j++) {
                if(world.isCollidable(i, j)) nodes[i][j] = null;
                else nodes[i][j] = new Node(i, j);
            }
        }
        return nodes;
    }

    private class Node {
        public int x;
        public int y;
        public Node prev;
        public float g;
        public float h;
        public boolean diagonaly;
        public Node(int x, int y) {
            this.x = x;
            this.y = y;
            diagonaly = false;
        }

        public void sethcost(Node n) {
            h = (Math.abs(x - n.x) + Math.abs(y - n.y)) * 10;
        }

        public void setgcost(Node n) {
            if(diagonaly) g = n.g + 14;
            else g = n.g + 10;
        }

        public int calculategcost(Node n) {
            if(diagonaly) return ((int)n.g + 14);
            else return ((int)n.g + 10);
        }

        public float getfcost() {
            return h + g;
        }
    }

    public void dispose() {

    }

}
