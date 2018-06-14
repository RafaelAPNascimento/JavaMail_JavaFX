package exe01;

import javax.mail.PasswordAuthentication;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

//interface gráfica para autenticação
//estende GridPane
public class DialogAutenticacao extends GridPane {
	
	private PasswordAuthentication passwordAuthentication;
	 
	//controles desta interface gráfica:
	private Text titulo;
	private Label emailLabel;
	private Label senhaLabel;
	private TextField emailTxt;
	private PasswordField senhaText;
	private Button okButton;
	private Label mensagem;	
	
	//no construtor passa o gerenciador de ventos
	public DialogAutenticacao(EventHandler<ActionEvent> eventController) {
		super();
		// TODO Auto-generated constructor stub
		//instanciado e configurando cada controle...
		this.setAlignment(Pos.TOP_LEFT);
		this.setHgap(10);
		this.setVgap(10);
		this.setPadding(new Insets(25, 25, 25, 25));
		
		titulo = new Text("Informe seu endereço de email e senha:");
		titulo.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		this.add(titulo, 0, 0, 2, 1);
		
		emailLabel = new Label("Email:");
		this.add(emailLabel, 0, 1);
		
		emailTxt = new TextField("henriquemanoeleduardo@gmail.com");
		this.add(emailTxt, 1, 1);
		
		senhaLabel = new Label("Senha:");
		this.add(senhaLabel, 0, 2);
		
		senhaText = new PasswordField();
		this.add(senhaText, 1, 2);
		
		okButton = new Button("OK");
		okButton.setPrefWidth(100);
		okButton.setOnAction(eventController);			
		
		HBox hBox = new HBox(10);
		hBox.setAlignment(Pos.BOTTOM_LEFT);
		hBox.getChildren().add(okButton);
		this.add(hBox, 1, 4);
						
		mensagem = new Label();
		mensagem.setTextFill(Color.RED);
		this.add(mensagem, 1, 6);
		
		//no javafx toda interface deve estar associada a um Scene
		Scene scene = new Scene(this, 500, 275);
	}	
	
	/**
	 * retorna o objeto PasswordAuthetication com as credenciais fornecidas pelo usuário
	 * @return obj
	 */
	public PasswordAuthentication getPasswordAuthentication() {
		
		String senha = new String(senhaText.getText());
		String email = emailTxt.getText();
		senhaText.setText("");
		passwordAuthentication = new PasswordAuthentication(email, senha);
		return passwordAuthentication;
	}

	/**
	 * mensagem ao usuario
	 * @return
	 */
	public Label getMensagem() {
		return mensagem;
	}

}
