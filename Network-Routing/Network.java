import java.lang.Math;

public class Network {

    private Router root;

    public Network() {
        this.root = null;
    }

    private int getHeight(Router router) {
        if (router == null) {
            return 0;
        }
        return router.height;
    }

    private int getBalance(Router router) {
        if (router == null) {
            return 0;
        }
        return getHeight(router.left) - getHeight(router.right);
    }

    private Router recursiveInsert(Router router, String deviceName, String ip, int subnet) {
        if (router == null) {
            return new Router(deviceName, ip, subnet);
        }

        if (ip.compareTo(router.ipAddress) < 0) {
            router.left = recursiveInsert(router.left, deviceName, ip, subnet);
        } else {
            router.right = recursiveInsert(router.right, deviceName, ip, subnet);
        }

        //Begin AVL section

        int leftHeight = (router.left != null) ? router.left.height : 0;
        int rightHeight = (router.right != null) ? router.right.height : 0;

        router.height = Math.max(leftHeight, rightHeight) + 1;

        int balance = getBalance(router);

        if (balance > 1 && ip.compareTo(router.left.ipAddress) < 0) {
            return rotateRight(router);
        }

        if (balance < -1 && ip.compareTo(router.right.ipAddress) > 0) {
            return rotateLeft(router);
        }

        if (balance > 1 && ip.compareTo(router.left.ipAddress) > 0) {
            router.left = rotateLeft(router.left);
            return rotateRight(router);
        }

        if (balance < -1 && ip.compareTo(router.right.ipAddress) < 0) {
            router.right = rotateRight(router.right);
            return rotateLeft(router);
        }

        return router;
    }


    public void insert(String deviceName, String ip, int subnet) {
        if (treeSearch(ip)) {
            System.out.println(ip + " already exists in the network.");
            return;
        } else {
            this.root = recursiveInsert(this.root, deviceName, ip, subnet);
        }
    }

    private boolean recursiveSearch(Router router, String ip) {
        if (router == null) {
            return false;
        } else if (router.ipAddress.equals(ip)) {
            return true;
        } else if (ip.compareTo(router.ipAddress) < 0) {
            return recursiveSearch(router.left, ip);
        } else  {
            return recursiveSearch(router.right, ip);
        }
    }

    public boolean treeSearch(String ip) {
        return recursiveSearch(this.root, ip);
    }

    private void recursiveInorder(Router router) {
        if (router != null) {
            recursiveInorder(router.left);
            router.printRouterInfo();
            recursiveInorder(router.right);
        }
    }

    public void inorderTraversal() {
        recursiveInorder(this.root);
    }

    private Router recursiveFindRouter(Router router, String ip) {
        if (router.ipAddress.equals(ip)) {
            return router;
        } else if (ip.compareTo(router.ipAddress) < 0) {
            return recursiveFindRouter(router.left, ip);
        } else {
            return recursiveFindRouter(router.right, ip);
        }
    }

    public void printRouter(String ip) {
        if (treeSearch(ip)) {
            recursiveFindRouter(this.root, ip).printRouterInfo();
        } else {
            System.out.println("Router not found");
        }
    }

    private Router recursiveGetSuccessor(Router router) {
        while(router.left != null) {
            router = router.left;
        }
        return router;
    }

    private Router getSuccessor(Router router) {
       return recursiveGetSuccessor(router.right);
    }

    private Router removeHelper(Router router, String ip) {
        if (router == null) {
            return null;
        }

        if (ip.compareTo(router.ipAddress) < 0) {
            router.left = removeHelper(router.left, ip);
        } else if (ip.compareTo(router.ipAddress) > 0) {
            router.right = removeHelper(router.right, ip);
        } else {
            if (router.left == null) {
                return router.right;
            } else if (router.right == null) {
                return router.left;
            } else {
                Router successor = getSuccessor(router.right);
                router.name = successor.name;
                router.ipAddress = successor.ipAddress;
                router.subnetMask = successor.subnetMask;
                router.right = removeHelper(router.right, successor.ipAddress);
            }
        }

        //Begin AVL section

        int leftHeight = (router.left != null) ? router.left.height : 0;
        int rightHeight = (router.right != null) ? router.right.height : 0;

        router.height = Math.max(leftHeight, rightHeight) + 1;

        int balance = getBalance(router);

        if (balance > 1 && getBalance(router.left) >= 0) {
            return rotateRight(router);
        }

        if (balance < -1 && getBalance(router.right) <= 0) {
            return rotateLeft(router);
        }

        if (balance > 1 && getBalance(router.left) < 0) {
            router.left = rotateLeft(router.left);
            return rotateRight(router);
        }

        if (balance < -1 && getBalance(router.right) > 0) {
            router.right = rotateRight(router.right);
            return rotateLeft(router);
        }

        return router;
    }

    public void remove(String ip) {
        if (treeSearch(ip)) {
            this.root = removeHelper(this.root, ip);
        } else {
            System.out.println("IP address not found");
        }
    }

    public void addDeviceToRouter(String routerIp, String deviceName) {
        if (treeSearch(routerIp)) {
            recursiveFindRouter(this.root, routerIp).addDevice(deviceName);
        } else {
            System.out.println("Router not found");
        }
    }

    private Router rotateRight(Router router) {
        Router tmp1 = router.left;
        Router tmp2 = tmp1.right;

        tmp1.right = router;
        router.left = tmp2;

        router.height = Math.max(getHeight(router.left), getHeight(router.right)) + 1;
        tmp1.height = Math.max(getHeight(tmp1.left), getHeight(tmp1.right)) + 1;


        return tmp1;
    }

    private Router rotateLeft(Router router) {
        Router tmp1 = router.right;
        Router tmp2 = tmp1.left;

        tmp1.left = router;
        router.right = tmp2;

        router.height = Math.max(getHeight(router.left), getHeight(router.right)) + 1;
        tmp1.height = Math.max(getHeight(tmp1.left), getHeight(tmp1.right)) + 1;


        return tmp1;
    }
}
