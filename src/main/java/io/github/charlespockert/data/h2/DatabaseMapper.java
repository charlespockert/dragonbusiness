package io.github.charlespockert.data.h2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.charlespockert.data.dto.*;
import io.github.charlespockert.data.h2.mappers.*;

public class DatabaseMapper {

	@SuppressWarnings({ "rawtypes" })
	private HashMap<Class, DtoMapper> mappers = new HashMap<Class, DtoMapper>();

	public DatabaseMapper() {
		// Add mappers
		mappers.put(CompanyDto.class, new CompanyDtoMapper());
		mappers.put(CompanySummaryDto.class, new CompanySummaryDtoMapper());
		mappers.put(EmployeeDto.class, new EmployeeDtoMapper());
		mappers.put(ShareDto.class, new ShareDtoMapper());
		mappers.put(TransactionDto.class, new TransactionDtoMapper());
		mappers.put(PeriodDto.class, new PeriodDtoMapper());
		mappers.put(CompanyPerformanceDto.class, new CompanyPerformanceDtoMapper());
	}

	public <T> T populateDto(ResultSet resultSet, Class<T> clazz) throws SQLException {
		DtoMapper<T> mapper = getMapperForClass(clazz);
		return mapper.map(resultSet);
	}

	public <T> T populateSingle(PreparedStatement statement, Class<T> clazz) throws SQLException {
		statement.execute();

		ResultSet resultSet = statement.getResultSet();

		if (resultSet == null)
			return null;

		if (!resultSet.next())
			return null;

		return populateDto(resultSet, clazz);
	}

	public <T> List<T> populateMany(PreparedStatement statement, Class<T> clazz) throws SQLException {
		ArrayList<T> dtos = new ArrayList<T>();
		statement.execute();
		ResultSet resultSet = statement.getResultSet();

		if (resultSet != null) {
			while (resultSet.next()) {
				dtos.add(populateDto(resultSet, clazz));
			}
		}

		return dtos;
	}

	@SuppressWarnings("unchecked")
	private <T> DtoMapper<T> getMapperForClass(Class<T> clazz) {
		return mappers.get(clazz);
	}
}
