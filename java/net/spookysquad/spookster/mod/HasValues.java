package net.spookysquad.spookster.mod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.spookysquad.spookster.mod.values.Value;

public interface HasValues {

	List<Value> getValues();

	Object getValue(String n);

	void setValue(String n, Object v);
}
