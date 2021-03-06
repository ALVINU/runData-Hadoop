package org.apache.hadoop.yarn.api.records;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;

/**
 * Created by jyb on 12/17/17.
 */
public class SplitDataInfo {
  private static final Log LOG = LogFactory.getLog(SplitDataInfo.class);

  private String fileName, filePath;
  private ApplicationId applicationId;
  private String containerId;
  private long start, neededLength, blockId, blockSize;
  private String sourceAddress, targetAddress;
  private String sourceName, targetName, sourceRackName;

  public SplitDataInfo(String filePath, long start, long neededLength) {
	this.filePath = filePath;
	this.start = start;
	this.neededLength = neededLength;
  }

  public SplitDataInfo(String sourceName, String filePath,
					   long start, long neededLength, long blockId, long blockSize) {
	this(filePath, start, neededLength);
	this.sourceName = sourceName;
	this.blockId = blockId;
	this.blockSize = blockSize;
  }

  public SplitDataInfo(String filePath, String sourceAddress, String targetAddress,
					   String targetName, long blockId, long blockSize){
	this.filePath = filePath;
	this.sourceAddress = sourceAddress;
	this.targetAddress = targetAddress;
	this.targetName = targetName;
	this.blockId = blockId;
	this.blockSize = blockSize;
  }

  public SplitDataInfo(String targetName, long blockId){
	this.targetName = targetName;
	this.blockId = blockId;
  }

  public static SplitDataInfo createNewInstanceAppMasterToRM(String[] args){
	//args: (rack/source Name: default believe sourceName), filePath, start, neededLength, blockId, blockSize
	SplitDataInfo sdi = new SplitDataInfo(args[0], args[1],
			Long.parseLong(args[2]), Long.parseLong(args[3]), Long.parseLong(args[4]), Long.parseLong(args[5]));
	return sdi;
  }

  public static SplitDataInfo createNewInstanceRMToAppMaster(String[] args){
	//args: (used), filePath, sourceAddress, targetAddress, targetName, blockId, blockSize
	SplitDataInfo sdi = new SplitDataInfo(args[1], args[2], args[3], args[4],
			Long.parseLong(args[5]), Long.parseLong(args[6]));
	return sdi;
  }

  public static SplitDataInfo createNewInstanceAppMasterToNode(String[] args){
	//args: (unused), containerId, filePath, sourceAddress, targetAddress, targetName, blockId, blockSize, start
	SplitDataInfo sdi = new SplitDataInfo(args[2], args[3], args[4], args[5],
			Long.parseLong(args[6]), Long.parseLong(args[7]));
	sdi.containerId = args[1];
	sdi.start = Long.parseLong(args[8]);
	return sdi;
  }

  public static SplitDataInfo createNewInstanceRMToNode(String info) {
	String[] splits = info.split("&");
	SplitDataInfo sdi = new SplitDataInfo(splits[0], Long.parseLong(splits[1]));
	return sdi;
  }

  public void changeSourceName(String sourceName) {
	this.sourceRackName = this.sourceName;
	this.sourceName = sourceName;
  }

  public void setTargetAddress(String targetAddress) { this.targetAddress = targetAddress; }

  public void setSourceName(String sourceName) { this.sourceName = sourceName; }

  public void setStart(long start) { this.start = start; }

  public void setContainerId(String containerId) {
	this.containerId = containerId;
  }

  public void setTargetName(String targetName) {
	this.targetName = targetName;
  }

  public void setSourceAddress(String sourceAddress) {
	this.sourceAddress = sourceAddress;
  }

  public void setBlockId(long blockId) {
	this.blockId = blockId;
  }

  public void setApplicationId(ApplicationId applicationId) {
	this.applicationId = applicationId;
  }

  public void setSourceRackName(String sourceRackName) {
	this.sourceRackName = sourceRackName;
  }

  public void setBlockSize(long blockSize) {
	this.blockSize = blockSize;
  }

  public String getTargetAddress() { return targetAddress; }

  public String getSourceAddress() { return sourceAddress; }

  public String getSourceName() { return sourceName; }

  public String getTargetName() { return targetName; }

  public String getContainerId() { return containerId; }

  public ApplicationId getApplicationId() { return applicationId; }

  public long getBlockSize() { return blockSize; }

  public String getFilePath() { return filePath; }

  public long getBlockId() { return blockId; }
  public String getBlockFullName() { return "blk_" + blockId; }

  public long getStart() { return start; }

  public long getNeededLength() { return neededLength; }

  public String getInfoAppMasterToRM() {
	String ret = "&" + filePath + "&" + start + "&" + neededLength + "&" + blockId + "&" + blockSize;
	return ret;
  }

  public String printSplitDataLogs() {
	String ret = filePath + " " + sourceRackName + " " + sourceName + " " +
			start + " " + neededLength + " " + blockId + " " + blockSize;
	return ret;
  }

  public String getInfoRMToAppMaster() {
	String ret = "&" + filePath + "&" + sourceAddress + "&" + targetAddress +
			"&" + targetName + "&" + blockId + "&" + blockSize;
	return ret;
  }

  public String getInfoAppMasterToNode() {
	String ret = "&" + containerId + getInfoRMToAppMaster() + "&" + start;
	return ret;
  }

  public static void createNecessaryPath() {
	File blocksFolder = new File("/home/jyb/Desktop/hadoop/hadoop-2.6.2/logs/blockCache");
	if(!blocksFolder.isDirectory()) blocksFolder.mkdirs();
  }

  public String getInfoRMToNode() {
	String ret = "&&" + getInfoRMToRMNode();
	return ret;
  }

  public String getInfoRMToRMNode() {
	String ret = targetName + "&" + blockId;
	return ret;
  }
}
