FROM mcr.microsoft.com/dotnet/sdk:6.0 AS build-env
WORKDIR /App
EXPOSE 5165

# Copy everything
COPY . ./

# Restore as distinct layers
RUN dotnet restore
# Build and publish a release
RUN dotnet publish -c Release -o out

#Added this vvvvv to fix the liblibdl.so: cannot open shared object file: No such file or directory at Grpc.Core.Internal.UnmanagedLibrary.Linux.dlopen(String filename, Int32 flags)
RUN sed -i "s@http://deb.debian.org@http://mirrors.aliyun.com@g" /etc/apt/sources.list && \
    sed -i "s@http://security.debian.org@http://mirrors.aliyun.com@g" /etc/apt/sources.list && \
    apt-get update
    
RUN apt-get install -y --allow-unauthenticated \
    apt-utils libgdiplus nano libc6-dev libx11-dev && \
    ln -s /usr/lib/libgdiplus.so /usr/lib/gdiplus.dll

# Build runtime image
FROM mcr.microsoft.com/dotnet/aspnet:6.0
WORKDIR /App
COPY --from=build-env /App/out .
ENTRYPOINT ["dotnet", "BucketHandlerService.dll"]
