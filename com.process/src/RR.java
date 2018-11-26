

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class RR {
	private int mProcessCount; // 进程数
	private Queue<Process> mReadyQueue; // 就绪队列，存放待运行的进程
	private Queue<Process> mUnreachQueue; // 存放到达时间未到的进程
	private int mTimeSlice; // 时间片

	private Queue<Process> mExecutedQueue; // 执行完毕的进程队列
	// 计算平均值用
	private double mTotalServedTime = 0.0;// 总周转时间
	private double mTotalRateTime = 0.0;// 总响应比

	public RR(int processCount, Queue<Process> processQueue, int timeSlice) {
		//SRT调用使得remainServiceTime的值改变
		for(Process g:processQueue) {
			g.setRemainServiceTime(g.getServeTime());
		}
		
		this.mProcessCount = processCount;
		this.mUnreachQueue = processQueue;//
		mReadyQueue = new LinkedBlockingQueue<>();// 阻塞添加:当阻塞队列元素已满时，队列会阻塞加入元素的线程，直队列元素不满时才重新唤醒线程执行元素加入操作。
		this.mTimeSlice = timeSlice;
		mExecutedQueue = new LinkedList<>();
	}

	public void setRR() {
		mReadyQueue.add(mUnreachQueue.poll());// 将主进程池里的第一个进程【取出并存入】就绪进程
		Process currProcess = mReadyQueue.poll();// 【取出】就绪进程池第一个进程
		// 第一个进程执行
		int currTime = executeProcess(currProcess, 0);

		while (mExecutedQueue.size()<mProcessCount||!mReadyQueue.isEmpty() || !mUnreachQueue.isEmpty()) {// 就绪进程或主进程池非空
			// 把所有“到达时间”达到的进程加入就绪队列
			while (!mUnreachQueue.isEmpty()) {// 主进程池非空
				if (mUnreachQueue.peek().getArriveTime() <= currTime) {// 将主进程池中的进程在【currTime】前到达的进程按【到达时间】先后加入就绪进程池
					mReadyQueue.add(mUnreachQueue.poll());
				} else {// 主进程池第一个进程未在【currTime】前进程到达，可知后续进程也未到达，便可break
					break;
				}
			}
			// 轮转法关键：未在一个时间片内完成的进程，加入就绪进程队列末尾
			if (currProcess.getRemainServiceTime() > 0)
				mReadyQueue.add(currProcess);
			// 就绪队列不为空时
			if (!mReadyQueue.isEmpty()) {
				currProcess = mReadyQueue.poll();
				currTime = executeProcess(currProcess, currTime);
			} else {
				// 当前没有进程执行，但还有进程为到达，时间直接跳转到到达时间
				currTime = mUnreachQueue.peek().getArriveTime();// peek返回队列第一个元素，非取出
			}
		}
	}

	private int executeProcess(Process currProcess, int currTime) {
		if (currProcess.getRemainServiceTime() - mTimeSlice <= 0) {
			// 当前进程在这个时间片内能执行完毕
			currProcess.setFinishTime(currTime+mTimeSlice);
			currProcess.setRemainServiceTime(0);
			// 对周转时间等进行计算
			// servedTime
			currProcess.setServedTime(currProcess.getFinishTime() - currProcess.getArriveTime());
			// 响应比
			currProcess.setRate(currProcess.getServedTime() / (double)currProcess.getServeTime());
			mTotalServedTime += currProcess.getServedTime();
			mTotalRateTime += currProcess.getRate();
			mExecutedQueue.add(currProcess);
		} else {
			// 不能执行完毕
			currProcess.setRemainServiceTime(currProcess.getRemainServiceTime() - mTimeSlice);
		}
		return currTime+mTimeSlice;
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
