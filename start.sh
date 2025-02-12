# Start up the backend.
# =====================
# Permissions may need to be changed in order to run this command.
# The Docker container can also be ran from Visual Studio Code by going to the docker-compose.yml and clicking on 'Run All Services'
# Or the container can be started from the Docker app if using Windows or macOS.
cd backend
docker compose -f './api-service/docker-compose.yml' up -d --build
cd ..

# Start up the frontend.
# =====================
cd frontend
npm install
npm run dev
cd..