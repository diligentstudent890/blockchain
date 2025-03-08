package edu.grinnell.csc207.blockchain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Tests {
    @Test
    @DisplayName("Placeholder Test")
    public void placeholderTest() {
        assertEquals(2, 1 + 1);
    }
    @Test
    @DisplayName("Test Genesis Block Creation")
    public void testGenesisBlock() {
        BlockChain bc = new BlockChain(300);
        assertEquals(1, bc.getSize(), "Blockchain should have only the genesis block.");
        assertTrue(bc.getHash().isValid(), "Genesis block hash should be valid.");
    }
    
    @Test
    @DisplayName("Test Mining a Block")
    public void testMiningBlock() {
        BlockChain bc = new BlockChain(300);
        var genesisHash = bc.getHash();
        Block mined = bc.mine(-100);
        assertEquals(2, bc.getSize(), "Blockchain should have two blocks after mining.");
        assertTrue(mined.getHash().isValid(), "Mined block hash should be valid.");
        assertEquals(genesisHash, mined.getPrevHash(), "Mined block's prevHash should match genesis block's hash.");
        assertTrue(bc.isValidBlockChain(), "Blockchain should be valid.");
    }
    
    @Test
    @DisplayName("Test Removing Last Block")
    public void testRemoveLastBlock() {
        BlockChain bc = new BlockChain(300);
        bc.mine(-50);
        assertEquals(2, bc.getSize(), "Blockchain should have 2 blocks.");
        bc.removeLast();
        assertEquals(1, bc.getSize(), "Blockchain should have 1 block after removal.");
        assertThrows(NoSuchElementException.class, () -> bc.removeLast(),
            "Removing the genesis block should throw a NoSuchElementException.");
    }
}
