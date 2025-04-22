package edu.grinnell.csc207.blockchain;
import java.util.NoSuchElementException;

/**
 * A linked list of hash-consistent blocks representing a ledger of
 * monetary transactions.
 */
public class BlockChain {

    /**
     * A singly linked node that holds a block and a reference
     * to the next node in the chain.
     */
    private static class Node {
        /** The block stored in this node. */
        Block block;
        /** The next node in the chain. */
        Node next;

        /**
         * Constructs a node with the specified block.
         *
         * @param b the block to store in this node
         */
        Node(Block b) {
            this.block = b;
            this.next = null;
        }
    }

    /** The first node (genesis block) in the chain. */
    private Node first;
    /** The last node (most recently added block) in the chain. */
    private Node last;
    /** The number of blocks in the chain. */
    private int size;

    /**
     * Constructs a new blockchain with a genesis block.
     * The genesis block has block number 0, the specified initial amount,
     * and a null previous hash. It is mined automatically (searching for a valid nonce).
     *
     * @param initial the initial amount to set Anna's balance
     */
    public BlockChain(int initial) {
        // Create the genesis block
        Block genesis = new Block(0, initial, null);
        Node genesisNode = new Node(genesis);
        this.first = genesisNode;
        this.last = genesisNode;
        this.size = 1;
    }

    /**
     * Mines a new block by searching for a nonce that yields a valid hash.
     * The new block's number is the current size of the chain, and its
     * prevHash is the hash of the current last block.
     *
     * @param amount the transaction amount for the new block
     * @return the newly mined Block
     */
    public Block mine(int amount) {
        Hash prevHash = last.block.getHash();
        Block newBlock = new Block(size, amount, prevHash);
        append(newBlock);
        return newBlock;
    }

    /**
     * Appends an already-mined block (or a block with a chosen nonce) to the end of the chain.
     * @param newBlock the Block to append
     * @throws IllegalArgumentException if newBlock's prevHash does not match
     *         the current last block's hash
     */
    public void append(Block newBlock) {
        Hash expectedPrevHash = last.block.getHash();
        if (!expectedPrevHash.equals(newBlock.getPrevHash())) {
            throw new IllegalArgumentException("Block's prevHash does
            not match the last block's hash!");
        }
        Node newNode = new Node(newBlock);
        last.next = newNode;
        last = newNode;
        size++;
    }

    /**
     * Removes the last block from the chain. The original block cannot be removed.
     * If there is only one block in the chain, this method throws a
     * NoSuchElementException.
     *
     * @throws NoSuchElementException if an attempt is made to remove the genesis block
     */
    public void removeLast() {
        if (size <= 1) {
            throw new NoSuchElementException("Cannot remove the genesis block from the chain.");
        }
        // Find the second-to-last node
        Node current = first;
        while (current.next != last) {
            current = current.next;
        }
        // Now current is the second-to-last node
        current.next = null;
        last = current;
        size--;
    }

    /**
     * Returns the number of blocks in the chain.
     *
     * @return the size of the chain
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the hash of the last block in the chain.
     *
     * @return the hash of the last block
     */
    public Hash getHash() {
        return last.block.getHash();
    }

    /**
     * Checks whether this blockchain is valid by verifying:
     * <ul>
     *   <li>Each block's hash is valid (its first three bytes are zero).</li>
     *   <li>Each block's prevHash matches the previous block's hash.</li>
     *   <li>Anna's balance never goes negative at any point.</li>
     * </ul>
     *
     * @return true if the chain is valid; false otherwise
     */
    public boolean isValidBlockChain() {
        int annaBalance = 0;

        Node current = first;
        Node prev = null;

        while (current != null) {
            Block b = current.block;

            // 1) The block's hash must be valid
            if (!b.getHash().isValid()) {
                return false;
            }

            // 2) The block's prevHash must match the previous block's hash
            if (prev == null) {
                if (b.getPrevHash() != null) {
                    return false;
                }
            } else {
                if (!b.getPrevHash().equals(prev.block.getHash())) {
                    return false;
                }
            }

            // 3) Check Anna's balance after the transaction
            annaBalance += b.getAmount();
            if (annaBalance < 0) {
                return false;
            }

            prev = current;
            current = current.next;
        }
        return true;
    }

    /**
     * Prints the current balances of Alice (Anna) and Bob. Anna's balance is the
     * sum of all block amounts in the chain, while Bob's balance is the negative
     * of that sum (assuming they both started at 0).
     */
    public void printBalances() {
        int annaBalance = 0;
        Node current = first;
        while (current != null) {
            annaBalance += current.block.getAmount();
            current = current.next;
        }
        int bobBalance = -annaBalance;
        System.out.println("Alice: " + annaBalance + ", Bob: " + bobBalance);
    }

    /**
     * Returns a string representation of the entire blockchain, from the
     * genesis block to the last block, each block on its own line.
     *
     * @return a string describing the blockchain
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node current = first;
        while (current != null) {
            sb.append(current.block.toString()).append("\n");
            current = current.next;
        }
        return sb.toString();
    }
}