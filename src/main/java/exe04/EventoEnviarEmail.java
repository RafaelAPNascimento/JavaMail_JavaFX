package exe04;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import exe02.MeuAutenticador;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

//Controle de Eventos da JanelaEnviar
public class EventoEnviarEmail implements EventHandler<ActionEvent>{

	JanelaEnviar janelaEnviar;
	String hostServer;
	String para;
	String de;
	String assunto;
	String username;	
	String senha;
	String conteudoHtmlMensagem;	
	
	
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
		preencherCampos();
		
		try {						
			enviarEmail();										
			janelaEnviar.setMensagemDeSucesso("Email Enviado com Sucesso");
			System.out.println("OK");
		}
		catch (UnsupportedEncodingException e) {
			janelaEnviar.setMensagemDeErro("Caracteres não suportados na mensagem");
			e.printStackTrace();
		}
		catch (AuthenticationFailedException e) {
			//endereco invalido
			janelaEnviar.setMensagemDeErro("Usuário ou senha Inválidos");
			e.printStackTrace();
		}
		catch (AddressException e) {
			//endereco invalido
			janelaEnviar.setMensagemDeErro("Formato de endereço de email Inválido");
			e.printStackTrace();
		}
		catch (MessagingException e) {
			//endereco invalido
			janelaEnviar.setMensagemDeErro("Erro ao enviar a mensagem");
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO: handle exception
			janelaEnviar.setMensagemDeErro("Erro ao enviar a mensagem");
			e.printStackTrace();
		}
		finally {
			janelaEnviar.txtSenha.setText("");
		}
	}
	
	private void enviarEmail() throws AddressException, MessagingException, UnsupportedEncodingException {
		
		// TODO Auto-generated method stub
		Authenticator authenticator = new MeuAutenticador(username, senha);
		
		//configura objeto properties
		Properties props = new Properties();
		props.put("mail.smtp.host", hostServer);		
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");	//porta ssl
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //especifica classe para criar SMTP Socket
		props.put("mail.smtp.auth", "true");	//autenticação requerida		
		
		//obtem um objeto Session sob as credenciais e propriedades especificadas
		Session session = Session.getInstance(props, authenticator);
		
		Message mensagem = new MimeMessage(session);
		
		Address enderecoDe = new InternetAddress(username, de);	//address except
		Address enderecoPara = new InternetAddress(para);
		
		mensagem.setFrom(enderecoDe);	//messaging excep
		mensagem.setRecipient(Message.RecipientType.TO, enderecoPara);
		mensagem.setSubject(assunto);		
		
		//coloca o conteudo html na mensagem
		setConteudoHTML(mensagem);
				
		Transport.send(mensagem);		//auth failed except
	}
	
	private void setConteudoHTML(Message mensagem) throws MessagingException {
		
		Multipart multipart = new MimeMultipart();		
		
		//cria um BodyPart representando um conteudo HTML
		BodyPart messageBodyPart = new MimeBodyPart();				
		
		messageBodyPart.setContent(conteudoHtmlMensagem, "text/html");
		
		//adiciona o BodyPart nop Multipart
		multipart.addBodyPart(messageBodyPart);
		
		//adiciona o MUltipart na mensagem
		mensagem.setContent(multipart);
	}

	private void preencherCampos() {
		// TODO Auto-generated method stub
		hostServer = janelaEnviar.txtHostServer.getText();
		para = janelaEnviar.txtPara.getText();
		de = janelaEnviar.txtDe.getText();
		assunto = janelaEnviar.txtAssunto.getText();
		username = janelaEnviar.txtUsername.getText();
		senha = janelaEnviar.txtSenha.getText();
		
		//extrai o conteudo html de HTMLEditor na forma de uma String 
		conteudoHtmlMensagem = janelaEnviar.htmlEditor.getHtmlText();		
	}	
}
