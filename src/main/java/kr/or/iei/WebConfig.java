package kr.or.iei;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer{

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
			.addResourceHandler("/**")
			.addResourceLocations("classpath:/templates/","classpath:/static/");
		registry
		 .addResourceHandler("/notice/editor/**")
		 .addResourceLocations("file:///C:/Temp/upload/notice/editor/");
		registry
		.addResourceHandler("/store/**")
		.addResourceLocations("file:///C:/Temp/upload/store/");
		registry
			.addResourceHandler("/notice/editor/**")
			.addResourceLocations("file:///C:/Temp/upload/notice/editor/");
		registry
			.addResourceHandler("/board/editor/**")
			.addResourceLocations("file:///C:/Temp/upload/board/editor/");
		registry
		.addResourceHandler("/store/menu/**")
		.addResourceLocations("file:///C:/Temp/upload/store/menu/");
		registry
		.addResourceHandler("/store/evidence/**")
		.addResourceLocations("file:///C:/Temp/upload/store/evidence/");

	}

}
