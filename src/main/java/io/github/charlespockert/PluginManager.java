package io.github.charlespockert;

import java.util.Optional;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;

import com.google.inject.Injector;

import io.github.charlespockert.commands.CommandBuilder;
import io.github.charlespockert.data.DatabaseManager;
import io.github.charlespockert.data.EmployeeRepository;
import io.github.charlespockert.entities.Employee;
import io.github.charlespockert.entities.EmployeeRank;
import io.github.charlespockert.events.EventManager;

public class PluginManager {
	
	private Injector injector;
	private Logger logger;

	private DatabaseManager databaseManager;
	private EmployeeRepository employeeRepository;
	private CommandBuilder commandBuilder;
	private EventManager eventManager;

	public PluginManager(Injector injector) {
		this.injector = injector;
	}
	
	public void startPlugin() throws Exception {		
		
		logger = injector.getInstance(Logger.class);

		// Check that an economy service is present
		Optional<EconomyService> service = Sponge.getServiceManager().provide(EconomyService.class);
		if(!service.isPresent()) {
			logger.error("Dragon Business could not start - there was no economy plugin installed. Dragon Business requires an economy plugin.");
			return;
		}
		
		databaseManager = injector.getInstance(DatabaseManager.class);
		employeeRepository = injector.getInstance(EmployeeRepository.class);
		commandBuilder = injector.getInstance(CommandBuilder.class);
		eventManager = injector.getInstance(EventManager.class);
		
		// Create commands
		commandBuilder.buildCommands();

		// Register all the events
		eventManager.registerEvents();
		
		logger.info("Checking/creating database");

		if(!databaseManager.databaseExists()) {
			databaseManager.createDatabase();
		}

		try {		
			logger.info("Looping employees");
			for(Employee emp : employeeRepository.getAllEmployees()) {
				logger.warn("Employee: " + emp.getName());
			}
		} catch(Exception e) {
			logger.error("Error getting employees: " + e.getMessage());
		}
	}
	
	public void stopPlugin() {
	
	}
	
	public void freezePlugin() {
		
	}
	
	public void unfreezePlugin() {
		
	}
}
