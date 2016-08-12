/**
 *  GMM Algorithm     2014/9/25 
 * @author nie
 *
 */
class GMM {
	private int mNdim;
	private int mNcompo;
	private double[] mPriors;
	private double[][] mMeans; //每一行存放均值向量
	private double[][] mVars;  ///每一行存放对角协方差
	
	private double[] mMinVars;
	private int mMaxIter;
	private double mEndError;
	
	public GMM(int Ndim, int Ncompo)
	{
		mNdim = Ndim;
		mNcompo = Ncompo;
		mMaxIter = 1000;	//最大迭代次数
		mEndError = 0.0001;//最小误差
		
		mPriors = new double[mNcompo];
		mMeans = new double[mNcompo][mNdim];
		mVars = new double[mNcompo][mNdim];
		mMinVars = new double[mNdim];
		//初值初始化
		for(int k = 0; k < mNcompo; k++)
		{
			mPriors[k] = 1.0 / mNcompo;
			for(int d = 0; d < mNdim; d++)
			{
				mMeans[k][d] = 0.0;
				mVars[k][d] = 1.0;
			}
		}
	}
	
	public void train(double[][] data, int size)
	{
		init(data, size);
		Boolean loop = true;
		int iterNum = 0;
		double lastL = 0.0;
		double currL = 0.0;
		int unchanged = 0;
		double[] x = null;
		double[] nextPriors = new double[mNcompo];
		double[][] nextMeans = new double[mNcompo][mNdim];
		double[][] nextVars = new double[mNcompo][mNdim];
		
		while(loop)
		{
			//先验、均值、协方差置0
			for(int k = 0; k < mNcompo; k++)
				nextPriors[k] = 0.0;
			for(int k = 0; k < mNcompo; k++)
			{
				for(int d = 0; d < mNdim; d++){
					nextMeans[k][d] = 0.0;
					nextVars[k][d] = 0.0;
				}
			}
			
			lastL = currL;
			currL = 0.0;
			
			//E-step
			for(int i = 0; i < size; i++)
			{
				x = data[i];
				double p = getProbability(x);
				for (int k = 0; k < mNcompo; k++)
				{
					//已知各个compotent的先验、均值、协方差，求gammaIk
					double gamma_ik =gaussian(x, k) * mPriors[k] / p; //第i个数据由第k的compotent生成的概率

					nextPriors[k] += gamma_ik;   //每个component的软计数

					for (int d = 0; d < mNdim; d++)
					{
						nextMeans[k][d] += gamma_ik * x[d];
						nextVars[k][d] += gamma_ik * x[d] * x[d];
					}
				}

				currL += (p > 1E-20) ? Math.log(p) : -20; //对数似然
			}
			currL /= size;
			
			//M-step
			// Re-estimation: generate new priors, means and variances.
			for (int k = 0; k < mNcompo; k++)
			{
				mPriors[k] = nextPriors[k] / size; //最大化先验概率

				if (mPriors[k] > 0)
				{
					for (int d = 0; d < mNdim; d++)
					{
						mMeans[k][d] = nextMeans[k][d] / nextPriors[k];
						mVars[k][d] = nextVars[k][d] / nextPriors[k] - mMeans[k][d] * mMeans[k][d];
						if (mVars[k][d] < mMinVars[d])
						{
							mVars[k][d] = mMinVars[d]; //每个component的协方差不能小于全局协方差*0.01
						}
					}
				}
			}
			// Terminal conditions
			iterNum++;
			if (Math.abs(currL - lastL) < mEndError * Math.abs(lastL))
			{
				unchanged++;
			}
			if (iterNum >= mMaxIter || unchanged >= 3)
			{
				loop = false;
			}
		}
	}
	//-----------------------end--------------------------
	//===================初始化各个component的先验、均值、协方差====================
	public void init(double[][] data, int size)
	{
		final double MIN_VAR = 1e-10;
		
		KMeans kmeans = new KMeans(mNdim, mNcompo);
		int[] labels = new int[size];
		kmeans.cluster(data, labels, size);
		//输出聚类结果
		System.out.println("K-Means cluster result:");
		for(int i = 0; i < size; i ++){
			for(int d = 0; d < mNdim; d++){
				System.out.print(data[i][d]+" ");
		    }
			System.out.println("belong to cluster "+ labels[i] );
		}
			
		int[] counts = new int[mNcompo];
		double[] overMeans = new double[mNdim];
		for(int d = 0; d < mNdim; d++)
		{
			overMeans[d] = 0.0;
			mMinVars[d] = 0.0;
		}
		for(int k = 0; k < mNcompo; k++)
		{
			counts[k] = 0;
			mPriors[k] = 0;
			for(int d = 0; d < mNdim; d++)
			{
				mMeans[k][d] = kmeans.getMean(k)[d];
				mVars[k][d] = 0;
			}
		}
		double[] x = null;
		int label = -1;
		
		for (int i = 0; i < size; i++)
		{
			x = data[i];
			label = labels[i];
			counts[label]++;
			double[] m = kmeans.getMean(label);
			for (int d = 0; d < mNdim; d++)
			{
				mVars[label][d] += (x[d] - m[d]) * (x[d] - m[d]);
			}
			for (int d = 0; d < mNdim; d++)
			{
				overMeans[d] += x[d];
				mMinVars[d] += x[d] * x[d];
			}
		}
		//训练数据的总协方差(* 0.01)作为最小协方差
		double tempVar;
		for (int d = 0; d < mNdim; d++)
		{
			overMeans[d] /= size;
			tempVar = 0.01 * (mMinVars[d] / size - overMeans[d] * overMeans[d]);//�󷽲ʽ
			mMinVars[d] = tempVar > MIN_VAR ? tempVar : MIN_VAR;
			//m_minVars[d] = max(MIN_VAR, 0.01 * (m_minVars[d] / size - overMeans[d] * overMeans[d]));
		}
		//初始化每个component
		for(int k = 0; k < mNcompo; k++)
		{
			mPriors[k] = 1.0 * counts[k] / size;
			if(mPriors[k] > 0)
			{
				for (int d = 0; d < mNdim; d++){
					mVars[k][d] = mVars[k][d] / counts[k]; //ÿ��component�ķ���
					// A minimum variance for each dimension is required
					//限定每维上的最小方差
					if (mVars[k][d] < mMinVars[d])
					{
						mVars[k][d] = mMinVars[d];
					}
				}
			}
			else
			{
				for (int d = 0; d < mNdim; d++)
					mVars[k][d] = mMinVars[d];
				System.out.println("[WARNING] Gaussian " + k+" of GMM is not used!");
			}
		}
	}
	//------------------------------------end----------------------------------------
	//==============================计算每个样本的概率=================================
	public double getProbability(double[] x)
	{
		double p = 0.0;
		for (int k = 0; k < mNcompo; k++)
		{
			p += mPriors[k] * gaussian(x, k);
		}
		return p;
	}
	public double gaussian(double[] x, int k)
	{
		double p = 1;
		for (int d = 0; d < mNdim; d++)
		{
			p *= 1 / Math.sqrt(2 * 3.14159 * mVars[k][d]);
			p *= Math.exp(-0.5 * (x[d] - mMeans[k][d]) * (x[d] - mMeans[k][d]) / mVars[k][d]);
		}
		return p;
	}
	//----------------------------------end-------------------------------------
	//-------------------------------
	public String getPartialProb(double[] x, int k)
	{
		double gamma_ik =gaussian(x, k) * mPriors[k] / getProbability(x);
		return OutputFormat.formatOut(gamma_ik);
	}
}
