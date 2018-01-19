package model_classes;

import java.util.ArrayList;

public class TransactionObj {
    public ArrayList<Transaction> getTransactionArrayList() {
        return transactionArrayList;
    }

    public void setTransactionArrayList(ArrayList<Transaction> transactionArrayList) {
        this.transactionArrayList = transactionArrayList;
    }

    private ArrayList<Transaction> transactionArrayList;


 /*   public TransactionObj(ArrayList<Transaction> transactionArrayList)
    {
        this.transactionArrayList = transactionArrayList;
    }*/




}