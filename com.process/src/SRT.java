import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class SRT {
	private int mProcessCount; // 进程数
	private List<Process> mReadyProcesses;  //存放就绪进程的链表（数组便于查看比较元素并交换顺序）
	private Queue<Process> mUnreachQueue; // 存放到达时间未到的进程
	private int mTimeSlice; // 时间片(SRT默认为1)

	private Queue<Process> mExecutedQueue; // 执行完毕的进程队列
	// 计算平均值用
	private double mTotalServedTime = 0.0;// 总周转时间
	private double mTotalRateTime = 0.0;// 总响应比

	public SRT(int processCount, Queue<Process> processQueue, int timeSlice) {
		//RR调用使得remainServiceTime的值改变
		for(Process g:processQueue) {
			g.setRemainServiceTime(g.getServeTime());
		}
		
		this.mProcessCount = processCount;
		this.mUnreachQueue = processQueue;//
		this.mReadyProcesses=new ArrayList<Process>();
		this.mTimeSlice = timeSlice;
		mExecutedQueue = new LinkedList<>();
	}

	public void setSRT() {
		Process currProcess = mUnreachQueue.poll();// 【取出】就绪进程池第一个进程
		// 第一个进程执行
		int currTime = executeProcess(currProcess, 0);

		while (mExecutedQueue.size()<mProcessCount||!mReadyProcesses.isEmpty() || !mUnreachQueue.isEmpty()) {// 就绪进程或主进程池非空
			// 把所有“到达时间”达到的进程加入就绪队列
			while (!mUnreachQueue.isEmpty()) {// 主进程池非空
				if (mUnreachQueue.peek().getArriveTime() <= currTime) {// 将主进程池中的进程在【currTime】前到达的进程按【到达时间】先后加入就绪进程池
					mReadyProcesses.add(mUnreachQueue.poll());
				} else {// 主进程池第一个进程未在【currTime】前进程到达，可知后续进程也未到达，便可break
					break;
				}
			}
			//
			if (currProcess.getRemainServiceTime()>0)
				mReadyProcesses.add(currProcess);
			// 就绪队列不为空时
			if (!mReadyProcesses.isEmpty()) {
				
				//判断就绪队列中剩余时间最短
				if(mReadyProcesses.size()==1) {//只有一个就绪进程
					currProcess=mReadyProcesses.get(0);
				}
				//对就绪队列按【remainServiceTime】排序,取出剩余时间最短的元素
				else {
					int flag=0;
					int remainServiceTimeFlag=mReadyProcesses.get(0).getRemainServiceTime();
					for(int i=1;i<mReadyProcesses.size();i++) {
						if(mReadyProcesses.get(i).getRemainServiceTime()<remainServiceTimeFlag) {
							flag=i;
							remainServiceTimeFlag=mReadyProcesses.get(i).getRemainServiceTime();
						}
					}
					if(flag!=0) {
						Process temp=new Process();
						temp=mReadyProcesses.get(0);
						mReadyProcesses.set(0, mReadyProcesses.get(flag));
						mReadyProcesses.set(flag, temp);				
					}
					currProcess=mReadyProcesses.get(0);
				}
				currTime = executeProcess(currProcess, currTime);
				mReadyProcesses.remove(0);
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
