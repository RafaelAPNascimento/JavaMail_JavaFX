package exe05;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

import exe02.MeuAutenticador;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

//Eventos da JanelaInbox
public class GerenciadorDeEventos {
	
	JanelaInbox janelaInbox;	
	String hostServer;
	String username;	
	String senha;
	
	//repositório de email
	private Store store;

	//pasta de mensagens
	private Folder folder;	
	
	//tenta uma conexão com o provedor
	public void conectar(ActionEvent event) {
		
		preencherCampos();
		Authenticator authenticator = new MeuAutenticador(username, senha);

		//configura propriedades
		Properties props = new Properties();		
		props.put("mail.imap.host", "imap.gmail.com");
		props.put("mail.imap.socketFactory.port", "993"); // porta ssl do servidor imap
		props.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // especifica classe para criar									
		props.put("mail.imap.auth", "true"); 	// autenticação requerida
		props.put("mail.store.protocol", "imap");	//protocolo para leitura de email
		
		try {
			Session session = Session.getInstance(props, authenticator);
			
			//conexão com o repositório de mensagens
			Store store = session.getStore();
			store.connect();
			
			//obtem a pasta INBOX que contem todas as mensagens
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);			
			
			//coloca cada mensagem na list de mensagens para exibir
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
	
	//ao temrinar a aplicação, encerra a conexão
	public void sair(ActionEvent evt) {
		try {
			folder.close();
			store.close();			
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		finally {
			Platform.exit();
		}
	}

	//evento selecionar mensagem
	public void selecionarMensagem(ObservableValue<? extends Message> mensagem, Message antigaSelecao, Message novaSelecao) {
				
		Message message = mensagem.getValue();			
		
		try {			
			//extrai o conteudo html da mensagem
			String html = getConteudoHTML(message);		
			
			//coloca o conteudo no webview pra renderizar
			janelaInbox.webView.getEngine().loadContent(html);
			
			//exibe as tags no campo conteudo da JanelaInbox
			janelaInbox.txtConteudoMensagem.setText(html);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//metodo auxiliar que retorna o conteudo html da mensagem ou null
	private String getConteudoHTML(Message message) throws IOException, MessagingException {
		
		Object obj = message.getContent();
		
		//pega o conteudo da mensagem e checa se é Multipart
		if(obj instanceof Multipart) {
			
			Multipart multipart = (Multipart) obj;
			
			//loop nas partes do Multipart
			for(int i = 0; i < multipart.getCount(); i++) {
				
				Part part = multipart.getBodyPart(i);
				System.out.println(part.getContentType());
				
				//se esta parte tiver conteudo text/plain ou text/html, extrai e retorna
				if(part.getContentType().toLowerCase().startsWith("text")) {
					return (String) part.getContent();
				}
			}
		}
		//a mensagem não tem conteudo html
		return "";
	}
	 
	private void preencherCampos() {
		// TODO Auto-generated method stub
		hostServer = janelaInbox.txtHostServer.getText();
		username = janelaInbox.txtUsername.getText();
		senha = janelaInbox.txtSenha.getText();
	}

}
