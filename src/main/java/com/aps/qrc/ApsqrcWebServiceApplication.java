package com.aps.qrc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/*
 * ============================================================
 * #Developer Details |
 * -------------------
 * @author: Hizbullah Watandost
 * @Designation: IT Research and Development Officer in APS
 * @Website: http://www.aps.af
 * @Contact_email: hizbullah.watandost@aps.af
 * ============================================================
 */

@SpringBootApplication(scanBasePackages = {"com.aps.qrc"})
public class ApsqrcWebServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApsqrcWebServiceApplication.class, args);
    }

}
