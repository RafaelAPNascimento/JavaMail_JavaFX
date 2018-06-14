package exe01;

import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

//classe que monitora quando o usuário solicita autenticação na DialogAutenticacao
public class AutenticarEvent implements EventHandler<ActionEvent> {

	DialogAutenticacao dialogAutenticacao;
	MeuAutenticador autenticador;	
	
	//inicializa a DialogAutenticacao e passa esta classe no construtor para monitorar os eventos
	public AutenticarEvent() {
		dialogAutenticacao = new DialogAutenticacao(this);
	}

	//o usuário clicou no botão autenticar
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub		
		processarAutenticacao(event);
	}	
		
	private void processarAutenticacao(ActionEvent event) {
				
		PasswordAuthentication passwordAuthentication = dialogAutenticacao.getPasswordAuthentication();
		//inica o MeuAutenticador
		autenticador = new MeuAutenticador(passwordAuthentication);
		
		try {
			//obtem uma Sessao passando o autneticador
			Session sessao = Session.getDefaultInstance(new Properties(), autenticador);
			
			//obtem uma referência ao repositório de email usando o protocolo imaps
			Store store = sessao.getStore("imaps");

			//tenta conectar, se falhar lança Exception...
			store.connect("imap.gmail.com", null, null);
			dialogAutenticacao.getMensagem().setText("Autenticação Efetuada com sucesso");	
		
			store.close();			
		}
		catch (AuthenticationFailedException e) {
			// TODO: handle exception
			dialogAutenticacao.getMensagem().setText("Credenciais Inválidas!");
		}		
		catch (MessagingException e) {
			// TODO: handle exception			
			dialogAutenticacao.getMensagem().setText(e.getMessage());
		}
	}
			

}
