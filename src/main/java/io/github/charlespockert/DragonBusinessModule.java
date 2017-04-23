package io.github.charlespockert;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.sql.SqlService;

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
		binder.bind(ConnectionManager.class).to(ConnectionManagerH2.class);
		binder.bind(DatabaseManager.class).to(DatabaseManagerH2.class);

		// Dao
		binder.bind(BusinessDao.class).to(BusinessH2Dao.class);

		binder.bind(SqlService.class).toInstance(Sponge.getServiceManager().provide(SqlService.class).get());
	}

}
