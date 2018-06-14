package exe07;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
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

//deletando email
public class GerenciadorDeEventos {

	JanelaInbox janelaInbox;
	String hostServer;
	String username;
	String senha;

	private Session session;
	private Store store;
	private Folder inbox;

	public void conectar(ActionEvent event) {

		preencherCampos();
		Authenticator authenticator = new MeuAutenticador(username, senha);

		Properties props = new Properties();
		props.put("mail.imap.host", "imap.gmail.com");
		props.put("mail.imap.socketFactory.port", "993"); // porta ssl do servidor imap
		props.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // especifica classe para criar
		props.put("mail.imap.auth", "true"); // autenticação requerida
		props.put("mail.store.protocol", "imap"); // protocolo para leitura de email

		try {
			session = Session.getInstance(props, authenticator);
			store = session.getStore();
			store.connect();
			inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);

			Arrays.stream(inbox.getMessages())			
				.forEach(janelaInbox.listMensagens::add);
		} 
		catch (AuthenticationFailedException e) {
			// TODO: handle exception
			janelaInbox.setMensagemDeErro("Login Inválido");
		}
		catch (NoSuchProviderException e) {
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

	public void excluir(ActionEvent event, Message message) {

		boolean confirmado = janelaInbox.confirmaExclusao();

		if (confirmado) {
			
			try {	
				//marca a mensagem pra deletar
				message.setFlag(Flags.Flag.DELETED, true);
				
				//remove da tabela
				janelaInbox.listMensagens.remove(message);
				janelaInbox.tabelaInbox.refresh();
			}
			catch (MessagingException e) {
				// TODO: handle exception
				e.printStackTrace();
				janelaInbox.setMensagemDeErro(e.getMessage());
			}
		}
	}

	public void sair(ActionEvent evt) {
		try {
			//fecha a pasta, excluindo todas as mensagens com a flag DELETE
			inbox.close(true);
			store.close();
		}		
		catch(NullPointerException e) {}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		finally {
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
