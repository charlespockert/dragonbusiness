package io.github.charlespockert.entities;

public enum EmployeeRank {
	Employee(0),
	Shareholder(1), // Automatically given if owning shares
	Officer(2),
	CEO(3);
	
	private final int value;
    private EmployeeRank(int value) {
        this.value = value;
    }
    
    public int getValue() {
    	return this.value;
    }
}
