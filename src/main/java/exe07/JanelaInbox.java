package exe07;

import java.text.SimpleDateFormat;
import java.util.Optional;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

//interface gráfica
public class JanelaInbox extends GridPane {

	TextField txtHostServer;
	TextField txtUsername;
	PasswordField txtSenha;

	Label lblHostServer;
	Label lblUserName;
	Label lblSenha;
	Text mensagemUsuario;

	Button btoConectar;
	Button btoSair;

	TableView<Message> tabelaInbox;
	ObservableList<Message> listMensagens;

	Scene scene;
	GerenciadorDeEventos gerenciadorDeEventos;
	
	enum CamposMensagem {REMETENTE, ASSUNTO, DATA}

	public JanelaInbox(GerenciadorDeEventos gerenciadorDeEventos) {

		this.gerenciadorDeEventos = gerenciadorDeEventos;
		this.gerenciadorDeEventos.janelaInbox = this;

		this.setAlignment(Pos.CENTER);
		this.setVgap(5);
		this.setHgap(5);
		this.setPadding(new Insets(10));

		txtHostServer = new TextField("imap.gmail.com");
		txtUsername = new TextField();
		txtSenha = new PasswordField();

		btoConectar = new Button("Conectar");
		btoConectar.setOnAction(gerenciadorDeEventos::conectar);
		
		btoSair = new Button(" Sair   ");
		btoSair.setOnAction(gerenciadorDeEventos::sair);

		lblHostServer = new Label("IMAP Server:");
		lblUserName = new Label("Login:");
		lblSenha = new Label("Senha:");
		mensagemUsuario = new Text();

		// node, col, row
		this.add(lblHostServer, 0, 0);
		this.add(txtHostServer, 1, 0);
		this.add(lblUserName, 0, 1);
		this.add(txtUsername, 1, 1);
		this.add(lblSenha, 0, 2);
		this.add(txtSenha, 1, 2);
		
		HBox hbox = new HBox(5, btoConectar, btoSair, mensagemUsuario);
		this.add(hbox, 0, 3, 3, 1);
		
		this.add(setUpTabelaInbox(), 0, 4, 2, 1);
		
		scene = new Scene(this);
	}

	private Node setUpTabelaInbox() {
		// TODO Auto-generated method stub
		listMensagens = FXCollections.observableArrayList();
		tabelaInbox = new TableView<>(listMensagens);
		tabelaInbox.setPlaceholder(new Label("Inbox Vazia."));
		
		TableColumn<Message, Message> colunaDe = new TableColumn<>("autor");
		colunaDe.setPrefWidth(400);
		colunaDe.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		colunaDe.setCellFactory(coluna -> new TableCell<Message, Message>(){
			@Override
			protected void updateItem(Message mensagem, boolean empty) {
				super.updateItem(mensagem, empty);
				
				if(mensagem != null)						
					setText(getCampoDaMensagem(mensagem, CamposMensagem.REMETENTE));							
			}
		});	
		
		TableColumn<Message, Message> colunaAssunto = new TableColumn<>("assunto");
		colunaAssunto.setPrefWidth(400);
		colunaAssunto.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		colunaAssunto.setCellFactory(coluna -> new TableCell<Message, Message>(){
			
			protected void updateItem(Message mensagem, boolean empty) {
				super.updateItem(mensagem, empty);				
				if(mensagem != null)						
					setText(getCampoDaMensagem(mensagem, CamposMensagem.ASSUNTO));
			}
		});
		
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
								
		TableColumn<Message, Message> colunaDeletar = new TableColumn<>();
		colunaDeletar.setPrefWidth(120);
		colunaDeletar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		colunaDeletar.setCellFactory(coluna -> new TableCell<Message, Message>(){
			
			final Button btoExcluir = new Button("excluir");
			
			protected void updateItem(Message message, boolean empty) {
				super.updateItem(message, empty);
				
				if(message == null) {
					setGraphic(null);
					return;
				}
				setGraphic(btoExcluir);
				btoExcluir.setOnAction(event -> gerenciadorDeEventos.excluir(event, message));
			}					
		});
		
		tabelaInbox.getColumns().addAll(colunaDe, colunaAssunto, colunaData, colunaDeletar);
		tabelaInbox.setPrefHeight(350);
		
		return tabelaInbox;
	}
	
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
	
	boolean confirmaExclusao() {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmação de Exclusão");
		alert.setContentText("Deseja Realmente Excluir esta mensagem?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if(result.isPresent())
			return result.get() == ButtonType.OK;
		
		return false;
	}
	
	void setMensagemDeSucesso(String msg){
		mensagemUsuario.setFill(Color.GREEN);
		mensagemUsuario.setText(msg);
	}
	
	void setMensagemDeErro(String msg){
		mensagemUsuario.setFill(Color.RED);
		mensagemUsuario.setText(msg);
	}


}
