package com.github.undo4j.bank;

import com.github.undo4j.*;

import java.util.Arrays;

/**
 * AccountOperations
 * 
 * @author afs
 * @version 2013
 */

public final class AccountOperations {

    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class AccountOperations.
     */
    private AccountOperations() { throw new AssertionError(); }



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    public static WriteOperation deposit
            (ResourceReference<Account> account, int amount) {
        return new DepositOperation(account, amount);
    }

    /** */
    public static WriteOperation withdraw
            (ResourceReference<Account> account, int amount) {
        return new WithdrawOperation(account, amount);
    }

    /** */
    public static WriteOperation transfer(
            ResourceReference<Account> source,
            ResourceReference<Account> target,
            int amount) {
        return new TransferOperation(
            new WithdrawOperation(source, amount),
            new DepositOperation(target, amount));
    }



    /*************************************************************************\
     *  Nested Classes
    \*************************************************************************/

    /** */
    private static class DepositOperation implements WriteOperation {
        final Account account;
        final ResourceId id;
        final int amount;
        private boolean done = false;

        DepositOperation(ResourceReference<Account> account, int amount) {
            assert amount > 0;
            this.account    = account.get();
            this.id         = account.id();
            this.amount     = amount;
        }

        @Override
        public void write() {
            account.deposit(amount);
            done = true;
        }

        @Override
        public void undo() {
            if (done) { account.withdraw(amount); }
        }

        @Override
        public Iterable<ResourceId> resources() {
            return Arrays.asList(id);
        }
    }


    /** */
    private static class WithdrawOperation implements WriteOperation {
        final Account account;
        final ResourceId id;
        final int amount;
        private boolean done = false;

        WithdrawOperation(ResourceReference<Account> account, int amount) {
            assert amount > 0;
            this.account    = account.get();
            this.id         = account.id();
            this.amount     = amount;
        }

        @Override
        public void write() {
            account.withdraw(amount);
            done = true;
        }

        @Override
        public void undo() {
            if (done) { account.deposit(amount); }
        }

        @Override
        public Iterable<ResourceId> resources() {
            return Arrays.asList(id);
        }
    }


    /** */
    private static class TransferOperation implements WriteOperation {
        final WithdrawOperation withdraw;
        final DepositOperation deposit;

        TransferOperation
                (WithdrawOperation withdraw, DepositOperation deposit) {
            this.withdraw   = withdraw;
            this.deposit    = deposit;
        }

        @Override
        public void write() {
            withdraw.write();
            deposit.write();
        }

        @Override
        public void undo() {
            deposit.undo();
            withdraw.undo();
        }

        @Override
        public Iterable<ResourceId> resources() {
            return Arrays.asList(withdraw.id, deposit.id);
        }
    }
}
