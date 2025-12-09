# Estrutura de Testes - Pipeline de Gerência

## 📋 Visão Geral

Este documento descreve a estrutura completa de testes implementada para o sistema de gerenciamento de tarefas, atendendo aos requisitos do trabalho prático de Gerenciamento de Configuração e Evolução de Software.

## 🗂️ Organização dos Testes

### 1. Testes Unitários (`src/test/java/com/example/pipeline_gerencia/unit/`)

Testes unitários isolados que testam componentes individuais usando mocks quando necessário.

#### Arquivos:
- **TaskTest.java** - Testes da entidade Task
  - Criação de tarefas
  - Valores padrão
  - Validação de porcentagem de conclusão
  - Detecção de tarefas atrasadas
  - Getters e setters

- **UserTest.java** - Testes da entidade User
  - Criação de usuários
  - Valores padrão
  - Getters e setters

- **CategoryTest.java** - Testes da entidade Category
  - Criação de categorias
  - Getters e setters

- **TaskValidatorTest.java** - Testes do validador de tarefas
  - Validação de tarefas válidas
  - Validação de título (tamanho mínimo e máximo)
  - Validação de descrição
  - Validação de porcentagem de conclusão
  - Tratamento de valores nulos

- **DateUtilsTest.java** - Testes das utilidades de data
  - Formatação de data/hora
  - Cálculo de dias até o prazo
  - Detecção de atraso
  - Adição de dias/horas

- **TaskServiceUnitTest.java** - Testes unitários do TaskService usando mocks
  - Criação de tarefas
  - Validação de entrada
  - Busca de tarefas
  - Atualização de status e prioridade
  - Cálculo de estatísticas

- **UserServiceUnitTest.java** - Testes unitários do UserService usando mocks
  - Criação de usuários
  - Validação de email único
  - Busca de usuários
  - Atualização e desativação

- **SearchFilterTest.java** - Testes do filtro de busca
  - Configuração de filtros
  - Combinação de critérios

**Total: 8 classes de teste unitário**

### 2. Testes de Integração (`src/test/java/com/example/pipeline_gerencia/integration/`)

Testes que verificam a integração entre componentes usando repositórios reais.

#### Arquivos:
- **TaskServiceIntegrationTest.java** - Testes de integração do TaskService
  - Criação e recuperação de tarefas
  - Fluxo completo de atualização de status
  - Busca com múltiplos critérios
  - Detecção de tarefas atrasadas
  - Estatísticas de conclusão
  - Tarefas vencendo em breve
  - Exclusão de tarefas

- **UserServiceIntegrationTest.java** - Testes de integração do UserService
  - Criação e recuperação de usuários
  - Prevenção de email duplicado
  - Busca por email e departamento
  - Gerenciamento de usuários ativos
  - Atualização e exclusão

- **RepositoryIntegrationTest.java** - Testes de integração dos repositórios
  - Operações CRUD completas
  - Busca por diferentes critérios
  - Integração entre repositórios

**Total: 3 classes de teste de integração**

### 3. Testes de Aceitação (`src/test/java/com/example/pipeline_gerencia/acceptance/`)

Testes end-to-end que validam cenários completos de uso do sistema.

#### Arquivos:
- **TaskManagerAcceptanceTest.java** - Teste de aceitação principal
  - **Cenário 1**: Fluxo completo de gerenciamento de tarefas
    - Configuração inicial (criação de equipe)
    - Criação de tarefas
    - Início do trabalho
    - Atualização de progresso
    - Conclusão de tarefas
    - Busca e filtros
    - Monitoramento de prazos
    - Estatísticas do projeto
    - Gerenciamento de usuários
    - Validação final de integridade
  
  - **Cenário 2**: Validação e tratamento de erros
    - Validação de dados inválidos
    - Prevenção de emails duplicados
    - Tratamento de tarefas inexistentes

**Total: 1 classe de teste de aceitação**

## 📊 Estatísticas de Testes

- **Testes Unitários**: 8 classes, ~50+ métodos de teste
- **Testes de Integração**: 3 classes, ~20+ métodos de teste
- **Testes de Aceitação**: 1 classe, 2 cenários completos
- **Total**: 12 classes de teste, 70+ métodos de teste

## 🛠️ Tecnologias Utilizadas

- **JUnit 5** - Framework de testes
- **Mockito** - Framework de mocking para testes unitários
- **Gradle** - Build tool e execução de testes

## ✅ Cobertura de Testes

### Funcionalidades Testadas:

#### Modelos (Entities)
- ✅ Task (criação, validação, estados, atrasos)
- ✅ User (criação, validação, departamentos)
- ✅ Category (criação, atributos)

#### Serviços
- ✅ TaskService (CRUD, busca, estatísticas, validação)
- ✅ UserService (CRUD, validação de email, departamentos)

#### Repositórios
- ✅ TaskRepository (todas as operações)
- ✅ UserRepository (todas as operações)
- ✅ CategoryRepository (todas as operações)

#### Utilitários
- ✅ TaskValidator (validações completas)
- ✅ DateUtils (formatação, cálculos, detecção)
- ✅ SearchFilter (filtros e combinações)

## 🚀 Executando os Testes

### Executar todos os testes:
```bash
./gradlew test
```

### Executar apenas testes unitários:
```bash
./gradlew test --tests "*unit*"
```

### Executar apenas testes de integração:
```bash
./gradlew test --tests "*integration*"
```

### Executar apenas testes de aceitação:
```bash
./gradlew test --tests "*acceptance*"
```

### Ver relatório de testes:
```bash
# Após executar ./gradlew test
open build/reports/tests/test/index.html
```

## 📝 Notas Importantes

1. **Testes Unitários**: Usam mocks para isolar componentes
2. **Testes de Integração**: Usam repositórios reais (in-memory)
3. **Testes de Aceitação**: Validam fluxos completos end-to-end
4. **Todos os testes são independentes**: Cada teste pode ser executado isoladamente
5. **Setup/Teardown**: Usa `@BeforeEach` para garantir estado limpo

## ✨ Requisitos Atendidos

✅ **Testes Unitários**: Implementados com JUnit 5 e Mockito  
✅ **Testes de Integração**: Separados e implementados  
✅ **Teste de Aceitação**: Implementado com cenários completos  
✅ **Automação**: Todos os testes podem ser executados via Gradle  
✅ **Cobertura**: Todas as classes principais possuem testes  

## 🔄 Próximos Passos

Para o pipeline CI/CD, estes testes serão executados automaticamente em:
1. **Etapa de Commit**: Testes unitários e de integração
2. **Etapa de Teste de Aceitação**: Teste de aceitação automatizado
3. **Relatórios**: Geração automática de relatórios de cobertura

