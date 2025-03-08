package edu.grinnell.csc207.blockchain;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * The main driver for the block chain program.
 */
public class BlockChainDriver {
    /**
     * The main entry point for the program.
     * 
     * @param args the command-line arguments
     */
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

        // Create the blockchain with the given initial amount
        BlockChain bc = new BlockChain(initialAmount);

        // For reading user commands
        Scanner sc = new Scanner(System.in);

        System.out.println("BlockChain created with initial amount: " + initialAmount);
        System.out.println("Valid commands: mine, append, remove, check, report, help, quit");

        // Interactive loop
        boolean done = false;
        while (!done) {
            // Print the entire chain each time
            System.out.println(bc.toString());

            System.out.print("Command? ");
            String command = sc.nextLine().trim().toLowerCase();

            switch (command) {
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
                    break;
            }
        }

        System.out.println("Exiting BlockChainDriver.");
        sc.close();
    }
    /**
     * Prints the list of valid commands, matching the example format.
     */
    private static void printHelp() {
        System.out.println("Valid commands:");
        System.out.println("  mine    - discovers the nonce for a given transaction amount");
        System.out.println("  append  - appends a block by directly providing amount & nonce");
        System.out.println("  remove  - removes the last block from the chain");
        System.out.println("  check   - checks that the blockchain is valid");
        System.out.println("  report  - reports the balances of Alice and Bob");
        System.out.println("  help    - prints this list of commands");
        System.out.println("  quit    - quits the program");
    }
    /**
     * Prompts the user for an amount and mines a new block with that amount.
     */
    private static void doMine(BlockChain bc, Scanner sc) {
        try {
            System.out.print("Amount transferred? ");
            int amount = Integer.parseInt(sc.nextLine());
            Block mined = bc.mine(amount);
            System.out.println("Mined new block:");
            System.out.println(mined);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Must be an integer.");
        }
    }
    /**
     * Prompts the user for an amount and nonce, then appends a block (no mining).
     */
    private static void doAppend(BlockChain bc, Scanner sc) {
        try {
            System.out.print("Amount transferred? ");
            int amount = Integer.parseInt(sc.nextLine());
            System.out.print("Nonce? ");
            long nonce = Long.parseLong(sc.nextLine());

            Block newBlock = new Block(bc.getSize(), amount, bc.getHash(), nonce);
            bc.append(newBlock);
            System.out.println("Appended new block:");
            System.out.println(newBlock);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Amount must be int, nonce must be long.");
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to append block: " + e.getMessage());
        }
    }

    /**
     * Removes the last block from the chain, if possible.
     */
    private static void doRemove(BlockChain bc) {
        try {
            bc.removeLast();
            System.out.println("Removed the last block.");
        } catch (NoSuchElementException e) {
            System.out.println("Cannot remove block: " + e.getMessage());
        }
    }

    /**
     * Checks the validity of the blockchain and reports the result.
     */
    private static void doCheck(BlockChain bc) {
        boolean valid = bc.isValidBlockChain();
        System.out.println("Blockchain is " + (valid ? "valid." : "NOT valid!"));
    }
}
