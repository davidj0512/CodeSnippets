/**
 * �򵥵����ܲ���С��ܣ��ɹ��ο�
 * From: Java 8 in Action, P144
 * ����������˳��������ͳ���ӣ��Ͳ�������ǰһǧ�����Ȼ����͵�ʱ�䣬����Ǵ�ͳ������죬������������ԭ��
 * 1�� Stream.iterate���ɵ���װ��Ķ��󣬱����������ֲ������
 * 2�� ���Ѱ�iterate�ֳɶ��������������ִ��
 */
import java.util.function.Function;
import java.util.stream.Stream;

public class PerformanceTestingCase {

	public static long measureSumPer(Function<Long, Long> adder, long n) {
		long fastest = Long.MAX_VALUE;
		for(int i=0; i<10; i++) {
			long start = System.nanoTime();
			long sum = adder.apply(n);
			long duration = (System.nanoTime() - start) / 1_000_000;
			System.out.println("Result: " + sum);
			if(duration < fastest)
				fastest = duration;
		}
		return fastest;
	}
	
	public static long sequentialSum(long n) {
		return Stream.iterate(1L, i -> i+1)
					 .limit(n)
					 .reduce(0L, Long::sum);
	}
	
	public static long iterativeSum(long n) {
		long result = 0;
		for (long i=1L; i<=n; i++) {
			result += i;
		}
		return result;
	}
	
	public static long parallelSum(long n) {
		return Stream.iterate(1L, i -> i+1)
					 .limit(n)
					 .parallel()
					 .reduce(0L, Long::sum);
	}
	
	public static void main(String[] args) {

		System.out.println("Sequential sum done in: " + 
				measureSumPer(PerformanceTestingCase::sequentialSum, 10_000_000));
		
		System.out.println("Interative sum done in: " + 
				measureSumPer(PerformanceTestingCase::iterativeSum, 10_000_000));
		
		System.out.println("ParallelStream sum done in: " + 
				measureSumPer(PerformanceTestingCase::parallelSum, 10_000_000));
	}

}
