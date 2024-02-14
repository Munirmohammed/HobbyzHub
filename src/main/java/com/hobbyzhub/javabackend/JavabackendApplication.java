package com.hobbyzhub.javabackend;
import com.hobbyzhub.javabackend.sharedconfig.CaffeineCacheConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({CaffeineCacheConfig.class})
public class JavabackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(JavabackendApplication.class, args);
	}
}
