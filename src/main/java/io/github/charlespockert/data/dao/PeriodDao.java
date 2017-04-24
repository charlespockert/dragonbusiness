package io.github.charlespockert.data.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import io.github.charlespockert.data.dto.PeriodDto;

public interface PeriodDao {
	public PeriodDto getById(int id) throws SQLException;

	public List<PeriodDto> getAll() throws SQLException;

	public PeriodDto getByTimestamp(Timestamp timestamp) throws SQLException;

	public PeriodDto getCurrent() throws SQLException;

	public int create() throws SQLException;
}
