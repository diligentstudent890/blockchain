package edu.grinnell.csc207.blockchain;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * A single block of a blockchain.
 */
public class Block {
    /** The block number in the chain. */
    private final int num;
    /** The transaction amount (positive for Bob->Anna, negative for Anna->Bob). */
    private final int amount;
    /** The nonce used to produce a valid hash. */
    private final long nonce;
    /** The hash of the previous block. */
    private final Hash prevHash;
    /** The computed hash of this block. */
    private final Hash hash;

    /**
     * Constructs a new block by searching for a nonce that yields a valid hash.
     * This is effectively the "mining" operation.
     *
     * @param num      the block number
     * @param amount   the transaction amount
     * @param prevHash the hash of the previous block for the genesis block)
     */
    public Block(int num, int amount, Hash prevHash) {
        this.num = num;
        this.amount = amount;
        this.prevHash = prevHash;

        long tentativeNonce = 0;
        Hash tentativeHash;
        do {
            tentativeHash = computeHash(num, amount, tentativeNonce, prevHash);
            if (!tentativeHash.isValid()) {
                tentativeNonce++;
            }
        } while (!tentativeHash.isValid());

        this.nonce = tentativeNonce;
        this.hash = tentativeHash;
    }

    /**
     * Constructs a new block using a provided nonce (no repeated searching).
     *
     * @param num      the block number
     * @param amount   the transaction amount
     * @param prevHash the hash of the previous block (may be null for the genesis block)
     * @param nonce    the nonce used to compute this block's hash
     */
    public Block(int num, int amount, Hash prevHash, long nonce) {
        this.num = num;
        this.amount = amount;
        this.prevHash = prevHash;
        this.nonce = nonce;
        this.hash = computeHash(num, amount, nonce, prevHash);
    }

    /**
     * Computes the SHA-256 hash over the concatenation of:
     * num, amount, nonce, prevHash.getData() (if prevHash is not null).
     *
     * @param num      the block number
     * @param amount   the transaction amount
     * @param nonce    the nonce used to attempt to produce a valid hash
     * @param prevHash the previous block's hash
     * @return a new hash object containing the SHA-256 digest
     * @throws RuntimeException if the SHA-256 algorithm is not available
     */
    private static Hash computeHash(int num, int amount, long nonce, Hash prevHash) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            int capacity = Integer.BYTES + Integer.BYTES + Long.BYTES;
            if (prevHash != null) {
                capacity += prevHash.getData().length;
            }
            ByteBuffer buffer = ByteBuffer.allocate(capacity);

            buffer.putInt(num);
            buffer.putInt(amount);
            buffer.putLong(nonce);
            if (prevHash != null) {
                buffer.put(prevHash.getData());
            }

            byte[] digest = md.digest(buffer.array());
            return new Hash(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    /**
     * Gets the block number.
     *
     * @return the block number
     */
    public int getNum() {
        return num;
    }

    /**
     * Gets the transaction amount.
     *
     * @return the transaction amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Gets the nonce used for this block.
     *
     * @return the nonce
     */
    public long getNonce() {
        return nonce;
    }

    /**
     * Gets the hash of the previous block.
     *
     * @return the previous block's hash, or null if this is the genesis block
     */
    public Hash getPrevHash() {
        return prevHash;
    }

    /**
     * Gets the hash of this block.
     *
     * @return this block's Hash
     */
    public Hash getHash() {
        return hash;
    }

    /**
     * Returns a string representation of this block, including its number,
     * amount, nonce, previous hash, and current hash.
     *
     * @return a string describing this block
     */
    @Override
    public String toString() {
        return String.format(
            "Block %d (Amount: %d, Nonce: %d, prevHash: %s, hash: %s)",
            num, amount, nonce,
            (prevHash == null ? "null" : prevHash.toString()),
            (hash == null ? "null" : hash.toString())
        );
    }
}