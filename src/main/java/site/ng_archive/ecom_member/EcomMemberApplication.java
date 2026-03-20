package site.ng_archive.ecom_member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import site.ng_archive.ecom_common.config.EnableEcomCommon;

@EnableEcomCommon
@SpringBootApplication
public class EcomMemberApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcomMemberApplication.class, args);
	}

}
