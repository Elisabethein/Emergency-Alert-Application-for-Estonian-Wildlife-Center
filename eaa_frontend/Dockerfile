# Step 1: Build the frontend
FROM node:18-alpine AS build

# Set working directory in the container
#RUN addgroup app && adduser -S app -G app
WORKDIR /app

# Copy package.json and package-lock.json to install dependencies
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy all project files
COPY . .

#RUN npm install -g @vue/cli
#RUN chown -R app:app /app
#USER app
#RUN chmod -R 755 /app/node_modules

# Build the frontend app for production
RUN npm run build

# Step 2: Serve the frontend using a lightweight web server
FROM nginx:alpine

# Copy the build output to the web server's root directory
COPY --from=build /app/dist /usr/share/nginx/html

# Expose the port on which the frontend will run
EXPOSE 8081

# Start Nginx server
CMD ["nginx", "-g", "daemon off;"]
