package io.github.charlespockert.entities;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import io.github.charlespockert.data.common.DataWriter;
import io.github.charlespockert.data.dto.EmployeeDto;

public class Employee implements DataWriter<EmployeeDto> {

	private String name;
	private UUID uuid;
	private EmployeeRank rank;
	private int companyId;
	private Date employmentStart;
	private boolean isOwner;

	public Employee() {
		this.employmentStart = Date.from(Instant.now());
	}

	public int getCompanyId() { 
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EmployeeRank getRank() {
		return rank;
	}

	public void setRank(EmployeeRank rank) {
		this.rank = rank;
	}

	public boolean isOwner() {
		return isOwner;
	}

	public Date getEmploymentStart() {
		return employmentStart;
	}

	@Override
	public EmployeeDto writeToDto() {
		EmployeeDto dto = new EmployeeDto();

		dto.name = this.name;
		dto.uuid = this.uuid;
		dto.rank = this.rank.getValue();

		return dto;
	}

	public static Employee readFromDto(EmployeeDto dto) {
		if(dto == null) return null;
		
		Employee employee = new Employee();
		employee.uuid = dto.uuid;
		employee.employmentStart = dto.employmentStart;
		employee.companyId = dto.company_id;
		employee.name = dto.name;
		employee.rank = EmployeeRank.values()[dto.rank];
		return employee;
	}

}
