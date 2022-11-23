## Como testar

### Rode a aplicação pelo Shell
 ``` shell
 $ ./mvnw spring-boot:run
 ```
Ela vai estar disponível em localhost:8080

### Postman

Uma vez que a aplicação está rodando, você pode importar a coleção da API no Postman:
[`./doc/projeto-integrador.postman_collection.json`](./doc/projeto-integrador.postman_collection.json)

### Swagger

Com a aplicação rodando, acesse `localhost:8080/swagger-ui/index.html` para consultar a documentação da API

### US06 - CRUD do vendedor

Foi desenvolvido o gerenciamento do vendedor, cotemplando a criação, atualização, deleção e consulta do registro de vendedor.

Para isso existem quatro rotas:
 
- Um POST para a criação de um vendedor, recebendo nome, sobrenome, email, endereço, número da casa e CEP.
- Um PATCH para atualizar o registro de um vendedor, podendo ou não receber nome, sobrenome, email, endereço, número da casa e CEP.
- Um DELETE que torna o vendedor inativo, para fins de histórico e criação de soluções, os dados não são removidos do banco de dados.
- Um GET para consultar os dados de um vendedor.

Também foi desenvolvido uma solução para os operadores dos armazéns consultarem os lotes de vendedores inativos.

Para isso foi criado uma rota:

- Um GET que consulta os lotes de vendedores inativos naquele armazém.