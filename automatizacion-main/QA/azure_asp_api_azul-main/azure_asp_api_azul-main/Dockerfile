# Imagen base para el entorno de ejecución
FROM mcr.microsoft.com/dotnet/aspnet:7.0 AS base
WORKDIR /app
EXPOSE 80
EXPOSE 443

# Imagen para compilar el proyecto
FROM mcr.microsoft.com/dotnet/sdk:7.0 AS build
WORKDIR /src

# Copiar archivos de proyecto
COPY ["Asp.Api.Azul/Asp.Api.Azul.csproj", "Asp.Api.Azul/"]
COPY ["Asp.Api.Azul.Core/Asp.Api.Azul.Core.csproj", "Asp.Api.Azul.Core/"]
COPY ["Asp.Api.Azul.Infrastructure/Asp.Api.Azul.Infrastructure.csproj", "Asp.Api.Azul.Infrastructure/"]

# Restaurar paquetes
RUN dotnet restore "Asp.Api.Azul/Asp.Api.Azul.csproj"

# Copiar el resto del código
COPY . .

# Construir el proyecto
WORKDIR "/src/Asp.Api.Azul"
RUN dotnet build "Asp.Api.Azul.csproj" -c Release -o /app/build

# Publicar la aplicación
FROM build AS publish
RUN dotnet publish "Asp.Api.Azul.csproj" -c Release -o /app/publish /p:UseAppHost=false

# Imagen final
FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "Asp.Api.Azul.dll"]