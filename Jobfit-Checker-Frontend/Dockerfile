FROM node:23-alpine3.20

WORKDIR /JobFitChecker/Jobfit-Checker-Frontend

COPY package*.json ./

RUN npm install

COPY . .

EXPOSE 3000

ENV HOST=0.0.0.0
ENV DANGEROUSLY_DISABLE_HOST_CHECK=true

CMD ["npm", "start"]