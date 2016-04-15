package com.zzb;

import java.io.IOException;

import org.pcap4j.core.BpfProgram.BpfCompileMode;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.core.PcapStat;
import org.pcap4j.packet.Packet;
import org.pcap4j.util.NifSelector;
import org.pcap4j.util.ByteArrays;
import org.apache.commons.lang.StringUtils;

import com.hbase.HBaseSender;
import com.hbase.TupleTableConfig;
import com.sun.jna.Platform;

@SuppressWarnings("javadoc")
public class phb {

  private static  HBaseSender m_hbase_sender;
	
  private static final String COUNT_KEY
    = phb.class.getName() + ".count";
  private static final int COUNT
    = Integer.getInteger(COUNT_KEY, 0);

  private static final String READ_TIMEOUT_KEY
    = phb.class.getName() + ".readTimeout";
  private static final int READ_TIMEOUT
    = Integer.getInteger(READ_TIMEOUT_KEY, 10); // [ms]

  private static final String SNAPLEN_KEY
    = phb.class.getName() + ".snaplen";
  private static final int SNAPLEN
    = Integer.getInteger(SNAPLEN_KEY, 65536); // [bytes]

  public static void main(String[] args) throws PcapNativeException, NotOpenException {
    String filter = args.length != 0 ? args[0] : "";

    System.out.println(COUNT_KEY + ": " + COUNT);
    System.out.println(READ_TIMEOUT_KEY + ": " + READ_TIMEOUT);
    System.out.println(SNAPLEN_KEY + ": " + SNAPLEN);
    System.out.println("\n");

    PcapNetworkInterface nif;
    try {
      nif = new NifSelector().selectNetworkInterface();
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    if (nif == null) {
      return;
    }

    System.out.println(nif.getName() + "(" + nif.getDescription() + ")");

    final PcapHandle handle
      = nif.openLive(SNAPLEN, PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);

    if (filter.length() != 0) {
      handle.setFilter(
        filter,
        BpfCompileMode.OPTIMIZE
      );
    }
     
    initializeHbaseSender();
    
    PacketListener listener
      = new PacketListener() {
          @Override
          public void gotPacket(Packet packet) {
            System.out.println(System.currentTimeMillis()); 
            //System.out.println(ByteArrays.toHexString(packet.getRawData(), " "));
            m_hbase_sender.execute(packet);

          }
        };

    try {
      handle.loop(COUNT, listener);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    PcapStat ps = handle.getStats();
    System.out.println("ps_recv: " + ps.getNumPacketsReceived());
    System.out.println("ps_drop: " + ps.getNumPacketsDropped());
    System.out.println("ps_ifdrop: " + ps.getNumPacketsDroppedByIf());
    if (Platform.isWindows()) {
      System.out.println("bs_capt: " + ps.getNumPacketsCaptured());
    }

    handle.close();
  }

  
  public static boolean initializeHbaseSender() {
		m_hbase_sender = new HBaseSender();
		System.out.println("init hbase sender");

		return true;
	}
}