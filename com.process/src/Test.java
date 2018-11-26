
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class Test {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("1.自动生成进程    2.手动输入  3.查看例子结果");
		int input = scanner.nextInt();
		// 自动生成进程
		if (input == 1) {
			System.out.print("请输入进程数：");
			int processNum = scanner.nextInt();
			Process[] processes = new Process[processNum];
			for (int i = 0; i < processNum; i++) {
				// 自动生成进程信息
				processes[i] = new Process();
			}

			System.out.println("随机生成的进程池：");
			Process.print(processes);

			// 将进程按到达时间排序
			Process[] sortedProcess = Process.sortProcesses(processes);

			System.out.println("按到达时间排序后的进程：");
			Process.print(sortedProcess);

			Queue<Process> fifoQueue = new LinkedBlockingQueue<>();
			Queue<Process> rrQueue = new LinkedBlockingQueue<>();
			Queue<Process> spnQueue = new LinkedBlockingQueue<>();
			Queue<Process> srtQueue = new LinkedBlockingQueue<>();
			Queue<Process> hrrnQueue = new LinkedBlockingQueue<>();
			for (Process g : sortedProcess) {
				fifoQueue.add(g);
				rrQueue.add(g);
				spnQueue.add(g);
				srtQueue.add(g);
				hrrnQueue.add(g);
			}

			System.out.println("以下算法均按进程完成时间先后顺序排列输出");
			// FIFO
			FIFO fifo = new FIFO(fifoQueue.size(), fifoQueue);
			fifo.setFIFO();
			System.out.println("FIFO：");
			fifo.print();
			// RR
			RR rr = new RR(rrQueue.size(), rrQueue, 1);
			rr.setRR();
			System.out.println("RR:");
			rr.print();

			// SPN
			SPN spn = new SPN(spnQueue.size(), spnQueue);
			spn.setSPN();
			System.out.println("SPN:");
			spn.print();

			// SRT
			SRT srt = new SRT(srtQueue.size(), srtQueue, 1);
			srt.setSRT();
			System.out.println("SRT:");
			srt.print();

			// HRRN
			HRRN hrrn = new HRRN(hrrnQueue.size(), hrrnQueue);
			hrrn.setHRRN();
			System.out.println("HRRN:");
			hrrn.print();
		} else if (input == 2) {
			System.out.print("请输入进程数：");
			int processNum = scanner.nextInt();
			System.out.println("请输入" + processNum + "个进程，如：\nA 0 3\nB 2 6\nC 4 4\nD 6 5\nE 8 2\n");
			String[] strings = new String[20];
			Process[] processes = new Process[20];
			scanner.nextLine();
			for (int i = 0; i < processNum; i++) {
				strings[i] = scanner.nextLine();
				String[] ss = strings[i].split(" ");
				processes[i] = new Process(ss[0].charAt(0), Integer.parseInt(ss[1]), Integer.parseInt(ss[2]));
			}
			
			Queue<Process> fifoQueue = new LinkedBlockingQueue<>();
			Queue<Process> rrQueue = new LinkedBlockingQueue<>();
			Queue<Process> spnQueue = new LinkedBlockingQueue<>();
			Queue<Process> srtQueue = new LinkedBlockingQueue<>();
			Queue<Process> hrrnQueue = new LinkedBlockingQueue<>();
			
			
			for (int i=0;i<processNum;i++) {
				fifoQueue.add(processes[i]);
				rrQueue.add(processes[i]);
				spnQueue.add(processes[i]);
				srtQueue.add(processes[i]);
				hrrnQueue.add(processes[i]);
			}

			FIFO fifo = new FIFO(fifoQueue.size(), fifoQueue);
			fifo.setFIFO();
			System.out.println("FIFO：");
			fifo.print();

			RR rr2 = new RR(rrQueue.size(), rrQueue, 1);
			rr2.setRR();
			System.out.println("RR:");
			rr2.print();

			SPN spn = new SPN(spnQueue.size(), spnQueue);
			spn.setSPN();
			System.out.println("SPN:");
			spn.print();

			SRT srt = new SRT(srtQueue.size(), srtQueue, 1);
			srt.setSRT();
			System.out.println("SRT:");
			srt.print();

			HRRN hrrn = new HRRN(hrrnQueue.size(), hrrnQueue);
			hrrn.setHRRN();
			System.out.println("HRRN:");
			hrrn.print();

		} else if (input == 3) {
			// 测试数据
			Process p1 = new Process('A', 0, 3);
			Process p2 = new Process('B', 2, 6);
			Process p3 = new Process('C', 4, 4);
			Process p4 = new Process('D', 6, 5);
			Process p5 = new Process('E', 8, 2);
			Queue<Process> queue = new LinkedBlockingQueue<>();
			queue.add(p1);
			queue.add(p2);
			queue.add(p3);
			queue.add(p4);
			queue.add(p5);
			Queue<Process> fifoQueue = new LinkedBlockingQueue<>();
			Queue<Process> rrQueue = new LinkedBlockingQueue<>();
			Queue<Process> spnQueue = new LinkedBlockingQueue<>();
			Queue<Process> srtQueue = new LinkedBlockingQueue<>();
			Queue<Process> hrrnQueue = new LinkedBlockingQueue<>();
			for (Process g : queue) {
				fifoQueue.add(g);
				rrQueue.add(g);
				spnQueue.add(g);
				srtQueue.add(g);
				hrrnQueue.add(g);
			}

			FIFO fifo = new FIFO(fifoQueue.size(), fifoQueue);
			fifo.setFIFO();
			System.out.println("FIFO：");
			fifo.print();

			RR rr2 = new RR(rrQueue.size(), rrQueue, 1);
			rr2.setRR();
			System.out.println("RR:");
			rr2.print();

			SPN spn = new SPN(spnQueue.size(), spnQueue);
			spn.setSPN();
			System.out.println("SPN:");
			spn.print();

			SRT srt = new SRT(srtQueue.size(), srtQueue, 1);
			srt.setSRT();
			System.out.println("SRT:");
			srt.print();

			HRRN hrrn = new HRRN(hrrnQueue.size(), hrrnQueue);
			hrrn.setHRRN();
			System.out.println("HRRN:");
			hrrn.print();
		} else {
			System.err.println("输入错误！！");
			System.exit(-1);
		}
	}
}