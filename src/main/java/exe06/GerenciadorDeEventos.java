package exe06;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage.RecipientType;

import exe02.MeuAutenticador;
import javafx.application.Platform;
import javafx.event.ActionEvent;

public class GerenciadorDeEventos {

	JanelaInbox janelaInbox;
	String hostServer;
	String username;
	String senha;

	private Session session;
	private Store store;
	private Folder folder;

	public void conectar(ActionEvent event) {

		preencherCampos();
		Authenticator authenticator = new MeuAutenticador(username, senha);

		Properties props = new Properties();
		props.put("mail.imap.host", "imap.gmail.com");
		props.put("mail.imap.socketFactory.port", "993"); // porta ssl do servidor imap
		props.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // especifica classe para criar
		props.put("mail.imap.auth", "true"); // autenticação requerida
		props.put("mail.store.protocol", "imap"); // protocolo para leitura de email

		props.put("mail.smtp.host", hostServer);
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465"); // porta ssl
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // especifica classe para criar
																						// SMTP Socket
		props.put("mail.smtp.auth", "true"); // autenticação requerida

		try {
			session = Session.getInstance(props, authenticator);
			Store store = session.getStore();
			store.connect();
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);

			Arrays.stream(inbox.getMessages())			
				.forEach(janelaInbox.listMensagens::add);
			
		} catch (AuthenticationFailedException e) {
			// TODO: handle exception
			janelaInbox.setMensagemDeErro("Login Inválido");
		} catch (NoSuchProviderException e) {
			// TODO: handle exception
			e.printStackTrace();
			janelaInbox.setMensagemDeErro("Provedor Inválido");
		} 
		catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			janelaInbox.setMensagemDeErro("Ocorreu um erro ao recuperar as mensagens");
		}
	}

	//quando o usuário clicar no botão encaminhar
	public void encaminhar(ActionEvent event, Message message) {

		//exibe a dialog que solicita o endereço
		Optional<String> result = janelaInbox.exibirDialogIformarEnderecos();

		//se o usuário relamente forneceu o endereço, procede o encamihamento
		if (result.isPresent()) {
			
			try {
				//divide a String por ponto e virgula
				String[] enderecos = result.get().split(";");				

				//cria uma nova mensagem
				Message forward = new MimeMessage(session);
				
				//como se trata de um encaminhamento, o destinatário vai viar o remetente
				String to = InternetAddress.toString(message.getRecipients(Message.RecipientType.TO));
				String subject = message.getSubject();				
				
				Address[] para = new InternetAddress[enderecos.length];
				
				for(int i = 0; i < enderecos.length; i++)
					//inicia cada índice do array
					para[i] = new InternetAddress(enderecos[i]);
				
				//adicona os noves destinatários
				forward.setRecipients(RecipientType.TO, para);
				
				forward.setSubject("encaminhada: " + subject);
				
				//como se trata de um encaminhamento, o destinatário vai viar o remetente
				forward.setFrom(new InternetAddress(to));
				
				//cria o BodyPart
				BodyPart messageBodyPart = new MimeBodyPart();
				
				//MUltipart container de partes da mensagem
				Multipart multipart = new MimeMultipart();
				
				//a parte da nova mensagem é a mensagem original
				messageBodyPart.setContent(message, "message/rfc822");
				
				multipart.addBodyPart(messageBodyPart);
				
				//coloca o multipart na nova mensagem
				forward.setContent(multipart);
				
		        forward.saveChanges();		

		        //envia
		        Transport transport = session.getTransport();
				transport.connect();

				//envia para todos os endereços
				transport.sendMessage(forward, forward.getAllRecipients());
				transport.close();
				
				janelaInbox.setMensagemDeSucesso("mensagem encaminhada");
			}
			catch (MessagingException e) {
				// TODO: handle exception
				janelaInbox.setMensagemDeErro(e.getMessage());
			}
		}
	}

	public void sair(ActionEvent evt) {
		try {
			folder.close();
			store.close();
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		} finally {
			Platform.exit();
		}
	}

	private void preencherCampos() {
		// TODO Auto-generated method stub
		hostServer = janelaInbox.txtHostServer.getText();
		username = janelaInbox.txtUsername.getText();
		senha = janelaInbox.txtSenha.getText();
	}

}
