import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;



public class SPN {
	private int mProcessCount; // 进程数
	private List<Process> mReadyProcesses;  //存放就绪进程的列表
	private Queue<Process> mUnreachQueue; // 存放到达时间未到的进程(初始主进程队列)

	private Queue<Process> mExecutedQueue; // 执行完毕的进程队列
	// 计算平均值用
	private double mTotalServedTime = 0.0;// 总周转时间
	private double mTotalRateTime = 0.0;// 总响应比

	public SPN(int processCount, Queue<Process> processQueue) {
		this.mProcessCount = processCount;
		this.mUnreachQueue = processQueue;//
		this.mReadyProcesses=new ArrayList<Process>();
		this.mExecutedQueue = new LinkedList<>();
	}

	public void setSPN() {
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
			
			// 就绪队列不为空时
			if (!mReadyProcesses.isEmpty()) {
				if(mReadyProcesses.size()==1) {//只有一个就绪进程
					currProcess=mReadyProcesses.get(0);
					mReadyProcesses.remove(0);//就绪进程进程个数归零
				}
				//对就绪队列按【serveTime】排序
				else {
					int flag=0;
					int serveTimeFlag=mReadyProcesses.get(0).getServeTime();
					for(int i=1;i<mReadyProcesses.size();i++) {
						if(mReadyProcesses.get(i).getServeTime()<serveTimeFlag) {
							flag=i;
							serveTimeFlag=mReadyProcesses.get(i).getServeTime();
						}
					}
					currProcess=mReadyProcesses.get(flag);
					mReadyProcesses.remove(flag);			
				}
				currTime = executeProcess(currProcess, currTime);
			} else {
				// 当前没有进程执行，但还有进程为到达，时间直接跳转到到达时间
				currTime = mUnreachQueue.peek().getArriveTime();// peek返回队列第一个元素，非取出
			}
		}
	}

	private int executeProcess(Process currProcess, int currTime) {
		currProcess.setFinishTime(currTime+currProcess.getServeTime());
		currProcess.setServedTime(currProcess.getFinishTime()-currProcess.getArriveTime());
		currProcess.setRate(currProcess.getServedTime() / (double)currProcess.getServeTime());
		mTotalServedTime += currProcess.getServedTime();
		mTotalRateTime += currProcess.getRate();
		mExecutedQueue.add(currProcess);
		
		currTime+=currProcess.getServeTime();
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
