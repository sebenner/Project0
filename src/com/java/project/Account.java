package com.java.project;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Account implements Serializable{
	private static final long serialVersionUID = 1L;
	private int accountId;
	private String type; //  change to enum
	private double amount;
	private String status;
	
	public Account(int accountId, String type, double amount) {
		this.accountId = accountId;
		this.type = type;
		this.amount = amount;
		this.status = "Pending Review";
	}
	
	public String view() {
		return this.toString();
	}
	
	public void withdraw(float withdrawal) throws AccountException {
		double newAmount = amount - withdrawal;
		if (withdrawal < 0) {
			throw new AccountException("You cannot withdraw a negative amount of money.");
		}
		else if (newAmount >= 0.0) {
			amount = newAmount;
			DatabaseAccessImpl dai = new DatabaseAccessImpl();
		}
		else {
			throw new AccountException("You cannot withdraw more money than there is in the account.");
		}
	}
	
	public void deposit(float newDeposit) throws AccountException {
		double newAmount = amount + newDeposit;
		if (newDeposit < 0) {
			throw new AccountException("You cannot deposit a negative amount of money.");
		}
		else {
			amount = newAmount;
		}
	}
	
	public void transfer (Account toAccount, float tranAmount) throws AccountException {
		if (tranAmount < 0) {
			throw new AccountException("You cannot transfer a negative amount of money.");
		}
		else {
			withdraw(tranAmount);
			toAccount.deposit(tranAmount);
		}
	}
	
	
	@Override
	public int hashCode() {
		return accountId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Account) {
			Account e = (Account) obj;
			return this.accountId == e.getAccountId();
		}
		else {
			System.out.println("Test");
			return false;
		}
	}
}
