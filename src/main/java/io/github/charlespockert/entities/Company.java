package io.github.charlespockert.entities;

import io.github.charlespockert.data.CompanyDto;
import io.github.charlespockert.data.common.DataReader;
import io.github.charlespockert.data.common.DataWriter;

public class Company implements DataReader<CompanyDto>, DataWriter<CompanyDto>  {

	private int id;

	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	private String name;
	
	public String getName() {
		return this.name;
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	private int numberOfEmployees;
	
	public int getNumberOfEmployees() {
		return numberOfEmployees;
	}
	
	@Override
	public CompanyDto writeToDto() {
		CompanyDto dto = new CompanyDto();
		dto.id = this.id;
		dto.name = this.name;

		return dto;
	}

	@Override
	public void readFromDto(CompanyDto dto) {
		this.id = dto.id;
		this.name = dto.name;
	}
}
