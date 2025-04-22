package edu.grinnell.csc207.blockchain;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Driver for a simple blockchain.  
 * Usage: java edu.grinnell.csc207.blockchain.BlockChainDriver <initialAmount>
 *
 * Commands:
 *   mine    - discovers the nonce for a given transaction
 *   append  - appends a new block to the end of the chain
 *   remove  - removes the last block from the chain
 *   check   - checks that the block chain is valid
 *   report  - reports the balances of Alice and Bob
 *   help    - prints this list of commands
 *   quit    - quits the program
 */
public class BlockChainDriver {
    /** Holds the last mined but not yet appended candidate block */
    private static Block pendingCandidate;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java BlockChainDriver <initialAmount>");
            return;
        }

        int initialAmount;
        try {
            initialAmount = Integer.parseInt(args[0]);
            if (initialAmount < 0) {
                System.err.println("Initial amount must be non-negative.");
                return;
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid initial amount. Must be an integer.");
            return;
        }

        BlockChain bc = new BlockChain(initialAmount);
        Scanner sc = new Scanner(System.in);
        boolean done = false;

        // Interactive loop
        while (!done) {
            // 1) Print the current chain
            System.out.println(bc.toString().trim());

            // 2) Prompt
            System.out.print("Command? ");
            String cmd = sc.nextLine().trim().toLowerCase();

            switch (cmd) {
                case "help":
                    printHelp();
                    break;

                case "mine":
                    doMine(bc, sc);
                    break;

                case "append":
                    doAppend(bc, sc);
                    break;

                case "remove":
                    doRemove(bc);
                    break;

                case "check":
                    doCheck(bc);
                    break;

                case "report":
                    bc.printBalances();
                    break;

                case "quit":
                    done = true;
                    break;

                default:
                    System.out.println("Unrecognized command. Type 'help' for options.");
            }
        }

        System.out.println("Exiting BlockChainDriver.");
        sc.close();
    }

    private static void printHelp() {
        System.out.println("Valid commands:");
        System.out.println("  mine: discovers the nonce for a given transaction");
        System.out.println("  append: appends a new block to the end of the chain");
        System.out.println("  remove: removes the last block from the chain");
        System.out.println("  check: checks that the block chain is valid");
        System.out.println("  report: reports the balances of Alice and Bob");
        System.out.println("  help: prints this list of commands");
        System.out.println("  quit: quits the program");
    }

    private static void doMine(BlockChain bc, Scanner sc) {
        try {
            System.out.print("Amount transferred? ");
            int amount = Integer.parseInt(sc.nextLine().trim());
            // Create but do NOT append: this is just mining the candidate
            pendingCandidate = new Block(bc.getSize(), amount, bc.getHash());
            System.out.println("amount = " 
                + pendingCandidate.getAmount() 
                + ", nonce = " 
                + pendingCandidate.getNonce());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Must be an integer.");
        }
    }

    private static void doAppend(BlockChain bc, Scanner sc) {
        try {
            System.out.print("Amount transferred? ");
            int amount = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Nonce? ");
            long nonce = Long.parseLong(sc.nextLine().trim());

            // Build the block with the user‐supplied nonce
            Block blk = new Block(bc.getSize(), amount, bc.getHash(), nonce);
            bc.append(blk);
            // Clear the pending candidate
            pendingCandidate = null;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Amount must be integer, nonce must be long.");
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to append block: " + e.getMessage());
        }
    }

    private static void doRemove(BlockChain bc) {
        try {
            bc.removeLast();
            System.out.println("Removed the last block.");
        } catch (NoSuchElementException e) {
            System.out.println("Cannot remove block: " + e.getMessage());
        }
    }

    private static void doCheck(BlockChain bc) {
        boolean valid = bc.isValidBlockChain();
        System.out.println("Blockchain is " + (valid ? "valid." : "NOT valid!"));
    }
}