# ============================================
# Script de Deploy Automatizado (PowerShell)
# Pipeline de Gerência - Sistema de Gerenciamento de Tarefas
# ============================================

$ErrorActionPreference = "Stop"

# Cores para output
function Write-Info {
    Write-Host "ℹ️  $args" -ForegroundColor Blue
}

function Write-Success {
    Write-Host "✅ $args" -ForegroundColor Green
}

function Write-Warning {
    Write-Host "⚠️  $args" -ForegroundColor Yellow
}

function Write-Error {
    Write-Host "❌ $args" -ForegroundColor Red
}

# Banner
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "🚀 Script de Deploy - Pipeline de Gerência" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar se está no diretório correto
if (-not (Test-Path "build.gradle")) {
    Write-Error "build.gradle não encontrado. Execute este script na raiz do projeto."
    exit 1
}

# ============================================
# ETAPA 1: Verificações pré-deploy
# ============================================
Write-Info "Etapa 1: Verificações pré-deploy"

# Verificar Java
try {
    $javaVersion = java -version 2>&1 | Select-String "version" | ForEach-Object { $_.Line }
    Write-Success "Java encontrado: $javaVersion"
} catch {
    Write-Error "Java não encontrado. Instale Java 21 ou superior."
    exit 1
}

# Verificar Gradle
if (-not (Test-Path "./gradlew.bat")) {
    Write-Error "Gradle Wrapper não encontrado."
    exit 1
}
Write-Success "Gradle Wrapper configurado"

# ============================================
# ETAPA 2: Build da aplicação
# ============================================
Write-Info "Etapa 2: Build da aplicação"

Write-Info "Limpando build anterior..."
& ./gradlew.bat clean --no-daemon

Write-Info "Compilando aplicação..."
& ./gradlew.bat build -x test --no-daemon

$jarFiles = Get-ChildItem -Path "build/libs" -Filter "*.jar" | Where-Object { $_.Name -notlike "*plain*" }
if ($jarFiles.Count -eq 0) {
    Write-Error "Build falhou - JAR não encontrado"
    exit 1
}

$jarFile = $jarFiles[0]
Write-Success "Build concluído: $jarFile.Name"

# ============================================
# ETAPA 3: Executar testes
# ============================================
Write-Info "Etapa 3: Executando testes"

Write-Info "Executando testes unitários..."
& ./gradlew.bat test --tests "*unit*" --no-daemon

Write-Info "Executando testes de integração..."
& ./gradlew.bat test --tests "*integration*" --no-daemon

Write-Info "Executando testes de aceitação..."
& ./gradlew.bat test --tests "*acceptance*" --no-daemon

Write-Success "Todos os testes passaram"

# ============================================
# ETAPA 4: Deploy
# ============================================
Write-Info "Etapa 4: Deploy da aplicação"

$deployMethod = if ($args.Count -gt 0) { $args[0] } else { "docker" }

switch ($deployMethod) {
    "docker" {
        Write-Info "Método de deploy: Docker"
        
        if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
            Write-Error "Docker não encontrado. Instale Docker ou use outro método de deploy."
            exit 1
        }
        
        Write-Info "Construindo imagem Docker..."
        docker build -t pipeline-gerencia:latest .
        
        Write-Info "Parando containers existentes (se houver)..."
        docker stop pipeline-gerencia-app 2>$null
        docker rm pipeline-gerencia-app 2>$null
        
        Write-Info "Iniciando container..."
        docker run -d `
            --name pipeline-gerencia-app `
            -p 8080:8080 `
            --restart unless-stopped `
            pipeline-gerencia:latest
        
        Write-Success "Aplicação implantada em Docker"
        Write-Info "Acesse: http://localhost:8080"
    }
    
    "local" {
        Write-Info "Método de deploy: Execução local"
        Write-Warning "A aplicação será executada em primeiro plano. Use Ctrl+C para parar."
        
        java -jar $jarFile.FullName
    }
    
    default {
        Write-Error "Método de deploy inválido: $deployMethod"
        Write-Host ""
        Write-Host "Métodos disponíveis:"
        Write-Host "  docker  - Deploy usando Docker"
        Write-Host "  local   - Executar localmente"
        Write-Host ""
        Write-Host "Uso: .\scripts\deploy.ps1 [docker|local]"
        exit 1
    }
}

# ============================================
# Resumo final
# ============================================
Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan
Write-Success "Deploy concluído com sucesso!"
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "📦 Artefato: $($jarFile.Name)"
Write-Host "🐳 Método: $deployMethod"
Write-Host "🌐 URL: http://localhost:8080"
Write-Host ""

