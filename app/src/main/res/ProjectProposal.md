### Proposta de Projeto: Aplicativo de Inventário de Estoque

### 1\. Objetivo do Projeto

O objetivo principal deste projeto é desenvolver uma aplicação móvel para **Android** que digitalize e otimize o processo de contagem de inventário. 
O aplicativo deve permitir a leitura rápida de códigos de barras de múltiplas bobinas de aço, armazenar os dados de forma organizada e segura no dispositivo e, por fim, exportar essas informações para um arquivo no formato **CSV ou Excel**, eliminando a necessidade de anotações manuais e reduzindo erros de transcrição. 
O resultado final deve ser um arquivo digital pronto para ser importado ou comparado com o sistema ERP da empresa.


-----

### 2\. Funcionalidades do Sistema

O aplicativo será composto por três funcionalidades principais que cobrem todo o fluxo de trabalho do usuário.

#### **Funcionalidade 1: Leitura e Captura de Dados**

Esta é a função central do aplicativo. Ela permite ao usuário escanear os códigos de barra dos itens.

- **Acesso à Câmera:** O sistema deve solicitar e obter permissão para usar a câmera do dispositivo.
- **Leitura de Código de Barras:** Utilizando a câmera, o aplicativo deve decodificar rapidamente códigos de barras (ex: EAN-13, CODE-39).
- **Entrada de Quantidade:** Após a leitura do código, o aplicativo deve exibir uma interface simples para que o usuário confirme ou altere a quantidade do item (padrão 1).
- **Adição de Item:** O item escaneado, junto com sua quantidade, deve ser adicionado a uma lista de inventário local.

#### **Funcionalidade 2: Gestão do Inventário Local**

Esta funcionalidade permite gerenciar a lista de itens que já foram escaneados.

- **Visualização da Lista:** O usuário deve ser capaz de ver uma lista de todos os itens já adicionados, com o código do produto e a quantidade.
- **Edição de Itens:** O usuário deve ser capaz de editar a quantidade de um item já adicionado na lista.
- **Remoção de Itens:** O usuário deve ser capaz de remover um ou mais itens da lista.
- **Limpeza da Lista:** Deve existir uma opção para limpar toda a lista, iniciando uma nova contagem.

#### **Funcionalidade 3: Exportação e Compartilhamento de Dados**

Esta funcionalidade permite que o usuário salve e compartilhe o resultado da contagem.

- **Geração de Arquivo CSV:** O aplicativo deve gerar um arquivo no formato `.csv` contendo as informações da lista de inventário.
- **Formato de Dados:** O arquivo `.csv` deve ter colunas claras, como **Código do Produto**, **Quantidade** e **Data/Hora da Contagem**.
- **Compartilhamento do Arquivo:** O aplicativo deve integrar-se com o sistema operacional para permitir o compartilhamento do arquivo gerado via e-mail, Google Drive, Bluetooth ou qualquer outro aplicativo instalado.

-----

### 3\. Critérios de Aceitação

Para cada funcionalidade, os seguintes critérios devem ser atendidos para que a entrega seja considerada um sucesso.

| Funcionalidade | Critérios de Aceitação |
| :--- | :--- |
| **Leitura e Captura** | O aplicativo deve iniciar a câmera e o leitor de código de barras em menos de 3 segundos. \<br\> O leitor de código de barras deve decodificar códigos válidos em ambientes com iluminação normal. \<br\> O valor do código de barras deve ser exibido na tela após a leitura. \<br\> Ao adicionar um item, ele deve aparecer na lista de inventário com a quantidade padrão de `1`. |
| **Gestão do Inventário** | A lista de itens deve ser atualizada em tempo real a cada novo escaneamento. \<br\> O usuário deve ser capaz de alterar a quantidade de um item para um número inteiro positivo. \<br\> A função de remoção deve excluir o item selecionado permanentemente da lista. \<br\> A função de "Limpar Lista" deve esvaziar a lista de inventário completamente. |
| **Exportação e Compartilhamento** | O aplicativo deve gerar um arquivo `.csv` no armazenamento do dispositivo. \<br\> O arquivo deve conter um cabeçalho com as colunas **Código**, **Quantidade** e **Timestamp** (data e hora). \<br\> O arquivo deve conter uma linha para cada item da lista de inventário. \<br\> A opção de compartilhamento deve exibir os aplicativos de compartilhamento padrão do sistema Android. |

-----

### 4\. Cenários de Teste

Os seguintes cenários de teste devem ser executados para garantir que o aplicativo funcione como esperado, cobrindo tanto o "caminho feliz" quanto possíveis erros.

#### **Cenário de Teste 1: Leitura de Código de Barras**

* **Caso Positivo:** Escanear um código de barras de uma bobina sob iluminação normal.
    * **Resultado Esperado:** O aplicativo deve ler o código, exibir o valor e adicionar o item à lista.
* **Caso Negativo:** Escanear um código de barras danificado, borrado ou sob pouca iluminação.
    * **Resultado Esperado:** O aplicativo não deve ler o código ou deve demorar mais para fazer a leitura. O usuário deve ser capaz de inserir o código manualmente.
* **Caso Negativo:** Tentar escanear uma área sem código de barras.
    * **Resultado Esperado:** O aplicativo não deve adicionar nenhum item à lista.

#### **Cenário de Teste 2: Gestão do Inventário Local**

* **Caso Positivo:** Adicionar 5 itens diferentes e depois editar a quantidade do segundo item para `2`.
    * **Resultado Esperado:** A quantidade do segundo item na lista deve ser atualizada para `2`.
* **Caso Positivo:** Adicionar 3 itens, remover o segundo da lista e, depois, usar a função "Limpar Lista".
    * **Resultado Esperado:** O segundo item deve ser removido, e a lista completa deve ser esvaziada.
* **Caso Negativo:** Tentar inserir um valor negativo ou texto (`-1`, `"cinco"`) no campo de quantidade.
    * **Resultado Esperado:** O aplicativo deve exibir uma mensagem de erro ou impedir a inserção de valores inválidos.

#### **Cenário de Teste 3: Exportação e Compartilhamento de Dados**

* **Caso Positivo:** Escanear 3 itens, ir para a tela de exportação e clicar em "Exportar".
    * **Resultado Esperado:** Um arquivo `.csv` deve ser gerado no dispositivo, contendo as 3 bobinas com seus códigos, quantidades e timestamp.
* **Caso Negativo:** Clicar em "Exportar" quando a lista de inventário está vazia.
    * **Resultado Esperado:** O aplicativo deve exibir uma mensagem informando que a lista está vazia e que não há dados para exportar.