package exe05;

import java.text.SimpleDateFormat;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;


//interface gráfica com o usuário
public class JanelaInbox extends GridPane{
	
	TextField txtHostServer;	
	TextField txtUsername;	
	PasswordField txtSenha;
	TextArea txtConteudoMensagem;	
	
	//widget javafx para conteúdo web
	WebView webView;
	
	Button btoConectar;	
	Button btoSair;
	
	Label lblHostServer;	
	Label lblUserName;
	Label lblSenha;
	Text mensagemUsuario;
	
	//tabela que exibe as mensagens na caixa de entrada
	TableView<Message> tabelaInbox;
	
	//list vinculada à tabela
	ObservableList<Message> listMensagens;
	
	Scene scene;
	GerenciadorDeEventos gerenciadorDeEventos;
	
	//auxiliar que representa os campos da mensagem
	enum CamposMensagem {REMETENTE, ASSUNTO, DATA}

	//inicializa os controles no construtor
	public JanelaInbox(GerenciadorDeEventos gerenciadorDeEventos) {
		// TODO Auto-generated constructor stub
		this.gerenciadorDeEventos = gerenciadorDeEventos;
		this.gerenciadorDeEventos.janelaInbox = this;
		
		//configura o gridPane
		this.setAlignment(Pos.CENTER);
		this.setVgap(5);
		this.setHgap(5);
		this.setPadding(new Insets(10));
		
		txtHostServer = new TextField("imap.gmail.com");		
		txtUsername = new TextField();
		txtSenha = new PasswordField();		
		txtConteudoMensagem = new TextArea();		
		txtConteudoMensagem.setPrefHeight(300);		
		
		webView = new WebView();
		
		btoConectar = new Button("Conectar");	
		btoSair = new Button(" Sair   ");
		
		lblHostServer = new Label("IMAP Server:");		
		lblUserName = new Label("Login:");
		lblSenha = new Label("Senha:");
		mensagemUsuario = new Text();
		
		//adiciona os controles no grid
		//add.(controle, coluna, linha)
		this.add(lblHostServer, 0, 0);
		this.add(txtHostServer, 1, 0);
		this.add(lblUserName, 0, 1);
		this.add(txtUsername, 1, 1);
		this.add(lblSenha, 0, 2);
		this.add(txtSenha, 1, 2);
		
		this.add(webView, 2, 0, 1, 6);
		webView.setPrefWidth(600);		
		
		HBox hbox = new HBox(5, btoConectar, btoSair, mensagemUsuario);
		this.add(hbox, 0, 3, 3, 1);
		
		//metdo auxiliar que configura a tabela inbox
		this.add(setUpTabelaInbox(), 0, 4, 2, 1);				
		this.add(txtConteudoMensagem, 0, 5, 2, 1);
		
		//toda vez que uma linha da tabela é selecionada, chama o método selecionarMensagem
		tabelaInbox.getSelectionModel().selectedItemProperty().addListener(gerenciadorDeEventos::selecionarMensagem);
		
		//registra um evento para o botão conectar
		btoConectar.setOnAction(gerenciadorDeEventos::conectar);
		
		//gerente de evento para o botão sair
		btoSair.setOnAction(gerenciadorDeEventos::sair);
		
		scene = new Scene(this);		
	}

	//metodo auxiliar para criar a tabela
	private Node setUpTabelaInbox() {
		// TODO Auto-generated method stub
		//list que contem os itens da tabela
		listMensagens = FXCollections.observableArrayList();
		tabelaInbox = new TableView<>(listMensagens);
		tabelaInbox.setPlaceholder(new Label("Inbox Vazia."));
		
		//coluna remetente
		TableColumn<Message, Message> colunaDe = new TableColumn<>("autor");
		colunaDe.setPrefWidth(400);
		colunaDe.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		colunaDe.setCellFactory(coluna -> new TableCell<Message, Message>(){
			//chamado automaticamente quando a tabela é construida
			@Override
			protected void updateItem(Message mensagem, boolean empty) {
				super.updateItem(mensagem, empty);
				//se tiver mensagem nesta linha, extrai o campo rementente e coloca nesta célula
				if(mensagem != null)						
					setText(getCampoDaMensagem(mensagem, CamposMensagem.REMETENTE));							
			}
		});				
		
		//coluna assunto da mensagem, mesma lógica da coluna remetente...
		TableColumn<Message, Message> colunaAssunto = new TableColumn<>("assunto");
		colunaAssunto.setPrefWidth(200);
		colunaAssunto.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		colunaAssunto.setCellFactory(coluna -> new TableCell<Message, Message>(){
			
			protected void updateItem(Message mensagem, boolean empty) {
				super.updateItem(mensagem, empty);				
				if(mensagem != null)						
					setText(getCampoDaMensagem(mensagem, CamposMensagem.ASSUNTO));
			}
		});
		
		//coluna data quando a mensagem foi enviada...
		TableColumn<Message, Message> colunaData = new TableColumn<>("data");
		colunaData.setPrefWidth(100);
		colunaData.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		colunaData.setCellFactory(coluna -> new TableCell<Message, Message>(){
			
			protected void updateItem(Message mensagem, boolean empty) {
				super.updateItem(mensagem, empty);				
				if(mensagem != null)						
					setText(getCampoDaMensagem(mensagem, CamposMensagem.DATA));
			}
		});
		
		//adiciona as colunas na tabela e retorna a tabela
		tabelaInbox.getColumns().addAll(colunaDe, colunaAssunto, colunaData);
		tabelaInbox.setPrefHeight(200);			
		
		return tabelaInbox;
	}
	
	//método auxiliar que extrai uma parte da mensagem e retorna na forma de String
	String getCampoDaMensagem(Message message, CamposMensagem campo) {
		
		try {
			if(campo == CamposMensagem.REMETENTE)
				return InternetAddress.toString(message.getFrom());
			
			else if(campo == CamposMensagem.ASSUNTO)
				return message.getSubject();
			
			else {
				return new SimpleDateFormat("dd/MM/yyyy").format(message.getSentDate());				
			}										
		}
		catch (MessagingException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	//mensagens ao usuário
	void setMensagemDeSucesso(String msg){
		mensagemUsuario.setFill(Color.GREEN);
		mensagemUsuario.setText(msg);
	}
	
	void setMensagemDeErro(String msg){
		mensagemUsuario.setFill(Color.RED);
		mensagemUsuario.setText(msg);
	}

}
