package com.example.jasper;

import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsViewResolver;

import com.entity.User;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan("com.entity")
public class JasperApplication {
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(JasperApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner commandLineRunner() {
		return (x) -> {
			userDao.save(new User("Bana", "Depok"));
			userDao.save(new User("Fatih", "Depok"));
			userDao.save(new User("Rafa", "Depok"));
			userDao.save(new User("ewewew", "wewewe"));
		};
	}
	
	@Controller
	private class MyController {
		@GetMapping("")
		public String showData(Model model) {
			List<User> users = (List<User>) userDao.findAll();
			model.addAttribute("users", users);
			return "index";
		}
		
		@GetMapping("/cetak")
		public String cetak(Model model) {
			model.addAttribute("datasource", dataSource);
			model.addAttribute("format", "pdf");
			return "rpt_user";
		}
	}
	
	@Configuration
	public class MyViewResolverConfig extends WebMvcConfigurerAdapter {
		
		@Bean
		public JasperReportsViewResolver getJasperReportViewResolver() {
			JasperReportsViewResolver jasperReportsViewResolver = new JasperReportsViewResolver();
			jasperReportsViewResolver.setPrefix("classpath:templates/reports/");
			jasperReportsViewResolver.setSuffix(".jrxml");

			jasperReportsViewResolver.setReportDataKey("datasource");
			jasperReportsViewResolver.setViewNames("*rpt_*");
			jasperReportsViewResolver.setViewClass(JasperReportsMultiFormatView.class);
			jasperReportsViewResolver.setOrder(0);
			return jasperReportsViewResolver;
		}
	}
}
