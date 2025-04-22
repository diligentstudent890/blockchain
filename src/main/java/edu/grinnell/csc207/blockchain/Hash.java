package edu.grinnell.csc207.blockchain;
import java.util.Arrays;

/**
 * A wrapper class over a hash value (a byte array).
 */
public class Hash {
    /** The internal byte array storing the hash value. */
    private final byte[] data;

    /**
     * Constructs a new hash object by copying the provided byte array.
     *
     * @param data the byte array representing the hash
     * @throws NullPointerException if data is null
     */
    public Hash(byte[] data) {
        if (data == null) {
            throw new NullPointerException("Hash data cannot be null");
        }
        this.data = Arrays.copyOf(data, data.length);
    }

    /**
     * Returns a copy of the underlying byte array.
     *
     * @return a new byte array containing the hash data
     */
    public byte[] getData() {
        return Arrays.copyOf(data, data.length);
    }

    /**
     * Determines if this hash meets our custom "validity" criterion:
     * the first three bytes must be zero.
     *
     * @return true if the first three bytes are zero;
     *         false otherwise
     */
    public boolean isValid() {
        return data.length >= 3
            && data[0] == 0
            && data[1] == 0
            && data[2] == 0;
    }

    /**
     * Compares this hash to another object for equality.
     * Two hash objects are equal if their underlying byte arrays
     * match exactly.
     *
     * @param other the object to compare to
     * @return true if both objects represent the same hash;
     *         false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Hash)) return false;
        Hash o = (Hash) other;
        return Arrays.equals(this.data, o.data);
    }

    /**
     * Returns a string representation of this hash in hexadecimal format.
     *
     * @return a hexadecimal string representing the hash
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
