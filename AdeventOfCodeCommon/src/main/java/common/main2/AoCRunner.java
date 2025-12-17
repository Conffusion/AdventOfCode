package common.main2;

import common.Executor;

public class AoCRunner<T> {
	private AoCContext context;
	private AoCSolver<T> solver;
	private AoCLogger logger;
	
	public AoCRunner(AoCSolver<T> solver) {
		context=new AoCContext(solver);
		logger=new AoCLogger();
		this.solver=solver;
		solver.setContext(context);
	}
	public AoCRunner<T> nolog() {
		logger.nolog(true);
		return this;
	}
	public AoCRunner<T> testMode(){
		context.setTestMode(true);
		return this;
	}
	public AoCRunner<T> withFileOutput() {
		context.enableWriteToFile();
		return this;
	}
	
	public void doStar1() {
		try {
			timer(1, solver::star1);
		} finally {
			context.terminateOutputFileWriter();
		}
	}
	/**
	 * Om star1 en star2 uit te voeren
	 * @param testMode
	 */
	public AoCRunner<T> start() {
		if(context.isTestMode()) {
			logger.logln("--- TEST ---");
	        //Print the jvm heap size.
			logger.logln("Heap Size = " + Runtime.getRuntime().totalMemory());

			solver.beforeAllTest();
		}
		solver.beforeAll();
		context.setOnStar(1);
		solver.beforeEach();
		doStar1();
		logger.logln("--------------------------");
		context.setOnStar(2);
		solver.beforeEach();
		doStar2();
		return this;
	}

	/**
	 * Om enkel star1 uit te voeren
	 */
	public AoCRunner<T> start1() {
		context.setOnStar(1);
		if(context.isTestMode()) {
			logger.logln("--- TEST ---");
			solver.beforeAllTest();
		}
		solver.beforeAll();
		solver.beforeEach();
		doStar1();
		return this;
	}
	/**
	 * Om enkel star1 uit te voeren
	 */
	public AoCRunner<T> start2() {
		context.setOnStar(2);
		if(context.isTestMode()) {
			logger.logln("--- TEST ---");
			solver.beforeAllTest();
		}
		solver.beforeAll();
		solver.beforeEach();
		doStar2();
		return this;
	}

	/**
	 * Voert star2 loopcount keer uit. Opgelet beforeAll en beforeEach worden slechts 1 keer aangeroepen
	 * @param loopcount
	 * @return
	 */
	public AoCRunner<T> start2(int loopcount) {
		context.setOnStar(2);
		if(context.isTestMode()) {
			logger.logln("--- TEST ---");
			solver.beforeAllTest();
		}
		solver.beforeAll();
		solver.beforeEach();
		long timeSum=0L;
		for(int i=1;i<=loopcount;i++)
			timeSum+=doStar2();
		System.out.println("\n**** Average time after "+loopcount+" times Star 2: "+(timeSum/loopcount)/1000000.0 + "ms");
		return this;
	}
	
	public long doStar2() {
		try {
			return timer(2, solver::star2);
		} finally {
			context.terminateOutputFileWriter();
		}
	}
	
	public long timer(int star,Executor<T> run) {
		long start = System.nanoTime();
		try {
			System.out.println("\n*** Star "+context.getOnStar()+": "+run.run());
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.nanoTime();
		System.out.println("Time:" + (end - start)/1000000.0 + "ms");
		return end-start;
	}

}
