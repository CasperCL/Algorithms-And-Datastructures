package adsa4;

import java.util.Stack;

/**
 *
 * @author Casper
 */
public class Tree {

    private Node root; // first node of tree
    private String orderS;
// -------------------------------------------------------------

    public Tree() // constructor
    {
        root = null;
        orderS = "";
    } // no nodes in tree yet
// -------------------------------------------------------------

    public Node find(int key) // find node with given key
    { // (assumes non-empty tree)
        Node current = root; // start at root
        while (current.id != key) // while no match,
        {
            if (key < current.id) // go left?
            {
                current = current.leftChild;
            } else // or go right?
            {
                current = current.rightChild;
            }
            if (current == null) // if no child,
            {
                return null; // didn't find it
            }
        }
        return current; // found it
    } // end find()
// -------------------------------------------------------------

    public void addTreeLeftChild(Tree t) {
        root.leftChild = t.root;
    }

    public void addTreeRightChild(Tree t) {
        root.rightChild = t.root;
    }

    public void insert(int id, char dd) {
        Node newNode = new Node(); // make new node
        newNode.id = id; // insert data
        newNode.dData = dd;
        if (root == null) // no node in root
        {
            root = newNode;
        } else // root occupied
        {
            Node current = root; // start at root
            Node parent;
            while (true) // (exits internally)
            {
                parent = current;
                if (id < current.id) // go left?
                {
                    current = current.leftChild;
                    if (current == null) // if end of the line,
                    { // insert on left
                        parent.leftChild = newNode;
                        return;
                    }
                } // end if go left
                else // or go right?
                {
                    current = current.rightChild;
                    if (current == null) // if end of the line
                    { // insert on right
                        parent.rightChild = newNode;
                        return;
                    }
                } // end else go right
            } // end while
        } // end else not root
    } // end insert()
// -------------------------------------------------------------

    public boolean delete(int key) // delete node with given key
    { // (assumes non-empty list)
        Node current = root;
        Node parent = root;
        boolean isLeftChild = true;
        while (current.id != key) // search for node
        {
            parent = current;
            if (key < current.id) // go left?
            {
                isLeftChild = true;
                current = current.leftChild;
            } else // or go right?
            {
                isLeftChild = false;
                current = current.rightChild;
            }
            if (current == null) // end of the line,
            {
                return false; // didn't find it
            }
        } // end while
        // found node to delete
        // if no children, simply delete it
        if (current.leftChild == null
                && current.rightChild == null) {
            if (current == root) // if root,
            {
                root = null; // tree is empty
            } else if (isLeftChild) {
                parent.leftChild = null; // disconnect
            } else // from parent
            {
                parent.rightChild = null;
            }
        } // if no right child, replace with left subtree
        else if (current.rightChild == null) {
            if (current == root) {
                root = current.leftChild;
            } else if (isLeftChild) {
                parent.leftChild = current.leftChild;
            } else {
                parent.rightChild = current.leftChild;
            }
        } // if no left child, replace with right subtree
        else if (current.leftChild == null) {
            if (current == root) {
                root = current.rightChild;
            } else if (isLeftChild) {
                parent.leftChild = current.rightChild;
            } else {
                parent.rightChild = current.rightChild;
            }
        } else // two children, so replace with inorder successor
        {
            // get successor of node to delete (current)
            Node successor = getSuccessor(current);
            // connect parent of current to successor instead
            if (current == root) {
                root = successor;
            } else if (isLeftChild) {
                parent.leftChild = successor;
            } else {
                parent.rightChild = successor;
            }
            // connect successor to current's left child
            successor.leftChild = current.leftChild;
        } // end else two children
// (successor cannot have a left child)
        return true; // success
    } // end delete()
// -------------------------------------------------------------
// returns node with next-highest value after delNode
// goes to right child, then right child's left descendents

    private Node getSuccessor(Node delNode) {
        Node successorParent = delNode;
        Node successor = delNode;
        Node current = delNode.rightChild; // go to right child
        while (current != null) // until no more
        { // left children,
            successorParent = successor;
            successor = current;
            current = current.leftChild; // go to left child
        }
// if successor not
        if (successor != delNode.rightChild) // right child,
        { // make connections
            successorParent.leftChild = successor.rightChild;
            successor.rightChild = delNode.rightChild;
        }
        return successor;
    }
// -------------------------------------------------------------

    public String preOrder() {
        orderS = "";
        preOrder(root);
        return orderS;
    }

    private void preOrder(Node localRoot) {
        if (localRoot != null) {
            orderS += localRoot.dData;
            preOrder(localRoot.leftChild);
            preOrder(localRoot.rightChild);
        }
    }
// -------------------------------------------------------------

    public String inOrder() {
        orderS = "";
        inOrder(root);

        // Strip the String so there are no more uncessary '(' or ')'
        char cA[] = orderS.toCharArray();
        char curr;  // Current char
        int bCount = 0; // amount of 'false' opening brackets

        for (int i = 0; i < cA.length; i++) {
            curr = cA[i];
            if (i >= 2) {
                if (curr == ')' && (cA[i - 1] >= '0' && cA[i - 1] <= '9') && cA[i - 2] == '(') { // mark all the uncessary '(' and ')'
                    cA[i - 2] = ' ';
                    cA[i] = ' ';
                    bCount++;
                }
            }
        }

        for (int i = 0; i < cA.length; i++) { // take out the spaces
            if (cA[i] == ' ') {
                for (int j = i; j < cA.length - 1; j++) {
                    cA[j] = cA[j + 1];
                }
            }
        }
        
        for (int i = cA.length - 1; i >= cA.length - (bCount * 2); i--) { // get the closing brackets out
            cA[i] = '\0';
        }
        orderS = new String(cA);
        return orderS;
    }

    private void inOrder(Node localRoot) {
        if (localRoot != null) {
            orderS += "(";
            inOrder(localRoot.leftChild);
            orderS += localRoot.dData;
            inOrder(localRoot.rightChild);
            orderS += ")";
        }
    }
// -------------------------------------------------------------

    public String postOrder() {
        orderS = "";
        postOrder(root);
        return orderS;
    }

    private void postOrder(Node localRoot) {
        if (localRoot != null) {
            postOrder(localRoot.leftChild);
            postOrder(localRoot.rightChild);
            orderS += localRoot.dData;
        }
    }

    public String getLastOrderS() {
        return orderS;
    }

    public void displayTree() {
        Stack globalStack = new Stack();
        globalStack.push(root);
        int nBlanks = 32;
        boolean isRowEmpty = false;
        System.out.println(
                "......................................................");
        while (isRowEmpty == false) {
            Stack localStack = new Stack();
            isRowEmpty = true;
            for (int j = 0; j < nBlanks; j++) {
                System.out.print(' ');
            }
            while (globalStack.isEmpty() == false) {
                Node temp = (Node) globalStack.pop();
                if (temp != null) {
                    System.out.print(temp.dData);
                    localStack.push(temp.leftChild);
                    localStack.push(temp.rightChild);
                    if (temp.leftChild != null
                            || temp.rightChild != null) {
                        isRowEmpty = false;
                    }
                } else {
                    System.out.print("--");
                    localStack.push(null);
                    localStack.push(null);
                }
                for (int j = 0; j < nBlanks * 2 - 2; j++) {
                    System.out.print(' ');
                }
            } // end while globalStack not empty
            System.out.println();
            nBlanks /= 2;
            while (localStack.isEmpty() == false) {
                globalStack.push(localStack.pop());
            }
        } // end while isRowEmpty is false
        System.out.println(
                "......................................................");
    } // end displayTree()
}
