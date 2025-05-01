FROM node:18-alpine3.21

WORKDIR /JobFitChecker/Jobfit-Checker-Frontend

COPY package*.json ./

RUN npm install

COPY . .

EXPOSE 3000

CMD ["npm", "start"]