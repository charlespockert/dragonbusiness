package io.github.charlespockert.data.common;

public interface DataReader<T> {
	public abstract void readFromDto(T dto);
}
