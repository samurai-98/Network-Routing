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

        router.height = Math.max(router.left.height, router.right.height) + 1;

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

    public boolean treeSearch(String ip) {
        Router curr = this.root;

        while (curr != null) {
            if (curr.ipAddress.equals(ip)) {
                return true;
            } else if (ip.compareTo(curr.ipAddress) < 0) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        return false;
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

    private Router getSuccessor(Router router) {
        Router curr = router.right;
        
        while (curr.left != null) {
            curr = curr.left;
        }
        return curr;
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

        int balance = getBalance(router);
        int balanceLeft = getBalance(router.left);
        int balanceRight = getBalance(router.right);

        if (balance > 1 && balanceLeft >= 0) {
            return rotateRight(router);
        }

        if (balance < -1 && balanceRight <= 0) {
            return rotateLeft(router);
        }

        if (balance > 1 && balanceLeft < 0) {
            router.left = rotateLeft(router.left);
            return rotateRight(router);
        }

        if (balance < -1 && balanceRight > 0) {
            router.right = rotateRight(router.right);
            return rotateLeft(router);
        }

        router.height = Math.max(router.left.height, router.right.height) + 1;

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
