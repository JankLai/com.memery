import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class FIFO {

	private int mProcessCount; // 进程数
	private Queue<Process> mUnreachQueue;// 存储主进程池
	private Queue<Process> mReadyProcesses; // 存放就绪进程的列表

	private Queue<Process> mExecutedQueue; // 执行完毕的进程队列
	// 计算平均值用
	private double mTotalServedTime = 0.0;// 总周转时间
	private double mTotalRateTime = 0.0;// 总响应比

	public FIFO(int processCount, Queue<Process> processQueue) {
		this.mProcessCount = processCount;
		this.mUnreachQueue = processQueue;
		this.mReadyProcesses = new LinkedBlockingQueue<>();
		this.mExecutedQueue = new LinkedList<>();
	}

	// 先进先出
	public void setFIFO() {
		Process currProcess = mUnreachQueue.poll();
		int currTime = executeProcess(currProcess, 0);
		while (mExecutedQueue.size() < mProcessCount || !mReadyProcesses.isEmpty() || !mUnreachQueue.isEmpty()) {// 就绪进程或主进程池非空
			// 把所有“到达时间”达到的进程加入就绪队列
			while (!mUnreachQueue.isEmpty()) {// 主进程池非空
				if (mUnreachQueue.peek().getArriveTime() <= currTime) {// 将主进程池中的进程在【currTime】前到达的进程按【到达时间】先后加入就绪进程池
					mReadyProcesses.add(mUnreachQueue.poll());
				} else {// 主进程池第一个进程未在【currTime】前进程到达，可知后续进程也未到达，便可break
					break;
				}
			}
			// 就绪队列不为空时
			if (!mReadyProcesses.isEmpty()) {
				currProcess=mReadyProcesses.poll();
				currTime = executeProcess(currProcess, currTime);
			} else {
				// 当前没有进程执行，但还有进程为到达，时间直接跳转到到达时间
				currTime = mUnreachQueue.peek().getArriveTime();// peek返回队列第一个元素，非取出
			}

		}
		
	}

	private int executeProcess(Process currProcess, int currTime) {
		currProcess.setFinishTime(currTime + currProcess.getServeTime());
		currProcess.setServedTime(currProcess.getFinishTime() - currProcess.getArriveTime());
		currProcess.setRate(currProcess.getServedTime() / (double) currProcess.getServeTime());
		mTotalServedTime += currProcess.getServedTime();
		mTotalRateTime += currProcess.getRate();
		mExecutedQueue.add(currProcess);

		currTime += currProcess.getServeTime();
		return currTime;
	}

	public void print() {
		 System.out.println("进程\t完成时间\t服务时间\t响应比");
			DecimalFormat df = new DecimalFormat("0.00");
	        Process process ;
	        while(!mExecutedQueue.isEmpty()) {
	            process = mExecutedQueue.poll();
	            System.out.println(process.getID() + "\t"+process.getFinishTime()+"\t"+process.getServedTime()+"\t"+df.format(process.getRate())+"\t");
	          
	        }
	        System.out.println("平均服务时间：" + df.format(mTotalServedTime / (double) mProcessCount));
	        System.out.println("平均响应比：" + df.format(mTotalRateTime / (double) mProcessCount)+"\n");
	}
}
