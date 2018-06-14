package exe02;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MeuAutenticador extends Authenticator {

	private final PasswordAuthentication passwordAuthentication;	
	
	//passamos login e senha no construtor
	public MeuAutenticador(String login, String senha) {
		super();
		passwordAuthentication = new PasswordAuthentication(login, senha);
	}
	
	//toda aplicação deve sobrescrever getPasswordAuthentication()
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		// TODO Auto-generated method stub
		return passwordAuthentication;
	}
}
