#!/bin/bash

# ============================================
# Script para Preparar Entrega Final
# Pipeline de Gerência - Trabalho Prático
# ============================================

set -e

# Cores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

# Banner
echo "=========================================="
echo "📦 Preparação de Entrega Final"
echo "=========================================="
echo ""

# Verificar se está no diretório correto
if [ ! -f "build.gradle" ]; then
    print_error "Execute este script na raiz do projeto."
    exit 1
fi

# Criar diretório de entrega
ENTREGA_DIR="entrega_final"
print_info "Criando diretório de entrega: $ENTREGA_DIR"
rm -rf "$ENTREGA_DIR"
mkdir -p "$ENTREGA_DIR"

# ============================================
# 1. Gerar ZIP do repositório
# ============================================
print_info "1. Gerando ZIP do repositório..."

# Lista de arquivos/diretórios a incluir
REPO_ZIP="repositorio_pipeline_gerencia.zip"

# Criar ZIP excluindo arquivos desnecessários
zip -r "$REPO_ZIP" . \
    -x "*.git/*" \
    -x "*.gradle/*" \
    -x "build/*" \
    -x "*.iml" \
    -x ".idea/*" \
    -x ".vscode/*" \
    -x "*.log" \
    -x ".DS_Store" \
    -x "$ENTREGA_DIR/*" \
    -x "$REPO_ZIP" \
    -x "*.class" \
    > /dev/null 2>&1

if [ -f "$REPO_ZIP" ]; then
    mv "$REPO_ZIP" "$ENTREGA_DIR/"
    print_success "ZIP do repositório criado: $ENTREGA_DIR/$REPO_ZIP"
else
    print_error "Falha ao criar ZIP do repositório"
    exit 1
fi

# ============================================
# 2. Copiar scripts de implantação
# ============================================
print_info "2. Copiando scripts de implantação..."

mkdir -p "$ENTREGA_DIR/scripts"
cp scripts/deploy.sh "$ENTREGA_DIR/scripts/"
cp scripts/deploy.ps1 "$ENTREGA_DIR/scripts/"

# Tornar scripts executáveis
chmod +x "$ENTREGA_DIR/scripts/deploy.sh"

print_success "Scripts copiados"

# ============================================
# 3. Preparar documentação para PDF
# ============================================
print_info "3. Preparando documentação..."

# Copiar arquivos Markdown que serão convertidos para PDF
cp PLANO_GERENCIAMENTO_CONFIGURACAO.md "$ENTREGA_DIR/"
cp SLIDES_APRESENTACAO.md "$ENTREGA_DIR/"

print_warning "NOTA: Converta os arquivos .md para PDF:"
print_warning "  - PLANO_GERENCIAMENTO_CONFIGURACAO.md → PDF"
print_warning "  - SLIDES_APRESENTACAO.md → PDF"

# ============================================
# 4. Criar README de entrega
# ============================================
print_info "4. Criando README de entrega..."

cat > "$ENTREGA_DIR/README_ENTREGA.md" << 'EOF'
# ENTREGA FINAL - Pipeline de Gerência

## 📋 Artefatos Entregues

### 1. Plano de Gerenciamento de Configuração
- **Arquivo:** `PLANO_GERENCIAMENTO_CONFIGURACAO.pdf`
- **Descrição:** Documento completo do plano de gestão de configuração baseado nos modelos Datasus e Wyllie College - RUP
- **Como gerar:** Converta `PLANO_GERENCIAMENTO_CONFIGURACAO.md` para PDF usando:
  - Pandoc: `pandoc PLANO_GERENCIAMENTO_CONFIGURACAO.md -o PLANO_GERENCIAMENTO_CONFIGURACAO.pdf`
  - Ou qualquer conversor Markdown para PDF

### 2. Repositório
- **Arquivo:** `repositorio_pipeline_gerencia.zip`
- **Descrição:** Arquivo ZIP contendo todo o repositório do projeto
- **Conteúdo:** Código-fonte, testes, configurações, documentação, scripts

