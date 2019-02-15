package waterJugProblem;


import java.util.*;

public class Main {

    private static void dfs (int a, int b, int c) {
        Tree Tree = new Tree(new Stack<>(), new HashSet<>(), new node(0,0,0));//new tree class object for use in dfs
        Main.Tree.generateChild(Tree, new node(0,0,0));// generate start state
        while (!Tree.getTree().isEmpty()){// run dfs until no more possible states
            node testNode = Tree.getTree().peek();//take node from head of stack to test later if there is any expansion possible
            if (Tree.getParentNode().getB() < b && Tree.getParentNode().getA() > 0){//Jug A to Jug B
                int[] pourResults = pourJug(Tree.getParentNode().getA(), Tree.getParentNode().getB(), b);
                Tree = Main.Tree.generateChild(Tree, new node(pourResults[0], pourResults[1], Tree.getParentNode().getC()));
            }
            if (Tree.getParentNode().getC() < c & Tree.getParentNode().getB() > 0){//Jug B to C
                int[] pourResults = pourJug(Tree.getParentNode().getB(), Tree.getParentNode().getC(), c);
                Tree = Main.Tree.generateChild(Tree, new node(Tree.getParentNode().getA(), pourResults[0], pourResults[1]));
            }
            if (Tree.getParentNode().getC() < c && Tree.getParentNode().getA() > 0){//Jug A to C
                int[] pourResults = pourJug(Tree.getParentNode().getA(), Tree.getParentNode().getC(), c);
                Tree = Main.Tree.generateChild(Tree, new node(pourResults[0], Tree.getParentNode().getB(), pourResults[1]));
            }
            if (Tree.getParentNode().getB() < b && Tree.getParentNode().getC() > 0){//Jug C to B
                int[] pourResults = pourJug(Tree.getParentNode().getC(), Tree.getParentNode().getB(), b);
                Tree = Main.Tree.generateChild(Tree, new node(Tree.getParentNode().getA(), pourResults[1], pourResults[0]));
            }
            if (Tree.getParentNode().getA() < a & Tree.getParentNode().getB() > 0){//Jug B to A
                int[] pourResults = pourJug( Tree.getParentNode().getB(), Tree.getParentNode().getA(), a);
                Tree = Main.Tree.generateChild(Tree, new node(pourResults[1], pourResults[0], Tree.getParentNode().getC()));
            }
            if (Tree.getParentNode().getA() < a && Tree.getParentNode().getC() > 0){//Jug C to A
                int[] pourResults = pourJug(Tree.getParentNode().getC(), Tree.getParentNode().getA(), a);
                Tree = Main.Tree.generateChild(Tree, new node(pourResults[1], Tree.getParentNode().getB(), pourResults[0]));
            }
            if (Tree.getParentNode().getA() == 0){//fill jug A
                Tree = Main.Tree.generateChild(Tree, new node(a, Tree.getParentNode().getB(), Tree.getParentNode().getC()));
            }
            if (Tree.getParentNode().getB() == 0){//fill jug B
                Tree = Main.Tree.generateChild(Tree, new node(Tree.getParentNode().getA(), b, Tree.getParentNode().getC()));
            }
            if (Tree.getParentNode().getC() == 0){//fill jug C
                Tree = Main.Tree.generateChild(Tree, new node(Tree.getParentNode().getA(), Tree.getParentNode().getB(), c));
            }
            if (Tree.getParentNode().getA() > 0){//empty jug A
                Tree = Main.Tree.generateChild(Tree, new node(0, Tree.getParentNode().getB(), Tree.getParentNode().getC()));
            }
            if (Tree.getParentNode().getB() > 0){//empty jug B
                Tree = Main.Tree.generateChild(Tree, new node(Tree.getParentNode().getA(), 0, Tree.getParentNode().getC()));
            }
            if (Tree.getParentNode().getC() > 0){//empty jug C
                Tree = Main.Tree.generateChild(Tree, new node(Tree.getParentNode().getA(), Tree.getParentNode().getB(), 0));
            }

            if (Tree.getTree().peek().equals(testNode)){
                //System.out.println("No expansion: " + Arrays.toString(Tree.getTree().peek().getValues()));
                Tree.setParentNode(Tree.getTree().pop());
            }
        }
        System.out.println(Tree.getVisited().size() + " states");
    }

    private static int[] pourJug (int childJug, int parentJug, int parentCap){//subtracts the difference from child jug transferring the amount to parent jug, whilst adding the difference to parent jug
        return new int[]{childJug-Math.min(childJug, parentCap-parentJug),parentJug+Math.min(childJug, parentCap-parentJug)};// use pourJug[0] for childJug, use pourJug[1] for Jug to be poured to.
    }
    private static boolean doesNotContain (node Node, HashSet<node> visited) {//boolean method to check if it does not contain the current node in visited
        for (node i : visited) {
            if (Arrays.equals(i.getValues(), Node.getValues())) {
                return false;
            }
        }
        return true;
    }

    static class Tree {//class for tree to use for dfs search
        Stack<node> tree;
        HashSet<node> visited;
        node parentNode;

        private Tree(Stack<node> tree, HashSet<node> visited, node parentNode) {//constructor for tree class
            this.tree = tree;
            this.visited = visited;
            this.parentNode = parentNode;
        }
        private Stack<node> getTree() { return tree; }//getters for each value in tree class
        private HashSet<node> getVisited() { return visited; }
        private void setParentNode(node parentNode) { this.parentNode = parentNode; }// setter for parent node to be tested
        private node getParentNode() { return parentNode; }

        private static Tree generateChild (Tree Tree, node currentNode) {//generate new node
            Stack<node> tree = Tree.getTree();
            HashSet<node> visited = Tree.getVisited();
            node parentNode = Tree.getParentNode();
            if (doesNotContain(currentNode, visited)){// check if visited contains node being tested
                tree.push(currentNode);
                visited.add(currentNode);//add new node to both data structures
                parentNode = tree.peek();//state node to be iterated on to the new node
                //System.out.println(Arrays.toString(currentNode.getValues()));//print new node
            }
            return new Tree(tree, visited, parentNode);//return new tree
        }
    }

    static class node {//class for node
        int a; int b; int c;
        private node (int a, int b, int c){
            this.a = a;
            this.b = b;
            this.c = c;
        }
        private int getA() { return a; }
        private int getB() { return b; }
        private int getC() { return c; }
        private int[] getValues() { return new int[]{a,b,c}; }//getters for node class
    }

    public static void main(String args[]) {
        int[] capacities = new int[3];
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter Jug Capacities...");
        for (int i = 0; i < 3; i++) {
            System.out.println("Jug" + i + ": ");
            capacities[i] = sc.nextInt();//take capacities as inputs from user
        }
        sc.close();
        dfs(capacities[0],capacities[1],capacities[2]);//run depth first search on inputs
    }
}