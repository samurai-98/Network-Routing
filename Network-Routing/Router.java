import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Router {
    public String name;
    public String ipAddress;
    public int[] ipOctets;
    public int subnetMask;
    public int subnetMaskBytes;
    public byte[] subnetMaskOctets;
    public int ipAvail;
    public List<Device> ipRangeArray;
    public Router left, right;
    public String ipRangeStart;
    public String ipRangeEnd;


    //Router class constructor
    public Router (String routerName, String ip, int subnet) {
        this.ipRangeArray = new ArrayList<>();
        this.name = routerName;
        this.ipAddress = ip;
        this.ipOctets = ipOctetSplit(ip);
        this.subnetMask = subnet;
        this.subnetMaskBytes = 0xFFFFFFFF << (32 - subnet);
        getMaskOctets(subnetMaskBytes);
        this.ipAvail = calculateAvailIp();
        this.left = null;
        this.right = null;

        int[] tmpArray1 = Arrays.copyOf(this.ipOctets, this.ipOctets.length);

        for (int i = 0; i < 4; i++) {
            tmpArray1[i] = tmpArray1[i] & (this.subnetMaskBytes >> (24 - (8*i)));
        }

        this.ipRangeStart = tmpArray1[0] + "." + tmpArray1[1] + "." + tmpArray1[2] + "." + tmpArray1[3];

        int[] tmpArray2 = Arrays.copyOf(this.ipOctets, this.ipOctets.length);

        for (int i = 0; i < 4; i++) {
            tmpArray2[i] = tmpArray2[i] | (~this.subnetMaskBytes >> (24 - (8*i)));
        }

        this.ipRangeEnd = tmpArray2[0] + "." + tmpArray2[1] + "." + tmpArray2[2] + "." + tmpArray2[3];
    }

    //Prints router information and the information of its connected devices
    public void printRouterInfo() {
        System.out.println("\nRouter name: " + this.name + "\nIP address: " + this.ipAddress + "/" + this.subnetMask + "\n" + "Devices:");

        for (int i = 0; i < ipRangeArray.size(); i++) {
            System.out.println(ipRangeArray.get(i).name + " - " + ipRangeArray.get(i).ipAddress);
        }
    }

    //Connects a new device to a router
    public void addDevice(String deviceName) {
        String deviceIp = getAvailIP();
        if(deviceIp != null) {
            Device device = new Device(deviceName, deviceIp);
            this.ipRangeArray.add(device);
            ipAvail--;
            System.out.println(deviceName + " added with IP address " + deviceIp);
        } else {
            System.out.println("No available IP addresses");
        }
    }

    //Splits an IPv4 address into its individual octets and saves each one as an element in an array
    private int[] ipOctetSplit(String ip) {
        String[] parts = ip.split("\\.");
        int[] ipSections = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            ipSections[i] = Integer.parseInt(parts[i]);
        }
        return ipSections;
    }

    //splits the subnet mask into 8 bit segments and saves each segment as an array element
    private void getMaskOctets(int mask) {
        this.subnetMaskOctets = new byte[4];
        this.subnetMaskOctets[0] = (byte)((mask >> 24) & 0xFF);
        this.subnetMaskOctets[1] = (byte)((mask >> 16) & 0xFF);
        this.subnetMaskOctets[2] = (byte)((mask >> 8) & 0xFF);
        this.subnetMaskOctets[3] = (byte)(mask & 0xFF);
    }

    //Initializes the amount of available IP addresses based on the subnet mask
    private int calculateAvailIp() {
        int tmp = 32 - this.subnetMask;
        return (int) Math.pow(2, tmp) - 2;
    }

    //Generates an IP address for a device to receive 
    private String getAvailIP() {
        if (this.ipAvail == 0) {
            return null;
        }
        String currIP = this.ipRangeStart;
        int[] currOctets = ipOctetSplit(currIP);

        while (!currIP.equals(this.ipRangeEnd)) {
            boolean isAvail = true;
            for (Device device : ipRangeArray) {
                if (device.ipAddress.equals(currIP) || this.ipAddress.equals(currIP)) {
                    isAvail = false;
                    break;
                }
            }
            if (isAvail) {
                return currIP;
            }

            currOctets[3]++;
            for (int i = 3; i > 0; i--) {
                if (currOctets[i] > 255) {
                    currOctets[i] = 0;
                    currOctets[i - 1]++;
                }
            }

            currIP = currOctets[0] + "." + currOctets[1] + "." + currOctets[2] + "." + currOctets[3];
        }

        boolean isAvail = true;
        for (Device device : ipRangeArray) {
            if (device.ipAddress.equals(currIP) || this.ipAddress.equals(currIP)) {
                isAvail = false;
                break;
            }
        }
        if (isAvail) {
            return currIP;
        }
        return null;
    }
}
