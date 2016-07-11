# hbasepacket
##基于HBASE的 分布式包记录、回溯 工具
###phb为抓包程序
###phb-service为数据包回溯服务


##API使用说明：
###GET:
####/pcap/pcapGetter/getPcapsByKeys (根据索引)
####param:
	keys        [存储的原始数据包的索引key  例如 ]</br>
	lastRowKey</br>
	startTime   [查询开始时间戳  如果为空表示从0开始]</br>
	endTime     [查询结束时间戳  如果为空表示当前时间]</br>
	includeDuplicateLastRow</br>
	includeReverseTraffic</br>
	maxResponseSize</br>
	
####/pcap/pcapGetter/getPcapsByKeyRange (根据索引范围)
    startKey String startKey,</br>
	endKeyString endKey,</br>
	maxResponseSize String maxResponseSize,</br>
	startTime   [查询开始时间戳  如果为空表示从0开始]</br>
	endTime     [查询结束时间戳  如果为空表示当前时间]</br>

例如:</br>
获取从时间戳1454577250386到当前时间的所有数据包</br>
http://127.0.0.1/pcap/pcapGetter/getPcapsByKeyRange?startKey=0-0-0-0-0-0-0-0&endKey=ffffffff-ffffffff-999-99999-99999-99999-99999&startTime=1454577250386</br>
		
####/pcap/pcapGetter/getPcapsByIdentifiers(根据特征，地址，端口等)
    srcIp</br>
	dstIp</br>
	protocol</br>
	srcPort</br>
	dstPort</br>
	startTime   [查询开始时间戳  如果为空表示从0开始]</br>
	endTime     [查询结束时间戳  如果为空表示当前时间]</br>
	includeReverseTraffic(默认"false"）</br>