# 🐳 Guia de Deploy com Docker

Este guia mostra como subir a aplicação Pipeline de Gerência usando Docker.

## 📋 Pré-requisitos

- Docker instalado ([Instalar Docker](https://docs.docker.com/get-docker/))
- Docker Compose instalado (geralmente vem com Docker Desktop)

## 🚀 Opção 1: Usando Docker Compose (Recomendado)

A forma mais simples e recomendada:

### 1. Construir e subir a aplicação

```bash
docker-compose up --build
```

### 2. Executar em background (detached mode)

```bash
docker-compose up -d --build
```

### 3. Ver logs

```bash
docker-compose logs -f
```

### 4. Parar a aplicação

```bash
docker-compose down
```

### 5. Parar e remover volumes

```bash
docker-compose down -v
```

## 🐳 Opção 2: Usando Docker diretamente

### 1. Construir a imagem

```bash
docker build -t pipeline-gerencia:latest .
```

### 2. Executar o container

```bash
docker run -d \
  --name pipeline-gerencia-app \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e JAVA_OPTS="-Xmx512m -Xms256m" \
  --restart unless-stopped \
  pipeline-gerencia:latest
```

### 3. Ver logs

```bash
docker logs -f pipeline-gerencia-app
```

### 4. Verificar status do health check

```bash
docker ps
```

### 5. Parar o container

```bash
docker stop pipeline-gerencia-app
```

### 6. Remover o container

```bash
docker rm pipeline-gerencia-app
```

## ✅ Verificar se está funcionando

Após subir a aplicação, você pode verificar se está funcionando:

### 1. Health Check (Actuator)

```bash
curl http://localhost:8080/actuator/health
```

Resposta esperada:
```json
{"status":"UP"}
```

### 2. Verificar logs do container

```bash
# Com Docker Compose
docker-compose logs

# Com Docker direto
docker logs pipeline-gerencia-app
```

### 3. Acessar via navegador

Abra no navegador: `http://localhost:8080`

## 🔧 Comandos úteis

### Reconstruir a imagem (forçar rebuild)

```bash
# Docker Compose
docker-compose build --no-cache

# Docker direto
docker build --no-cache -t pipeline-gerencia:latest .
```

### Ver containers em execução

```bash
docker ps
```

### Ver todas as imagens

```bash
docker images
```

### Limpar recursos não utilizados

```bash
docker system prune -a
```

### Executar comandos dentro do container

```bash
docker exec -it pipeline-gerencia-app sh
```

## 📊 Monitoramento

### Verificar uso de recursos

```bash
docker stats pipeline-gerencia-app
```

### Verificar health check status

```bash
docker inspect pipeline-gerencia-app | grep -A 10 Health
```

## 🐛 Troubleshooting

### Porta 8080 já está em uso

Se a porta 8080 estiver ocupada, você pode alterar a porta no `docker-compose.yml`:

```yaml
ports:
  - "8081:8080"  # Muda a porta externa para 8081
```

Ou no comando Docker:

```bash
docker run -d -p 8081:8080 --name pipeline-gerencia-app pipeline-gerencia:latest
```

### Container não inicia

1. Verifique os logs:
   ```bash
   docker logs pipeline-gerencia-app
   ```

2. Verifique se o build foi bem-sucedido:
   ```bash
   docker build -t pipeline-gerencia:latest .
   ```

3. Verifique se há conflitos de nome:
   ```bash
   docker ps -a | grep pipeline-gerencia
   ```

### Health check falhando

O health check pode falhar nos primeiros 40 segundos (start period). Aguarde um pouco e verifique novamente:

```bash
curl http://localhost:8080/actuator/health
```

## 📝 Variáveis de Ambiente

Você pode customizar as variáveis de ambiente:

### No docker-compose.yml:

```yaml
environment:
  - SPRING_PROFILES_ACTIVE=prod
  - JAVA_OPTS=-Xmx1024m -Xms512m
```

### No comando Docker:

```bash
docker run -d \
  -e SPRING_PROFILES_ACTIVE=dev \
  -e JAVA_OPTS="-Xmx1024m -Xms512m" \
  -p 8080:8080 \
  pipeline-gerencia:latest
```

## 🎯 Resumo Rápido

```bash
# Subir com Docker Compose
docker-compose up -d --build

# Ver logs
docker-compose logs -f

# Parar
docker-compose down

# Verificar health
curl http://localhost:8080/actuator/health
```

---

**Pronto!** Sua aplicação está rodando em `http://localhost:8080` 🚀
