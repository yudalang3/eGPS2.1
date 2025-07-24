package module.heatmap.eheatmap.model.transform;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;

/**
 * https://zhuanlan.zhihu.com/p/36284359
 * @author yudalang
 */
public class CoxBoxTransform implements VectorTransform {

	
	final KolmogorovSmirnovTest ks = new KolmogorovSmirnovTest();
	final double[] iteratesForLess0 = {1,2,3,4,5,6,7,8,9};
	final double[] iteratesForMorethan0 = {-2,-1.5,-1,-0.5,0,0.5,1,1.5,2};
	
	double[] transfromedArray;
	private double choosedLambda;
	
	public double getChoosedLambda() {
		return choosedLambda;
	}
	
	@Override
	public double[] transform(double[] in) {
		double maxP = 0.0;
		double[] returnedRet = null;
		
		if (containsValueLessZero(in)) {
			for (double lam : iteratesForLess0) {
				double tt = runOnceTranformNot0(lam,in);
				if (tt > maxP) {
					maxP = tt;
					choosedLambda = lam;
					returnedRet = transfromedArray;
				}
			}
		}else {
			for (double lam : iteratesForMorethan0) {
				double tt;
				if (lam == 0) {
					tt = runOnceTranform0(lam,in);
				}else {
					tt = runOnceTranformNot0(lam,in);
				}
				
				if (tt > maxP) {
					choosedLambda = lam;
					returnedRet = transfromedArray;
				}
			}
		}
		
		return returnedRet;
	}
	
	private double runOnceTranformNot0(double d, double[] in) {
		int length = in.length;
		transfromedArray = new double[length];
		for (int i = 0; i < length; i++) {
			transfromedArray[i] = (Math.pow(in[i], d) - 1) / d;
		}
		NormalDistribution ndist = new NormalDistribution(StatUtils.mean(transfromedArray),
				Math.sqrt(StatUtils.variance(transfromedArray)));
		double tt = ks.kolmogorovSmirnovTest(ndist,transfromedArray);
		//System.err.print(tt+" "+d+"||\t");
		return tt;
	}
	
	private double runOnceTranform0(double d, double[] in) {
		int length = in.length;
		transfromedArray = new double[length];
		for (int i = 0; i < length; i++) {
			transfromedArray[i] = Math.log(in[i]);
		}
		NormalDistribution ndist = new NormalDistribution(StatUtils.mean(transfromedArray),
				Math.sqrt(StatUtils.variance(transfromedArray)));
		
		return ks.kolmogorovSmirnovTest(ndist,transfromedArray);
	}

	private boolean containsValueLessZero(double[] in) {
		for (double d : in) {
			if (d <= 0) {
				return true;
			}
		}
		return false;
	}

//	public static void main(String[] args) {
//		double[] y = {0.169800331692481,-2.10475092213292,0.699343972412532,0.0469714345945611,1.34981035803497,-0.472874431827244,0.236876216891615,0.267876910921799,0.826428873684374,0.48170295995494,0.198956871528575,0.0243865500640921,-1.12940060704275,0.328842660515953,0.724812250066173,-2.31521248956101,-1.26852209091923,-0.460967099915849,-2.34585962025317,-0.276288682849663,0.134935443605237,1.19894514314908,0.210625097211875,-0.0884284936541139,-0.0472679676056079,1.90909666058802,-1.28290194682061,0.0731672602948719,0.657778939003331,-1.22661284535175,0.194600787269606,1.03671266392383,-0.309799016530572,0.121664488378244,-0.492730182163604,1.12993879134562,-0.569145655055558,-0.509315330210418,-0.255936735518009,-0.563994693092705,0.0142301607223359,0.98000111313707,1.70144802729401,-1.57541450908059,0.245070574035133,-0.929400136137479,0.441793543610746,-1.31755819132763,0.266873679196283,-1.38791441127576};
//		double[] x = {0.606965586377606,0.495767848396714,-1.87843238422199,0.227898656927376,-0.00788338219616115,-1.41144919441546,0.810312072752879,-1.50714164412604,-0.0545408239016916,-0.463690932171698,0.736799702554194,1.1957410377838,1.15896285701432,-1.05965982949558,-0.238653311241307,0.91541776964554,-0.159073104327873,0.00673063906731335,-0.38257635848393,0.656189388970406,0.867081882502409,-0.0750208714399462,1.98400585009997,-0.850524338077374,-1.7864222494143,1.49794452553075,0.232588712456834,0.761408284518876,0.156943857090994,-0.157890372163847,-0.715028138847481,0.39235834767359,0.76893107372562,-0.339057151527165,-0.866918424460426,-0.403337958270791,0.554789261033599,2.46102089451435,-0.591799041997133,-0.102471943791179,-0.179049494994607,1.49919783097032,-0.748444363198706,-0.312465981616151,-0.342349234343418,-0.653713161694363,0.237656144695974,-1.61592779906752,-0.238404201744559,-0.0613902694640809,0.59884873840295,-0.420034164096748,0.682772105602774,-0.198243368899117,-0.766103344576237,-0.0896957802963414,0.137349478937039,-0.171805900543987,-0.650662501810048,-1.64449459197129,1.31094765986806,-0.761051455358562,-0.836144044069701,0.118661988236435,-1.95386990984489,-0.89264256532564,-1.26947651669417,0.839430834451992,1.35583005200753,-0.630567192138271,-0.822511229311798,-0.296547715029097,0.291912115136746,-2.1409936349089,0.516297167966203,-0.265988658127482,0.00934336992826739,-0.551174928633442,-0.668937517122785,1.22244231579254,-0.452428200734696,-0.919471465138502,-1.34176728224702,-2.23052943828656,-1.25735008648627,0.476393814292622,-1.72468316433523,0.222026787691491,-0.484231396878971,0.57458939818733,-0.363207969707539,-0.0595520734073626,-0.882606045328221,0.0409280638389576,-1.01880168171644,1.41077241551021,0.678201214641864,-2.73290397177881,0.726699075215887,-0.457035249968736};
//		
//		
//		CoxBoxTransform coxBoxTransform = new CoxBoxTransform();
//	
//		KolmogorovSmirnovTest ks = new KolmogorovSmirnovTest();
//		NormalDistribution ndist = new NormalDistribution(StatUtils.mean(x),Math.sqrt( StatUtils.variance(x)));
//		
//		double[] yy = y.clone();
//		for (int i = 0; i < yy.length; i++) {
//			yy[i] = yy[i] - 1;
//		}
//		
//		double[] transform = coxBoxTransform.transform(y);
//		System.out.println();
//		System.out.println(coxBoxTransform.getChoosedLambda());
//		System.out.println(Arrays.toString(transform));
//	}

}
