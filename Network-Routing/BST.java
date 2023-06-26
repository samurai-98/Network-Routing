import java.net.UnknownHostException;

public class BST {

    private Router root;

    public BST() {
        this.root = null;
    }

    private Router recursiveInsert(Router router, String deviceName, String ip, int subnet, String ipStart, String ipEnd) {
        if (router == null) {
            return new Router(deviceName, ip, subnet, ipStart, ipEnd);
        }

        if (ip.compareTo(router.ipAddress) < 0) {
            router.left = recursiveInsert(router.left, deviceName, ip, subnet, ipStart, ipEnd);
        } else {
            router.right = recursiveInsert(router.right, deviceName, ip, subnet, ipStart, ipEnd);
        }

        return router;
    }

    public void insert(String deviceName, String ip, int subnet, String ipStart, String ipEnd) {
        if (treeSearch(ip)) {
            System.out.println(ip + " already exists in the network.");
            return;
        } else {
            this.root = recursiveInsert(this.root, deviceName, ip, subnet, ipStart, ipEnd);
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
            System.out.println(router.ipAddress + " ");
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
        while(router.left != null) {
            router = router.left;
        }
        return router;
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

        return router;
    }

    public void remove(String ip) {
        if (treeSearch(ip)) {
            this.root = removeHelper(this.root, ip);
        } else {
            System.out.println("IP address not found");
        }
    }

    public void addDeviceToRouter(String routerIp, String deviceName) throws UnknownHostException {
        if (treeSearch(routerIp)) {
            recursiveFindRouter(this.root, routerIp).addDevice(deviceName);
        } else {
            System.out.println("Router not found");
        }
    }
}
