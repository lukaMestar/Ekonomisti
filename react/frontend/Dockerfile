# Build stage
FROM node:18-alpine AS build
WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci

# Copy source code
COPY . .

# Build the application
RUN npm run build

# Production stage - copy built files to output directory
FROM alpine:latest
WORKDIR /usr/share/nginx/html
# Copy built files
COPY --from=build /app/dist .

