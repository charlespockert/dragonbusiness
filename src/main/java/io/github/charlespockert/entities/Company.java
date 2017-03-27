package io.github.charlespockert.entities;

import org.spongepowered.api.entity.living.player.Player;

import io.github.charlespockert.data.CompanyDto;
import io.github.charlespockert.data.common.DataReader;
import io.github.charlespockert.data.common.DataWriter;

public class Company implements DataReader<CompanyDto>, DataWriter<CompanyDto>  {

	private int id;
	private String name;
	
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
	
	public void printInfo(Player player) {
		
	}

}
