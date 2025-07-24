package module.evoldist.msa2distview;

import module.evoldist.operator.AbstructCalculateGDistsPipe;
import egps2.modulei.RunningTask;

public class PLMSA2DistMatrix extends AbstructCalculateGDistsPipe implements RunningTask{

	protected int processIndex = 1;
	private int currentProgress = 0;
	protected double[][] distMatrix;
	
	
	public PLMSA2DistMatrix() {
	}

	@Override
	public int processNext() throws Exception {
		
//		Thread.sleep(10);
		switch (processIndex) {
		case 1:
			assignParameters();
			processIndex++;
			break;
		case 2:
			if (!calculateDistMatrix()) {
				processIndex++;
			}
			break;
		default:
			return PROGRESS_FINSHED;
		}
		

		currentProgress ++;
		return currentProgress;
	}

	/**
	 * 
	 * @return if we still need to invoke!
	 * @throws Exception
	 */
	protected boolean calculateDistMatrix() throws Exception {
		if (ifBootStrap) {
			boolean runOnceBSIteration = bootstrap.runOnceBSIteration();
			//System.out.println("Once\t"+runOnceBSIteration);
			if (!runOnceBSIteration) {
				bootstrap.closeBWriter();
				distMatrix = bootstrap.getFinalDistance();
			}
			return runOnceBSIteration;
		}else {
			distMatrix = distanceCalculator.getFinalDistance();
			return false;
		}
	}

	protected void assignParameters() throws Exception {
		incorporateParametersAndPackageCalculatorInstance();
	}
	
	
	
	@Override
	public void actionsAfterFinished() throws Exception {
		// 这里的最后处理都放在 VOICM中了
	}

	@Override
	public int getTotalSteps() {
		if (ifBootStrap) {
			return bootStrapTimes + 2;
		}
		return 2;
	}


	@Override
	public boolean isTimeCanEstimate() {
		return true;
	}
}
