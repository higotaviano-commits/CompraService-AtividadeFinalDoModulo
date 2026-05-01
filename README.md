

Foram selecionados métodos da camada de serviço por concentrar os principais métodos da regra de negócio.

Classes testadas

- AdicionarProdutosService
- UpdateValorTotalCompraService
- ModificarStatusCompraService
- RealizarCompraService

Justificativa

- Essas classes concentram os principais cenários de uso da aplicação.
- Devido a importância nos casos de uso.

Cenário Testados

AdicionarProdutoService

- Criar uma nova compra
- Adicionar item a uma compra existente
- Lançar exceção quando compra não existe
- Lançar exceção quando estoque é insuficiente

UpdateValorTotalCompraService

- Calcular corretamente o valor total da compra
- Atualizar o valor total na entidade
- Salvar a compra no repositório
- Cenário com lista de itens vazia

ModificarStatusCompraService

- Atualizar status da compra
- Persistir alteração no banco

RealizarCompraService

- Realizar compra
- Lançar exceção se não encontrar a compra



Evidência da execução dos testes


Resultado do relatório Jacoco

| Métrica                | Valor        |
|------------------------|-------------|
| Cobertura Total        | 92%         |
| Branch Coverage        | 100%        |
| Instruções Perdidas    | 24 de 307   |
| Complexidade (Cxty)    | 17          |
| Linhas Perdidas        | 9           |
| Métodos Perdidos       | 1           |
| Classes Perdidas       | 0           |

| Classe                          | Cobertura | Branch | Linhas Perdidas | Métodos Perdidos |
|---------------------------------|-----------|--------|-----------------|------------------|
| NotificarClienteService         | 14%       | n/a    | 9               | 1                |
| AdicionarProdutosService        | 100%      | 100%   | 0               | 0                |
| RealizarCompraService           | 100%      | n/a    | 0               | 0                |
| UpdateValorTotalCompraService   | 100%      | n/a    | 0               | 0                |
| ModificarStatusCompraService    | 100%      | n/a    | 0               | 0                |


