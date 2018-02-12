/**
 * 简单的性能测试小框架，可供参考
 * From: Java 8 in Action, P144
 * 本例测试了顺序流，传统叠加，和并行流对前一千万个自然数求和的时间，结果是传统叠加最快，并行流最慢，原因：
 * 1， Stream.iterate生成的是装箱的对象，必须拆箱程数字才能求和
 * 2， 很难把iterate分成多个独立块来并行执行
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
