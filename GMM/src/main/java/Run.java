/**
 * 执行程序                    2016/7/25
 * @author nie
 *
 */
public class Run {
	public static void main(String[] args) {
		double[][] trainset = {
				{0.0, 0.2, 0.4},
		        {0.3, 0.2, 0.4},
		        {0.4, 0.2, 0.4},
		        {0.5, 0.2, 0.4},
		        {5.0, 5.2, 8.4},
		        {6.0, 5.2, 7.4},
		        {4.0, 5.2, 4.4},
		        {10.3, 10.4, 10.5},
		        {10.1, 10.6, 10.7},
		        {11.3, 10.2, 10.9} };
		int size = trainset.length;
		int Ndim = trainset[0].length;   //训练数据维度
		int Ncluster = 3;          //簇的个数
		
		//testKMeans(trainset, size, Ndim, Ncluster);  //测试K均值ֵ
		testGMM(trainset, size, Ndim, Ncluster);	 //测试GMM
 	}
	
	public static void testKMeans(double[][] trainset, int size, int Ndim, int Ncluster)
	{
		System.out.println("***********************************************");
		KMeans kmeans = new KMeans(Ndim, Ncluster);
		int[] labels = new int[size];
		
		kmeans.cluster(trainset, labels, size);
		System.out.println("K-means cluster result:");
		for(int i = 0; i < size; i ++){
			for(int d = 0; d < Ndim; d++){
				System.out.print(trainset[i][d]+" ");
		    }
			System.out.println("belong to cluster "+ labels[i] );
		}
		System.out.println("***********************************************");
	}
	
	public static void testGMM(double[][] trainset, int size, int Ndim, int Ncompo)
	{
		System.out.println("***********************************************");
		System.out.println("Train GMM:");
		GMM gmm = new GMM(Ndim, 3); // GMM has 3 components
		gmm.train(trainset, size);
		
		double[][] testset = {
				{ 0.1, 0.2, 0.3 },
				{ 0.4, 0.5, 0.6 },
				{ 5.0, 6.2, 8.4 },
				{ 10.3, 10.4, 10.5 }};
		 System.out.println("Test GMM:");
		for(int i = 0; i < testset.length; i++)
		{
			System.out.print("The probability of ");
			for(int d = 0; d < Ndim; d++)
			{
				System.out.print(testset[i][d]+" ");
			}
			System.out.println("is: " + gmm.getProbability(testset[i]) );
			
			for(int k = 0; k < Ncompo; k++)
			{
				System.out.println("         由第"+ k + "个分模型生成的概率是： "+gmm.getPartialProb(testset[i], k));
			}	
		}
		System.out.println("***********************************************");
	}
}
