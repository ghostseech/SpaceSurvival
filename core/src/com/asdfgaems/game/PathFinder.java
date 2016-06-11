package com.asdfgaems.game;

import com.asdfgaems.game.actors.GameActor;
import com.asdfgaems.game.actors.characters.LiveActor;
import com.asdfgaems.game.actors.objects.Door;
import com.asdfgaems.stages.GameStage;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.LinkedList;
import java.util.List;

public class PathFinder {
    private static final int DOOR_LVL_0 = 1;
    private static final int OBJECT = 2;
    private static final int LIVE_ACTOR = 3;
    private static final int COLLIDABLE = 4;
    private static final int EMPTY = 5;

    public static Array<Integer> liveActorIgnoreList = new Array<Integer>();

    static {
        liveActorIgnoreList.add(DOOR_LVL_0);
        liveActorIgnoreList.add(EMPTY);
    }

    private GameStage stage;

    private Node nodes[][];


    public PathFinder(GameStage stage) {
        this.stage = stage;
    }


    public List<Vector2> findPath(int startx, int starty, int endx, int endy, Array<Integer> ignoreList) {
        if(endx >= stage.getWorldWidth() || endx < 0 || endy >= stage.getWorldHeight() || endy < 0) return new LinkedList<Vector2>();

       // System.out.println(startx + " " + starty + " " + stage.getWorldWidth() + " " + stage.getWorldHeight());

        LinkedList<Node> openList = new LinkedList<Node>();
        LinkedList<Node> closeList = new LinkedList<Node>();

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
                LinkedList<Node> adjacent = getAdjacent(nodes, closeList, ignoreList, current, stage.getWorldWidth(), stage.getWorldHeight(), startx, starty, endx, endy);
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

    public void update(LinkedList<GameActor> objects, LinkedList<LiveActor> actors) {
        nodes = new Node[stage.getWorldWidth()][stage.getWorldHeight()];

        for(int i = 0; i < stage.getWorldWidth(); i++) {
            for(int j = 0; j < stage.getWorldHeight(); j++) {
                nodes[i][j] = new Node(i, j);
                if(stage.getTileMap().getTile(i, j).collidable) nodes[i][j].type = COLLIDABLE;
            }
        }

        for(GameActor a : objects) {
            setActor(a);
        }

        for(GameActor a : actors) {
            setActor(a);
        }
    }

    private void setActor(GameActor a) {
        int w = a.getTileWidth();
        int h = a.getTileHeight();
        int x = a.getTileX();
        int y = a.getTileY();

        int type = OBJECT;

        if(a instanceof LiveActor) type = LIVE_ACTOR;
        else if(a instanceof Door) {
            if(((Door) a).getLevel() == 0) type = DOOR_LVL_0;
        }

        for(int i = x; i < x + w; i ++) {
            for(int j = y; j < y + h; j ++) {
                nodes[i][j].type = type;
            }
        }
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

    public LinkedList<Node> getAdjacent(Node[][] nodes, LinkedList<Node> closeList, Array<Integer> ignoreList, Node node, int width, int height, int startx, int starty, int endx, int endy) {
        LinkedList<Node> result = new LinkedList<Node>();

        if(node.x > 0) {
            int tmpx = node.x - 1;
            int tmpy = node.y;
            if(ignoreList.contains(nodes[tmpx][tmpy].type, false) && !closeList.contains(nodes[tmpx][tmpy])) {
                result.add(nodes[tmpx][tmpy]);
                nodes[tmpx][tmpy].diagonaly = false;
            }
            else {
                if((tmpx == startx && tmpy == starty) || (tmpx == endx && tmpy == endy)) {
                    result.add(nodes[tmpx][tmpy]);
                    nodes[tmpx][tmpy].diagonaly = false;
                }
            }
        }
        if(node.y > 0) {
            int tmpx = node.x;
            int tmpy = node.y - 1;
            if(ignoreList.contains(nodes[tmpx][tmpy].type, false) && !closeList.contains(nodes[tmpx][tmpy])) {
                result.add(nodes[tmpx][tmpy]);
                nodes[tmpx][tmpy].diagonaly = false;
            }
            else {
                if((tmpx == startx && tmpy == starty) || (tmpx == endx && tmpy == endy)) {
                    result.add(nodes[tmpx][tmpy]);
                    nodes[tmpx][tmpy].diagonaly = false;
                }
            }
        }
        if(node.x < width) {
            int tmpx = node.x + 1;
            int tmpy = node.y;
            if(ignoreList.contains(nodes[tmpx][tmpy].type, false) && !closeList.contains(nodes[tmpx][tmpy])) {
                result.add(nodes[tmpx][tmpy]);
                nodes[tmpx][tmpy].diagonaly = false;
            }
            else {
                if((tmpx == startx && tmpy == starty) || (tmpx == endx && tmpy == endy)) {
                    result.add(nodes[tmpx][tmpy]);
                    nodes[tmpx][tmpy].diagonaly = false;
                }
            }
        }
        if(node.y < height) {
            int tmpx = node.x;
            int tmpy = node.y + 1;
            if(ignoreList.contains(nodes[tmpx][tmpy].type, false) && !closeList.contains(nodes[tmpx][tmpy])) {
                result.add(nodes[tmpx][tmpy]);
                nodes[tmpx][tmpy].diagonaly = false;
            }
            else {
                if((tmpx == startx && tmpy == starty) || (tmpx == endx && tmpy == endy)) {
                    result.add(nodes[tmpx][tmpy]);
                    nodes[tmpx][tmpy].diagonaly = false;
                }
            }
        }
        if(node.x > 0 && node.y > 0) {
            int tmpx = node.x - 1;
            int tmpy = node.y - 1;
            if(ignoreList.contains(nodes[tmpx][tmpy].type, false) && !closeList.contains(nodes[tmpx][tmpy])) {
                result.add(nodes[tmpx][tmpy]);
                nodes[tmpx][tmpy].diagonaly = true;
            }
            else {
                if((tmpx == startx && tmpy == starty) || (tmpx == endx && tmpy == endy)) {
                    result.add(nodes[tmpx][tmpy]);
                    nodes[tmpx][tmpy].diagonaly = true;
                }
            }
        }
        if(node.x > 0 && node.y < height) {
            int tmpx = node.x - 1;
            int tmpy = node.y + 1;
            if(ignoreList.contains(nodes[tmpx][tmpy].type, false) && !closeList.contains(nodes[tmpx][tmpy])) {
                result.add(nodes[tmpx][tmpy]);
                nodes[tmpx][tmpy].diagonaly = true;
            }
            else {
                if((tmpx == startx && tmpy == starty) || (tmpx == endx && tmpy == endy)) {
                    result.add(nodes[tmpx][tmpy]);
                    nodes[tmpx][tmpy].diagonaly = true;
                }
            }
        }
        if(node.x < width && node.y > 0) {
            int tmpx = node.x + 1;
            int tmpy = node.y - 1;
            if(ignoreList.contains(nodes[tmpx][tmpy].type, false) && !closeList.contains(nodes[tmpx][tmpy])) {
                result.add(nodes[tmpx][tmpy]);
                nodes[tmpx][tmpy].diagonaly = true;
            }
            else {
                if((tmpx == startx && tmpy == starty) || (tmpx == endx && tmpy == endy)) {
                    result.add(nodes[tmpx][tmpy]);
                    nodes[tmpx][tmpy].diagonaly = true;
                }
            }
        }
        if(node.x < width && node.y < height) {
            int tmpx = node.x + 1;
            int tmpy = node.y + 1;
            if(ignoreList.contains(nodes[tmpx][tmpy].type, false) && !closeList.contains(nodes[tmpx][tmpy])) {
                result.add(nodes[tmpx][tmpy]);
                nodes[tmpx][tmpy].diagonaly = true;
            }
            else {
                if((tmpx == startx && tmpy == starty) || (tmpx == endx && tmpy == endy)) {
                    result.add(nodes[tmpx][tmpy]);
                    nodes[tmpx][tmpy].diagonaly = true;
                }
            }
        }
        return result;
    }


    private class Node {
        public int x;
        public int y;
        public Node prev;
        public float g;
        public float h;
        public boolean diagonaly;
        public int type;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
            diagonaly = false;
            type = EMPTY;
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

}
