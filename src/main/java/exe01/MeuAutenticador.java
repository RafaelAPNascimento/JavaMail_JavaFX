package exe01;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

//Implementação para Authenticator
public class MeuAutenticador extends Authenticator {

	private PasswordAuthentication passwordAuthentication;
		
	public MeuAutenticador(PasswordAuthentication passwordAuthentication) {
		super();
		this.passwordAuthentication = passwordAuthentication;
	}

	//toda aplicação deve sobrescrever o método getPasswordAuthentication()
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		// TODO Auto-generated method stub
		return this.passwordAuthentication;
	}	
}
