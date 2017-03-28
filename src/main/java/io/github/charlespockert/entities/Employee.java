package io.github.charlespockert.entities;

import io.github.charlespockert.data.EmployeeDto;
import io.github.charlespockert.data.common.DataReader;
import io.github.charlespockert.data.common.DataWriter;

public class Employee implements DataReader<EmployeeDto>, DataWriter<EmployeeDto> {

	private String name;
	private String uuid;
	private EmployeeRank rank;
	private int companyId;
	
	public int getCompanyId() { 
		return companyId;
	}
	
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
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

	@Override
	public EmployeeDto writeToDto() {
		EmployeeDto dto = new EmployeeDto();
	
		dto.name = this.name;
		dto.uuid = this.uuid;
		dto.rank = this.rank.getValue();

		return dto;
	}

	@Override
	public void readFromDto(EmployeeDto dto) {
		this.name = dto.name;
		this.uuid = dto.uuid;
		this.rank = EmployeeRank.values()[dto.rank];
	}

}
