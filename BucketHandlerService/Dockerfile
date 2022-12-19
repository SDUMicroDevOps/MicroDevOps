FROM mcr.microsoft.com/dotnet/sdk:6.0 AS build-env
WORKDIR /App
EXPOSE 5165

# Copy everything
COPY . ./

# Restore as distinct layers
RUN dotnet restore
# Build and publish a release
RUN dotnet publish -c Release -o out

# Build runtime image
FROM mcr.microsoft.com/dotnet/aspnet:6.0
WORKDIR /App
COPY --from=build-env /App/out .
#The following lines are necessary, since GoogleSDK depends on libc6, but it is not installed in gke pod
RUN apt-get update
RUN apt-get install -y libc6-dev

ENTRYPOINT ["dotnet", "BucketHandlerService.dll"]
