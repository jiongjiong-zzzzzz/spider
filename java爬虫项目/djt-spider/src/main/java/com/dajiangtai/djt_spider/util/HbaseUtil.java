package com.dajiangtai.djt_spider.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.dajiangtai.djt_spider.entity.Page;

/**
 * HBase 工具类 
 * Created by dajiangtai on 2016-10-04
 */
public class HbaseUtil {

	/**
	 * HBASE 表名称
	 */
	public static final String TABLE_NAME = "tvcount";
	/**
	 * 列簇1
	 */
	public static final String COLUMNFAMILY_1 = "tvinfo";
	/**
	 * 列簇1中的列
	 */
	public static final String COLUMNFAMILY_1_TVNAME = "tvname";
	public static final String COLUMNFAMILY_1_URL = "url";
	public static final String COLUMNFAMILY_1_ALLNUMBER = "allnumber";
	public static final String COLUMNFAMILY_1_DAYNUMBER = "daynumber";
	public static final String COLUMNFAMILY_1_COMMENTNUMBER = "commentnumber";
	public static final String COLUMNFAMILY_1_COLLECTNUMBER = "collectnumber";
	public static final String COLUMNFAMILY_1_SUPPORTNUMBER = "supportnumber";
	public static final String COLUMNFAMILY_1_AGAINSTNUMBER = "againstnumber";
	/**
	 * 列簇2
	 */
	public static final String COLUMNFAMILY_2 = "episode";

	/**
	 * 列簇2中的列
	 */
	public static final String COLUMNFAMILY_2_EPISODENUMBER = "episodenumber";

	HBaseAdmin admin = null;
	Configuration conf = null;

	/**
	 * 构造函数加载配置
	 */
	public HbaseUtil() {
		conf = new Configuration();
		conf.set("hbase.zookeeper.quorum", "192.168.20.129:2181");
		conf.set("hbase.rootdir", "hdfs://192.168.20.129:9000/hbase");
		try {
			admin = new HBaseAdmin(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		HbaseUtil hbase = new HbaseUtil();
		// 创建一张表
		//hbase.createTable(TABLE_NAME, COLUMNFAMILY_1);
		// 查询所有表名
		hbase.getALLTable();
	}

	/**
	 * rowFilter的使用
	 * 
	 * @param tableName
	 * @param reg
	 * @throws Exception
	 */
	public void getRowFilter(String tableName, String reg) throws Exception {
		HTable hTable = new HTable(conf, tableName);
		Scan scan = new Scan();
		// Filter
		RowFilter rowFilter = new RowFilter(CompareOp.NOT_EQUAL,
				new RegexStringComparator(reg));
		scan.setFilter(rowFilter);
		ResultScanner scanner = hTable.getScanner(scan);
		for (Result result : scanner) {
			System.out.println(new String(result.getRow()));
		}
	}

	/**
	 * scan 扫描数据
	 * @param tableName
	 * @param family
	 * @param qualifier
	 * @throws Exception
	 */
	public void getScanData(String tableName, String family, String qualifier)
			throws Exception {
		HTable hTable = new HTable(conf, tableName);
		Scan scan = new Scan();
		scan.addColumn(family.getBytes(), qualifier.getBytes());
		ResultScanner scanner = hTable.getScanner(scan);
		for (Result result : scanner) {
			if (result.raw().length == 0) {
				System.out.println(tableName + " 表数据为空！");
			} else {
				for (KeyValue kv : result.raw()) {
					System.out.println(new String(kv.getKey()) + "\t"
							+ new String(kv.getValue()));
				}
			}
		}
	}

	/**
	 * 删除表
	 * @param tableName
	 */
	private void deleteTable(String tableName) {
		try {
			if (admin.tableExists(tableName)) {
				admin.disableTable(tableName);
				admin.deleteTable(tableName);
				System.out.println(tableName + "表删除成功！");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(tableName + "表删除失败！");
		}

	}

	/**
	 * 删除一条记录
	 * @param tableName
	 * @param rowKey
	 */
	public void deleteOneRecord(String tableName, String rowKey) {
		HTablePool hTablePool = new HTablePool(conf, 1000);
		HTableInterface table = hTablePool.getTable(tableName);
		Delete delete = new Delete(rowKey.getBytes());
		try {
			table.delete(delete);
			System.out.println(rowKey + "记录删除成功！");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(rowKey + "记录删除失败！");
		}
	}

	/**
	 * 获取表的所有数据
	 * 
	 * @param tableName
	 */
	public void getALLData(String tableName) {
		try {
			HTable hTable = new HTable(conf, tableName);
			Scan scan = new Scan();
			ResultScanner scanner = hTable.getScanner(scan);
			for (Result result : scanner) {
				if (result.raw().length == 0) {
					System.out.println(tableName + " 表数据为空！");
				} else {
					for (KeyValue kv : result.raw()) {
						System.out.println(new String(kv.getKey()) + "\t"
								+ new String(kv.getValue()));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 添加一条记录
	 * @param tableName
	 * @param row
	 * @param columnFamily
	 * @param column
	 * @param data
	 * @throws IOException
	 */
	public void put(String tableName, String row, String columnFamily,
			String column, String data) throws IOException {
		HTablePool hTablePool = new HTablePool(conf, 1000);
		HTableInterface table = hTablePool.getTable(tableName);
		Put p1 = new Put(Bytes.toBytes(row));
		p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column),
				Bytes.toBytes(data));
		table.put(p1);
		System.out.println("put'" + row + "'," + columnFamily + ":" + column
				+ "','" + data + "'");
	}

	/**
	 * 查询所有表名
	 * @return
	 * @throws Exception
	 */
	public List<String> getALLTable() throws Exception {
		ArrayList<String> tables = new ArrayList<String>();
		if (admin != null) {
			HTableDescriptor[] listTables = admin.listTables();
			if (listTables.length > 0) {
				for (HTableDescriptor tableDesc : listTables) {
					tables.add(tableDesc.getNameAsString());
					System.out.println(tableDesc.getNameAsString());
				}
			}
		}
		return tables;
	}

	/**
	 * 创建一张表
	 * 
	 * @param tableName
	 * @param column
	 * @throws Exception
	 */
	public void createTable(String tableName, String column) throws Exception {
		if (admin.tableExists(tableName)) {
			System.out.println(tableName + "表已经存在！");
		} else {
			HTableDescriptor tableDesc = new HTableDescriptor(tableName);
			tableDesc.addFamily(new HColumnDescriptor(column.getBytes()));
			admin.createTable(tableDesc);
			System.out.println(tableName + "表创建成功！");
		}
	}

	/**
	 * 获取电视剧详细信息
	 * @param tableName
	 * @param row
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "resource" })
	public Page get(String tableName, String row) {
		HTablePool hTablePool = new HTablePool(conf, 1000);
		HTableInterface table = hTablePool.getTable(tableName);
		Get get = new Get(row.getBytes());
		Page page = null;
		try {
			Result result = table.get(get);
			KeyValue[] raw = result.raw();
			if (raw.length == 8) {
				page = new Page();
				page.setTvId(row);
				page.setAgainstnumber(new String(raw[0].getValue()));
				page.setAllnumber(new String(raw[1].getValue()));
				page.setCollectnumber(new String(raw[2].getValue()));
				page.setCommentnumber(new String(raw[3].getValue()));
				page.setDaynumber(new String(raw[4].getValue()));
				page.setSupportnumber(new String(raw[5].getValue()));
				page.setTvname(new String(raw[6].getValue()));
				page.setUrl(new String(raw[7].getValue()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return page;
	}
	
}