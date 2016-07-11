# hbasepacket
##基于HBASE的 分布式包记录、回溯 工具
###phb为抓包程序
###phb-service为数据包回溯服务
####详细说明可以查看：基于Hbase数据收集与回溯系统搭建.pdf  thks for vista85:vista85@sina.com


####索引格式：
#####0a020a5a-0a20038d-6-22-49795-31905-0
#####源ip-目的ip-协议-端口-srcport-dstport-sessionkey-0
#####IP为16进制表示格式

####GET:
####pcapGetter/getPcapsByKeys (根据索引)
#####根据索引列表获取数据包：
	keys [键值列表 使用','分割]
	lastRowKey  [一般不用]
	startTime   [查询开始时间戳  如果为空表示从0开始]
	endTime     [查询结束时间戳  如果为空表示当前时间]
	includeDuplicateLastRow [一般不用]
	includeReverseTraffic [一般不用]
	maxResponseSize [限制返回的pcap文件的大小 单位 bytes]

    
    例如：
    0a020a5a-0a20038d-6-22-49795-31905-0,0a020a5a-0a20038d-6-22-49795-31906-0
    http://127.0.0.1/pcap/pcapGetter/getPcapsByKeys?keys=0a020a5a-0a20038d-6-22-49795-31905-0,0a020a5a-0a20038d-6-22-49795-31906-0&startTime=1454577250386


####pcapGetter/getPcapsByKeyRange (根据索引范围)
#####根据索引范围来获取数据包
    startKey String startKey,
    endKeyString endKey,
    maxResponseSize String maxResponseSize, [限制返回的pcap文件的大小 单位 bytes]
    startTime   [查询开始时间戳  如果为空表示从0开始]
    endTime     [查询结束时间戳  如果为空表示当前时间]
    例如：
    startKey:0a020a5a-0a20038d-0-0-0-0-0
    endKey: 0a020a5a-0a20038d-99999-99999-99999-99999-0
    以上表示 0a020a5a到0a20038d所有的数据包(10.2.10.90 到 10.32.3.141)
    http://127.0.0.1/pcap/pcapGetter/getPcapsByKeyRange?startKey=0a020a5a-0a20038d-0-0-0-0-0&endKey=0a020a5a-0a20038d-99999-99999-99999-99999-0&startTime=1454577250386


####pcapGetter/getPcapsByIdentifiers
#####根据ip地址协议端口等信息获取原始数据包(根据特征，地址，端口等)
    srcIp
    dstIp
    protocol
    srcPort
    dstPort
    startTime   [查询开始时间戳  如果为空表示从0开始]
    endTime     [查询结束时间戳  如果为空表示当前时间]		
    includeReverseTraffic(默认"false"）[一般不用]
    例如：
     http://127.0.0.1:8000/pcap/pcapGetter/getPcapsByIdentifiers?srcIp=10.2.10.90&dstIp=10.32.3.141&protocol=6&srcPort=22&dstPort=49795