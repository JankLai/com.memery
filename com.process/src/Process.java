
import java.util.Random;

public class Process {
	private char ID;// 进程名
	private int arriveTime;// 到达时间
	private int serveTime;// 服务时间
	private int finishTime;// 完成时间
	private int servedTime;// 服务时间
	private int remainServiceTime;//还需要服务的时间
	
	private double rate;
	private static int index = 0;
	private Random random = new Random();

	public char getID() {
		return ID;
	}

	public void setID(char iD) {
		ID = iD;
	}

	public int getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(int arriveTime) {
		this.arriveTime = arriveTime;
	}

	public int getServeTime() {
		return serveTime;
	}

	public void setServeTime(int serveTime) {
		this.serveTime = serveTime;
	}

	public static int getIndex() {
		return index;
	}

	public static void setIndex(int index) {
		Process.index = index;
	}

	public Random getRandom() {
		return random;
	}

	public void setRandom(Random random) {
		this.random = random;
	}

	public int getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(int finishTime) {
		this.finishTime = finishTime;
	}

	public int getServedTime() {
		return servedTime;
	}

	public void setServedTime(int servedTime) {
		this.servedTime = servedTime;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
	
	 public int getRemainServiceTime() {
	        return remainServiceTime;
	    }

	 public void setRemainServiceTime(int remainServiceTime) {
	        this.remainServiceTime = remainServiceTime;
	 }

	public Process() {
		this.setID((char) ('A' + index));
		if (index == 0)
			this.setArriveTime(0);
		else {
			this.setArriveTime(random.nextInt(10) + 1);
		}
		int time=random.nextInt(10) + 2;
		this.setServeTime(time);
		this.setRemainServiceTime(time);

		++index;
	}
	public Process(char ID,int arriveTime,int serveTime) {
		this.setID(ID);
		this.setArriveTime(arriveTime);
		this.setServeTime(serveTime);
		this.setRemainServiceTime(serveTime);
	}

	public static void print(Process[] processes) {
		System.out.println("进程    到达时间    服务时间");
		for (Process g : processes) {
			System.out.println(g.getID() + "\t" + g.getArriveTime() + "\t" + g.getServeTime());
		}
	}

	

	public static Process[] sortProcesses(Process[] processes) {
		int k = processes.length;
		boolean flag = true;
		while (flag) {
			flag = false;
			for (int j = 1; j < k; j++) {
				if (processes[j - 1].getArriveTime() > processes[j].getArriveTime()) {
					Process generator;
					generator = processes[j - 1];
					processes[j - 1] = processes[j];
					processes[j] = generator;
					flag = true;
				}
			}
			k--;
		}
		return processes;
	}


}
