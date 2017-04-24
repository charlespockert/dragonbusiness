package io.github.charlespockert.business;

import java.util.concurrent.TimeUnit;

import org.spongepowered.api.scheduler.Task;

import com.google.inject.Inject;

import io.github.charlespockert.DragonBusinessPlugin;
import io.github.charlespockert.PluginLifecycle;
import io.github.charlespockert.business.tasks.CloseFiscalPeriod;
import io.github.charlespockert.business.tasks.PaySalary;
import io.github.charlespockert.config.MainConfig;

public class FiscalPeriodTaskRunner implements PluginLifecycle {

	private Task.Builder taskBuilder = Task.builder();

	private MainConfig mainConfig;

	private DragonBusinessPlugin plugin;

	private CloseFiscalPeriod closeFiscalPeriod;

	private PaySalary paySalary;

	private Task paySalaryTask;

	private Task closeFiscalPeriodTask;

	@Inject
	public FiscalPeriodTaskRunner(DragonBusinessPlugin plugin, MainConfig mainConfig,
			CloseFiscalPeriod closeFiscalPeriod, PaySalary paySalary) {
		this.mainConfig = mainConfig;
		this.plugin = plugin;
		this.closeFiscalPeriod = closeFiscalPeriod;
		this.paySalary = paySalary;
	}

	private void createTasks() {
		paySalaryTask = taskBuilder.delay(mainConfig.business.salary_interval_minutes, TimeUnit.MINUTES)
				.interval(mainConfig.business.salary_interval_minutes, TimeUnit.MINUTES)
				.name("DragonBusiness - Salary Payments").execute(paySalary).submit(plugin);

		closeFiscalPeriodTask = taskBuilder.delay(mainConfig.business.fiscal_period_length_minutes, TimeUnit.MINUTES)
				.interval(mainConfig.business.fiscal_period_length_minutes, TimeUnit.MINUTES)
				.name("DragonBusiness - Close Fiscal Period").execute(closeFiscalPeriod).submit(plugin);
	}

	private void cancelTasks() {
		paySalaryTask.cancel();
		closeFiscalPeriodTask.cancel();
	}

	@Override
	public void start() throws Exception {
		createTasks();
	}

	@Override
	public void shutdown() throws Exception {
		cancelTasks();
	}

	@Override
	public void freeze() {
		cancelTasks();
	}

	@Override
	public void unfreeze() {
		createTasks();
	}
}
