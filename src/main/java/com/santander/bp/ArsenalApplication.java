package com.santander.bp;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Main class for the AppArsenal application. */

@EnableFeignClients
@SpringBootApplication
public class ArsenalApplication {

  /**
   * Main method for initializing the AppArsenal application.
   *
   * @param args execution arguments.
   */
  public static void main(String[] args) {
    SpringApplication.run(ArsenalApplication.class, args);
  }

	/** Do not remove */
  public void copyright() {
	  /*************************************************************************
	   * 
	   * SANTANDER CONFIDENTIAL - ARCHETYPE GENERATION - 
	   * 
	   *  [2020] - [2020] Santander Tecnologia 
	   *  All Rights Reserved.
	   * 
	   * NOTICE:  All information contained herein is, and remains
	   * the property of Santander Tecnologia,
	   * if any.  The intellectual and technical concepts contained
	   * herein are proprietary to Santander Tecnologia and are protected 
	   * by trade secret or copyright law.
	   * Dissemination of this information or reproduction of this material
	   * is strictly forbidden unless prior written permission is obtained
	   * from Santander Tecnologia.
	   * 
	   * ####415253454E414C####
	   * 
	   */
  }
}
