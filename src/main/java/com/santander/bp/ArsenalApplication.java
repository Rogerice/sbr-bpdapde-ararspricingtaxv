package com.santander.bp;

import br.com.santander.ars.altair.annotation.EnableArsenalAltair;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/** Main class for the AppArsenal application. */
@EnableFeignClients
@EnableArsenalAltair
@SpringBootApplication(scanBasePackages = {"com.santander.bp", "com.santander.bp.exception"})
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
    /**
     * ***********************************************************************
     *
     * <p>SANTANDER CONFIDENTIAL - ARCHETYPE GENERATION -
     *
     * <p>[2020] - [2020] Santander Tecnologia All Rights Reserved.
     *
     * <p>NOTICE: All information contained herein is, and remains the property of Santander
     * Tecnologia, if any. The intellectual and technical concepts contained herein are proprietary
     * to Santander Tecnologia and are protected by trade secret or copyright law. Dissemination of
     * this information or reproduction of this material is strictly forbidden unless prior written
     * permission is obtained from Santander Tecnologia.
     *
     * <p>####415253454E414C####
     */
  }
}
