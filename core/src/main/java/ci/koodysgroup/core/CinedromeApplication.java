package ci.koodysgroup.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"ci.koodysgroup.domains.entities"})
@EnableJpaRepositories(basePackages = "ci.koodysgroup.repositories")
@SpringBootApplication(scanBasePackages = "ci.koodysgroup")
public class CinedromeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinedromeApplication.class, args);
	}

}
