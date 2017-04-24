package io.github.charlespockert;

import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.sql.SqlService;

import com.google.inject.Binder;
import com.google.inject.Module;
import io.github.charlespockert.data.*;
import io.github.charlespockert.data.dao.DaoContainer;
import io.github.charlespockert.data.h2.*;
import io.github.charlespockert.data.h2.dao.DaoH2Container;

public class DragonBusinessModule implements Module {

	@Override
	public void configure(Binder binder) {

		// Connections/db
		binder.bind(ConnectionManager.class).to(ConnectionManagerH2.class);
		binder.bind(DatabaseManager.class).to(DatabaseManagerH2.class);

		// Dao
		binder.bind(DaoContainer.class).to(DaoH2Container.class);

		binder.bind(SqlService.class).toInstance(Sponge.getServiceManager().provide(SqlService.class).get());
		binder.bind(EconomyService.class).toInstance(Sponge.getServiceManager().provide(EconomyService.class).get());
		binder.bind(Server.class).toInstance(Sponge.getServer());
	}

}
