package pl.ncdchot.foosball;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.WebApplicationInitializer;

@SpringBootApplication
public class FoosballApplication extends SpringBootServletInitializer{
//		implements WebApplicationInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(FoosballApplication.class);
	}
	public static void main(String[] args) {
		SpringApplication.run(FoosballApplication.class, args);
	}


//	private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
//		return builder.sources(FoosballApplication.class).bannerMode(Banner.Mode.OFF);
//	}
}
