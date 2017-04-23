package io.github.charlespockert.entities;

import io.github.charlespockert.data.dto.CompanySummaryDto;

public class CompanySummary {

	private String name;

	private String owner;

	private int employeeCount;

	private float value;

	public String getName() {
		return name;
	}

	public String getOwner() {
		return owner;
	}

	public int getEmployeeCount() {
		return employeeCount;
	}

	public float getValue() {
		return value;
	}

	public static CompanySummary readFromDto(CompanySummaryDto dto) {
		CompanySummary summary = new CompanySummary();
		summary.name = dto.name;
		summary.owner = dto.owner;
		summary.employeeCount = dto.employeeCount;
		summary.value = dto.value;
		return summary;
	}
}
