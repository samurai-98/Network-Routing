import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Router {
    public String name;
    public String ipAddress;
    public int[] ipOctets;
    public int subnetMask;
    public int subnetMaskBytes;
    public byte[] subnetMaskOctets;
    public int ipAvail;
    public List<Device> deviceArray;
    public Router left, right;
    public String ipRangeStart;
    public String ipRangeEnd;
    public int height;
    public Set<String> allocatedIps = new HashSet<>();


    //Router class constructor
    public Router (String routerName, String ip, int subnet) {
        this.deviceArray = new ArrayList<>();
        this.name = routerName;
        this.ipAddress = ip;
        this.ipOctets = ipOctetSplit(ip);
        this.subnetMask = subnet;
        this.subnetMaskBytes = 0xFFFFFFFF << (32 - subnet);
        getMaskOctets(subnetMaskBytes);
        this.ipAvail = calculateAvailIp();
        this.left = null;
        this.right = null;
        this.height = 1;

        int[] tmpArray1 = Arrays.copyOf(this.ipOctets, this.ipOctets.length);

        for (int i = 0; i < 4; i++) {
            tmpArray1[i] = tmpArray1[i] & (this.subnetMaskBytes >> (24 - (8*i)));
        }

        this.ipRangeStart = octetToStr(tmpArray1);

        int[] tmpArray2 = Arrays.copyOf(this.ipOctets, this.ipOctets.length);

        for (int i = 0; i < 4; i++) {
            tmpArray2[i] = tmpArray2[i] | (~this.subnetMaskBytes >> (24 - (8*i)));
        }

        this.ipRangeEnd = octetToStr(tmpArray2);
    }

    private String octetToStr(int[] tmp) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            sb.append(tmp[i]);
            if (i < 3) {
                sb.append(".");
            }
        }
        return sb.toString();
    }

    //Prints router information and the information of its connected devices
    public void printRouterInfo() {
        System.out.println("\nRouter name: " + this.name + "\nIP address: " + this.ipAddress + "/" + this.subnetMask + "\n" + "Devices:");

        for (int i = 0; i < deviceArray.size(); i++) {
            System.out.println(deviceArray.get(i).name + " - " + deviceArray.get(i).ipAddress);
        }
    }

    //Connects a new device to a router
    public void addDevice(String deviceName) {
        String deviceIp = getAvailIP();
        if(deviceIp != null) {
            Device device = new Device(deviceName, deviceIp);
            this.deviceArray.add(device);
            this.allocatedIps.add(deviceIp);
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
            if (!allocatedIps.contains(currIP) && !this.ipAddress.equals(currIP)) {
                return currIP;
            }

            currOctets[3]++;
            for (int i = 3; i > 0; i--) {
                if (currOctets[i] > 255) {
                    currOctets[i] = 0;
                    currOctets[i - 1]++;
                }
            }

            currIP = octetToStr(currOctets);
        }

        if (!allocatedIps.contains(currIP) && !this.ipAddress.equals(currIP)) {
            return currIP;
        }
        return null;
    }

}
