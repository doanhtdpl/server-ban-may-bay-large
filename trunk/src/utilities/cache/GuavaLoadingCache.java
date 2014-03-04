/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities.cache;

import com.google.common.cache.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.ByteBuffer;
import javax.swing.Timer;

/**
 *
 * @author quocpt
 */
public class GuavaLoadingCache<E extends ICacheData>
{

    private long maximunWeight;
    private long currentWeight;
    private int cuncurrencyLevel;
    private LoadingCache<ByteBuffer, ICacheData> gauvaCache;

    public GuavaLoadingCache()
    {
    }

    private void buildCache(String spec, final Class<E> cacheDataType)
    {
	//=====
	CacheLoader<ByteBuffer, ICacheData> loader = new CacheLoader<ByteBuffer, ICacheData>()
	{
	    @Override
	    public ICacheData load(ByteBuffer k) throws Exception
	    {
		return cacheDataType.newInstance();
	    }
	};

	Weigher<ByteBuffer, ICacheData> weighBySize = new Weigher<ByteBuffer, ICacheData>()
	{
	    @Override
	    public int weigh(ByteBuffer k, ICacheData v)
	    {
		int size = v.getSize();
		currentWeight += size;
		return size;
	    }
	};

	RemovalListener<ByteBuffer, ICacheData> removeListener = new RemovalListener<ByteBuffer, ICacheData>()
	{
	    @Override
	    public void onRemoval(RemovalNotification<ByteBuffer, ICacheData> n)
	    {
		if (n.wasEvicted())
		{
		    ICacheData data = n.getValue();
		    if (data != null)
			currentWeight -= data.getSize();
		}
	    }
	};

	gauvaCache = CacheBuilder.from(spec)
		.recordStats()
		.removalListener(removeListener)
		.weigher(weighBySize)
		.build(loader);
    }

    public void initCache(long maxWeight, int cuncurrencyLvl, int expireAfterWriteSeconds, final Class<E> cacheDataType)
    {
	maximunWeight = maxWeight;
	cuncurrencyLevel = cuncurrencyLvl;

	StringBuilder strBuilder = new StringBuilder();
	strBuilder.append("concurrencyLevel=").append(cuncurrencyLevel).append(",");
	strBuilder.append("maximumWeight=").append(maximunWeight).append(",");
	strBuilder.append("expireAfterWrite=").append(expireAfterWriteSeconds).append("s");

	buildCache(strBuilder.toString(), cacheDataType);
    }

    public void initCache(long maxWeight, int cuncurrencyLvl, final Class<E> cacheDataType)
    {
	maximunWeight = maxWeight;
	cuncurrencyLevel = cuncurrencyLvl;

	StringBuilder strBuilder = new StringBuilder();
	strBuilder.append("concurrencyLevel=").append(cuncurrencyLvl).append(",");
	strBuilder.append("maximumWeight=").append(maxWeight);

	buildCache(strBuilder.toString(), cacheDataType);
    }

    public String dumpStats()
    {
	CacheStats stats = gauvaCache.stats();
	StringBuilder str = new StringBuilder();
	str.append("cacheUsage: ").append((int) (((float) currentWeight / (float) maximunWeight) * 100.0)).append("%</br>");
	str.append("maximunWeight: ").append(maximunWeight).append(" | ");
	str.append("currentWeight: ").append(currentWeight).append(" | ");
	str.append("cacheEntrySize: ").append(gauvaCache.size()).append("</br>");
	str.append("hitCount: ").append(stats.hitCount()).append(" | ");
	str.append("hitRate: ").append(String.format("%.2f", stats.hitRate() * 100.0f)).append("% | ");
	str.append("loadCount: ").append(stats.loadCount()).append(" | ");
	str.append("missCount: ").append(stats.missCount()).append(" | ");
	str.append("missRate: ").append(String.format("%.2f", stats.missRate() * 100.0f)).append("% | ");
	str.append("evictionCount: ").append(stats.evictionCount()).append(" | ");
	str.append("averageLoadPenalty: ").append(String.format("%.2f", stats.averageLoadPenalty())).append(" (nanoseconds) | ");
	str.append("loadExceptionCount: ").append(stats.loadExceptionCount()).append(" | ");
	str.append("loadExceptionRate: ").append(stats.loadExceptionRate()).append(" | ");
	str.append("loadSuccessCount: ").append(stats.loadSuccessCount()).append(" | ");
	str.append("requestCount: ").append(stats.requestCount()).append(" | ");
	str.append("totalLoadTime: ").append(stats.totalLoadTime());


	return str.toString();
    }

    public void set(byte[] key, E data)
    {
	if (data == null)
	    return;
       
	ByteBuffer buff = ByteBuffer.wrap(key);
	gauvaCache.put(buff, data);
    }

    @SuppressWarnings("unchecked")
    public E get(byte[] key)
    {
	ByteBuffer buff = ByteBuffer.wrap(key);
	ICacheData data = gauvaCache.getIfPresent(buff);
	if (data != null)
	    return (E) data;

	return null;
    }
    //=======================================================
    private static int count = 0;
    private static GuavaLoadingCache<CacheByteData> cache;

    public static void main(String[] args) throws InterruptedException
    {
	cache = new GuavaLoadingCache<CacheByteData>();
	cache.initCache(1000, 4, CacheByteData.class);

	Timer t = new Timer(5, new ActionListener()
	{
	    @Override
	    public void actionPerformed(ActionEvent ae)
	    {
		byte data[] =
		{
		    1
		};
		for (int i = 0; i < 5; i++)
		{
		    count++;
		    count %= 1500;
		    cache.set(("Key" + count).getBytes(), CacheByteData.wrap(data));
		}
	    }
	});

	t.start();

	t = new Timer(7, new ActionListener()
	{
	    @Override
	    public void actionPerformed(ActionEvent ae)
	    {
		for (int i = 0; i < 2; i++)
		{
		    cache.get(("Key" + (int) (Math.random() * 1500.0)).getBytes());
		}
	    }
	});

	t.start();

	t = new Timer(700, new ActionListener()
	{
	    @Override
	    public void actionPerformed(ActionEvent ae)
	    {
		for (int i = 0; i < 1500; i++)
		{
		    count++;
		    count %= 1500;
		    cache.set(("Key" + count).getBytes(), null);
		}
	    }
	});

	t.start();

	t = new Timer(1000, new ActionListener()
	{
	    @Override
	    public void actionPerformed(ActionEvent ae)
	    {
		System.out.println("=============================");
		System.out.println(cache.dumpStats());
	    }
	});

	t.start();
	Thread.sleep(600000);
    }
}
