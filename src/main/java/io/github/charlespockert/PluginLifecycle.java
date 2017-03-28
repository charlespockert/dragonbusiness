package io.github.charlespockert;

public interface PluginLifecycle {
	public void start() throws Exception;

	public void shutdown() throws Exception;

	public void freeze();

	public void unfreeze();
}
