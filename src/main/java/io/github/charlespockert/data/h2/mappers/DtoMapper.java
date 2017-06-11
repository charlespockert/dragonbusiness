package io.github.charlespockert.data.h2.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DtoMapper<T> {
	public T map(ResultSet resultSet) throws SQLException;
}
