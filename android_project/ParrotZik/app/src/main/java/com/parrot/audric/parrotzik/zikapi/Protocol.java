package com.parrot.audric.parrotzik.zikapi;

public class Protocol {
    // Byte array to send to open a session with device
    public static final byte[] start = {0, 0, 0};

    // Get byte array to send to get information corresponding to API request

    public static final byte[] getRequest(String request) {
        return Protocol.pack("GET " + request);
    }

    // Get byte array to send to set values to corresponding API request
    public static final byte[] setRequest(String request, String arguments) {
        return Protocol.pack("SET " + request + "?arg=" + arguments);
    }


    // TODO function getting an XML from a byte array: array[7:]


    private static final byte[] pack(String request) {
        // Protocol is the following: first 2 bytes are packet size, then a minimal
        // value byte, and then the bytes of the string message
        int size = request.length() + 3;
        byte[] ret = new byte[size];

        ret[0] = (byte) ((size >> 8) & 0xff);
        ret[1] = (byte) (size & 0xff);
        ret[2] = Byte.MIN_VALUE;

        byte[] requestAsBytes = request.getBytes();

        for (int i = 0; i < requestAsBytes.length; i++) {
            ret[i + 3] = requestAsBytes[i];
        }

        return ret;
    }

}