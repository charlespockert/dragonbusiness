package io.github.charlespockert.entities;

import io.github.charlespockert.data.EmployeeDto;
import io.github.charlespockert.data.common.DataReader;
import io.github.charlespockert.data.common.DataWriter;

public class Employee implements DataReader<EmployeeDto>, DataWriter<EmployeeDto> {

	private String name;
	private int uuid;
	private EmployeeRank rank;
	
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
