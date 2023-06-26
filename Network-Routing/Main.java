import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {

    public static void printMenu() {
        System.out.println("1: Add a new router");
        System.out.println("2: Search for an IP address");
        System.out.println("3: Inorder tree traversal");
        System.out.println("4: Print a router's information");
        System.out.println("5: Remove a router");
        System.out.println("6: Add a device to a router");
        System.out.println("7: Exit");
    }

    public static void main(String[] args) throws UnknownHostException {
        // This is just a test scenario to guarantee that all functions work
        BST testTree = new BST();
        boolean cont = true;
        int decision;

        Scanner sc = new Scanner(System.in);

        printMenu();
        
        while (cont == true) {
            System.out.println("What would you like to do? (Type '8' to see the options): ");

            decision = sc.nextInt();
            sc.nextLine();

            if (decision == 1) {

                System.out.println("[ADD] Router name: ");
                String newName = sc.nextLine();
                System.out.println("[ADD] IP address: ");
                String address = sc.nextLine();
                System.out.println("[ADD] Subnet mask: ");
                int subnet = sc.nextInt();
                sc.nextLine();
                System.out.println("[ADD] IP range start: ");
                String start = sc.nextLine();
                System.out.println("[ADD] IP range end: ");
                String end = sc.nextLine();
                testTree.insert(newName, address, subnet, start, end);

            } else if(decision == 2) {

                System.out.println("[SEARCH] IP address: ");
                String address = sc.nextLine();
                if(testTree.treeSearch(address)) {
                    System.out.println(address + " is in the network");
                } else {
                    System.out.println("IP address not found");
                }

            } else if (decision == 3){

                testTree.inorderTraversal();

            } else if (decision == 4){

                System.out.println("[PRINT] IP address: ");
                String address = sc.nextLine();
                testTree.printRouter(address);

            } else if (decision == 5) {

                System.out.println("[REMOVE] IP address: ");
                String address = sc.nextLine();
                testTree.remove(address);

            } else if (decision == 6) {

                System.out.println("[NEW DEVICE] IP address: ");
                String address = sc.nextLine();
                System.out.println("[NEW DEVICE] Name: ");
                String newName = sc.nextLine();
                testTree.addDeviceToRouter(address, newName);

            } else if (decision == 7) {
                cont = false;
            } else if (decision == 8) {
                printMenu();
            }
        }
        sc.close();
    }
}
