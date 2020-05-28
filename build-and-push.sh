./mvnw package -Pnative  -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=podman
podman build -f src/main/docker/Dockerfile.native -t quay.io/abattagl/iot-demo-device .
# podman login quay.io
podman push quay.io/abattagl/iot-demo-device
