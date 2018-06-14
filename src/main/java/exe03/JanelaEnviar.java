package exe03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

//interface gráfica com o usuário GUI
public class JanelaEnviar extends GridPane {

	//controles da GUI 
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

	//tabela de arquivos anexados
	TableView<ArquivoModel> tabelaAnexos;
	
	//lista de arquivos exibidos pela tabela
	ObservableList<ArquivoModel> listAnexos;

	//Scene da Janela
	Scene scene;

	//inicializando e configurando os controles no construtor
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
		txtConteudoMensagem = new TextArea();

		btoEnviar = new Button("Enviar");
		btoEnviar.setOnAction(eventControler);

		lblHostServer = new Label("SMTP Server:");
		lblPara = new Label("Para:");
		lblDe = new Label("De:");
		lblAssunto = new Label("Assunto:");
		lblUserName = new Label("Login:");
		lblSenha = new Label("Senha:");
		mensagem = new Text();

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

		//método auxiliar para configurar a tabela
		this.add(setUpTabelaAnexos(), 0, 6, 2, 1);

		this.add(txtConteudoMensagem, 0, 7, 2, 1);

		//VBox é outro container que agrupa os controles verticalmente
		//adiconamos o botão enviar e o campo conteudo em VBox...
		VBox vBox = new VBox(5, btoEnviar, mensagem);
		vBox.setAlignment(Pos.CENTER);
		this.add(vBox, 0, 8, 2, 1);

		//inicializa a Scena deste Grid
		scene = new Scene(this);
	}

	//método auxiliar de configuraçã da tabela
	private Node setUpTabelaAnexos() {

		listAnexos = FXCollections.observableArrayList();
		
		//vincula a list na tabela
		tabelaAnexos = new TableView<>(listAnexos);
		
		//mensagem quando a tabela estiver vazia
		tabelaAnexos.setPlaceholder(new Label("Nenhum Arquivo Anexado"));

		//a tabela tera 2 colunas: nome do arquivo e outra coluna com um botão em cada linha para excluir o anexo, 
		//caso o usuário mude de ideia 
		
		//configurando a coluna nome do arquivo
		TableColumn<ArquivoModel, String> colNomeArquivo = new TableColumn<>("Tabela de Anexos");
		
		//especifica o tipo de dado da coluna e em qual campo do objeto ele está		
		colNomeArquivo.setCellValueFactory(new PropertyValueFactory<>("nomeArquivo"));
		colNomeArquivo.setPrefWidth(450);

		//coluna utilitária que possui um botão remover em cada linha
		TableColumn<ArquivoModel, ArquivoModel> colRemoveAnexo = new TableColumn<>();
		colRemoveAnexo.setPrefWidth(80);
		colRemoveAnexo.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		
		//configura o conteudo da coluna com um objeto TableCell personalizado
		colRemoveAnexo.setCellFactory(coluna -> new TableCell<ArquivoModel, ArquivoModel>() {

			final Button btoRemoveAnexo = new Button("X");

			//metodo updateItem é chamado automaticamente quando se constroi a tabela
			@Override
			protected void updateItem(ArquivoModel arquivoAnexo, boolean empty) {

				super.updateItem(arquivoAnexo, empty);
				
				//se a linha for vazia, não faz nada
				if (arquivoAnexo == null) {
					setGraphic(null);
					return;
				}
				//se tiver registro na linha, configura o botão
				setGraphic(btoRemoveAnexo);
				//toda vez quando o botão é clicado, o respectivo anexo é removido
				btoRemoveAnexo.setOnAction(event -> getTableView().getItems().remove(arquivoAnexo));
			};
		});
		
		//adiciona as coluna na tabela 
		tabelaAnexos.getColumns().addAll(colNomeArquivo, colRemoveAnexo);
		tabelaAnexos.setPrefHeight(150);

		//configurando o botão anexar
		Button btoAddAnexo = new Button("Anexar...");
		
		//registrando o evento de ação para quando o botão for acionado pelo usuário
		btoAddAnexo.setOnAction(event -> {
			try {
				//dialog para seleção de arquivos
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Escolher Anexo");
				
				//exibe a dialog de seleção de arquivos vinculada à JanelaEnviar
				//quando o usuário fecha a dialog de seleção ela retorna um objeto java.io.File
				Path anexo = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow()).toPath();

				//verifica se o anexo realmente existe
				if (Files.exists(anexo)) {

					//extrai o tipo de dado que o anexo contem
					String mimeType = Files.probeContentType(anexo);
					System.out.printf("mime type %s", mimeType);
					
					//converte o anexo em byte[]
					byte[] conteudo = Files.readAllBytes(anexo);
					
					//extrai o nome do anexo
					String nomeAnexo = anexo.getFileName().toString();
					
					//cria o objeto ArquivoModel, utilitario para manipular os anexos
					ArquivoModel novoAnexo = new ArquivoModel(nomeAnexo, conteudo, mimeType);
					
					//adiciona o anexo na lista vinculada à tabela
					listAnexos.add(novoAnexo);
				}
			}
			catch (NullPointerException | IOException e) {
				e.printStackTrace();
			}
		});
		
		//colaca a tabela e o botão addAnexo em uma VBox e retorna
		VBox vBoxTabelaAnexo = new VBox(5, tabelaAnexos, btoAddAnexo);

		return vBoxTabelaAnexo;
	}

	//retorna os anexos
	ArquivoModel[] getAnexos() {
		return listAnexos.toArray(new ArquivoModel[listAnexos.size()]);
	}

	//mensagens para o usuário
	void setMensagemDeSucesso(String msg) {
		mensagem.setFill(Color.GREEN);
		mensagem.setText(msg);
	}

	void setMensagemDeErro(String msg) {
		mensagem.setFill(Color.RED);
		mensagem.setText(msg);
	}
}
