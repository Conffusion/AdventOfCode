package common;

public interface IMainMaster<T> {
	
	public IMainMaster<T> start1();
	public IMainMaster<T> start2();
	public IMainMaster<T> testMode(boolean flag);
	public IMainMaster<T> nolog(boolean flag);
}
