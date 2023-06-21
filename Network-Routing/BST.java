public class BST {
    class Node {
        private String name;
        private String ipAddress;
        private String subnetMask;
        private Node left, right;

        public Node (String deviceName, String ip, String subnet) {
            this.name = deviceName;
            this.ipAddress = ip;
            this.subnetMask = subnet;
            this.left = null;
            this.right = null;
        }

        public void printNodeInfo() {
            System.out.println("\nDevice name: " + this.name + "\nIP address: " + this.ipAddress + "\nSubnet mask: " + this.subnetMask + "\n");
        }
    }

    private Node root;

    public BST() {
        this.root = null;
    }

    private Node recursiveInsert(Node rootNode, String deviceName, String ip, String subnet) {
        if (rootNode == null) {
            return new Node(deviceName, ip, subnet);
        }

        if (ip.compareTo(rootNode.ipAddress) < 0) {
            rootNode.left = recursiveInsert(rootNode.left, deviceName, ip, subnet);
        } else {
            rootNode.right = recursiveInsert(rootNode.right, deviceName, ip, subnet);
        }

        return rootNode;
    }

    public void insert(String deviceName, String ip, String subnet) {
        if (treeSearch(ip)) {
            System.out.println(ip + " already exists in the network.");
            return;
        } else {
            this.root = recursiveInsert(this.root, deviceName, ip, subnet);
        }
    }

    private boolean recursiveSearch(Node rootNode, String ip) {
        if (rootNode == null) {
            return false;
        } else if (rootNode.ipAddress.equals(ip)) {
            return true;
        } else if (ip.compareTo(rootNode.ipAddress) < 0) {
            return recursiveSearch(rootNode.left, ip);
        } else  {
            return recursiveSearch(rootNode.right, ip);
        }
    }

    public boolean treeSearch(String ip) {
        return recursiveSearch(this.root, ip);
    }

    private void recursiveInorder(Node rootNode) {
        if (rootNode != null) {
            recursiveInorder(rootNode.left);
            System.out.println(rootNode.ipAddress + " ");
            recursiveInorder(rootNode.right);
        }
    }

    public void inorderTraversal() {
        recursiveInorder(this.root);
    }

    private Node recursiveFindNode(Node rootNode, String ip) {
        if (rootNode.ipAddress.equals(ip)) {
            return rootNode;
        } else if (ip.compareTo(rootNode.ipAddress) < 0) {
            return recursiveFindNode(rootNode.left, ip);
        } else {
            return recursiveFindNode(rootNode.right, ip);
        }
    }

    public void printNode(String ip) {
        if (treeSearch(ip)) {
            recursiveFindNode(this.root, ip).printNodeInfo();
        } else {
            System.out.println("IP address not found");
        }
    }

    private Node getSuccessor(Node node) {
        while(node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node removeHelper(Node rootNode, String ip) {
        if (rootNode == null) {
            return null;
        }

        if (ip.compareTo(rootNode.ipAddress) < 0) {
            rootNode.left = removeHelper(rootNode.left, ip);
        } else if (ip.compareTo(rootNode.ipAddress) > 0) {
            rootNode.right = removeHelper(rootNode.right, ip);
        } else {
            if (rootNode.left == null) {
                return rootNode.right;
            } else if (rootNode.right == null) {
                return rootNode.left;
            } else {
                Node successor = getSuccessor(rootNode.right);
                rootNode.name = successor.name;
                rootNode.ipAddress = successor.ipAddress;
                rootNode.subnetMask = successor.subnetMask;
                rootNode.right = removeHelper(rootNode.right, successor.ipAddress);
            }
        }

        return rootNode;
    }

    public void remove(String ip) {
        if (treeSearch(ip)) {
            this.root = removeHelper(this.root, ip);
        } else {
            System.out.println("IP address not found");
        }
    }
}