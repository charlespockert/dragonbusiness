package io.github.charlespockert.data.dto;

public enum ApplicationStatus {
	Requested(0), Accepted(1), Rejected(2);

	private final int value;

	private ApplicationStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}
