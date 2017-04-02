package io.github.charlespockert.data.h2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import io.github.charlespockert.data.UuidUtil;
import io.github.charlespockert.data.dto.*;

public class DtoUtil {
	
	// Employees
	public static EmployeeDto employeePopulateDto(ResultSet resultSet) throws SQLException {
		EmployeeDto dto = new EmployeeDto();
		
		dto.uuid = UuidUtil.asUuid(resultSet.getBytes("uuid"));
		dto.name = resultSet.getString("name");
		dto.rank = resultSet.getInt("rank");

		return dto;	
	}
	
	public static EmployeeDto employeePopulateSingle(PreparedStatement statement) throws SQLException {		
		statement.execute();

		ResultSet resultSet = statement.getResultSet();		

		if(resultSet == null) return null;
		
		if (!resultSet.next())
			return null;
		
		return employeePopulateDto(resultSet);
	}

	public static List<EmployeeDto> employeePopulateMany(PreparedStatement statement) throws SQLException {
		ArrayList<EmployeeDto> employeeDtos = new ArrayList<EmployeeDto>();
		statement.execute();
		ResultSet resultSet = statement.getResultSet();

		if(resultSet != null) {
			while(resultSet.next()) {
				employeeDtos.add(employeePopulateDto(resultSet));
			}
		}

		return employeeDtos;
	}
	
	// Companies
	public static CompanyDto companyPopulateDto(ResultSet resultSet) throws SQLException {
		CompanyDto dto = new CompanyDto();

		dto.id = resultSet.getInt("id");
		dto.name = resultSet.getString("name");
		dto.bankrupt = resultSet.getBoolean("bankrupt");
		dto.sharesIssued = resultSet.getInt("shares_issued");
		dto.value = resultSet.getFloat("value");
		
		return dto;	
	}

	public static CompanyDto companyPopulateSingle(PreparedStatement statement) throws SQLException {		
		statement.execute();

		ResultSet resultSet = statement.getResultSet();		

		if(resultSet == null) return null;
		
		if(!resultSet.next())
			return null;
		
		return companyPopulateDto(resultSet);
	}

	public static List<CompanyDto> companyPopulateMany(PreparedStatement statement) throws SQLException {
		ArrayList<CompanyDto> companyDtos = new ArrayList<CompanyDto>();
		statement.execute();
		ResultSet resultSet = statement.getResultSet();

		if(resultSet != null) {
			while(resultSet.next()) {
				companyDtos.add(companyPopulateDto(resultSet));
			}
		}

		return companyDtos;
	}
	
	public static CompanySummaryDto companySummaryPopulateDto(ResultSet resultSet) throws SQLException {
		CompanySummaryDto dto = new CompanySummaryDto();

		dto.name = resultSet.getString("name");
		dto.owner = resultSet.getString("owner");
		dto.employeeCount = resultSet.getInt("employeecount");
		dto.value = resultSet.getFloat("value");
		
		return dto;	
	}

	
	public static List<CompanySummaryDto> companySummaryPopulateMany(PreparedStatement statement) throws SQLException {
		ArrayList<CompanySummaryDto> summaryDtos = new ArrayList<CompanySummaryDto>();
		statement.execute();
		ResultSet resultSet = statement.getResultSet();

		if(resultSet != null) {
			while(resultSet.next()) {
				summaryDtos.add(companySummaryPopulateDto(resultSet));
			}
		}

		return summaryDtos;
	}
}
