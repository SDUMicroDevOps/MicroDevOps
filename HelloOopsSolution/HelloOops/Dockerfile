FROM mcr.microsoft.com/dotnet/sdk:6.0 AS build-env
WORKDIR /app

EXPOSE 5000

COPY controllers ./
COPY Properties ./
COPY HelloOops.csproj .
COPY index.html .
COPY Program.cs .
COPY Startup.cs .

# Restore as distinct layers
RUN dotnet restore
# Build and publish a release
RUN dotnet publish -c Release -o out

ENTRYPOINT ["dotnet", "out/HelloOops.dll"]