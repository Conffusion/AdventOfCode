package aoc2023.day20;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractModule implements Module {
	protected String id;
	List<Module> outputs=new ArrayList<>();
	
	protected void setId(String id) {
		this.id=id;
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void registerOutput(Module module) {
		outputs.add(module);
	}
	
	@Override
	public List<Module> getOutputs() {
		return outputs;
	}
	@Override
	public String toString() {
		return getId();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractModule other = (AbstractModule) obj;
		return Objects.equals(id, other.id);
	}
}
