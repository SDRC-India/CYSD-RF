package org.sdrc.cysdrf.service;

import java.io.IOException;


public interface AggregationService {

	void doAggregate(String projectList,String areaCode,int timeperiodId,String fileName,Boolean flag) throws Exception ;

	String oprationalAggregate(int sourceNId,String areaCode,int timeperiodId,String fileName,Boolean flag) throws IOException;
}
