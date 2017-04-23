package io.github.charlespockert.data.h2.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DtoMapper<T> {
	public abstract T map(ResultSet resultSet) throws SQLException;
}
