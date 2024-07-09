FROM node:19.4-bullseye AS build

WORKDIR /react-client

COPY package*.json ./

RUN --mount=type=cache,target=/react-client/.npm \
  npm set cache /react-client/.npm && \
  npm install

COPY . .

COPY src/Constants-web.js ./src/Constants.js

RUN npm run build

FROM nginxinc/nginx-unprivileged:1.23-alpine-perl

# Use COPY --link to avoid breaking cache if we change the second stage base image
COPY --link nginx.conf /etc/nginx/conf.d/default.conf

COPY --link --from=build /react-client/build/ /usr/share/nginx/html

EXPOSE 8080
