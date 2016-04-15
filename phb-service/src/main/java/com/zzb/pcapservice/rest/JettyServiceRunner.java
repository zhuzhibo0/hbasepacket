package com.zzb.pcapservice.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.zzb.pcapservice.PcapReceiverImplRestEasy;

public class JettyServiceRunner extends Application  {
	

	private static Set services = new HashSet(); 
		
	public  JettyServiceRunner() {     
		// initialize restful services   
		System.out.printf( "initialize restful services\n" );
		services.add(new PcapReceiverImplRestEasy());  

	}
	@Override
	public  Set getSingletons() {
		return services;
	}  
	public  static Set getServices() {  
		return services;
	} 
}