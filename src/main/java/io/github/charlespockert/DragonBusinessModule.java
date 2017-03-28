package io.github.charlespockert;

import com.google.inject.Binder;
import com.google.inject.Module;

import io.github.charlespockert.data.*;
import io.github.charlespockert.data.h2.*;

public class DragonBusinessModule implements Module {

	@Override
	public void configure(Binder binder) {		
		
		// Connections/db
		binder.bind(ConnectionManager.class).to(ConnectionManagerH2.class);
		binder.bind(DatabaseManager.class).to(DatabaseManagerH2.class);

		// Dao
		binder.bind(EmployeeDao.class).to(EmployeeH2Dao.class);
		binder.bind(CompanyDao.class).to(CompanyH2Dao.class);
	}

}