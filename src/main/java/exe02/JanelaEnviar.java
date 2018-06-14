package exe02;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

//classe de interface gráfica com o usuário (GUI), estende GridPane
public class JanelaEnviar extends GridPane{

	//copntroles utilizados
	TextField txtHostServer;
	TextField txtPara;
	TextField txtDe;
	TextField txtAssunto;
	TextField txtUsername;	
	PasswordField txtSenha;
	TextArea txtConteudoMensagem;
	Button btoEnviar;
	Label lblHostServer;
	Label lblPara;
	Label lblDe;
	Label lblAssunto;
	Label lblUserName;
	Label lblSenha;
	Text mensagem;
	
	//objeto Scene que abriga esta GUI
	Scene scene;
	
	//inicia e configura os controles no construtor 
	public JanelaEnviar(EventHandler<ActionEvent> eventControler) {
		// TODO Auto-generated constructor stub
		
		//configura o GridPane
		this.setAlignment(Pos.CENTER);
		
		//espaços e margens em pixels entre os controles
		this.setVgap(5);
		this.setHgap(5);
		this.setPadding(new Insets(10));
		
		
		txtHostServer = new TextField("smtp.gmail.com");
		txtPara = new TextField();
		txtDe = new TextField();
		txtAssunto = new TextField();
		txtUsername = new TextField();
		txtSenha = new PasswordField();
		txtConteudoMensagem = new TextArea("conteúdo...");
		
		btoEnviar = new Button("Enviar");
		//associa o controlador de eventos do botão enviar
		btoEnviar.setOnAction(eventControler);
		
		lblHostServer = new Label("SMTP Server:");
		lblPara = new Label("Para:");
		lblDe = new Label("De:");
		lblAssunto = new Label("Assunto:");
		lblUserName = new Label("Login:");
		lblSenha = new Label("Senha:");
		mensagem = new Text();
		mensagem.setFill(Color.RED);
				
		//adiciona os controles neste GridPane
		// informando na ordem: controle, coluna e linha
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
		
		//o controle conteudo ocupa mais espaço, ele ocupará duas colunas
		this.add(txtConteudoMensagem, 0, 6, 2, 1);
		
		//VBox é outro container que agrupa os controles verticalmente
		//adiconamos o botão enviar e o campo conteudo em VBox... 
		VBox vBox = new VBox(5, btoEnviar, mensagem);
		vBox.setAlignment(Pos.CENTER);		
		
		//...depois adicionamos VBox no Grid
		this.add(vBox, 0, 7, 2, 1);
				
		scene = new Scene(this);
	}		
}
