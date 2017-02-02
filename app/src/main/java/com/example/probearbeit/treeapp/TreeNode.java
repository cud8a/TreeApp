package com.example.probearbeit.treeapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by probearbeit on 01.02.17.
 */
public class TreeNode {
    public enum Traverse {
        DEPTH_FIRST, BREADTH_FIRST
    }

    private String name;
    private TreeNode parent;
    private List<TreeNode> children;
    private int nextChildIndexForTraverse = 0;
    private boolean visited = false;
    private Traverse traverse;

    public TreeNode(JSONObject tree, TreeNode parent, Traverse traverse) throws Exception {
        name = tree.getString("name");
        children = new ArrayList();
        this.parent = parent;
        this.traverse = traverse;
        JSONArray chil = tree.getJSONArray("children");
        for (int i = 0; i < chil.length(); i++) {
            children.add(new TreeNode(chil.getJSONObject(i), this, traverse));
        }
    }

    public boolean isVisited() {
        return visited;
    }

    public boolean isRoot() {
        return parent == null;
    }

    private void resetTraverseState() {
        visited = false;
        nextChildIndexForTraverse = 0;

        for (TreeNode child: children) {
            child.resetTraverseState();
        }
    }

    public void setTraverse(Traverse traverse) {
        this.traverse = traverse;
    }

    public void traverse() {
        if (traverse == Traverse.DEPTH_FIRST) {
            depthFirstTraverse("");
        } else {
            breadthFirstTraverse("");
        }

        resetTraverseState();
    }

    public boolean find(String name) {
        boolean found = false;
        if (traverse == Traverse.DEPTH_FIRST) {
            found = depthFirstTraverse(name);
        } else {
            found = breadthFirstTraverse(name);
        }

        resetTraverseState();

        return found;
    }

    private boolean depthFirstTraverse(String name) {
        boolean found = visit(name);
        if (found) {
            return true;
        }

        if (children.size() > nextChildIndexForTraverse) {
            found = children.get(nextChildIndexForTraverse++).depthFirstTraverse(name);
        }

        if (found) {
            return true;
        }

        if (!isRoot()) {
            found = parent.depthFirstTraverse(name);
        }

        return found;
    }

    private boolean breadthFirstTraverse(String name) {
        boolean found = visit(name);
        if (found) {
            return true;
        }

        ArrayList<TreeNode> breadthFirstQueue = new ArrayList();
        found = visitChildren(name, breadthFirstQueue);
        if (found) {
            return true;
        }

        ArrayList<TreeNode> newQueue = null;
        do {
            newQueue = new ArrayList();
            for (TreeNode child: breadthFirstQueue) {
                found = child.visitChildren(name, newQueue);
                if (found) {
                    break;
                }
            }
            breadthFirstQueue = newQueue;
        } while (newQueue.size() > 0 && !found);

        return found;
    }

    public boolean visit(String name) {
        if (!visited) {
            MainActivity.log("name: " + this.name);
            visited = true;
        } else {
            MainActivity.log("---------------> name: (" + this.name + ")");
        }

        if (name.equals(this.name)) {
            return true;
        }

        return false;
    }

    public boolean visitChildren(String name, ArrayList<TreeNode> queue) {
        boolean found = false;
        for (TreeNode child: children) {
            found = child.visit(name);
            if (found) {
                break;
            }
            queue.add(child);
        }

        return found;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }
}
