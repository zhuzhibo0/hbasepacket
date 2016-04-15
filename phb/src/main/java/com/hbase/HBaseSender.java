package com.hbase;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.pcap4j.packet.Packet;
 
public class HBaseSender {  
  protected HTableConnector connector;
  protected TupleTableConfig conf;
  protected boolean autoAck = true;
  
  private String _quorum;
  private String _port;

  public HBaseSender() {
	String tableName = "pcap";
    this.conf = new TupleTableConfig(tableName,"pcap_id","timestamp");

	String allColumnFamiliesColumnQualifiers = "t:pcap";
	// This is expected in the form
	// "<cf1>:<cq11>,<cq12>,<cq13>|<cf2>:<cq21>,<cq22>|......."
	String[] tokenizedColumnFamiliesWithColumnQualifiers = StringUtils
			.split(allColumnFamiliesColumnQualifiers, "\\|");
	for (String tokenizedColumnFamilyWithColumnQualifiers : tokenizedColumnFamiliesWithColumnQualifiers) {
		
		System.out.println(tokenizedColumnFamilyWithColumnQualifiers);
		String[] cfCqTokens = StringUtils.split(
				tokenizedColumnFamilyWithColumnQualifiers, ":");
		String columnFamily = cfCqTokens[0];
		String[] columnQualifiers = StringUtils.split(cfCqTokens[1],
				",");
		for (String columnQualifier : columnQualifiers) {
			conf.addColumn(columnFamily, columnQualifier);
		}

		conf.setBatch(false);

	}
    _quorum = "127.0.0.1";
    _port = "2181";
    init();
  }
 
  public void init() {
    try {
    	System.out.println("init HTableConnector");
      this.connector = new HTableConnector(conf, _quorum, _port);

      System.out.println("finish HTableConnector");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }
  
  public void execute(Packet input) {
	
	  
    try {
      this.connector.getTable().put(conf.getPutFromPacket(input));
    } catch (IOException ex) {

  		//error
    	System.out.println(ex);
      throw new RuntimeException(ex);
    }

 
  }
 
  
  public void destory() {
    this.connector.close();
  }
    
}