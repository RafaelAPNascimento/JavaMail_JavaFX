package exe02;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

//Monitora quando o usuário clicar no botão enviar
public class EventoEnviarEmail implements EventHandler<ActionEvent>{

	JanelaEnviar janelaEnviar;
	
	String hostServer;
	String para;
	String de;
	String assunto;
	String username;	
	String senha;
	String conteudoMensagem;
	
	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
		janelaEnviar.mensagem.setText("");
		preencherCampos();
		
		try {						
			enviarEmail();			
			janelaEnviar.mensagem.setText("Mensagem enviada com sucesso.");			
		}
		catch (UnsupportedEncodingException e) {
			janelaEnviar.mensagem.setText("Caracteres não suportados na mensagem");
			e.printStackTrace();
		}
		catch (AuthenticationFailedException e) {
			//endereco invalido
			janelaEnviar.mensagem.setText("Usuário ou senha Inválidos");
			e.printStackTrace();
		}
		catch (AddressException e) {
			//endereco invalido
			janelaEnviar.mensagem.setText("Formato de endereço de email Inválido");
			e.printStackTrace();
		}
		catch (MessagingException e) {
			//endereco invalido
			janelaEnviar.mensagem.setText("Erro ao enviar a mensagem");
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO: handle exception
			janelaEnviar.mensagem.setText("Erro ao enviar a mensagem");
			e.printStackTrace();
		}
		finally {
			janelaEnviar.txtSenha.setText("");
		}
	}
	
	private void enviarEmail() throws AddressException, MessagingException, UnsupportedEncodingException {
		
		// TODO Auto-generated method stub
		Authenticator authenticator = new MeuAutenticador(username, senha);
		
		//configura o objeto Properties
		Properties props = new Properties();
		props.put("mail.smtp.host", hostServer);				
		props.put("mail.smtp.socketFactory.port", "465");	//porta ssl
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //especifica classe usada para criar conexão SMTP
		props.put("mail.smtp.auth", "true");	//autenticação requerida		
		
		//obtem um objeto Session sob as credenciais e propriedades especificadas
		Session session = Session.getInstance(props, authenticator);
		
		//cria uma mensagem vazia
		Message mensagem = new MimeMessage(session);
		
		//endereço de quem envia e recebe
		Address enderecoDe = new InternetAddress(username, de);	//address except		
		Address enderecoPara = new InternetAddress(para);
		
		//configura os campos da mensagem
		mensagem.setFrom(enderecoDe);	//messaging excep
		mensagem.setRecipient(Message.RecipientType.TO, enderecoPara);
		mensagem.setSubject(assunto);
		mensagem.setText(conteudoMensagem);
				
		//envia
		Transport.send(mensagem);		//auth failed except
	}

	private void preencherCampos() {
		// TODO Auto-generated method stub
		hostServer = janelaEnviar.txtHostServer.getText();
		para = janelaEnviar.txtPara.getText();
		de = janelaEnviar.txtDe.getText();
		assunto = janelaEnviar.txtAssunto.getText();
		username = janelaEnviar.txtUsername.getText();
		senha = janelaEnviar.txtSenha.getText();
		conteudoMensagem = janelaEnviar.txtConteudoMensagem.getText();
	}	
}
