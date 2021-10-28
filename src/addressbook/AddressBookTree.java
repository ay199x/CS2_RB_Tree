package addressbook;

class Node<T extends Comparable<T>, U extends Comparable<U>>
{
    T name;
    U office;
    Node left, right;
    Node parent;
    int color;

}

public class AddressBookTree<T extends Comparable<T>, U extends Comparable<U>>
{
   private Node root;
   private Node TNULL;

    public AddressBookTree()
    {
        TNULL = new Node();
        TNULL.color = 0;
        TNULL.name = null;
        TNULL.office = null;
        TNULL.left = null;
        TNULL.right = null;
        root = TNULL;
    }


   public void insert(T name, U office)
   {
       Node node = new Node();
       node.parent = null;
       node.name = name;
       node.office = office;
       node.right = TNULL;
       node.color = 1; // new node must be red

       Node y = null;
       Node x = this.root;

       while (x != TNULL)
       {
           y = x;
           if (node.name.compareTo(x.name.toString()) < 0)
           {
               x = x.left;
           } else {
               x = x.right;
           }
       }

       // y is parent of x
       node.parent = y;
       if (y == null)
       {
           root = node;
       }
       else if (node.name.compareTo(y.name.toString()) < 0)
       {
           y.left = node;
       } else {
           y.right = node;
       }

       // if new node is a root node, simply return
       if (node.parent == null)
       {
           node.color = 0;
           return;
       }

       // if the grandparent is null, simply return
       if (node.parent.parent == null)
       {
           return;
       }

       // Fix the tree
       fixInsert(node);
   }

   private void fixInsert(Node x)
   {
       Node y;
       while(x.parent.color == 1)
       {
           if(x.parent == x.parent.parent.left)
           {
               y = x.parent.parent.right; //uncle
               if(y.color == 1)
               {
                   x.parent.color = 0;
                   y.color = 0;
                   x.parent.parent.color = 1;
                   x = x.parent.parent;
               }

               else
               {
                   if (x == x.parent.right) {
                       x = x.parent;
                       leftRotate(x);
                   }
                   x.parent.color = 0;
                   x.parent.parent.color = 1;
                   rightRotate(x.parent.parent);
               }

           } //outer if ends here

           else
           {
               y = x.parent.parent.left; // uncle
               if (x.color == 1) {
                   x.color = 0;
                   x.parent.color = 0;
                   x.parent.parent.color = 1;
                   x = x.parent.parent;
               } else {
                   if (x == x.parent.left) {
                       // case 3.2.2
                       x = x.parent;
                       rightRotate(x);
                   }
                   // case 3.2.1
                   x.parent.color = 0;
                   x.parent.parent.color = 1;
                   leftRotate(x.parent.parent);
               }
           } //outer else ends here

       }//while loop ends here
   }

    // rotate left at node x
    public void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != TNULL)
        {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    // rotate right at node x
    public void rightRotate(Node x)
    {
        Node y = x.left;
        x.left = y.right;
        if (y.right != TNULL) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    private void deleteNodeHelper(Node node, T key)
    {
        // find the node containing key
        Node z = TNULL;
        Node x, y;
        while (node != TNULL){
            if (node.name.equals(key)) {
                z = node;
            }

            if (node.name.compareTo(key) <= 0)
            {
                node = node.right;
            } else {
                node = node.left;
            }
        }

        if (z == TNULL) {
            System.out.println("Couldn't find key in the tree");
            return;
        }

        y = z;
        int yOriginalColor = y.color;
        if (z.left == TNULL) {
            x = z.right;
            rbTransplant(z, z.right);
        } else if (z.right == TNULL) {
            x = z.left;
            rbTransplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
            } else {
                rbTransplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }

            rbTransplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if (yOriginalColor == 0)
        {
            fixDelete(x);
        }
    }
    // fix the rb tree modified by the delete operation
    private void fixDelete(Node x) {
        Node s;
        while (x != root && x.color == 0) {
            if (x == x.parent.left) {
                s = x.parent.right;
                if (s.color == 1) {
                    // case 3.1
                    s.color = 0;
                    x.parent.color = 1;
                    leftRotate(x.parent);
                    s = x.parent.right;
                }

                if (s.left.color == 0 && s.right.color == 0) {
                    // case 3.2
                    s.color = 1;
                    x = x.parent;
                } else {
                    if (s.right.color == 0) {
                        // case 3.3
                        s.left.color = 0;
                        s.color = 1;
                        rightRotate(s);
                        s = x.parent.right;
                    }

                    // case 3.4
                    s.color = x.parent.color;
                    x.parent.color = 0;
                    s.right.color = 0;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                s = x.parent.left;
                if (s.color == 1) {
                    // case 3.1
                    s.color = 0;
                    x.parent.color = 1;
                    rightRotate(x.parent);
                    s = x.parent.left;
                }

                if (s.right.color == 0 && s.right.color == 0) {
                    // case 3.2
                    s.color = 1;
                    x = x.parent;
                } else {
                    if (s.left.color == 0) {
                        // case 3.3
                        s.right.color = 0;
                        s.color = 1;
                        leftRotate(s);
                        s = x.parent.left;
                    }

                    // case 3.4
                    s.color = x.parent.color;
                    x.parent.color = 0;
                    s.left.color = 0;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = 0;
    }
    private void rbTransplant(Node u, Node v)
    {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left){
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    // find the node with the minimum key
    public Node minimum(Node node) {
        while (node.left != TNULL) {
            node = node.left;
        }
        return node;
    }

    public Node getRoot()
    {
        return this.root;
    }

    public void deleteNode(T name)
    {
        deleteNodeHelper(this.root, name);
    }

    void inorderTraversalHelper(Node node)
    {
        if(node!=null)
        {
            inorderTraversalHelper(node.left);
            System.out.printf("%s %s \n", node.name, node.office);
            inorderTraversalHelper(node.right);
        }
    }

    public void display()
    {
        inorderTraversalHelper(this.root);
    }

    public int countBlack(Node node)
    {
        int count = 0;
        if(node != null)
        {
            countBlack(node.left);

                    if(node.color == 0)
                        count++;

                    countBlack(node.right);
        }

        return count;
    }

    public int countRed(Node node)
    {
        int count = 0;
        if(node != null)
        {
            countRed(node.left);

            if(node.color == 1)
                count++;

            countRed(node.right);
        }

        return count;
    }

}//RB Tree ends here
