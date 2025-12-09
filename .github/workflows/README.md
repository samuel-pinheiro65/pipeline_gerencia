# GitHub Actions - CI/CD Pipeline

Este diretório contém o pipeline de CI/CD configurado para o projeto Pipeline de Gerência.

## 📋 Arquivos

- `ci.yml` - Pipeline principal de CI/CD

## 🚀 Como Funciona

O pipeline é disparado automaticamente quando:
- Há commits na branch `main` ou `master`
- Há pull requests para `main` ou `master`

## 📊 Etapas do Pipeline

### 1. Etapa de Commit (build-and-test)
- ✅ Checkout do código
- ✅ Configuração do Java 21
- ✅ Compilação da aplicação
- ✅ Execução de testes unitários
- ✅ Execução de testes de integração
- ✅ Publicação de relatórios de teste

### 2. Etapa de Teste de Aceitação (acceptance-test)
- ✅ Execução de testes de aceitação
- ✅ Validação funcional e não funcional
- ✅ Publicação de relatórios

### 3. Etapa de Lançamento (deploy)
- ✅ Build final da aplicação
- ✅ Geração do artefato JAR
- ✅ Construção da imagem Docker
- ✅ Publicação do artefato

## 🔍 Visualizar Execuções

1. Acesse a aba "Actions" no GitHub
2. Selecione a execução desejada
3. Veja os logs de cada etapa

## 🛠️ Executar Localmente

Para testar o pipeline localmente antes de fazer commit:

```bash
# Instalar act (ferramenta para executar GitHub Actions localmente)
# macOS: brew install act
# Linux: https://github.com/nektos/act

# Executar o pipeline
act push
```

## 📝 Notas

- O pipeline usa Java 21
- Todos os testes devem passar para o deploy ser executado
- O deploy só é executado na branch main/master

