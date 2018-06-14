package exe04;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.web.HTMLEditor;

//interface gráfica com o usuário GUI
public class JanelaEnviar extends GridPane{

	//controles da GUI
	TextField txtHostServer;
	TextField txtPara;
	TextField txtDe;
	TextField txtAssunto;
	TextField txtUsername;	
	PasswordField txtSenha;	
	Button btoEnviar;
	Label lblHostServer;
	Label lblPara;
	Label lblDe;
	Label lblAssunto;
	Label lblUserName;
	Label lblSenha;
	Text mensagem;	
	
	//widget do javafx que gera tags html
	HTMLEditor htmlEditor;
	
	Scene scene;
	
	public JanelaEnviar(EventHandler<ActionEvent> eventControler) {
		// TODO Auto-generated constructor stub
		
		this.setAlignment(Pos.CENTER);
		this.setVgap(5);
		this.setHgap(5);
		this.setPadding(new Insets(10));
		
		txtHostServer = new TextField("smtp.gmail.com");
		txtPara = new TextField();
		txtDe = new TextField();
		txtAssunto = new TextField();
		txtUsername = new TextField();
		txtSenha = new PasswordField();				
		
		btoEnviar = new Button("Enviar");
		btoEnviar.setOnAction(eventControler);
		
		lblHostServer = new Label("SMTP Server:");
		lblPara = new Label("Para:");
		lblDe = new Label("De:");
		lblAssunto = new Label("Assunto:");
		lblUserName = new Label("Login:");
		lblSenha = new Label("Senha:");
		mensagem = new Text();		
		htmlEditor = new HTMLEditor();
		htmlEditor.setPrefHeight(430);
		
		//node, col, row
		this.add(lblHostServer, 0, 0);
		this.add(txtHostServer, 1, 0);
		this.add(lblPara, 0, 1);
		this.add(txtPara, 1, 1);
		this.add(lblDe, 0, 2);
		this.add(txtDe, 1, 2);
		this.add(lblAssunto, 0, 3);
		this.add(txtAssunto, 1, 3);
		this.add(lblUserName, 0, 4);
		this.add(txtUsername, 1, 4);
		this.add(lblSenha, 0, 5);
		this.add(txtSenha, 1, 5);	
		
		this.add(htmlEditor, 0, 6, 2, 1);			
		
		VBox vBox = new VBox(5, btoEnviar, mensagem);
		vBox.setAlignment(Pos.CENTER);		
		this.add(vBox, 0, 7, 2, 1);
				
		scene = new Scene(this);
	}	
	
	void setMensagemDeSucesso(String msg){
		mensagem.setFill(Color.GREEN);
		mensagem.setText(msg);
	}
	
	void setMensagemDeErro(String msg){
		mensagem.setFill(Color.RED);
		mensagem.setText(msg);
	}
}





