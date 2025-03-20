# Giai đoạn build
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Thiết lập thư mục làm việc
WORKDIR /workspace/app

# Sao chép tệp cấu hình và mã nguồn vào container
COPY pom.xml ./
COPY src ./src

# Biên dịch ứng dụng mà không chạy test
RUN mvn clean package -DskipTests

# Giai đoạn runtime
FROM openjdk:17-jdk-alpine

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép tệp JAR đã biên dịch từ giai đoạn build
COPY --from=build /workspace/app/target/*.jar app.jar

# Mở cổng ứng dụng
EXPOSE 8080

# Định nghĩa lệnh khởi động mặc định
ENTRYPOINT ["java", "-jar", "app.jar"]

# Lệnh CMD có thể được ghi đè khi chạy container
CMD []