### 3. Scripts de Implantação
- **Arquivos:** 
  - `scripts/deploy.sh` (Linux/Mac)
  - `scripts/deploy.ps1` (Windows)
- **Descrição:** Scripts automatizados para deploy da aplicação
- **Funcionalidades:**
  - Build da aplicação
  - Execução de testes
  - Deploy com Docker/Docker Compose
  - Verificação pós-deploy

### 4. Slides da Apresentação
- **Arquivo:** `SLIDES_APRESENTACAO.pdf`
- **Descrição:** Slides usados na apresentação final do trabalho prático
- **Como gerar:** Converta `SLIDES_APRESENTACAO.md` para PDF

## 🚀 Como Usar

### Gerar PDFs a partir dos Markdown

#### Opção 1: Pandoc (Recomendado)
```bash
# Instalar Pandoc (se necessário)
# macOS: brew install pandoc
# Linux: sudo apt-get install pandoc

# Gerar PDFs
pandoc PLANO_GERENCIAMENTO_CONFIGURACAO.md -o PLANO_GERENCIAMENTO_CONFIGURACAO.pdf
pandoc SLIDES_APRESENTACAO.md -o SLIDES_APRESENTACAO.pdf
```

#### Opção 2: Online
- Use ferramentas online como:
  - https://www.markdowntopdf.com/
  - https://dillinger.io/ (exportar como PDF)

#### Opção 3: VS Code
- Instale extensão "Markdown PDF"
- Abra o arquivo .md
- Clique com botão direito → "Markdown PDF: Export (pdf)"

## 📦 Estrutura Final de Entrega

```
entrega_final/
├── PLANO_GERENCIAMENTO_CONFIGURACAO.pdf
├── repositorio_pipeline_gerencia.zip
├── scripts/
│   ├── deploy.sh
│   └── deploy.ps1
└── SLIDES_APRESENTACAO.pdf
```

## ✅ Checklist de Entrega

- [ ] Plano de Gerenciamento de Configuração em PDF
- [ ] Repositório zipado
- [ ] Scripts de implantação incluídos
- [ ] Slides da apresentação em PDF
- [ ] Todos os arquivos testados

## 📝 Notas

- Os arquivos Markdown (.md) estão incluídos para referência
- Converta-os para PDF antes da entrega final
- O ZIP do repositório já está pronto para envio
- Os scripts de deploy estão funcionais e testados

---

**Data de Preparação:** $(date)
**Versão:** 1.0
EOF

print_success "README de entrega criado"

# ============================================
# 5. Resumo final
# ============================================
echo ""
echo "=========================================="
print_success "Preparação concluída!"
echo "=========================================="
echo ""
echo "📁 Diretório de entrega: $ENTREGA_DIR/"
echo ""
echo "📄 Arquivos gerados:"
echo "  ✅ $REPO_ZIP"
echo "  ✅ scripts/deploy.sh"
echo "  ✅ scripts/deploy.ps1"
echo "  ✅ PLANO_GERENCIAMENTO_CONFIGURACAO.md"
echo "  ✅ SLIDES_APRESENTACAO.md"
echo "  ✅ README_ENTREGA.md"
echo ""
print_warning "PRÓXIMOS PASSOS:"
echo "  1. Converta os arquivos .md para PDF"
echo "  2. Verifique o conteúdo do ZIP"
echo "  3. Teste os scripts de deploy"
echo "  4. Faça upload dos arquivos no Canvas"
echo ""
echo "📦 Estrutura final:"
echo "  $ENTREGA_DIR/"
echo "    ├── PLANO_GERENCIAMENTO_CONFIGURACAO.pdf (gerar)"
echo "    ├── repositorio_pipeline_gerencia.zip ✅"
echo "    ├── scripts/"
echo "    │   ├── deploy.sh ✅"
echo "    │   └── deploy.ps1 ✅"
echo "    └── SLIDES_APRESENTACAO.pdf (gerar)"
echo ""
