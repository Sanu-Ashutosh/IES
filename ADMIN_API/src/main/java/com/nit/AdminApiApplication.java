package com.nit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AdminApiApplication implements CommandLineRunner {
	
	public static PasswordEncoder encoder=new BCryptPasswordEncoder();;

	public static void main(String[] args) {
		SpringApplication.run(AdminApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		 System.out.println("Start");
		 String encode = encoder.encode("Ashutosh@123");
	     System.out.println(encode);
	     System.out.println(encoder.matches("Ashutosh@123", "$2a$10$h8SMQzGSJv2n2AB8OBHQk.Clfk4BeLcSGrTQwm9lYzLLoon3ZeNCq"));
		
	}

}
