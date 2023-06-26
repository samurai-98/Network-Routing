import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
//import java.net.InetAddress;
//import java.net.UnknownHostException;

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

    public Router (String routerName, String ip, int subnet, String rangeStart, String rangeEnd) {
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
        this.ipRangeStart = rangeStart;
        this.ipRangeEnd = rangeEnd;
        this.ipRangeArray = new ArrayList<>();
    }

    public void printRouterInfo() {
        System.out.println("\nRouter name: " + this.name + "\nIP address: " + this.ipAddress + "/" + this.subnetMask + "\n" + "Devices:");

        for (int i = 0; i < ipRangeArray.size(); i++) {
            System.out.println(ipRangeArray.get(i).name + " - " + ipRangeArray.get(i).ipAddress);
        }
    }

    public void addDevice(String deviceName) /*throws UnknownHostException*/ {
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

    private int[] ipOctetSplit(String ip) {
        String[] parts = ip.split("\\.");
        int[] ipSections = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            ipSections[i] = Integer.parseInt(parts[i]);
        }
        return ipSections;
    }

    private void getMaskOctets(int mask) {
        this.subnetMaskOctets = new byte[4];
        this.subnetMaskOctets[0] = (byte)((mask >> 24) & 0xFF);
        this.subnetMaskOctets[1] = (byte)((mask >> 16) & 0xFF);
        this.subnetMaskOctets[2] = (byte)((mask >> 8) & 0xFF);
        this.subnetMaskOctets[3] = (byte)(mask & 0xFF);
    }

    private int calculateAvailIp() {
        int tmp = 32 - this.subnetMask;
        return (int) Math.pow(2, tmp) - 2;
    }

    private String getAvailIP() {
        String currIP = this.ipRangeStart;
        int[] currOctets = ipOctetSplit(currIP);
        //int[] endOctets = ipOctetSplit(this.ipRangeEnd);

        while (!currIP.equals(this.ipRangeEnd)) {
            boolean isAvail = true;
            for (Device device : ipRangeArray) {
                if (device.ipAddress.equals(currIP)) {
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
            if (device.ipAddress.equals(currIP)) {
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
