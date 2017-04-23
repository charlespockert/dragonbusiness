package io.github.charlespockert.entities;

public enum TransactionType {
	Salary(0),
	Bonus(1),
	Dividend(2),
	EmployeeIncome(3);
	
	private final int value;
    private TransactionType(int value) {
        this.value = value;
    }
    
    public int getValue() {
    	return this.value;
    }
}
