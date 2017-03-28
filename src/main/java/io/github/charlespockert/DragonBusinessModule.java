package io.github.charlespockert;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;

import io.github.charlespockert.commands.CommandBuilder;
import io.github.charlespockert.data.*;
import io.github.charlespockert.data.h2.*;
import io.github.charlespockert.events.EventManager;

public class DragonBusinessModule implements Module {

	@Override
	public void configure(Binder binder) {		

		// Connections/db
		binder.bind(ConnectionManager.class).to(ConnectionManagerH2.class).asEagerSingleton();
		binder.bind(DatabaseManager.class).to(DatabaseManagerH2.class).asEagerSingleton();

		// Dao
		binder.bind(EmployeeDao.class).to(EmployeeH2Dao.class);
		binder.bind(CompanyDao.class).to(CompanyH2Dao.class);
	}

}
