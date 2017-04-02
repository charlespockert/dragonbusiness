package io.github.charlespockert.entities;

import io.github.charlespockert.data.common.DataWriter;
import io.github.charlespockert.data.dto.CompanyDto;

public class Company implements DataWriter<CompanyDto>  {

	private int id;
	
	private String name;

	private boolean bankrupt;
	
	private int sharesIssued;
	
	private float value;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isBankrupt() {
		return bankrupt;
	}

	public void setBankrupt(boolean bankrupt) {
		this.bankrupt = bankrupt;
	}

	public int getSharesIssued() {
		return sharesIssued;
	}

	public void setSharesIssued(int sharesIssued) {
		this.sharesIssued = sharesIssued;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	@Override
	public CompanyDto writeToDto() {
		CompanyDto dto = new CompanyDto();
		dto.id = this.id;
		dto.name = this.name;
		dto.bankrupt = this.bankrupt;
		dto.sharesIssued = this.sharesIssued;
		dto.value = this.value;
		
		return dto;
	}

	public static Company readFromDto(CompanyDto dto) {
		Company company = new Company();
		company.id = dto.id;
		company.name = dto.name;
		company.bankrupt = dto.bankrupt;
		company.sharesIssued = dto.sharesIssued;
		company.value = dto.value;
		return company;
	}
}
