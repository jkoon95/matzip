package kr.or.iei;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MatzipEmail {
	@Autowired	
	private JavaMailSender matzipEmail;
}
