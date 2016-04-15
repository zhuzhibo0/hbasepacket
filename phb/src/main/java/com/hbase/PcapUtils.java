package com.hbase;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
 

/**
 * The Class PcapUtils.
 */
public class PcapUtils {

  /** The Constant SESSION_KEY_SEPERATOR. */
  private static final char SESSION_KEY_SEPERATOR = '-';

 
  /**
   * Convert ipv4 ip to hex.
   * 
   * @param ipAddress
   *          the ip address
   * @return the string
   */
  public static String convertIpv4IpToHex(String ipAddress) {
    StringBuffer hexIp = new StringBuffer(64);
    String[] ipSegments = ipAddress.split("\\.");

    for (String ipSegment : ipSegments) {
      hexIp.append(convertIpSegmentToHex(ipSegment));
    }

    return hexIp.toString();

  }

  /**
   * Gets the session key.
   * 
   * @param srcIp
   *          the src ip
   * @param dstIp
   *          the dst ip
   * @param protocol
   *          the protocol
   * @param srcPort
   *          the src port
   * @param dstPort
   *          the dst port
   * @return the session key
   */
  public static String getSessionKey(String srcIp, String dstIp,
      String protocol, String srcPort, String dstPort) {
    return getSessionKey(srcIp, dstIp, protocol, srcPort, dstPort, null, null);
  }

  /**
   * Gets the session key.
   * 
   * @param srcIp
   *          the src ip
   * @param dstIp
   *          the dst ip
   * @param protocol
   *          the protocol
   * @param srcPort
   *          the src port
   * @param dstPort
   *          the dst port
   * @param ipId
   *          the ip id
   * @param fragmentOffset
   *          the fragment offset
   * @return the session key
   */
  public static String getSessionKey(String srcIp, String dstIp,
      String protocol, String srcPort, String dstPort, String ipId,
      String fragmentOffset) {

    StringBuffer sb = new StringBuffer(40);
    sb.append(convertIpv4IpToHex(srcIp)).append(SESSION_KEY_SEPERATOR)
        .append(convertIpv4IpToHex(dstIp)).append(SESSION_KEY_SEPERATOR)
        .append(protocol == null ? "0" : protocol)
        .append(SESSION_KEY_SEPERATOR).append(srcPort == null ? "0" : srcPort)
        .append(SESSION_KEY_SEPERATOR).append(dstPort == null ? "0" : dstPort)
        .append(SESSION_KEY_SEPERATOR).append(ipId == null ? "0" : ipId)
        .append(SESSION_KEY_SEPERATOR)
        .append(fragmentOffset == null ? "0" : fragmentOffset);

    return sb.toString();
  }

  /**
   * Gets the session key.
   * 
   * @param srcIp
   *          the src ip
   * @param dstIp
   *          the dst ip
   * @param protocol
   *          the protocol
   * @param srcPort
   *          the src port
   * @param dstPort
   *          the dst port
   * @param ipId
   *          the ip id
   * @param fragmentOffset
   *          the fragment offset
   * @return the session key
   */
  public static String getSessionKey(String srcIp, String dstIp, int protocol,
      int srcPort, int dstPort, int ipId, int fragmentOffset) {
    String keySeperator = "-";
    StringBuffer sb = new StringBuffer(40);
    sb.append(convertIpv4IpToHex(srcIp)).append(keySeperator)
        .append(convertIpv4IpToHex(dstIp)).append(keySeperator)
        .append(protocol).append(keySeperator).append(srcPort)
        .append(keySeperator).append(dstPort).append(keySeperator).append(ipId)
        .append(keySeperator).append(fragmentOffset);

    return sb.toString();
  }

  /**
   * Gets the short session key. (5-tuple only)
   * 
   * @param srcIp
   *          the src ip
   * @param dstIp
   *          the dst ip
   * @param protocol
   *          the protocol
   * @param srcPort
   *          the src port
   * @param dstPort
   *          the dst port
   * @return the session key
   */
  public static String getShortSessionKey(String srcIp, String dstIp, int protocol,
      int srcPort, int dstPort) {
    String keySeperator = "-";
    StringBuffer sb = new StringBuffer(40);
    sb.append(convertIpv4IpToHex(srcIp)).append(keySeperator)
        .append(convertIpv4IpToHex(dstIp)).append(keySeperator)
        .append(protocol).append(keySeperator).append(srcPort)
        .append(keySeperator).append(dstPort);

    return sb.toString();
  }
  
  // public static String convertPortToHex(String portNumber) {
  // return convertPortToHex(Integer.valueOf(portNumber));
  //
  // }
  //
  // public static String convertPortToHex(int portNumber) {
  // return convertToHex(portNumber, 4);
  //
  // }
  //
  // public static String convertProtocolToHex(String protocol) {
  // return convertProtocolToHex(Integer.valueOf(protocol));
  //
  // }
  //
  // public static String convertProtocolToHex(int protocol) {
  // return convertToHex(protocol, 2);
  // }

  /**
   * Convert ip segment to hex.
   * 
   * @param ipSegment
   *          the ip segment
   * @return the string
   */
  public static String convertIpSegmentToHex(String ipSegment) {
    return convertIpSegmentToHex(Integer.valueOf(ipSegment));

  }

  /**
   * Convert ip segment to hex.
   * 
   * @param ipSegment
   *          the ip segment
   * @return the string
   */
  public static String convertIpSegmentToHex(int ipSegment) {
    return convertToHex(ipSegment, 2);

  }

  /**
   * Convert to hex.
   * 
   * @param number
   *          the number
   * @param length
   *          the length
   * @return the string
   */
  public static String convertToHex(int number, int length) {
    return StringUtils.leftPad(Integer.toHexString(number), length, '0');

  }

    
 
}