package com.hbase;

import java.io.IOException;
import java.io.Serializable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.util.Bytes; 
 

/**
 * HTable connector for Storm {@link Bolt}
 * <p>
 * The HBase configuration is picked up from the first <tt>hbase-site.xml</tt> encountered in the
 * classpath
 */
@SuppressWarnings("serial")
public class HTableConnector implements Serializable { 

  private Configuration conf;
  protected HTable table;
  private String tableName;

  /**
   * Initialize HTable connection
   * @param conf The {@link TupleTableConfig}
   * @throws IOException
   */
  public HTableConnector(final TupleTableConfig tconf, String _quorum, String _port) throws IOException {
    this.tableName = tconf.getTableName();
    this.conf = HBaseConfiguration.create();
    
    if(_quorum != null && _port != null)
    {
    	this.conf.set("hbase.zookeeper.quorum", _quorum);
    	this.conf.set("hbase.zookeeper.property.clientPort", _port);
    }

	this.conf.set("hbase.rootdir", "/hbase");
   // LOG.info(String.format("Initializing connection to HBase table %s at %s", tableName,this.conf.get("hbase.rootdir")));
    System.out.println(String.format("Initializing connection to HBase table %s at %s,%s %s", tableName,this.conf.get("hbase.rootdir"),_quorum,_port));
    try {
    	System.out.println(this.conf.toString());
      this.table = new HTable(this.conf, this.tableName);
    } catch (IOException ex) {

    	System.out.println(ex.toString());
      throw new IOException("Unable to establish connection to HBase table " + this.tableName, ex);
    }
    System.out.println("get table");
    if (tconf.isBatch()) {
      // Enable client-side write buffer
      this.table.setAutoFlush(false, true);
   //   LOG.info("Enabled client-side write buffer");
      System.out.println("Enabled client-side write buffer");
    }

    System.out.println("isBatch");
    // If set, override write buffer size
    if (tconf.getWriteBufferSize() > 0) {
      try {
        this.table.setWriteBufferSize(tconf.getWriteBufferSize());

      //  LOG.info("Setting client-side write buffer to " + tconf.getWriteBufferSize());

        System.out.println("Setting client-side write buffer to " + tconf.getWriteBufferSize());
      } catch (IOException ex) {
     //   LOG.error("Unable to set client-side write buffer size for HBase table " + this.tableName,ex);
      }
    }

    System.out.println("getColumnFamilies");
    // Check the configured column families exist
    for (String cf : tconf.getColumnFamilies()) {
 
//      if (!columnFamilyExists(cf)) {
//    	  System.out.println(String.format(
//          "HBase table '%s' does not have column family '%s'", tconf.getTableName(), cf));
//      }
    }

    System.out.println("finished");
  }

  /**
   * Checks to see if table contains the given column family
   * @param columnFamily The column family name
   * @return boolean
   * @throws IOException
   */
  private boolean columnFamilyExists(final String columnFamily) throws IOException {
    return this.table.getTableDescriptor().hasFamily(Bytes.toBytes(columnFamily));
  }

  /**
   * @return the table
   */
  public HTable getTable() {
    return table;
  }

  /**
   * Close the table
   */
  public void close() {
    try {
      this.table.close();
    } catch (IOException ex) {
     // LOG.error("Unable to close connection to HBase table " + tableName, ex);
    }
  }
}