package mapi.lotto.config;

import lombok.extern.slf4j.Slf4j;
import mapi.lotto.Application;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Profile;

@Slf4j
@Profile("war")
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		logger.info("Configure with spring boot servlet initializer!");
		return application.sources(Application.class);
	}
}