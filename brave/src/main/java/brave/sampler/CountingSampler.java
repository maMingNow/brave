package brave.sampler;

import java.util.BitSet;
import java.util.Random;

/**
 * This sampler is appropriate for low-traffic instrumentation (ex servers that each receive <100K
 * requests), or those who do not provision random trace ids. It not appropriate for collectors as
 * the sampling decision isn't idempotent (consistent based on trace id).
 *
 * <h3>Implementation</h3>
 *
 * <p>This initializes a random bitset of size 100 (corresponding to 1% granularity). This means
 * that it is accurate in units of 100 traces. At runtime, this loops through the bitset, returning
 * the value according to a counter.
 * 比例抽样,即按照一定比例抽样数据
 */
public final class CountingSampler extends Sampler {

  /**
   * @param rate 0 means never sample, 1 means always sample. Otherwise minimum sample rate is 0.01,
   * or 1% of traces
   */
  public static Sampler create(final float rate) {
    if (rate == 0) return NEVER_SAMPLE;
    if (rate == 1.0) return ALWAYS_SAMPLE;
    if (rate < 0.01f || rate > 1) {
      throw new IllegalArgumentException("rate should be between 0.01 and 1: was " + rate);
    }
    return new CountingSampler(rate);
  }

  private int i; // guarded by this 下一次的位置
  private final BitSet sampleDecisions;//1的位置说明是要参与抽样的位置

  /** Fills a bitset with decisions according to the supplied rate. */
  CountingSampler(float rate) {
    int outOf100 = (int) (rate * 100.0f);//将小数转换成100以内的数
    this.sampleDecisions = randomBitSet(100, outOf100, new Random());
  }

  /** loops over the pre-canned decisions, resetting to zero when it gets to the end. */
  @Override
  public synchronized boolean isSampled(long traceIdIgnored) {
    boolean result = sampleDecisions.get(i++);//该位置是否是true,是则说明要被抽样
    if (i == 100) i = 0;
    return result;
  }

  @Override
  public String toString() {
    return "CountingSampler()";
  }

  /**
   * Reservoir sampling algorithm borrowed from Stack Overflow.
   *
   * http://stackoverflow.com/questions/12817946/generate-a-random-bitset-with-n-1s
   */
  static BitSet randomBitSet(int size, int cardinality, Random rnd) {
    BitSet result = new BitSet(size);//一共100个空间
    int[] chosen = new int[cardinality];//要多少个空间作为true
    int i;
    for (i = 0; i < cardinality; ++i) {//先将前cardinality个元素占上true的位置
      chosen[i] = i;
      result.set(i);
    }
    for (; i < size; ++i) {//继续寻找接下来的空间
      int j = rnd.nextInt(i + 1);//随机选择一个位置
      if (j < cardinality) {
        result.clear(chosen[j]);//将该位置设置成false
        result.set(i);
        chosen[j] = i;
      }
    }
    return result;
  }
}
